package id.sekota.content

import id.sekota.core.model.BookSlug
import id.sekota.core.model.Language
import id.sekota.core.model.LText
import id.sekota.core.model.ProductSlug
import id.sekota.feature.books.data.BooksCatalog
import id.sekota.feature.contact.data.ContactCatalog
import id.sekota.feature.contact.domain.ContactTopicKey
import id.sekota.feature.landing.data.LandingCatalog
import id.sekota.feature.product.data.ProductCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ContentCatalogTest {

    private fun LText.nonBlankBoth() {
        assertTrue(id.isNotBlank(), "Indonesian copy is blank")
        assertTrue(en.isNotBlank(), "English copy is blank")
    }

    @Test
    fun landingHasTheDesignedSections() {
        val content = LandingCatalog.content
        assertEquals(4, content.services.items.size)
        assertEquals(4, content.process.steps.size)
        assertEquals(4, content.platforms.items.size)
        assertEquals(6, content.books.items.size)
        assertEquals(7, content.faq.items.size)
        assertEquals(6, content.sectors.size)
        assertEquals(ProductSlug.entries.toSet(), content.services.items.map { it.platform }.toSet())
        content.services.items.forEach { item ->
            assertEquals(3, item.points.size)
            item.title.nonBlankBoth()
            item.tagline.nonBlankBoth()
            item.points.forEach { it.nonBlankBoth() }
        }
        content.faq.items.forEach {
            it.question.nonBlankBoth()
            assertTrue(it.answer[Language.ID].length > 20)
            assertTrue(it.answer[Language.EN].length > 20)
        }
    }

    @Test
    fun landingHeroIsBilingual() {
        LandingCatalog.content.hero.titleLine1.nonBlankBoth()
        LandingCatalog.content.hero.titleLine2.nonBlankBoth()
        LandingCatalog.content.hero.lead.nonBlankBoth()
        assertEquals(4, LandingCatalog.content.hero.suiteRows.size)
    }

    @Test
    fun everyProductHasCompleteDetail() {
        assertEquals(4, ProductCatalog.all.size)
        ProductCatalog.all.forEach { (slug, detail) ->
            assertEquals(slug, detail.slug)
            assertEquals(3, detail.problems.size)
            assertEquals(6, detail.features.size)
            assertEquals(4, detail.flow.size)
            assertEquals(4, detail.audience.size)
            assertTrue(detail.screens.size >= 2)
            detail.headline.nonBlankBoth()
            detail.lead.nonBlankBoth()
            detail.ctaTitle.nonBlankBoth()
            detail.relatedText.nonBlankBoth()
        }
    }

    @Test
    fun booksAreCompleteAndBilingual() {
        val content = BooksCatalog.content
        assertEquals(BookSlug.entries.size, content.books.size)
        assertEquals(3, content.series.links.size)
        content.books.forEach { book ->
            assertTrue(book.paragraphs.size >= 3, "${book.title} has too few paragraphs")
            assertTrue(book.audience.isNotEmpty())
            assertTrue(book.contents.size >= 3)
            book.subtitle.nonBlankBoth()
            book.quote.nonBlankBoth()
            book.paragraphs.forEach { it.nonBlankBoth() }
        }
    }

    @Test
    fun contactTopicsAndPrefillsAreStable() {
        val content = ContactCatalog.content
        assertEquals(ContactTopicKey.entries.size, content.topics.size)
        assertEquals(content.topics.size, content.topics.map { it.key }.toSet().size)
        content.topics.forEach { it.label.nonBlankBoth() }

        val demo = assertNotNull(ContactCatalog.prefill("demo-clarivio"))
        assertEquals(ContactTopicKey.DemoClarivio, demo.topic)
        assertTrue(demo.message[Language.ID].contains("Clarivio"))

        val partner = assertNotNull(ContactCatalog.prefill("mitra-awal"))
        assertEquals(ContactTopicKey.Partner, partner.topic)

        val book = assertNotNull(ContactCatalog.prefill("buku-blind-spot-radar"))
        assertEquals(ContactTopicKey.Books, book.topic)
        assertTrue(book.message[Language.ID].contains("Blind Spot Radar"))

        assertNull(ContactCatalog.prefill("tidak-ada"))
        assertNull(ContactCatalog.prefill(null))
    }

    @Test
    fun navigationAnchorsMatchLandingSections() {
        val anchors = LandingCatalog.content.navItems.map { it.anchor }
        assertEquals(listOf("tentang", "layanan", "cara-kerja", "platform", "wawasan", "faq"), anchors)
    }
}
