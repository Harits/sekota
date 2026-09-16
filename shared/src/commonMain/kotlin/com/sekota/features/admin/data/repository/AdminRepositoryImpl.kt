package com.sekota.features.admin.data.repository

import com.sekota.NetworkClient
import com.sekota.core.storage.AdminDataStorage
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.repository.AdminRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
private data class ApiAdminBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null
)

class AdminRepositoryImpl(
    private val dataStorage: AdminDataStorage = AdminDataStorage()
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
        // 1. Try fetching from remote network gateway if available
        try {
            val response: List<ApiAdminBook> = NetworkClient.authClient.get("https://sekota.id/api/v1/admin/books").body()
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
        } catch (_: Exception) {
            // Fallback to local persistent layer
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
        val current = getBooks().toMutableList()
        val index = current.indexOfFirst { it.id == book.id }
        if (index >= 0) {
            current[index] = book
        } else {
            current.add(book)
        }
        persistBooks(current)
        return Result.success(book)
    }

    override suspend fun deleteBook(id: String): Result<Boolean> {
        val current = getBooks().toMutableList()
        val removed = current.removeAll { it.id == id }
        if (removed) {
            persistBooks(current)
        }
        return Result.success(removed)
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
}
