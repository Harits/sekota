package id.sekota.feature.legal

import id.sekota.core.model.Language
import id.sekota.feature.legal.data.LegalCatalog
import id.sekota.feature.legal.domain.LegalBlock
import id.sekota.feature.legal.domain.LegalDocType
import id.sekota.feature.legal.domain.LegalLink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LegalCatalogTest {

    private val docs = listOf(LegalCatalog.privacy, LegalCatalog.terms)

    @Test
    fun bothDocumentsHaveContentInBothLanguages() {
        docs.forEach { doc ->
            assertTrue(doc.title[Language.ID].isNotBlank())
            assertTrue(doc.title[Language.EN].isNotBlank())
            assertTrue(doc.lead[Language.ID].length > 40)
            assertTrue(doc.lead[Language.EN].length > 40)
            assertTrue(doc.sections.size >= 10)
            doc.sections.forEach { section ->
                assertTrue(section.title[Language.ID].isNotBlank())
                assertTrue(section.title[Language.EN].isNotBlank())
                assertTrue(section.blocks.isNotEmpty(), "section ${section.id} has no blocks")
            }
        }
    }

    @Test
    fun sectionIdsAreUniqueAndStable() {
        LegalCatalog.privacy.sections.map { it.id }.let { ids ->
            assertEquals(ids.size, ids.toSet().size)
            assertTrue(ids.containsAll(listOf("data", "hak", "kontak")))
        }
        LegalCatalog.terms.sections.map { it.id }.let { ids ->
            assertEquals(ids.size, ids.toSet().size)
            assertTrue(ids.containsAll(listOf("hki", "tanggung-jawab", "hukum")))
        }
    }

    @Test
    fun documentsLinkToEmailContactAndEachOther() {
        val links = docs.flatMap { doc ->
            doc.sections.flatMap { section ->
                section.blocks.flatMap { block ->
                    when (block) {
                        is LegalBlock.Paragraph -> block.spans.mapNotNull { it.link }
                        is LegalBlock.Bullets -> block.items.flatten().mapNotNull { it.link }
                    }
                }
            }
        }
        assertTrue(links.contains(LegalLink.Email))
        assertTrue(links.contains(LegalLink.Contact))
        assertEquals(LegalDocType.Terms, LegalCatalog.privacy.otherType)
        assertEquals(LegalDocType.Privacy, LegalCatalog.terms.otherType)
    }

    @Test
    fun bulletsCarryTheExpectedItems() {
        val prohibited = LegalCatalog.terms.sections.first { it.id == "larangan" }
            .blocks.filterIsInstance<LegalBlock.Bullets>().single()
        assertEquals(4, prohibited.items.size)
        assertTrue(prohibited.items.all { it.isNotEmpty() })
    }
}
