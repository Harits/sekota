package com.sekota.ui.brand

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Outline icons used by the company profile (lucide set, as in the approved design).
 * Built as vectors so they stay sharp on Wasm, JS, desktop and Android (rule 17:
 * no emoji or text glyphs as icons). Tint with `Icon(tint = …)`.
 */
object BrandIcons {
    private fun circle(cx: Float, cy: Float, r: Float) =
        "M${cx - r} ${cy}a$r $r 0 1 0 ${2 * r} 0a$r $r 0 1 0 ${-2 * r} 0"

    private fun icon(name: String, vararg paths: String): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            paths.forEach { d ->
                addPath(
                    pathData = addPathNodes(d),
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                )
            }
        }.build()

    val Check by lazy { icon("check", "M20 6 9 17l-5-5") }
    val ArrowRight by lazy { icon("arrow-right", "M5 12h14", "m12 5 7 7-7 7") }
    val ArrowUpRight by lazy { icon("arrow-up-right", "M7 7h10v10", "M7 17 17 7") }
    val Plus by lazy { icon("plus", "M5 12h14", "M12 5v14") }
    val Menu by lazy { icon("menu", "M4 6h16", "M4 12h16", "M4 18h16") }
    val Close by lazy { icon("x", "M18 6 6 18", "m6 6 12 12") }
    val Mail by lazy { icon("mail", "M4 4h16a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z", "m22 6-10 7L2 6") }

    // Services
    val Radar by lazy {
        icon(
            "radar",
            "M19.07 4.93A10 10 0 0 0 6.99 3.34", "M4 6h.01", "M2.29 9.62A10 10 0 1 0 21.31 8.35",
            "M16.24 7.76A6 6 0 1 0 8.23 16.67", "M12 18h.01", "M17.99 11.66A6 6 0 0 1 15.77 16.67",
            circle(12f, 12f, 2f), "m13.41 10.59 5.66-5.66"
        )
    }
    val Users by lazy {
        icon("users", "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2", circle(9f, 7f, 4f), "M22 21v-2a4 4 0 0 0-3-3.87", "M16 3.13a4 4 0 0 1 0 7.75")
    }
    val Sprout by lazy {
        icon(
            "sprout",
            "M7 20h10", "M10 20c5.5-2.5.8-6.4 3-10",
            "M9.5 9.4c1.1.8 1.8 2.2 2.3 3.7-2 .4-3.5.4-4.8-.3-1.2-.6-2.3-1.9-3-4.2 2.8-.5 4.4 0 5.5.8z",
            "M14.1 6a7 7 0 0 0-1.1 4c1.9-.1 3.3-.6 4.3-1.4 1-1 1.6-2.3 1.7-4.6-2.7.1-4 1-4.9 2z"
        )
    }
    val Leaf by lazy {
        icon("leaf", "M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10Z", "M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12")
    }

    // Process stages
    val Search by lazy { icon("search", circle(11f, 11f, 8f), "m21 21-4.3-4.3") }
    val Layers by lazy {
        icon(
            "layers",
            "M12.83 2.18a2 2 0 0 0-1.66 0L2.6 6.08a1 1 0 0 0 0 1.83l8.58 3.91a2 2 0 0 0 1.66 0l8.58-3.9a1 1 0 0 0 0-1.83z",
            "m22 17.65-9.17 4.16a2 2 0 0 1-1.66 0L2 17.65", "m22 12.65-9.17 4.16a2 2 0 0 1-1.66 0L2 12.65"
        )
    }
    val Rocket by lazy {
        icon(
            "rocket",
            "M4.5 16.5c-1.5 1.26-2 5-2 5s3.74-.5 5-2c.71-.84.7-2.13-.09-2.91a2.18 2.18 0 0 0-2.91-.09z",
            "m12 15-3-3a22 22 0 0 1 2-3.95A12.88 12.88 0 0 1 22 2c0 2.72-.78 7.5-6 11a22.35 22.35 0 0 1-4 2z",
            "M9 12H4s.55-3.03 2-4c1.62-1.08 5 0 5 0", "M12 15v5s3.03-.55 4-2c1.08-1.62 0-5 0-5"
        )
    }
    val Refresh by lazy {
        icon(
            "refresh",
            "M3 12a9 9 0 0 1 9-9 9.75 9.75 0 0 1 6.74 2.74L21 8", "M21 3v5h-5",
            "M21 12a9 9 0 0 1-9 9 9.75 9.75 0 0 1-6.74-2.74L3 16", "M8 16H3v5"
        )
    }

    // About pillars
    val BookOpen by lazy { icon("book-open", "M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z", "M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z") }
    val ShieldCheck by lazy {
        icon(
            "shield-check",
            "M20 13c0 5-3.5 7.5-7.66 8.95a1 1 0 0 1-.67-.01C7.5 20.5 4 18 4 13V6a1 1 0 0 1 1-1c2 0 4.5-1.2 6.24-2.72a1.17 1.17 0 0 1 1.52 0C14.51 3.81 17 5 19 5a1 1 0 0 1 1 1z",
            "m9 12 2 2 4-4"
        )
    }
    val TrendingUp by lazy { icon("trending-up", "M22 7 13.5 15.5 8.5 10.5 2 17", "M16 7h6v6") }

    /** Icon for a CMS-provided icon name; unknown names fall back to [Layers]. */
    fun named(name: String?): ImageVector = when (name?.lowercase()) {
        "radar" -> Radar
        "users" -> Users
        "sprout" -> Sprout
        "leaf" -> Leaf
        "search" -> Search
        "layers" -> Layers
        "rocket" -> Rocket
        "refresh" -> Refresh
        "book" -> BookOpen
        "shield" -> ShieldCheck
        "growth" -> TrendingUp
        "check" -> Check
        else -> Layers
    }

    /** Service icon by its position in the services list (01–04), as in the design. */
    fun forService(index: Int): ImageVector = listOf(Radar, Users, Sprout, Leaf).getOrElse(index) { Layers }

    /** Process stage icon by position (Diagnosis, Perancangan, Penerapan, Keberlanjutan). */
    fun forStage(index: Int): ImageVector = listOf(Search, Layers, Rocket, Refresh).getOrElse(index) { Layers }
}
