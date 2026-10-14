package com.sekota.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import sekota.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import com.sekota.components.cms.CmsActionHeader
import com.sekota.components.cms.CmsInputField
import com.sekota.components.cms.CmsImageUploader
import com.sekota.features.admin.domain.model.ValueLoopConfig
import com.sekota.features.admin.domain.model.AdminValueLoopStep
import com.sekota.features.admin.domain.model.WhySekotaConfig
import com.sekota.features.admin.domain.model.WhySekotaCard
import com.sekota.features.admin.domain.usecase.*
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily
import com.sekota.ui.WindowWidth
import com.sekota.utils.decodeBase64ToBitmap

@Composable
fun ValueLoopTab(
    getValueLoopUseCase: GetAdminValueLoopUseCase,
    saveValueLoopUseCase: SaveAdminValueLoopUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(com.sekota.features.admin.domain.model.ValueLoopConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getValueLoopUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getValueLoopUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CmsActionHeader(
            title = "Strategic Value Loop",
            subtitle = "Kelola judul, deskripsi, dan tahapan siklus tertutup 01-05 Solusi Kami di portal publik Sekota.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Header Editor Card
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
                    text = "Pengaturan Header Section",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.sectionEyebrow,
                    onValueChange = {
                        config = config.copy(sectionEyebrow = it)
                        statusFeedback = null
                    },
                    label = { Text("Eyebrow (Label Kecil Atas)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = config.title,
                    onValueChange = {
                        config = config.copy(title = it)
                        statusFeedback = null
                    },
                    label = { Text("Judul Utama Section", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = config.description,
                    onValueChange = {
                        config = config.copy(description = it)
                        statusFeedback = null
                    },
                    label = { Text("Deskripsi / Subjudul", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Steps Editor Section
        Text(
            text = "Langkah-Langkah Siklus (01 - 05)",
            fontFamily = getMontserratFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = InkNavy
        )
        Spacer(modifier = Modifier.height(12.dp))

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            config.steps.forEachIndexed { index, step ->
                Card(
                    modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier.widthIn(min = 280.dp, max = 340.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = BrandTeal,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = step.stepNumber,
                                    fontFamily = getDmSansFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Langkah ${index + 1}",
                                fontFamily = getMontserratFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = InkNavy
                            )
                        }

                        OutlinedTextField(
                            value = step.title,
                            onValueChange = { newTitle ->
                                val updatedSteps = config.steps.toMutableList()
                                updatedSteps[index] = step.copy(title = newTitle)
                                config = config.copy(steps = updatedSteps)
                                statusFeedback = null
                            },
                            label = { Text("Judul Tahap (e.g. INTENT)", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = step.subtitle,
                            onValueChange = { newSubtitle ->
                                val updatedSteps = config.steps.toMutableList()
                                updatedSteps[index] = step.copy(subtitle = newSubtitle)
                                config = config.copy(steps = updatedSteps)
                                statusFeedback = null
                            },
                            label = { Text("Subjudul Tahap (e.g. Definisi Strategis)", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
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
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveValueLoopUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = "✅ Strategic Value Loop berhasil diperbarui dan disinkronkan!"
                            } else {
                                statusFeedback = "❌ Gagal memperbarui: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Strategic Value Loop", "Landing Page Solusi Kami", false) {
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
                Text(
                    text = if (isSaving) "Menyimpan..." else "💾 Simpan Perubahan",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = {
                    config = com.sekota.features.admin.domain.model.ValueLoopConfig()
                    statusFeedback = "ℹ️ Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)"
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "↺ Reset Bawaan",
                    fontFamily = getDmSansFontFamily(),
                    color = InkNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun WhySekotaTab(
    getWhySekotaUseCase: GetAdminWhySekotaUseCase,
    saveWhySekotaUseCase: SaveAdminWhySekotaUseCase,
    windowWidth: WindowWidth,
    onActionWithProductionGuard: ((String, String, Boolean, () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var config by remember { mutableStateOf(WhySekotaConfig()) }
    var statusFeedback by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        config = getWhySekotaUseCase()
        com.sekota.syncService.syncEventFlow.collect {
            config = getWhySekotaUseCase()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CmsActionHeader(
            title = "Why Sekota (Nilai Keunggulan)",
            subtitle = "Kelola pre-title, headline, deskripsi, tombol CTA, serta daftar kartu pilar 'Mengapa Sekota' di portal publik.",
            windowWidth = windowWidth
        )

        statusFeedback?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = if (it.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    fontFamily = getDmSansFontFamily(),
                    color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Content / Header Editor Card
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
                    text = "Konten Utama Section",
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InkNavy
                )

                OutlinedTextField(
                    value = config.preTitle,
                    onValueChange = {
                        config = config.copy(preTitle = it)
                        statusFeedback = null
                    },
                    label = { Text("Pre-title / Eyebrow (e.g. MENGAPA SEKOTA)", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                    maxLines = 4
                )

                OutlinedTextField(
                    value = config.description,
                    onValueChange = {
                        config = config.copy(description = it)
                        statusFeedback = null
                    },
                    label = { Text("Deskripsi / Subjudul", fontFamily = getDmSansFontFamily()) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = config.ctaLabel,
                        onValueChange = {
                            config = config.copy(ctaLabel = it)
                            statusFeedback = null
                        },
                        label = { Text("Label Tombol CTA", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = config.ctaTarget,
                        onValueChange = {
                            config = config.copy(ctaTarget = it)
                            statusFeedback = null
                        },
                        label = { Text("Target Link / Aksi CTA (e.g. #kontak)", fontFamily = getDmSansFontFamily()) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pillar / Value Cards Management
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kartu Pilar Keunggulan (${config.cards.size} kartu)",
                fontFamily = getMontserratFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = InkNavy
            )

            Button(
                onClick = {
                    val nextNum = config.cards.size + 1
                    val newCard = WhySekotaCard(
                        id = "card_${kotlin.random.Random.nextInt(1000, 9999)}",
                        title = "Keunggulan Baru #$nextNum",
                        description = "Deskripsi nilai keunggulan atau dampak strategis bagi mitra.",
                        iconName = "sparkle"
                    )
                    config = config.copy(cards = config.cards + newCard)
                    statusFeedback = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = "➕ Tambah Kartu Pilar",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val availableIcons = listOf("trending", "chip", "shield", "sparkle")

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            config.cards.forEachIndexed { index, card ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = BrandTeal,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "#${index + 1}",
                                        fontFamily = getDmSansFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = "Kartu Pilar ${index + 1}",
                                    fontFamily = getMontserratFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = InkNavy
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Move Up Button
                                IconButton(
                                    onClick = {
                                        if (index > 0) {
                                            val mutable = config.cards.toMutableList()
                                            val item = mutable.removeAt(index)
                                            mutable.add(index - 1, item)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    },
                                    enabled = index > 0
                                ) {
                                    com.sekota.components.SekotaArrowUpVectorIcon(
                                        size = 18.dp,
                                        tint = if (index > 0) InkNavy else Color.LightGray
                                    )
                                }

                                // Move Down Button
                                IconButton(
                                    onClick = {
                                        if (index < config.cards.lastIndex) {
                                            val mutable = config.cards.toMutableList()
                                            val item = mutable.removeAt(index)
                                            mutable.add(index + 1, item)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    },
                                    enabled = index < config.cards.lastIndex
                                ) {
                                    com.sekota.components.SekotaArrowDownVectorIcon(
                                        size = 18.dp,
                                        tint = if (index < config.cards.lastIndex) InkNavy else Color.LightGray
                                    )
                                }

                                // Delete Button
                                IconButton(
                                    onClick = {
                                        if (config.cards.size > 1) {
                                            val mutable = config.cards.toMutableList()
                                            mutable.removeAt(index)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        } else {
                                            statusFeedback = "❌ Minimal harus ada 1 kartu pilar 'Why Sekota'"
                                        }
                                    },
                                    enabled = config.cards.size > 1
                                ) {
                                    com.sekota.components.SekotaTrashVectorIcon(
                                        size = 18.dp,
                                        tint = if (config.cards.size > 1) Color(0xFFE53E3E) else Color.LightGray
                                    )
                                }
                            }
                        }

                        // Icon Selector Chips with real Compose drawables
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Pilih Ikon Pilar:",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf(
                                    Triple("trending", "Tren", Res.drawable.icon_3),
                                    Triple("chip", "Chip", Res.drawable.icon_4),
                                    Triple("shield", "Shield", Res.drawable.shield),
                                    Triple("sparkle", "Sparkle", Res.drawable.icon_6)
                                ).forEach { (iconKey, iconLabel, iconRes) ->
                                    val isSelected = card.iconName.equals(iconKey, ignoreCase = true)
                                    Surface(
                                        color = if (isSelected) BrandTeal.copy(alpha = 0.12f) else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, if (isSelected) BrandTeal else Color(0xFFCBD5E1)),
                                        modifier = Modifier.clickable {
                                            val mutable = config.cards.toMutableList()
                                            mutable[index] = card.copy(iconName = iconKey)
                                            config = config.copy(cards = mutable)
                                            statusFeedback = null
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(iconRes),
                                                contentDescription = iconLabel,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = iconLabel,
                                                fontFamily = getDmSansFontFamily(),
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) BrandTeal else InkNavy
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        OutlinedTextField(
                            value = card.title,
                            onValueChange = { newTitle ->
                                val mutable = config.cards.toMutableList()
                                mutable[index] = card.copy(title = newTitle)
                                config = config.copy(cards = mutable)
                                statusFeedback = null
                            },
                            label = { Text("Judul Kartu Pilar", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = card.description,
                            onValueChange = { newDesc ->
                                val mutable = config.cards.toMutableList()
                                mutable[index] = card.copy(description = newDesc)
                                config = config.copy(cards = mutable)
                                statusFeedback = null
                            },
                            label = { Text("Deskripsi Kartu", fontFamily = getDmSansFontFamily()) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )
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
                    if (config.title.isBlank()) {
                        statusFeedback = "❌ Judul Utama / Headline tidak boleh kosong"
                        return@Button
                    }
                    if (config.cards.any { it.title.isBlank() }) {
                        statusFeedback = "❌ Judul pada setiap kartu pilar tidak boleh kosong"
                        return@Button
                    }
                    val performSave = {
                        scope.launch {
                            isSaving = true
                            val result = saveWhySekotaUseCase(config)
                            isSaving = false
                            if (result.isSuccess) {
                                statusFeedback = "✅ 'Why Sekota' berhasil diperbarui dan disinkronkan ke Web!"
                            } else {
                                statusFeedback = "❌ Gagal memperbarui: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    }

                    if (onActionWithProductionGuard != null) {
                        onActionWithProductionGuard("Simpan Why Sekota", "Landing Page Pilar Keunggulan", false) {
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
                Text(
                    text = if (isSaving) "Menyimpan..." else "💾 Simpan Perubahan",
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = {
                    config = WhySekotaConfig()
                    statusFeedback = "ℹ️ Reset ke konfigurasi bawaan (Tekan Simpan untuk menerapkan)"
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "↺ Reset Bawaan",
                    fontFamily = getDmSansFontFamily(),
                    color = InkNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}



@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValueLoopTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        ValueLoopTab(
            getValueLoopUseCase = remember { GetAdminValueLoopUseCase(repo) },
            saveValueLoopUseCase = remember { SaveAdminValueLoopUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun WhySekotaTabPreview() {
    MaterialTheme {
        val repo = remember { com.sekota.features.admin.data.repository.AdminRepositoryImpl() }
        WhySekotaTab(
            getWhySekotaUseCase = remember { GetAdminWhySekotaUseCase(repo) },
            saveWhySekotaUseCase = remember { SaveAdminWhySekotaUseCase(repo) },
            windowWidth = WindowWidth.Expanded
        )
    }
}
