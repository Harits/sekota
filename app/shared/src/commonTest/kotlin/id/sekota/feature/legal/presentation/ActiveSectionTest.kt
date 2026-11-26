package id.sekota.feature.legal.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActiveSectionTest {

    private val line = 108

    private fun spans(vararg pairs: Pair<Int, Int>) =
        pairs.map { (index, top) -> SectionSpan(index, top) }

    private fun active(
        sections: List<SectionSpan>,
        atEnd: Boolean = false,
        pastSections: Boolean = false,
        last: Int = 9,
    ) = resolveActiveSectionIndex(sections, line, last, atEnd, pastSections)

    @Test
    fun readingLineAboveSectionsSelectsFirst() {
        assertEquals(0, active(spans(0 to 400)))
    }

    @Test
    fun nextSectionWinsAsSoonAsItsTopCrossesTheLine() {
        assertEquals(9, active(spans(8 to -50, 9 to 100)))
    }

    @Test
    fun previousSectionWinsWhileItsBodyOwnsTheLine() {
        assertEquals(8, active(spans(8 to -500, 9 to 150)))
    }

    @Test
    fun previousSectionPaddingAloneDoesNotKeepItActive() {
        assertEquals(9, active(spans(8 to -44, 9 to 20)))
    }

    @Test
    fun endOfDocumentAlwaysSelectsLastSection() {
        // Reported bug: the final heading never reaches the line on a tall viewport.
        assertEquals(9, active(spans(8 to -20, 9 to 120), atEnd = true))
    }

    @Test
    fun footerOnlyKeepsLastSectionActive() {
        assertEquals(9, active(emptyList(), pastSections = true))
    }

    @Test
    fun secondToLastClickAfterScrollUpSelectsIt() {
        assertEquals(8, active(spans(7 to -400, 8 to 108, 9 to 480)))
    }

    @Test
    fun shortSectionsSelectTheOneUnderTheLine() {
        assertEquals(3, active(spans(2 to -100, 3 to 50, 4 to 150)))
    }

    @Test
    fun fastFlingSkipsIntermediateSections() {
        assertEquals(8, active(spans(5 to -500, 8 to 60, 9 to 400)))
    }

    @Test
    fun boundaryEqualitySelectsTheNextSection() {
        assertEquals(1, active(spans(0 to -200, 1 to 108)))
    }

    @Test
    fun shortDocumentThatFitsIsNotForcedToLast() {
        assertEquals(0, active(spans(0 to 60, 1 to 200), last = 1))
    }

    @Test
    fun emptyDocumentIsDefensive() {
        assertEquals(0, resolveActiveSectionIndex(emptyList(), line, -1, false))
    }

    @Test
    fun documentEndRequiresNoForwardScrollAndSomeOffset() {
        assertFalse(isDocumentEnd(canScrollForward = true, firstVisibleItemIndex = 3, firstVisibleItemScrollOffset = 200))
        assertFalse(isDocumentEnd(canScrollForward = false, firstVisibleItemIndex = 0, firstVisibleItemScrollOffset = 0))
        assertTrue(isDocumentEnd(canScrollForward = false, firstVisibleItemIndex = 2, firstVisibleItemScrollOffset = 0))
        // Short overflow: item 0 is still first, but the list is scrolled.
        assertTrue(isDocumentEnd(canScrollForward = false, firstVisibleItemIndex = 0, firstVisibleItemScrollOffset = 100))
    }
}
