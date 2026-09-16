# Milestone Review & Acceptance Report (Gate 3 - Sprint 1 to 3)
**Dokumen Referensi: PM-FRM-05 / ISO 21502:2020 & SNI ISO 9001:2015**

---

## 1. Ringkasan Eksekutif Milestone

Laporan ini merangkum status pencapaian dan penerimaan teknis inkremen Sekota Web Platform dari **Sprint 1 (Foundation)**, **Sprint 2 (Interaction & Sync)**, hingga status berjalan **Sprint 3 (Persistence & Profile)**.

| Parameter | Catatan Evaluasi |
|---|---|
| **Periode Evaluasi** | 12 Mei 2026 s.d. 16 September 2026 |
| **Status Gerbang G3** | **ACCEPTED (SPRINT 1 - 3 CLOSED)** |
| **Pencapaian Story Points** | 43 / 43 Points Terselesaikan (100% Selesai) |
| **Kepatuhan Arsitektur** | Clean & Screaming Architecture diterapkan pada `shared` dan `composeApp` |
| **Kepatuhan Desain** | Editorial Design System Sekota (`Montserrat`, `DM Sans`, Ink Navy `#0D1F2D`, Brand Teal `#00B5C8`) |

---

## 2. Audit Hasil Delivery per Sprint

### Sprint 1: Foundation (Selesai - 19 Mei 2026)
- **Status**: 100% Closed (`WP-001` s.d `WP-008`).
- **Pencapaian**:
  - Web Navigation Bar responsif berbasis token Figma.
  - Setup Ktor Web Client untuk arsitektur multiplatform.
  - Landing Page Hero Section, Feature Grid, Intelligence Suite, Value Loop, Value Prop, E-Book Promo, dan Footer.
  - Sidebar Filter dinamis, eBook Catalog & Details, dan Merchandise Catalog.
- **Verifikasi**: Desktop preview aktif pada seluruh komponen UI utama.

### Sprint 2: Interaction & Sync (Selesai - 27 Mei 2026)
- **Status**: 100% Completed (`WP-009` s.d `WP-015`).
- **Pencapaian**:
  - Konfigurasi Ktor Server JWT Authentication.
  - API endpoint registrasi & login pengguna.
  - `AuthService` dan `TokenStorage` pada shared module.
  - Antarmuka Login dan Signup Screen responsif.
  - Engine WebSocket real-time sync (`/ws/sync`) di backend dan client listener.

### Sprint 3: Persistence & Profile (Selesai - 16 September 2026)
- **Status Work Packages**:
  - `WP-016` (SQLDelight Local Persistence): **Completed / Closed**
  - `WP-017` (PostgreSQL Database & Exposed ORM on Server): **Completed / Closed**
  - `WP-018` (User Profile API): **Completed / Closed**
  - `WP-019` (User Profile Screen UI): **Completed / Closed**
  - `WP-020` (Persistent Login Session): **Completed / Closed**
  - `WP-021` (UI/UX Design refinements for Auth & Profile from Figma `0:1` & Dynamic Role Based Display): **Completed / Closed**
- **Ringkasan Delivery**:
  - Integrasi dual base URL (Local Server Ktor `:8080` & Production Auth API `https://sekota.id/api/v1/`).
  - Persistent login token pada Wasm local storage.
  - Role-based profile support (`ADMIN`, `BOD`, `BD`, `READER`) dengan token warna Figma.
  - Desktop previews `@Preview(device = DESKTOP)` pada semua layar.

---

## 3. Matriks Kepatuhan Non-Fungsional (NFR & Keamanan)

| Aspek Kontrol | Standar Evaluasi | Status | Bukti Verifikasi |
|---|---|---|---|
| **ISO 27001 A.8.28** | Secure Coding (No SQL Injection) | **PASSED** | Exposed ORM DSL parameterized queries |
| **ISO 27001 A.8.12** | Data Leakage Prevention (Secrets) | **PASSED** | Token terisolasi di `.env` / SQLDelight |
| **ISO 27001 A.8.16** | Monitoring Activities | **PASSED** | Ktor CallLogging & Logback terintegrasi |
| **SNI ISO 9001 7.1.5.1** | Digital Verification & Builds | **PASSED** | Gradle build Wasm & Ktor test suite pass |
| **Sekota UI Preview** | `@Preview(device = DESKTOP)` | **PASSED** | Diterapkan pada 95% composables |

---

## 4. Penutupan Gate 3 & Persiapan Phase 1 Iterasi Master
1. Seluruh backlog dan parent feature Sprint 3 di OpenProject telah berstatus **Closed** (Version 820: Closed).
2. Perubahan pada branch `sprint-3` di-merge ke branch `master`.
3. Siklus pengembangan berikutnya siap melanjutkan Phase 1 refinement langsung di branch `master`.
