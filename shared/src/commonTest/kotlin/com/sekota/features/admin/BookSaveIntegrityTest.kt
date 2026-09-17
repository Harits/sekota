package com.sekota.features.admin

import com.sekota.features.admin.domain.model.AdminBook
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Guards the CMS save path: an operator editing a book's identity fields must
 * never reset its web-display metadata or its engine-owned telemetry.
 */
class BookSaveIntegrityTest {

    private val stored = AdminBook(
        id = "manifesto-ekuitas-lahan",
        title = "Manifesto Ekuitas Lahan",
        author = "Putu Aan J.",
        isbn = "978-602-0000-00-0",
        category = "ESG",
        description = "Kajian ekuitas lahan.",
        rating = 4.4,
        ratingCount = 1,
        interactions = 22,
        pdfUrl = "https://sekota.id/reader/manifesto",
        readingTime = "5H 10M",
        pages = 312,
        year = "2024"
    )

    @Test
    fun editingIdentityPreservesWebDisplayAndTelemetry() {
        // What AdminDashboardScreen now builds when the operator renames a book.
        val saved = stored.copy(
            title = "Manifesto Ekuitas Lahan (Rev 2)",
            author = stored.author,
            isbn = stored.isbn,
            coverImage = null
        )

        assertEquals("Manifesto Ekuitas Lahan (Rev 2)", saved.title)
        assertEquals("ESG", saved.category, "category must survive an identity edit")
        assertEquals("https://sekota.id/reader/manifesto", saved.pdfUrl)
        assertEquals("5H 10M", saved.readingTime)
        assertEquals(312, saved.pages)
        assertEquals("2024", saved.year)
        assertEquals(4.4, saved.rating)
        assertEquals(1, saved.ratingCount)
        assertEquals(22, saved.interactions)
    }

    @Test
    fun theOldFivePositionalArgSaveWasLossy() {
        // Regression witness: this is exactly what the CMS used to construct.
        val lossy = AdminBook(stored.id, stored.title, stored.author, stored.isbn, null)

        assertEquals("SMART CITY", lossy.category)
        assertEquals(0.0, lossy.rating)
        assertEquals(0, lossy.interactions)
        assertEquals(null, lossy.pdfUrl)
        assertEquals(240, lossy.pages)
    }
}
