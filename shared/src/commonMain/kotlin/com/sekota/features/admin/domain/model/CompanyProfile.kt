package com.sekota.features.admin.domain.model

import com.sekota.localization.AppLocale
import kotlinx.serialization.Serializable

/**
 * Bilingual copy value object used by every company-profile entity (CORE-07-01 / #22656).
 *
 * Indonesian ([id]) is the source of truth; English ([en]) falls back to Indonesian
 * when it has not been authored yet, so a missing translation never renders blank.
 */
@Serializable
data class LocalizedText(
    val id: String = "",
    val en: String = ""
) {
    fun resolve(locale: AppLocale): String =
        if (locale == AppLocale.EN && en.isNotBlank()) en else id

    val isBlank: Boolean get() = id.isBlank()
}

/** One of the four Sekota services shown in the "Layanan" section. */
@Serializable
data class ServiceOffering(
    val id: String,
    val number: String,
    val title: LocalizedText,
    val summary: LocalizedText,
    val highlights: List<LocalizedText> = emptyList(),
    /** Code of the platform that powers this service (e.g. "CLV" for Clarivio). */
    val productCode: String? = null,
    val sortOrder: Int = 0,
    val isVisible: Boolean = true
)

/** A question in the "Pertanyaan sebelum bekerja sama" accordion. */
@Serializable
data class Faq(
    val id: String,
    val question: LocalizedText,
    val answer: LocalizedText,
    val sortOrder: Int = 0,
    val isVisible: Boolean = true
)

@Serializable
data class AboutPillar(
    val title: LocalizedText,
    val description: LocalizedText,
    val iconName: String = "sparkle"
)

/**
 * Company-level facts: the "Tentang" section plus the sectors Sekota serves and the
 * offerings listed under "Juga tersedia" in the services section.
 */
@Serializable
data class AboutConfig(
    val eyebrow: LocalizedText = LocalizedText(),
    val title: LocalizedText = LocalizedText(),
    val description: LocalizedText = LocalizedText(),
    val pillars: List<AboutPillar> = emptyList(),
    val sectors: List<LocalizedText> = emptyList(),
    val additionalOfferings: List<LocalizedText> = emptyList()
) {
    val isEmpty: Boolean get() = title.isBlank && pillars.isEmpty()
}

/** The gradient "Mari tumbuh dan berkembang bersama" call to action. */
@Serializable
data class PartnerCtaConfig(
    val title: LocalizedText = LocalizedText(),
    val description: LocalizedText = LocalizedText(),
    val ctaLabel: LocalizedText = LocalizedText(),
    /** Destination route, resolved by NavigationRouter (e.g. "/kontak#mitra-awal"). */
    val ctaRoute: String = "/kontak"
) {
    val isEmpty: Boolean get() = title.isBlank
}

/** A capability tile on a product detail page (content authored in Sprint 8). */
@Serializable
data class ProductCapability(
    val title: LocalizedText,
    val description: LocalizedText,
    val iconName: String = "sparkle"
)

/** One of the four "how to use" steps on a product detail page. */
@Serializable
data class ProductUsageStep(
    val title: LocalizedText,
    val description: LocalizedText
)
