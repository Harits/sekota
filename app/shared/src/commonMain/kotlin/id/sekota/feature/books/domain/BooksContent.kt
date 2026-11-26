package id.sekota.feature.books.domain

import id.sekota.core.model.BookSlug
import id.sekota.core.model.LText

data class BooksPageContent(
    val overline: LText,
    val titlePrefix: LText,
    val titleGradient: LText,
    val heroLead: LText,
    val indexLabel: LText,
    val bookCta: LText,
    val bookCtaHint: LText,
    val books: List<BookDetail>,
    val series: SeriesContent,
    val ctaTitle: LText,
    val ctaText: LText,
    val ctaButton: LText,
)

data class BookDetail(
    val slug: BookSlug,
    val author: LText,
    val title: String,
    val subtitle: LText,
    val quote: LText,
    val paragraphs: List<LText>,
    val audienceTitle: LText,
    val audience: List<LText>,
    val contentsTitle: LText,
    val contents: List<BookContentItem>,
)

data class BookContentItem(val title: LText, val detail: LText)

data class SeriesContent(
    val overline: LText,
    val title: LText,
    val text: LText,
    val quote: LText,
    val links: List<SeriesLink>,
)

data class SeriesLink(val slug: BookSlug, val label: LText, val meta: LText)

interface BooksRepository {
    fun load(): BooksPageContent
}

class GetBooks(private val repository: BooksRepository) {
    operator fun invoke(): BooksPageContent = repository.load()
}
