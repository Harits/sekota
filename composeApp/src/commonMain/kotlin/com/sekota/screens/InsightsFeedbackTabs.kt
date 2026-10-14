package com.sekota.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.syncService
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.components.cms.CmsActionHeader
import com.sekota.components.cms.CmsInputField
import com.sekota.components.cms.CmsStatusBanner
import com.sekota.components.cms.CmsStatusFeedback
import com.sekota.components.SekotaTrashVectorIcon
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.WindowWidth

@Composable
fun LiveMetricsTab(
    getLiveMetricsUseCase: GetAdminLiveMetricsUseCase,
    saveLiveMetricsUseCase: SaveAdminLiveMetricsUseCase,
    windowWidth: WindowWidth = WindowWidth.Expanded,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    var accuracy by remember { mutableStateOf("99.8%") }
    var totalClients by remember { mutableStateOf("100+") }
    var establishedYear by remember { mutableStateOf("2021") }
    var statusFeedback by remember { mutableStateOf<CmsStatusFeedback?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val metrics = getLiveMetricsUseCase()
        accuracy = metrics.dataAccuracy
        totalClients = metrics.totalClients
        establishedYear = metrics.establishedYear
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CmsActionHeader(
            title = "Live Metrics Editor",
            subtitle = "Konfigurasi angka metrik yang tampil langsung pada Hero & Trust section Landing Page",
            windowWidth = windowWidth
        )
        Spacer(modifier = Modifier.height(12.dp))

        CmsStatusBanner(feedback = statusFeedback)

        Spacer(modifier = Modifier.height(12.dp))
        
        // Rule 11: Adaptive wrapping for Live Metrics Workbench
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val saveMetric: (String, CmsStatusFeedback) -> Unit = { metricName, feedback ->
                val performSave = {
                    scope.launch {
                        val result = saveLiveMetricsUseCase(AdminLiveMetrics(accuracy.trim(), totalClients.trim(), establishedYear.trim()))
                        if (result.isSuccess) {
                            statusFeedback = feedback
                        } else {
                            statusFeedback = CmsStatusFeedback.error("Gagal memperbarui $metricName: ${result.exceptionOrNull()?.message}")
                        }
                    }
                }

                if (onActionWithProductionGuard != null) {
                    onActionWithProductionGuard("Update Live Metrics ($metricName)", "Landing Page Metrics", false) {
                        performSave()
                    }
                } else {
                    performSave()
                }
            }

            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Data Accuracy",
                value = accuracy,
                onValueChange = { accuracy = it; statusFeedback = null },
                onSave = { saveMetric("Data Accuracy", CmsStatusFeedback.success("Data Accuracy diperbarui ke '$accuracy'")) }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Total Clients",
                value = totalClients,
                onValueChange = { totalClients = it; statusFeedback = null },
                onSave = { saveMetric("Total Clients", CmsStatusFeedback.success("Total Clients diperbarui ke '$totalClients'")) }
            )
            MetricCardEditor(
                modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier,
                label = "Established",
                value = establishedYear,
                onValueChange = { establishedYear = it; statusFeedback = null },
                onSave = { saveMetric("Established Year", CmsStatusFeedback.success("Tahun Didirikan diperbarui ke '$establishedYear'")) }
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

@Composable
fun ClientInquiriesTab(
    getInquiriesUseCase: GetAdminInquiriesUseCase,
    deleteInquiryUseCase: DeleteAdminInquiryUseCase,
    getStrategicConversationUseCase: GetAdminStrategicConversationUseCase = remember { GetAdminStrategicConversationUseCase(AdminRepositoryImpl()) },
    saveStrategicConversationUseCase: SaveAdminStrategicConversationUseCase = remember { SaveAdminStrategicConversationUseCase(AdminRepositoryImpl()) },
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var inquiries by remember { mutableStateOf<List<ClientInquiry>>(emptyList()) }
    var config by remember { mutableStateOf(StrategicConversationConfig()) }
    var statusFeedback by remember { mutableStateOf<CmsStatusFeedback?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var activeSubTab by remember { mutableStateOf(0) } // 0: "Pengaturan Konten Formulir", 1: "Daftar Pesan Masuk (Leads)"

    LaunchedEffect(Unit) {
        inquiries = getInquiriesUseCase()
        config = getStrategicConversationUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            inquiries = getInquiriesUseCase()
            config = getStrategicConversationUseCase()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CmsActionHeader(
            title = "Contact & Inquiries",
            subtitle = "Kelola konten formulir percakapan strategis publik dan tinjau pesan konsultasi (leads) yang masuk.",
            windowWidth = windowWidth
        )

        // Sub-Tab Switcher (Segmented Button Pills)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = { activeSubTab = 0; statusFeedback = null },
                shape = RoundedCornerShape(100.dp),
                color = if (activeSubTab == 0) BrandTeal else Color.White,
                border = BorderStroke(1.dp, if (activeSubTab == 0) BrandTeal else Color(0xFFE2E8F0)),
                shadowElevation = if (activeSubTab == 0) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    com.sekota.components.SekotaCmsNavVectorIcon(
                        index = 1,
                        size = 16.dp,
                        tint = if (activeSubTab == 0) Color.White else InkNavy
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pengaturan Konten Formulir",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (activeSubTab == 0) Color.White else InkNavy
                    )
                }
            }

            Surface(
                onClick = { activeSubTab = 1; statusFeedback = null },
                shape = RoundedCornerShape(100.dp),
                color = if (activeSubTab == 1) BrandTeal else Color.White,
                border = BorderStroke(1.dp, if (activeSubTab == 1) BrandTeal else Color(0xFFE2E8F0)),
                shadowElevation = if (activeSubTab == 1) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    com.sekota.components.SekotaCmsNavVectorIcon(
                        index = 6,
                        size = 16.dp,
                        tint = if (activeSubTab == 1) Color.White else InkNavy
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daftar Pesan Masuk (${inquiries.size})",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (activeSubTab == 1) Color.White else InkNavy
                    )
                }
            }
        }

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(12.dp))
            CmsStatusBanner(feedback = it)
        }

        if (activeSubTab == 0) {
            // "Pengaturan Konten Formulir" View
            val formScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(formScrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Card 1: Informasi Kontak & Headline
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
                            text = "Informasi Kontak & Headline",
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = InkNavy
                        )

                        OutlinedTextField(
                            value = config.title,
                            onValueChange = {
                                config = config.copy(title = it)
                                statusFeedback = null
                            },
                            label = { Text("Judul Utama / Headline", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = config.subtitle,
                            onValueChange = {
                                config = config.copy(subtitle = it)
                                statusFeedback = null
                            },
                            label = { Text("Subjudul / SLA Respons", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = config.address,
                            onValueChange = {
                                config = config.copy(address = it)
                                statusFeedback = null
                            },
                            label = { Text("Alamat Kantor", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.email,
                                onValueChange = {
                                    config = config.copy(email = it)
                                    statusFeedback = null
                                },
                                label = { Text("Email Kontak Resmi", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.website,
                                onValueChange = {
                                    config = config.copy(website = it)
                                    statusFeedback = null
                                },
                                label = { Text("Website", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.email,
                                    onValueChange = {
                                        config = config.copy(email = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Email Kontak Resmi", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.website,
                                    onValueChange = {
                                        config = config.copy(website = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Website", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Card 2: Label & Placeholder Formulir Klien
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
                            text = "Konfigurasi Label & Placeholder Formulir",
                            fontFamily = getMontserratFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = InkNavy
                        )

                        // Nama
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.nameLabel,
                                onValueChange = {
                                    config = config.copy(nameLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Nama", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.namePlaceholder,
                                onValueChange = {
                                    config = config.copy(namePlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Nama", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.nameLabel,
                                    onValueChange = {
                                        config = config.copy(nameLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Nama", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.namePlaceholder,
                                    onValueChange = {
                                        config = config.copy(namePlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Nama", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Email
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.emailLabel,
                                onValueChange = {
                                    config = config.copy(emailLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Email", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.emailPlaceholder,
                                onValueChange = {
                                    config = config.copy(emailPlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Email", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.emailLabel,
                                    onValueChange = {
                                        config = config.copy(emailLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Email", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.emailPlaceholder,
                                    onValueChange = {
                                        config = config.copy(emailPlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Email", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Pesan
                        if (windowWidth.isCompact) {
                            OutlinedTextField(
                                value = config.messageLabel,
                                onValueChange = {
                                    config = config.copy(messageLabel = it)
                                    statusFeedback = null
                                },
                                label = { Text("Label Kolom Pesan", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = config.messagePlaceholder,
                                onValueChange = {
                                    config = config.copy(messagePlaceholder = it)
                                    statusFeedback = null
                                },
                                label = { Text("Placeholder Pesan", fontFamily = getDmSansFontFamily()) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = config.messageLabel,
                                    onValueChange = {
                                        config = config.copy(messageLabel = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Label Kolom Pesan", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = config.messagePlaceholder,
                                    onValueChange = {
                                        config = config.copy(messagePlaceholder = it)
                                        statusFeedback = null
                                    },
                                    label = { Text("Placeholder Pesan", fontFamily = getDmSansFontFamily()) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Tombol CTA
                        OutlinedTextField(
                            value = config.submitButtonLabel,
                            onValueChange = {
                                config = config.copy(submitButtonLabel = it)
                                statusFeedback = null
                            },
                            label = { Text("Label Tombol CTA Kirim", fontFamily = getDmSansFontFamily()) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val performSave = {
                                scope.launch {
                                    isSaving = true
                                    val result = saveStrategicConversationUseCase(config)
                                    isSaving = false
                                    if (result.isSuccess) {
                                        statusFeedback = CmsStatusFeedback.success("Konten formulir percakapan strategis berhasil disimpan dan disinkronkan!")
                                    } else {
                                        statusFeedback = CmsStatusFeedback.error("Gagal menyimpan: ${result.exceptionOrNull()?.message}")
                                    }
                                }
                            }

                            if (onActionWithProductionGuard != null) {
                                onActionWithProductionGuard("Simpan Pengaturan Formulir Klien", "Contact Form Configuration", false) {
                                    performSave()
                                }
                            } else {
                                performSave()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                        shape = RoundedCornerShape(100.dp),
                        enabled = !isSaving
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            com.sekota.components.SekotaSaveVectorIcon(size = 16.dp, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSaving) "Menyimpan..." else "Simpan Perubahan",
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            config = StrategicConversationConfig()
                            statusFeedback = CmsStatusFeedback.success("Formulir dikembalikan ke nilai default. Klik 'Simpan Perubahan' untuk menerapkan.")
                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = InkNavy)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            com.sekota.components.SekotaRefreshVectorIcon(size = 16.dp, tint = InkNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Reset ke Default",
                                fontFamily = getDmSansFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            // "Daftar Pesan Masuk (Leads)" View
            if (inquiries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    com.sekota.components.SekotaCmsNavVectorIcon(index = 6, size = 48.dp, tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada pesan konsultasi masuk.",
                        fontFamily = getDmSansFontFamily(),
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(inquiries, key = { it.id }) { inquiry ->
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
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = BrandTeal.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = inquiry.status.uppercase(),
                                            fontFamily = getDmSansFontFamily(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = BrandTeal,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = inquiry.timestamp,
                                        fontFamily = getDmSansFontFamily(),
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val performDelete = {
                                            scope.launch {
                                                val res = deleteInquiryUseCase(inquiry.id)
                                                if (res.isSuccess) {
                                                    inquiries = getInquiriesUseCase()
                                                    statusFeedback = CmsStatusFeedback.success("Pesan dari ${inquiry.name} berhasil dihapus")
                                                } else {
                                                    statusFeedback = CmsStatusFeedback.error("Gagal menghapus pesan: ${res.exceptionOrNull()?.message ?: ""}")
                                                }
                                            }
                                        }

                                        if (onActionWithProductionGuard != null) {
                                            onActionWithProductionGuard(
                                                "Hapus Pesan Konsultasi",
                                                "Inquiry dari ${inquiry.name} (${inquiry.email})",
                                                true
                                            ) {
                                                performDelete()
                                            }
                                        } else {
                                            performDelete()
                                        }
                                    }
                                ) {
                                    com.sekota.components.SekotaTrashVectorIcon(size = 18.dp, tint = Color(0xFFE53E3E))
                                }
                            }

                            Text(
                                text = inquiry.name,
                                fontFamily = getMontserratFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = InkNavy
                            )

                            Text(
                                text = inquiry.email,
                                fontFamily = getDmSansFontFamily(),
                                color = BrandTeal,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )

                            Surface(
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = inquiry.message,
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color(0xFF334155),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(16.dp)
                                 )
                             }
                         }
                     }
                 }
             }
         }
        }
    }
}


@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ClientInquiriesTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        ClientInquiriesTab(
            getInquiriesUseCase = remember { GetAdminInquiriesUseCase(repo) },
            deleteInquiryUseCase = remember { DeleteAdminInquiryUseCase(repo) },
            getStrategicConversationUseCase = remember { GetAdminStrategicConversationUseCase(repo) },
            saveStrategicConversationUseCase = remember { SaveAdminStrategicConversationUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}
