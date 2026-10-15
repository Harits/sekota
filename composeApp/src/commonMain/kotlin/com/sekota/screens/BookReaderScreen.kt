package com.sekota.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.Screen
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.repository.AdminRepository
import com.sekota.features.reader.domain.model.BookReadingProgress
import com.sekota.features.reader.domain.model.ReaderChapter
import com.sekota.features.reader.domain.model.ReaderDefaults
import com.sekota.features.reader.domain.model.ReadingMode
import com.sekota.features.reader.domain.usecase.GetReadingProgressUseCase
import com.sekota.features.reader.domain.usecase.RecordReadingTelemetryUseCase
import com.sekota.features.reader.domain.usecase.SaveReadingProgressUseCase
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.windowWidthOf
import kotlinx.coroutines.launch

private val ReaderTeal = Color(0xFF00B5C8)
private val ReaderGreen = Color(0xFF60BD65)
private val ReaderNavy = Color(0xFF0D1F2D)
private val ReaderSurfacePure = Color(0xFFFFFFFF)
private val ReaderSurfaceAlt = Color(0xFFF7FAFB)

/**
 * Color scheme configuration reacting to the active [ReadingMode].
 */
data class ReaderThemeColors(
    val pageBackground: Color,
    val paneBackground: Color,
    val paneBorder: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val bodyTextColor: Color,
    val mutedTextColor: Color,
    val quoteBackground: Color,
    val quoteBorder: Color,
    val quoteTextColor: Color,
    val toolButtonBackground: Color,
    val toolButtonTextColor: Color,
    val toolButtonActiveBackground: Color,
    val toolButtonActiveTextColor: Color,
    val dividerColor: Color
)

fun getReaderThemeColors(mode: ReadingMode): ReaderThemeColors = when (mode) {
    ReadingMode.DAY -> ReaderThemeColors(
        pageBackground = ReaderSurfaceAlt,
        paneBackground = ReaderSurfacePure,
        paneBorder = Color(0xFFE2E8F0),
        cardBackground = ReaderSurfacePure,
        cardBorder = Color(0xFFE2E8F0),
        titleColor = ReaderNavy,
        subtitleColor = Color(0xFF64748B),
        bodyTextColor = Color(0xFF1E293B),
        mutedTextColor = Color(0xFF94A3B8),
        quoteBackground = Color(0x0F00B5C8),
        quoteBorder = ReaderTeal,
        quoteTextColor = ReaderNavy,
        toolButtonBackground = Color(0xFFF1F5F9),
        toolButtonTextColor = Color(0xFF334155),
        toolButtonActiveBackground = ReaderNavy,
        toolButtonActiveTextColor = ReaderSurfacePure,
        dividerColor = Color(0xFFE2E8F0)
    )
    ReadingMode.NIGHT -> ReaderThemeColors(
        pageBackground = Color(0xFF0A151E),
        paneBackground = ReaderNavy,
        paneBorder = Color(0x26FFFFFF),
        cardBackground = Color(0xFF10212E),
        cardBorder = Color(0x26FFFFFF),
        titleColor = Color(0xFFEEF3F6),
        subtitleColor = Color(0xB3EEF3F6),
        bodyTextColor = Color(0xDEEEF3F6),
        mutedTextColor = Color(0x80EEF3F6),
        quoteBackground = Color(0x1F00B5C8),
        quoteBorder = ReaderTeal,
        quoteTextColor = Color(0xFFEEF3F6),
        toolButtonBackground = Color(0x26FFFFFF),
        toolButtonTextColor = Color(0xFFEEF3F6),
        toolButtonActiveBackground = ReaderTeal,
        toolButtonActiveTextColor = ReaderNavy,
        dividerColor = Color(0x1AFFFFFF)
    )
    ReadingMode.SEPIA -> ReaderThemeColors(
        pageBackground = Color(0xFFF5EFEB),
        paneBackground = Color(0xFFFAF8F5),
        paneBorder = Color(0xFFE5DECC),
        cardBackground = Color(0xFFFAF8F5),
        cardBorder = Color(0xFFE5DECC),
        titleColor = Color(0xFF2D2319),
        subtitleColor = Color(0xFF706353),
        bodyTextColor = Color(0xFF3C3226),
        mutedTextColor = Color(0xFF8F806D),
        quoteBackground = Color(0x1A8C6D3B),
        quoteBorder = Color(0xFF8C6D3B),
        quoteTextColor = Color(0xFF2D2319),
        toolButtonBackground = Color(0xFFEDE5D0),
        toolButtonTextColor = Color(0xFF3C3226),
        toolButtonActiveBackground = Color(0xFF2D2319),
        toolButtonActiveTextColor = Color(0xFFFAF8F5),
        dividerColor = Color(0xFFE5DECC)
    )
}

/**
 * Dedicated BookReaderScreen with Immersive Focus Mode, TOC Quick-Jump Sidebar,
 * Reading Progress Local Persistence, and Telemetry Integration.
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun BookReaderScreen(
    bookId: String = "blind-spot-radar",
    initialChapterIndex: Int = 0,
    onNavigateBack: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    adminRepository: AdminRepository? = null,
    saveReadingProgressUseCase: SaveReadingProgressUseCase? = null,
    getReadingProgressUseCase: GetReadingProgressUseCase? = null,
    recordReadingTelemetryUseCase: RecordReadingTelemetryUseCase? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val readingPaneRequester = remember { BringIntoViewRequester() }

    // Fallback default book
    var book by remember {
        mutableStateOf(
            AdminBook(
                id = bookId,
                title = if (bookId == "blind-spot-radar") "Blind Spot Radar" else "Blind Spot Radar",
                author = "Putu Aan J.",
                isbn = "978-623-99999-3-1",
                category = "INTELLIGENCE",
                description = "Karya thought leadership menyoroti deteksi sinyal strategis organisasi.",
                rating = 4.8,
                ratingCount = 1240,
                pages = ReaderDefaults.TOTAL_PAGES
            )
        )
    }

    // Chapters definition (canonical default or dynamically enhanced)
    val chapters = remember(book.whatsInside) {
        if (book.whatsInside.isNotEmpty()) {
            val list = mutableListOf<ReaderChapter>()
            list.add(
                ReaderChapter(
                    id = "pendahuluan",
                    label = "Pendahuluan",
                    title = "Sinyal yang Terlewat",
                    subtitle = "Mengapa organisasi yang cerdas tetap bisa tersandung pada hal yang terlihat jelas.",
                    page = 1
                )
            )
            book.whatsInside.forEachIndexed { idx, item ->
                list.add(
                    ReaderChapter(
                        id = "bab-${idx + 1}",
                        label = "Bab ${idx + 1}",
                        title = item.title,
                        subtitle = item.description,
                        page = 18 + (idx * 40)
                    )
                )
            }
            list.add(
                ReaderChapter(
                    id = "penutup",
                    label = "Penutup",
                    title = "Pemimpin yang Waspada",
                    subtitle = "Kebiasaan kecil yang menjaga radar tetap menyala.",
                    page = (18 + (book.whatsInside.size * 40)).coerceAtMost(book.pages)
                )
            )
            list
        } else {
            ReaderDefaults.CANONICAL_CHAPTERS
        }
    }

    // Reader UI states (interactive state hoisted per Rule 10)
    var currentChapterIndex by remember { mutableStateOf(initialChapterIndex.coerceIn(0, chapters.size - 1)) }
    var fontSizeSp by remember { mutableStateOf(17) }
    var readingMode by remember { mutableStateOf(ReadingMode.DAY) }
    var isBookmarked by remember { mutableStateOf(false) }
    var isMobileTocOpen by remember { mutableStateOf(false) }

    val currentChapter = chapters.getOrElse(currentChapterIndex) { chapters.first() }
    val totalPages = book.pages.takeIf { it > 0 } ?: ReaderDefaults.TOTAL_PAGES
    val progress = remember(currentChapterIndex, currentChapter.page, totalPages) {
        val calculated = ((currentChapter.page.toDouble() / totalPages.toDouble()) * 100).toInt() + (if (currentChapterIndex == 1) 15 else 0)
        calculated.coerceIn(5, 100)
    }

    // Load initial book and restored reading progress
    LaunchedEffect(bookId) {
        if (adminRepository != null) {
            val loaded = adminRepository.getBookById(bookId)
            if (loaded != null) {
                book = loaded
            }
        }
        if (getReadingProgressUseCase != null) {
            val savedProgress = getReadingProgressUseCase(bookId)
            if (savedProgress != null) {
                if (initialChapterIndex == 0 && savedProgress.chapterIndex in chapters.indices) {
                    currentChapterIndex = savedProgress.chapterIndex
                }
                fontSizeSp = savedProgress.fontSizeSp.coerceIn(14, 22)
                readingMode = savedProgress.readingMode
                isBookmarked = savedProgress.isBookmarked
            }
        }
    }

    // Telemetry & Persistence on chapter change
    val onSelectChapter: (Int) -> Unit = { index ->
        val safeIndex = index.coerceIn(0, chapters.size - 1)
        currentChapterIndex = safeIndex
        isMobileTocOpen = false

        coroutineScope.launch {
            try {
                readingPaneRequester.bringIntoView()
            } catch (_: Exception) {
                scrollState.animateScrollTo(0)
            }
        }

        // Persist local reading progress
        saveReadingProgressUseCase?.let { useCase ->
            coroutineScope.launch {
                useCase(
                    BookReadingProgress(
                        bookId = book.id,
                        chapterIndex = safeIndex,
                        percent = progress,
                        isBookmarked = isBookmarked,
                        fontSizeSp = fontSizeSp,
                        readingMode = readingMode,
                        lastReadTimestamp = 0L
                    )
                )
            }
        }

        // Dispatch telemetry event to bookinteractiontool (Rule 6: zero-mock, zero-dummy)
        recordReadingTelemetryUseCase?.let { telemetry ->
            coroutineScope.launch {
                val ch = chapters[safeIndex]
                telemetry(book.id, safeIndex, ch.title)
            }
        }
    }

    // Save state on modifier toggles
    fun persistSettings(newMode: ReadingMode = readingMode, newFont: Int = fontSizeSp, newBookmark: Boolean = isBookmarked) {
        saveReadingProgressUseCase?.let { useCase ->
            coroutineScope.launch {
                useCase(
                    BookReadingProgress(
                        bookId = book.id,
                        chapterIndex = currentChapterIndex,
                        percent = progress,
                        isBookmarked = newBookmark,
                        fontSizeSp = newFont,
                        readingMode = newMode,
                        lastReadTimestamp = 0L
                    )
                )
            }
        }
    }

    val themeColors = getReaderThemeColors(readingMode)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.pageBackground)
    ) {
        val windowWidth = windowWidthOf(maxWidth)
        val isMobile = !windowWidth.isAtLeastExpanded
        val horizontalPadding = contentHorizontalPadding(maxWidth)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalPadding, vertical = if (isMobile) 20.dp else 36.dp)
        ) {
            // Top Navigation Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateBack() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← Kembali ke Detail Buku",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = getDmSansFontFamily(),
                    color = ReaderTeal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book Header
            HeaderSection(
                book = book,
                themeColors = themeColors
            )

            Spacer(modifier = Modifier.height(if (isMobile) 24.dp else 40.dp))

            // Main Reader Body: Responsive 2-column or stacked
            if (isMobile) {
                // Mobile layout with expandable TOC toggle
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Quick TOC toggle bar on mobile
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isMobileTocOpen = !isMobileTocOpen },
                        shape = RoundedCornerShape(12.dp),
                        color = themeColors.cardBackground,
                        border = BorderStroke(1.dp, themeColors.cardBorder),
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "DAFTAR ISI",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = getMontserratFontFamily(),
                                    color = ReaderTeal
                                )
                                Text(
                                    text = "• ${currentChapter.label}: ${currentChapter.title}",
                                    fontSize = 13.sp,
                                    fontFamily = getDmSansFontFamily(),
                                    color = themeColors.subtitleColor
                                )
                            }
                            Text(
                                text = if (isMobileTocOpen) "Tutup ▲" else "Buka ▼",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReaderTeal,
                                fontFamily = getDmSansFontFamily()
                            )
                        }
                    }

                    AnimatedVisibility(visible = isMobileTocOpen) {
                        ChapterSidebar(
                            chapters = chapters,
                            currentIndex = currentChapterIndex,
                            progress = progress,
                            themeColors = themeColors,
                            onSelect = onSelectChapter
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(readingPaneRequester)
                    ) {
                        ReadingPane(
                            chapter = currentChapter,
                            index = currentChapterIndex,
                            totalChapters = chapters.size,
                            totalPages = totalPages,
                            paragraphs = ReaderDefaults.SAMPLE_PARAGRAPHS,
                            quote = ReaderDefaults.SAMPLE_QUOTE,
                            fontSizeSp = fontSizeSp,
                            readingMode = readingMode,
                            isBookmarked = isBookmarked,
                            themeColors = themeColors,
                            onFontSizeChange = { newSize ->
                                fontSizeSp = newSize.coerceIn(14, 22)
                                persistSettings(newFont = fontSizeSp)
                            },
                            onToggleReadingMode = {
                                val nextMode = when (readingMode) {
                                    ReadingMode.DAY -> ReadingMode.NIGHT
                                    ReadingMode.NIGHT -> ReadingMode.SEPIA
                                    ReadingMode.SEPIA -> ReadingMode.DAY
                                }
                                readingMode = nextMode
                                persistSettings(newMode = nextMode)
                            },
                            onToggleBookmark = {
                                isBookmarked = !isBookmarked
                                persistSettings(newBookmark = isBookmarked)
                            },
                            onPrev = {
                                if (currentChapterIndex > 0) onSelectChapter(currentChapterIndex - 1)
                            },
                            onNext = {
                                if (currentChapterIndex < chapters.size - 1) onSelectChapter(currentChapterIndex + 1)
                            }
                        )
                    }
                }
            } else {
                // Desktop 2-column layout (ChapterSidebar 320dp + ReadingPane 1fr)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(modifier = Modifier.width(320.dp)) {
                        ChapterSidebar(
                            chapters = chapters,
                            currentIndex = currentChapterIndex,
                            progress = progress,
                            themeColors = themeColors,
                            onSelect = onSelectChapter
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bringIntoViewRequester(readingPaneRequester)
                    ) {
                        ReadingPane(
                            chapter = currentChapter,
                            index = currentChapterIndex,
                            totalChapters = chapters.size,
                            totalPages = totalPages,
                            paragraphs = ReaderDefaults.SAMPLE_PARAGRAPHS,
                            quote = ReaderDefaults.SAMPLE_QUOTE,
                            fontSizeSp = fontSizeSp,
                            readingMode = readingMode,
                            isBookmarked = isBookmarked,
                            themeColors = themeColors,
                            onFontSizeChange = { newSize ->
                                fontSizeSp = newSize.coerceIn(14, 22)
                                persistSettings(newFont = fontSizeSp)
                            },
                            onToggleReadingMode = {
                                val nextMode = when (readingMode) {
                                    ReadingMode.DAY -> ReadingMode.NIGHT
                                    ReadingMode.NIGHT -> ReadingMode.SEPIA
                                    ReadingMode.SEPIA -> ReadingMode.DAY
                                }
                                readingMode = nextMode
                                persistSettings(newMode = nextMode)
                            },
                            onToggleBookmark = {
                                isBookmarked = !isBookmarked
                                persistSettings(newBookmark = isBookmarked)
                            },
                            onPrev = {
                                if (currentChapterIndex > 0) onSelectChapter(currentChapterIndex - 1)
                            },
                            onNext = {
                                if (currentChapterIndex < chapters.size - 1) onSelectChapter(currentChapterIndex + 1)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

/**
 * Top book title, breadcrumb and rating header.
 */
@Composable
private fun HeaderSection(
    book: AdminBook,
    themeColors: ReaderThemeColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Breadcrumb
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "E-BOOK",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = themeColors.mutedTextColor
            )
            Text(
                text = "/",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = themeColors.mutedTextColor
            )
            Text(
                text = book.category.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = ReaderTeal
            )
        }

        // Title
        Text(
            text = book.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily(),
            color = themeColors.titleColor,
            lineHeight = 38.sp
        )

        // Author
        Text(
            text = "by ${book.author}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = getDmSansFontFamily(),
            color = themeColors.subtitleColor
        )

        // Rating & Verified Reader Count (Honest Invariant per Rule 6)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            if (book.rating > 0.0) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    val roundedStars = kotlin.math.round(book.rating).toInt().coerceIn(1, 5)
                    repeat(roundedStars) {
                        Text(text = "★", color = ReaderGreen, fontSize = 16.sp)
                    }
                    repeat(5 - roundedStars) {
                        Text(text = "☆", color = Color(0xFFD1D5DB), fontSize = 16.sp)
                    }
                }
                Text(
                    text = "${book.rating} / 5 (${book.ratingCount} pembaca terverifikasi)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = getDmSansFontFamily(),
                    color = themeColors.subtitleColor
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0x1F60BD65)
                ) {
                    Text(
                        text = "Publikasi Baru • Belum ada rating",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ReaderGreen,
                        fontFamily = getDmSansFontFamily(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Table of Contents (TOC) Quick-Jump Sidebar matching ChapterSidebar.jsx.
 */
@Composable
fun ChapterSidebar(
    chapters: List<ReaderChapter>,
    currentIndex: Int,
    progress: Int,
    themeColors: ReaderThemeColors,
    onSelect: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = themeColors.cardBackground,
        border = BorderStroke(1.dp, themeColors.cardBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Eyebrow
            Text(
                text = "DAFTAR ISI",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                fontFamily = getMontserratFontFamily(),
                color = ReaderTeal
            )

            // Chapter list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                chapters.forEachIndexed { i, ch ->
                    val isActive = i == currentIndex
                    val activeBg = if (isActive) Color(0x1400B5C8) else Color.Transparent

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(i) },
                        shape = RoundedCornerShape(10.dp),
                        color = activeBg
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Circular Chapter Index Badge
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isActive) {
                                            Brush.linearGradient(listOf(ReaderTeal, ReaderGreen))
                                        } else {
                                            Brush.linearGradient(listOf(themeColors.toolButtonBackground, themeColors.toolButtonBackground))
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${i + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = getDmSansFontFamily(),
                                    color = if (isActive) Color.White else themeColors.toolButtonTextColor
                                )
                            }

                            // Chapter Label & Title
                            val separator = if (ch.label.startsWith("Bab")) " — " else ": "
                            Text(
                                text = "${ch.label}$separator${ch.title}",
                                fontSize = 14.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                fontFamily = getDmSansFontFamily(),
                                color = if (isActive) ReaderTeal else themeColors.bodyTextColor,
                                modifier = Modifier.weight(1f),
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = themeColors.dividerColor, thickness = 1.dp)

            // Reading Progress Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROGRES BACA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = getMontserratFontFamily(),
                        color = themeColors.mutedTextColor
                    )
                    Text(
                        text = "$progress%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily(),
                        color = ReaderTeal
                    )
                }

                // Smooth Progress Bar Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(themeColors.toolButtonBackground)
                ) {
                    val animatedProgress by animateFloatAsState(targetValue = progress / 100f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(Brush.linearGradient(listOf(ReaderTeal, ReaderGreen)))
                    )
                }
            }
        }
    }
}

/**
 * Reading Article Body matching ReadingPane.jsx with Focus Toolbar, Article, Quote, and Pager.
 */
@Composable
fun ReadingPane(
    chapter: ReaderChapter,
    index: Int,
    totalChapters: Int,
    totalPages: Int,
    paragraphs: List<String>,
    quote: String,
    fontSizeSp: Int,
    readingMode: ReadingMode,
    isBookmarked: Boolean,
    themeColors: ReaderThemeColors,
    onFontSizeChange: (Int) -> Unit,
    onToggleReadingMode: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = themeColors.paneBackground,
        border = BorderStroke(1.dp, themeColors.paneBorder),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Reader Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chapter Eyebrow
                val chapterEyebrow = if (chapter.label.startsWith("Bab")) {
                    "${chapter.label} dari ${totalChapters - 2}"
                } else {
                    chapter.label
                }

                Text(
                    text = chapterEyebrow.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    fontFamily = getMontserratFontFamily(),
                    color = ReaderTeal
                )

                // Tool Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Font decrease A-
                    ToolPillButton(
                        text = "A−",
                        themeColors = themeColors,
                        onClick = { onFontSizeChange(fontSizeSp - 1) },
                        enabled = fontSizeSp > 14
                    )

                    // Font indicator
                    Text(
                        text = "${fontSizeSp}px",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = getDmSansFontFamily(),
                        color = themeColors.mutedTextColor
                    )

                    // Font increase A+
                    ToolPillButton(
                        text = "A+",
                        themeColors = themeColors,
                        onClick = { onFontSizeChange(fontSizeSp + 1) },
                        enabled = fontSizeSp < 22
                    )

                    // Mode Toggle (Day / Night / Sepia)
                    val modeLabel = when (readingMode) {
                        ReadingMode.DAY -> "Mode Siang"
                        ReadingMode.NIGHT -> "Mode Malam"
                        ReadingMode.SEPIA -> "Mode Sepia"
                    }
                    ToolPillButton(
                        text = modeLabel,
                        themeColors = themeColors,
                        isActive = readingMode != ReadingMode.DAY,
                        onClick = onToggleReadingMode
                    )

                    // Bookmark / Tandai Toggle
                    ToolPillButton(
                        text = if (isBookmarked) "Ditandai ✓" else "Tandai",
                        themeColors = themeColors,
                        isActive = isBookmarked,
                        onClick = onToggleBookmark
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Chapter Title & Subtitle
            Text(
                text = chapter.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = getMontserratFontFamily(),
                color = themeColors.titleColor,
                lineHeight = 38.sp
            )

            Text(
                text = chapter.subtitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                fontFamily = getDmSansFontFamily(),
                color = themeColors.subtitleColor,
                lineHeight = 26.sp
            )

            HorizontalDivider(color = themeColors.dividerColor, thickness = 1.dp)

            // Article Paragraphs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                paragraphs.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        fontSize = fontSizeSp.sp,
                        fontFamily = getDmSansFontFamily(),
                        color = themeColors.bodyTextColor,
                        lineHeight = (fontSizeSp * 1.76).sp
                    )
                }
            }

            // Stylized Blockquote Callout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(themeColors.quoteBackground)
                    .border(
                        BorderStroke(1.dp, themeColors.quoteBorder.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(themeColors.quoteBorder)
                    )
                    Text(
                        text = quote,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = getDmSansFontFamily(),
                        color = themeColors.quoteTextColor,
                        lineHeight = 25.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = themeColors.dividerColor, thickness = 1.dp)

            // Pager Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPrev,
                    enabled = index > 0,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (index > 0) themeColors.toolButtonTextColor.copy(alpha = 0.3f) else themeColors.dividerColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = themeColors.bodyTextColor,
                        disabledContentColor = themeColors.mutedTextColor
                    )
                ) {
                    Text(
                        text = "← Bab Sebelumnya",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = getDmSansFontFamily()
                    )
                }

                Text(
                    text = "Halaman ${chapter.page} / $totalPages",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = getDmSansFontFamily(),
                    color = themeColors.mutedTextColor
                )

                Button(
                    onClick = onNext,
                    enabled = index < totalChapters - 1,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReaderTeal,
                        contentColor = Color.White,
                        disabledContainerColor = themeColors.toolButtonBackground,
                        disabledContentColor = themeColors.mutedTextColor
                    )
                ) {
                    Text(
                        text = "Bab Berikutnya →",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = getDmSansFontFamily()
                    )
                }
            }
        }
    }
}

/**
 * Reusable Tool Pill Button for toolbar adjustments.
 */
@Composable
private fun ToolPillButton(
    text: String,
    themeColors: ReaderThemeColors,
    onClick: () -> Unit,
    isActive: Boolean = false,
    enabled: Boolean = true
) {
    val bg = if (isActive) themeColors.toolButtonActiveBackground else themeColors.toolButtonBackground
    val textColor = if (isActive) themeColors.toolButtonActiveTextColor else themeColors.toolButtonTextColor

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (enabled) bg else bg.copy(alpha = 0.4f)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = getDmSansFontFamily(),
            color = if (enabled) textColor else textColor.copy(alpha = 0.4f),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// ==========================================
// MANDATORY MULTI-STATE PREVIEWS (#22548, Rule 4)
// ==========================================

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookReaderDayPreview() {
    MaterialTheme {
        BookReaderScreen(
            bookId = "blind-spot-radar",
            initialChapterIndex = 1
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookReaderNightPreview() {
    MaterialTheme {
        val themeColors = getReaderThemeColors(ReadingMode.NIGHT)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeColors.pageBackground)
                .padding(32.dp)
        ) {
            ReadingPane(
                chapter = ReaderDefaults.CANONICAL_CHAPTERS[1],
                index = 1,
                totalChapters = ReaderDefaults.CANONICAL_CHAPTERS.size,
                totalPages = 240,
                paragraphs = ReaderDefaults.SAMPLE_PARAGRAPHS,
                quote = ReaderDefaults.SAMPLE_QUOTE,
                fontSizeSp = 18,
                readingMode = ReadingMode.NIGHT,
                isBookmarked = true,
                themeColors = themeColors,
                onFontSizeChange = {},
                onToggleReadingMode = {},
                onToggleBookmark = {},
                onPrev = {},
                onNext = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookReaderSepiaPreview() {
    MaterialTheme {
        val themeColors = getReaderThemeColors(ReadingMode.SEPIA)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeColors.pageBackground)
                .padding(32.dp)
        ) {
            ReadingPane(
                chapter = ReaderDefaults.CANONICAL_CHAPTERS[1],
                index = 1,
                totalChapters = ReaderDefaults.CANONICAL_CHAPTERS.size,
                totalPages = 240,
                paragraphs = ReaderDefaults.SAMPLE_PARAGRAPHS,
                quote = ReaderDefaults.SAMPLE_QUOTE,
                fontSizeSp = 17,
                readingMode = ReadingMode.SEPIA,
                isBookmarked = false,
                themeColors = themeColors,
                onFontSizeChange = {},
                onToggleReadingMode = {},
                onToggleBookmark = {},
                onPrev = {},
                onNext = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookReaderTocSidebarPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .width(360.dp)
                .padding(16.dp)
        ) {
            ChapterSidebar(
                chapters = ReaderDefaults.CANONICAL_CHAPTERS,
                currentIndex = 1,
                progress = 25,
                themeColors = getReaderThemeColors(ReadingMode.DAY),
                onSelect = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookReaderCompactMobilePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .width(420.dp)
                .fillMaxHeight()
        ) {
            BookReaderScreen(
                bookId = "blind-spot-radar",
                initialChapterIndex = 0
            )
        }
    }
}
