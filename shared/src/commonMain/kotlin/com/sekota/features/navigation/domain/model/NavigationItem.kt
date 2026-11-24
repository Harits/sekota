package com.sekota.features.navigation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigationItem(
    val id: String,
    val label: String,
    val route: String,
    val iconUrl: String? = null,
    val order: Int = 0,
    val isVisible: Boolean = true,
    val labelEn: String? = null
) {
    fun labelFor(locale: com.sekota.localization.AppLocale): String =
        if (locale == com.sekota.localization.AppLocale.EN) labelEn?.takeIf { it.isNotBlank() } ?: label else label

    companion object {
        /** Main navigation of the company profile (WEB-07-03 / #22655). */
        val CANONICAL_ITEMS = listOf(
            NavigationItem(id = "nav_tentang", label = "Tentang", labelEn = "About", route = "#tentang", order = 1),
            NavigationItem(id = "nav_layanan", label = "Layanan", labelEn = "Services", route = "#layanan", order = 2),
            NavigationItem(id = "nav_cara_kerja", label = "Cara Kerja", labelEn = "How We Work", route = "#cara-kerja", order = 3),
            NavigationItem(id = "nav_platform", label = "Platform", labelEn = "Platform", route = "#platform", order = 4),
            NavigationItem(id = "nav_wawasan", label = "Wawasan", labelEn = "Insights", route = "#wawasan", order = 5),
            NavigationItem(id = "nav_faq", label = "FAQ", labelEn = "FAQ", route = "#faq", order = 6)
        )
    }
}
