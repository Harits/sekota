package id.sekota.feature.landing.presentation.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.CtaBanner
import id.sekota.core.designsystem.LocalReducedMotion
import id.sekota.core.designsystem.Reveal
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaIcons
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaSpacing
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.SectionBand
import id.sekota.core.designsystem.SectionHeader
import id.sekota.core.designsystem.VSpace
import id.sekota.core.designsystem.rememberCardHover
import id.sekota.core.model.BookSlug
import id.sekota.core.model.Language
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize
import id.sekota.core.ui.cover
import id.sekota.feature.landing.domain.BookCard
import id.sekota.feature.landing.domain.BooksSectionContent
import id.sekota.feature.landing.domain.FaqContent
import id.sekota.feature.landing.domain.FaqItem
import id.sekota.feature.landing.domain.PartnerContent
import org.jetbrains.compose.resources.painterResource

@Composable
fun BooksSection(
    content: BooksSectionContent,
    language: Language,
    onBook: (BookSlug) -> Unit,
) {
    val window = LocalWindowSize.current
    val columns = when (window) {
        WindowSize.Expanded -> 6
        WindowSize.Wide, WindowSize.Medium -> 3
        WindowSize.Compact -> 2
    }
    val gap = if (window == WindowSize.Compact) 20.dp else 24.dp
    SectionBand {
        SectionHeader(
            overline = content.overline[language],
            title = content.title[language],
            subtitle = content.subtitle[language],
        )
        VSpace(if (window == WindowSize.Compact) 32.dp else 56.dp)
        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
            content.items.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.Top,
                ) {
                    rowItems.forEachIndexed { index, book ->
                        Reveal(delayMillis = index * 60, modifier = Modifier.weight(1f)) {
                            BookItem(book, language, content.detailLabel[language]) { onBook(book.slug) }
                        }
                    }
                    repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun BookItem(
    book: BookCard,
    language: Language,
    detailLabel: String,
    onClick: () -> Unit,
) {
    val (interaction, hovered) = rememberCardHover()
    val reduce = LocalReducedMotion.current
    val elevation by animateDpAsState(
        if (hovered) 24.dp else 2.dp,
        tween(600),
        label = "bookElevation",
    )
    var tiltXTarget by remember { mutableStateOf(0f) }
    var tiltYTarget by remember { mutableStateOf(0f) }
    val tiltX by animateFloatAsState(tiltXTarget, tween(150), label = "bookTiltX")
    val tiltY by animateFloatAsState(tiltYTarget, tween(150), label = "bookTiltY")
    val shine by animateFloatAsState(
        targetValue = if (hovered && !reduce) 1f else 0f,
        animationSpec = tween(1000),
        label = "bookShine",
    )
    Column(
        modifier = Modifier
            .clip(SekotaShape.md)
            .hoverable(interaction)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(bottom = 4.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(180f / 248f)
                .shadow(elevation, SekotaShape.sm)
                .clip(SekotaShape.sm)
                .graphicsLayer {
                    rotationX = if (reduce) 0f else tiltX
                    rotationY = if (reduce) 0f else tiltY
                    translationY = if (hovered && !reduce) -6.dp.toPx() else 0f
                    // CSS perspective:900px — a large distance keeps the tilt subtle.
                    cameraDistance = 900f
                }
                .pointerInput(reduce) {
                    if (reduce) return@pointerInput
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Move, PointerEventType.Enter -> {
                                    val change = event.changes.firstOrNull() ?: continue
                                    if (change.type == PointerType.Touch) continue
                                    val w = size.width.toFloat().coerceAtLeast(1f)
                                    val h = size.height.toFloat().coerceAtLeast(1f)
                                    tiltYTarget = (change.position.x / w - 0.5f) * 10f
                                    tiltXTarget = -(change.position.y / h - 0.5f) * 8f
                                }
                                PointerEventType.Exit -> {
                                    tiltXTarget = 0f
                                    tiltYTarget = 0f
                                }
                                else -> Unit
                            }
                        }
                    }
                }
                .drawWithContent {
                    drawContent()
                    // Soft spine shadow on the binding edge.
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.25f), Color.Transparent),
                            startX = 0f,
                            endX = 8.dp.toPx(),
                        ),
                        size = androidx.compose.ui.geometry.Size(8.dp.toPx(), size.height),
                    )
                    if (shine > 0.001f) {
                        val x = (shine * 2.2f - 1.1f) * size.width
                        val band = size.width * 0.3f
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.35f),
                                    Color.Transparent,
                                ),
                                start = Offset(x - band, 0f),
                                end = Offset(x + band, size.height * 0.6f),
                            ),
                        )
                    }
                },
        ) {
            Image(
                painter = painterResource(book.slug.cover()),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = book.title,
            style = SekotaTheme.type.h4,
            color = SekotaColors.Ink,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = book.tagline[language],
            style = SekotaTheme.type.monoSmall,
            color = SekotaColors.BrandInk,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = book.blurb[language],
            style = SekotaTheme.type.small,
            color = SekotaColors.Ink2,
        )
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = detailLabel,
                style = SekotaTheme.type.small.copy(fontWeight = FontWeight.SemiBold),
                color = SekotaColors.BrandInk,
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = SekotaIcons.ArrowRight,
                contentDescription = null,
                tint = SekotaColors.BrandInk,
                modifier = Modifier
                    .size(15.dp)
                    .graphicsLayer { translationX = if (hovered) 3.dp.toPx() else 0f },
            )
        }
    }
}

@Composable
fun PartnerSection(content: PartnerContent, language: Language, onContact: () -> Unit) {
    val compact = LocalWindowSize.current == WindowSize.Compact
    Box(
        Modifier
            .fillMaxWidth()
            .background(SekotaColors.Bg),
    ) {
        ContentWrap {
            Column(Modifier.padding(bottom = if (compact) SekotaSpacing.sectionCompact else SekotaSpacing.section)) {
                CtaBanner(
                    title = content.title[language],
                    text = content.text[language],
                    buttonLabel = content.cta[language],
                    onClick = onContact,
                )
            }
        }
    }
}

@Composable
fun FaqSection(
    content: FaqContent,
    language: Language,
    openFaqId: Int,
    onToggle: (Int) -> Unit,
) {
    val window = LocalWindowSize.current
    val expanded = window == WindowSize.Expanded
    SectionBand(subtle = true) {
        if (expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(80.dp),
            ) {
                Column(Modifier.width(400.dp)) {
                    SectionHeader(overline = content.overline[language], title = content.title[language])
                    VSpace(24.dp)
                    Mailbox(content, language)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    content.items.forEach { item ->
                        FaqRow(item, language, openFaqId == item.id) { onToggle(item.id) }
                    }
                }
            }
        } else {
            Column {
                SectionHeader(overline = content.overline[language], title = content.title[language])
                VSpace(24.dp)
                Mailbox(content, language)
                VSpace(40.dp)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    content.items.forEach { item ->
                        FaqRow(item, language, openFaqId == item.id) { onToggle(item.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun Mailbox(content: FaqContent, language: Language) {
    Row(
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .clip(SekotaShape.md)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.md)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = SekotaIcons.Mail,
            contentDescription = null,
            tint = SekotaColors.Teal,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = content.mailboxLabel[language],
                style = SekotaTheme.type.small,
                color = SekotaColors.Ink2,
            )
            Text(
                text = content.email,
                style = SekotaTheme.type.h4,
                color = SekotaColors.Ink,
            )
        }
    }
}

@Composable
private fun FaqRow(item: FaqItem, language: Language, open: Boolean, onToggle: () -> Unit) {
    val interaction = androidx.compose.runtime.remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, if (open) SekotaColors.Cyan else SekotaColors.Line, SekotaShape.lg)
            .clickable(interactionSource = interaction, indication = null, onClick = onToggle)
            .semantics {
                role = Role.Button
                stateDescription = if (open) "Terbuka" else "Tertutup"
            }
            .animateContentSize(tween(450)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.question[language],
                style = SekotaTheme.type.h4,
                color = SekotaColors.Ink,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (open) SekotaColors.BrandGradient else androidx.compose.ui.graphics.SolidColor(SekotaColors.MutedBg)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = SekotaIcons.Plus,
                    contentDescription = null,
                    tint = if (open) Color.White else SekotaColors.Ink,
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer { rotationZ = if (open) 45f else 0f },
                )
            }
        }
        AnimatedVisibility(
            visible = open,
            enter = expandVertically(tween(350)) + fadeIn(tween(300)),
            exit = shrinkVertically(tween(250)) + fadeOut(tween(150)),
        ) {
            Text(
                text = item.answer[language],
                style = SekotaTheme.type.small.copy(fontSize = 15.sp, lineHeight = 24.sp),
                color = SekotaColors.Ink2,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 22.dp),
            )
        }
    }
}
