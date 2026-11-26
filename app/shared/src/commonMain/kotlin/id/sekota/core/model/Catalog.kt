package id.sekota.core.model

/** The four Sekota AI platforms. Order matches the design's "Intelligence Suite". */
enum class ProductSlug(val path: String) {
    Clarivio("clarivio"),
    Ascendio("ascendio"),
    Sociara("sociara"),
    Evidara("evidara");

    companion object {
        fun fromPath(path: String): ProductSlug? = entries.firstOrNull { it.path == path }
    }
}

/** The six thought-leadership books, with the anchors used by the design. */
enum class BookSlug(val anchor: String) {
    ForgettingEnterprise("forgetting-enterprise"),
    BlindSpotRadar("blind-spot-radar"),
    ManifestoEkuitasLahan("manifesto-ekuitas-lahan"),
    ImpactDrivenCsr("impact-driven-csr"),
    AccountableEsg("accountable-esg"),
    SustainabilityLeadership("sustainability-leadership");

    companion object {
        fun fromAnchor(anchor: String): BookSlug? = entries.firstOrNull { it.anchor == anchor }
    }
}
