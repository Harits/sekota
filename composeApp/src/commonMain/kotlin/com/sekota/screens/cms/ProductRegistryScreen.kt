package com.sekota.screens.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.components.cms.CmsActionHeader
import com.sekota.components.cms.CmsBrandTeal
import com.sekota.components.cms.CmsImageUploader
import com.sekota.components.cms.CmsInputField
import com.sekota.components.cms.CmsInkNavy
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.WindowWidth
import com.sekota.utils.decodeBase64ToBitmap

/**
 * Product Registry: Unified Catalog Workspace consolidating Books, Intelligence Suite, and Merchandise.
 * Powered by MVI ProductRegistryViewModel.
 */
@Composable
fun ProductRegistryScreen(
    getBooksUseCase: GetAdminBooksUseCase,
    saveBookUseCase: SaveAdminBookUseCase,
    getCategoriesUseCase: GetAdminCategoriesUseCase,
    addCategoryUseCase: AddAdminCategoryUseCase,
    deleteCategoryUseCase: DeleteAdminCategoryUseCase,
    getProductsUseCase: GetAdminProductsUseCase,
    saveProductUseCase: SaveAdminProductUseCase,
    deleteProductUseCase: DeleteAdminProductUseCase,
    getMerchUseCase: GetAdminMerchUseCase,
    saveMerchUseCase: SaveAdminMerchUseCase,
    deleteMerchUseCase: DeleteAdminMerchUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val viewModel = remember(
        getBooksUseCase, saveBookUseCase, getCategoriesUseCase,
        getProductsUseCase, saveProductUseCase, deleteProductUseCase,
        getMerchUseCase, saveMerchUseCase, deleteMerchUseCase
    ) {
        ProductRegistryViewModel(
            getBooksUseCase, saveBookUseCase, getCategoriesUseCase,
            getProductsUseCase, saveProductUseCase, deleteProductUseCase,
            getMerchUseCase, saveMerchUseCase, deleteMerchUseCase,
            scope
        )
    }

    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProductRegistryEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is ProductRegistryEffect.RequestProductionGuard -> {
                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard(
                            effect.actionName,
                            effect.entityName,
                            effect.isCriticalDelete,
                            effect.executeAction
                        )
                    } else {
                        effect.executeAction()
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ProductRegistryContent(
            state = state,
            windowWidth = windowWidth,
            onIntent = { viewModel.onIntent(it) }
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}

@Composable
fun ProductRegistryContent(
    state: ProductRegistryState,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onIntent: (ProductRegistryIntent) -> Unit
) {
    // 1. Modal Book Web Metadata Editor
    if (state.activeModal is ActiveModalState.BookMetadataModal) {
        val modal = state.activeModal
        AlertDialog(
            onDismissRequest = { onIntent(ProductRegistryIntent.DismissModal) },
            title = {
                Column {
                    Text(
                        text = "Kelola Tampilan Web Buku",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CmsInkNavy
                    )
                    Text(
                        text = modal.targetBook.title,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    modal.validationError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = modal.category,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateBookCategory(it)) },
                        label = "Kategori Buku"
                    )

                    CmsInputField(
                        value = modal.description,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateBookDescription(it)) },
                        label = "Sinopsis & Deskripsi",
                        isSingleLine = false,
                        minLines = 3
                    )

                    CmsInputField(
                        value = modal.pdfUrl,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateBookPdfUrl(it)) },
                        label = "PDF Reader URL"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmsInputField(
                            value = modal.readingTime,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookReadingTime(it)) },
                            label = "Estimasi Baca",
                            modifier = Modifier.weight(1f)
                        )
                        CmsInputField(
                            value = modal.pages,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookPages(it)) },
                            label = "Jumlah Hal",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmsInputField(
                            value = modal.year,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookYear(it)) },
                            label = "Tahun Terbit",
                            modifier = Modifier.weight(1f)
                        )
                        CmsInputField(
                            value = modal.language,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookLanguage(it)) },
                            label = "Bahasa",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    CmsInputField(
                        value = modal.publishedDate,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateBookPublishedDate(it)) },
                        label = "Tanggal Rilis (e.g. 12 Januari 2026)"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(ProductRegistryIntent.SaveActiveModal) },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ProductRegistryIntent.DismissModal) }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // 2. Modal Product Suite Editor
    if (state.activeModal is ActiveModalState.SuiteProductModal) {
        val modal = state.activeModal
        AlertDialog(
            onDismissRequest = { onIntent(ProductRegistryIntent.DismissModal) },
            title = {
                Text(
                    text = if (modal.editingProductId == null) "Tambah Intelligence Module" else "Edit Intelligence Module",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CmsInkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    modal.validationError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = modal.name,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateProductName(it)) },
                        label = "Nama Produk"
                    )

                    CmsInputField(
                        value = modal.code,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateProductCode(it)) },
                        label = "Kode Produk"
                    )

                    CmsInputField(
                        value = modal.categoryEyebrow,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateProductEyebrow(it)) },
                        label = "Category Eyebrow"
                    )

                    CmsInputField(
                        value = modal.description,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateProductDescription(it)) },
                        label = "Deskripsi Solusi",
                        isSingleLine = false,
                        minLines = 3
                    )

                    CmsInputField(
                        value = modal.featuresText,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateProductFeaturesText(it)) },
                        label = "Fitur Utama (Pisahkan dengan koma)",
                        placeholder = "e.g. Analytics, AI Forecast, Edge IoT"
                    )

                    CmsImageUploader(
                        imageBase64 = modal.coverImageBase64,
                        onImageSelected = { onIntent(ProductRegistryIntent.UpdateProductCoverImage(it)) },
                        label = "Icon / Cover Modul Intelligence"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(ProductRegistryIntent.SaveActiveModal) },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ProductRegistryIntent.DismissModal) }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // 3. Modal Merchandise Editor
    if (state.activeModal is ActiveModalState.MerchModal) {
        val modal = state.activeModal
        AlertDialog(
            onDismissRequest = { onIntent(ProductRegistryIntent.DismissModal) },
            title = {
                Text(
                    text = if (modal.editingMerchId == null) "Tambah Merchandise" else "Edit Merchandise",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CmsInkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    modal.validationError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = modal.title,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateMerchTitle(it)) },
                        label = "Nama Produk Merchandise"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmsInputField(
                            value = modal.category,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateMerchCategory(it)) },
                            label = "Kategori",
                            modifier = Modifier.weight(1f)
                        )
                        CmsInputField(
                            value = modal.seriesName,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateMerchSeriesName(it)) },
                            label = "Nama Series",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    CmsInputField(
                        value = modal.price,
                        onValueChange = { onIntent(ProductRegistryIntent.UpdateMerchPrice(it)) },
                        label = "Harga (USD)",
                        placeholder = "e.g. 29.99"
                    )

                    CmsImageUploader(
                        imageBase64 = modal.imageBase64,
                        onImageSelected = { onIntent(ProductRegistryIntent.UpdateMerchImage(it)) },
                        label = "Foto Produk Merchandise"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(ProductRegistryIntent.SaveActiveModal) },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ProductRegistryIntent.DismissModal) }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // Main Unified Registry View
    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Product Registry",
            subtitle = "Repositori terpadu katalog buku (${state.books.size}), intelligence suite (${state.products.size}), dan merchandise (${state.merchList.size}).",
            windowWidth = windowWidth,
            actionLabel = if (state.selectedFilter == ProductTypeFilter.BOOKS) null else "Tambah Item",
            onAction = { onIntent(ProductRegistryIntent.OpenCreateModal) },
            secondaryLabel = "Refresh Data",
            onSecondaryAction = { onIntent(ProductRegistryIntent.Refresh) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Responsive Filter Bar & Search Box
        if (windowWidth.isCompact) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onIntent(ProductRegistryIntent.SetSearchQuery(it)) },
                    placeholder = {
                        Text(
                            text = "Cari produk, buku, atau kategori...",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = CmsInkNavy
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                )

                // Responsive Horizontal Scroll on mobile to prevent chip collision
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ProductTypeFilter.values().forEach { filter ->
                        FilterChip(
                            selected = state.selectedFilter == filter,
                            onClick = { onIntent(ProductRegistryIntent.SetFilter(filter)) },
                            label = {
                                Text(
                                    text = filter.title.replace("Registry", "").replace("Intelligence ", "").trim(),
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CmsBrandTeal.copy(alpha = 0.15f),
                                selectedLabelColor = CmsBrandTeal
                            )
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProductTypeFilter.values().forEach { filter ->
                        FilterChip(
                            selected = state.selectedFilter == filter,
                            onClick = { onIntent(ProductRegistryIntent.SetFilter(filter)) },
                            label = {
                                Text(
                                    text = filter.title,
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CmsBrandTeal.copy(alpha = 0.15f),
                                selectedLabelColor = CmsBrandTeal
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onIntent(ProductRegistryIntent.SetSearchQuery(it)) },
                    placeholder = {
                        Text(
                            text = "Cari produk atau buku...",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 12.sp,
                        color = CmsInkNavy
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(260.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unified Adaptive Grid with 280dp minSize for optimal mobile and medium layouts
        val items = state.filteredItems
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Tidak ada entitas yang sesuai dengan filter/pencarian.",
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 280.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items, key = { it.id }) { item ->
                    RegistryCardItem(
                        item = item,
                        onEdit = {
                            when (item) {
                                is RegistryItem.Book -> onIntent(ProductRegistryIntent.OpenEditBookModal(item.book))
                                is RegistryItem.SuiteProduct -> onIntent(ProductRegistryIntent.OpenEditProductModal(item.product))
                                is RegistryItem.Merch -> onIntent(ProductRegistryIntent.OpenEditMerchModal(item.merch))
                            }
                        },
                        onDelete = {
                            when (item) {
                                is RegistryItem.Book -> {
                                    // Invariant: Book identity is telemetry-managed
                                }
                                is RegistryItem.SuiteProduct -> onIntent(ProductRegistryIntent.RequestDeleteProduct(item.product))
                                is RegistryItem.Merch -> onIntent(ProductRegistryIntent.RequestDeleteMerch(item.merch))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RegistryCardItem(
    item: RegistryItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val decodedBitmap = remember(item.imageUrl) {
        val img = item.imageUrl
        if (!img.isNullOrBlank() && !img.startsWith("http") && !img.startsWith("/")) {
            try {
                decodeBase64ToBitmap(img.substringAfter(","))
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (item) {
                        is RegistryItem.Book -> Color(0xFFE0F2FE)
                        is RegistryItem.SuiteProduct -> Color(0xFFF3E8FF)
                        is RegistryItem.Merch -> Color(0xFFFEF3C7)
                    },
                    modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                ) {
                    Text(
                        text = when (item) {
                            is RegistryItem.Book -> "Buku: ${item.category}"
                            is RegistryItem.SuiteProduct -> "Suite: ${item.category}"
                            is RegistryItem.Merch -> "Merch: ${item.category}"
                        },
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = when (item) {
                            is RegistryItem.Book -> Color(0xFF0369A1)
                            is RegistryItem.SuiteProduct -> Color(0xFF6B21A8)
                            is RegistryItem.Merch -> Color(0xFF92400E)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "ID: ${item.id}",
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (decodedBitmap != null) {
                            Image(
                                bitmap = decodedBitmap,
                                contentDescription = item.title,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = when (item) {
                                    is RegistryItem.Book -> "\uD83D\uDCD6"
                                    is RegistryItem.SuiteProduct -> "\u2699\uFE0F"
                                    is RegistryItem.Merch -> "\uD83D\uDC55"
                                },
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CmsInkNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.subtitle,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item !is RegistryItem.Book) {
                    TextButton(onClick = onDelete) {
                        Text("Hapus", color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (item is RegistryItem.Book) "Edit Web Metadata" else "Edit Detail",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Previews
// -------------------------------------------------------------

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductRegistryPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            RegistryCardItem(
                item = RegistryItem.Book(
                    AdminBook(
                        id = "bk-01",
                        title = "Transformasi Tata Kelola Kota Digital 2026",
                        author = "Sekota ThinkTank",
                        category = "Kebijakan Publik",
                        isbn = "978-602-000-1"
                    )
                ),
                onEdit = {},
                onDelete = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductRegistryEmptyPreview() {
    MaterialTheme {
        ProductRegistryContent(
            state = ProductRegistryState(),
            windowWidth = WindowWidth.Expanded,
            onIntent = {}
        )
    }
}
