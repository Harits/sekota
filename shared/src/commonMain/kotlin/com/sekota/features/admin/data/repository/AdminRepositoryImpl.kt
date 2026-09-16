package com.sekota.features.admin.data.repository

import com.sekota.AUTH_BASE_URL
import com.sekota.LOCAL_API_BASE_URL
import com.sekota.NetworkClient
import com.sekota.core.storage.AdminDataStorage
import com.sekota.core.storage.TokenStorage
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
        AdminBook("manifesto-ekuitas-lahan", "Manifesto Ekuitas Lahan", "Putu Aan J.", "978-623-99999-0-0"),
        AdminBook("blind-spot-radar", "Blind Spot Radar", "Sekota Team", "978-623-99999-3-1"),
        AdminBook("csr-berdampak", "CSR Berdampak", "Sekota Team", "978-623-99999-1-7"),
        AdminBook("esg-strategic-integration", "ESG Strategic Integration", "Sekota Team", "978-623-99999-2-4")
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
                        val mapped = response.map {
                            AdminBook(
                                id = it.id,
                                title = it.title,
                                author = it.author,
                                isbn = it.isbn,
                                coverImage = it.coverImage
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
                    val savedBook = AdminBook(
                        id = remoteBook.id,
                        title = remoteBook.title,
                        author = remoteBook.author,
                        isbn = remoteBook.isbn,
                        coverImage = remoteBook.coverImage
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

    private fun updateLocalBookCache(book: AdminBook) {
        val current = (dataStorage.getBooksJson()?.let {
            try { json.decodeFromString<List<AdminBook>>(it) } catch (_: Exception) { null }
        } ?: defaultCanonicalBooks).toMutableList()

        val index = current.indexOfFirst { it.id == book.id }
        if (index >= 0) {
            current[index] = book
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
}
