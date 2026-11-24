package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminProduct(
    val id: String,
    val code: String,
    val name: String,
    val categoryEyebrow: String,
    val description: String,
    val features: List<String>,
    val iconUrlOrBase64: String? = null,
    val accentColorHex: String? = null,
    val categoryEyebrowEn: String? = null,
    val descriptionEn: String? = null,
    val featuresEn: List<String> = emptyList(),

    // ---- Company profile revamp (CORE-07-01-02 / #22657) ----
    /** URL segment for the product page: /produk/{slug}. */
    val slug: String = "",
    /** Id of the [ServiceOffering] this platform powers. */
    val serviceId: String? = null,
    /** One-line promise shown in the hero suite card. */
    val tagline: LocalizedText = LocalizedText(),
    /** Transformation label, e.g. "Sinyal → keputusan". */
    val flowLabel: LocalizedText = LocalizedText(),
    /** Summary shown on the dark "Platform" tile. */
    val platformSummary: LocalizedText = LocalizedText(),
    /** Asset keys resolved by the UI (see ProductAssets). */
    val logoLightAsset: String? = null,
    val logoDarkAsset: String? = null,
    val dashboardAsset: String? = null,
    // Product detail page content (rendered in Sprint 8, WEB-08-01).
    val headline: LocalizedText = LocalizedText(),
    val problems: List<LocalizedText> = emptyList(),
    val capabilities: List<ProductCapability> = emptyList(),
    val screenshotAssets: List<String> = emptyList(),
    val usageSteps: List<ProductUsageStep> = emptyList(),
    val audience: List<LocalizedText> = emptyList(),
    val sortOrder: Int = 0,
    val isVisible: Boolean = true
) {
    /** Slug used in routes; falls back to the lower-cased name for legacy records. */
    val routeSlug: String get() = slug.ifBlank { name.lowercase().replace(' ', '-') }

    fun resolveForLocale(locale: com.sekota.localization.AppLocale): AdminProduct {
        return if (locale == com.sekota.localization.AppLocale.EN) {
            this.copy(
                categoryEyebrow = categoryEyebrowEn?.takeIf { it.isNotBlank() } ?: categoryEyebrow,
                description = descriptionEn?.takeIf { it.isNotBlank() } ?: description,
                features = if (featuresEn.isNotEmpty()) featuresEn else features
            )
        } else {
            this
        }
    }
}
