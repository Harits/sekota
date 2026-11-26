package id.sekota.feature.books.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteFooter
import id.sekota.core.designsystem.ContentWrap
import id.sekota.core.designsystem.CtaBanner
import id.sekota.core.designsystem.Overline
import id.sekota.core.designsystem.SekotaButton
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaShape
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.designsystem.SectionBand
import id.sekota.core.designsystem.VSpace
import id.sekota.core.model.BookSlug
import id.sekota.core.model.Language
import id.sekota.core.ui.LocalWindowSize
import id.sekota.core.ui.WindowSize
import id.sekota.core.ui.cover
import id.sekota.feature.books.domain.BookDetail
import id.sekota.feature.books.domain.BooksPageContent
import id.sekota.feature.books.domain.GetBooks
import id.sekota.feature.books.domain.SeriesContent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun BooksScreen(
    appState: AppState,
    initialAnchor: BookSlug?,
    onContact: (String?) -> Unit,
) {
    val getBooks = koinInject<GetBooks>()
    val content = remember { getBooks() }
    val language = appState.language
    val listState = rememberLazyListState()
    DisposableEffect(listState) {
        appState.activeListState = listState
        onDispose { if (appState.activeListState === listState) appState.activeListState = null }
    }
    val scope = rememberCoroutineScope()
    val window = LocalWindowSize.current
    val headerOffset = with(LocalDensity.current) { 84.dp.roundToPx() }

    val keys = remember(content) {
        buildList {
            add("hero")
            content.books.forEachIndexed { index, book ->
                add(book.slug.anchor)
                if (index == 2) add("series")
            }
            add("cta")
            add("footer")
        }
    }

    LaunchedEffect(initialAnchor) {
        val index = initialAnchor?.let { keys.indexOf(it.anchor) } ?: 0
        if (index >= 0) {
            listState.scrollToItem(index, if (index == 0) 0 else -headerOffset)
        }
    }
    LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow { listState.firstVisibleItemIndex }
            .collect { appState.chromeSolid = it > 0 }
    }

    fun scrollTo(anchor: BookSlug) {
        val index = keys.indexOf(anchor.anchor)
        if (index >= 0) scope.launch {
            listState.animateScrollToItem(index, if (index == 0) 0 else -headerOffset)
        }
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
        item(key = "hero") {
            BooksHero(content, language, window, onBook = ::scrollTo)
        }
        content.books.forEachIndexed { index, book ->
            item(key = book.slug.anchor) {
                BookBlock(
                    book = book,
                    index = index,
                    language = language,
                    bookCta = content.bookCta[language],
                    bookCtaHint = content.bookCtaHint[language],
                ) { onContact("buku-${book.slug.anchor}") }
            }
            if (index == 2) {
                item(key = "series") {
                    SeriesBand(content.series, language, window, onBook = ::scrollTo)
                }
            }
        }
        item(key = "cta") {
            SectionBand {
                CtaBanner(
                    title = content.ctaTitle[language],
                    text = content.ctaText[language],
                    buttonLabel = content.ctaButton[language],
                    onClick = { onContact(null) },
                )
            }
        }
        item(key = "footer") { SiteFooter(appState) }
    }
}

@Composable
private fun BooksHero(
    content: BooksPageContent,
    language: Language,
    window: WindowSize,
    onBook: (BookSlug) -> Unit,
) {
    val compact = window == WindowSize.Compact
    val type = SekotaTheme.type
    val titleStyle = when (window) {
        WindowSize.Compact -> type.headline.copy(fontSize = 30.sp, lineHeight = 36.sp)
        WindowSize.Medium -> type.headline.copy(fontSize = 36.sp, lineHeight = 42.sp)
        WindowSize.Wide -> type.headline.copy(fontSize = 42.sp, lineHeight = 48.sp)
        WindowSize.Expanded -> type.headline.copy(fontSize = 52.sp, lineHeight = 58.sp)
    }
    Box(Modifier.fillMaxWidth().background(SekotaColors.Bg)) {
        ContentWrap {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = if (compact) 120.dp else 152.dp, bottom = 56.dp),
            ) {
                Overline(content.overline[language])
                VSpace(16.dp)
                Text(
                    text = buildAnnotatedString {
                        append(content.titlePrefix[language])
                        withStyle(SpanStyle(brush = SekotaColors.BrandGradient)) {
                            append(content.titleGradient[language])
                        }
                    },
                    style = titleStyle,
                    color = SekotaColors.Ink,
                    modifier = Modifier.semantics { heading() },
                )
                VSpace(16.dp)
                Text(
                    text = content.heroLead[language],
                    style = type.lead,
                    color = SekotaColors.Ink2,
                    modifier = Modifier.widthIn(max = 620.dp),
                )
                VSpace(56.dp)
                BooksIndexGrid(content, language, window, onBook = onBook)
            }
        }
    }
}

@Composable
private fun BooksIndexGrid(
    content: BooksPageContent,
    language: Language,
    window: WindowSize,
    onBook: (BookSlug) -> Unit,
) {
    val columns = when (window) {
        WindowSize.Expanded -> 6
        WindowSize.Compact -> 2
        else -> 3
    }
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            content.books.chunked(columns).forEach { rowBooks ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.fillMaxWidth()) {
                    rowBooks.forEach { book ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onBook(book.slug) },
                        ) {
                            Image(
                                painter = painterResource(book.slug.cover()),
                                contentDescription = book.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(180f / 248f)
                                    .clip(SekotaShape.sm),
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = book.title,
                                style = SekotaTheme.type.small.copy(
                                    fontSize = 15.sp,
                                    lineHeight = 21.sp,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = SekotaColors.Ink,
                            )
                        }
                    }
                    repeat(columns - rowBooks.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
}

@Composable
private fun BookBlock(
    book: BookDetail,
    index: Int,
    language: Language,
    bookCta: String,
    bookCtaHint: String,
    onContact: () -> Unit,
) {
    val window = LocalWindowSize.current
    val expanded = window == WindowSize.Expanded
    SectionBand(subtle = index % 2 == 1) {
        if (!expanded) {
            val compact = window == WindowSize.Compact
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    BookCover(book, Modifier.width(if (compact) 120.dp else 150.dp))
                    Spacer(Modifier.width(20.dp))
                    Text(
                        text = book.author[language],
                        style = SekotaTheme.type.monoSmall,
                        color = SekotaColors.Muted,
                    )
                }
                VSpace(24.dp)
                BookText(book, language, bookCta, bookCtaHint, onContact)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(72.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(Modifier.width(340.dp)) {
                    BookCover(book, Modifier.fillMaxWidth())
                    VSpace(20.dp)
                    Text(
                        text = book.author[language],
                        style = SekotaTheme.type.monoSmall,
                        color = SekotaColors.Muted,
                    )
                }
                Box(Modifier.weight(1f)) {
                    BookText(book, language, bookCta, bookCtaHint, onContact)
                }
            }
        }
    }
}

@Composable
private fun BookCover(book: BookDetail, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(book.slug.cover()),
        contentDescription = book.title,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(180f / 248f)
            .clip(SekotaShape.md),
    )
}

@Composable
private fun BookText(
    book: BookDetail,
    language: Language,
    bookCta: String,
    bookCtaHint: String,
    onContact: () -> Unit,
) {
    Column(Modifier.widthIn(max = 720.dp)) {
        Text(
            text = book.title,
            style = id.sekota.core.designsystem.responsiveHeadlineStyle(),
            color = SekotaColors.Ink,
            modifier = Modifier.semantics { heading() },
        )
        VSpace(8.dp)
        Text(
            text = book.subtitle[language],
            style = SekotaTheme.type.lead.copy(fontSize = 19.sp, lineHeight = 28.sp),
            color = SekotaColors.Ink2,
        )
        VSpace(24.dp)
        Quote(book.quote[language])
        VSpace(24.dp)
        book.paragraphs.forEachIndexed { paragraphIndex, paragraph ->
            Text(
                text = paragraph[language],
                style = SekotaTheme.type.body.copy(
                    fontSize = 17.sp,
                    lineHeight = 29.sp,
                    fontWeight = if (paragraphIndex == 0) FontWeight.SemiBold else null,
                ),
                color = if (paragraphIndex == 0) SekotaColors.Ink else SekotaColors.Ink2,
            )
            VSpace(16.dp)
        }
        VSpace(8.dp)
        AudienceAndContents(book, language)
        VSpace(28.dp)
        SekotaButton(text = bookCta, onClick = onContact)
        VSpace(10.dp)
        Text(
            text = bookCtaHint,
            style = SekotaTheme.type.caption,
            color = SekotaColors.Muted,
        )
    }
}

@Composable
private fun Quote(text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(SekotaColors.Cyan.copy(alpha = 0.07f), Color.Transparent),
                ),
            )
            .height(IntrinsicSize.Min),
    ) {
        Box(
            Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(SekotaColors.Cyan),
        )
        Text(
            text = text,
            style = SekotaTheme.type.lead.copy(
                fontSize = 20.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = SekotaColors.Ink,
            modifier = Modifier.padding(start = 21.dp, end = 24.dp, top = 20.dp, bottom = 20.dp),
        )
    }
}

@Composable
private fun AudienceAndContents(book: BookDetail, language: Language) {
    val window = LocalWindowSize.current
    val stacked = window != WindowSize.Expanded
    if (stacked) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AudienceBox(book, language)
            ContentsBox(book, language)
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Box(Modifier.weight(1f)) { AudienceBox(book, language) }
            Box(Modifier.weight(1.4f)) { ContentsBox(book, language) }
        }
    }
}

@Composable
private fun AudienceBox(book: BookDetail, language: Language) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.lg)
            .padding(24.dp),
    ) {
        Text(
            text = book.audienceTitle[language].uppercase(),
            style = SekotaTheme.type.monoOverline,
            color = SekotaColors.BrandInk,
        )
        VSpace(14.dp)
        book.audience.forEach { item ->
            Row(Modifier.padding(bottom = 10.dp)) {
                Box(
                    Modifier
                        .padding(top = 8.dp)
                        .height(6.dp)
                        .width(6.dp)
                        .clip(CircleShape)
                        .background(SekotaColors.Cyan),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = item[language],
                    style = SekotaTheme.type.small,
                    color = SekotaColors.Ink2,
                )
            }
        }
    }
}

@Composable
private fun ContentsBox(book: BookDetail, language: Language) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.lg)
            .background(Color.White)
            .border(1.dp, SekotaColors.Line, SekotaShape.lg)
            .padding(24.dp),
    ) {
        Text(
            text = book.contentsTitle[language].uppercase(),
            style = SekotaTheme.type.monoOverline,
            color = SekotaColors.BrandInk,
        )
        VSpace(14.dp)
        book.contents.forEachIndexed { itemIndex, item ->
            Column(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = item.title[language],
                    style = SekotaTheme.type.small.copy(fontWeight = FontWeight.SemiBold),
                    color = SekotaColors.Ink,
                )
                Text(
                    text = item.detail[language],
                    style = SekotaTheme.type.caption,
                    color = SekotaColors.Ink2,
                )
            }
            if (itemIndex != book.contents.lastIndex) {
                Box(Modifier.fillMaxWidth().height(1.dp).background(SekotaColors.Line2))
            }
        }
    }
}

@Composable
private fun SeriesBand(
    series: SeriesContent,
    language: Language,
    window: WindowSize,
    onBook: (BookSlug) -> Unit,
) {
    val stacked = window == WindowSize.Compact || window == WindowSize.Medium
    SectionBand(dark = true) {
        Overline(series.overline[language], dark = true)
        VSpace(16.dp)
        Text(
            text = series.title[language],
            style = id.sekota.core.designsystem.responsiveHeadlineStyle(),
            color = Color.White,
        )
        VSpace(16.dp)
        Text(
            text = series.text[language],
            style = SekotaTheme.type.lead,
            color = SekotaColors.OnDarkMuted,
            modifier = Modifier.widthIn(max = 760.dp),
        )
        VSpace(24.dp)
        Text(
            text = series.quote[language],
            style = SekotaTheme.type.h4.copy(fontStyle = FontStyle.Italic),
            color = Color.White,
        )
        VSpace(40.dp)
        if (stacked) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                series.links.forEach { link -> SeriesCard(link.label[language], link.meta[language]) { onBook(link.slug) } }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                series.links.forEach { link ->
                    Box(Modifier.weight(1f)) {
                        SeriesCard(link.label[language], link.meta[language]) { onBook(link.slug) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesCard(label: String, meta: String, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(SekotaShape.xl)
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), SekotaShape.xl)
            .clickable(onClick = onClick)
            .padding(24.dp),
    ) {
        Text(label, style = SekotaTheme.type.h4, color = Color.White)
        VSpace(8.dp)
        Text(meta, style = SekotaTheme.type.caption, color = SekotaColors.OnDarkMuted)
    }
}
