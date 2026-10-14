package com.sekota.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.syncService
import com.sekota.components.cms.CmsActionHeader
import com.sekota.components.cms.CmsInputField
import com.sekota.components.cms.CmsStatusBanner
import com.sekota.components.cms.CmsStatusFeedback
import com.sekota.components.SekotaTrashVectorIcon
import com.sekota.components.SekotaSaveVectorIcon
import com.sekota.components.SekotaRefreshVectorIcon
import com.sekota.components.SekotaPlusVectorIcon
import com.sekota.features.admin.domain.model.FooterColumnConfig
import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.FooterLinkItem
import com.sekota.features.admin.domain.usecase.*
import com.sekota.features.navigation.domain.model.NavigationItem
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.WindowWidth

@Composable
fun AppBarManagementTab(
    getNavigationItemsUseCase: GetAdminNavigationItemsUseCase,
    saveNavigationItemsUseCase: SaveAdminNavigationItemsUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf<List<com.sekota.features.navigation.domain.model.NavigationItem>>(emptyList()) }
    var statusFeedback by remember { mutableStateOf<CmsStatusFeedback?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    var editingItem by remember { mutableStateOf<com.sekota.features.navigation.domain.model.NavigationItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        items = getNavigationItemsUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            items = getNavigationItemsUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CmsActionHeader(
            title = "AppBar Navigation Management",
            subtitle = "Kelola struktur tautan navigasi utama (AppBar), visibilitas, rute, dan urutan tampilan secara dinamis.",
            windowWidth = windowWidth,
            actionLabel = "+ Tambah Item Navigasi",
            onAction = {
                val maxOrder = items.maxOfOrNull { it.order } ?: 0
                editingItem = com.sekota.features.navigation.domain.model.NavigationItem(
                    id = "nav_" + kotlin.random.Random.nextInt(1000, 9999),
                    label = "",
                    route = "#",
                    order = maxOrder + 1,
                    isVisible = true
                )
                showDialog = true
            }
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            CmsStatusBanner(feedback = it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reset to Canonical Action Card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reset ke Navigasi Bawaan (Canonical)",
                        fontFamily = getMontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = InkNavy
                    )
                    Text(
                        text = "Kembalikan 5 tautan standar: Solusi (#solusi), Produk (#produk), E-Book (/catalog), Merchandise (/merchandise), dan Kontak (#kontak).",
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {
                        val executeReset: () -> Unit = {
                            scope.launch {
                                isSaving = true
                                val canonical = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS
                                val res = saveNavigationItemsUseCase(canonical)
                                if (res.isSuccess) {
                                    items = res.getOrThrow()
                                    statusFeedback = CmsStatusFeedback.success("Berhasil mereset navigasi ke 5 tautan standar canonical!")
                                    syncService.triggerLocalUpdate()
                                } else {
                                    statusFeedback = CmsStatusFeedback.error("Gagal reset: ${res.exceptionOrNull()?.message}")
                                }
                                isSaving = false
                            }
                        }
                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Reset Navigasi Bawaan", "AppBar Navigation", false, executeReset)
                        } else {
                            executeReset()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandTeal)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SekotaRefreshVectorIcon(size = 16.dp, tint = BrandTeal)
                        Text(text = "Reset Bawaan", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Items List Table / Cards
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Daftar Tautan Aktif (${items.size} item)",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = InkNavy
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (items.isEmpty()) {
                    Text(
                        text = "Belum ada item navigasi yang tersimpan. Klik '+ Tambah Item Navigasi' atau 'Reset Bawaan'.",
                        fontFamily = getDmSansFontFamily(),
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    val sortedList = items.sortedBy { it.order }
                    sortedList.forEachIndexed { index, item ->
                        Surface(
                            color = if (item.isVisible) Color(0xFFF8FAFC) else Color(0xFFFFF1F2),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (item.isVisible) Color(0xFFE2E8F0) else Color(0xFFFECDD3)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        color = BrandTeal.copy(alpha = 0.15f),
                                        shape = CircleShape,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${item.order}",
                                                fontFamily = getDmSansFontFamily(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = BrandTeal
                                            )
                                        }
                                    }

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                text = item.label,
                                                fontFamily = getMontserratFontFamily(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = InkNavy
                                            )
                                            if (!item.isVisible) {
                                                Surface(
                                                    color = Color(0xFFF43F5E),
                                                    shape = RoundedCornerShape(4.dp),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "HIDDEN",
                                                        fontFamily = getDmSansFontFamily(),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "Route: ${item.route}",
                                            fontFamily = getDmSansFontFamily(),
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Move Up / Down
                                    IconButton(
                                        onClick = {
                                            if (index > 0) {
                                                val prevItem = sortedList[index - 1]
                                                val updatedList = sortedList.toMutableList()
                                                val currentOrder = item.order
                                                updatedList[index] = item.copy(order = prevItem.order)
                                                updatedList[index - 1] = prevItem.copy(order = currentOrder)
                                                val executeMove: () -> Unit = {
                                                    scope.launch {
                                                        val res = saveNavigationItemsUseCase(updatedList)
                                                        if (res.isSuccess) {
                                                            items = res.getOrThrow()
                                                            syncService.triggerLocalUpdate()
                                                        }
                                                    }
                                                }
                                                if (onActionWithProductionGuard != null) {
                                                    onActionWithProductionGuard("Pindahkan Item Navigasi Ke Atas", item.label, false, executeMove)
                                                } else {
                                                    executeMove()
                                                }
                                            }
                                        },
                                        enabled = index > 0,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaArrowUpVectorIcon(
                                            size = 18.dp,
                                            tint = if (index > 0) InkNavy else Color.LightGray
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (index < sortedList.size - 1) {
                                                val nextItem = sortedList[index + 1]
                                                val updatedList = sortedList.toMutableList()
                                                val currentOrder = item.order
                                                updatedList[index] = item.copy(order = nextItem.order)
                                                updatedList[index + 1] = nextItem.copy(order = currentOrder)
                                                val executeMove: () -> Unit = {
                                                    scope.launch {
                                                        val res = saveNavigationItemsUseCase(updatedList)
                                                        if (res.isSuccess) {
                                                            items = res.getOrThrow()
                                                            syncService.triggerLocalUpdate()
                                                        }
                                                    }
                                                }
                                                if (onActionWithProductionGuard != null) {
                                                    onActionWithProductionGuard("Pindahkan Item Navigasi Ke Bawah", item.label, false, executeMove)
                                                } else {
                                                    executeMove()
                                                }
                                            }
                                        },
                                        enabled = index < sortedList.size - 1,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaArrowDownVectorIcon(
                                            size = 18.dp,
                                            tint = if (index < sortedList.size - 1) InkNavy else Color.LightGray
                                        )
                                    }

                                    // Quick Toggle Visibility
                                    IconButton(
                                        onClick = {
                                            val updated = items.map {
                                                if (it.id == item.id) it.copy(isVisible = !it.isVisible) else it
                                            }
                                            val executeToggle: () -> Unit = {
                                                scope.launch {
                                                    val res = saveNavigationItemsUseCase(updated)
                                                    if (res.isSuccess) {
                                                        items = res.getOrThrow()
                                                        val stateText = if (!item.isVisible) "ditampilkan" else "disembunyikan"
                                                        statusFeedback = CmsStatusFeedback.success("Item '${item.label}' berhasil $stateText pada AppBar.")
                                                        syncService.triggerLocalUpdate()
                                                    }
                                                }
                                            }
                                            if (onActionWithProductionGuard != null) {
                                                onActionWithProductionGuard("Ubah Visibilitas Navigasi", item.label, false, executeToggle)
                                            } else {
                                                executeToggle()
                                            }
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        if (item.isVisible) {
                                            com.sekota.components.SekotaEyeVectorIcon(size = 18.dp, tint = InkNavy)
                                        } else {
                                            com.sekota.components.SekotaEyeOffVectorIcon(size = 18.dp, tint = Color(0xFFF43F5E))
                                        }
                                    }

                                    // Edit
                                    OutlinedButton(
                                        onClick = {
                                            editingItem = item
                                            showDialog = true
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text(text = "Edit", fontSize = 12.sp, fontFamily = getDmSansFontFamily())
                                    }

                                    // Delete
                                    IconButton(
                                        onClick = {
                                            val executeDelete: () -> Unit = {
                                                scope.launch {
                                                    val updated = items.filterNot { it.id == item.id }
                                                    val res = saveNavigationItemsUseCase(updated)
                                                    if (res.isSuccess) {
                                                        items = res.getOrThrow()
                                                        statusFeedback = CmsStatusFeedback.success("Item '${item.label}' berhasil dihapus.")
                                                        syncService.triggerLocalUpdate()
                                                    } else {
                                                        statusFeedback = CmsStatusFeedback.error("Gagal hapus: ${res.exceptionOrNull()?.message}")
                                                    }
                                                }
                                            }
                                            if (onActionWithProductionGuard != null) {
                                                onActionWithProductionGuard("Hapus Item Navigasi", item.label, true, executeDelete)
                                            } else {
                                                executeDelete()
                                            }
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        com.sekota.components.SekotaTrashVectorIcon(size = 18.dp, tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for adding / editing navigation item
    if (showDialog && editingItem != null) {
        val currentItem = editingItem!!
        var labelInput by remember { mutableStateOf(currentItem.label) }
        var routeInput by remember { mutableStateOf(currentItem.route) }
        var orderInput by remember { mutableStateOf(currentItem.order.toString()) }
        var isVisibleInput by remember { mutableStateOf(currentItem.isVisible) }
        var dialogError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingItem = null
            },
            title = {
                Text(
                    text = if (items.any { it.id == currentItem.id }) "Edit Item Navigasi" else "Tambah Item Navigasi",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dialogError?.let {
                        Text(
                            text = it,
                            fontFamily = getDmSansFontFamily(),
                            color = Color(0xFFEF4444),
                            fontSize = 13.sp
                        )
                    }

                    OutlinedTextField(
                        value = labelInput,
                        onValueChange = { labelInput = it },
                        label = { Text("Label Navigasi") },
                        placeholder = { Text("misal: Solusi, E-Book, Insights") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = routeInput,
                        onValueChange = { routeInput = it },
                        label = { Text("Route / Hash Target") },
                        placeholder = { Text("misal: #solusi, /catalog, #kontak") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = orderInput,
                        onValueChange = { orderInput = it },
                        label = { Text("Urutan Tampilan (Order)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable { isVisibleInput = !isVisibleInput }
                    ) {
                        Checkbox(
                            checked = isVisibleInput,
                            onCheckedChange = { isVisibleInput = it }
                        )
                        Text(
                            text = "Tampilkan di AppBar (Visible)",
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 14.sp,
                            color = InkNavy
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (labelInput.isBlank()) {
                            dialogError = "Label tidak boleh kosong"
                            return@Button
                        }
                        if (routeInput.isBlank()) {
                            dialogError = "Route tidak boleh kosong"
                            return@Button
                        }
                        val parsedOrder = orderInput.toIntOrNull() ?: currentItem.order

                        val updatedItem = currentItem.copy(
                            label = labelInput.trim(),
                            route = routeInput.trim(),
                            order = parsedOrder,
                            isVisible = isVisibleInput
                        )

                        val executeSave: () -> Unit = {
                            scope.launch {
                                val currentList = items.filterNot { it.id == updatedItem.id }.toMutableList()
                                currentList.add(updatedItem)
                                val res = saveNavigationItemsUseCase(currentList)
                                if (res.isSuccess) {
                                    items = res.getOrThrow()
                                    statusFeedback = CmsStatusFeedback.success("Item '${updatedItem.label}' berhasil disimpan!")
                                    showDialog = false
                                    editingItem = null
                                    syncService.triggerLocalUpdate()
                                } else {
                                    dialogError = res.exceptionOrNull()?.message
                                }
                            }
                        }

                        if (onActionWithProductionGuard != null) {
                            onActionWithProductionGuard("Simpan Item Navigasi", updatedItem.label, false, executeSave)
                        } else {
                            executeSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text(text = "Simpan", fontFamily = getDmSansFontFamily(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        editingItem = null
                    }
                ) {
                    Text(text = "Batal", fontFamily = getDmSansFontFamily())
                }
            }
        )
    }
}

@Composable
fun FooterManagementTab(
    getFooterUseCase: GetAdminFooterUseCase,
    saveFooterUseCase: SaveAdminFooterUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(com.sekota.features.admin.domain.model.FooterConfig()) }
    var statusFeedback by remember { mutableStateOf<CmsStatusFeedback?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getFooterUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getFooterUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CmsActionHeader(
            title = "Footer Content Management",
            subtitle = "Kelola tagline, afiliasi, hak cipta, tautan media sosial, serta kolom tautan Produk, Perusahaan, dan Legal secara dinamis.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            CmsStatusBanner(feedback = it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card 1: Branding, Tagline, & Afiliasi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Branding, Tagline & Afiliasi",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.tagline,
                    onValueChange = { config = config.copy(tagline = it); statusFeedback = null },
                    label = { Text("Tagline Sekota", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = config.affiliationText,
                        onValueChange = { config = config.copy(affiliationText = it); statusFeedback = null },
                        label = { Text("Teks Afiliasi", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = config.affiliationSubtext,
                        onValueChange = { config = config.copy(affiliationSubtext = it); statusFeedback = null },
                        label = { Text("Subteks Afiliasi", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = config.copyrightText,
                    onValueChange = { config = config.copy(copyrightText = it); statusFeedback = null },
                    label = { Text("Teks Hak Cipta (Copyright)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card 2: Kolom Produk
        FooterColumnEditorCard(
            columnTitle = "Kolom Produk",
            columnConfig = config.productColumn,
            onColumnChange = { updated ->
                config = config.copy(productColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 3: Kolom Perusahaan
        FooterColumnEditorCard(
            columnTitle = "Kolom Perusahaan",
            columnConfig = config.companyColumn,
            onColumnChange = { updated ->
                config = config.copy(companyColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 4: Kolom Legal
        FooterColumnEditorCard(
            columnTitle = "Kolom Legal",
            columnConfig = config.legalColumn,
            onColumnChange = { updated ->
                config = config.copy(legalColumn = updated)
                statusFeedback = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card 5: Media Sosial
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Tautan Media Sosial",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                config.socialLinks.forEachIndexed { sIdx, social ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = social.platform,
                            onValueChange = { newPlat ->
                                val list = config.socialLinks.toMutableList()
                                list[sIdx] = social.copy(platform = newPlat)
                                config = config.copy(socialLinks = list)
                                statusFeedback = null
                            },
                            label = { Text("Platform", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.width(140.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = social.url,
                            onValueChange = { newUrl ->
                                val list = config.socialLinks.toMutableList()
                                list[sIdx] = social.copy(url = newUrl)
                                config = config.copy(socialLinks = list)
                                statusFeedback = null
                            },
                            label = { Text("URL Tautan Eksternal", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = social.isVisible,
                                onCheckedChange = { checked ->
                                    val list = config.socialLinks.toMutableList()
                                    list[sIdx] = social.copy(isVisible = checked)
                                    config = config.copy(socialLinks = list)
                                    statusFeedback = null
                                }
                            )
                            Text("Tampil", fontFamily = getDmSansFontFamily(), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (config.tagline.isBlank()) {
                        statusFeedback = CmsStatusFeedback.error("Tagline tidak boleh kosong")
                        return@Button
                    }
                    if (config.copyrightText.isBlank()) {
                        statusFeedback = CmsStatusFeedback.error("Teks hak cipta tidak boleh kosong")
                        return@Button
                    }
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveFooterUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = CmsStatusFeedback.success("Konten Footer berhasil diperbarui dan disinkronkan ke Web!")
                            } else {
                                statusFeedback = CmsStatusFeedback.error("Gagal memperbarui: ${result.exceptionOrNull()?.message}")
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Konten Footer", "Footer Global Web", false) {
                            performSave()
                        }
                    } else {
                        performSave()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp),
                enabled = !isSaving,
                modifier = Modifier.height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SekotaSaveVectorIcon(size = 18.dp, tint = Color.White)
                    Text(
                        text = if (isSaving) "Menyimpan..." else "Simpan Perubahan Footer",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    config = com.sekota.features.admin.domain.model.FooterConfig()
                    statusFeedback = CmsStatusFeedback.success("Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)")
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SekotaRefreshVectorIcon(size = 18.dp, tint = InkNavy)
                    Text(
                        text = "Reset Bawaan",
                        fontFamily = getDmSansFontFamily(),
                        color = InkNavy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FooterColumnEditorCard(
    columnTitle: String,
    columnConfig: com.sekota.features.admin.domain.model.FooterColumnConfig,
    onColumnChange: (com.sekota.features.admin.domain.model.FooterColumnConfig) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = columnTitle,
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )
                Button(
                    onClick = {
                        val nextIdx = columnConfig.items.size + 1
                        val newItem = com.sekota.features.admin.domain.model.FooterLinkItem(
                            id = "item_${kotlin.random.Random.nextInt(1000, 9999)}",
                            label = "Menu Baru #$nextIdx",
                            urlOrCode = "",
                            sortOrder = nextIdx
                        )
                        onColumnChange(columnConfig.copy(items = columnConfig.items + newItem))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SekotaPlusVectorIcon(size = 14.dp, tint = Color.White)
                        Text("Tambah Tautan", fontFamily = getDmSansFontFamily(), fontSize = 12.sp, color = Color.White)
                    }
                }
            }

            OutlinedTextField(
                value = columnConfig.title,
                onValueChange = { onColumnChange(columnConfig.copy(title = it)) },
                label = { Text("Judul Kolom", fontFamily = getDmSansFontFamily()) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            columnConfig.items.forEachIndexed { index, item ->
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = item.label,
                            onValueChange = { newLabel ->
                                val list = columnConfig.items.toMutableList()
                                list[index] = item.copy(label = newLabel)
                                onColumnChange(columnConfig.copy(items = list))
                            },
                            label = { Text("Label", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                            modifier = Modifier.weight(1.2f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = item.urlOrCode,
                            onValueChange = { newTarget ->
                                val list = columnConfig.items.toMutableList()
                                list[index] = item.copy(urlOrCode = newTarget)
                                onColumnChange(columnConfig.copy(items = list))
                            },
                            label = { Text("Target / Kode / URL", fontFamily = getDmSansFontFamily(), fontSize = 11.sp) },
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = item.isVisible,
                                onCheckedChange = { checked ->
                                    val list = columnConfig.items.toMutableList()
                                    list[index] = item.copy(isVisible = checked)
                                    onColumnChange(columnConfig.copy(items = list))
                                }
                            )
                            Text("Aktif", fontFamily = getDmSansFontFamily(), fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = {
                                val list = columnConfig.items.toMutableList()
                                list.removeAt(index)
                                onColumnChange(columnConfig.copy(items = list))
                            }
                        ) {
                            com.sekota.components.SekotaTrashVectorIcon(size = 18.dp)
                        }
                    }
                }
            }
        }
    }
}


@Preview(device = DESKTOP, showBackground = true)
@Composable
fun AppBarManagementTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        AppBarManagementTab(
            getNavigationItemsUseCase = remember { GetAdminNavigationItemsUseCase(repo) },
            saveNavigationItemsUseCase = remember { SaveAdminNavigationItemsUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun FooterManagementTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        FooterManagementTab(
            getFooterUseCase = remember { GetAdminFooterUseCase(repo) },
            saveFooterUseCase = remember { SaveAdminFooterUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}
