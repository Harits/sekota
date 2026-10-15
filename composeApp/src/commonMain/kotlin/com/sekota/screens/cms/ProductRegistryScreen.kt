package com.sekota.screens.cms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
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
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
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
    // 0a. Modal Pemilihan Tipe Entitas Baru (Anti-Confusion Modal)
    if (state.activeModal is ActiveModalState.SelectCreateTypeModal) {
        AlertDialog(
            onDismissRequest = { onIntent(ProductRegistryIntent.DismissModal) },
            title = {
                Text(
                    text = "Pilih Jenis Item Katalog",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CmsInkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Silakan tentukan jenis entitas katalog yang ingin ditambahkan ke Sekota Platform:",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    // Option 1: Intelligence Suite Module
                    Surface(
                        onClick = {
                            onIntent(ProductRegistryIntent.OpenEditProductModal(AdminProduct(
                                id = "", code = "", name = "", categoryEyebrow = "Enterprise", description = "", features = emptyList()
                            )))
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF3E8FF),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    com.sekota.components.SekotaGearVectorIcon(size = 22.dp, tint = Color(0xFF9333EA))
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Intelligence Suite Module",
                                    fontFamily = getMontserratFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CmsInkNavy
                                )
                                Text(
                                    text = "Modul perangkat lunak analitik, platform intelijen, atau solusi SaaS kota cerdas.",
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Option 2: Merchandise Item
                    Surface(
                        onClick = {
                            onIntent(ProductRegistryIntent.OpenEditMerchModal(AdminMerch(
                                id = "", title = "", category = "Apparel", seriesName = "The Urban Collaborator", price = 0.0, rating = 0.0
                            )))
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFEF3C7),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    com.sekota.components.SekotaApparelVectorIcon(size = 22.dp, tint = Color(0xFFD97706))
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Merchandise",
                                    fontFamily = getMontserratFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CmsInkNavy
                                )
                                Text(
                                    text = "Produk apparel, aksesoris, atau cinderamata resmi Sekota Series.",
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Option 3: Buku / Publikasi (Info Guardrail)
                    Surface(
                        onClick = {
                            onIntent(ProductRegistryIntent.OpenBookInfoDialog)
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        color = Color(0xFFF0FDF4),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE0F2FE),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    com.sekota.components.BookEditionVectorIcon(size = 22.dp, tint = Color(0xFF0284C7))
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "Buku / Publikasi Riset",
                                        fontFamily = getMontserratFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CmsInkNavy
                                    )
                                    Surface(
                                        color = Color(0xFFE2E8F0),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Info",
                                            fontFamily = getDmSansFontFamily(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF475569),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Identitas & rilis buku dikelola via sistem telemetri (klik untuk detail info).",
                                    fontFamily = getDmSansFontFamily(),
                                    fontSize = 11.sp,
                                    color = Color(0xFF0369A1)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { onIntent(ProductRegistryIntent.DismissModal) }) {
                    Text("Tutup", fontFamily = getDmSansFontFamily(), color = Color.Gray)
                }
            }
        )
    }

    // 0b. Modal Penjelasan Buku Tidak Bisa Ditambah dari CMS (Telemetry Protocol Info)
    if (state.activeModal is ActiveModalState.BookInfoModal) {
        AlertDialog(
            onDismissRequest = { onIntent(ProductRegistryIntent.DismissModal) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    com.sekota.components.SekotaInfoVectorIcon(size = 20.dp, tint = CmsBrandTeal)
                    Text(
                        text = "Informasi Katalog Buku",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CmsInkNavy
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = Color(0xFFE0F2FE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Buku tidak dapat ditambahkan langsung secara manual dari form CMS ini.",
                            fontFamily = getDmSansFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF0369A1),
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Text(
                        text = "Sesuai Standar Protokol Integritas Telemetri Sekota (Rule 6):\n\n" +
                               "1. Identitas buku, ISBN, data pembaca terdaftar, dan rating bintang dihitung secara otomatis oleh Telemetry Engine.\n\n" +
                               "2. CMS Admin Workbench hanya berfungsi untuk memperkaya Web Metadata (Kategori, Sinopsis, Link PDF Reader, Estimasi Waktu Baca, Jumlah Halaman, dan Daftar Bab).\n\n" +
                               "3. Untuk mengedit metadata tampilan web buku yang sudah ada, gunakan tombol 'Edit Web Metadata' pada kartu buku di bawah.",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(ProductRegistryIntent.DismissModal) },
                    colors = ButtonDefaults.buttonColors(containerColor = CmsBrandTeal),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Saya Mengerti", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

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

                    // Bilingual Authoring Switcher & Translation Status
                    val isEnTab = modal.activeLanguageTab == "EN"
                    val hasEnContent = modal.categoryEn.isNotBlank() && modal.descriptionEn.isNotBlank()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language segmented pill switcher
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ID Tab Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (!isEnTab) CmsBrandTeal else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onIntent(ProductRegistryIntent.SwitchBookModalLanguage("ID")) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "\uD83C\uDDEE\uD83C\uDDE9 Bahasa Indonesia",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = if (!isEnTab) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (!isEnTab) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }

                            // EN Tab Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isEnTab) CmsBrandTeal else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onIntent(ProductRegistryIntent.SwitchBookModalLanguage("EN")) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "\uD83C\uDDEC\uD83C\uDDE7 English",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = if (isEnTab) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (isEnTab) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }
                        }

                        // Subtle Translation Status Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (hasEnContent) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                            border = BorderStroke(1.dp, if (hasEnContent) Color(0xFFA5D6A7) else Color(0xFFFFE082))
                        ) {
                            Text(
                                text = if (hasEnContent) "Status: Lengkap (ID + EN)" else "Status: EN Fallback",
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = if (hasEnContent) Color(0xFF2E7D32) else Color(0xFFB78103),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    if (!isEnTab) {
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
                    } else {
                        CmsInputField(
                            value = modal.categoryEn,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookCategoryEn(it)) },
                            label = "Kategori Buku (EN)",
                            placeholder = "e.g. SMART CITY, ESG, LAND EQUITY"
                        )

                        CmsInputField(
                            value = modal.descriptionEn,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateBookDescriptionEn(it)) },
                            label = "Sinopsis & Deskripsi (EN)",
                            placeholder = "(English synopsis - leave empty to fallback to ID)",
                            isSingleLine = false,
                            minLines = 3
                        )
                    }

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

                    // Bilingual Authoring Switcher & Translation Status
                    val isEnTab = modal.activeLanguageTab == "EN"
                    val hasEnContent = modal.categoryEyebrowEn.isNotBlank() || modal.descriptionEn.isNotBlank() || modal.featuresTextEn.isNotBlank()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language segmented pill switcher
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ID Tab Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (!isEnTab) CmsBrandTeal else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onIntent(ProductRegistryIntent.SwitchSuiteModalLanguage("ID")) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "\uD83C\uDDEE\uD83C\uDDE9 Bahasa Indonesia",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = if (!isEnTab) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (!isEnTab) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }

                            // EN Tab Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isEnTab) CmsBrandTeal else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onIntent(ProductRegistryIntent.SwitchSuiteModalLanguage("EN")) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "\uD83C\uDDEC\uD83C\uDDE7 English",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = if (isEnTab) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (isEnTab) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }
                        }

                        // Subtle Translation Status Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (hasEnContent) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                            border = BorderStroke(1.dp, if (hasEnContent) Color(0xFFA5D6A7) else Color(0xFFFFE082))
                        ) {
                            Text(
                                text = if (hasEnContent) "Status: Lengkap (ID + EN)" else "Status: EN Fallback",
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = if (hasEnContent) Color(0xFF2E7D32) else Color(0xFFB78103),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    if (!isEnTab) {
                        // ID Inputs
                        CmsInputField(
                            value = modal.categoryEyebrow,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductEyebrow(it)) },
                            label = "Category Eyebrow (ID)",
                            placeholder = "e.g. Enterprise, Government, Smart City"
                        )

                        CmsInputField(
                            value = modal.description,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductDescription(it)) },
                            label = "Deskripsi Solusi (ID)",
                            isSingleLine = false,
                            minLines = 3,
                            placeholder = "Deskripsi modul solusi dalam Bahasa Indonesia"
                        )

                        CmsInputField(
                            value = modal.featuresText,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductFeaturesText(it)) },
                            label = "Fitur Utama (ID) - Pisahkan dengan koma",
                            placeholder = "e.g. Analisis Waktu Nyata, Integrasi IoT, Prediksi AI"
                        )
                    } else {
                        // EN Inputs
                        CmsInputField(
                            value = modal.categoryEyebrowEn,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductEyebrowEn(it)) },
                            label = "Category Eyebrow (EN)",
                            placeholder = "(English translation - leave empty to fallback to ID)"
                        )

                        CmsInputField(
                            value = modal.descriptionEn,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductDescriptionEn(it)) },
                            label = "Solution Description (EN)",
                            isSingleLine = false,
                            minLines = 3,
                            placeholder = "(English translation - leave empty to fallback to ID)"
                        )

                        CmsInputField(
                            value = modal.featuresTextEn,
                            onValueChange = { onIntent(ProductRegistryIntent.UpdateProductFeaturesTextEn(it)) },
                            label = "Core Features (EN) - Comma separated",
                            placeholder = "(English translation - leave empty to fallback to ID)"
                        )
                    }

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
            actionLabel = when (state.selectedFilter) {
                ProductTypeFilter.ALL -> "+ Tambah Item"
                ProductTypeFilter.SUITE -> "+ Tambah Suite Module"
                ProductTypeFilter.MERCHANDISE -> "+ Tambah Merchandise"
                ProductTypeFilter.BOOKS -> "ℹ️ Info Katalog Buku"
            },
            onAction = {
                when (state.selectedFilter) {
                    ProductTypeFilter.BOOKS -> onIntent(ProductRegistryIntent.OpenBookInfoDialog)
                    else -> onIntent(ProductRegistryIntent.OpenCreateModal)
                }
            },
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

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductRegistrySuiteModalIdPreview() {
    MaterialTheme {
        ProductRegistryContent(
            state = ProductRegistryState(
                activeModal = ActiveModalState.SuiteProductModal(
                    editingProductId = "prod-01",
                    name = "Sekota CityPulse Analytics",
                    code = "PULSE",
                    categoryEyebrow = "Enterprise Platform",
                    description = "Platform analitik prediktif mobilitas perkotaan berbasis sensor spasial dan AI.",
                    featuresText = "Mobilitas Cerdas, Sensor Spasial, Prediksi Kemacetan",
                    activeLanguageTab = "ID",
                    categoryEyebrowEn = "Enterprise Platform",
                    descriptionEn = "Predictive urban mobility analytics platform powered by spatial sensors and AI.",
                    featuresTextEn = "Smart Mobility, Spatial Sensor, Traffic Forecast"
                )
            ),
            windowWidth = WindowWidth.Expanded,
            onIntent = {}
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductRegistrySuiteModalEnPreview() {
    MaterialTheme {
        ProductRegistryContent(
            state = ProductRegistryState(
                activeModal = ActiveModalState.SuiteProductModal(
                    editingProductId = "prod-01",
                    name = "Sekota CityPulse Analytics",
                    code = "PULSE",
                    categoryEyebrow = "Enterprise Platform",
                    description = "Platform analitik prediktif mobilitas perkotaan berbasis sensor spasial dan AI.",
                    featuresText = "Mobilitas Cerdas, Sensor Spasial, Prediksi Kemacetan",
                    activeLanguageTab = "EN",
                    categoryEyebrowEn = "Enterprise Platform",
                    descriptionEn = "Predictive urban mobility analytics platform powered by spatial sensors and AI.",
                    featuresTextEn = "Smart Mobility, Spatial Sensor, Traffic Forecast"
                )
            ),
            windowWidth = WindowWidth.Expanded,
            onIntent = {}
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ProductRegistryBookModalEnPreview() {
    MaterialTheme {
        ProductRegistryContent(
            state = ProductRegistryState(
                activeModal = ActiveModalState.BookMetadataModal(
                    targetBook = AdminBook(
                        id = "bk-01",
                        title = "Transformasi Tata Kelola Kota Digital 2026",
                        author = "Sekota ThinkTank",
                        category = "Kebijakan Publik",
                        isbn = "978-602-000-1",
                        categoryEn = "PUBLIC POLICY",
                        descriptionEn = "Strategic framework for digital city governance in 2026."
                    ),
                    category = "Kebijakan Publik",
                    description = "Kerangka strategis transformasi tata kelola kota digital 2026.",
                    pdfUrl = "https://sekota.id/docs/policy.pdf",
                    readingTime = "2H 30M",
                    pages = "180",
                    year = "2026",
                    language = "Indonesia & English",
                    publishedDate = "15 Januari 2026",
                    activeLanguageTab = "EN",
                    categoryEn = "PUBLIC POLICY",
                    descriptionEn = "Strategic framework for digital city governance in 2026."
                )
            ),
            windowWidth = WindowWidth.Expanded,
            onIntent = {}
        )
    }
}
