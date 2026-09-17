package com.sekota.features.admin.data.repository

import com.sekota.AUTH_BASE_URL
import com.sekota.LOCAL_API_BASE_URL
import com.sekota.NetworkClient
import com.sekota.core.storage.AdminDataStorage
import com.sekota.core.storage.TokenStorage
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
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

    private val defaultCanonicalBooks = listOf(
        AdminBook(
            id = "manifesto-ekuitas-lahan",
            title = "Manifesto Ekuitas Lahan",
            author = "Putu Aan J.",
            isbn = "978-623-99999-0-0",
            coverImage = null,
            category = "SMART CITY",
            description = "Strategi dan framework implementasi Smart City dan tata kelola ekuitas lahan perkotaan.",
            rating = 4.9,
            ratingCount = 890,
            pdfUrl = "https://sekota.id/assets/docs/manifesto-ekuitas-lahan.pdf",
            readingTime = "4H 15M",
            pages = 280
        ),
        AdminBook(
            id = "blind-spot-radar",
            title = "Blind Spot Radar",
            author = "Sekota Team",
            isbn = "978-623-99999-3-1",
            coverImage = null,
            category = "INTELLIGENCE",
            description = "Metodologi Open Source Intelligence untuk mendeteksi blind spot kebijakan publik dan fenomena keputusan strategis.",
            rating = 4.8,
            ratingCount = 1240,
            pdfUrl = "https://sekota.id/assets/docs/blind-spot-radar.pdf",
            readingTime = "3H 45M",
            pages = 240
        ),
        AdminBook(
            id = "csr-berdampak",
            title = "CSR Berdampak",
            author = "Sekota Team",
            isbn = "978-623-99999-1-7",
            coverImage = null,
            category = "ESG",
            description = "Panduan penyusunan program tanggung jawab sosial perusahaan berbasis dampak nyata dan keberlanjutan.",
            rating = 4.7,
            ratingCount = 650,
            pdfUrl = "https://sekota.id/assets/docs/csr-berdampak.pdf",
            readingTime = "2H 30M",
            pages = 180
        ),
        AdminBook(
            id = "esg-strategic-integration",
            title = "ESG Strategic Integration",
            author = "Sekota Team",
            isbn = "978-623-99999-2-4",
            coverImage = null,
            category = "ESG",
            description = "Integrasi menyeluruh standar kepatuhan lingkungan, sosial, dan tata kelola korporasi modern.",
            rating = 4.9,
            ratingCount = 1020,
            pdfUrl = "https://sekota.id/assets/docs/esg-strategic-integration.pdf",
            readingTime = "5H 10M",
            pages = 320
        )
    )


    private val defaultCanonicalProducts = listOf(
        AdminProduct("p1", "VRD", "Veridia", "Intelligence Suite", "Description for Veridia", listOf("Feature 1", "Feature 2")),
        AdminProduct("p2", "ASC", "Ascendio", "Intelligence Suite", "Description for Ascendio", listOf("Feature A", "Feature B")),
        AdminProduct("p3", "SOC", "Sociara", "Intelligence Suite", "Description for Sociara", listOf("Feature X", "Feature Y")),
        AdminProduct("p4", "ECO", "Ecoflow", "Intelligence Suite", "Description for Ecoflow", listOf("Feature M", "Feature N"))
    )

    private val defaultCanonicalMerch = listOf(
        AdminMerch("m1", "Sekota Hoodie", "Apparel", "Core", 45.0, 4.8),
        AdminMerch("m2", "Sekota Mug", "Accessories", "Core", 15.0, 4.5),
        AdminMerch("m3", "Kotlin Multiplatform T-Shirt", "Apparel", "Dev", 25.0, 4.9)
    )

    override suspend fun getBooks(): List<AdminBook> {
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
                                year = local?.year ?: "2025"
                            )
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
     */
    private fun preserveTelemetry(incoming: AdminBook, existing: AdminBook): AdminBook =
        incoming.copy(
            rating = existing.rating,
            ratingCount = existing.ratingCount,
            interactions = existing.interactions
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

    override suspend fun getMerchandise(): List<AdminMerch> {
        val storedJson = dataStorage.getMerchJson()
        if (!storedJson.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<AdminMerch>>(storedJson)
            } catch (_: Exception) {
                // Ignore
            }
        }
        persistMerch(defaultCanonicalMerch)
        return defaultCanonicalMerch
    }

    override suspend fun saveMerchandise(merch: AdminMerch): Result<AdminMerch> {
        val current = getMerchandise().toMutableList()
        val index = current.indexOfFirst { it.id == merch.id }
        if (index >= 0) {
            current[index] = merch
        } else {
            current.add(merch)
        }
        persistMerch(current)
        return Result.success(merch)
    }

    override suspend fun deleteMerchandise(id: String): Result<Boolean> {
        val current = getMerchandise().toMutableList()
        val removed = current.removeAll { it.id == id }
        if (removed) {
            persistMerch(current)
        }
        return Result.success(removed)
    }

    private fun persistMerch(merch: List<AdminMerch>) {
        dataStorage.saveMerchJson(json.encodeToString(merch))
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
}

