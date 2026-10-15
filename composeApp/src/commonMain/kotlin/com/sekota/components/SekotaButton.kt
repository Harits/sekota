package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.SekotaColors
import com.sekota.SekotaRadii
import com.sekota.SekotaTheme
import com.sekota.getDmSansFontFamily

/**
 * Standardized Sekota Button Variants (WP-046-2 / #22541)
 *
 * Direct parity with .design_sources/sekota-web/src/components/ui/Button.jsx & Button.css
 */
enum class SekotaButtonVariant {
    Gradient,
    Dark,
    Outline,
    Ghost
}

enum class SekotaButtonSize {
    Medium,
    Small
}

@Composable
fun SekotaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    variant: SekotaButtonVariant = SekotaButtonVariant.Dark,
    size: SekotaButtonSize = SekotaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    gradientText: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isInteractive = enabled && !loading

    // Dimensions based on SekotaButtonSize
    val minHeight: Dp = when (size) {
        SekotaButtonSize.Medium -> 48.dp
        SekotaButtonSize.Small -> 40.dp
    }
    val horizontalPadding: Dp = when (size) {
        SekotaButtonSize.Medium -> 28.dp
        SekotaButtonSize.Small -> 18.dp
    }
    val verticalPadding: Dp = when (size) {
        SekotaButtonSize.Medium -> 12.dp
        SekotaButtonSize.Small -> 8.dp
    }
    val fontSize = when (size) {
        SekotaButtonSize.Medium -> 15.sp
        SekotaButtonSize.Small -> 13.sp
    }

    // Colors and backgrounds
    val defaultContentColor: Color = when (variant) {
        SekotaButtonVariant.Gradient -> Color.White
        SekotaButtonVariant.Dark -> Color.White
        SekotaButtonVariant.Outline -> SekotaColors.Azure11
        SekotaButtonVariant.Ghost -> SekotaColors.Azure11
    }

    val spinnerColor: Color = when (variant) {
        SekotaButtonVariant.Gradient -> Color.White
        SekotaButtonVariant.Dark -> Color.White
        SekotaButtonVariant.Outline -> SekotaColors.CyanActive
        SekotaButtonVariant.Ghost -> SekotaColors.CyanActive
    }

    val shape = SekotaRadii.ShapePill

    val borderStroke: BorderStroke? = when (variant) {
        SekotaButtonVariant.Outline -> BorderStroke(1.5.dp, SekotaColors.Navy10)
        else -> null
    }

    // Base Box container with pill clipping
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .alpha(if (enabled) 1f else 0.4f)
            .clip(shape)
            .then(
                if (borderStroke != null) {
                    Modifier.border(borderStroke, shape)
                } else {
                    Modifier
                }
            )
            .then(
                when (variant) {
                    SekotaButtonVariant.Gradient -> Modifier.background(SekotaColors.GradientPrimary, shape)
                    SekotaButtonVariant.Dark -> Modifier.background(SekotaColors.Azure11, shape)
                    SekotaButtonVariant.Outline -> Modifier.background(Color.Transparent, shape)
                    SekotaButtonVariant.Ghost -> Modifier.background(Color.Transparent, shape)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isInteractive,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = spinnerColor
                )
                if (text != null || content != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            } else if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (content != null) {
                content()
            } else if (text != null) {
                val textStyle = if (gradientText && (variant == SekotaButtonVariant.Outline || variant == SekotaButtonVariant.Ghost)) {
                    TextStyle(
                        brush = SekotaColors.GradientPrimary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = fontSize
                    )
                } else {
                    TextStyle(
                        color = defaultContentColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = fontSize
                    )
                }

                Text(
                    text = text,
                    style = textStyle,
                    maxLines = 1,
                    softWrap = false
                )
            }

            if (!loading && trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

// =========================================================================
// DESKTOP PREVIEWS (WP-046-4 / #22543)
// =========================================================================

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SekotaButtonAllVariantsPreview() {
    SekotaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SekotaColors.PageSurface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Sekota Standardized Button Variants (M3 & Figma Parity)",
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SekotaColors.Azure11
            )

            // Medium (Default)
            Text("Medium Size (48dp height)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SekotaButton(
                    text = "Konsultasi Strategis",
                    variant = SekotaButtonVariant.Gradient,
                    size = SekotaButtonSize.Medium,
                    onClick = {}
                )
                SekotaButton(
                    text = "Akses Sekarang",
                    variant = SekotaButtonVariant.Dark,
                    size = SekotaButtonSize.Medium,
                    onClick = {}
                )
                SekotaButton(
                    text = "Lihat Katalog",
                    variant = SekotaButtonVariant.Outline,
                    gradientText = true,
                    size = SekotaButtonSize.Medium,
                    onClick = {}
                )
                SekotaButton(
                    text = "Pelajari Selengkapnya",
                    variant = SekotaButtonVariant.Ghost,
                    size = SekotaButtonSize.Medium,
                    onClick = {}
                )
            }

            // Small
            Text("Small Size (40dp height)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SekotaButton(
                    text = "Konsultasi",
                    variant = SekotaButtonVariant.Gradient,
                    size = SekotaButtonSize.Small,
                    onClick = {}
                )
                SekotaButton(
                    text = "Masuk",
                    variant = SekotaButtonVariant.Dark,
                    size = SekotaButtonSize.Small,
                    onClick = {}
                )
                SekotaButton(
                    text = "Unduh PDF",
                    variant = SekotaButtonVariant.Outline,
                    size = SekotaButtonSize.Small,
                    onClick = {}
                )
                SekotaButton(
                    text = "Batal",
                    variant = SekotaButtonVariant.Ghost,
                    size = SekotaButtonSize.Small,
                    onClick = {}
                )
            }

            // Loading States
            Text("In-Flight Loading States (Deterministic Spinners)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SekotaButton(
                    text = "Mengirim Formulir...",
                    variant = SekotaButtonVariant.Gradient,
                    loading = true,
                    onClick = {}
                )
                SekotaButton(
                    text = "Memuat Sesi...",
                    variant = SekotaButtonVariant.Dark,
                    loading = true,
                    onClick = {}
                )
                SekotaButton(
                    text = "Sinkronisasi...",
                    variant = SekotaButtonVariant.Outline,
                    loading = true,
                    onClick = {}
                )
            }

            // Disabled States
            Text("Disabled States (0.4f Opacity Non-Interactive)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SekotaButton(
                    text = "Konsultasi Strategis",
                    variant = SekotaButtonVariant.Gradient,
                    enabled = false,
                    onClick = {}
                )
                SekotaButton(
                    text = "Akses Sekarang",
                    variant = SekotaButtonVariant.Dark,
                    enabled = false,
                    onClick = {}
                )
                SekotaButton(
                    text = "Lihat Katalog",
                    variant = SekotaButtonVariant.Outline,
                    enabled = false,
                    onClick = {}
                )
                SekotaButton(
                    text = "Batal",
                    variant = SekotaButtonVariant.Ghost,
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}
