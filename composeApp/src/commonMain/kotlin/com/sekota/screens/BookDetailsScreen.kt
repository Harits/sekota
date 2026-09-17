package com.sekota.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.GetBookByIdUseCase
import com.sekota.utils.decodeBase64ToBitmap
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun BookDetailsScreen(
    bookId: String? = "blind-spot-radar",
    isLoggedIn: Boolean = false,
    onRequestAuth: (onSuccess: () -> Unit) -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getBookByIdUseCase = remember { GetBookByIdUseCase(repository) }
    var book by remember {
        mutableStateOf(
            AdminBook(
                id = "blind-spot-radar",
                title = "Blind Spot Radar",
                author = "Putu Aan J.",
                isbn = "978-623-99999-3-1",
                category = "INTELLIGENCE",
                description = "Buku \"Blind Spot Radar: Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar\" merupakan karya thought leadership yang menyoroti fenomena di mana para pengambil keputusan tingkat atas sering kali gagal mendeteksi ancaman nyata atau peluang strategis, meskipun mereka memiliki kecerdasan dan data yang memadai.",
                rating = 4.8,
                ratingCount = 1240,
                pdfUrl = "https://sekota.id/assets/docs/blind-spot-radar.pdf",
                readingTime = "3H 45M",
                pages = 240
            )
        )
    }

    LaunchedEffect(bookId) {
        if (!bookId.isNullOrBlank()) {
            val loaded = getBookByIdUseCase(bookId)
            if (loaded != null) {
                book = loaded
            }
        }
        syncService.syncEventFlow.collect {
            if (!bookId.isNullOrBlank()) {
                getBookByIdUseCase(bookId)?.let { book = it }
            }
        }
    }

    var readSuccessMessage by remember { mutableStateOf<String?>(null) }
    var librarySuccessMessage by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        val windowWidth = windowWidthOf(maxWidth)
        // A 340dp cover plus a readable info column needs the Expanded breakpoint;
        // below that the cover sits above the copy.
        val isMobile = !windowWidth.isAtLeastExpanded
        val horizontalPadding = windowWidth.sectionHorizontalPadding
        val verticalPadding = if (windowWidth.isCompact) 24.dp else 48.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Hero Section
            if (isMobile) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BookCover(
                        title = book.title,
                        coverImage = book.coverImage,
                        modifier = Modifier
                            .widthIn(max = 260.dp)
                            .fillMaxWidth()
                            .height(370.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    BookInfoContent(
                        book = book,
                        isMobile = true,
                        isLoggedIn = isLoggedIn,
                        onReadNowClick = {
                            val targetUrl = book.pdfUrl ?: "https://sekota.id/reader/${book.id}"
                            if (isLoggedIn) {
                                readSuccessMessage = "Membuka manuscript e-book: $targetUrl"
                            } else {
                                onRequestAuth {
                                    readSuccessMessage = "Autentikasi terverifikasi. Mengakses naskah: $targetUrl"
                                }
                            }
                        },
                        onAddToLibraryClick = {
                            if (isLoggedIn) {
                                librarySuccessMessage = "Buku \"${book.title}\" berhasil ditambahkan ke pustaka pribadi Anda!"
                            } else {
                                onRequestAuth {
                                    librarySuccessMessage = "Autentikasi terverifikasi. Buku \"${book.title}\" ditambahkan ke pustaka!"
                                }
                            }
                        },
                        readSuccessMessage = readSuccessMessage,
                        librarySuccessMessage = librarySuccessMessage
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                    horizontalArrangement = Arrangement.spacedBy(64.dp)
                ) {
                    BookCover(
                        title = book.title,
                        coverImage = book.coverImage,
                        modifier = Modifier
                            .width(340.dp)
                            .height(480.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        BookInfoContent(
                            book = book,
                            isMobile = false,
                            isLoggedIn = isLoggedIn,
                            onReadNowClick = {
                                val targetUrl = book.pdfUrl ?: "https://sekota.id/reader/${book.id}"
                                if (isLoggedIn) {
                                    readSuccessMessage = "Membuka manuscript e-book: $targetUrl"
                                } else {
                                    onRequestAuth {
                                        readSuccessMessage = "Autentikasi terverifikasi. Mengakses naskah: $targetUrl"
                                    }
                                }
                            },
                            onAddToLibraryClick = {
                                if (isLoggedIn) {
                                    librarySuccessMessage = "Buku \"${book.title}\" berhasil ditambahkan ke pustaka pribadi Anda!"
                                } else {
                                    onRequestAuth {
                                        librarySuccessMessage = "Autentikasi terverifikasi. Buku \"${book.title}\" ditambahkan ke pustaka!"
                                    }
                                }
                            },
                            readSuccessMessage = readSuccessMessage,
                            librarySuccessMessage = librarySuccessMessage
                        )
                    }
                }
            }

            // Details Section
            if (isMobile) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    WhatsInsideSection(
                        book = book,
                        readingTime = book.readingTime,
                        modifier = Modifier.fillMaxWidth(),
                        stackChapters = windowWidth.isCompact
                    )
                    MetadataSection(
                        book = book,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    WhatsInsideSection(
                        book = book,
                        readingTime = book.readingTime,
                        modifier = Modifier.weight(2f)
                    )
                    MetadataSection(
                        book = book,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            Footer()
        }
    }
}

@Composable
private fun BookInfoContent(
    book: AdminBook,
    isMobile: Boolean,
    isLoggedIn: Boolean = false,
    onReadNowClick: () -> Unit = {},
    onAddToLibraryClick: () -> Unit = {},
    readSuccessMessage: String? = null,
    librarySuccessMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            color = Color(0xFF00B5C8).copy(alpha = 0.12f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = book.category.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0097A7),
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = book.title,
            fontSize = if (isMobile) 36.sp else 52.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily(),
            color = Color(0xFF0D1F2D),
            letterSpacing = (-1).sp,
            lineHeight = if (isMobile) 42.sp else 58.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "by ${book.author}",
            fontSize = if (isMobile) 18.sp else 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = getDmSansFontFamily(),
            color = Color(0x99143244)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (book.rating > 0.0) {
                RatingStars(rating = kotlin.math.round(book.rating).toInt().coerceIn(1, 5), color = Color(0xFF60BD65))
                Spacer(modifier = Modifier.width(12.dp))
                val readerLabel = if (book.ratingCount == 1) "1 pembaca terdaftar" else "${book.ratingCount} pembaca terdaftar"
                Text(
                    text = "(${book.rating}/5 dari $readerLabel • ${book.interactions} interaksi)",
                    fontSize = 14.sp,
                    color = Color(0xFF71717A),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            } else if (book.interactions > 0) {
                Text(
                    text = "📊 ${book.interactions} Interaksi Pembaca Tercatat (E-Score dalam kalkulasi)",
                    fontSize = 14.sp,
                    color = Color(0xFF00B5C8),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "📖 Publikasi Baru • Belum Ada Interaksi Pembaca",
                    fontSize = 14.sp,
                    color = Color(0xFF71717A),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if (book.description.isNotBlank()) book.description else "Buku publikasi resmi dari Sekota Research & Intelligence, menghadirkan analisis komprehensif, framework taktis, dan rekomendasi implementasi berdaya saing.",
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = Color(0xFF0D1F2D),
            fontFamily = getDmSansFontFamily()
        )
        if (!book.pdfUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📄 Digital Edition: Termasuk manuskrip PDF resolusi tinggi dan lisensi pembaca interaktif.",
                fontSize = 14.sp,
                color = Color(0xFF00796B),
                fontFamily = getDmSansFontFamily(),
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (readSuccessMessage != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = readSuccessMessage,
                    color = Color(0xFF2E7D32),
                    fontSize = 13.sp,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        if (librarySuccessMessage != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE0F7FA),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = librarySuccessMessage,
                    color = Color(0xFF00838F),
                    fontSize = 13.sp,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        
        // Responsive action buttons (stack on small screens or wrap cleanly)
        if (isMobile) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onReadNowClick,
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Read Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = getDmSansFontFamily())
                }
                
                OutlinedButton(
                    onClick = onAddToLibraryClick,
                    shape = RoundedCornerShape(9999.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00B5C8)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00B5C8)),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Add to Library", fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = getDmSansFontFamily())
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onReadNowClick,
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1F2D)),
                    modifier = Modifier.height(52.dp).weight(1f).widthIn(max = 200.dp)
                ) {
                    Text("Read Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = getDmSansFontFamily(), maxLines = 1)
                }

                OutlinedButton(
                    onClick = onAddToLibraryClick,
                    shape = RoundedCornerShape(9999.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00B5C8)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00B5C8)),
                    modifier = Modifier.height(52.dp).weight(1.2f).widthIn(max = 230.dp)
                ) {
                    Text("Add to Library", fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = getDmSansFontFamily(), maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun BookCover(
    title: String = "BLIND SPOT\nRADAR",
    coverImage: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 8.dp,
        color = Color(0xFF121212)
    ) {
        val bitmap = remember(coverImage) {
            if (!coverImage.isNullOrBlank()) decodeBase64ToBitmap(coverImage) else null
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF0D1F2D))
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = title.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = getMontserratFontFamily(),
                    lineHeight = 34.sp
                )
            }
        }
    }
}

@Composable
fun RatingStars(rating: Int, color: Color) {
    Row {
        repeat(rating) {
            Image(
                painter = painterResource(Res.drawable.filled_star),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(color)
            )
        }
    }
}

@Composable
fun WhatsInsideSection(
    book: AdminBook? = null,
    readingTime: String = "3H 45M",
    modifier: Modifier = Modifier,
    // Two chapter tiles side by side need ~500dp of pane; below that they stack.
    stackChapters: Boolean = false
) {
    val chapters = remember(book?.id, book?.category) {
        when (book?.category?.uppercase()) {
            "ESG" -> listOf(
                Pair("Prinsip ESG & Tata Kelola", "Menelaah standar akuntabilitas kepatuhan lingkungan dan sosial korporasi."),
                Pair("Integrasi Strategis Keberlanjutan", "Langkah operasionalisasi metrik ESG ke dalam model bisnis institusi."),
                Pair("Audit Dampak & Pelaporan", "Metodologi evaluasi dampak sosial dan mitigasi risiko regulasi."),
                Pair("Transformasi Ekosistem Hijau", "Peta jalan menuju ekosistem operasional rendah karbon dan berdaya tahan.")
            )
            "SMART CITY" -> listOf(
                Pair("Urban Data & Sensorik Cerdas", "Membangun arsitektur data kota berbasis sensor dan telemetri terpadu."),
                Pair("Tata Kelola Ekuitas Lahan", "Framework regulasi dan redistribusi spasial perkotaan yang berkeadilan."),
                Pair("Mobilitas & Integrasi Multimoda", "Sistem transportasi cerdas untuk optimasi aliran logistik warga."),
                Pair("Ketahanan Iklim Perkotaan", "Mitigasi bencana ekologis perkotaan melalui pemodelan prediktif.")
            )
            else -> listOf(
                Pair("The Attention Economy", "Understanding the mechanics behind digital distractions."),
                Pair("Deep Work Protocols", "Actionable steps to enter flow state on command."),
                Pair("Mindful Tech Integration", "Setting boundaries that stick without the guilt."),
                Pair("Cognitive Recovery", "Restoring your mental energy after intense digital usage.")
            )
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(40.dp)) {
            Text(
                text = "What's Inside",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily()
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            val chapterIcons = listOf(
                Res.drawable.icon_6,
                Res.drawable.icon_7,
                Res.drawable.icon_8,
                Res.drawable.icon_5
            )
            val chapterRows = if (stackChapters) 1 else 2

            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                chapterIcons.withIndex().chunked(chapterRows).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        rowItems.forEach { (index, icon) ->
                            InsideItem(
                                icon = icon,
                                title = chapters.getOrNull(index)?.first ?: "Chapter ${index + 1}",
                                subtitle = chapters.getOrNull(index)?.second ?: "",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            HorizontalDivider(color = Color(0xFFF1F4F7), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AVERAGE READING TIME",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = readingTime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = Color(0xFF46B778),
                trackColor = Color(0xFFF1F4F7),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun InsideItem(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF71717A),
                fontFamily = getDmSansFontFamily(),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MetadataSection(
    book: AdminBook? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F4F7)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MetadataItem("PUBLISHED", book?.publishedDate ?: "Nov 2025")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("PAGES", "${book?.pages ?: 240}")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("LANGUAGE", book?.language ?: "Indonesia")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("FORMAT", if (!book?.pdfUrl.isNullOrBlank()) "eBook, PDF" else "eBook")
        }
    }
}

@Composable
fun MetadataItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF71717A),
            fontWeight = FontWeight.Bold,
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookDetailsScreenPreview() {
    MaterialTheme {
        BookDetailsScreen()
    }
}
