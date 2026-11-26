package id.sekota.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Design tokens lifted from the Sekota landing page (Design System v2).
 * Ink #0D1F2D, brand gradient #60BD65 -> #02B6CF.
 */
object SekotaColors {
    val Ink = Color(0xFF0D1F2D)
    val Ink2 = Color(0xFF4B6474)
    val Muted = Color(0xFF52707F)
    val BrandInk = Color(0xFF077C8E)

    val Bg = Color(0xFFFFFFFF)
    val Subtle = Color(0xFFF6F9FB)
    val MutedBg = Color(0xFFEDF2F5)
    val Line = Color(0xFFDFE6EB)
    val Line2 = Color(0xFFEDF2F5)

    val Dark = Color(0xFF06131D)
    val Dark2 = Color(0xFF0D1F2D)
    val OnDarkMuted = Color(0xFF9FB3C1)

    val Green = Color(0xFF60BD65)
    val Cyan = Color(0xFF02B6CF)
    val Teal = Color(0xFF0298AD)

    val Success = Color(0xFF43A34A)
    val SuccessBg = Color(0xFFE7F5E8)
    val SuccessInk = Color(0xFF2E7D32)
    val CyanBg = Color(0xFFE6F8FB)
    val Required = Color(0xFFC2410C)

    val GradientStops = listOf(Green, Cyan)

    val BrandGradient = Brush.linearGradient(
        colors = GradientStops,
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    val BrandGradientSoft = Brush.linearGradient(
        colors = listOf(Green.copy(alpha = 0.12f), Cyan.copy(alpha = 0.12f)),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    val DarkGradient = Brush.linearGradient(
        colors = listOf(Dark, Color(0xFF0F2A3B)),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    val CtaGradient = Brush.linearGradient(
        colors = listOf(Green, Cyan, Color(0xFF2FBA9A), Green),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    val GlowCyan = Color(0x6102B6CF)
    val GlowGreen = Color(0x5260BD65)
}

@Immutable
data class SekotaPalette(
    val ink: Color = SekotaColors.Ink,
    val ink2: Color = SekotaColors.Ink2,
    val muted: Color = SekotaColors.Muted,
    val brandInk: Color = SekotaColors.BrandInk,
    val bg: Color = SekotaColors.Bg,
    val subtle: Color = SekotaColors.Subtle,
    val mutedBg: Color = SekotaColors.MutedBg,
    val line: Color = SekotaColors.Line,
    val line2: Color = SekotaColors.Line2,
    val dark: Color = SekotaColors.Dark,
    val dark2: Color = SekotaColors.Dark2,
    val onDarkMuted: Color = SekotaColors.OnDarkMuted,
    val green: Color = SekotaColors.Green,
    val cyan: Color = SekotaColors.Cyan,
    val success: Color = SekotaColors.Success,
)
