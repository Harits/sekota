# Matriks Pemetaan Konten CMS Workbench vs Komponen Web Sekota (Content Mapping Matrix)

Dokumen ini merupakan panduan master operasional bagi Administrator, Content Editor, dan Pengembang platform **Sekota Web 2026**. Dokumen ini menjabarkan pemetaan teknis antara field input yang dikelola di panel CMS (Back-End) dengan komponen visual (*Composable*) yang merendernya di portal Web publik (Front-End), beserta batasan karakter, rekomendasi visual, dan kaidah input.

---

## 1. Ikhtisar Arsitektur Sinkronisasi

```
┌─────────────────────────────────┐           WebSocket / HTTP Sync          ┌──────────────────────────────────┐
│          CMS Workbench          │ ───────────────────────────────────────> │         Portal Web Publik        │
│ (Desktop JVM / Android Tablet)  │ <─────────────────────────────────────── │           (Wasm / Web)           │
└─────────────────────────────────┘          Real-Time Auto-Refresh          └──────────────────────────────────┘
```
Setiap mutasi data yang disimpan di CMS Workbench akan secara otomatis dipersistensikan ke `AdminDataStorage` dan disiarkan secara real-time via `CmsSyncServer` (`http://localhost:8088/api/v1/sync`). Klien Web Sekota mendengarkan stream `SyncService.syncEventFlow` dan memperbarui tampilan seketika tanpa perlu reload halaman browser.

---

## 2. Matriks Pemetaan Entitas Konten (Content Mapping Matrix)

### A. Books Registry & Publikasi Riset

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `title` | `String` | `CatalogScreen` (Card Title), `BookDetailsScreen` (Header Title) | Max 80 karakter | **Good**: "Laporan Transformasi Smart City 2026"<br>**Bad**: "Laporan yang sangat panjang sekali melebihi tiga baris judul..." (Overflow) |
| `author` | `String` | `CatalogScreen`, `BookDetailsScreen` (Metadata Bar) | Max 50 karakter | **Good**: "Tim Riset Sekota & APJASI"<br>**Bad**: "Ditulis oleh admin 1 bersama tim riset..." |
| `category` | `String` | Filter Chip Catalog, Detail Badge | Pilihan terdaftar (`getCategories`) | Wajib memilih kategori yang valid dari daftar. |
| `year` | `Int` | Badge Tahun di Card & Detail | 4 digit tahun (misal `2026`) | **Good**: `2026`<br>**Bad**: `26` atau `Tahun 2026` |
| `coverBase64` / `coverUrl` | `String` | Cover Image di Card & Detail Preview | Aspect Ratio 3:4 atau 1:1.4, Max 500KB | **Good**: Gambar beresolusi tajam 600x800px PNG/JPG.<br>**Bad**: Foto lanskap melebar yang terpotong. |
| `description` | `String` | `BookDetailsScreen` Sinopsis & Abstrak | Max 1000 karakter | Gunakan paragraf ringkas dan jelas. |
| `rating` & `ratingCount` | `Telemetry` | Telemetry Reader Integrity Protocol | **Read-Only** (Dihitung sistem) | Dilarang memanipulasi rating secara manual di CMS. |

---

### B. Strategic Value Loop (Siklus Solusi)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `title` | `String` | `ValueLoopSection.kt` (Header Title) | Max 60 karakter | **Good**: "Strategic Value Loop: Integrasi End-to-End"<br>**Bad**: "Solusi" (Terlalu ambigu) |
| `subtitle` | `String` | `ValueLoopSection.kt` (Supporting Subtitle) | Max 160 karakter | Jelaskan nilai transformasi siklus data. |
| `steps[i].stepNumber` | `String` | Pill Indikator Tahapan (misal `01`, `02`) | Tepat 2 digit (`01`-`05`) | **Good**: `01`, `02`, `03`<br>**Bad**: `Langkah 1`, `Satu` |
| `steps[i].title` | `String` | Judul Kartu Tahap Siklus | Max 40 karakter | **Good**: "Akuisisi Data Multi-Sektor"<br>**Bad**: Teks panjang lebih dari 2 baris |
| `steps[i].description` | `String` | Deskripsi Kartu Tahap | Max 140 karakter | Penjelasan fokus pada dampak dan output. |

---

### C. Intelligence Suite (Ekosistem Produk)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `code` | `String` | `IntelligenceSuite.kt`, `ProductDetailsScreen` | Tepat 3 huruf kapital unik (`VRD`, `ASC`, `SOC`, `ECO`) | Digunakan untuk routing dan deep link produk. |
| `name` | `String` | Nama Produk Utama | Max 30 karakter | **Good**: "Veridia", "Ascendio"<br>**Bad**: "Veridia Platform v2.0 Live" |
| `categoryEyebrow` | `String` | Eyebrow Tag di atas nama produk | Max 45 karakter huruf kapital | **Good**: "ENVIRONMENT & EMISSION INTELLIGENCE" |
| `description` | `String` | Ringkasan kartu produk | Max 180 karakter | Memberikan proposisi nilai produk. |
| `features` | `List<String>` | Bullet point kapabilitas produk | 3 - 5 item, maks 60 karakter/item | Sorot usecase utama produk. |

---

### D. Merchandise (Swag & Merchandise Store)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `name` | `String` | `MerchandiseScreen.kt` (Card Title) | Max 40 karakter | **Good**: "Sekota Eco-Tech Hoodie"<br>**Bad**: "Baju Sekota Murah" |
| `category` | `String` | Filter Chip Kategori Merch | Kategori baku (`Apparel`, `Accessories`, dll.) | Pilih kategori terstandarisasi. |
| `price` | `Double` | Price Tag Badge | Format desimal positif (misal `45.0`) | **Good**: `45.0` (USD) atau angka nominal baku. |
| `rating` | `Double` | Star Rating Merch | Skala 0.0 s/d 5.0 | Menunjukkan kepuasan pembeli. |

---

### E. Why Sekota (Pilar Nilai & Dampak)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `title` | `String` | `ValuePropDark.kt` (Header Headline) | Max 70 karakter | **Good**: "Mengapa Memilih Ekosistem Sekota?" |
| `subtitle` | `String` | `ValuePropDark.kt` (Subheadline) | Max 180 karakter | Proposisi nilai utama keunggulan kemitraan. |
| `cards[i].title` | `String` | Judul Kartu Pilar | Max 40 karakter | **Good**: "Akurasi Data Terverifikasi" |
| `cards[i].description` | `String` | Deskripsi Kartu Pilar | Max 150 karakter | Dampak nyata dan kredibilitas institusi. |
| `cards[i].iconName` | `String` | Ikon Vektor Kartu | `trending`, `chip`, `shield`, `sparkle` | Pilih ikon vektor resmi yang sesuai pilar. |

---

### F. Contact & Inquiries (Percakapan Strategis)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `title` | `String` | `ContactFormSection.kt` (Left Headline) | Max 50 karakter (dapat multi-line `\n`) | **Good**: "Mulai Percakapan\nStrategis." |
| `subtitle` | `String` | `ContactFormSection.kt` (Subtitle) | Max 140 karakter | Ekspektasi waktu respons (misal 1x24 jam). |
| `address` | `String` | Info Kontak Alamat Kantor | Alamat resmi kantor | **Good**: "Jl. H. Rasam No.96, Parigi Baru..." |
| `email` | `String` | Info Kontak Email Bisnis | Format email valid | **Good**: "sales@sekota.id" |
| `website` | `String` | Info Kontak Web URL | Domain Sekota | **Good**: "www.sekota.id" |

---

### G. Footer Management (Dynamic Global Footer)

| Field CMS | Tipe Data | Komponen Penerima Web Sekota | Batas Karakter & Format | Kaidah Input (Good vs Bad) |
|---|---|---|---|---|
| `tagline` | `String` | `Footer.kt` (Branding Tagline) | Max 160 karakter | Diterjemahkan dinamis saat switch bahasa. |
| `affiliationText` | `String` | Teks Afiliasi eBdesk | Max 50 karakter | Standar: "eBdesk Group Affiliation". |
| `affiliationSubtext` | `String` | Subteks Kemitraan | Max 30 karakter uppercase | Standar: "NATIONWIDE PARTNER". |
| `copyrightText` | `String` | Baris Bawah Hak Cipta | Max 100 karakter | Otomatis diselaraskan per bahasa. |
| `productColumn` | `Config` | Kolom 2 Tautan Produk | Urutan (`sortOrder`), visibilitas toggle | Mendukung kode produk (`VRD`, `ASC`, dll.). |
| `companyColumn` | `Config` | Kolom 3 Tautan Perusahaan | Urutan, visibilitas toggle | Mendukung rute internal (`EBOOK`, `MERCH`, dll.). |
| `legalColumn` | `Config` | Kolom 4 Tautan Legalitas | Urutan, visibilitas toggle | Tautan privasi dan ketentuan. |
| `socialLinks` | `List` | Ikon Media Sosial | Platform & URL eksternal valid | **Good**: `https://linkedin.com/company/sekota` |

---

## 3. Checklist Validasi & Quality Assurance untuk Operator CMS

- [ ] **Panjang Karakter**: Pastikan judul dan deskripsi tidak melebihi rekomendasi batas karakter di atas untuk menjaga ritme grid visual responsif.
- [ ] **Resolusi Aset Gambar**: Pastikan cover buku dan gambar produk berukuran wajar (maks 500KB) dengan aspek rasio yang konsisten (3:4 untuk buku).
- [ ] **Pemeriksaan Multi-Bahasa**: Uji peralihan bahasa pada portal publik untuk memastikan label menu dan copywriting tampil natural baik dalam Bahasa Indonesia maupun English.
- [ ] **Koneksi Lingkungan**: Perhatikan **Environment Badge** pada pojok kanan atas CMS Workbench (Development, Staging, atau Production) sebelum melakukan penyimpanan data.
