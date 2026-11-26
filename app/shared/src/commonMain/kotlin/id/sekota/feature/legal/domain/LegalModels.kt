package id.sekota.feature.legal.domain

import id.sekota.core.model.LText

enum class LegalDocType { Privacy, Terms }

enum class LegalLink { Email, Contact, Privacy, Terms }

/** A run inside a paragraph or bullet; links are rendered as tappable text. */
data class LegalSpan(
    val text: LText,
    val link: LegalLink? = null,
    val bold: Boolean = false,
)

sealed interface LegalBlock {
    data class Paragraph(val spans: List<LegalSpan>) : LegalBlock
    data class Bullets(val items: List<List<LegalSpan>>) : LegalBlock
}

data class LegalSection(
    val id: String,
    val title: LText,
    val blocks: List<LegalBlock>,
)

data class LegalDoc(
    val type: LegalDocType,
    val overline: LText,
    val title: LText,
    val lead: LText,
    val updated: LText,
    val tocTitle: LText,
    val otherLabel: LText,
    val otherType: LegalDocType,
    val sections: List<LegalSection>,
)

interface LegalRepository {
    fun privacy(): LegalDoc
    fun terms(): LegalDoc
}

class GetLegalDoc(private val repository: LegalRepository) {
    operator fun invoke(type: LegalDocType): LegalDoc = when (type) {
        LegalDocType.Privacy -> repository.privacy()
        LegalDocType.Terms -> repository.terms()
    }
}
