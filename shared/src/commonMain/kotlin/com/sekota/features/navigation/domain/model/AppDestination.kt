package com.sekota.features.navigation.domain.model

/**
 * Sections of the company profile landing page, in page order (WEB-07-03 / #22655).
 * [id] is the URL hash used by the approved design (e.g. "#cara-kerja").
 */
enum class LandingAnchor(val id: String) {
    TENTANG("tentang"),
    LAYANAN("layanan"),
    CARA_KERJA("cara-kerja"),
    PLATFORM("platform"),
    WAWASAN("wawasan"),
    MITRA("mitra"),
    FAQ("faq");

    companion object {
        fun fromId(raw: String): LandingAnchor? {
            val clean = raw.trim().removePrefix("#")
            return entries.firstOrNull { it.id.equals(clean, ignoreCase = true) }
        }
    }
}

/**
 * Domain-level navigation destination: what the visitor wants to reach, independent
 * of how a platform renders it (Clean & Screaming Architecture).
 */
sealed class AppDestination {
    data object Landing : AppDestination()
    data class Section(val anchor: LandingAnchor) : AppDestination()
    /** Product page, /produk/{slug} (page itself ships in Sprint 8). */
    data class Product(val slug: String) : AppDestination()
    /** Books page, /buku with an optional #slug anchor (page itself ships in Sprint 8). */
    data class Books(val bookSlug: String? = null) : AppDestination()
    /** Contact page, /kontak with an optional anchor such as "mitra-awal". */
    data class Contact(val anchor: String? = null) : AppDestination()
    data object Admin : AppDestination()
    data class External(val url: String) : AppDestination()
    data class Unknown(val raw: String) : AppDestination()
}

/**
 * Maps destinations to routes and parses routes, CMS link codes and legacy links
 * from the previous design back into destinations.
 */
object NavigationRouter {
    fun getRoute(destination: AppDestination): String = when (destination) {
        AppDestination.Landing -> "/"
        is AppDestination.Section -> "#${destination.anchor.id}"
        is AppDestination.Product -> "/produk/${destination.slug}"
        is AppDestination.Books -> destination.bookSlug?.let { "/buku#$it" } ?: "/buku"
        is AppDestination.Contact -> destination.anchor?.let { "/kontak#$it" } ?: "/kontak"
        AppDestination.Admin -> "/admin"
        is AppDestination.External -> destination.url
        is AppDestination.Unknown -> destination.raw
    }

    /** Platform codes from the product registry, including the two retired ones. */
    private val productCodeToSlug = mapOf(
        "CLV" to "clarivio",
        "ASC" to "ascendio",
        "SOC" to "sociara",
        "EVD" to "evidara"
    )
    private val retiredProductCodes = setOf("VRD", "ECO", "VERIDIA", "ECOFLOW")

    /** Routes of features removed in the company profile revamp; they land on the home page. */
    private val retiredRoutes = setOf(
        "/merchandise", "merchandise", "#merchandise", "merch",
        "/login", "login", "auth", "/signup", "signup", "/profile", "profile"
    )

    fun parseDestination(rawRouteOrCode: String): AppDestination {
        val clean = rawRouteOrCode.trim()
        if (clean.isEmpty()) return AppDestination.Unknown(clean)
        val lower = clean.lowercase()
        val upper = clean.uppercase()

        if (lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("mailto:")) {
            return AppDestination.External(clean)
        }

        // Path and optional hash, accepting both app routes and the design's file names.
        val path = lower.substringBefore('#').removeSuffix("/").removePrefix("/").removeSuffix(".html")
        val hash = clean.substringAfter('#', missingDelimiterValue = "").takeIf { it.isNotBlank() }

        return when {
            lower == "/" || path == "index" && hash == null || lower == "#top" || lower == "#beranda" || lower == "home" -> AppDestination.Landing
            path == "admin" -> AppDestination.Admin
            path.startsWith("produk/") -> AppDestination.Product(path.removePrefix("produk/"))
            path in productCodeToSlug.values -> AppDestination.Product(path)
            upper in productCodeToSlug -> AppDestination.Product(productCodeToSlug.getValue(upper))
            upper in retiredProductCodes -> AppDestination.Section(LandingAnchor.PLATFORM)
            path == "buku" || path == "catalog" || upper == "EBOOK" || lower == "#catalog" -> AppDestination.Books(hash)
            path == "kontak" || upper == "CONTACT" || lower == "#kontak" -> AppDestination.Contact(if (path == "kontak") hash else null)
            lower.startsWith("/reader/") || (lower.startsWith("/ebook/") && lower.endsWith("/baca")) -> AppDestination.Landing
            lower in retiredRoutes -> AppDestination.Landing
            // Legacy anchors of the Sprint 6 landing page.
            lower == "#solusi" || lower == "solusi" -> AppDestination.Section(LandingAnchor.CARA_KERJA)
            lower == "#produk" || lower == "produk" -> AppDestination.Section(LandingAnchor.PLATFORM)
            upper == "ABOUT" -> AppDestination.Section(LandingAnchor.TENTANG)
            upper == "INSIGHTS" -> AppDestination.Section(LandingAnchor.WAWASAN)
            else -> LandingAnchor.fromId(clean)?.let { AppDestination.Section(it) }
                ?: (if (path == "index" && hash != null) LandingAnchor.fromId(hash)?.let { AppDestination.Section(it) } else null)
                ?: AppDestination.Unknown(clean)
        }
    }
}
