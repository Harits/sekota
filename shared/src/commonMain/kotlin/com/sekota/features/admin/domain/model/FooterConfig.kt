package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FooterLinkItem(
    val id: String,
    val label: String,
    val urlOrCode: String = "",
    val isVisible: Boolean = true,
    val sortOrder: Int = 0
)

@Serializable
data class FooterColumnConfig(
    val title: String,
    val items: List<FooterLinkItem> = emptyList()
)

@Serializable
data class FooterSocialLink(
    val id: String,
    val platform: String, // e.g. "LINKEDIN", "TWITTER", "INSTAGRAM"
    val url: String = "",
    val isVisible: Boolean = true
)

@Serializable
data class FooterConfig(
    val tagline: String = "Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.",
    val affiliationText: String = "eBdesk Group Affiliation",
    val affiliationSubtext: String = "NATIONWIDE PARTNER",
    val copyrightText: String = "© 2025 PT. SEKOTA SINERGI INDONESIA. BAGIAN DARI EBDESK GROUP.",
    val socialLinks: List<FooterSocialLink> = listOf(
        FooterSocialLink(id = "soc_li", platform = "LINKEDIN", url = "https://linkedin.com/company/sekota"),
        FooterSocialLink(id = "soc_tw", platform = "TWITTER", url = "https://twitter.com/sekota_id")
    ),
    val productColumn: FooterColumnConfig = FooterColumnConfig(
        title = "PRODUK",
        items = listOf(
            FooterLinkItem(id = "p_vrd", label = "VERIDIA", urlOrCode = "VRD", sortOrder = 1),
            FooterLinkItem(id = "p_asc", label = "ASCENDIO", urlOrCode = "ASC", sortOrder = 2),
            FooterLinkItem(id = "p_soc", label = "SOCIARA", urlOrCode = "SOC", sortOrder = 3),
            FooterLinkItem(id = "p_eco", label = "ECOFLOW", urlOrCode = "ECO", sortOrder = 4)
        )
    ),
    val companyColumn: FooterColumnConfig = FooterColumnConfig(
        title = "PERUSAHAAN",
        items = listOf(
            FooterLinkItem(id = "c_about", label = "Tentang Kami", urlOrCode = "ABOUT", sortOrder = 1),
            FooterLinkItem(id = "c_ebook", label = "E-Book", urlOrCode = "EBOOK", sortOrder = 2),
            FooterLinkItem(id = "c_merch", label = "Merchandise", urlOrCode = "MERCH", sortOrder = 3),
            FooterLinkItem(id = "c_insights", label = "Insights", urlOrCode = "INSIGHTS", sortOrder = 4),
            FooterLinkItem(id = "c_contact", label = "Kontak", urlOrCode = "CONTACT", sortOrder = 5)
        )
    ),
    val legalColumn: FooterColumnConfig = FooterColumnConfig(
        title = "LEGAL",
        items = listOf(
            FooterLinkItem(id = "l_privacy", label = "Privasi", urlOrCode = "PRIVACY", sortOrder = 1),
            FooterLinkItem(id = "l_terms", label = "Syarat & Ketentuan", urlOrCode = "TERMS", sortOrder = 2)
        )
    )
)
