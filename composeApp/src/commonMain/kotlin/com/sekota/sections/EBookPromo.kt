package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.GetAdminBooksUseCase
import com.sekota.utils.decodeBase64ToBitmap
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.contentGridColumns
import com.sekota.ui.gridSpacing
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding
import com.sekota.ui.windowWidthOf

@Composable
fun EBookPromo(
    onNavigateToCatalog: () -> Unit = {},
    onBookClick: (AdminBook) -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        EBookPromoContent(windowWidthOf(maxWidth), contentHorizontalPadding(maxWidth), onNavigateToCatalog, onBookClick)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EBookPromoContent(
    windowWidth: WindowWidth,
    horizontalPadding: Dp,
    onNavigateToCatalog: () -> Unit,
    onBookClick: (AdminBook) -> Unit
) {
    val repository = remember { AdminRepositoryImpl() }
    val getBooksUseCase = remember { GetAdminBooksUseCase(repository) }
    var allBooks by remember { mutableStateOf<List<AdminBook>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        allBooks = getBooksUseCase()
        syncService.syncEventFlow.collect {
            allBooks = getBooksUseCase()
        }
    }

    val filteredBooks = remember(allBooks, selectedFilter) {
        if (selectedFilter.equals("Semua", ignoreCase = true)) {
            allBooks
        } else {
            allBooks.filter { it.category.contains(selectedFilter, ignoreCase = true) }
        }
    }

    val palette = listOf(
        Color(0xFF1976D2),
        Color(0xFFBF360C),
        Color(0xFF388E3C),
        Color(0xFF673AB7),
        Color(0xFF00838F),
        Color(0xFF455A64)
    )

    val isCompact = windowWidth.isCompact

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = windowWidth.sectionVerticalPadding,
                horizontal = horizontalPadding
            )
    ) {
        // Subtitle
        Text(
            text = "PERPUSTAKAAN DIGITAL",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF26A69A),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Title and Filters. The four pills need ~380dp; below Expanded they drop
        // onto their own wrapping row instead of squeezing the headline.
        val title = @Composable {
            Text(
                text = "E-Book & Panduan\nStrategis.",
                fontSize = if (isCompact) 32.sp else 48.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = getMontserratFontFamily(),
                lineHeight = if (isCompact) 40.sp else 56.sp
            )
        }
        val filters = @Composable {
            FlowRow(
                modifier = Modifier
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Semua", "Smart City", "ESG", "Intelligence").forEach { filter ->
                    FilterPill(
                        text = filter,
                        active = selectedFilter.equals(filter, ignoreCase = true),
                        onClick = { selectedFilter = filter }
                    )
                }
            }
        }

        if (windowWidth.isAtMostMedium) {
            title()
            Spacer(modifier = Modifier.height(24.dp))
            filters()
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                title()
                filters()
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 40.dp else 64.dp))

        // Cards Row
        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada e-book untuk kategori ini.",
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            // 4 across only from Large; a 420dp-tall card needs ~240dp of width
            // before its title and description start clipping.
            val columns = when (windowWidth) {
                WindowWidth.Compact -> 1
                WindowWidth.Medium -> 2
                WindowWidth.Expanded -> 3
                else -> 4
            }
            val shown = filteredBooks.take(4)
            Column(verticalArrangement = Arrangement.spacedBy(windowWidth.gridSpacing)) {
                shown.chunked(columns).forEachIndexed { rowIndex, rowBooks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(windowWidth.gridSpacing)
                    ) {
                        rowBooks.forEachIndexed { colIndex, book ->
                            val color = palette[(rowIndex * columns + colIndex) % palette.size]
                            EBookCard(
                                book = book,
                                color = color,
                                modifier = Modifier.weight(1f),
                                onClick = { onBookClick(book) }
                            )
                        }
                        repeat(columns - rowBooks.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 40.dp else 64.dp))

        // Footer Button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = onNavigateToCatalog,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF26A69A)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                modifier = Modifier.height(56.dp).padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Lihat Katalog",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily()
                )
            }
        }
    }
}

@Composable
fun FilterPill(text: String, active: Boolean, onClick: () -> Unit = {}) {
    Surface(
        color = if (active) Color.White else Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = if (active) 2.dp else 0.dp,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            fontSize = 14.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            color = if (active) Color.Black else Color.Gray,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Composable
fun EBookCard(
    book: AdminBook,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cover = book.coverImage
    val coverBitmap = remember(cover) {
        if (!cover.isNullOrBlank()) {
            decodeBase64ToBitmap(cover)
        } else null
    }

    Card(
        modifier = modifier
            .height(420.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Section (Cover Image or Vibrant Color Card)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .background(color, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                if (coverBitmap != null) {
                    Image(
                        bitmap = coverBitmap,
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Semi-transparent gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Tag
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = book.category.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Title
                    Text(
                        text = book.title,
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = getMontserratFontFamily(),
                        lineHeight = 26.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Bottom Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val desc = if (book.description.isNotBlank()) book.description else "Karya ilmiah dan panduan taktis resmi dari ${book.author}."
                Text(
                    text = desc,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIHAT DETAIL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = getDmSansFontFamily()
                    )
                    Text(
                        text = "›",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun EBookPromoPreview() {
    EBookPromo()
}
