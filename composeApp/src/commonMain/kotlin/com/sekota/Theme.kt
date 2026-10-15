package com.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Unified Sekota Design Tokens (WP-046 / #22540)
 *
 * Sourced directly from .design_sources/sekota-web/src/styles/tokens.css
 * Guarantees visual harmony across Sekota Web, CMS Workbench, and Mobile targets.
 */
object SekotaColors {
    // ---------- Color: Brand ----------
    val Azure11 = Color(0xFF0D1F2D)      // Navy — headlines, dark buttons, "Mengapa" section
    val Azure17 = Color(0xFF143244)      // Ink — paragraph text (used at 40-70% opacity)
    val White = Color(0xFFFFFFFF)
    val Grey98 = Color(0xFFF7FAFB)       // Soft section background
    val PageSurface = Color(0xFFF7F9FB)  // Catalog and details page surface
    val Cyan39 = Color(0xFF00B5C8)
    val CyanActive = Color(0xFF02B6CF)   // Active menu, secondary button
    val BrandGreen = Color(0xFF60BD65)
    val Teal = Color(0xFF35A190)

    // Legacy aliases for backward compatibility across existing screens
    val InkNavy = Azure11
    val BrandTeal = Cyan39

    // ---------- Color: Product Accents ----------
    val Veridia = Color(0xFF35A190)
    val Ascendio = Color(0xFF378ADD)
    val Sociara = Color(0xFF187D61)
    val Ecoflow = Color(0xFF4EA547)

    // ---------- Color: Status (Kartu Tantangan & Feedback) ----------
    val Danger = Color(0xFFE24B4A)
    val Warning = Color(0xFFEF9F27)
    val Info = Color(0xFF378ADD)
    val Success = BrandGreen

    // ---------- Color: UI Catalog & Surfaces ----------
    val CatalogInk = Color(0xFF191C1E)
    val CatalogMuted = Color(0xFF464555)
    val CatalogSub = Color(0xFF565E74)
    val Surface = Color(0xFFF2F4F6)
    val Surface2 = Color(0xFFF1F5F9)
    val Surface3 = Color(0xFFF4F4F4)
    val EbookCard = Color(0xFFF7F7F7)
    val BorderLilac = Color(0xFFC7C4D8)
    val Track = Color(0xFFE0E3E5)
    val FooterGrey = Color(0xFF8998A1)

    // ---------- Color: Opacity Turunan (Ink & Navy) ----------
    val Ink85 = Color(0xD9143244) // rgba(20, 50, 68, 0.85)
    val Ink70 = Color(0xB3143244) // rgba(20, 50, 68, 0.70)
    val Ink65 = Color(0xA6143244) // rgba(20, 50, 68, 0.65)
    val Ink60 = Color(0x99143244) // rgba(20, 50, 68, 0.60)
    val Ink50 = Color(0x80143244) // rgba(20, 50, 68, 0.50)
    val Ink40 = Color(0x66143244) // rgba(20, 50, 68, 0.40)
    val Ink30 = Color(0x4D143244) // rgba(20, 50, 68, 0.30)

    val Navy80 = Color(0xCC0D1F2D) // rgba(13, 31, 45, 0.80)
    val Navy30 = Color(0x4D0D1F2D) // rgba(13, 31, 45, 0.30)
    val Navy20 = Color(0x330D1F2D) // rgba(13, 31, 45, 0.20)
    val Navy10 = Color(0x1A0D1F2D) // rgba(13, 31, 45, 0.10)
    val Navy08 = Color(0x140D1F2D) // rgba(13, 31, 45, 0.08)
    val Navy05 = Color(0x0D0D1F2D) // rgba(13, 31, 45, 0.05)
    val Navy03 = Color(0x080D1F2D) // rgba(13, 31, 45, 0.03)

    val Lilac30 = Color(0x4DC7C4D8) // rgba(199, 196, 216, 0.30)
    val Lilac50 = Color(0x80C7C4D8) // rgba(199, 196, 216, 0.50)

    val White80 = Color(0xCCFFFFFF) // rgba(255, 255, 255, 0.80)
    val White50 = Color(0x80FFFFFF) // rgba(255, 255, 255, 0.50)
    val White40 = Color(0x66FFFFFF) // rgba(255, 255, 255, 0.40)
    val White20 = Color(0x33FFFFFF) // rgba(255, 255, 255, 0.20)
    val White05 = Color(0x0DFFFFFF) // rgba(255, 255, 255, 0.05)

    // ---------- Gradients ----------
    val GradientPrimary = Brush.linearGradient(listOf(BrandGreen, CyanActive))
    val GradientBadge = Brush.linearGradient(listOf(BrandGreen, CyanActive))
    val GradientEbookBlue = Brush.linearGradient(listOf(Color(0xFF185FA5), Color(0xFF378ADD)))
    val GradientEbookOrange = Brush.linearGradient(listOf(Color(0xFF993C1D), Color(0xFFD85A30)))
    val GradientEbookGreen = Brush.linearGradient(listOf(Color(0xFF3B6D11), Color(0xFF639922)))
    val GradientEbookViolet = Brush.linearGradient(listOf(Color(0xFF534AB7), Color(0xFF7F77DD)))
}

// Top-level color aliases for package-wide backward compatibility
val InkNavy: Color = SekotaColors.Azure11
val BrandTeal: Color = SekotaColors.Cyan39
val BrandGreen: Color = SekotaColors.BrandGreen


/**
 * Sekota Corner Radii Tokens
 */
object SekotaRadii {
    val Xs: Dp = 4.dp
    val Sm: Dp = 8.dp
    val Md: Dp = 12.dp
    val Lg: Dp = 16.dp
    val Xl: Dp = 24.dp
    val Xl2: Dp = 32.dp
    val Xl3: Dp = 40.dp
    val Pill: Dp = 9999.dp

    val ShapeXs = RoundedCornerShape(Xs)
    val ShapeSm = RoundedCornerShape(Sm)
    val ShapeMd = RoundedCornerShape(Md)
    val ShapeLg = RoundedCornerShape(Lg)
    val ShapeXl = RoundedCornerShape(Xl)
    val Shape2Xl = RoundedCornerShape(Xl2)
    val Shape3Xl = RoundedCornerShape(Xl3)
    val ShapePill = RoundedCornerShape(50)
}

/**
 * Sekota Elevation Palette
 */
object SekotaElevation {
    val None: Dp = 0.dp
    val Card: Dp = 1.dp
    val CardUi: Dp = 2.dp
    val Hover: Dp = 6.dp
    val Float: Dp = 12.dp
    val Cover: Dp = 16.dp
    val Modal: Dp = 24.dp
}

/**
 * In-App Reader Focus Mode Tokens (WP-047 / #22540)
 */
enum class ReaderFocusMode {
    Day,
    Paper,
    Night
}

data class ReaderFocusPalette(
    val surface: Color,
    val text: Color,
    val mutedText: Color,
    val accent: Color,
    val border: Color
)

fun ReaderFocusMode.palette(): ReaderFocusPalette = when (this) {
    ReaderFocusMode.Day -> ReaderFocusPalette(
        surface = SekotaColors.White,
        text = SekotaColors.Azure17,
        mutedText = SekotaColors.CatalogSub,
        accent = SekotaColors.Cyan39,
        border = SekotaColors.Navy08
    )
    ReaderFocusMode.Paper -> ReaderFocusPalette(
        surface = Color(0xFFFAF8F5),
        text = SekotaColors.Azure17,
        mutedText = Color(0xFF64748B),
        accent = SekotaColors.Teal,
        border = Color(0xFFE5DEC9)
    )
    ReaderFocusMode.Night -> ReaderFocusPalette(
        surface = SekotaColors.Azure11,
        text = Color(0xFFE2E8F0),
        mutedText = Color(0xFF94A3B8),
        accent = SekotaColors.CyanActive,
        border = SekotaColors.Navy30
    )
}

val LocalSekotaColors = staticCompositionLocalOf { SekotaColors }
val LocalReaderFocusMode = compositionLocalOf { ReaderFocusMode.Day }

/**
 * Sekota M3 Unified Theme Wrapper
 */
@Composable
fun SekotaTheme(
    focusMode: ReaderFocusMode = ReaderFocusMode.Day,
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = SekotaColors.Cyan39,
        onPrimary = SekotaColors.White,
        primaryContainer = SekotaColors.Grey98,
        onPrimaryContainer = SekotaColors.Azure11,
        secondary = SekotaColors.BrandGreen,
        onSecondary = SekotaColors.White,
        tertiary = SekotaColors.Teal,
        onTertiary = SekotaColors.White,
        background = SekotaColors.PageSurface,
        onBackground = SekotaColors.Azure11,
        surface = SekotaColors.White,
        onSurface = SekotaColors.Azure11,
        surfaceVariant = SekotaColors.Grey98,
        onSurfaceVariant = SekotaColors.Azure17,
        outline = SekotaColors.Navy10,
        error = SekotaColors.Danger,
        onError = SekotaColors.White
    )

    androidx.compose.runtime.CompositionLocalProvider(
        LocalSekotaColors provides SekotaColors,
        LocalReaderFocusMode provides focusMode
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

// =========================================================================
// DESKTOP PREVIEWS (WP-046-4 / #22543)
// =========================================================================

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SekotaColorPalettePreview() {
    SekotaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SekotaColors.PageSurface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Sekota Unified Design Tokens — Brand & Products",
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = SekotaColors.Azure11
            )

            Text("Brand Colors", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ColorChip("Azure 11", SekotaColors.Azure11, textColor = Color.White)
                ColorChip("Azure 17", SekotaColors.Azure17, textColor = Color.White)
                ColorChip("Cyan 39", SekotaColors.Cyan39, textColor = Color.White)
                ColorChip("Cyan Active", SekotaColors.CyanActive, textColor = Color.White)
                ColorChip("Brand Green", SekotaColors.BrandGreen, textColor = Color.White)
                ColorChip("Teal", SekotaColors.Teal, textColor = Color.White)
            }

            Text("Product Accents", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ColorChip("Veridia", SekotaColors.Veridia, textColor = Color.White)
                ColorChip("Ascendio", SekotaColors.Ascendio, textColor = Color.White)
                ColorChip("Sociara", SekotaColors.Sociara, textColor = Color.White)
                ColorChip("Ecoflow", SekotaColors.Ecoflow, textColor = Color.White)
            }

            Text("Opacity / Ink Tints", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ColorChip("Ink 85", SekotaColors.Ink85, textColor = Color.White)
                ColorChip("Ink 70", SekotaColors.Ink70, textColor = Color.White)
                ColorChip("Navy 20", SekotaColors.Navy20, textColor = SekotaColors.Azure11)
                ColorChip("Navy 05", SekotaColors.Navy05, textColor = SekotaColors.Azure11)
                ColorChip("Grey 98", SekotaColors.Grey98, textColor = SekotaColors.Azure11)
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ReaderFocusModesPreview() {
    SekotaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E8F0))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reader Focus Modes (Day, Paper, Night)",
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SekotaColors.Azure11
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FocusModeSampleCard(
                    title = "Mode Day (Default)",
                    mode = ReaderFocusMode.Day,
                    modifier = Modifier.weight(1f)
                )
                FocusModeSampleCard(
                    title = "Mode Paper (Warm Sepia)",
                    mode = ReaderFocusMode.Paper,
                    modifier = Modifier.weight(1f)
                )
                FocusModeSampleCard(
                    title = "Mode Night (Immersive Dark)",
                    mode = ReaderFocusMode.Night,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ColorChip(
    label: String,
    color: Color,
    textColor: Color = Color.Black
) {
    Box(
        modifier = Modifier
            .size(width = 110.dp, height = 54.dp)
            .clip(SekotaRadii.ShapeSm)
            .background(color)
            .border(1.dp, SekotaColors.Navy10, SekotaRadii.ShapeSm)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = getDmSansFontFamily(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun FocusModeSampleCard(
    title: String,
    mode: ReaderFocusMode,
    modifier: Modifier = Modifier
) {
    val palette = mode.palette()
    Surface(
        modifier = modifier
            .clip(SekotaRadii.ShapeMd)
            .border(1.dp, palette.border, SekotaRadii.ShapeMd),
        color = palette.surface,
        shadowElevation = SekotaElevation.Card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = palette.accent
            )
            Text(
                text = "Bab 1: Menavigasi Ekosistem Riset Sekota",
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = palette.text
            )
            Text(
                text = "Penerapan metodologi sistematis menghadirkan sintesis data yang konsisten dan meminimalkan distraksi visual...",
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = palette.mutedText,
                lineHeight = 16.sp
            )
        }
    }
}
