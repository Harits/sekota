# UI/UX Design System Specification: Sekota Enterprise Theme
**Document Identifier:** `SEC-SPEC-DS-01`  
**Phase:** Fase 2 (Specification & Design System)  
**Standard Compliance:** Material Design 3 (M3), ISO 9001:2015, ISO 27001:2022  

---

## 1. Executive Summary & Brand Identity
Sekota Design System mendefinisikan bahasa visual terpadu untuk platform **Sekota Web** (eksternal pembaca/klien) dan **Sekota CMS** (internal backoffice Desktop macOS, Android, dan CLI). Sistem ini menerapkan prinsip kejelasan data (*data clarity*), tipografi kontras tinggi, dan responsivitas ergonomis multiplatform.

### 🎨 Brand Color Tokens
| Token Name | Hex Code | Semantic Role | Usage & Invariants |
| :--- | :--- | :--- | :--- |
| `InkNavy` | `#0D1F2D` | `Surface High / Primary Dark` | Header bar, Navigation Rail, kartu kontras, judul utama |
| `BrandTeal` | `#00B5C8` | `Primary Brand Accent` | Tombol aksi utama, tautan aktif, tab terpilih, status indikator |
| `BrandGreen` | `#60BD65` | `Secondary Accent / Success` | Indikator stok tersedia, rating tinggi, gradient aksen |
| `SurfacePure` | `#FFFFFF` | `Surface Lowest` | Latar kartu utama, modal dialog, textfield background |
| `SurfaceAlt` | `#F7FAFB` | `Surface Canvas` | Latar belakang layar scaffold, pembatas netral |
| `BorderSubtle` | `#E2E8F0` | `Outline Variant` | Garis pembatas kartu, placeholder gambar, dividers |
| `TextSecondary`| `#4A5568` | `OnSurface Variant` | Teks keterangan sekunder, pengarang, metadata ISBN |
| `ErrorRed` | `#E53E3E` | `Error` | Tombol aksi destruktif (Delete), pesan error validasi |

> [!CAUTION]
> **Aturan Mutlak (#000000 Invariant)**: Dilarang menggunakan warna hitam pekat (`#000000`). Gunakan `InkNavy` (`#0D1F2D`) atau `Color(0xFF1A202C)` untuk kontras gelap alami.

---

## 2. Tipografi Resmi (Typography Scales)

| Token Scale | Font Family | Size | Weight | Tracking | Purpose |
| :--- | :--- | :---: | :---: | :---: | :--- |
| `Headline Large` | **Montserrat** | `32sp` | Bold (700) | `-0.5sp` | Judul layar utama (e.g. "Books Registry", "Intelligence Suite") |
| `Headline Medium`| **Montserrat** | `24sp` | Bold (700) | `-0.2sp` | Judul kartu produk & judul modal dialog |
| `Title Large` | **Montserrat** | `20sp` | Bold (700) | `0.0sp` | Brand header bar ("SEKOTA CMS", "ADMIN PANEL") |
| `Title Medium` | **DM Sans** | `16sp` | Medium (500) | `+0.1sp` | Label item navigasi, sub-header kartu |
| `Body Large` | **DM Sans** | `15sp` | Regular (400) | `+0.2sp` | Deskripsi panjang, teks tombol konsultasi |
| `Body Medium` | **DM Sans** | `13sp` | Regular (400) | `+0.2sp` | Metadata buku (Author, ISBN), rating, checklist fitur |
| `Category Eyebrow`| **DM Sans** | `11sp` | Bold (700) | `+1.0sp` | Badge uppercase kategori produk (e.g. "INTELLIGENCE SUITE") |

---

## 3. Shape Tokens & Geometry
* **Pill Buttons (`RoundedCornerShape(50)` / `100.dp`)**: Digunakan untuk tombol utama panggilan aksi (*Call-to-Action*), seperti tombol "+ Add Book" dan "Konsultasi Strategis".
* **Container Cards (`RoundedCornerShape(12.dp)` - `16.dp`)**: Sudut membulat modern untuk kartu daftar buku, kartu produk, dan dialog form.
* **Input Fields (`RoundedCornerShape(8.dp)`)**: Sudut rounded terstruktur untuk input teks form admin.

---

## 4. Adaptive Layout Standards (Breakpoints)

```
Mobile Phone (< 700dp)            Desktop & Tablet (>= 700dp)
┌──────────────────────────┐      ┌──────────┬─────────────────────────────┐
│ Top AppBar (SEKOTA CMS)  │      │ ADMIN    │ Content Workbench           │
├──────────────────────────┤      │ PANEL    │                             │
│ Content (Single Column)  │      │          │ Title (+ Add Action)        │
│                          │  vs  │ Books    │                             │
│                          │      │ Suite    │ Multi-column Card Grid      │
├──────────────────────────┤      │ Merch    │                             │
│ Bottom Navigation Bar    │      │ Metrics  │                             │
└──────────────────────────┘      └──────────┴─────────────────────────────┘
```

1. **Compact Mobile Form (< 700dp)**:
   - Header atas: Top AppBar statis (`statusBarsPadding()`).
   - Navigasi: Bottom Navigation Bar 4 tab ergonomis.
   - List & Grid: Kolom tunggal vertikal.
2. **Expanded Desktop Form ($\ge$ 700dp)**:
   - Navigasi: Left Navigation Rail (`250.dp`) dengan brand header tebal.
   - Workbench: Split-screen multi-column grid (`GridCells.Fixed(2)` atau `Fixed(3)`).

---

## 5. Standard Component Specifications

### 5.1 Interactive Form Input Dialog (Modal)
Setiap aksi CRUD (tambah/edit) wajib menggunakan modal dialog Material 3 dengan spesifikasi:
- **Container**: `Surface` dengan latar `SurfacePure`, elevation `6.dp`, radius `16.dp`.
- **Header**: Teks Montserrat Bold `20sp` warna `InkNavy`.
- **Input Fields**: `OutlinedTextField` dengan label DM Sans, border `BrandTeal` saat fokus.
- **Action Buttons**: Row horizontal di kanan bawah (Tombol `Batal` warna abu-abu & Tombol `Simpan` warna `BrandTeal` bentuk kapsul).

### 5.2 Destructive Action Confirmation
Setiap penghapusan item wajib memiliki dialog konfirmasi:
- Tombol `Hapus` berwarna merah `ErrorRed` (`#E53E3E`).
- Konfirmasi eksplisit: "Apakah Anda yakin ingin menghapus item ini?".
