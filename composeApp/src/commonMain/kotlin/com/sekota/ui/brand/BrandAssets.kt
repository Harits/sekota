package com.sekota.ui.brand

import org.jetbrains.compose.resources.DrawableResource
import sekota.composeapp.generated.resources.*

/**
 * Maps CMS asset keys (e.g. "clarivio_light", "cover2") to bundled WebP drawables
 * (WEB-07-01-02 / #22661). Unknown keys return null so callers can fall back to a
 * CMS-uploaded image or a text label instead of a broken image.
 */
object BrandAssets {
    val SekotaLogoLight: DrawableResource get() = Res.drawable.sekota_light
    val SekotaLogoDark: DrawableResource get() = Res.drawable.sekota_dark

    private val byKey: Map<String, DrawableResource> by lazy {
        mapOf(
            "sekota_light" to Res.drawable.sekota_light,
            "sekota_dark" to Res.drawable.sekota_dark,
            "clarivio_light" to Res.drawable.clarivio_light,
            "clarivio_dark" to Res.drawable.clarivio_dark,
            "ascendio_light" to Res.drawable.ascendio_light,
            "ascendio_dark" to Res.drawable.ascendio_dark,
            "sociara_light" to Res.drawable.sociara_light,
            "sociara_dark" to Res.drawable.sociara_dark,
            "evidara_light" to Res.drawable.evidara_light,
            "evidara_dark" to Res.drawable.evidara_dark,
            "dash_clarivio" to Res.drawable.dash_clarivio,
            "dash_ascendio" to Res.drawable.dash_ascendio,
            "dash_sociara" to Res.drawable.dash_sociara,
            "dash_evidara" to Res.drawable.dash_evidara,
            "cover1" to Res.drawable.cover1,
            "cover2" to Res.drawable.cover2,
            "cover3" to Res.drawable.cover3,
            "cover4" to Res.drawable.cover4,
            "cover5" to Res.drawable.cover5,
            "cover6" to Res.drawable.cover6,
            "clarivio_1" to Res.drawable.clarivio_1,
            "clarivio_2" to Res.drawable.clarivio_2,
            "ascendio_1" to Res.drawable.ascendio_1,
            "ascendio_2" to Res.drawable.ascendio_2,
            "sociara_1" to Res.drawable.sociara_1,
            "sociara_2" to Res.drawable.sociara_2,
            "sociara_3" to Res.drawable.sociara_3,
            "evidara_1" to Res.drawable.evidara_1,
            "evidara_2" to Res.drawable.evidara_2
        )
    }

    fun drawable(key: String?): DrawableResource? = key?.let { byKey[it.trim().lowercase()] }

    val keys: Set<String> get() = byKey.keys
}
