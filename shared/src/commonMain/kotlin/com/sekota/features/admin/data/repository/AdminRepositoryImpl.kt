package com.sekota.features.admin.data.repository

import com.sekota.NetworkClient
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.repository.AdminRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
private data class ApiAdminBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null
)

class AdminRepositoryImpl : AdminRepository {
    private val inMemoryBooks = mutableListOf(
        AdminBook("b1", "The Kotlin Book", "JetBrains", "123-456-789"),
        AdminBook("b2", "Compose Multiplatform", "Google", "987-654-321")
    )
    private val inMemoryProducts = mutableListOf(
        AdminProduct("p1", "VRD", "Veridia", "Intelligence Suite", "Description for Veridia", listOf("Feature 1", "Feature 2")),
        AdminProduct("p2", "ASC", "Ascendio", "Intelligence Suite", "Description for Ascendio", listOf("Feature A", "Feature B")),
        AdminProduct("p3", "SOC", "Sociara", "Intelligence Suite", "Description for Sociara", listOf("Feature X", "Feature Y")),
        AdminProduct("p4", "ECO", "Ecoflow", "Intelligence Suite", "Description for Ecoflow", listOf("Feature M", "Feature N"))
    )
    private val inMemoryMerch = mutableListOf(
        AdminMerch("m1", "Sekota Hoodie", "Apparel", "Core", 45.0, 4.8),
        AdminMerch("m2", "Sekota Mug", "Accessories", "Core", 15.0, 4.5),
        AdminMerch("m3", "Kotlin Multiplatform T-Shirt", "Apparel", "Dev", 25.0, 4.9)
    )

    override suspend fun getBooks(): List<AdminBook> {
        return try {
            val response: List<ApiAdminBook> = NetworkClient.authClient.get("https://sekota.id/api/v1/admin/books").body()
            response.map {
                AdminBook(
                    id = it.id,
                    title = it.title,
                    author = it.author,
                    isbn = it.isbn,
                    coverImage = it.coverImage
                )
            }
        } catch (e: Exception) {
            inMemoryBooks.toList()
        }
    }

    override suspend fun saveBook(book: AdminBook): Result<AdminBook> {
        val existingIndex = inMemoryBooks.indexOfFirst { it.id == book.id }
        if (existingIndex >= 0) {
            inMemoryBooks[existingIndex] = book
        } else {
            inMemoryBooks.add(book)
        }
        return Result.success(book)
    }

    override suspend fun deleteBook(id: String): Result<Boolean> {
        val removed = inMemoryBooks.removeAll { it.id == id }
        return Result.success(removed)
    }

    override suspend fun getProducts(): List<AdminProduct> {
        return inMemoryProducts.toList()
    }

    override suspend fun saveProduct(product: AdminProduct): Result<AdminProduct> {
        val existingIndex = inMemoryProducts.indexOfFirst { it.id == product.id }
        if (existingIndex >= 0) {
            inMemoryProducts[existingIndex] = product
        } else {
            inMemoryProducts.add(product)
        }
        return Result.success(product)
    }

    override suspend fun deleteProduct(id: String): Result<Boolean> {
        val removed = inMemoryProducts.removeAll { it.id == id }
        return Result.success(removed)
    }

    override suspend fun getMerchandise(): List<AdminMerch> {
        return inMemoryMerch.toList()
    }

    override suspend fun saveMerchandise(merch: AdminMerch): Result<AdminMerch> {
        val existingIndex = inMemoryMerch.indexOfFirst { it.id == merch.id }
        if (existingIndex >= 0) {
            inMemoryMerch[existingIndex] = merch
        } else {
            inMemoryMerch.add(merch)
        }
        return Result.success(merch)
    }

    override suspend fun deleteMerchandise(id: String): Result<Boolean> {
        val removed = inMemoryMerch.removeAll { it.id == id }
        return Result.success(removed)
    }
}
