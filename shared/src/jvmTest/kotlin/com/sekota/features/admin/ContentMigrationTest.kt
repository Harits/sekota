package com.sekota.features.admin

import com.sekota.core.storage.AdminDataStorage
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.data.seed.CompanyProfileSeed
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.ValueLoopConfig
import com.sekota.features.admin.domain.model.AdminValueLoopStep
import com.sekota.features.navigation.domain.model.NavigationItem
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * One-time migration of content stored by the Sprint 6 design (CORE-07-01-04 / #22659).
 * Runs against a temporary directory so it never touches the developer's ~/.sekota.
 */
class ContentMigrationTest {
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var dir: java.io.File
    private var previous: String? = null

    @BeforeTest
    fun setUp() {
        dir = Files.createTempDirectory("sekota-migration").toFile()
        previous = System.getProperty("sekota.storage.dir")
        System.setProperty("sekota.storage.dir", dir.absolutePath)
    }

    @AfterTest
    fun tearDown() {
        if (previous == null) System.clearProperty("sekota.storage.dir") else System.setProperty("sekota.storage.dir", previous!!)
        dir.deleteRecursively()
    }

    private fun seedLegacyStore(storage: AdminDataStorage) {
        storage.saveProductsJson(json.encodeToString(listOf(
            AdminProduct("p1", "VRD", "Veridia", "ESG", "old", emptyList()),
            AdminProduct("p4", "ECO", "Ecoflow", "Eco", "old", emptyList())
        )))
        storage.saveBooksJson(json.encodeToString(listOf(
            AdminBook("blind-spot-radar", "Blind Spot Radar", "Sekota Team", "978-623-99999-3-1",
                rating = 4.8, ratingCount = 1240, interactions = 77, pdfUrl = "https://example.test/bsr.pdf"),
            AdminBook("csr-berdampak", "CSR Berdampak", "Sekota Team", "x"),
            AdminBook("custom-book", "Buku Tambahan CMS", "Tim", "y")
        )))
        storage.saveValueLoopJson(json.encodeToString(ValueLoopConfig(
            title = "Strategic Value Loop",
            steps = listOf(AdminValueLoopStep("01", "INTENT", "Definisi Strategis"))
        )))
        storage.saveNavigationItemsJson(json.encodeToString(listOf(NavigationItem("nav_merchandise", "Merchandise", "/merchandise"))))
        storage.saveContactConfigJson(json.encodeToString(StrategicConversationConfig(email = "sales@sekota.id")))
    }

    @Test
    fun legacyContentIsMigratedOnceAndTelemetryIsKept() = runBlocking {
        val storage = AdminDataStorage()
        seedLegacyStore(storage)
        val repo = AdminRepositoryImpl(dataStorage = storage)

        assertEquals(listOf("CLV", "ASC", "SOC", "EVD"), repo.getProducts().map { it.code })

        // getBooks() would call the live bookinteractiontool API; read the migrated store instead.
        val books = json.decodeFromString<List<AdminBook>>(storage.getBooksJson()!!).associateBy { it.id }
        assertFalse("csr-berdampak" in books, "retired seed book must be dropped")
        assertTrue("custom-book" in books, "books added in the CMS must survive")
        assertTrue(CompanyProfileSeed.books.all { it.id in books })
        val radar = books.getValue("blind-spot-radar")
        assertEquals(1240, radar.ratingCount)
        assertEquals(77, radar.interactions)
        assertEquals("https://example.test/bsr.pdf", radar.pdfUrl)
        assertEquals("Apa yang sedang kita lewatkan?", radar.tagline.id)

        assertEquals(4, repo.getValueLoop().steps.size)
        assertEquals(NavigationItem.CANONICAL_ITEMS.map { it.id }, repo.getNavigationItems().map { it.id })
        assertEquals("info@sekota.id", repo.getStrategicConversation().email)
        assertEquals("2", storage.getJson(AdminRepositoryImpl.KEY_CONTENT_SCHEMA))
    }

    @Test
    fun migrationDoesNotOverwriteCmsEditsAfterItRan() = runBlocking {
        val storage = AdminDataStorage()
        seedLegacyStore(storage)
        AdminRepositoryImpl(dataStorage = storage).getProducts() // migrate

        val repo = AdminRepositoryImpl(dataStorage = storage)
        val edited = repo.getProducts().first().copy(name = "Clarivio Pro")
        repo.saveProduct(edited)

        // A fresh repository instance (next app start) must keep the edit.
        assertEquals("Clarivio Pro", AdminRepositoryImpl(dataStorage = storage).getProducts().first().name)
    }

    @Test
    fun customContactEmailIsNotReplaced() = runBlocking {
        val storage = AdminDataStorage()
        storage.saveContactConfigJson(json.encodeToString(StrategicConversationConfig(email = "bd@sekota.id")))
        assertEquals("bd@sekota.id", AdminRepositoryImpl(dataStorage = storage).getStrategicConversation().email)
    }

    @Test
    fun emptyStoreIsSeededWithCompanyProfile() = runBlocking {
        val repo = AdminRepositoryImpl(dataStorage = AdminDataStorage())
        assertEquals(4, repo.getServices().size)
        assertEquals(7, repo.getFaqs().size)
        assertEquals("Tentang Sekota", repo.getAbout().eyebrow.id)
        assertEquals("Program Mitra Awal: bangun solusi analisis bersama tim inti Sekota, dengan target yang kita sepakati bersama.",
            repo.getPartnerCta().description.id)
    }
}
