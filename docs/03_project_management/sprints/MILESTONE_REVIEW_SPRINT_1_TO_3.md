# Milestone Review & Acceptance Report (Gate 3 - Sprint 1 to 3)
**Dokumen Referensi: PM-FRM-05 / ISO 21502:2020 & SNI ISO 9001:2015**

---

## 1. Ringkasan Eksekutif Milestone

Laporan ini merangkum status pencapaian dan penerimaan teknis inkremen Sekota Web Platform dari **Phase 1 / Sprint 1 (Foundation)**, **Phase 2 / Sprint 2 (Interaction & Sync)**, hingga status berjalan **Phase 3 / Sprint 3 (Persistence & Profile)**, serta ketetapan batas cakupan (*scope boundary*) dengan aplikasi Sekota Industrial CMS.

| Parameter | Catatan Evaluasi |
|---|---|
| **Periode Evaluasi** | 12 Mei 2026 s.d. 16 September 2026 |
| **Status Gerbang G3** | **ACCEPTED (PHASE 1 - 3 CLOSED)** |
| **Pencapaian Story Points** | 43 / 43 Points Terselesaikan (100% Selesai) |
| **Kepatuhan Arsitektur** | Clean & Screaming Architecture diterapkan pada `shared` dan `composeApp` |
| **Kepatuhan Desain** | Editorial Design System Sekota (`Montserrat`, `DM Sans`, Ink Navy `#0D1F2D`, Brand Teal `#00B5C8`) |
| **Pemisahan Peran CMS** | Books Registry pada Web/Desktop CMS beroperasi secara **View-Only**; CRUD naskah dikelola di **Sekota Industrial CMS** |

---

## 2. Batas Tata Kelola Cakupan: Industrial CMS Master vs Web Metadata Enrichment
 
Berdasarkan tinjauan operasional divisi editorial, riset, dan sistem web:
1. **Master Data Source (`bookinteractiontool` / Sekota Industrial CMS)**:
   - Pengelolaan master buku, judul, kepenulisan resmi, pendaftaran ISBN, dan penghapusan arsip fisik/penerbitan dikelola terpusat oleh **Operation Department**.
   - Berfungsi sebagai otoritas inventaris tunggal (*single source of truth*) yang diakses via API `/api/v1/admin/books`.
2. **Web Presentation & Metadata Enrichment (Sekota CMS)**:
   - Aplikasi **Sekota CMS** (Desktop JVM & Android) menyinkronkan data master dari `bookinteractiontool` dan memberikan kapabilitas pengayaan metadata khusus pembaca web melalui dialog **`✏️ Kelola Tampilan Web`**:
     - **Kategori Publikasi**: `SMART CITY`, `ESG`, `INTELLIGENCE`, `GOVERNANCE`.
     - **Sinopsis & Executive Summary** untuk pembaca eksekutif.
     - **Rating & Pembaca**: Skor bintang (e.g. `4.8/5`) dan total pembaca terverifikasi (e.g. `1,240 readers`).
     - **Tautan Naskah Digital**: URL pembaca manuskrip/download PDF (`pdfUrl`).
     - **Spesifikasi Dokumen**: Jumlah halaman dan estimasi durasi baca (`readingTime`, `pages`).
     - **Unggah Sampul Resolusi Tinggi**: File picker base64 terintegrasi.
3. **Penyajian Dinamis pada Web Sekota**:
   - `CatalogScreen` meneruskan ID buku terpilih ke `App.kt` routing context.
   - `BookDetailsScreen` merender naskah spesifik secara dinamis dengan visual rating bintang, pembaca terverifikasi, spesifikasi teknis, serta aksi baca/unduh PDF yang terlindungi modal `AuthGateDialog`.

---

## 3. Audit Hasil Delivery per Sprint / Fase

### Phase 1 / Sprint 1: Foundation (Selesai - 19 Mei 2026)
- **Status**: 100% Closed (`WP-001` s.d `WP-008`).
- **Pencapaian**:
  - Web Navigation Bar responsif berbasis token Figma.
  - Setup Ktor Web Client untuk arsitektur multiplatform.
  - Landing Page Hero Section, Feature Grid, Intelligence Suite, Value Loop, Value Prop, E-Book Promo, dan Footer.
  - Sidebar Filter dinamis, eBook Catalog & Details, dan Merchandise Catalog.
- **Verifikasi**: Desktop preview aktif pada seluruh komponen UI utama.

### Phase 2 / Sprint 2: Interaction & Sync (Selesai - 27 Mei 2026)
- **Status**: 100% Closed (`WP-009` s.d `WP-015`).
- **Pencapaian**:
  - Konfigurasi Ktor Server JWT Authentication.
  - API endpoint registrasi & login pengguna.
  - `AuthService` dan `TokenStorage` pada shared module.
  - Antarmuka Login dan Signup Screen responsif.
  - Engine WebSocket real-time sync (`/ws/sync`) di backend dan client listener.

### Phase 3 / Sprint 3: Persistence & Profile (Selesai - 16 September 2026)
- **Status Work Packages**:
  - `WP-016` (SQLDelight Local Persistence): **Closed (100%)**
  - `WP-017` (PostgreSQL Database & Exposed ORM on Server): **Closed (100%)**
  - `WP-018` (User Profile API): **Closed (100%)**
  - `WP-019` (User Profile Screen UI): **Closed (100%)**
  - `WP-020` (Persistent Login Session): **Closed (100%)**
  - `WP-021` (UI/UX Design refinements for Auth & Profile from Figma `0:1` & Dynamic Role Based Display): **Closed (100%)**
- **Ringkasan Delivery**:
  - Integrasi dual base URL (Local Server Ktor `:8080` & Production Auth API `https://sekota.id/api/v1/`).
  - Persistent login token pada Wasm local storage dan SQLDelight driver.
  - Role-based profile support (`ADMIN`, `BOD`, `BD`, `READER`) dengan token warna Figma.
  - Desktop previews `@Preview(device = DESKTOP)` pada semua layar.

---

## 4. Matriks Kepatuhan Non-Fungsional (NFR & Keamanan)

| Aspek Kontrol | Standar Evaluasi | Status | Bukti Verifikasi |
|---|---|---|---|
| **ISO 27001 A.8.28** | Secure Coding (No SQL Injection) | **PASSED** | Exposed ORM DSL parameterized queries |
| **ISO 27001 A.8.12** | Data Leakage Prevention (Secrets) | **PASSED** | Token terisolasi di `.env` / SQLDelight |
| **ISO 27001 A.8.16** | Monitoring Activities | **PASSED** | Ktor CallLogging & Logback terintegrasi |
| **SNI ISO 9001 7.1.5.1** | Digital Verification & Builds | **PASSED** | Gradle build Wasm & Desktop pass tanpa error |
| **Sekota UI Preview** | `@Preview(device = DESKTOP)` | **PASSED** | Diterapkan pada seluruh komponen utama |

---

## 5. Penutupan Gate 3 & Transisi ke Phase 1 Refinement (Sprint 4)
1. Seluruh backlog dan parent feature Phase 1 sampai Phase 3 di OpenProject telah berstatus **Closed**.
2. Branch `sprint-3` telah berhasil diintegrasikan ke `master`.
3. Siklus Sprint 4 berfokus pada penyempurnaan CMS Tri-Platform dan Inline Auth-Gating dengan isolasi arsitektural yang terstandar.
