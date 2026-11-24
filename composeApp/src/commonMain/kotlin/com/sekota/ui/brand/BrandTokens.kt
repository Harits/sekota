package com.sekota.ui.brand

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import sekota.composeapp.generated.resources.IBMPlexMono_Medium
import sekota.composeapp.generated.resources.IBMPlexMono_Regular
import sekota.composeapp.generated.resources.PlusJakartaSans_Bold
import sekota.composeapp.generated.resources.PlusJakartaSans_Medium
import sekota.composeapp.generated.resources.PlusJakartaSans_Regular
import sekota.composeapp.generated.resources.PlusJakartaSans_SemiBold
import sekota.composeapp.generated.resources.Res

/**
 * Company profile design tokens (WEB-07-01-01 / #22660).
 *
 * Values mirror the `:root` custom properties of the approved static design, so a
 * token here and its CSS counterpart can always be compared one to one.
 */
object BrandColors {
    val Ink = Color(0xFF0D1F2D)          // --ink: headings, primary text
    val Ink2 = Color(0xFF4B6474)         // --ink-2: body copy, leads
    val Muted = Color(0xFF52707F)        // --muted: mono captions
    val BrandInk = Color(0xFF077C8E)     // --brand-ink: overlines, links on light
    val Background = Color(0xFFFFFFFF)   // --bg
    val Subtle = Color(0xFFF6F9FB)       // --subtle: alternate bands, chips
    val MutedBg = Color(0xFFEDF2F5)      // --muted-bg: tags, toggles
    val Line = Color(0xFFDFE6EB)         // --line: card borders
    val Line2 = Color(0xFFEDF2F5)        // --line-2: dividers, hero grid
    val Dark = Color(0xFF06131D)         // --dark: footer, platform band
    val Dark2 = Color(0xFF0D1F2D)        // --dark-2
    val DarkEnd = Color(0xFF0F2A3B)      // end stop of the platform band gradient
    val OnDarkMuted = Color(0xFF9FB3C1)  // --on-dark-muted
    val Green = Color(0xFF60BD65)        // --g1
    val Cyan = Color(0xFF02B6CF)         // --g2
    val CheckGreen = Color(0xFF43A34A)   // checklist ticks
    val TileIcon = Color(0xFF0298AD)     // icon colour inside gradient tiles
    val TagGreenBg = Color(0xFFE7F5E8)
    val TagGreen = Color(0xFF2E7D32)
    val TagCyanBg = Color(0xFFE6F8FB)
    val CtaMint = Color(0xFF2FBA9A)      // third stop of the partner CTA gradient
}

object BrandBrushes {
    /** --grad: linear-gradient(120deg, g1, g2). */
    val Primary: Brush = Brush.linearGradient(
        colors = listOf(BrandColors.Green, BrandColors.Cyan),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /** Horizontal variant for gradient text, where a diagonal looks uneven on one line. */
    val PrimaryHorizontal: Brush = Brush.horizontalGradient(listOf(BrandColors.Green, BrandColors.Cyan))

    /** .dark band: linear-gradient(140deg, #06131D 30%, #0F2A3B). */
    val DarkBand: Brush = Brush.linearGradient(
        0.3f to BrandColors.Dark,
        1f to BrandColors.DarkEnd,
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /** .ctab partner banner. */
    val PartnerCta: Brush = Brush.linearGradient(
        colors = listOf(BrandColors.Green, BrandColors.Cyan, BrandColors.CtaMint),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /** .tile background: the brand gradient at 12% opacity. */
    val Tile: Brush = Brush.linearGradient(
        listOf(BrandColors.Green.copy(alpha = 0.12f), BrandColors.Cyan.copy(alpha = 0.12f))
    )
}

object BrandShapes {
    val Card = RoundedCornerShape(20.dp)        // --r
    val CardLarge = RoundedCornerShape(24.dp)   // suite, platform tiles
    val Banner = RoundedCornerShape(28.dp)      // partner CTA
    val Faq = RoundedCornerShape(16.dp)
    val Button = RoundedCornerShape(12.dp)
    val Tile = RoundedCornerShape(12.dp)
    val Toggle = RoundedCornerShape(10.dp)
    val Cover = RoundedCornerShape(10.dp)
    val Pill = RoundedCornerShape(999.dp)
}

object BrandLayout {
    /** --max: content never grows past this. */
    val MaxContentWidth: Dp = 1200.dp
    val Gutter: Dp = 24.dp
    val NavHeight: Dp = 80.dp
    val CardGap: Dp = 24.dp
    val SectionHeaderGap: Dp = 56.dp

    /** .band padding: 112px, 72px below 860px. */
    fun bandPadding(width: Dp): Dp = if (width < 860.dp) 72.dp else 112.dp

    /** Card grids (.cards4, .steps): 4 → 2 (≤1100px) → 1 (≤600px). */
    fun fourUpColumns(width: Dp): Int = when {
        width <= 600.dp -> 1
        width <= 1100.dp -> 2
        else -> 4
    }

    /** Book shelf: 6 → 3 (≤1100px) → 2 (≤600px). */
    fun shelfColumns(width: Dp): Int = when {
        width <= 600.dp -> 2
        width <= 1100.dp -> 3
        else -> 6
    }

    /** Hero, platform tiles and FAQ stack below 1100px / 860px respectively. */
    fun isStacked(width: Dp): Boolean = width <= 1100.dp
    fun isNarrow(width: Dp): Boolean = width <= 860.dp
}

@Composable
fun jakartaFontFamily(): FontFamily = FontFamily(
    Font(Res.font.PlusJakartaSans_Regular, FontWeight.Normal),
    Font(Res.font.PlusJakartaSans_Medium, FontWeight.Medium),
    Font(Res.font.PlusJakartaSans_SemiBold, FontWeight.SemiBold),
    Font(Res.font.PlusJakartaSans_Bold, FontWeight.Bold)
)

@Composable
fun plexMonoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.IBMPlexMono_Regular, FontWeight.Normal),
    Font(Res.font.IBMPlexMono_Medium, FontWeight.Medium)
)

/**
 * Type scale of the company profile. Headings use weight 600 and tight tracking
 * (h1 -0.035em, others -0.02em); the CSS clamp() sizes are resolved per width class.
 */
class BrandTypography(private val sans: FontFamily, private val monoFamily: FontFamily, compact: Boolean) {
    private fun s(size: TextUnit, line: TextUnit, weight: FontWeight = FontWeight.Normal, spacing: TextUnit = TextUnit.Unspecified, family: FontFamily = sans) =
        TextStyle(fontFamily = family, fontSize = size, lineHeight = line, fontWeight = weight, letterSpacing = spacing)

    val display = s(if (compact) 36.sp else 56.sp, if (compact) 41.sp else 64.sp, FontWeight.SemiBold, (-0.035).em)
    val h2 = s(if (compact) 30.sp else 40.sp, if (compact) 36.sp else 48.sp, FontWeight.SemiBold, (-0.02).em)
    val h3 = s(20.sp, 28.sp, FontWeight.SemiBold, (-0.02).em)
    val h4 = s(17.sp, 24.sp, FontWeight.SemiBold, (-0.02).em)
    val lead = s(18.sp, 30.sp)
    val body = s(16.sp, 26.sp)
    val bodyStrong = s(16.sp, 26.sp, FontWeight.SemiBold)
    val small = s(14.sp, 22.sp)
    val smallStrong = s(14.sp, 22.sp, FontWeight.Medium)
    val caption = s(12.sp, 16.sp)
    val button = s(15.sp, 20.sp, FontWeight.SemiBold)
    val nav = s(14.sp, 20.sp, FontWeight.Medium)
    val overline = s(12.sp, 16.sp, FontWeight.Medium, 0.13.em, monoFamily)
    val mono = s(13.sp, 18.sp, FontWeight.Normal, TextUnit.Unspecified, monoFamily)
    val stepNumber = s(40.sp, 48.sp, FontWeight.SemiBold, (-0.03).em)
}

@Composable
fun rememberBrandTypography(compact: Boolean = false): BrandTypography =
    BrandTypography(jakartaFontFamily(), plexMonoFontFamily(), compact)
