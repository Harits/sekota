package id.sekota.feature.landing.presentation.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.core.designsystem.GradientText
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
import id.sekota.core.ui.dashboard
import id.sekota.core.ui.vector
import id.sekota.feature.landing.domain.PlatformCard
import id.sekota.feature.landing.domain.PlatformsContent
import id.sekota.feature.landing.domain.ProcessContent
import id.sekota.feature.landing.domain.ProcessStep
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProcessSection(content: ProcessContent, language: Language, progress: Float = 0f) {
    val window = LocalWindowSize.current
    val columns = when (window) {
        WindowSize.Expanded -> 4
        WindowSize.Wide, WindowSize.Medium -> 2
        WindowSize.Compact -> 1
    }
    SectionBand {
        SectionHeader(
            overline = content.overline[language],
            title = content.title[language],
            subtitle = content.subtitle[language],
        )
        if (window == WindowSize.Expanded) {
            VSpace(56.dp)
            StepProgress(progress)
            VSpace(16.dp)
        } else {
            VSpace(32.dp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            content.steps.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    rowItems.forEachIndexed { itemIndex, step ->
                        Reveal(delayMillis = itemIndex * 60, modifier = Modifier.weight(1f)) {
                            StepCard(step = step, language = language)
                        }
                    }
                    repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        VSpace(56.dp)
        Box(
            Modifier
                .fillMaxWidth()
                .clip(SekotaShape.lg)
                .background(SekotaColors.BrandGradientSoft)
                .border(1.dp, SekotaColors.Line2, SekotaShape.lg)
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = content.collaborationTitle[language],
                    style = SekotaTheme.type.h4,
                    color = SekotaColors.Ink,
                )
                content.collaborationModels.forEach { model ->
                    SekotaChip(text = model[language])
                }
            }
        }
    }
}

@Composable
private fun StepCard(step: ProcessStep, language: Language) {
    val (interaction, hovered) = rememberCardHover()
    val reduce = LocalReducedMotion.current
    val lift by animateDpAsState(
        targetValue = if (hovered && !reduce) 4.dp else 0.dp,
        animationSpec = tween(400, easing = SekotaEase),
        label = "stepLift",
    )
    val tile by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = tween(500, easing = SekotaEase),
        label = "stepTile",
    )
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = -lift.toPx() }
            .clip(shape)
            .background(Color.White, shape)
            .border(
                1.dp,
                if (hovered) SekotaColors.Cyan.copy(alpha = 0.45f) else SekotaColors.Line,
                shape,
            )
            .hoverable(interaction)
            .padding(24.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(44.dp)
                        .graphicsLayer {
                            rotationZ = -8f * tile
                            scaleX = 1f + 0.06f * tile
                            scaleY = 1f + 0.06f * tile
                        },
                ) {
                    IconTile(step.icon.vector(), size = 44.dp, iconSize = 22.dp)
                }
                GradientText(
                    text = step.number,
                    style = SekotaTheme.type.bigNumber,
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = step.title[language],
                style = SekotaTheme.type.h3,
                color = SekotaColors.Ink,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = step.description[language],
                style = SekotaTheme.type.small,
                color = SekotaColors.Ink2,
            )
        }
    }
}

@Composable
private fun StepProgress(progress: Float) {
    BoxWithConstraints(Modifier.fillMaxWidth().height(40.dp)) {
        val p = progress.coerceIn(0f, 1f)
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(top = 19.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(SekotaColors.Line),
        )
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(top = 19.dp)
                .width(maxWidth * p)
                .height(2.dp)
                .background(SekotaColors.BrandGradient),
        )
        repeat(4) { i ->
            val x = 24.dp + maxWidth * (i * 0.25f)
            val active = p > 0f && p >= i / 3f - 0.001f
            Box(
                Modifier
                    .offset(x = x, y = 13.dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, if (active) SekotaColors.Cyan else SekotaColors.Line, CircleShape),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlatformsSection(
    content: PlatformsContent,
    language: Language,
    onProduct: (ProductSlug) -> Unit,
) {
    val window = LocalWindowSize.current
    val expanded = window == WindowSize.Expanded || window == WindowSize.Wide
    val columns = if (expanded) 2 else 1
    SectionBand(dark = true) {
        if (expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                SectionHeader(
                    overline = content.overline[language],
                    title = content.title[language],
                    subtitle = content.subtitle[language],
                    dark = true,
                )
                TechBadge(content.techBadge[language])
            }
        } else {
            Column {
                SectionHeader(
                    overline = content.overline[language],
                    title = content.title[language],
                    subtitle = content.subtitle[language],
                    dark = true,
                )
                VSpace(24.dp)
                TechBadge(content.techBadge[language])
            }
        }
        VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            content.items.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    rowItems.forEach { card ->
                        PlatformTile(
                            card = card,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { onProduct(card.slug) },
                        )
                    }
                    repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun TechBadge(text: String) {
    Row(
        modifier = Modifier
            .clip(SekotaShape.pill)
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), SekotaShape.pill)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = SekotaIcons.Cpu,
            contentDescription = null,
            tint = SekotaColors.OnDarkMuted,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = SekotaTheme.type.monoSmall,
            color = SekotaColors.OnDarkMuted,
        )
    }
}

@Composable
private fun PlatformTile(
    card: PlatformCard,
    language: Language,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val (interaction, hovered) = rememberCardHover()
    val shotScale by animateFloatAsState(
        targetValue = if (hovered) 1.03f else 1f,
        animationSpec = tween(1100, easing = SekotaEase),
        label = "shotScale",
    )
    Column(
        modifier = modifier
            .clip(SekotaShape.xxl)
            .background(if (hovered) Color.White.copy(alpha = 0.07f) else Color.White.copy(alpha = 0.05f))
            .border(
                1.dp,
                if (hovered) SekotaColors.Cyan.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.1f),
                SekotaShape.xxl,
            )
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(28.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductLogo(slug = card.slug, dark = true, height = 32.dp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = card.learnMore[language],
                    style = SekotaTheme.type.small.copy(fontWeight = FontWeight.Medium),
                    color = Color.White,
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = SekotaIcons.ArrowUpRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer {
                            translationX = if (hovered) 2.dp.toPx() else 0f
                            translationY = if (hovered) (-2).dp.toPx() else 0f
                        },
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = card.description[language],
            style = SekotaTheme.type.lead,
            color = SekotaColors.OnDarkMuted,
        )
        Spacer(Modifier.height(24.dp))
        Image(
            painter = painterResource(card.slug.dashboard()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1064f / 592f)
                .graphicsLayer { scaleX = shotScale; scaleY = shotScale }
                .shadow(30.dp, SekotaShape.lg)
                .clip(SekotaShape.lg),
        )
    }
}
