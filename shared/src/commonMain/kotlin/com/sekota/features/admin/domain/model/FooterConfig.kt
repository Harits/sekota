package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FooterLinkItem(
    val id: String,
    val label: String,
    val urlOrCode: String = "",
    val isVisible: Boolean = true,
    val sortOrder: Int = 0,
    val labelEn: String? = null
)

@Serializable
data class FooterColumnConfig(
    val title: String,
    val items: List<FooterLinkItem> = emptyList(),
    val titleEn: String? = null
)

@Serializable
data class FooterSocialLink(
    val id: String,
    val platform: String, // e.g. "LINKEDIN", "TWITTER", "INSTAGRAM"
    val url: String = "",
    val isVisible: Boolean = true
)

/**
 * Global footer content. Defaults follow the company profile revamp (Sprint 7):
 * Navigasi / Platform / Kontak columns, routes resolved by NavigationRouter.
 */
@Serializable
data class FooterConfig(
    val tagline: String = "PT Sekota Sinergi Indonesia, bagian dari eBdesk Group. Mitra analisis untuk keputusan yang berdampak.",
    val affiliationText: String = "eBdesk Group",
    val affiliationSubtext: String = "Teknologi oleh eBdesk",
    val copyrightText: String = "© 2026 PT Sekota Sinergi Indonesia · Teknologi oleh eBdesk",
    val socialLinks: List<FooterSocialLink> = emptyList(),
    val productColumn: FooterColumnConfig = FooterColumnConfig(
        title = "Platform",
        titleEn = "Platform",
        items = listOf(
            FooterLinkItem(id = "p_clv", label = "Clarivio", urlOrCode = "/produk/clarivio", sortOrder = 1),
            FooterLinkItem(id = "p_asc", label = "Ascendio", urlOrCode = "/produk/ascendio", sortOrder = 2),
            FooterLinkItem(id = "p_soc", label = "Sociara", urlOrCode = "/produk/sociara", sortOrder = 3),
            FooterLinkItem(id = "p_evd", label = "Evidara", urlOrCode = "/produk/evidara", sortOrder = 4)
        )
    ),
    val companyColumn: FooterColumnConfig = FooterColumnConfig(
        title = "Navigasi",
        titleEn = "Navigation",
        items = listOf(
            FooterLinkItem(id = "n_tentang", label = "Tentang", urlOrCode = "#tentang", sortOrder = 1, labelEn = "About"),
            FooterLinkItem(id = "n_layanan", label = "Layanan", urlOrCode = "#layanan", sortOrder = 2, labelEn = "Services"),
            FooterLinkItem(id = "n_cara_kerja", label = "Cara Kerja", urlOrCode = "#cara-kerja", sortOrder = 3, labelEn = "How We Work"),
            FooterLinkItem(id = "n_platform", label = "Platform", urlOrCode = "#platform", sortOrder = 4, labelEn = "Platform"),
            FooterLinkItem(id = "n_wawasan", label = "Wawasan", urlOrCode = "#wawasan", sortOrder = 5, labelEn = "Insights"),
            FooterLinkItem(id = "n_faq", label = "FAQ", urlOrCode = "#faq", sortOrder = 6, labelEn = "FAQ")
        )
    ),
    val legalColumn: FooterColumnConfig = FooterColumnConfig(
        title = "Legal",
        titleEn = "Legal",
        items = listOf(
            FooterLinkItem(id = "l_privacy", label = "Privasi", urlOrCode = "PRIVACY", sortOrder = 1, labelEn = "Privacy"),
            FooterLinkItem(id = "l_terms", label = "Syarat & Ketentuan", urlOrCode = "TERMS", sortOrder = 2, labelEn = "Terms & Conditions")
        )
    ),
    val taglineEn: String? = "PT Sekota Sinergi Indonesia, part of eBdesk Group. An intelligence partner for decisions that make an impact.",
    val copyrightTextEn: String? = "© 2026 PT Sekota Sinergi Indonesia · Technology by eBdesk"
)
