package com.sekota.ui.brand

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sekota.localization.AppLocale

// ---------------------------------------------------------------------------------
// Theme plumbing
// ---------------------------------------------------------------------------------

class BrandTypeSet(val regular: BrandTypography, val compact: BrandTypography) {
    fun of(compact: Boolean): BrandTypography = if (compact) this.compact else regular
}

val LocalBrandType = staticCompositionLocalOf<BrandTypeSet> { error("BrandTheme is not set") }

/** Provides the company profile type scale and the reduce-motion preference. */
@Composable
fun BrandTheme(reduceMotion: Boolean = platformPrefersReducedMotion(), content: @Composable () -> Unit) {
    val types = BrandTypeSet(rememberBrandTypography(compact = false), rememberBrandTypography(compact = true))
    CompositionLocalProvider(
        LocalBrandType provides types,
        LocalReduceMotion provides reduceMotion,
        content = content
    )
}

@Composable
fun brandType(compact: Boolean = false): BrandTypography = LocalBrandType.current.of(compact)

// ---------------------------------------------------------------------------------
// Layout
// ---------------------------------------------------------------------------------

/** Centres content at the 1200dp design measure with the 24dp gutter (.wrap). */
@Composable
fun BrandContainer(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = BrandLayout.MaxContentWidth + BrandLayout.Gutter * 2)
                .fillMaxWidth()
                .padding(horizontal = BrandLayout.Gutter),
            content = content
        )
    }
}

/** Lays [items] out in rows of [columns] equal-width cells. */
@Composable
fun <T> BrandGrid(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    gap: Dp = BrandLayout.CardGap,
    equalHeight: Boolean = true,
    cell: @Composable (index: Int, item: T, modifier: Modifier) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(gap)) {
        items.chunked(columns.coerceAtLeast(1)).forEachIndexed { rowIndex, row ->
            Row(
                modifier = if (equalHeight) Modifier.fillMaxWidth().height(IntrinsicSize.Max) else Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                row.forEachIndexed { i, item ->
                    val cellModifier = Modifier.weight(1f).let { if (equalHeight) it.fillMaxHeight() else it }
                    cell(rowIndex * columns + i, item, cellModifier)
                }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// Text
// ---------------------------------------------------------------------------------

@Composable
fun GradientText(text: String, style: TextStyle, modifier: Modifier = Modifier) {
    Text(text = text, modifier = modifier, style = style.copy(brush = BrandBrushes.PrimaryHorizontal))
}

/** .ov: a gradient tick followed by uppercase mono text. */
@Composable
fun Overline(text: String, modifier: Modifier = Modifier, onDark: Boolean = false) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(width = 18.dp, height = 2.dp).clip(BrandShapes.Pill).background(BrandBrushes.PrimaryHorizontal))
        Text(
            text = text.uppercase(),
            style = brandType().overline,
            color = if (onDark) BrandColors.OnDarkMuted else BrandColors.BrandInk
        )
    }
}

/** .sh: overline, title and optional lead, max 680dp wide. */
@Composable
fun SectionHeader(
    overline: String?,
    title: String,
    modifier: Modifier = Modifier,
    lead: String? = null,
    onDark: Boolean = false,
    compact: Boolean = false,
    centered: Boolean = false
) {
    val type = brandType(compact)
    Column(
        modifier = modifier.widthIn(max = 680.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        if (!overline.isNullOrBlank()) Overline(overline, onDark = onDark)
        Text(
            text = title,
            style = type.h2,
            color = if (onDark) Color.White else BrandColors.Ink,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start
        )
        if (!lead.isNullOrBlank()) {
            Text(
                text = lead,
                style = type.lead,
                color = if (onDark) BrandColors.OnDarkMuted else BrandColors.Ink2,
                textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.widthIn(max = 620.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------------
// Interaction helpers
// ---------------------------------------------------------------------------------

/** Lifts an element on pointer hover (.svc / .step hover). No-op under reduce motion. */
fun Modifier.hoverLift(lift: Dp = 6.dp, interactionSource: MutableInteractionSource? = null): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val reduce = LocalReduceMotion.current
    val offset by animateDpAsState(if (hovered && !reduce) -lift else 0.dp, tween(450, easing = BrandEasing), label = "hoverLift")
    val px = with(LocalDensity.current) { offset.toPx() }
    this.hoverable(source).graphicsLayer { translationY = px }
}

// ---------------------------------------------------------------------------------
// Controls
// ---------------------------------------------------------------------------------

enum class BrandButtonVariant { Primary, Secondary, OnGradient }

/** .btn-p / .btn-s: 52dp (44dp when [small]) high, 12dp radius. */
@Composable
fun BrandButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BrandButtonVariant = BrandButtonVariant.Primary,
    small: Boolean = false,
    trailingIcon: ImageVector? = null,
    contentDescription: String? = null
) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val reduce = LocalReduceMotion.current
    val lift by animateDpAsState(if (hovered && !reduce) (-1).dp else 0.dp, tween(300, easing = BrandEasing), label = "btnLift")
    val iconShift by animateDpAsState(if (hovered && !reduce) 3.dp else 0.dp, tween(300, easing = BrandEasing), label = "btnIcon")
    val liftPx = with(LocalDensity.current) { lift.toPx() }
    val height = if (small) 44.dp else 52.dp
    val horizontal = if (small) 20.dp else 24.dp

    val shaped = Modifier
        .graphicsLayer { translationY = liftPx }
        .then(
            when (variant) {
                BrandButtonVariant.Primary -> Modifier
                    .shadow(if (hovered) 14.dp else 10.dp, BrandShapes.Button, ambientColor = BrandColors.Cyan, spotColor = BrandColors.Cyan)
                    .clip(BrandShapes.Button)
                    .background(BrandBrushes.Primary)
                BrandButtonVariant.Secondary -> Modifier
                    .clip(BrandShapes.Button)
                    .background(Color.White)
                    .border(1.dp, if (hovered) Color(0xFFB9C6CE) else BrandColors.Line, BrandShapes.Button)
                BrandButtonVariant.OnGradient -> Modifier
                    .clip(BrandShapes.Button)
                    .background(Color.White)
            }
        )
    val contentColor = if (variant == BrandButtonVariant.Primary) Color.White else BrandColors.Ink

    Row(
        modifier = modifier
            .then(shaped)
            .height(height)
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, role = Role.Button, onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .semantics { if (contentDescription != null) this.contentDescription = contentDescription }
            .padding(horizontal = horizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Text(text = text, style = brandType().button, color = contentColor, maxLines = 1)
        if (trailingIcon != null) {
            Icon(trailingIcon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp).offset(x = iconShift))
        }
    }
}

/** .chip: pill with a gradient dot. */
@Composable
fun BrandChip(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(BrandShapes.Pill)
            .background(Color.White)
            .border(1.dp, BrandColors.Line, BrandShapes.Pill)
            .padding(start = 12.dp, end = 14.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(BrandBrushes.Primary))
        Text(text, style = brandType().smallStrong, color = BrandColors.Ink2, maxLines = 1)
    }
}

enum class TagTone { Neutral, Green, Cyan }

/** .tag: small rounded label with a status dot. */
@Composable
fun BrandTag(text: String, modifier: Modifier = Modifier, tone: TagTone = TagTone.Neutral) {
    val (bg, fg) = when (tone) {
        TagTone.Neutral -> BrandColors.MutedBg to BrandColors.Ink2
        TagTone.Green -> BrandColors.TagGreenBg to BrandColors.TagGreen
        TagTone.Cyan -> BrandColors.TagCyanBg to BrandColors.BrandInk
    }
    Row(
        modifier = modifier.clip(BrandShapes.Pill).background(bg).padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(fg.copy(alpha = 0.7f)))
        Text(text, style = brandType().caption.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium), color = fg, maxLines = 1)
    }
}

/** .tile: 48dp gradient-tinted square holding an outline icon. */
@Composable
fun IconTile(icon: ImageVector, modifier: Modifier = Modifier, size: Dp = 48.dp, tilted: Boolean = false) {
    val reduce = LocalReduceMotion.current
    val angle by animateFloatAsState(if (tilted && !reduce) -8f else 0f, tween(450, easing = BrandEasing), label = "tileTilt")
    val scale by animateFloatAsState(if (tilted && !reduce) 1.06f else 1f, tween(450, easing = BrandEasing), label = "tileScale")
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer { rotationZ = angle; scaleX = scale; scaleY = scale }
            .clip(BrandShapes.Tile)
            .background(BrandBrushes.Tile)
            .border(1.dp, BrandColors.Line2, BrandShapes.Tile),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = BrandColors.TileIcon, modifier = Modifier.size(size * 0.46f))
    }
}

/** .pts li: green tick with a short label. */
@Composable
fun CheckItem(text: String, modifier: Modifier = Modifier, onDark: Boolean = false) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(BrandIcons.Check, contentDescription = null, tint = BrandColors.CheckGreen, modifier = Modifier.size(16.dp))
        Text(text, style = brandType().smallStrong, color = if (onDark) Color.White else BrandColors.Ink)
    }
}

/** .qa: one FAQ entry; the answer expands below the question. */
@Composable
fun FaqItem(
    question: String,
    answer: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reduce = LocalReduceMotion.current
    val border by animateColorAsState(if (expanded) BrandColors.Cyan else BrandColors.Line, tween(350), label = "faqBorder")
    val rotation by animateFloatAsState(if (expanded) 45f else 0f, tween(if (reduce) 0 else 450, easing = BrandEasing), label = "faqIcon")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(BrandShapes.Faq)
            .background(Color.White)
            .border(BorderStroke(1.dp, border), BrandShapes.Faq)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClick = onToggle)
                .pointerHoverIcon(PointerIcon.Hand)
                .semantics { stateDescription = if (expanded) "expanded" else "collapsed" }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(question, style = brandType().h4, color = BrandColors.Ink, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .then(if (expanded) Modifier.background(BrandBrushes.Primary) else Modifier.background(BrandColors.MutedBg)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    BrandIcons.Plus,
                    contentDescription = null,
                    tint = if (expanded) Color.White else BrandColors.Ink,
                    modifier = Modifier.size(16.dp).rotate(rotation)
                )
            }
        }
        AnimatedVisibility(
            visible = expanded,
            enter = if (reduce) fadeIn(tween(0)) else expandVertically(tween(500, easing = BrandEasing)) + fadeIn(tween(400, delayMillis = 120)),
            exit = if (reduce) fadeOut(tween(0)) else shrinkVertically(tween(400, easing = BrandEasing)) + fadeOut(tween(200))
        ) {
            Text(
                text = answer,
                style = brandType().body,
                color = BrandColors.Ink2,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 22.dp)
            )
        }
    }
}

/** .lang: ID | EN segmented toggle. Switching keeps the current page. */
@Composable
fun LanguageToggle(selected: AppLocale, onSelect: (AppLocale) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(BrandShapes.Toggle)
            .background(Color.White.copy(alpha = 0.7f))
            .border(1.dp, BrandColors.Line, BrandShapes.Toggle)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        listOf(AppLocale.ID, AppLocale.EN).forEach { locale ->
            val active = locale == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCorner7)
                    .background(if (active) BrandColors.MutedBg else Color.Transparent)
                    .clickable(role = Role.Tab) { if (!active) onSelect(locale) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .semantics { stateDescription = if (active) "selected" else "not selected" }
                    .padding(horizontal = 9.dp, vertical = 8.dp)
            ) {
                Text(
                    text = locale.name,
                    style = brandType().caption.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
                    color = if (active) BrandColors.Ink else BrandColors.Ink2
                )
            }
        }
    }
}

private val RoundedCorner7 = androidx.compose.foundation.shape.RoundedCornerShape(7.dp)
