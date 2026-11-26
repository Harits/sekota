package id.sekota.feature.landing.presentation.section

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.core.designsystem.IconTile
import id.sekota.core.designsystem.LocalReducedMotion
import id.sekota.core.designsystem.Reveal
import id.sekota.core.designsystem.SekotaChip
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaEase
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.SectionBand
import id.sekota.core.designsystem.SectionHeader
import id.sekota.core.designsystem.VSpace
import id.sekota.core.designsystem.rememberCardHover
import id.sekota.core.model.Language
import id.sekota.core.model.ProductSlug
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.ProductLogo
import id.sekota.core.ui.WindowSize
import id.sekota.core.ui.vector
import id.sekota.feature.landing.domain.AboutContent
import id.sekota.feature.landing.domain.ReasonItem
import id.sekota.feature.landing.domain.ServiceItem
import id.sekota.feature.landing.domain.ServicesContent

@Composable
fun AboutSection(content: AboutContent, language: Language) {
    val window = LocalWindowSize.current
    val expanded = window == WindowSize.Expanded
    SectionBand {
        if (expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(80.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    SectionHeader(
                        overline = content.overline[language],
                        title = content.title[language],
                    )
                    VSpace(24.dp)
                    Text(
                        text = content.lead[language],
                        style = SekotaTheme.type.lead,
                        color = SekotaColors.Ink2,
                        modifier = Modifier.widthIn(max = 520.dp),
                    )
                }
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    content.reasons.forEachIndexed { index, reason ->
                        Reveal(delayMillis = index * 70) {
                            ReasonCard(reason, language)
                        }
                    }
                }
            }
        } else {
            Column {
                SectionHeader(
                    overline = content.overline[language],
                    title = content.title[language],
                )
                VSpace(24.dp)
                Text(
                    text = content.lead[language],
                    style = SekotaTheme.type.lead,
                    color = SekotaColors.Ink2,
                )
                VSpace(40.dp)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    content.reasons.forEachIndexed { index, reason ->
                        Reveal(delayMillis = index * 70) { ReasonCard(reason, language) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReasonCard(reason: ReasonItem, language: Language) {
    val (interaction, hovered) = rememberCardHover()
    val reduce = LocalReducedMotion.current
    val shift by animateDpAsState(
        targetValue = if (hovered && !reduce) 6.dp else 0.dp,
        animationSpec = tween(400, easing = SekotaEase),
        label = "reasonShift",
    )
    val tile by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = tween(500, easing = SekotaEase),
        label = "reasonTile",
    )
    val border by animateColorAsState(
        targetValue = if (hovered) SekotaColors.Cyan.copy(alpha = 0.45f) else SekotaColors.Line,
        animationSpec = tween(400),
        label = "reasonBorder",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { translationX = shift.toPx() }
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, border, SekotaShape.lg)
            .hoverable(interaction)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(48.dp)
                .graphicsLayer {
                    rotationZ = -8f * tile
                    scaleX = 1f + 0.06f * tile
                    scaleY = 1f + 0.06f * tile
                },
        ) {
            IconTile(reason.icon.vector(), size = 48.dp)
        }
        Spacer(Modifier.width(20.dp))
        Column {
            Text(
                text = reason.title[language],
                style = SekotaTheme.type.h4,
                color = SekotaColors.Ink,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = reason.text[language],
                style = SekotaTheme.type.small,
                color = SekotaColors.Ink2,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServicesSection(
    content: ServicesContent,
    language: Language,
    onProduct: (ProductSlug) -> Unit,
) {
    val window = LocalWindowSize.current
    val columns = when (window) {
        WindowSize.Expanded -> 4
        WindowSize.Wide, WindowSize.Medium -> 2
        WindowSize.Compact -> 1
    }
    SectionBand(subtle = true) {
        SectionHeader(
            overline = content.overline[language],
            title = content.title[language],
        )
        VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            content.items.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    rowItems.forEach { item ->
                        ServiceCard(
                            item = item,
                            language = language,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            onClick = { onProduct(item.platform) },
                        )
                    }
                    repeat(columns - rowItems.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        VSpace(40.dp)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = content.alsoAvailable[language],
                style = SekotaTheme.type.small.copy(fontWeight = FontWeight.Medium),
                color = SekotaColors.Ink2,
            )
            content.extras.forEach { extra ->
                SekotaChip(text = extra[language])
            }
        }
    }
}

@Composable
private fun ServiceCard(
    item: ServiceItem,
    language: Language,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interaction = androidx.compose.runtime.remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val focused by interaction.collectIsFocusedAsState()
    val keyboardMode = LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val reduce = LocalReducedMotion.current
    val lift by animateDpAsState(
        targetValue = if (hovered && !reduce) 6.dp else 0.dp,
        animationSpec = tween(450, easing = SekotaEase),
        label = "serviceLift",
    )
    val tile by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = tween(500, easing = SekotaEase),
        label = "serviceTile",
    )
    val borderAlpha by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = tween(450, easing = SekotaEase),
        label = "serviceBorder",
    )
    val shape = RoundedCornerShape(20.dp)
    val radius = 20.dp

    Box(
        modifier = modifier
            .graphicsLayer { translationY = -lift.toPx() }
            .clip(shape)
            .background(Color.White, shape)
            .border(
                width = if (focused && keyboardMode) 2.dp else 1.dp,
                color = if (focused && keyboardMode) SekotaColors.Ink else SekotaColors.Line,
                shape = shape,
            )
            .drawWithContent {
                drawContent()
                if (borderAlpha > 0.01f) {
                    drawRoundRect(
                        brush = SekotaColors.BrandGradient,
                        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                        style = Stroke(1.dp.toPx()),
                        alpha = borderAlpha,
                    )
                }
            }
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    Modifier
                        .size(48.dp)
                        .graphicsLayer {
                            rotationZ = -8f * tile
                            scaleX = 1f + 0.06f * tile
                            scaleY = 1f + 0.06f * tile
                        },
                ) {
                    IconTile(item.icon.vector(), size = 48.dp, iconSize = 24.dp)
                }
                Text(
                    text = item.number,
                    style = SekotaTheme.type.number,
                    color = SekotaColors.Muted,
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = item.title[language],
                style = SekotaTheme.type.h3,
                color = SekotaColors.Ink,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.tagline[language],
                style = SekotaTheme.type.small,
                color = SekotaColors.Ink2,
            )
            Spacer(Modifier.height(20.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(SekotaColors.Line2))
            Spacer(Modifier.height(18.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item.points.forEach { point ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = SekotaIcons.Check,
                            contentDescription = null,
                            tint = SekotaColors.Success,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = point[language],
                            style = SekotaTheme.type.small.copy(fontWeight = FontWeight.Medium),
                            color = SekotaColors.Ink,
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SekotaShape.sm)
                    .background(SekotaColors.Subtle)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Platform",
                    style = SekotaTheme.type.monoSmall.copy(fontSize = 12.sp),
                    color = SekotaColors.Muted,
                )
                Spacer(Modifier.width(10.dp))
                ProductLogo(slug = item.platform, height = 19.dp)
            }
        }
    }
}
