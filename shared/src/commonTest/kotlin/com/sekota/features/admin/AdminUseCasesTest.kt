package com.sekota.features.admin

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.repository.AdminRepository
import com.sekota.features.admin.domain.usecase.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeAdminRepository : AdminRepository {
    val books = mutableListOf<AdminBook>()
    val products = mutableListOf<AdminProduct>()
    val merchandise = mutableListOf<AdminMerch>()
    val inquiries = mutableListOf<ClientInquiry>()
    var liveMetrics = AdminLiveMetrics()

    override suspend fun getBooks(): List<AdminBook> = books.toList()
    override suspend fun getBookById(id: String): AdminBook? = books.firstOrNull { it.id == id }
    override suspend fun saveBook(book: AdminBook): Result<AdminBook> {
        books.removeAll { it.id == book.id }
        books.add(book)
        return Result.success(book)
    }
    override suspend fun deleteBook(id: String): Result<Boolean> {
        val removed = books.removeAll { it.id == id }
        return Result.success(removed)
    }

    override suspend fun getProducts(): List<AdminProduct> = products.toList()
    override suspend fun saveProduct(product: AdminProduct): Result<AdminProduct> {
        products.removeAll { it.id == product.id }
        products.add(product)
        return Result.success(product)
    }
    override suspend fun deleteProduct(id: String): Result<Boolean> {
        val removed = products.removeAll { it.id == id }
        return Result.success(removed)
    }

    override suspend fun getMerchandise(): List<AdminMerch> = merchandise.toList()
    override suspend fun saveMerchandise(merch: AdminMerch): Result<AdminMerch> {
        merchandise.removeAll { it.id == merch.id }
        merchandise.add(merch)
        return Result.success(merch)
    }
    override suspend fun deleteMerchandise(id: String): Result<Boolean> {
        val removed = merchandise.removeAll { it.id == id }
        return Result.success(removed)
    }

    override suspend fun getLiveMetrics(): AdminLiveMetrics = liveMetrics
    override suspend fun saveLiveMetrics(metrics: AdminLiveMetrics): Result<AdminLiveMetrics> {
        liveMetrics = metrics
        return Result.success(metrics)
    }

    override suspend fun getInquiries(): List<ClientInquiry> = inquiries.toList()
    override suspend fun saveInquiry(inquiry: ClientInquiry): Result<ClientInquiry> {
        inquiries.removeAll { it.id == inquiry.id }
        inquiries.add(inquiry)
        return Result.success(inquiry)
    }
    override suspend fun deleteInquiry(id: String): Result<Boolean> {
        val removed = inquiries.removeAll { it.id == id }
        return Result.success(removed)
    }
}

class AdminUseCasesTest {

    @Test
    fun testBookValidationAndSave() = runTest {
        val repo = FakeAdminRepository()
        val saveUseCase = SaveAdminBookUseCase(repo)
        val getUseCase = GetAdminBooksUseCase(repo)

        // 1. Validation error when title is blank
        val invalidBook = AdminBook("b1", "", "Author", "ISBN123")
        val failResult = saveUseCase(invalidBook)
        assertTrue(failResult.isFailure)
        assertEquals("Judul buku tidak boleh kosong", failResult.exceptionOrNull()?.message)

        // 2. Success save
        val validBook = AdminBook("b1", "KMP in Production", "Sekota Dev", "ISBN-001")
        val successResult = saveUseCase(validBook)
        assertTrue(successResult.isSuccess)
        assertEquals("KMP in Production", getUseCase().first().title)
    }

    @Test
    fun testBookDelete() = runTest {
        val repo = FakeAdminRepository()
        repo.books.add(AdminBook("b1", "Book 1", "Author 1", "ISBN-1"))
        val deleteUseCase = DeleteAdminBookUseCase(repo)
        val getUseCase = GetAdminBooksUseCase(repo)

        val result = deleteUseCase("b1")
        assertTrue(result.isSuccess)
        assertEquals(0, getUseCase().size)
    }

    @Test
    fun testProductValidationAndSave() = runTest {
        val repo = FakeAdminRepository()
        val saveProductUseCase = SaveAdminProductUseCase(repo)
        val getProductUseCase = GetAdminProductsUseCase(repo)

        val blankProduct = AdminProduct("p1", "VRD", "", "Suite", "Desc", emptyList())
        val failResult = saveProductUseCase(blankProduct)
        assertTrue(failResult.isFailure)

        val validProduct = AdminProduct("p1", "VRD", "Veridia", "Suite", "Desc", listOf("Insight"))
        val successResult = saveProductUseCase(validProduct)
        assertTrue(successResult.isSuccess)
        assertEquals(1, getProductUseCase().size)
        assertEquals("Veridia", getProductUseCase().first().name)
    }

    @Test
    fun testMerchandisePriceValidation() = runTest {
        val repo = FakeAdminRepository()
        val saveMerchUseCase = SaveAdminMerchUseCase(repo)

        val negativePriceMerch = AdminMerch("m1", "Sekota Mug", "Accessories", "Core", -10.0, 5.0)
        val failResult = saveMerchUseCase(negativePriceMerch)
        assertTrue(failResult.isFailure)
        assertEquals("Harga merchandise harus lebih dari 0", failResult.exceptionOrNull()?.message)
    }

    @Test
    fun testLiveMetricsValidationAndSave() = runTest {
        val repo = FakeAdminRepository()
        val saveMetricsUseCase = SaveAdminLiveMetricsUseCase(repo)
        val getMetricsUseCase = GetAdminLiveMetricsUseCase(repo)

        val blankMetrics = AdminLiveMetrics(dataAccuracy = "")
        val failResult = saveMetricsUseCase(blankMetrics)
        assertTrue(failResult.isFailure)

        val validMetrics = AdminLiveMetrics("99.9%", "250+", "2020")
        val successResult = saveMetricsUseCase(validMetrics)
        assertTrue(successResult.isSuccess)
        assertEquals("99.9%", getMetricsUseCase().dataAccuracy)
        assertEquals("250+", getMetricsUseCase().totalClients)
        assertEquals("2020", getMetricsUseCase().establishedYear)
    }

    @Test
    fun testValidateAdminRole() {
        val validateRole = ValidateAdminRoleUseCase()

        assertTrue(validateRole("ADMIN"))
        assertTrue(validateRole("admin"))
        assertTrue(validateRole("BOD"))
        assertTrue(validateRole("SYSADMIN"))

        kotlin.test.assertFalse(validateRole("READER"))
        kotlin.test.assertFalse(validateRole("CLIENT"))
        kotlin.test.assertFalse(validateRole(""))
        kotlin.test.assertFalse(validateRole(null))
    }
}

