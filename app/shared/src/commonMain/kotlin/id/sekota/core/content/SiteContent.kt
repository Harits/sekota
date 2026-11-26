package id.sekota.core.content

import id.sekota.core.model.LText
import id.sekota.core.model.ProductSlug
import id.sekota.core.model.ltext

data class SiteNavItem(val anchor: String, val label: LText)

data class FooterColumn(val title: LText, val links: List<LText>)

/** Copy shared by the top navigation and footer on every page. */
object SiteContent {
    val navItems = listOf(
        SiteNavItem("tentang", ltext("Tentang", "About")),
        SiteNavItem("layanan", ltext("Layanan", "Services")),
        SiteNavItem("cara-kerja", ltext("Cara Kerja", "How We Work")),
        SiteNavItem("platform", ltext("Platform", "Platform")),
        SiteNavItem("wawasan", ltext("Wawasan", "Insights")),
        SiteNavItem("faq", ltext("FAQ", "FAQ")),
    )

    val contactUs = ltext("Hubungi Kami", "Contact Us")
    val email = "info@sekota.id"

    val platformNames: Map<ProductSlug, LText> = mapOf(
        ProductSlug.Clarivio to ltext("Clarivio", "Clarivio"),
        ProductSlug.Ascendio to ltext("Ascendio", "Ascendio"),
        ProductSlug.Sociara to ltext("Sociara", "Sociara"),
        ProductSlug.Evidara to ltext("Evidara", "Evidara"),
    )

    val platformServices: Map<ProductSlug, LText> = mapOf(
        ProductSlug.Clarivio to ltext("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
        ProductSlug.Ascendio to ltext("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
        ProductSlug.Sociara to ltext("Dampak Sosial & CSR", "Social Impact & CSR"),
        ProductSlug.Evidara to ltext("Kinerja ESG", "ESG Performance"),
    )

    val footerDescription = ltext(
        "PT Sekota Sinergi Indonesia, bagian dari eBdesk Group. Mitra analisis untuk keputusan yang berdampak.",
        "PT Sekota Sinergi Indonesia, part of eBdesk Group. An intelligence partner for decisions that make an impact.",
    )
    val footerNavigation = ltext("Navigasi", "Navigation")
    val footerPlatform = ltext("Platform", "Platform")
    val footerContact = ltext("Kontak", "Contact")
    val copyright = ltext(
        "© 2026 PT Sekota Sinergi Indonesia · Teknologi oleh eBdesk",
        "© 2026 PT Sekota Sinergi Indonesia · Technology by eBdesk",
    )
    val legal = ltext("Privasi · Syarat & Ketentuan", "Privacy · Terms & Conditions")
    val privacy = ltext("Privasi", "Privacy")
    val terms = ltext("Syarat & Ketentuan", "Terms & Conditions")
    val languageGroup = ltext("Bahasa", "Language")
}
