package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.components.ProductCard
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.GetAdminBooksUseCase
import com.sekota.ui.WindowWidth
import com.sekota.ui.contentGridColumns
import com.sekota.ui.gridSpacing
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.windowWidthOf

@Composable
fun CatalogScreen(onBookClick: (String) -> Unit = {}, onNavigate: (Screen) -> Unit = {}) {
    val repository = remember { AdminRepositoryImpl() }
    val getBooksUseCase = remember { GetAdminBooksUseCase(repository) }
    var books by remember { mutableStateOf<List<AdminBook>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf("Newest") }
    var selectedOptions by remember { mutableStateOf(listOf("All")) }
    var selectedYears by remember { mutableStateOf(listOf("2025")) }

    LaunchedEffect(Unit) {
        books = getBooksUseCase()
        syncService.syncEventFlow.collect {
            books = getBooksUseCase()
        }
    }

    val filteredBooks = remember(books, searchQuery, selectedSort, selectedOptions, selectedYears) {
        books.filter { book ->
            val matchQuery = searchQuery.isBlank() ||
                book.title.contains(searchQuery, ignoreCase = true) ||
                book.author.contains(searchQuery, ignoreCase = true) ||
                book.description.contains(searchQuery, ignoreCase = true)

            val matchCategory = selectedOptions.contains("All") ||
                selectedOptions.any { opt -> book.category.equals(opt, ignoreCase = true) }

            val matchYear = selectedYears.isEmpty() || selectedYears.contains(book.year)

            matchQuery && matchCategory && matchYear
        }.let { list ->
            when (selectedSort) {
                "Rating Tertinggi" -> list.sortedByDescending { it.rating }
                "A-Z" -> list.sortedBy { it.title.lowercase() }
                "Z-A" -> list.sortedByDescending { it.title.lowercase() }
                else -> list // "Newest" default ordering
            }
        }
    }

    BoxWithConstraints {
        val windowWidth = windowWidthOf(maxWidth)
        // The 280dp filter rail only earns its space once the content pane can still
        // hold a two-up card grid beside it, i.e. from the Expanded breakpoint.
        val railVisible = windowWidth.isAtLeastExpanded
        val isCompact = !railVisible

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            if (isCompact) {
                // Stack vertically
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                        SidebarFilter(
                            modifier = Modifier.fillMaxWidth(),
                            onNavigate = onNavigate,
                            isMerchandise = false,
                            searchQuery = searchQuery,
                            onSearchChange = { searchQuery = it },
                            selectedSort = selectedSort,
                            onSortChange = { selectedSort = it },
                            selectedOptions = selectedOptions,
                            onOptionsChange = { selectedOptions = it },
                            selectedYears = selectedYears,
                            onYearsChange = { selectedYears = it }
                        )
                    }
                    CatalogContent(
                        windowWidth = windowWidth,
                        filteredBooks = filteredBooks,
                        onBookClick = onBookClick
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth().heightIn(min = 1000.dp)) {
                    Box(modifier = Modifier.width(280.dp).fillMaxHeight().background(Color.White)) {
                        SidebarFilter(
                            onNavigate = onNavigate,
                            isMerchandise = false,
                            searchQuery = searchQuery,
                            onSearchChange = { searchQuery = it },
                            selectedSort = selectedSort,
                            onSortChange = { selectedSort = it },
                            selectedOptions = selectedOptions,
                            onOptionsChange = { selectedOptions = it },
                            selectedYears = selectedYears,
                            onYearsChange = { selectedYears = it }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CatalogContent(
                            windowWidth = windowWidth,
                            filteredBooks = filteredBooks,
                            onBookClick = onBookClick
                        )
                    }
                }
            }
            Footer()
        }
    }
}

@Composable
fun CatalogContent(windowWidth: WindowWidth, filteredBooks: List<AdminBook>, onBookClick: (String) -> Unit) {
    val isCompact = windowWidth.isCompact
    // The rail steals 280dp from the pane on Expanded, so the pane's own card count
    // is one tier below the raw window class.
    val columns = if (windowWidth.isAtLeastExpanded) 3 else windowWidth.contentGridColumns
    val spacing = windowWidth.gridSpacing

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(windowWidth.sectionHorizontalPadding)
    ) {
        Text(
            text = "eBook Catalogue",
            fontSize = if (isCompact) 32.sp else 48.sp,
            lineHeight = if (isCompact) 40.sp else 56.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily(),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Membawa semangat sinergi ke dalam genggaman. E-book katalog ini adalah kurasi instrumen taktis yang dirancang untuk memperkuat konektivitas tim dan mitra strategis Anda. Mari orkestrasi identitas profesional bersama Sekota.",
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.7f),
            fontFamily = getDmSansFontFamily(),
            modifier = Modifier.widthIn(max = 800.dp),
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(48.dp))

        val palette = listOf(
            Color(0xFF1A1A1A),
            Color(0xFF2D2D2D),
            Color(0xFF0F172A),
            Color(0xFF003840),
            Color(0xFF1E293B),
            Color(0xFF334155)
        )

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📖 Tidak ada e-book yang cocok dengan filter Anda.",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                filteredBooks.chunked(columns).forEachIndexed { rowIndex, rowBooks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        rowBooks.forEachIndexed { colIndex, book ->
                            val color = palette[(rowIndex * columns + colIndex) % palette.size]
                            ProductCard(
                                title = book.title,
                                authorOrSubtitle = "${book.author} • ${book.category}",
                                rating = book.rating,
                                imageColor = color,
                                imageUrlOrBase64 = book.coverImage,
                                cardHeight = if (columns >= 3) 520.dp else 480.dp,
                                coverHeight = if (columns >= 3) 340.dp else 300.dp,
                                modifier = Modifier.weight(1f),
                                onClick = { onBookClick(book.id) }
                            )
                        }
                        repeat(columns - rowBooks.size) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 40.dp else 64.dp))
        Pagination()
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun Pagination() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Arrow Left
        PaginationArrow("<")
        
        Spacer(modifier = Modifier.width(12.dp))
        
        val pages = listOf("1", "2", "3", "...", "10")
        pages.forEach { page ->
            val isActive = page == "1"
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(40.dp)
                    .background(
                        if (isActive) Color(0xFF00BFA5) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page,
                    color = if (isActive) Color.White else Color.Gray,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Arrow Right
        PaginationArrow(">")
    }
}

@Composable
fun PaginationArrow(text: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogScreen(onBookClick = {}, onNavigate = {})
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun PaginationPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Pagination()
        }
    }
}
