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
    val featuresEn: List<String> = emptyList()
) {
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

