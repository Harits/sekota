package id.sekota.core.designsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize

/** Full-width section wrapper matching the design's bands and spacing. */
@Composable
fun SectionBand(
    modifier: Modifier = Modifier,
    subtle: Boolean = false,
    dark: Boolean = false,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    val compact = LocalWindowSize.current == WindowSize.Compact
    val vertical = if (compact) SekotaSpacing.sectionCompact else SekotaSpacing.section
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                when {
                    dark -> SekotaColors.DarkGradient
                    subtle -> SolidColor(SekotaColors.Subtle)
                    else -> SolidColor(SekotaColors.Bg)
                },
            ),
    ) {
        if (dark) {
            Canvas(Modifier.fillMaxSize()) {
                drawGlow(Offset(size.width * 0.05f, size.height * 0.1f), size.minDimension * 0.8f, SekotaColors.Green)
                drawGlow(Offset(size.width * 0.95f, size.height * 0.85f), size.minDimension * 0.9f, SekotaColors.Cyan)
            }
        }
        ContentWrap {
            Column(Modifier.padding(vertical = vertical)) {
                content()
            }
        }
    }
}

private fun DrawScope.drawGlow(center: Offset, radius: Float, color: Color) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color.copy(alpha = 0.22f), Color.Transparent),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
    )
}

/** Browser chrome around a product screenshot. */
@Composable
fun BrowserFrame(
    url: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    aspectRatio: Float? = 16f / 10f,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Column(
        modifier = modifier
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.lg),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(SekotaColors.Subtle)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            repeat(3) {
                Box(Modifier.size(9.dp).clip(SekotaShape.pill).background(Color(0xFFD5DDE3)))
            }
            Spacer(Modifier.size(10.dp))
            Text(
                text = url,
                style = SekotaTheme.type.monoSmall.copy(fontSize = 12.sp),
                color = SekotaColors.Muted,
            )
        }
        val imageModifier = if (aspectRatio != null) {
            Modifier.fillMaxWidth().aspectRatio(aspectRatio)
        } else {
            Modifier.fillMaxWidth()
        }
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = imageModifier,
        )
    }
}

/** Gradient call-to-action used at the bottom of product and book pages. */
@Composable
fun CtaBanner(
    title: String,
    text: String,
    buttonLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val window = LocalWindowSize.current
    val compact = window == WindowSize.Compact || window == WindowSize.Medium
    val reducedMotion = LocalReducedMotion.current

    val transition = rememberInfiniteTransition(label = "cta")
    val phase = if (reducedMotion) {
        0f
    } else {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(7000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "ctaFlow",
        ).value
    }
    val pulse = if (reducedMotion) {
        0f
    } else {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = SekotaEase),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "ctaPulse",
        ).value
    }
    val ringScale = 1f + 0.06f * pulse
    val ringAlpha = 1f - 0.5f * pulse

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(28.dp))
            .background(SekotaColors.CtaGradient),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            // Moving 4-stop gradient (CSS background-size 300% / flow 14s).
            val travel = phase * size.width * 1.8f
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        SekotaColors.Green,
                        SekotaColors.Cyan,
                        Color(0xFF2FBA9A),
                        SekotaColors.Green,
                        SekotaColors.Cyan,
                    ),
                    start = Offset(-size.width * 0.7f + travel, 0f),
                    end = Offset(size.width * 0.9f + travel, size.height * 0.5f),
                ),
            )
            // Three rings share one centre in the design: (width - 120dp, 80dp).
            val center = Offset(size.width - 120.dp.toPx(), 80.dp.toPx())
            val ringColor = Color.White.copy(alpha = 0.22f * ringAlpha)
            listOf(260.dp, 180.dp, 100.dp).forEach { radius ->
                drawCircle(
                    color = ringColor,
                    radius = radius.toPx() * ringScale,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()),
                )
            }
        }
        if (compact) {
            Column(
                Modifier.padding(horizontal = 28.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(title, style = SekotaTheme.type.headline.copy(fontSize = 28.sp, lineHeight = 34.sp), color = Color.White, modifier = Modifier.widthIn(max = 640.dp))
                    Text(text, style = SekotaTheme.type.lead.copy(fontSize = 16.sp, lineHeight = 26.sp), color = Color.White.copy(alpha = 0.92f), modifier = Modifier.widthIn(max = 620.dp))
                }
                SekotaButton(buttonLabel, onClick, variant = ButtonVariant.Secondary, modifier = Modifier.widthIn(min = 220.dp))
            }
        } else {
            Row(
                Modifier.padding(horizontal = 64.dp, vertical = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(48.dp),
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(title, style = responsiveHeadlineStyle(), color = Color.White, modifier = Modifier.widthIn(max = 640.dp))
                    Text(text, style = SekotaTheme.type.lead, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.widthIn(max = 620.dp))
                }
                SekotaButton(buttonLabel, onClick, variant = ButtonVariant.Secondary, modifier = Modifier.widthIn(min = 220.dp))
            }
        }
    }
}
