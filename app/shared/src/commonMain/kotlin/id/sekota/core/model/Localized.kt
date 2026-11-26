package id.sekota.core.model

/** Supported site languages. The design ships full Indonesian and English copy. */
enum class Language(val code: String, val label: String) {
    ID("id", "ID"),
    EN("en", "EN");

    fun other(): Language = if (this == ID) EN else ID
}

/**
 * A piece of copy held in both languages. Content is authored once so switching
 * language never triggers a reload or a network call.
 */
data class LText(val id: String, val en: String) {
    operator fun get(language: Language): String = if (language == Language.ID) id else en
}

fun ltext(id: String, en: String): LText = LText(id, en)
