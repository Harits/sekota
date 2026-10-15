package com.sekota.features.admin

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.WhySekotaCard
import com.sekota.features.admin.domain.model.WhySekotaConfig
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

    var valueLoop = com.sekota.features.admin.domain.model.ValueLoopConfig()
    override suspend fun getValueLoop(): com.sekota.features.admin.domain.model.ValueLoopConfig = valueLoop
    override suspend fun saveValueLoop(config: com.sekota.features.admin.domain.model.ValueLoopConfig): Result<com.sekota.features.admin.domain.model.ValueLoopConfig> {
        valueLoop = config
        return Result.success(config)
    }

    var categories = mutableListOf("SMART CITY", "ESG", "INTELLIGENCE", "Other")
    override suspend fun getCategories(): List<String> = categories
    override suspend fun addCategory(category: String): Result<List<String>> {
        if (!categories.contains(category)) categories.add(category)
        return Result.success(categories)
    }
    override suspend fun deleteCategory(category: String): Result<List<String>> {
        categories.removeAll { it.equals(category, ignoreCase = true) }
        return Result.success(categories)
    }

    var whySekota = WhySekotaConfig()
    override suspend fun getWhySekota(): WhySekotaConfig = whySekota
    override suspend fun saveWhySekota(config: WhySekotaConfig): Result<WhySekotaConfig> {
        whySekota = config
        return Result.success(config)
    }

    var strategicConversation = StrategicConversationConfig()
    override suspend fun getStrategicConversation(): StrategicConversationConfig = strategicConversation
    override suspend fun saveStrategicConversation(config: StrategicConversationConfig): Result<StrategicConversationConfig> {
        strategicConversation = config
        return Result.success(config)
    }

    var footer = com.sekota.features.admin.domain.model.FooterConfig()
    override suspend fun getFooter(): com.sekota.features.admin.domain.model.FooterConfig = footer
    override suspend fun saveFooter(config: com.sekota.features.admin.domain.model.FooterConfig): Result<com.sekota.features.admin.domain.model.FooterConfig> {
        footer = config
        return Result.success(config)
    }

    var navigationItems = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS.toMutableList()
    override suspend fun getNavigationItems(): List<com.sekota.features.navigation.domain.model.NavigationItem> = navigationItems.toList()
    override suspend fun saveNavigationItems(items: List<com.sekota.features.navigation.domain.model.NavigationItem>): Result<List<com.sekota.features.navigation.domain.model.NavigationItem>> {
        navigationItems = items.toMutableList()
        return Result.success(items)
    }

    val readingProgressMap = mutableMapOf<String, com.sekota.features.reader.domain.model.BookReadingProgress>()
    val recordedTelemetry = mutableListOf<Triple<String, Int, String>>()

    override suspend fun saveReadingProgress(progress: com.sekota.features.reader.domain.model.BookReadingProgress): Result<Unit> {
        readingProgressMap[progress.bookId] = progress
        return Result.success(Unit)
    }

    override suspend fun getReadingProgress(bookId: String): com.sekota.features.reader.domain.model.BookReadingProgress? {
        return readingProgressMap[bookId]
    }

    override suspend fun recordReadingTelemetry(bookId: String, chapterIndex: Int, chapterTitle: String): Result<Boolean> {
        recordedTelemetry.add(Triple(bookId, chapterIndex, chapterTitle))
        return Result.success(true)
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

    @Test
    fun testValueLoopUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getUseCase = GetAdminValueLoopUseCase(repo)
        val saveUseCase = SaveAdminValueLoopUseCase(repo)

        val initial = getUseCase()
        assertEquals("Strategic Value Loop", initial.title)
        assertEquals(5, initial.steps.size)

        val updated = initial.copy(
            title = "Updated Value Loop",
            steps = listOf(
                com.sekota.features.admin.domain.model.AdminValueLoopStep("01", "DISCOVERY", "Analisis Awal")
            )
        )
        val saveResult = saveUseCase(updated)
        assertTrue(saveResult.isSuccess)

        val fetched = getUseCase()
        assertEquals("Updated Value Loop", fetched.title)
        assertEquals(1, fetched.steps.size)
        assertEquals("DISCOVERY", fetched.steps.first().title)

        // Validation failures
        val emptyTitleResult = saveUseCase(updated.copy(title = "   "))
        assertTrue(emptyTitleResult.isFailure)

        val emptyStepsResult = saveUseCase(updated.copy(steps = emptyList()))
        assertTrue(emptyStepsResult.isFailure)
    }

    @Test
    fun testCategoryUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getCategoriesUseCase = GetAdminCategoriesUseCase(repo)
        val addCategoryUseCase = AddAdminCategoryUseCase(repo)
        val deleteCategoryUseCase = DeleteAdminCategoryUseCase(repo)

        // 1. Initial categories
        val initial = getCategoriesUseCase()
        assertEquals(4, initial.size)
        assertTrue(initial.contains("ESG"))

        // 2. Add category
        val addResult = addCategoryUseCase("ARTIFICIAL INTELLIGENCE")
        assertTrue(addResult.isSuccess)
        val updatedAfterAdd = getCategoriesUseCase()
        assertEquals(5, updatedAfterAdd.size)
        assertTrue(updatedAfterAdd.contains("ARTIFICIAL INTELLIGENCE"))

        // 3. Add blank category validation failure
        val blankResult = addCategoryUseCase("   ")
        assertTrue(blankResult.isFailure)
        assertEquals("Nama kategori tidak boleh kosong", blankResult.exceptionOrNull()?.message)

        // 4. Delete category
        val deleteResult = deleteCategoryUseCase("Other")
        assertTrue(deleteResult.isSuccess)
        val updatedAfterDelete = getCategoriesUseCase()
        assertEquals(4, updatedAfterDelete.size)
        kotlin.test.assertFalse(updatedAfterDelete.contains("Other"))

        // 5. Delete blank category validation failure
        val blankDeleteResult = deleteCategoryUseCase("")
        assertTrue(blankDeleteResult.isFailure)
        assertEquals("Nama kategori tidak valid", blankDeleteResult.exceptionOrNull()?.message)
    }

    @Test
    fun testWhySekotaUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getUseCase = GetAdminWhySekotaUseCase(repo)
        val saveUseCase = SaveAdminWhySekotaUseCase(repo)

        // 1. Initial default state
        val initial = getUseCase()
        assertEquals("MENGAPA SEKOTA", initial.preTitle)
        assertEquals("Sekota Bukan Sekadar Vendor, Bukan Sekadar Konsultan.", initial.title)
        assertEquals(2, initial.cards.size)
        assertEquals("c1", initial.cards[0].id)
        assertEquals("trending", initial.cards[0].iconName)

        // 2. Save valid updated config
        val updated = initial.copy(
            title = "Inovasi Berkelanjutan Sekota",
            cards = listOf(
                WhySekotaCard("c1", "Kemitraan Jangka Panjang", "Kolaborasi nyata berkesinambungan", "shield"),
                WhySekotaCard("c2", "Kecepatan dan Skalabilitas", "Arsitektur cloud teruji", "sparkle")
            )
        )
        val saveResult = saveUseCase(updated)
        assertTrue(saveResult.isSuccess)

        val fetched = getUseCase()
        assertEquals("Inovasi Berkelanjutan Sekota", fetched.title)
        assertEquals(2, fetched.cards.size)
        assertEquals("shield", fetched.cards[0].iconName)

        // 3. Validation failure: empty title
        val blankTitleResult = saveUseCase(updated.copy(title = "   "))
        assertTrue(blankTitleResult.isFailure)
        assertEquals("Judul 'Why Sekota' tidak boleh kosong", blankTitleResult.exceptionOrNull()?.message)

        // 4. Validation failure: empty cards
        val emptyCardsResult = saveUseCase(updated.copy(cards = emptyList()))
        assertTrue(emptyCardsResult.isFailure)
        assertEquals("Kartu 'Why Sekota' minimal 1 kartu", emptyCardsResult.exceptionOrNull()?.message)

        // 5. Validation failure: card with blank title
        val blankCardTitleResult = saveUseCase(updated.copy(cards = listOf(WhySekotaCard("c1", "   ", "Deskripsi", "trending"))))
        assertTrue(blankCardTitleResult.isFailure)
        assertEquals("Judul setiap kartu tidak boleh kosong", blankCardTitleResult.exceptionOrNull()?.message)
    }

    @Test
    fun testStrategicConversationUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getUseCase = GetAdminStrategicConversationUseCase(repo)
        val saveUseCase = SaveAdminStrategicConversationUseCase(repo)

        // 1. Initial default state
        val initial = getUseCase()
        assertEquals("Mulai Percakapan\nStrategis.", initial.title)
        assertEquals("sales@sekota.id", initial.email)
        assertEquals("Kirim & Mulai Konsultasi", initial.submitButtonLabel)

        // 2. Save valid updated config
        val updated = initial.copy(
            title = "Hubungi Sekota Hari Ini",
            email = "contact@sekota.id",
            submitButtonLabel = "Mulai Diskusi"
        )
        val saveResult = saveUseCase(updated)
        assertTrue(saveResult.isSuccess)

        val fetched = getUseCase()
        assertEquals("Hubungi Sekota Hari Ini", fetched.title)
        assertEquals("contact@sekota.id", fetched.email)
        assertEquals("Mulai Diskusi", fetched.submitButtonLabel)

        // 3. Validation failure: blank title
        val blankTitleResult = saveUseCase(updated.copy(title = "   "))
        assertTrue(blankTitleResult.isFailure)
        assertEquals("Judul percakapan strategis tidak boleh kosong", blankTitleResult.exceptionOrNull()?.message)

        // 4. Validation failure: blank email
        val blankEmailResult = saveUseCase(updated.copy(email = ""))
        assertTrue(blankEmailResult.isFailure)
        assertEquals("Email tidak boleh kosong", blankEmailResult.exceptionOrNull()?.message)

        // 5. Validation failure: blank submitButtonLabel
        val blankButtonResult = saveUseCase(updated.copy(submitButtonLabel = "   "))
        assertTrue(blankButtonResult.isFailure)
        assertEquals("Label tombol kirim tidak boleh kosong", blankButtonResult.exceptionOrNull()?.message)
    }

    @Test
    fun testFooterUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getUseCase = GetAdminFooterUseCase(repo)
        val saveUseCase = SaveAdminFooterUseCase(repo)

        // 1. Initial default state
        val initial = getUseCase()
        assertTrue(initial.tagline.contains("Trusted Intelligence"))
        assertEquals("eBdesk Group Affiliation", initial.affiliationText)
        assertEquals(4, initial.productColumn.items.size)
        assertEquals(5, initial.companyColumn.items.size)
        assertEquals(2, initial.legalColumn.items.size)

        // 2. Save valid updated footer
        val updated = initial.copy(
            tagline = "Inovasi Berkelanjutan Sekota 2026",
            copyrightText = "© 2026 PT Sekota."
        )
        val saveResult = saveUseCase(updated)
        assertTrue(saveResult.isSuccess)

        val fetched = getUseCase()
        assertEquals("Inovasi Berkelanjutan Sekota 2026", fetched.tagline)
        assertEquals("© 2026 PT Sekota.", fetched.copyrightText)

        // 3. Validation failure: blank tagline
        val blankTaglineResult = saveUseCase(updated.copy(tagline = "   "))
        assertTrue(blankTaglineResult.isFailure)
        assertEquals("Tagline footer tidak boleh kosong", blankTaglineResult.exceptionOrNull()?.message)

        // 4. Validation failure: blank copyright
        val blankCopyrightResult = saveUseCase(updated.copy(copyrightText = ""))
        assertTrue(blankCopyrightResult.isFailure)
        assertEquals("Copyright footer tidak boleh kosong", blankCopyrightResult.exceptionOrNull()?.message)
    }

    @Test
    fun testNavigationItemsUseCases() = runTest {
        val repo = FakeAdminRepository()
        val getUseCase = GetAdminNavigationItemsUseCase(repo)
        val saveUseCase = SaveAdminNavigationItemsUseCase(repo)

        // 1. Initial canonical items
        val initialItems = getUseCase()
        assertEquals(5, initialItems.size)
        assertEquals("Solusi", initialItems[0].label)
        assertEquals("#solusi", initialItems[0].route)

        // 2. Save updated navigation items
        val customItems = listOf(
            com.sekota.features.navigation.domain.model.NavigationItem("nav_1", "Beranda", "/", order = 1),
            com.sekota.features.navigation.domain.model.NavigationItem("nav_2", "Publikasi", "/catalog", order = 2)
        )
        val saveResult = saveUseCase(customItems)
        assertTrue(saveResult.isSuccess)

        val fetched = getUseCase()
        assertEquals(2, fetched.size)
        assertEquals("Publikasi", fetched[1].label)

        // 3. Validation failure: blank label
        val invalidLabelResult = saveUseCase(listOf(
            com.sekota.features.navigation.domain.model.NavigationItem("nav_x", "", "#test")
        ))
        assertTrue(invalidLabelResult.isFailure)
        assertEquals("Label navigasi tidak boleh kosong", invalidLabelResult.exceptionOrNull()?.message)

        // 4. Validation failure: blank route
        val invalidRouteResult = saveUseCase(listOf(
            com.sekota.features.navigation.domain.model.NavigationItem("nav_y", "Test", "")
        ))
        assertTrue(invalidRouteResult.isFailure)
        assertEquals("Route navigasi tidak boleh kosong", invalidRouteResult.exceptionOrNull()?.message)
    }

    @Test
    fun testReadingProgressAndTelemetryUseCases() = runTest {
        val repo = FakeAdminRepository()
        val saveProgressUseCase = com.sekota.features.reader.domain.usecase.SaveReadingProgressUseCase(repo)
        val getProgressUseCase = com.sekota.features.reader.domain.usecase.GetReadingProgressUseCase(repo)
        val telemetryUseCase = com.sekota.features.reader.domain.usecase.RecordReadingTelemetryUseCase(repo)

        // 1. Initial progress should be null
        val initial = getProgressUseCase("blind-spot-radar")
        assertEquals(null, initial)

        // 2. Save reading progress
        val progress = com.sekota.features.reader.domain.model.BookReadingProgress(
            bookId = "blind-spot-radar",
            chapterIndex = 2,
            percent = 45,
            isBookmarked = true,
            fontSizeSp = 18,
            readingMode = com.sekota.features.reader.domain.model.ReadingMode.NIGHT
        )
        val saveResult = saveProgressUseCase(progress)
        assertTrue(saveResult.isSuccess)

        // 3. Retrieve saved progress
        val loaded = getProgressUseCase("blind-spot-radar")
        assertEquals(2, loaded?.chapterIndex)
        assertEquals(45, loaded?.percent)
        assertEquals(true, loaded?.isBookmarked)
        assertEquals(18, loaded?.fontSizeSp)
        assertEquals(com.sekota.features.reader.domain.model.ReadingMode.NIGHT, loaded?.readingMode)

        // 4. Dispatch telemetry event
        val telemetryResult = telemetryUseCase("blind-spot-radar", 2, "Mindful Tech Integration")
        assertTrue(telemetryResult.isSuccess)
        assertEquals(1, repo.recordedTelemetry.size)
        assertEquals("blind-spot-radar", repo.recordedTelemetry[0].first)
        assertEquals(2, repo.recordedTelemetry[0].second)
        assertEquals("Mindful Tech Integration", repo.recordedTelemetry[0].third)
    }
}

