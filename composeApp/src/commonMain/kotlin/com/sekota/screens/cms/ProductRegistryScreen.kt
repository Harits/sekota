package com.sekota.screens.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
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
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.AdminWhatsInsideItem
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.syncService
import com.sekota.ui.WindowWidth
import com.sekota.utils.decodeBase64ToBitmap
import kotlinx.coroutines.launch

/**
 * Product Registry: Unified Catalog Workspace consolidating Books, Intelligence Suite, and Merchandise.
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

    var books by remember { mutableStateOf<List<AdminBook>>(emptyList()) }
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }
    var merchList by remember { mutableStateOf<List<AdminMerch>>(emptyList()) }

    var selectedFilter by remember { mutableStateOf(ProductTypeFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state for Book Web Metadata
    var showBookDialog by remember { mutableStateOf(false) }
    var targetBook by remember { mutableStateOf<AdminBook?>(null) }
    var bookCategory by remember { mutableStateOf("") }
    var bookDescription by remember { mutableStateOf("") }
    var bookPdfUrl by remember { mutableStateOf("") }
    var bookReadingTime by remember { mutableStateOf("") }
    var bookPages by remember { mutableStateOf("") }
    var bookYear by remember { mutableStateOf("") }
    var bookLanguage by remember { mutableStateOf("") }
    var bookPublishedDate by remember { mutableStateOf("") }
    var bookWhatsInside by remember { mutableStateOf<List<AdminWhatsInsideItem>>(emptyList()) }
    var bookError by remember { mutableStateOf<String?>(null) }

    // Dialog state for Product (Suite)
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProductId by remember { mutableStateOf<String?>(null) }
    var productName by remember { mutableStateOf("") }
    var productCode by remember { mutableStateOf("") }
    var productEyebrow by remember { mutableStateOf("Enterprise") }
    var productDesc by remember { mutableStateOf("") }
    var productCoverImage by remember { mutableStateOf("") }
    var productFeaturesText by remember { mutableStateOf("") }
    var productError by remember { mutableStateOf<String?>(null) }

    // Dialog state for Merchandise
    var showMerchDialog by remember { mutableStateOf(false) }
    var editingMerchId by remember { mutableStateOf<String?>(null) }
    var merchTitle by remember { mutableStateOf("") }
    var merchCategory by remember { mutableStateOf("Apparel") }
    var merchSeriesName by remember { mutableStateOf("The Urban Collaborator") }
    var merchPrice by remember { mutableStateOf("") }
    var merchImageUrl by remember { mutableStateOf("") }
    var merchError by remember { mutableStateOf<String?>(null) }

    fun refreshData() {
        scope.launch {
            books = getBooksUseCase()
            categories = getCategoriesUseCase()
            products = getProductsUseCase()
            merchList = getMerchUseCase()
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    val registryItems = remember(books, products, merchList, selectedFilter, searchQuery) {
        val list = mutableListOf<RegistryItem>()
        if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.BOOKS) {
            list.addAll(books.map { RegistryItem.Book(it) })
        }
        if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.SUITE) {
            list.addAll(products.map { RegistryItem.SuiteProduct(it) })
        }
        if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.MERCHANDISE) {
            list.addAll(merchList.map { RegistryItem.Merch(it) })
        }
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list.filter {
                it.title.lowercase().contains(q) ||
                        it.subtitle.lowercase().contains(q) ||
                        it.category.lowercase().contains(q)
            }
        } else {
            list
        }
    }

    // Modal Book Web Metadata Editor
    if (showBookDialog && targetBook != null) {
        val b = targetBook!!
        AlertDialog(
            onDismissRequest = { showBookDialog = false; targetBook = null },
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
                        text = b.title,
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
                    bookError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = bookCategory,
                        onValueChange = { bookCategory = it; bookError = null },
                        label = "Kategori Buku"
                    )

                    CmsInputField(
                        value = bookDescription,
                        onValueChange = { bookDescription = it },
                        label = "Sinopsis & Deskripsi",
                        isSingleLine = false,
                        minLines = 3
                    )

                    CmsInputField(
                        value = bookPdfUrl,
                        onValueChange = { bookPdfUrl = it },
                        label = "PDF Reader URL"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmsInputField(
                            value = bookReadingTime,
                            onValueChange = { bookReadingTime = it },
                            label = "Estimasi Baca",
                            modifier = Modifier.weight(1f)
                        )
                        CmsInputField(
                            value = bookPages,
                            onValueChange = { bookPages = it },
                            label = "Jumlah Hal",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val performSave = {
                            val updated = b.copy(
                                category = bookCategory.trim(),
                                description = bookDescription.trim(),
                                pdfUrl = bookPdfUrl.trim().ifBlank { null },
                                readingTime = bookReadingTime.trim(),
                                pages = bookPages.trim().toIntOrNull() ?: b.pages,
                                year = bookYear.trim(),
                                language = bookLanguage.trim(),
                                publishedDate = bookPublishedDate.trim(),
                                whatsInside = bookWhatsInside
                            )
                            scope.launch {
                                saveBookUseCase(updated)
                                syncService.triggerLocalUpdate()
                                refreshData()
                                showBookDialog = false
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Simpan Metadata Web Buku", b.title, false) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookDialog = false }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // Modal Product Suite Editor
    if (showProductDialog) {
        AlertDialog(
            onDismissRequest = { showProductDialog = false },
            title = {
                Text(
                    text = if (editingProductId == null) "Tambah Intelligence Module" else "Edit Intelligence Module",
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
                    productError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = productName,
                        onValueChange = { productName = it; productError = null },
                        label = "Nama Produk"
                    )

                    CmsInputField(
                        value = productCode,
                        onValueChange = { productCode = it; productError = null },
                        label = "Kode Produk"
                    )

                    CmsInputField(
                        value = productEyebrow,
                        onValueChange = { productEyebrow = it },
                        label = "Category Eyebrow"
                    )

                    CmsInputField(
                        value = productDesc,
                        onValueChange = { productDesc = it },
                        label = "Deskripsi Solusi",
                        isSingleLine = false,
                        minLines = 3
                    )

                    CmsInputField(
                        value = productFeaturesText,
                        onValueChange = { productFeaturesText = it },
                        label = "Fitur Utama (Pisahkan dengan koma)",
                        placeholder = "e.g. Analytics, AI Forecast, Edge IoT"
                    )

                    CmsImageUploader(
                        imageBase64 = productCoverImage,
                        onImageSelected = { productCoverImage = it },
                        label = "Icon / Cover Modul Intelligence"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (productName.isBlank() || productCode.isBlank()) {
                            productError = "Nama dan Kode produk tidak boleh kosong"
                            return@Button
                        }
                        val performSave = {
                            val id = editingProductId ?: "p-${kotlin.random.Random.nextInt(1000, 9999)}"
                            val feats = productFeaturesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            val prod = AdminProduct(
                                id = id,
                                code = productCode.trim().uppercase(),
                                name = productName.trim(),
                                categoryEyebrow = productEyebrow.trim(),
                                description = productDesc.trim(),
                                features = feats,
                                iconUrlOrBase64 = productCoverImage.ifBlank { null }
                            )
                            scope.launch {
                                saveProductUseCase(prod)
                                syncService.triggerLocalUpdate()
                                refreshData()
                                showProductDialog = false
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard(
                                if (editingProductId == null) "Tambah Produk" else "Edit Produk",
                                productName.trim(),
                                false
                            ) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProductDialog = false }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // Modal Merchandise Editor
    if (showMerchDialog) {
        AlertDialog(
            onDismissRequest = { showMerchDialog = false },
            title = {
                Text(
                    text = if (editingMerchId == null) "Tambah Merchandise" else "Edit Merchandise",
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
                    merchError?.let {
                        Text(it, color = Color(0xFFE53E3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    CmsInputField(
                        value = merchTitle,
                        onValueChange = { merchTitle = it; merchError = null },
                        label = "Nama Item"
                    )

                    CmsInputField(
                        value = merchCategory,
                        onValueChange = { merchCategory = it },
                        label = "Kategori Merchandise"
                    )

                    CmsInputField(
                        value = merchSeriesName,
                        onValueChange = { merchSeriesName = it },
                        label = "Nama Seri / Koleksi"
                    )

                    CmsInputField(
                        value = merchPrice,
                        onValueChange = { merchPrice = it; merchError = null },
                        label = "Harga ($ USD)"
                    )

                    CmsImageUploader(
                        imageBase64 = merchImageUrl,
                        onImageSelected = { merchImageUrl = it },
                        label = "Foto Merchandise"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = merchPrice.toDoubleOrNull()
                        if (merchTitle.isBlank()) {
                            merchError = "Nama item tidak boleh kosong"
                            return@Button
                        }
                        if (price == null || price <= 0.0) {
                            merchError = "Harga harus berupa angka valid lebih dari 0"
                            return@Button
                        }
                        val performSave = {
                            val id = editingMerchId ?: "m-${kotlin.random.Random.nextInt(1000, 9999)}"
                            val m = AdminMerch(
                                id = id,
                                title = merchTitle.trim(),
                                category = merchCategory.trim(),
                                seriesName = merchSeriesName.trim(),
                                price = price,
                                rating = 5.0,
                                imageUrl = merchImageUrl.ifBlank { null }
                            )
                            scope.launch {
                                saveMerchUseCase(m)
                                syncService.triggerLocalUpdate()
                                refreshData()
                                showMerchDialog = false
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard(
                                if (editingMerchId == null) "Tambah Merchandise" else "Edit Merchandise",
                                merchTitle.trim(),
                                false
                            ) {
                                performSave()
                            }
                        } else {
                            performSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Simpan Live", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMerchDialog = false }) {
                    Text("Batal", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // Main Unified Registry View
    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Product Registry",
            subtitle = "Repositori terpadu katalog buku (${books.size}), intelligence suite (${products.size}), dan merchandise (${merchList.size}).",
            windowWidth = windowWidth,
            actionLabel = "Tambah Item",
            onAction = {
                when (selectedFilter) {
                    ProductTypeFilter.ALL, ProductTypeFilter.SUITE -> {
                        editingProductId = null
                        productName = ""
                        productCode = ""
                        productEyebrow = "Enterprise"
                        productDesc = ""
                        productCoverImage = ""
                        productFeaturesText = ""
                        showProductDialog = true
                    }
                    ProductTypeFilter.MERCHANDISE -> {
                        editingMerchId = null
                        merchTitle = ""
                        merchCategory = "Apparel"
                        merchSeriesName = "The Urban Collaborator"
                        merchPrice = ""
                        merchImageUrl = ""
                        showMerchDialog = true
                    }
                    ProductTypeFilter.BOOKS -> {
                        // Invariant: Book identity is managed by telemetry/bookinteractiontool
                    }
                }
            },
            secondaryLabel = "Refresh Data",
            onSecondaryAction = { refreshData() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Responsive Filter Bar & Search Box (Rule 11: Zero Collision & Adaptive Layout)
        if (windowWidth.isCompact) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari produk, buku, atau kategori...", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ProductTypeFilter.values().forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
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
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
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
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari produk atau buku...", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.widthIn(max = 260.dp).height(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unified Adaptive Grid
        if (registryItems.isEmpty()) {
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
                columns = GridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(registryItems, key = { it.id }) { item ->
                    RegistryCardItem(
                        item = item,
                        onEdit = {
                            when (item) {
                                is RegistryItem.Book -> {
                                    targetBook = item.book
                                    bookCategory = item.book.category
                                    bookDescription = item.book.description
                                    bookPdfUrl = item.book.pdfUrl ?: ""
                                    bookReadingTime = item.book.readingTime
                                    bookPages = item.book.pages.toString()
                                    bookYear = item.book.year
                                    bookLanguage = item.book.language
                                    bookPublishedDate = item.book.publishedDate
                                    bookWhatsInside = item.book.whatsInside
                                    showBookDialog = true
                                }
                                is RegistryItem.SuiteProduct -> {
                                    editingProductId = item.product.id
                                    productName = item.product.name
                                    productCode = item.product.code
                                    productEyebrow = item.product.categoryEyebrow
                                    productDesc = item.product.description
                                    productCoverImage = item.product.iconUrlOrBase64 ?: ""
                                    productFeaturesText = item.product.features.joinToString(", ")
                                    showProductDialog = true
                                }
                                is RegistryItem.Merch -> {
                                    editingMerchId = item.merch.id
                                    merchTitle = item.merch.title
                                    merchCategory = item.merch.category
                                    merchSeriesName = item.merch.seriesName
                                    merchPrice = item.merch.price.toString()
                                    merchImageUrl = item.merch.imageUrl ?: ""
                                    showMerchDialog = true
                                }
                            }
                        },
                        onDelete = {
                            when (item) {
                                is RegistryItem.Book -> {
                                    // Read-only invariant: Book identity cannot be deleted manually by CMS operator
                                }
                                is RegistryItem.SuiteProduct -> {
                                    val performDelete = {
                                        scope.launch {
                                            deleteProductUseCase(item.product.id)
                                            syncService.triggerLocalUpdate()
                                            refreshData()
                                        }
                                    }
                                    if (onActionWithProductionGuard != null) {
                                        onActionWithProductionGuard("Hapus Modul Suite", item.product.name, true) {
                                            performDelete()
                                        }
                                    } else {
                                        performDelete()
                                    }
                                }
                                is RegistryItem.Merch -> {
                                    val performDelete = {
                                        scope.launch {
                                            deleteMerchUseCase(item.merch.id)
                                            syncService.triggerLocalUpdate()
                                            refreshData()
                                        }
                                    }
                                    if (onActionWithProductionGuard != null) {
                                        onActionWithProductionGuard("Hapus Merchandise", item.merch.title, true) {
                                            performDelete()
                                        }
                                    } else {
                                        performDelete()
                                    }
                                }
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
                    }
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
                    color = Color.Gray
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
