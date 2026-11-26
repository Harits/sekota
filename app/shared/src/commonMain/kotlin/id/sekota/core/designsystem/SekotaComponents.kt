package id.sekota.core.designsystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.InputMode
import id.sekota.core.model.Language
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize
import kotlinx.coroutines.delay

enum class ButtonVariant { Primary, Secondary }
enum class ButtonSize { Regular, Small }

val SekotaEase = FastOutSlowInEasing

/** Centered 1200dp container with the design's 24dp gutters. */
@Composable
fun ContentWrap(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val compact = LocalWindowSize.current == WindowSize.Compact
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = SekotaSpacing.maxContentWidth)
                .fillMaxWidth()
                .padding(horizontal = if (compact) SekotaSpacing.gutterCompact else SekotaSpacing.gutter),
            content = content,
        )
    }
}

@Composable
fun SekotaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Regular,
    trailingIcon: ImageVector? = SekotaIcons.ArrowRight,
    enabled: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val focused by interaction.collectIsFocusedAsState()
    val pressed by interaction.collectIsPressedAsState()
    val keyboardMode = LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val reducedMotion = LocalReducedMotion.current
    val height = if (size == ButtonSize.Small) 44.dp else 52.dp
    val shape = SekotaShape.md
    val radius = 12.dp

    val lift by animateDpAsState(
        targetValue = if (hovered && enabled) 1.dp else 0.dp,
        animationSpec = tween(300, easing = SekotaEase),
        label = "buttonLift",
    )
    val pressScale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.98f else 1f,
        animationSpec = tween(140),
        label = "buttonPressScale",
    )
    val hoverShift by animateFloatAsState(
        targetValue = if (hovered && enabled && !reducedMotion) 1f else 0f,
        animationSpec = tween(500, easing = SekotaEase),
        label = "buttonGradientShift",
    )
    val sheen by animateFloatAsState(
        targetValue = if (hovered && enabled && !reducedMotion) 1f else 0f,
        animationSpec = tween(800, easing = SekotaEase),
        label = "buttonSheen",
    )
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.55f,
        animationSpec = tween(200),
        label = "buttonAlpha",
    )
    val isPrimary = variant == ButtonVariant.Primary
    val contentColor = if (isPrimary) Color.White else SekotaColors.Ink
    val borderColor by animateColorAsState(
        targetValue = if (isPrimary) Color.Transparent else if (hovered) Color(0xFFB9C6CE) else SekotaColors.Line,
        animationSpec = tween(350, easing = SekotaEase),
        label = "buttonBorder",
    )
    val showFocus = focused && keyboardMode

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = -lift.toPx()
                scaleX = pressScale
                scaleY = pressScale
                this.alpha = alpha
            }
            .height(height)
            .shadow(
                elevation = when {
                    isPrimary && hovered -> 14.dp
                    isPrimary -> 8.dp
                    else -> 2.dp
                },
                shape = shape,
                ambientColor = if (isPrimary) SekotaColors.Cyan.copy(alpha = 0.35f) else SekotaColors.Ink.copy(alpha = 0.08f),
                spotColor = if (isPrimary) SekotaColors.Cyan.copy(alpha = 0.35f) else SekotaColors.Ink.copy(alpha = 0.08f),
            )
            .clip(shape)
            .drawWithContent {
                val drawSize = this.size
                if (isPrimary) {
                    // background-size:160%; background-position 0 -> 100% on hover
                    val shift = drawSize.width * 0.6f * hoverShift
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = SekotaColors.GradientStops,
                            start = Offset(-drawSize.width * 0.3f + shift, 0f),
                            end = Offset(drawSize.width * 0.7f + shift, drawSize.height),
                        ),
                        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    )
                } else {
                    drawRoundRect(
                        color = Color.White,
                        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    )
                }
                drawContent()
                if (isPrimary && sheen > 0.001f) {
                    val x = (sheen * 2.4f - 1.2f) * drawSize.width
                    val band = drawSize.width * 0.35f
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.35f),
                                Color.Transparent,
                            ),
                            start = Offset(x - band, 0f),
                            end = Offset(x + band, drawSize.height * 0.55f),
                        ),
                    )
                }
                if (showFocus) {
                    drawRoundRect(
                        color = SekotaColors.Ink,
                        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                        style = Stroke(2.dp.toPx()),
                    )
                }
            }
            .border(
                width = if (showFocus) 0.dp else 1.dp,
                color = if (showFocus) Color.Transparent else borderColor,
                shape = shape,
            )
            .hoverable(interaction)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = if (size == ButtonSize.Small) 20.dp else 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = SekotaTheme.type.button,
                color = contentColor,
            )
            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .size(18.dp)
                        .graphicsLayer {
                            translationX = if (hovered) 3.dp.toPx() else 0f
                        },
                )
            }
        }
    }
}

@Composable
fun SekotaChip(
    text: String,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    showDot: Boolean = true,
) {
    Row(
        modifier = modifier
            .clip(SekotaShape.pill)
            .background(if (dark) Color.White.copy(alpha = 0.06f) else Color.White)
            .border(
                width = 1.dp,
                color = if (dark) Color.White.copy(alpha = 0.14f) else SekotaColors.Line,
                shape = SekotaShape.pill,
            )
            .padding(start = if (showDot) 12.dp else 14.dp, end = 14.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showDot) {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(SekotaShape.pill)
                    .background(SekotaColors.BrandGradient),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = SekotaTheme.type.caption.copy(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium),
            color = if (dark) SekotaColors.OnDarkMuted else SekotaColors.Ink2,
        )
    }
}

@Composable
fun Overline(text: String, dark: Boolean = false, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = SekotaTheme.type.monoOverline,
        color = if (dark) SekotaColors.OnDarkMuted else SekotaColors.BrandInk,
        modifier = modifier,
    )
}

/** Matches the design's `clamp(30px, 3.4vw, 40px)` heading scale. */
@Composable
fun responsiveHeadlineStyle(): TextStyle {
    val base = SekotaTheme.type.headline
    return when (LocalWindowSize.current) {
        WindowSize.Compact -> base.copy(fontSize = 30.sp, lineHeight = 36.sp)
        WindowSize.Medium -> base.copy(fontSize = 30.sp, lineHeight = 36.sp)
        WindowSize.Wide -> base.copy(fontSize = 32.sp, lineHeight = 38.sp)
        WindowSize.Expanded -> base
    }
}

@Composable
fun SectionHeader(
    overline: String,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    dark: Boolean = false,
    titleStyle: TextStyle? = null,
    centered: Boolean = false,
) {
    val headline = titleStyle ?: responsiveHeadlineStyle()
    Column(
        modifier = modifier.widthIn(max = 680.dp),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        Overline(overline, dark = dark)
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            style = headline,
            color = if (dark) Color.White else SekotaColors.Ink,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
            modifier = Modifier.semantics { heading() },
        )
        if (subtitle != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = subtitle,
                style = SekotaTheme.type.lead,
                modifier = Modifier.widthIn(max = 620.dp),
                color = if (dark) SekotaColors.OnDarkMuted else SekotaColors.Ink2,
                textAlign = if (centered) TextAlign.Center else TextAlign.Start,
            )
        }
    }
}

@Composable
fun IconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    dark: Boolean = false,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(SekotaShape.md)
            .background(
                if (dark) SolidColor(Color.White.copy(alpha = 0.06f)) else SekotaColors.BrandGradientSoft,
            )
            .border(1.dp, if (dark) Color.White.copy(alpha = 0.1f) else SekotaColors.Line2, SekotaShape.md),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SekotaColors.Teal,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun GradientText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = style.copy(brush = SekotaColors.BrandGradient),
    )
}

/**
 * Fade-and-rise entrance used across the landing page. Items animate when they
 * enter composition, which maps to scrolling them into view inside the LazyColumn.
 */
@Composable
fun Reveal(
    delayMillis: Int = 0,
    distance: Dp = 16.dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val reduce = LocalReducedMotion.current
    var visible by remember { mutableStateOf(reduce) }
    LaunchedEffect(Unit) {
        if (reduce) {
            visible = true
        } else {
            if (delayMillis > 0) delay(delayMillis.toLong())
            visible = true
        }
    }
    val progress = remember(reduce) { Animatable(if (reduce) 1f else 0f) }
    LaunchedEffect(visible, reduce) {
        if (reduce) {
            progress.snapTo(1f)
        } else if (visible) {
            progress.animateTo(1f, tween(700, easing = SekotaEase))
        }
    }
    Box(
        modifier = modifier.graphicsLayer {
            alpha = progress.value
            translationY = (1f - progress.value) * distance.toPx()
        },
        content = content,
    )
}

/** Hover lift for cards; returns the interaction source so callers can reuse it. */
@Composable
fun rememberCardHover(): Pair<MutableInteractionSource, Boolean> {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    return source to hovered
}

@Composable
fun SekotaCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    background: Color = Color.White,
    borderColor: Color = SekotaColors.Line,
    shape: RoundedCornerShape = SekotaShape.xl,
    hoveredBorder: Color = SekotaColors.Cyan.copy(alpha = 0.45f),
    content: @Composable ColumnScope.() -> Unit,
) {
    val (interaction, hovered) = rememberCardHover()
    val focused by interaction.collectIsFocusedAsState()
    val keyboardMode = LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val lift by animateDpAsState(
        targetValue = if (hovered) 5.dp else 0.dp,
        animationSpec = tween(400, easing = SekotaEase),
        label = "cardLift",
    )
    val stroke by animateColorAsState(if (hovered) hoveredBorder else borderColor, tween(350), label = "cardStroke")
    Column(
        modifier = modifier
            .graphicsLayer { translationY = -lift.toPx() }
            .clip(shape)
            .background(background, shape)
            .border(
                width = if (focused && keyboardMode) 2.dp else 1.dp,
                color = if (focused && keyboardMode) SekotaColors.Ink else stroke,
                shape = shape,
            )
            .then(
                if (onClick != null) {
                    Modifier
                        .hoverable(interaction)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClick = onClick,
                        )
                } else {
                    Modifier.hoverable(interaction)
                },
            ),
        content = content,
    )
}

/** Tiny wrapper documenting the intended animation duration for reveals. */
@Composable
fun rememberEnterProgress(key: Any? = Unit): Float {
    val anim = remember(key) { Animatable(0f) }
    LaunchedEffect(key) { anim.animateTo(1f, tween(700, easing = SekotaEase)) }
    return anim.value
}

/** Convenience for the design's "subtle" bands. */
@Composable
fun BandBackground(subtle: Boolean = false, dark: Boolean = false, content: @Composable BoxScope.() -> Unit) {
    val brush = when {
        dark -> SekotaColors.DarkGradient
        else -> SolidColor(if (subtle) SekotaColors.Subtle else SekotaColors.Bg)
    }
    Box(Modifier.fillMaxWidth().background(brush), content = content)
}

/** Language label used by the ID/EN switch. */
fun Language.switchLabel(): String = label

@Composable
fun VSpace(height: Dp) = Spacer(Modifier.height(height))

@Composable
fun HSpace(width: Dp) = Spacer(Modifier.width(width))
