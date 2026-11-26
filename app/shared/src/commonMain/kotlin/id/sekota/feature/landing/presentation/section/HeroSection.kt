package id.sekota.feature.landing.presentation.section

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.GradientText
import id.sekota.core.designsystem.Reveal
import id.sekota.core.designsystem.SekotaButton
import id.sekota.core.designsystem.SekotaChip
import id.sekota.core.designsystem.HeroBackdrop
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.model.Language
import id.sekota.core.model.LText
import id.sekota.core.model.ProductSlug
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.ProductLogo
import id.sekota.core.ui.WindowSize
import id.sekota.core.ui.vector
import id.sekota.feature.landing.domain.HeroContent
import id.sekota.feature.landing.domain.SectorItem
import id.sekota.feature.landing.domain.SuiteRow
import id.sekota.feature.landing.domain.TagStyle
import kotlin.math.roundToInt

@Composable
fun HeroSection(
    content: HeroContent,
    sectorsTitle: String,
    sectors: List<SectorItem>,
    language: Language,
    highlightedRow: Int,
    onViewServices: () -> Unit,
    onContact: () -> Unit,
    onProduct: (ProductSlug) -> Unit,
    onRowHover: (Int) -> Unit,
) {
    val window = LocalWindowSize.current
    val compact = window == WindowSize.Compact
    val stacked = window != WindowSize.Expanded

    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 0.dp else 820.dp)
            .background(SekotaColors.Bg),
    ) {
        HeroBackdrop(Modifier.matchParentSize())
        ContentWrap {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = if (compact) 120.dp else 172.dp, bottom = if (compact) 56.dp else 96.dp),
            ) {
                if (stacked) {
                    Column {
                        HeroCopy(
                            content = content,
                            language = language,
                            compact = compact,
                            onViewServices = onViewServices,
                            onContact = onContact,
                        )
                        Spacer(Modifier.height(40.dp))
                        if (compact) {
                            SuiteCard(
                                content, language, highlightedRow, onProduct, onRowHover,
                                Modifier.fillMaxWidth(),
                            )
                        } else {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                                Box(
                                    Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                                    contentAlignment = Alignment.TopEnd,
                                ) {
                                    SuiteCard(
                                        content, language, highlightedRow, onProduct, onRowHover,
                                        Modifier.padding(top = 16.dp).width(472.dp),
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Box(Modifier.weight(1.2f)) {
                            HeroCopy(
                                content = content,
                                language = language,
                                compact = false,
                                onViewServices = onViewServices,
                                onContact = onContact,
                            )
                        }
                        Box(Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                            SuiteCard(
                                content, language, highlightedRow, onProduct, onRowHover,
                                Modifier.padding(top = 16.dp).width(472.dp),
                            )
                        }
                    }
                }
            }
        }
    }
    SectorMarquee(sectorsTitle, sectors, language)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroCopy(
    content: HeroContent,
    language: Language,
    compact: Boolean,
    onViewServices: () -> Unit,
    onContact: () -> Unit,
) {
    val type = SekotaTheme.type
    val heroStyle = when (LocalWindowSize.current) {
        WindowSize.Compact -> type.hero.copy(fontSize = 36.sp, lineHeight = 42.sp, letterSpacing = (-1.26).sp)
        WindowSize.Medium -> type.hero.copy(fontSize = 36.sp, lineHeight = 42.sp, letterSpacing = (-1.26).sp)
        WindowSize.Wide -> type.hero.copy(fontSize = 40.sp, lineHeight = 46.sp, letterSpacing = (-1.4).sp)
        WindowSize.Expanded -> type.hero
    }
    Column(verticalArrangement = Arrangement.spacedBy(28.dp)) {
        Reveal {
            SekotaChip(text = content.chip[language])
        }
        Reveal(delayMillis = 80) {
            Column {
                Text(
                    text = content.titleLine1[language],
                    style = heroStyle,
                    color = SekotaColors.Ink,
                    modifier = Modifier.semantics { heading() },
                )
                GradientText(
                    text = content.titleLine2[language],
                    style = heroStyle,
                )
            }
        }
        Reveal(delayMillis = 160) {
            Text(
                text = content.lead[language],
                style = if (compact) type.body.copy(fontSize = 17.sp, lineHeight = 28.sp) else type.lead,
                color = SekotaColors.Ink2,
                modifier = Modifier.widthIn(max = 540.dp),
            )
        }
        Reveal(delayMillis = 240) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SekotaButton(text = content.primaryCta[language], onClick = onViewServices)
                SekotaButton(
                    text = content.secondaryCta[language],
                    onClick = onContact,
                    variant = id.sekota.core.designsystem.ButtonVariant.Secondary,
                    trailingIcon = null,
                )
            }
        }
        Reveal(delayMillis = 320) {
            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (compact) 20.dp else 32.dp),
            ) {
                ProofStat(4, content.proofPlatforms[language])
                Box(
                    Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(SekotaColors.Line),
                )
                ProofStat(6, content.proofBooks[language])
            }
        }
    }
}

@Composable
private fun ProofStat(target: Int, label: String) {
    val reduce = id.sekota.core.designsystem.LocalReducedMotion.current
    val anim = remember { Animatable(if (reduce) target.toFloat() else 0f) }
    LaunchedEffect(reduce) {
        if (reduce) {
            anim.snapTo(target.toFloat())
        } else {
            kotlinx.coroutines.delay(900)
            anim.animateTo(target.toFloat(), tween(1200, easing = androidx.compose.animation.core.FastOutSlowInEasing))
        }
    }
    Column {
        Text(
            text = anim.value.roundToInt().toString(),
            style = SekotaTheme.type.h4.copy(fontSize = 24.sp, lineHeight = 32.sp),
            color = SekotaColors.Ink,
        )
        Text(
            text = label,
            style = SekotaTheme.type.monoSmall,
            color = SekotaColors.Muted,
        )
    }
}

@Composable
private fun SuiteCard(
    content: HeroContent,
    language: Language,
    highlightedRow: Int,
    onProduct: (ProductSlug) -> Unit,
    onRowHover: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                shape = SekotaShape.xxl,
                ambientColor = SekotaColors.Ink.copy(alpha = 0.18f),
                spotColor = SekotaColors.Ink.copy(alpha = 0.18f),
            )
            .clip(SekotaShape.xxl)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.xxl)
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = content.suiteTitle[language],
                    style = SekotaTheme.type.h4,
                    color = SekotaColors.Ink,
                )
                Text(
                    text = content.suiteSubtitle[language],
                    style = SekotaTheme.type.caption,
                    color = SekotaColors.Ink2,
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = content.suiteCount[language],
                style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.Medium),
                color = SekotaColors.Ink2,
                modifier = Modifier
                    .clip(SekotaShape.pill)
                    .background(SekotaColors.Subtle)
                    .border(1.dp, SekotaColors.Line, SekotaShape.pill)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        Spacer(Modifier.height(16.dp))
        content.suiteRows.forEachIndexed { index, row ->
            SuiteRowItem(
                row = row,
                language = language,
                highlighted = index == highlightedRow,
                onClick = {
                    onRowHover(index)
                    onProduct(row.slug)
                },
            )
            if (index != content.suiteRows.lastIndex) Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SuiteRowItem(row: SuiteRow, language: Language, highlighted: Boolean, onClick: () -> Unit) {
    val background by animateColorAsState(
        if (highlighted) Color.White else SekotaColors.Subtle,
        tween(400),
        label = "suiteRowBg",
    )
    val border by animateColorAsState(
        if (highlighted) SekotaColors.Cyan.copy(alpha = 0.35f) else Color.Transparent,
        tween(400),
        label = "suiteRowBorder",
    )
    val compact = LocalWindowSize.current == WindowSize.Compact
    val interaction = remember { MutableInteractionSource() }
    val base = Modifier
        .fillMaxWidth()
        .graphicsLayer { translationX = if (highlighted) (-6).dp.toPx() else 0f }
        .then(
            if (highlighted) {
                Modifier.shadow(
                    elevation = 8.dp,
                    shape = SekotaShape.md,
                    ambientColor = SekotaColors.Cyan.copy(alpha = 0.45f),
                    spotColor = SekotaColors.Cyan.copy(alpha = 0.45f),
                )
            } else {
                Modifier
            },
        )
        .clip(SekotaShape.md)
        .background(background)
        .border(1.dp, border, SekotaShape.md)
        .clickable(interactionSource = interaction, indication = null, onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 12.dp)

    if (compact) {
        Column(base) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductLogo(slug = row.slug, height = 22.dp, contentDescription = null)
                TagPill(row.tag[language], row.tagStyle)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = row.serviceName[language],
                style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
                color = SekotaColors.Ink,
            )
            Text(
                text = row.description[language],
                style = SekotaTheme.type.caption.copy(fontSize = 12.sp, lineHeight = 16.sp),
                color = SekotaColors.Ink2,
            )
        }
    } else {
        Row(base, verticalAlignment = Alignment.Top) {
            Box(Modifier.width(96.dp)) {
                ProductLogo(slug = row.slug, height = 22.dp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = row.serviceName[language],
                        style = SekotaTheme.type.caption.copy(fontWeight = FontWeight.SemiBold),
                        color = SekotaColors.Ink,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    TagPill(row.tag[language], row.tagStyle)
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = row.description[language],
                    style = SekotaTheme.type.caption.copy(fontSize = 12.sp, lineHeight = 16.sp),
                    color = SekotaColors.Ink2,
                )
            }
        }
    }
}

@Composable
private fun TagPill(text: String, style: TagStyle) {
    val (bg, fg) = when (style) {
        TagStyle.Neutral -> SekotaColors.MutedBg to SekotaColors.Ink2
        TagStyle.Green -> SekotaColors.SuccessBg to SekotaColors.SuccessInk
        TagStyle.Cyan -> SekotaColors.CyanBg to SekotaColors.BrandInk
    }
    Row(
        modifier = Modifier
            .clip(SekotaShape.pill)
            .background(bg)
            .padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(fg.copy(alpha = 0.7f)))
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = SekotaTheme.type.caption.copy(fontSize = 11.sp, lineHeight = 14.sp, fontWeight = FontWeight.Medium),
            color = fg,
        )
    }
}

@Composable
private fun SectorMarquee(title: String, sectors: List<SectorItem>, language: Language) {
    val reduce = id.sekota.core.designsystem.LocalReducedMotion.current
    val transition = rememberInfiniteTransition(label = "marquee")
    val animated by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(42000, easing = LinearEasing)),
        label = "marqueeProgress",
    )
    val progress = if (reduce) 0f else animated
    var totalWidth by remember { mutableIntStateOf(0) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clipToBounds()
            .background(SekotaColors.Subtle)
            .border(width = 1.dp, color = SekotaColors.Line2)
            .drawWithContent {
                drawContent()
                val fade = 200.dp.toPx()
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(SekotaColors.Subtle, SekotaColors.Subtle.copy(alpha = 0f)),
                        startX = 0f,
                        endX = fade,
                    ),
                    topLeft = Offset.Zero,
                    size = androidx.compose.ui.geometry.Size(fade, size.height),
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(SekotaColors.Subtle.copy(alpha = 0f), SekotaColors.Subtle),
                        startX = size.width - fade,
                        endX = size.width,
                    ),
                    topLeft = Offset(size.width - fade, 0f),
                    size = androidx.compose.ui.geometry.Size(fade, size.height),
                )
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .offset { IntOffset(-(progress * totalWidth / 2f).roundToInt(), 0) }
                .onSizeChanged { totalWidth = it.width }
                .wrapContentWidth(unbounded = true),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val doubled = sectors + sectors
            doubled.forEach { sector ->
                Row(
                    modifier = Modifier.padding(end = 56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = sector.icon.vector(),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = sector.label[language],
                        style = SekotaTheme.type.body.copy(
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = SekotaColors.Ink2,
                    )
                }
            }
        }
    }
}

