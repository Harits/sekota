package id.sekota.feature.legal.presentation

/**
 * A legal section item that intersects the LazyColumn viewport.
 *
 * @param index 0-based index in the document's section list (not the LazyColumn item index)
 * @param top item top edge in pixels, relative to the viewport top
 */
internal data class SectionSpan(
    val index: Int,
    val top: Int,
)

/**
 * Resolves the section that owns the reading line.
 *
 * "First visible item" is not a reliable anchor: `visibleItemsInfo` also contains
 * items whose remaining pixels sit behind the 80dp overlay header, and every
 * section item carries 44dp of trailing padding. That makes the highlight lag by
 * one section, permanently so at max scroll, where the penultimate item's tail can
 * never leave the viewport.
 *
 * Rules, in order:
 *  1. At the end of a scrollable document, the last section is active.
 *  2. If only the trailing "next"/footer items remain, the last section is active.
 *  3. Otherwise the active section is the last one whose top crossed the line.
 *  4. If the line is still above every visible section, the first one is active.
 */
internal fun resolveActiveSectionIndex(
    visibleSections: List<SectionSpan>,
    activationLinePx: Int,
    lastDocumentSection: Int,
    atDocumentEnd: Boolean,
    hasScrolledPastSections: Boolean = false,
): Int {
    if (lastDocumentSection < 0) return 0
    if (atDocumentEnd || hasScrolledPastSections) return lastDocumentSection

    val containing = visibleSections.lastOrNull { it.top <= activationLinePx }
        ?: visibleSections.firstOrNull()
    return (containing?.index ?: 0).coerceIn(0, lastDocumentSection)
}

/**
 * True when the list is at the end and the first item is not still flush at the
 * top. The offset check covers documents that overflow by less than the height of
 * the first (hero) item, where item 0 stays first visible at max scroll.
 */
internal fun isDocumentEnd(
    canScrollForward: Boolean,
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
): Boolean = !canScrollForward &&
    (firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0)
