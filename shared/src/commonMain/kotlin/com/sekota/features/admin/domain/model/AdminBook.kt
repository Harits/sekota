package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminWhatsInsideItem(
    val title: String,
    val description: String,
    val iconName: String = "sparkle" // sparkle, frame, gear, leaf
)

@Serializable
data class AdminBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null,
    val category: String = "SMART CITY",
    val description: String = "",
    val rating: Double = 0.0,
    val ratingCount: Int = 0, // Registered Readers (Active Collaborators with Accounts)
    val interactions: Int = 0, // Total interactions / tool usages
    val pdfUrl: String? = null,
    val readingTime: String = "3H 45M",
    val pages: Int = 240,
    val publishedDate: String = "Nov 2025",
    val language: String = "Indonesia",
    val year: String = "2025",
    val coverColorHex: String? = null,
    val whatsInside: List<AdminWhatsInsideItem> = emptyList(),
    val categoryEn: String? = null,
    val descriptionEn: String? = null,

    // ---- Company profile revamp (CORE-07-01-02 / #22657) ----
    /** Anchor on the books page: /buku#{slug}. */
    val slug: String = "",
    /** The question the book answers, e.g. "Apa yang sedang kita lewatkan?". */
    val tagline: LocalizedText = LocalizedText(),
    /** Transformation label, e.g. "sinyal → keputusan". */
    val flowLabel: LocalizedText = LocalizedText(),
    val blurb: LocalizedText = LocalizedText(),
    /** "Untuk siapa" — the readers this book is written for. */
    val audience: List<LocalizedText> = emptyList(),
    /** Series id when the book belongs to one (e.g. the CSR/ESG/Leadership trilogy). */
    val seriesId: String? = null,
    /** Bundled cover asset key resolved by the UI; [coverImage] (URL/base64) wins when set. */
    val coverAsset: String? = null,
    val sortOrder: Int = 0
) {
    val anchorSlug: String get() = slug.ifBlank { id }

    fun resolveForLocale(locale: com.sekota.localization.AppLocale): AdminBook {
        return if (locale == com.sekota.localization.AppLocale.EN) {
            this.copy(
                category = categoryEn?.takeIf { it.isNotBlank() } ?: category,
                description = descriptionEn?.takeIf { it.isNotBlank() } ?: description
            )
        } else {
            this
        }
    }
}

