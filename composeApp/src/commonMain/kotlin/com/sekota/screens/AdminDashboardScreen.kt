package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.usecase.*
import com.sekota.ui.WindowWidth
import com.sekota.ui.windowWidthOf

val InkNavy = Color(0xFF0D1F2D)
val BrandTeal = Color(0xFF00B5C8)

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getBooksUseCase = remember { GetAdminBooksUseCase(repository) }
    val saveBookUseCase = remember { SaveAdminBookUseCase(repository) }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    val saveProductUseCase = remember { SaveAdminProductUseCase(repository) }
    val deleteProductUseCase = remember { DeleteAdminProductUseCase(repository) }
    val getMerchUseCase = remember { GetAdminMerchUseCase(repository) }
    val saveMerchUseCase = remember { SaveAdminMerchUseCase(repository) }
    val deleteMerchUseCase = remember { DeleteAdminMerchUseCase(repository) }
    val getLiveMetricsUseCase = remember { GetAdminLiveMetricsUseCase(repository) }
    val saveLiveMetricsUseCase = remember { SaveAdminLiveMetricsUseCase(repository) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Books Registry", "Intelligence Suite", "Merchandise", "Live Metrics")
    val icons = listOf("📝", "🛠", "🛒", "ℹ️")

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // M3 adaptive navigation: NavigationBar below 600dp, NavigationRail from
        // Medium up. The rail collapses to icons-only at Medium so it does not eat
        // 250dp of a 600-840dp workbench.
        val windowWidth = windowWidthOf(maxWidth)
        val isCompact = windowWidth.isCompact
        val railExpanded = windowWidth.isAtLeastExpanded

        if (isCompact) {
            // Mobile (Android Phone) Layout: Top AppBar + Content + Bottom Navigation Bar
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = InkNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SEKOTA CMS",
                            color = BrandTeal,
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onLogout) {
                            Text(text = "🚪", fontSize = 18.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    when (selectedTab) {
                        0 -> BooksRegistryTab(getBooksUseCase, saveBookUseCase, windowWidth)
                        1 -> IntelligenceSuiteTab(getProductsUseCase, saveProductUseCase, deleteProductUseCase, windowWidth)
                        2 -> MerchandiseTab(getMerchUseCase, saveMerchUseCase, deleteMerchUseCase, windowWidth)
                        3 -> LiveMetricsTab(getLiveMetricsUseCase, saveLiveMetricsUseCase, windowWidth)
                    }
                }

                NavigationBar(
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(text = icons[index], fontSize = 20.sp) },
                            label = {
                                Text(
                                    text = title.split(" ").first(),
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.6f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        } else {
            // Desktop / Tablet Layout: Left Navigation Rail + Right Content Workbench
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier.width(if (railExpanded) 250.dp else 88.dp).fillMaxHeight(),
                    containerColor = InkNavy,
                    contentColor = Color.White
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = if (railExpanded) "ADMIN PANEL" else "CMS",
                        color = BrandTeal,
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = if (railExpanded) 20.sp else 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (railExpanded) 24.dp else 8.dp, vertical = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    tabs.forEachIndexed { index, title ->
                        NavigationRailItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(text = icons[index], fontSize = 24.sp) },
                            label = {
                                Text(
                                    text = if (railExpanded) title else title.split(" ").first(),
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = if (railExpanded) 14.sp else 10.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (selectedTab == index) BrandTeal else Color.LightGray
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = BrandTeal,
                                selectedTextColor = BrandTeal,
                                indicatorColor = InkNavy.copy(alpha = 0.5f),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onLogout,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(100.dp),
                        contentPadding = if (railExpanded) ButtonDefaults.ContentPadding else PaddingValues(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (railExpanded) 24.dp else 8.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = if (railExpanded) "🚪 Sign Out" else "🚪",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 13.sp,
                            maxLines = 1,
                            color = Color(0xFFFF8A80)
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(32.dp)) {
                    when (selectedTab) {
                        0 -> BooksRegistryTab(getBooksUseCase, saveBookUseCase, windowWidth)
                        1 -> IntelligenceSuiteTab(getProductsUseCase, saveProductUseCase, deleteProductUseCase, windowWidth)
                        2 -> MerchandiseTab(getMerchUseCase, saveMerchUseCase, deleteMerchUseCase, windowWidth)
                        3 -> LiveMetricsTab(getLiveMetricsUseCase, saveLiveMetricsUseCase, windowWidth)
                    }
                }
            }
        }
    }
}

/**
 * CMS workbench section header. A 32sp title next to a pill button needs roughly
 * 520dp; below that the action drops onto its own full-width row so neither the
 * heading nor the button is clipped.
 */
@Composable
private fun WorkbenchHeader(
    title: String,
    subtitle: String,
    windowWidth: WindowWidth,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    secondaryLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    val isCompact = windowWidth.isCompact
    val heading = @Composable { modifier: Modifier ->
        Column(modifier = modifier) {
            Text(
                text = title,
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = if (isCompact) 24.sp else 32.sp,
                lineHeight = if (isCompact) 30.sp else 40.sp,
                color = InkNavy
            )
            Text(
                text = subtitle,
                fontFamily = getDmSansFontFamily(),
                color = Color.Gray,
                fontSize = if (isCompact) 13.sp else 14.sp
            )
        }
    }
    val action = @Composable { modifier: Modifier ->
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (secondaryLabel != null && onSecondaryAction != null) {
                OutlinedButton(
                    onClick = onSecondaryAction,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = InkNavy),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text(secondaryLabel, fontFamily = getDmSansFontFamily(), fontSize = 13.sp, maxLines = 1)
                }
            }
            if (actionLabel != null && onAction != null) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50),
                    modifier = if (isCompact) Modifier.weight(1f) else Modifier
                ) {
                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(actionLabel, fontFamily = getDmSansFontFamily(), maxLines = 1)
                }
            }
        }
    }

    if (isCompact) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            heading(Modifier.fillMaxWidth())
            action(Modifier.fillMaxWidth())
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // weight(1f) keeps the heading from starving the button of width.
            heading(Modifier.weight(1f).padding(end = 16.dp))
            action(Modifier)
        }
    }
}

@Composable
fun BooksRegistryTab(
    getBooksUseCase: GetAdminBooksUseCase,
    saveBookUseCase: SaveAdminBookUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded
) {
    var books by remember { mutableStateOf<List<AdminBook>>(emptyList()) }

    // UC-CMS-03 "Kelola Tampilan Web": operator-owned presentation metadata.
    // Book identity (title/author/ISBN/cover) and lifecycle (create/delete) are
    // owned by bookinteractiontool; this tab is read-only for those.
    var showWebDialog by remember { mutableStateOf(false) }
    var webBook by remember { mutableStateOf<AdminBook?>(null) }
    var webCategory by remember { mutableStateOf("") }
    var webDescription by remember { mutableStateOf("") }
    var webPdfUrl by remember { mutableStateOf("") }
    var webReadingTime by remember { mutableStateOf("") }
    var webPages by remember { mutableStateOf("") }
    var webYear by remember { mutableStateOf("") }
    var webLanguage by remember { mutableStateOf("") }
    var webPublishedDate by remember { mutableStateOf("") }
    var webError by remember { mutableStateOf<String?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = getBooksUseCase()
    }

    // UC-CMS-03 / WP-026: web presentation metadata editor.
    if (showWebDialog && webBook != null) {
        val target = webBook!!
        AlertDialog(
            onDismissRequest = { showWebDialog = false; webBook = null },
            title = {
                Column {
                    Text(
                        text = "\u270F\uFE0F Kelola Tampilan Web",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = InkNavy
                    )
                    Text(
                        text = target.title,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    webError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        "KATEGORI KATALOG",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    // Must match SidebarFilter's options or the book drops out of
                    // every catalog filter on the public site.
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("SMART CITY", "ESG", "INTELLIGENCE", "Other").forEach { cat ->
                            FilterChip(
                                selected = webCategory.equals(cat, ignoreCase = true),
                                onClick = { webCategory = cat; webError = null },
                                label = { Text(cat, fontFamily = getDmSansFontFamily(), fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandTeal.copy(alpha = 0.18f),
                                    selectedLabelColor = InkNavy
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = webDescription,
                        onValueChange = { webDescription = it; webError = null },
                        label = { Text("Deskripsi Editorial", fontFamily = getDmSansFontFamily()) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = webPdfUrl,
                        onValueChange = { webPdfUrl = it; webError = null },
                        label = { Text("URL PDF / Reader", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("https://sekota.id/reader/${target.id}") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val durationField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webReadingTime,
                            onValueChange = { webReadingTime = it; webError = null },
                            label = { Text("Durasi Baca", fontFamily = getDmSansFontFamily()) },
                            placeholder = { Text("3H 45M") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val pagesField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webPages,
                            onValueChange = { webPages = it.filter { c -> c.isDigit() }; webError = null },
                            label = { Text("Jumlah Halaman", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            isError = webPages.isNotBlank() && webPages.toIntOrNull() == null,
                            modifier = modifier
                        )
                    }
                    if (windowWidth.isCompact) {
                        durationField(Modifier.fillMaxWidth())
                        pagesField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            durationField(Modifier.weight(1f))
                            pagesField(Modifier.weight(1f))
                        }
                    }

                    val yearField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webYear,
                            onValueChange = { webYear = it.filter { c -> c.isDigit() }.take(4); webError = null },
                            label = { Text("Tahun Terbit", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val languageField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = webLanguage,
                            onValueChange = { webLanguage = it; webError = null },
                            label = { Text("Bahasa", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    if (windowWidth.isCompact) {
                        yearField(Modifier.fillMaxWidth())
                        languageField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            yearField(Modifier.weight(1f))
                            languageField(Modifier.weight(1f))
                        }
                    }

                    OutlinedTextField(
                        value = webPublishedDate,
                        onValueChange = { webPublishedDate = it; webError = null },
                        label = { Text("Tanggal Terbit", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("Nov 2025") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    // Architecture rule 6: telemetry is engine-owned. Shown for
                    // context, deliberately not editable.
                    Text(
                        "TELEMETRI (READ-ONLY)",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val hasRating = target.rating > 0.0
                            Text(
                                text = if (hasRating) "\u2605 ${target.rating}  \u2022  ${target.ratingCount} pembaca terdaftar"
                                       else "Publikasi Baru \u2014 belum ada rating",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasRating) Color(0xFF60BD65) else Color.Gray
                            )
                            Text(
                                text = "${target.interactions} interaksi / penggunaan tool",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Dihitung oleh mesin telemetri bookinteractiontool; tidak dapat diubah operator.",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pages = webPages.trim().toIntOrNull()
                        if (webPages.isNotBlank() && (pages == null || pages <= 0)) {
                            webError = "Jumlah halaman harus berupa angka lebih dari 0"
                            return@Button
                        }
                        scope.launch {
                            val updated = target.copy(
                                category = webCategory.trim().ifBlank { target.category },
                                description = webDescription.trim(),
                                pdfUrl = webPdfUrl.trim().ifBlank { null },
                                readingTime = webReadingTime.trim().ifBlank { target.readingTime },
                                pages = pages ?: target.pages,
                                year = webYear.trim().ifBlank { target.year },
                                language = webLanguage.trim().ifBlank { target.language },
                                publishedDate = webPublishedDate.trim().ifBlank { target.publishedDate }
                            )
                            val result = saveBookUseCase(updated)
                            if (result.isSuccess) {
                                books = getBooksUseCase()
                                showWebDialog = false
                                webBook = null
                                webError = null
                            } else {
                                webError = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Tampilan Web", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWebDialog = false; webBook = null; webError = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Books Registry",
            subtitle = "Master naskah disinkronkan dari bookinteractiontool \u2014 kelola tampilan web di sini (${books.size} terdaftar)",
            windowWidth = windowWidth,
            secondaryLabel = if (isRefreshing) "Memuat..." else "\u21BB Refresh Live Data",
            onSecondaryAction = {
                if (!isRefreshing) {
                    scope.launch {
                        isRefreshing = true
                        books = getBooksUseCase()
                        isRefreshing = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(books, key = { it.id }) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    // Metadata and the two action buttons need ~560dp side by side;
                    // on compact the actions move to their own row underneath.
                    val bookRowStacked = windowWidth.isCompact
                    val bookMeta = @Composable { modifier: Modifier ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
                            Surface(
                                modifier = Modifier.size(60.dp, 80.dp),
                                color = if (book.coverImage != null) BrandTeal.copy(alpha = 0.15f) else InkNavy.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (book.coverImage != null) "🖼️" else "📖", fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    book.title, 
                                    fontFamily = getMontserratFontFamily(), 
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 16.sp,
                                    color = InkNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Penulis: ${book.author}  •  ISBN: ${book.isbn}",
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "ID: ${book.id}", 
                                        fontFamily = getDmSansFontFamily(), 
                                        color = BrandTeal,
                                        fontSize = 11.sp
                                    )
                                    if (book.coverImage != null) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "• Cover Set", 
                                            fontFamily = getDmSansFontFamily(), 
                                            color = Color(0xFF60BD65),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    val bookActions = @Composable { modifier: Modifier ->
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = modifier,
                            horizontalArrangement = Arrangement.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    webBook = book
                                    webCategory = book.category
                                    webDescription = book.description
                                    webPdfUrl = book.pdfUrl ?: ""
                                    webReadingTime = book.readingTime
                                    webPages = book.pages.toString()
                                    webYear = book.year
                                    webLanguage = book.language
                                    webPublishedDate = book.publishedDate
                                    webError = null
                                    showWebDialog = true
                                }
                            ) {
                                Text(
                                    "\u270F\uFE0F Tampilan Web",
                                    color = Color(0xFF60BD65),
                                    fontFamily = getDmSansFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    if (bookRowStacked) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            bookMeta(Modifier.fillMaxWidth())
                            bookActions(Modifier.fillMaxWidth())
                        }
                    } else {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            bookMeta(Modifier.weight(1f))
                            bookActions(Modifier)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IntelligenceSuiteTab(
    getProductsUseCase: GetAdminProductsUseCase,
    saveProductUseCase: SaveAdminProductUseCase,
    deleteProductUseCase: DeleteAdminProductUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded
) {
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<AdminProduct?>(null) }
    var prodCode by remember { mutableStateOf("") }
    var prodName by remember { mutableStateOf("") }
    var prodCategory by remember { mutableStateOf("Intelligence Suite") }
    var prodDesc by remember { mutableStateOf("") }
    var prodFeaturesText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; editingProduct = null },
            title = {
                Text(
                    text = if (editingProduct == null) "Tambah Produk Intelligence Suite" else "Edit Modul: ${editingProduct?.name}",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp)
                    }
                    val codeField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = prodCode,
                            onValueChange = { prodCode = it; errorMessage = null },
                            label = { Text("Kode (e.g. VRD, ASC)") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    val nameField = @Composable { modifier: Modifier ->
                        OutlinedTextField(
                            value = prodName,
                            onValueChange = { prodName = it; errorMessage = null },
                            label = { Text("Nama Produk") },
                            singleLine = true,
                            modifier = modifier
                        )
                    }
                    // Two fields on one line leave the code field unreadable on a phone.
                    if (windowWidth.isCompact) {
                        codeField(Modifier.fillMaxWidth())
                        nameField(Modifier.fillMaxWidth())
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            codeField(Modifier.weight(0.4f))
                            nameField(Modifier.weight(0.6f))
                        }
                    }
                    OutlinedTextField(
                        value = prodCategory,
                        onValueChange = { prodCategory = it; errorMessage = null },
                        label = { Text("Kategori / Eyebrow") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodDesc,
                        onValueChange = { prodDesc = it; errorMessage = null },
                        label = { Text("Deskripsi Editorial") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prodFeaturesText,
                        onValueChange = { prodFeaturesText = it; errorMessage = null },
                        label = { Text("Core Modules / Features (pisahkan dengan koma)") },
                        placeholder = { Text("e.g. Sentiment Analytics, ESG Metric Tracker") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (prodName.isBlank()) {
                            errorMessage = "Nama produk tidak boleh kosong"
                            return@Button
                        }
                        if (prodCode.isBlank()) {
                            errorMessage = "Kode produk tidak boleh kosong"
                            return@Button
                        }
                        scope.launch {
                            val id = editingProduct?.id ?: prodCode.lowercase().trim().ifBlank { "prod-${kotlin.random.Random.nextInt(1000, 9999)}" }
                            val features = prodFeaturesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            val productToSave = AdminProduct(
                                id = id,
                                code = prodCode.trim().uppercase(),
                                name = prodName.trim(),
                                categoryEyebrow = prodCategory.trim().ifBlank { "Intelligence Suite" },
                                description = prodDesc.trim(),
                                features = if (features.isNotEmpty()) features else listOf("Core Intelligence")
                            )
                            val result = saveProductUseCase(productToSave)
                            if (result.isSuccess) {
                                products = getProductsUseCase()
                                showDialog = false
                                editingProduct = null
                                prodCode = ""
                                prodName = ""
                                prodDesc = ""
                                prodFeaturesText = ""
                                errorMessage = null
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text(if (editingProduct == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingProduct = null }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Intelligence Suite",
            subtitle = "Kelola produk kecerdasan bisnis Sekota (${products.size} modul terdaftar)",
            windowWidth = windowWidth,
            actionLabel = "Add Product",
            onAction = {
                editingProduct = null
                prodCode = ""
                prodName = ""
                prodCategory = "Intelligence Suite"
                prodDesc = ""
                prodFeaturesText = ""
                errorMessage = null
                showDialog = true
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rule 11: Adaptive grid for CMS Intelligence Suite
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (windowWidth.isCompact) 240.dp else 320.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products, key = { it.id }) { prod ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prod.name, 
                                fontFamily = getMontserratFontFamily(), 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 22.sp,
                                color = InkNavy
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrandTeal.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = prod.code,
                                    color = BrandTeal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(prod.description, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Core Modules:", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        prod.features.forEach { feat ->
                            Text("• $feat", fontFamily = getDmSansFontFamily(), color = Color.DarkGray, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingProduct = prod
                                    prodCode = prod.code
                                    prodName = prod.name
                                    prodCategory = prod.categoryEyebrow
                                    prodDesc = prod.description
                                    prodFeaturesText = prod.features.joinToString(", ")
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) {
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    products = products.filter { it.id != prod.id }
                                    scope.launch {
                                        deleteProductUseCase(prod.id)
                                    }
                                }
                            ) {
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MerchandiseTab(
    getMerchUseCase: GetAdminMerchUseCase,
    saveMerchUseCase: SaveAdminMerchUseCase,
    deleteMerchUseCase: DeleteAdminMerchUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded
) {
    var merchList by remember { mutableStateOf<List<AdminMerch>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingMerchId by remember { mutableStateOf<String?>(null) }
    var itemTitle by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("Apparel") }
    var itemPrice by remember { mutableStateOf("") }
    var itemImageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        merchList = getMerchUseCase()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (editingMerchId == null) "Tambah Merchandise Baru" else "Edit Merchandise",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    errorMessage?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedTextField(
                        value = itemTitle,
                        onValueChange = { itemTitle = it; errorMessage = null },
                        label = { Text("Nama Item", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemCategory,
                        onValueChange = { itemCategory = it; errorMessage = null },
                        label = { Text("Kategori (e.g. Apparel, Accessories)", fontFamily = getDmSansFontFamily()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it; errorMessage = null },
                        label = { Text("Harga (USD / $)", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("Contoh: 45.0") },
                        singleLine = true,
                        isError = errorMessage != null && itemPrice.toDoubleOrNull() == null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemImageUrl,
                        onValueChange = { itemImageUrl = it; errorMessage = null },
                        label = { Text("Image URL / Path", fontFamily = getDmSansFontFamily()) },
                        placeholder = { Text("e.g. /images/merch-hoodie.png") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Image thumbnail preview
                    if (itemImageUrl.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp, 40.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = BrandTeal.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🖼️", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Image Ready", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = InkNavy)
                                Text(itemImageUrl, fontFamily = getDmSansFontFamily(), fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = itemPrice.trim().toDoubleOrNull()
                        if (itemTitle.isBlank()) {
                            errorMessage = "Nama item tidak boleh kosong"
                            return@Button
                        }
                        if (parsedPrice == null || parsedPrice <= 0.0) {
                            errorMessage = "Harga harus berupa angka valid lebih dari 0 (contoh: 45.0)"
                            return@Button
                        }
                        scope.launch {
                            val id = editingMerchId ?: "m-${kotlin.random.Random.nextInt(1000, 9999)}"
                            val img = itemImageUrl.trim().ifBlank { null }
                            val result = saveMerchUseCase(AdminMerch(id, itemTitle.trim(), itemCategory.trim(), "The Urban Collaborator", parsedPrice, 5.0, img))
                            if (result.isSuccess) {
                                merchList = getMerchUseCase()
                                showDialog = false
                                editingMerchId = null
                                itemTitle = ""
                                itemCategory = "Apparel"
                                itemPrice = ""
                                itemImageUrl = ""
                                errorMessage = null
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (editingMerchId == null) "Simpan" else "Perbarui", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; editingMerchId = null }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Merchandise Store",
            subtitle = "Kelola inventaris suvenir dan pernak-pernik resmi (${merchList.size} item)",
            windowWidth = windowWidth,
            actionLabel = "Add Item",
            onAction = {
                editingMerchId = null
                itemTitle = ""
                itemCategory = "Apparel"
                itemPrice = ""
                itemImageUrl = ""
                errorMessage = null
                showDialog = true
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rule 11: Adaptive grid for CMS Merchandise
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (windowWidth.isCompact) 200.dp else 260.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(merchList, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            color = if (item.imageUrl != null) BrandTeal.copy(alpha = 0.1f) else InkNavy.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (item.imageUrl != null) "🖼️" else "👕", fontSize = 36.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(item.title, fontFamily = getMontserratFontFamily(), fontWeight = FontWeight.Bold, color = InkNavy, fontSize = 16.sp)
                        Text("$${item.price}", fontFamily = getDmSansFontFamily(), color = BrandTeal, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.category, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 12.sp)
                            Text("★ ${item.rating}", fontFamily = getDmSansFontFamily(), color = Color(0xFF60BD65), fontSize = 12.sp)
                        }
                        if (item.imageUrl != null) {
                            Text(
                                "Image: ${item.imageUrl}",
                                fontFamily = getDmSansFontFamily(),
                                color = Color.Gray,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = {
                                    editingMerchId = item.id
                                    itemTitle = item.title
                                    itemCategory = item.category
                                    itemPrice = item.price.toString()
                                    itemImageUrl = item.imageUrl ?: ""
                                    errorMessage = null
                                    showDialog = true
                                }
                            ) { 
                                Text("✏️ Edit", color = BrandTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                            TextButton(
                                onClick = {
                                    merchList = merchList.filter { it.id != item.id }
                                    scope.launch {
                                        deleteMerchUseCase(item.id)
                                    }
                                }
                            ) { 
                                Text("🗑 Delete", color = Color(0xFFE53E3E), fontSize = 13.sp, fontWeight = FontWeight.Bold) 
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMetricsTab(
    getLiveMetricsUseCase: GetAdminLiveMetricsUseCase,
    saveLiveMetricsUseCase: SaveAdminLiveMetricsUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded
) {
    var accuracy by remember { mutableStateOf("99.8%") }
    var totalClients by remember { mutableStateOf("100+") }
    var establishedYear by remember { mutableStateOf("2021") }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val metrics = getLiveMetricsUseCase()
        accuracy = metrics.dataAccuracy
        totalClients = metrics.totalClients
        establishedYear = metrics.establishedYear
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkbenchHeader(
            title = "Live Metrics Editor",
            subtitle = "Konfigurasi angka metrik yang tampil langsung pada Hero & Trust section Landing Page",
            windowWidth = windowWidth
        )
        Spacer(modifier = Modifier.height(12.dp))

        statusFeedback?.let {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32),
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        // Rule 11: Adaptive wrapping for Live Metrics Workbench
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Data Accuracy",
                value = accuracy,
                onValueChange = { accuracy = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Data Accuracy diperbarui ke '$accuracy'"
                        }
                    }
                }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Total Clients",
                value = totalClients,
                onValueChange = { totalClients = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Total Clients diperbarui ke '$totalClients'"
                        }
                    }
                }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Established",
                value = establishedYear,
                onValueChange = { establishedYear = it; statusFeedback = null },
                onSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = "✅ Tahun Didirikan diperbarui ke '$establishedYear'"
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MetricCardEditor(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.widthIn(min = 180.dp, max = 260.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontFamily = getDmSansFontFamily(), color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BrandTeal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Metrik", fontFamily = getDmSansFontFamily(), fontSize = 13.sp)
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AdminDashboardPreview() {
    MaterialTheme {
        AdminDashboardScreen()
    }
}
