package id.sekota.feature.legal.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteFooter
import id.sekota.app.navigation.Route
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.LocalReducedMotion
import id.sekota.core.designsystem.Overline
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaSpacing
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.model.Language
import id.sekota.core.model.LText
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize
import id.sekota.feature.legal.domain.GetLegalDoc
import id.sekota.feature.legal.domain.LegalBlock
import id.sekota.feature.legal.domain.LegalDoc
import id.sekota.feature.legal.domain.LegalDocType
import id.sekota.feature.legal.domain.LegalLink
import id.sekota.feature.legal.domain.LegalSection
import id.sekota.feature.legal.domain.LegalSpan
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LegalScreen(
    appState: AppState,
    type: LegalDocType,
) {
    val getLegal = koinInject<GetLegalDoc>()
    val doc = remember(type) { getLegal(type) }
    val language = appState.language
    val window = LocalWindowSize.current
    val desktop = window == WindowSize.Expanded || window == WindowSize.Wide
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val reducedMotion = LocalReducedMotion.current

    DisposableEffect(listState) {
        appState.activeListState = listState
        onDispose { if (appState.activeListState === listState) appState.activeListState = null }
    }
    LaunchedEffect(type) { listState.scrollToItem(0) }
    LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow { listState.firstVisibleItemIndex }
            .collect { appState.chromeSolid = it > 0 }
    }

    val density = LocalDensity.current
    // SiteHeader is an 80dp overlay, so items behind it still count as visible.
    // 108dp = header + 28dp breathing room, matching the TOC's stick position.
    val headerPx = with(density) { SekotaSpacing.navHeight.roundToPx() }
    val readingLinePx = headerPx + with(density) { 28.dp.roundToPx() }
    val sectionPadPx = with(density) { 24.dp.roundToPx() }
    val firstSectionIndex = if (desktop) 1 else 2
    val lastSectionItemIndex = firstSectionIndex + doc.sections.lastIndex
    val footerIndex = lastSectionItemIndex + 2
    var tocHeightPx by remember { mutableIntStateOf(0) }

    val visibleItems by remember {
        androidx.compose.runtime.derivedStateOf { listState.layoutInfo.visibleItemsInfo }
    }
    // Keyed so a breakpoint or density change cannot keep captured indices.
    val activeSection by remember(firstSectionIndex, lastSectionItemIndex, readingLinePx) {
        androidx.compose.runtime.derivedStateOf {
            val visibleSections = visibleItems.mapNotNull { item ->
                val section = item.index - firstSectionIndex
                if (section in doc.sections.indices) SectionSpan(section, item.offset) else null
            }
            resolveActiveSectionIndex(
                visibleSections = visibleSections,
                activationLinePx = readingLinePx,
                lastDocumentSection = doc.sections.lastIndex,
                atDocumentEnd = isDocumentEnd(
                    canScrollForward = listState.canScrollForward,
                    firstVisibleItemIndex = listState.firstVisibleItemIndex,
                    firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset,
                ),
                hasScrolledPastSections = listState.firstVisibleItemIndex > lastSectionItemIndex,
            )
        }
    }
    val showToc by remember(
        desktop,
        firstSectionIndex,
        lastSectionItemIndex,
        footerIndex,
        headerPx,
        sectionPadPx,
    ) {
        androidx.compose.runtime.derivedStateOf {
            if (!desktop ||
                visibleItems.none { it.index in firstSectionIndex..lastSectionItemIndex }
            ) {
                return@derivedStateOf false
            }
            val footerTop = visibleItems.firstOrNull { it.index == footerIndex }?.offset
            // Keep the card visible while it can sit below the header; it may ride
            // slightly above the 108dp reading line near the footer.
            footerTop == null ||
                footerTop - tocHeightPx - sectionPadPx >= headerPx
        }
    }
    val tocTopPx by remember(firstSectionIndex, footerIndex, readingLinePx, sectionPadPx) {
        androidx.compose.runtime.derivedStateOf {
            val firstSection = visibleItems.firstOrNull { it.index == firstSectionIndex }
            val natural = if (firstSection != null) {
                firstSection.offset + sectionPadPx
            } else {
                readingLinePx
            }
            val footerTop = visibleItems.firstOrNull { it.index == footerIndex }?.offset
            val maxTop = if (footerTop != null && tocHeightPx > 0) {
                footerTop - tocHeightPx - sectionPadPx
            } else {
                Int.MAX_VALUE
            }
            // Never slide under the header.
            natural.coerceAtLeast(readingLinePx)
                .coerceAtMost(maxTop.coerceAtLeast(headerPx))
        }
    }

    val openLink: (LegalLink) -> Unit = { link ->
        when (link) {
            LegalLink.Email -> uriHandler.openUri("mailto:info@sekota.id")
            LegalLink.Contact -> appState.navigator.navigate(Route.Contact())
            LegalLink.Privacy -> appState.navigator.navigate(Route.Privacy)
            LegalLink.Terms -> appState.navigator.navigate(Route.Terms)
        }
    }
    val scrollToSection: (Int) -> Unit = { index ->
        scope.launch {
            val item = firstSectionIndex + index
            val offset = -readingLinePx
            if (reducedMotion) {
                listState.scrollToItem(item, scrollOffset = offset)
            } else {
                listState.animateScrollToItem(item, scrollOffset = offset)
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            item(key = "hero") {
                LegalHero(doc, language)
            }
            if (!desktop) {
                item(key = "toc") {
                    ContentWrap {
                        TocCard(
                            doc = doc,
                            language = language,
                            active = activeSection,
                            onSelect = scrollToSection,
                        )
                    }
                }
            }
            doc.sections.forEachIndexed { index, section ->
                item(key = section.id) {
                    ContentWrap {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = if (desktop) 312.dp else 0.dp)
                                .padding(top = if (index == 0) 24.dp else 0.dp, bottom = 44.dp),
                        ) {
                            LegalSectionView(
                                section = section,
                                number = index + 1,
                                language = language,
                                onLink = openLink,
                            )
                        }
                    }
                }
            }
            item(key = "next") {
                ContentWrap {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = if (desktop) 312.dp else 0.dp)
                            .padding(bottom = 112.dp),
                    ) {
                        NextDocLink(doc, language) {
                            appState.navigator.navigate(
                                if (doc.otherType == LegalDocType.Privacy) Route.Privacy else Route.Terms,
                            )
                        }
                    }
                }
            }
            item(key = "footer") { SiteFooter(appState) }
        }

        if (desktop && showToc) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .offset { androidx.compose.ui.unit.IntOffset(0, tocTopPx) },
            ) {
                ContentWrap {
                    Toc(
                        doc = doc,
                        language = language,
                        active = activeSection,
                        onSelect = scrollToSection,
                        modifier = Modifier
                            .width(240.dp)
                            .onSizeChanged { tocHeightPx = it.height },
                    )
                }
            }
        }
    }
}

@Composable
private fun LegalHero(doc: LegalDoc, language: Language) {
    val compact = LocalWindowSize.current == WindowSize.Compact
    val titleStyle = when (LocalWindowSize.current) {
        WindowSize.Compact -> SekotaTheme.type.headline.copy(fontSize = 34.sp, lineHeight = 40.sp)
        WindowSize.Medium -> SekotaTheme.type.headline.copy(fontSize = 38.sp, lineHeight = 44.sp)
        WindowSize.Wide -> SekotaTheme.type.headline.copy(fontSize = 42.sp, lineHeight = 48.sp)
        WindowSize.Expanded -> SekotaTheme.type.headline.copy(fontSize = 50.sp, lineHeight = 56.sp, letterSpacing = (-1.5).sp)
    }
    Box(
        Modifier
            .fillMaxWidth()
            .background(SekotaColors.Bg)
            .padding(top = if (compact) 112.dp else 150.dp, bottom = if (compact) 40.dp else 56.dp),
    ) {
        ContentWrap {
            Column {
                Overline(doc.overline[language])
                Spacer(Modifier.height(16.dp))
                Text(
                    text = doc.title[language],
                    style = titleStyle,
                    color = SekotaColors.Ink,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    text = doc.lead[language],
                    style = SekotaTheme.type.lead,
                    color = SekotaColors.Ink2,
                    modifier = Modifier.widthIn(max = 680.dp),
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = doc.updated[language],
                    style = SekotaTheme.type.monoSmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.72.sp,
                    ),
                    color = SekotaColors.Muted,
                )
            }
        }
    }
}

@Composable
private fun TocCard(
    doc: LegalDoc,
    language: Language,
    active: Int,
    onSelect: (Int) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.lg)
            .padding(20.dp),
    ) {
        Toc(doc, language, active, onSelect)
    }
}

@Composable
private fun Toc(
    doc: LegalDoc,
    language: Language,
    active: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = doc.tocTitle[language].uppercase(),
            style = SekotaTheme.type.monoOverline.copy(letterSpacing = 1.44.sp),
            color = SekotaColors.BrandInk,
        )
        Spacer(Modifier.height(10.dp))
        doc.sections.forEachIndexed { index, section ->
            TocRow(
                label = section.title[language],
                active = index == active,
                onClick = { onSelect(index) },
            )
        }
    }
}

@Composable
private fun TocRow(label: String, active: Boolean, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val color = if (active || hovered) SekotaColors.Ink else SekotaColors.Ink2
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 34.dp)
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, onClick = onClick)
            .semantics {
                selected = active
                role = Role.Tab
            }
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(2.dp)
                .height(20.dp)
                .background(if (active || hovered) SekotaColors.Cyan else SekotaColors.Line),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = SekotaTheme.type.small.copy(fontSize = 14.sp, lineHeight = 20.sp),
            color = color,
            fontWeight = if (active) FontWeight.SemiBold else null,
        )
    }
}

@Composable
private fun LegalSectionView(
    section: LegalSection,
    number: Int,
    language: Language,
    onLink: (LegalLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.widthIn(max = 720.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = number.toString().padStart(2, '0'),
                style = SekotaTheme.type.monoSmall.copy(
                    fontSize = 13.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Medium,
                ),
                color = SekotaColors.BrandInk,
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = section.title[language],
                style = SekotaTheme.type.h3.copy(fontSize = 22.sp, lineHeight = 30.sp),
                color = SekotaColors.Ink,
                modifier = Modifier.semantics { heading() },
            )
        }
        Spacer(Modifier.height(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            section.blocks.forEach { block ->
                when (block) {
                    is LegalBlock.Paragraph -> ParagraphRuns(block.spans, language, onLink)
                    is LegalBlock.Bullets -> BulletList(block.items, language, onLink)
                }
            }
        }
    }
}

@Composable
private fun ParagraphRuns(
    spans: List<LegalSpan>,
    language: Language,
    onLink: (LegalLink) -> Unit,
) {
    Text(
        text = runs(spans, language, onLink),
        style = SekotaTheme.type.body.copy(fontSize = 16.sp, lineHeight = 27.sp),
    )
}

@Composable
private fun BulletList(
    items: List<List<LegalSpan>>,
    language: Language,
    onLink: (LegalLink) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            Row(Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .padding(top = 11.dp)
                        .size(6.dp)
                        .clip(SekotaShape.pill)
                        .background(SekotaColors.BrandGradient),
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = runs(item, language, onLink),
                    style = SekotaTheme.type.body.copy(fontSize = 16.sp, lineHeight = 27.sp),
                )
            }
        }
    }
}

@Composable
private fun runs(
    spans: List<LegalSpan>,
    language: Language,
    onLink: (LegalLink) -> Unit,
) = buildAnnotatedString {
    spans.forEach { span ->
        val base = SpanStyle(
            color = if (span.bold) SekotaColors.Ink else SekotaColors.Ink2,
            fontWeight = if (span.bold) FontWeight.SemiBold else null,
        )
        val target = span.link
        if (target != null) {
            withLink(
                LinkAnnotation.Clickable(
                    tag = target.name,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = SekotaColors.BrandInk,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                    linkInteractionListener = LinkInteractionListener { onLink(target) },
                ),
            ) {
                withStyle(base) { append(span.text[language]) }
            }
        } else {
            withStyle(base) { append(span.text[language]) }
        }
    }
}

@Composable
private fun NextDocLink(doc: LegalDoc, language: Language, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    Column(
        Modifier
            .widthIn(max = 720.dp)
            .fillMaxWidth(),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SekotaColors.Line),
        )
        Spacer(Modifier.height(28.dp))
        Row(
            modifier = Modifier
                .heightIn(min = 44.dp)
                .hoverable(source)
                .clickable(interactionSource = source, indication = null, onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = doc.otherLabel[language],
                style = SekotaTheme.type.body.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = SekotaColors.BrandInk,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "→",
                style = SekotaTheme.type.body.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = SekotaColors.BrandInk,
            )
        }
    }
}
