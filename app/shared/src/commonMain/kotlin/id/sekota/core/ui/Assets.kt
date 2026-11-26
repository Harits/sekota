package id.sekota.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.sekota.core.model.BookSlug
import id.sekota.core.model.ProductSlug
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.app.shared.generated.resources.Res
import sekota.app.shared.generated.resources.cover_1
import sekota.app.shared.generated.resources.cover_2
import sekota.app.shared.generated.resources.cover_3
import sekota.app.shared.generated.resources.cover_4
import sekota.app.shared.generated.resources.cover_5
import sekota.app.shared.generated.resources.cover_6
import sekota.app.shared.generated.resources.dash_ascendio
import sekota.app.shared.generated.resources.dash_clarivio
import sekota.app.shared.generated.resources.dash_evidara
import sekota.app.shared.generated.resources.dash_sociara
import sekota.app.shared.generated.resources.logo_ascendio_dark
import sekota.app.shared.generated.resources.logo_ascendio_light
import sekota.app.shared.generated.resources.logo_clarivio_dark
import sekota.app.shared.generated.resources.logo_clarivio_light
import sekota.app.shared.generated.resources.logo_evidara_dark
import sekota.app.shared.generated.resources.logo_evidara_light
import sekota.app.shared.generated.resources.logo_sekota_dark
import sekota.app.shared.generated.resources.logo_sekota_light
import sekota.app.shared.generated.resources.logo_sociara_dark
import sekota.app.shared.generated.resources.logo_sociara_light
import sekota.app.shared.generated.resources.shot_ascendio_1
import sekota.app.shared.generated.resources.shot_ascendio_2
import sekota.app.shared.generated.resources.shot_clarivio_1
import sekota.app.shared.generated.resources.shot_clarivio_2
import sekota.app.shared.generated.resources.shot_evidara_1
import sekota.app.shared.generated.resources.shot_evidara_2
import sekota.app.shared.generated.resources.shot_sociara_1
import sekota.app.shared.generated.resources.shot_sociara_2
import sekota.app.shared.generated.resources.shot_sociara_3

fun ProductSlug.logoLight(): DrawableResource = when (this) {
    ProductSlug.Clarivio -> Res.drawable.logo_clarivio_light
    ProductSlug.Ascendio -> Res.drawable.logo_ascendio_light
    ProductSlug.Sociara -> Res.drawable.logo_sociara_light
    ProductSlug.Evidara -> Res.drawable.logo_evidara_light
}

fun ProductSlug.logoDark(): DrawableResource = when (this) {
    ProductSlug.Clarivio -> Res.drawable.logo_clarivio_dark
    ProductSlug.Ascendio -> Res.drawable.logo_ascendio_dark
    ProductSlug.Sociara -> Res.drawable.logo_sociara_dark
    ProductSlug.Evidara -> Res.drawable.logo_evidara_dark
}

fun ProductSlug.dashboard(): DrawableResource = when (this) {
    ProductSlug.Clarivio -> Res.drawable.dash_clarivio
    ProductSlug.Ascendio -> Res.drawable.dash_ascendio
    ProductSlug.Sociara -> Res.drawable.dash_sociara
    ProductSlug.Evidara -> Res.drawable.dash_evidara
}

fun ProductSlug.screens(): List<DrawableResource> = when (this) {
    ProductSlug.Clarivio -> listOf(Res.drawable.shot_clarivio_1, Res.drawable.shot_clarivio_2)
    ProductSlug.Ascendio -> listOf(Res.drawable.shot_ascendio_1, Res.drawable.shot_ascendio_2)
    ProductSlug.Sociara -> listOf(Res.drawable.shot_sociara_1, Res.drawable.shot_sociara_2, Res.drawable.shot_sociara_3)
    ProductSlug.Evidara -> listOf(Res.drawable.shot_evidara_1, Res.drawable.shot_evidara_2)
}

fun BookSlug.cover(): DrawableResource = when (this) {
    BookSlug.ForgettingEnterprise -> Res.drawable.cover_1
    BookSlug.BlindSpotRadar -> Res.drawable.cover_2
    BookSlug.ManifestoEkuitasLahan -> Res.drawable.cover_3
    BookSlug.ImpactDrivenCsr -> Res.drawable.cover_4
    BookSlug.AccountableEsg -> Res.drawable.cover_5
    BookSlug.SustainabilityLeadership -> Res.drawable.cover_6
}

@Composable
fun SekotaLogo(dark: Boolean = false, height: Dp = 36.dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(if (dark) Res.drawable.logo_sekota_dark else Res.drawable.logo_sekota_light),
        contentDescription = "Sekota",
        modifier = modifier.height(height),
    )
}

@Composable
fun ProductLogo(
    slug: ProductSlug,
    dark: Boolean = false,
    height: Dp = 22.dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = slug.name,
) {
    Image(
        painter = painterResource(if (dark) slug.logoDark() else slug.logoLight()),
        contentDescription = contentDescription,
        modifier = modifier.height(height),
    )
}
