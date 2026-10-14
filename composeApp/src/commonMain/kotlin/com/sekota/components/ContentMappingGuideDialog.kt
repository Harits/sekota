package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily

@Composable
fun ContentMappingGuideDialog(
    initialTab: Int = 0,
    onDismiss: () -> Unit
) {
    var selectedSectionIndex by remember { mutableStateOf(initialTab) }
    val sectionNames = listOf(
        "Books & E-Book",
        "Value Loop",
        "Intelligence Suite",
        "Why Sekota",
        "Contact",
        "Footer",
        "AppBar Navigation"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 800.dp)
                .heightIn(max = 680.dp)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                // Header
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
                            color = Color(0xFFE0F7FA),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                SekotaInfoVectorIcon(size = 20.dp, tint = Color(0xFF00838F))
                            }
                        }
                        Column {
                            Text(
                                text = "Panduan Pemetaan Konten (Content Mapping)",
                                fontFamily = getMontserratFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Ketahui hubungan field CMS dengan komponen visual Web Sekota",
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section Tabs Row (Responsive with Horizontal Scroll)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sectionNames.forEachIndexed { idx, name ->
                        val isSelected = selectedSectionIndex == idx
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = if (isSelected) Color(0xFF00B5C8) else Color(0xFFF1F5F9),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFF00B5C8) else Color(0xFFCBD5E1)),
                            modifier = Modifier.clickable { selectedSectionIndex = idx }
                        ) {
                            Text(
                                text = name,
                                fontFamily = getDmSansFontFamily(),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedSectionIndex) {
                        0 -> GuideSectionBooks()
                        1 -> GuideSectionValueLoop()
                        2 -> GuideSectionSuite()
                        3 -> GuideSectionWhySekota()
                        4 -> GuideSectionContact()
                        5 -> GuideSectionFooter()
                        6 -> GuideSectionAppBar()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B5C8)),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text("Tutup Panduan", fontFamily = getDmSansFontFamily(), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun GuideSectionBooks() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Judul Publikasi (Title)",
            target = "CatalogScreen Card & BookDetailsScreen Header",
            rule = "Maksimal 80 karakter. Hindari judul berlebihan agar layout kartu tidak pecah.",
            good = "Laporan Transformasi Smart City 2026",
            bad = "Laporan yang sangat panjang sekali melebihi tiga baris judul di layar..."
        )
        GuideItemRow(
            field = "Penulis (Author)",
            target = "Metadata Bar & Detail Page",
            rule = "Maksimal 50 karakter.",
            good = "Tim Riset Sekota & APJASI",
            bad = "Ditulis oleh admin 1 bersama tim riset..."
        )
        GuideItemRow(
            field = "Cover Buku",
            target = "Cover Image (Grid & Detail)",
            rule = "Aspek rasio 3:4 (rekomendasi 600x800 px, maks 500 KB).",
            good = "Gambar tajam resolusi vertikal",
            bad = "Foto lanskap melebar terdistorsi"
        )
        GuideItemRow(
            field = "Rating & Pembaca",
            target = "Telemetry & Reader Count",
            rule = "Read-only: Dihitung secara objektif oleh Telemetry Engine. Dilarang manipulasi.",
            good = "Dihitung otomatis",
            bad = "Manual input bintang 5 palsu"
        )
    }
}

@Composable
private fun GuideSectionValueLoop() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Judul Header & Subtitle",
            target = "ValueLoopSection Header",
            rule = "Judul maks 60 karakter, subtitle maks 160 karakter.",
            good = "Strategic Value Loop: Integrasi End-to-End",
            bad = "Solusi Kami"
        )
        GuideItemRow(
            field = "Nomor Tahap (Step)",
            target = "Step Badge (01 - 05)",
            rule = "Gunakan tepat 2 digit angka.",
            good = "01, 02, 03",
            bad = "Langkah 1, Pertama"
        )
        GuideItemRow(
            field = "Judul & Deskripsi Tahap",
            target = "Kartu Tahap Siklus",
            rule = "Judul maks 40 karakter, deskripsi maks 140 karakter.",
            good = "Akuisisi Data Multi-Sektor",
            bad = "Paragraf panjang tanpa poin fokus"
        )
    }
}

@Composable
private fun GuideSectionSuite() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Kode Produk (Code)",
            target = "Routing & Detail Identifier",
            rule = "3 huruf kapital unik (VRD, ASC, SOC, ECO).",
            good = "VRD (Veridia)",
            bad = "veridia_platform"
        )
        GuideItemRow(
            field = "Eyebrow & Nama Produk",
            target = "Product Card Header",
            rule = "Eyebrow huruf kapital (maks 45 kar), nama maks 30 karakter.",
            good = "ENVIRONMENT INTELLIGENCE • Veridia",
            bad = "Produk Unggulan • Veridia Baru"
        )
        GuideItemRow(
            field = "Fitur Utama (Features)",
            target = "Bullet Points Kartu",
            rule = "3 sampai 5 butir fitur ringkas.",
            good = "Pemantauan Emisi Karbon Real-Time",
            bad = "Deskripsi panjang berulang"
        )
    }
}

@Composable
private fun GuideSectionWhySekota() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Headline 'Why Sekota'",
            target = "ValuePropDark Section Header",
            rule = "Maksimal 70 karakter untuk fokus dampak.",
            good = "Mengapa Memilih Ekosistem Sekota?",
            bad = "Keunggulan kami yang sangat banyak sekali"
        )
        GuideItemRow(
            field = "Kartu Pilar & Ikon",
            target = "Grid Kartu Nilai",
            rule = "Pilih ikon: trending, chip, shield, sparkle. Judul maks 40 kar.",
            good = "Akurasi Data Terverifikasi (Ikon Shield)",
            bad = "Judul tidak relevan"
        )
    }
}

@Composable
private fun GuideSectionContact() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Headline Formulir",
            target = "ContactFormSection Left Headline",
            rule = "Maksimal 50 karakter (dapat gunakan baris baru \\n).",
            good = "Mulai Percakapan\\nStrategis.",
            bad = "Hubungi Kami"
        )
        GuideItemRow(
            field = "Email & Info Kantor",
            target = "Metadata Kontak Langsung",
            rule = "Format email valid & alamat resmi.",
            good = "sales@sekota.id",
            bad = "email_tidak_valid"
        )
    }
}

@Composable
private fun GuideSectionFooter() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Tagline & Afiliasi",
            target = "Footer Branding Section",
            rule = "Maksimal 160 karakter. Otomatis diterjemahkan per bahasa.",
            good = "Trusted Intelligence for Sustainable Impact...",
            bad = "Footer Sekota"
        )
        GuideItemRow(
            field = "Kolom Menu (Produk, Perusahaan, Legal)",
            target = "Footer 3 Columns Grid",
            rule = "Atur sortOrder, target URL/kode, dan toggle visibilitas aktif.",
            good = "Label 'E-Book' -> Target 'EBOOK' (Aktif: True)",
            bad = "Label kosong tanpa target"
        )
    }
}

@Composable
private fun GuideSectionAppBar() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItemRow(
            field = "Label Navigasi (Label)",
            target = "Navbar Desktop & Mobile Drawer Items",
            rule = "Maksimal 30 karakter. Gunakan nama ringkas agar tidak memicu overflow horizontal.",
            good = "Solusi, E-Book, Insights",
            bad = "Halaman Direktori Seluruh Buku dan Laporan Lengkap"
        )
        GuideItemRow(
            field = "Target Rute / Hash (Route)",
            target = "Click Event Handler & Scroll Target",
            rule = "Gunakan hashtag '#section' untuk scrolling seksi Landing Page atau '/path' untuk layar terpisah.",
            good = "#solusi, #produk, /catalog, /merchandise",
            bad = "unsupported_link_format"
        )
        GuideItemRow(
            field = "Urutan Tampilan (Order)",
            target = "Position Order (Kiri ke Kanan)",
            rule = "Angka bulat berurutan (1, 2, 3...) menentukan prioritas penempatan item.",
            good = "1 (Solusi), 2 (Produk), 3 (E-Book)",
            bad = "Angka acak atau bernilai negatif"
        )
        GuideItemRow(
            field = "Visibilitas (Is Visible)",
            target = "AppBar Filter (isVisible == true)",
            rule = "Toggle untuk menyembunyikan halaman dalam pengembangan tanpa menghapus data.",
            good = "Checked (Aktif tampil)",
            bad = "Unchecked untuk menu draft"
        )
    }
}

@Composable
private fun GuideItemRow(
    field: String,
    target: String,
    rule: String,
    good: String,
    bad: String
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = field,
                    fontFamily = getMontserratFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A)
                )
                Surface(
                    color = Color(0xFFE0F2FE),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = target,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0369A1),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "Kaidah: $rule",
                fontFamily = getDmSansFontFamily(),
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "✅ $good",
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 11.sp,
                    color = Color(0xFF15803D),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "❌ $bad",
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 11.sp,
                    color = Color(0xFFB91C1C),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContentMappingGuideDialogPreview() {
    MaterialTheme {
        ContentMappingGuideDialog(
            initialTab = 0,
            onDismiss = {}
        )
    }
}
