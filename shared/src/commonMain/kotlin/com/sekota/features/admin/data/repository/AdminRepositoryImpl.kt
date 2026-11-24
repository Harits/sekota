package com.sekota.features.admin.data.repository

import com.sekota.AUTH_BASE_URL
import com.sekota.LOCAL_API_BASE_URL
import com.sekota.NetworkClient
import com.sekota.core.storage.AdminDataStorage
import com.sekota.core.storage.TokenStorage
import com.sekota.features.admin.data.seed.CompanyProfileSeed
import com.sekota.features.admin.domain.model.AboutConfig
import com.sekota.features.admin.domain.model.Faq
import com.sekota.features.admin.domain.model.PartnerCtaConfig
import com.sekota.features.admin.domain.model.ServiceOffering
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.WhySekotaConfig
import com.sekota.features.admin.domain.repository.AdminRepository
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RemoteBookDto(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null,
    val qrLogo: String? = null
)

@Serializable
data class RemoteBookSummaryDto(
    val bookId: String,
    val title: String,
    val author: String,
    val totalInteractions: Int,
    val averageEngagementScore: Double,
    val highValueLeads: Int
)

@Serializable
data class RemoteUserActivityDto(
    val email: String,
    val bookTitle: String,
    val toolName: String,
    val timestamp: String = "",
    val data: String = ""
)

@Serializable
data class RemoteDashboardSummaryDto(
    val books: List<RemoteBookSummaryDto> = emptyList(),
    val totalReaders: Int = 0,
    val timestamp: String = "",
    val activeUsers: List<String> = emptyList(),
    val recentActivities: List<RemoteUserActivityDto> = emptyList()
)

@Serializable
data class CreateBookRequestDto(
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null,
    val qrLogo: String? = null
)

class AdminRepositoryImpl(
    private val dataStorage: AdminDataStorage = AdminDataStorage(),
    private val tokenStorage: TokenStorage = TokenStorage()
) : AdminRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    // Canonical content lives in the data-layer seed (CORE-07-01-03 / #22658).
    private val defaultCanonicalBooks: List<AdminBook> get() = CompanyProfileSeed.books
    private val defaultCanonicalProducts: List<AdminProduct> get() = CompanyProfileSeed.products

    /**
     * Web presentation fields are owned by the CMS, not by bookinteractiontool, so a
     * remote refresh must carry them over from the local record (or the seed).
     */
    private fun AdminBook.withEnrichmentFrom(source: AdminBook?): AdminBook {
        if (source == null) return this
        return copy(
            whatsInside = source.whatsInside,
            coverColorHex = source.coverColorHex,
            slug = source.slug,
            tagline = source.tagline,
            flowLabel = source.flowLabel,
            blurb = source.blurb,
            audience = source.audience,
            seriesId = source.seriesId,
            coverAsset = source.coverAsset,
            sortOrder = source.sortOrder
        )
    }

    // ---------------------------------------------------------------------------------
    // One-time content migration to the company profile schema (CORE-07-01-03 / #22658)
    // ---------------------------------------------------------------------------------
    private var contentSchemaChecked = false

    /**
     * Brings locally stored content written by the previous design up to
     * [CompanyProfileSeed.CONTENT_SCHEMA_VERSION]. Runs once per store:
     *  - products: replaced by the new roster (Clarivio, Ascendio, Sociara, Evidara);
     *  - books: new six-book catalogue; telemetry, covers and PDF links of books that
     *    still exist are kept, retired seed books are dropped, unknown books are kept;
     *  - Cara Kerja, main navigation and footer: reset to the new structure;
     *  - contact email: moved to info@sekota.id only if it still holds the old default.
     * Services, FAQ, About and Partner CTA are seeded lazily by their getters.
     */
    private fun ensureContentSchema() {
        if (contentSchemaChecked) return
        contentSchemaChecked = true
        val stored = dataStorage.getJson(KEY_CONTENT_SCHEMA)?.trim()?.toIntOrNull() ?: 1
        if (stored >= CompanyProfileSeed.CONTENT_SCHEMA_VERSION) return

        persistProducts(CompanyProfileSeed.products)

        val existingBooks = dataStorage.getBooksJson()?.let {
            runCatching { json.decodeFromString<List<AdminBook>>(it) }.getOrNull()
        }.orEmpty()
        if (existingBooks.isNotEmpty()) {
            val byId = existingBooks.associateBy { it.id }
            val seeded = CompanyProfileSeed.books.map { seed ->
                val old = byId[seed.id] ?: return@map seed
                seed.copy(
                    rating = old.rating,
                    ratingCount = old.ratingCount,
                    interactions = old.interactions,
                    coverImage = old.coverImage ?: seed.coverImage,
                    pdfUrl = old.pdfUrl ?: seed.pdfUrl,
                    isbn = old.isbn.ifBlank { seed.isbn }
                )
            }
            val seedIds = CompanyProfileSeed.books.map { it.id }.toSet()
            val extra = existingBooks.filter { it.id !in seedIds && it.id !in CompanyProfileSeed.RETIRED_BOOK_IDS }
            persistBooks(seeded + extra)
        }

        persistValueLoop(com.sekota.features.admin.domain.model.ValueLoopConfig())
        persistNavigationItems(com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS)
        persistFooter(com.sekota.features.admin.domain.model.FooterConfig())

        dataStorage.getContactConfigJson()?.let { raw ->
            runCatching { json.decodeFromString<StrategicConversationConfig>(raw) }.getOrNull()
        }?.takeIf { it.email == LEGACY_CONTACT_EMAIL }?.let {
            persistStrategicConversation(it.copy(email = StrategicConversationConfig().email))
        }

        dataStorage.saveJson(KEY_CONTENT_SCHEMA, CompanyProfileSeed.CONTENT_SCHEMA_VERSION.toString())
    }

    override suspend fun getBooks(): List<AdminBook> {
        ensureContentSchema()
        // 1. Try fetching from live HTTP backend (supporting bookinteractiontool API at AUTH_BASE_URL or LOCAL_API_BASE_URL)
        val token = tokenStorage.getToken()
        val baseUrls = listOf(
            "${AUTH_BASE_URL}admin/books",
            "${LOCAL_API_BASE_URL}api/v1/admin/books",
            "${AUTH_BASE_URL}books"
        )

        val existingLocal = runCatching {
            dataStorage.getBooksJson()?.let { json.decodeFromString<List<AdminBook>>(it) }
        }.getOrNull()?.associateBy { it.id } ?: emptyMap()

        // Attempt to fetch real telemetry metrics (Real Readers, Active Collaborators, & Real Stars) from bookinteractiontool dashboard
        val dashboardSummary = runCatching {
            val dashboardUrls = listOf(
                "${LOCAL_API_BASE_URL}api/v1/dashboard/summary",
                "${AUTH_BASE_URL}dashboard/summary"
            )
            var summary: RemoteDashboardSummaryDto? = null
            for (dUrl in dashboardUrls) {
                try {
                    val resp = NetworkClient.authClient.get(dUrl) {
                        if (token != null) header(HttpHeaders.Authorization, "Bearer $token")
                    }
                    if (resp.status.isSuccess()) {
                        summary = resp.body<RemoteDashboardSummaryDto>()
                        if (summary.books.isNotEmpty()) break
                    }
                } catch (_: Exception) {}
            }
            summary
        }.getOrNull()

        val telemetryMap = dashboardSummary?.books?.associateBy { it.bookId } ?: emptyMap()

        // Map registered readers per book from recentActivities and activeUsers
        val registeredReadersPerBook = mutableMapOf<String, MutableSet<String>>()
        dashboardSummary?.recentActivities?.forEach { activity ->
            val bookId = dashboardSummary.books.find { it.title.equals(activity.bookTitle, ignoreCase = true) }?.bookId
                ?: activity.bookTitle.lowercase().replace(" ", "-")
            registeredReadersPerBook.getOrPut(bookId) { mutableSetOf() }.add(activity.email)
        }

        for (url in baseUrls) {
            try {
                val httpResponse = NetworkClient.authClient.get(url) {
                    if (token != null) {
                        header(HttpHeaders.Authorization, "Bearer $token")
                    }
                }
                if (httpResponse.status.isSuccess()) {
                    val response = httpResponse.body<List<RemoteBookDto>>()
                    if (response.isNotEmpty()) {
                        val mapped = response.map { remote ->
                            val local = existingLocal[remote.id]
                            val telemetry = telemetryMap[remote.id]

                            // Real star computation: strictly derived from telemetry averageEngagementScore (0-100% -> 1.0-5.0 scale)
                            val derivedStar = telemetry?.averageEngagementScore?.let { score ->
                                if (score > 0.0) {
                                    val computed = 1.0 + (score / 100.0) * 4.0
                                    (kotlin.math.round(computed * 10.0) / 10.0).coerceIn(1.0, 5.0)
                                } else 0.0
                            } ?: 0.0

                            // Real Registered Readers (Active Collaborator Accounts like RP-2026-ZQAX)
                            val registeredCount = registeredReadersPerBook[remote.id]?.size
                                ?: if (remote.id == "manifesto-ekuitas-lahan" && dashboardSummary?.activeUsers?.isNotEmpty() == true) {
                                    dashboardSummary.activeUsers.size
                                } else 0

                            // Total Raw Interactions from telemetry
                            val totalInteractions = telemetry?.totalInteractions ?: 0

                            val defaultCategory = when (remote.id) {
                                "blind-spot-radar" -> "INTELLIGENCE"
                                "manifesto-ekuitas-lahan" -> "SMART CITY"
                                "csr-berdampak", "esg-strategic-integration" -> "ESG"
                                else -> "SMART CITY"
                            }

                            AdminBook(
                                id = remote.id,
                                title = remote.title,
                                author = remote.author,
                                isbn = remote.isbn,
                                coverImage = remote.coverImage ?: local?.coverImage,
                                category = local?.category?.takeIf { it.isNotBlank() } ?: defaultCategory,
                                description = local?.description ?: "",
                                rating = derivedStar,
                                ratingCount = registeredCount,
                                interactions = totalInteractions,
                                pdfUrl = local?.pdfUrl,
                                readingTime = local?.readingTime ?: "3H 45M",
                                pages = local?.pages ?: 240,
                                publishedDate = local?.publishedDate ?: "Nov 2025",
                                language = local?.language ?: "Indonesia",
                                year = local?.year ?: "2025",
                                categoryEn = local?.categoryEn,
                                descriptionEn = local?.descriptionEn
                            ).withEnrichmentFrom(local ?: defaultCanonicalBooks.firstOrNull { it.id == remote.id })
                        }
                        persistBooks(mapped)
                        return mapped
                    }
                }
            } catch (_: Exception) {
                // Continue trying next candidate url or fallback
            }
        }

        // 2. Read from persistent local disk storage
        val storedJson = dataStorage.getBooksJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<AdminBook>>(storedJson)
            } catch (_: Exception) {
                // Fallback to default canonical seed if corrupt
            }
        }

        // 3. Seed default canonical books and persist
        persistBooks(defaultCanonicalBooks)
        return defaultCanonicalBooks
    }

    override suspend fun getBookById(id: String): AdminBook? {
        val books = getBooks()
        return books.firstOrNull { it.id == id || it.id.equals(id, ignoreCase = true) }
    }

    override suspend fun saveBook(book: AdminBook): Result<AdminBook> {
        val token = tokenStorage.getToken()
        val candidateUrls = listOf(
            "${AUTH_BASE_URL}admin/books",
            "${LOCAL_API_BASE_URL}api/v1/admin/books"
        )

        // Attempt remote save via POST (per BookRoutes in bookinteractiontool)
        for (url in candidateUrls) {
            try {
                val httpResponse = NetworkClient.authClient.post(url) {
                    contentType(ContentType.Application.Json)
                    if (token != null) {
                        header(HttpHeaders.Authorization, "Bearer $token")
                    }
                    setBody(CreateBookRequestDto(
                        title = book.title,
                        author = book.author,
                        isbn = book.isbn,
                        coverImage = book.coverImage
                    ))
                }
                if (httpResponse.status.isSuccess()) {
                    val remoteBook = httpResponse.body<RemoteBookDto>()
                    val savedBook = book.copy(
                        id = remoteBook.id,
                        title = remoteBook.title,
                        author = remoteBook.author,
                        isbn = remoteBook.isbn,
                        coverImage = remoteBook.coverImage ?: book.coverImage
                    )
                    updateLocalBookCache(savedBook)
                    return Result.success(savedBook)
                }
            } catch (_: Exception) {
                // Fallback to local persistent storage
            }
        }

        // Local persistent update
        updateLocalBookCache(book)
        return Result.success(book)
    }

    /**
     * Telemetry fields are owned by the bookinteractiontool engine, never by the
     * CMS operator (architecture rule 6). A save carries whatever the caller
     * happened to hold, so the stored values always win for these three -- this
     * is what stops an edit from resetting a book's rating and reader counts.
     * Also cleanly preserves or applies bilingual fields (categoryEn, descriptionEn).
     */
    private fun preserveTelemetry(incoming: AdminBook, existing: AdminBook): AdminBook =
        incoming.copy(
            rating = existing.rating,
            ratingCount = existing.ratingCount,
            interactions = existing.interactions,
            categoryEn = incoming.categoryEn ?: existing.categoryEn,
            descriptionEn = incoming.descriptionEn ?: existing.descriptionEn
        )

    private fun updateLocalBookCache(book: AdminBook) {
        val current = (dataStorage.getBooksJson()?.let {
            try { json.decodeFromString<List<AdminBook>>(it) } catch (_: Exception) { null }
        } ?: defaultCanonicalBooks).toMutableList()

        val index = current.indexOfFirst { it.id == book.id }
        if (index >= 0) {
            current[index] = preserveTelemetry(book, current[index])
        } else {
            current.add(book)
        }
        persistBooks(current)
    }

    override suspend fun deleteBook(id: String): Result<Boolean> {
        val token = tokenStorage.getToken()
        val candidateUrls = listOf(
            "${AUTH_BASE_URL}admin/books/$id",
            "${LOCAL_API_BASE_URL}api/v1/admin/books/$id"
        )

        // Attempt remote delete via DELETE
        for (url in candidateUrls) {
            try {
                val httpResponse = NetworkClient.authClient.delete(url) {
                    if (token != null) {
                        header(HttpHeaders.Authorization, "Bearer $token")
                    }
                }
                if (httpResponse.status.isSuccess()) {
                    deleteFromLocalBookCache(id)
                    return Result.success(true)
                }
            } catch (_: Exception) {
                // Fallback
            }
        }

        // Local persistent delete
        val removed = deleteFromLocalBookCache(id)
        return Result.success(removed)
    }

    private fun deleteFromLocalBookCache(id: String): Boolean {
        val current = (dataStorage.getBooksJson()?.let {
            try { json.decodeFromString<List<AdminBook>>(it) } catch (_: Exception) { null }
        } ?: defaultCanonicalBooks).toMutableList()

        val removed = current.removeAll { it.id == id }
        if (removed) {
            persistBooks(current)
        }
        return removed
    }

    private fun persistBooks(books: List<AdminBook>) {
        val encoded = json.encodeToString(books)
        dataStorage.saveBooksJson(encoded)
    }

    override suspend fun getProducts(): List<AdminProduct> {
        ensureContentSchema()
        val storedJson = dataStorage.getProductsJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<AdminProduct>>(storedJson)
            } catch (_: Exception) {
                // Ignore
            }
        }
        persistProducts(defaultCanonicalProducts)
        return defaultCanonicalProducts
    }

    override suspend fun saveProduct(product: AdminProduct): Result<AdminProduct> {
        val current = getProducts().toMutableList()
        val index = current.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            current[index] = product
        } else {
            current.add(product)
        }
        persistProducts(current)
        return Result.success(product)
    }

    override suspend fun deleteProduct(id: String): Result<Boolean> {
        val current = getProducts().toMutableList()
        val removed = current.removeAll { it.id == id }
        if (removed) {
            persistProducts(current)
        }
        return Result.success(removed)
    }

    private fun persistProducts(products: List<AdminProduct>) {
        dataStorage.saveProductsJson(json.encodeToString(products))
    }

    override suspend fun getLiveMetrics(): AdminLiveMetrics {
        val storedJson = dataStorage.getMetricsJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<AdminLiveMetrics>(storedJson)
            } catch (_: Exception) {
                // Ignore fallback to defaults
            }
        }
        val defaultMetrics = AdminLiveMetrics()
        persistLiveMetrics(defaultMetrics)
        return defaultMetrics
    }

    override suspend fun saveLiveMetrics(metrics: AdminLiveMetrics): Result<AdminLiveMetrics> {
        persistLiveMetrics(metrics)
        return Result.success(metrics)
    }

    private fun persistLiveMetrics(metrics: AdminLiveMetrics) {
        dataStorage.saveMetricsJson(json.encodeToString(metrics))
    }

    override suspend fun getInquiries(): List<ClientInquiry> {
        val storedJson = dataStorage.getInquiriesJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<ClientInquiry>>(storedJson)
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    override suspend fun saveInquiry(inquiry: ClientInquiry): Result<ClientInquiry> {
        val current = getInquiries().toMutableList()
        val index = current.indexOfFirst { it.id == inquiry.id }
        if (index >= 0) {
            current[index] = inquiry
        } else {
            current.add(0, inquiry)
        }
        dataStorage.saveInquiriesJson(json.encodeToString(current))
        return Result.success(inquiry)
    }

    override suspend fun deleteInquiry(id: String): Result<Boolean> {
        val current = getInquiries().toMutableList()
        val removed = current.removeAll { it.id == id }
        if (removed) {
            dataStorage.saveInquiriesJson(json.encodeToString(current))
        }
        return Result.success(removed)
    }

    override suspend fun getValueLoop(): com.sekota.features.admin.domain.model.ValueLoopConfig {
        ensureContentSchema()
        val storedJson = dataStorage.getValueLoopJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<com.sekota.features.admin.domain.model.ValueLoopConfig>(storedJson)
            } catch (_: Exception) {}
        }
        val defaultConfig = com.sekota.features.admin.domain.model.ValueLoopConfig()
        persistValueLoop(defaultConfig)
        return defaultConfig
    }

    override suspend fun saveValueLoop(config: com.sekota.features.admin.domain.model.ValueLoopConfig): Result<com.sekota.features.admin.domain.model.ValueLoopConfig> {
        persistValueLoop(config)
        return Result.success(config)
    }

    private fun persistValueLoop(config: com.sekota.features.admin.domain.model.ValueLoopConfig) {
        dataStorage.saveValueLoopJson(json.encodeToString(config))
    }

    private val defaultCanonicalCategories = listOf("SMART CITY", "ESG", "INTELLIGENCE", "Other")

    override suspend fun getCategories(): List<String> {
        val storedJson = dataStorage.getCategoriesJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                val list = json.decodeFromString<List<String>>(storedJson)
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        persistCategories(defaultCanonicalCategories)
        return defaultCanonicalCategories
    }

    override suspend fun addCategory(category: String): Result<List<String>> {
        val trimmed = category.trim()
        if (trimmed.isBlank()) {
            return Result.failure(IllegalArgumentException("Nama kategori tidak boleh kosong"))
        }
        val current = getCategories().toMutableList()
        val exists = current.any { it.equals(trimmed, ignoreCase = true) }
        if (!exists) {
            current.add(trimmed)
            persistCategories(current)
        }
        return Result.success(current)
    }

    override suspend fun deleteCategory(category: String): Result<List<String>> {
        val trimmed = category.trim()
        val current = getCategories().toMutableList()
        val removed = current.removeAll { it.equals(trimmed, ignoreCase = true) }
        if (removed) {
            persistCategories(current)
        }
        return Result.success(current)
    }

    private fun persistCategories(categories: List<String>) {
        dataStorage.saveCategoriesJson(json.encodeToString(categories))
    }

    override suspend fun getWhySekota(): WhySekotaConfig {
        val storedJson = dataStorage.getWhySekotaJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<WhySekotaConfig>(storedJson)
            } catch (_: Exception) {}
        }
        val defaultConfig = WhySekotaConfig()
        persistWhySekota(defaultConfig)
        return defaultConfig
    }

    override suspend fun saveWhySekota(config: WhySekotaConfig): Result<WhySekotaConfig> {
        persistWhySekota(config)
        return Result.success(config)
    }

    private fun persistWhySekota(config: WhySekotaConfig) {
        dataStorage.saveWhySekotaJson(json.encodeToString(config))
    }

    override suspend fun getStrategicConversation(): StrategicConversationConfig {
        ensureContentSchema()
        val storedJson = dataStorage.getContactConfigJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<StrategicConversationConfig>(storedJson)
            } catch (_: Exception) {}
        }
        val defaultConfig = StrategicConversationConfig()
        persistStrategicConversation(defaultConfig)
        return defaultConfig
    }

    override suspend fun saveStrategicConversation(config: StrategicConversationConfig): Result<StrategicConversationConfig> {
        persistStrategicConversation(config)
        return Result.success(config)
    }

    private fun persistStrategicConversation(config: StrategicConversationConfig) {
        dataStorage.saveContactConfigJson(json.encodeToString(config))
    }

    override suspend fun getFooter(): com.sekota.features.admin.domain.model.FooterConfig {
        ensureContentSchema()
        val storedJson = dataStorage.getFooterConfigJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<com.sekota.features.admin.domain.model.FooterConfig>(storedJson)
            } catch (_: Exception) {}
        }
        val defaultConfig = com.sekota.features.admin.domain.model.FooterConfig()
        persistFooter(defaultConfig)
        return defaultConfig
    }

    override suspend fun saveFooter(config: com.sekota.features.admin.domain.model.FooterConfig): Result<com.sekota.features.admin.domain.model.FooterConfig> {
        persistFooter(config)
        return Result.success(config)
    }

    private fun persistFooter(config: com.sekota.features.admin.domain.model.FooterConfig) {
        dataStorage.saveFooterConfigJson(json.encodeToString(config))
    }

    override suspend fun getNavigationItems(): List<com.sekota.features.navigation.domain.model.NavigationItem> {
        ensureContentSchema()
        val storedJson = dataStorage.getNavigationItemsJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                val items = json.decodeFromString<List<com.sekota.features.navigation.domain.model.NavigationItem>>(storedJson)
                if (items.isNotEmpty()) {
                    return items
                }
            } catch (_: Exception) {}
        }
        val defaultItems = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS
        persistNavigationItems(defaultItems)
        return defaultItems
    }

    override suspend fun saveNavigationItems(items: List<com.sekota.features.navigation.domain.model.NavigationItem>): Result<List<com.sekota.features.navigation.domain.model.NavigationItem>> {
        persistNavigationItems(items)
        return Result.success(items)
    }

    private fun persistNavigationItems(items: List<com.sekota.features.navigation.domain.model.NavigationItem>) {
        dataStorage.saveNavigationItemsJson(json.encodeToString(items))
    }

    // ---------------------------------------------------------------------------------
    // Company profile entities (CORE-07-01-03 / #22658)
    // ---------------------------------------------------------------------------------
    private inline fun <reified T> readDocument(key: String): T? =
        dataStorage.getJson(key)?.takeIf { it.isNotBlank() }?.let {
            runCatching { json.decodeFromString<T>(it) }.getOrNull()
        }

    private inline fun <reified T> writeDocument(key: String, value: T) {
        dataStorage.saveJson(key, json.encodeToString(value))
    }

    override suspend fun getServices(): List<ServiceOffering> {
        readDocument<List<ServiceOffering>>(KEY_SERVICES)?.let { return it.sortedBy { s -> s.sortOrder } }
        writeDocument(KEY_SERVICES, CompanyProfileSeed.services)
        return CompanyProfileSeed.services
    }

    override suspend fun saveService(service: ServiceOffering): Result<ServiceOffering> {
        val current = getServices().filterNot { it.id == service.id } + service
        writeDocument(KEY_SERVICES, current.sortedBy { it.sortOrder })
        return Result.success(service)
    }

    override suspend fun deleteService(id: String): Result<Boolean> {
        val current = getServices()
        val remaining = current.filterNot { it.id == id }
        if (remaining.size != current.size) writeDocument(KEY_SERVICES, remaining)
        return Result.success(remaining.size != current.size)
    }

    override suspend fun getFaqs(): List<Faq> {
        readDocument<List<Faq>>(KEY_FAQS)?.let { return it.sortedBy { f -> f.sortOrder } }
        writeDocument(KEY_FAQS, CompanyProfileSeed.faqs)
        return CompanyProfileSeed.faqs
    }

    override suspend fun saveFaq(faq: Faq): Result<Faq> {
        val current = getFaqs().filterNot { it.id == faq.id } + faq
        writeDocument(KEY_FAQS, current.sortedBy { it.sortOrder })
        return Result.success(faq)
    }

    override suspend fun deleteFaq(id: String): Result<Boolean> {
        val current = getFaqs()
        val remaining = current.filterNot { it.id == id }
        if (remaining.size != current.size) writeDocument(KEY_FAQS, remaining)
        return Result.success(remaining.size != current.size)
    }

    override suspend fun getAbout(): AboutConfig {
        readDocument<AboutConfig>(KEY_ABOUT)?.let { return it }
        writeDocument(KEY_ABOUT, CompanyProfileSeed.about)
        return CompanyProfileSeed.about
    }

    override suspend fun saveAbout(config: AboutConfig): Result<AboutConfig> {
        writeDocument(KEY_ABOUT, config)
        return Result.success(config)
    }

    override suspend fun getPartnerCta(): PartnerCtaConfig {
        readDocument<PartnerCtaConfig>(KEY_PARTNER_CTA)?.let { return it }
        writeDocument(KEY_PARTNER_CTA, CompanyProfileSeed.partnerCta)
        return CompanyProfileSeed.partnerCta
    }

    override suspend fun savePartnerCta(config: PartnerCtaConfig): Result<PartnerCtaConfig> {
        writeDocument(KEY_PARTNER_CTA, config)
        return Result.success(config)
    }

    companion object {
        const val KEY_CONTENT_SCHEMA = "content_schema_version"
        const val KEY_SERVICES = "services"
        const val KEY_FAQS = "faqs"
        const val KEY_ABOUT = "about"
        const val KEY_PARTNER_CTA = "partner_cta"
        private const val LEGACY_CONTACT_EMAIL = "sales@sekota.id"
    }

}

