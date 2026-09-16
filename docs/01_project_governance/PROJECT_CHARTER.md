# Project Charter: Sekota Web Platform (Tier M)
**Dokumen Referensi: PM-FRM-03 / ISO 21502:2020 & ISO 9001:2015**

---

## 1. Identitas & Tata Kelola Proyek

| Parameter | Rincian |
|---|---|
| **Nama Proyek** | Sekota Web Platform (Portal & B2B/B2G Intelligence Experience) |
| **Kode Proyek** | `SEKOTA-WEB-2026` |
| **Klasifikasi Tier** | **Tier M (Standar)** — Lintas modul (Web, KMP Shared, Server, Persistence, WebSocket Sync) |
| **Sponsoring Organization** | PT Sekota Sinergi Indonesia |
| **Senior Supplier** | PT eBdesk Teknologi (Work Package & Core Intelligence Provider) |
| **Product Owner (PO)** | Client / Business Lead (Agy Ahmad) |
| **Lead PM / Scrum Master** | AI Lead Project Manager / AS Agent |
| **Lead Developer** | AS Agent & Core Contributors |
| **Branch Aktif** | `sprint-3` (Tracking `origin/sprint-3`) |
| **Repositori** | Git Private: `Harits/sekota` |

---

## 2. Latar Belakang & Tujuan Proyek (Strategic Fit)

### 2.1 Problem Statement
- Pengambilan keputusan strategis di sektor B2B/B2G kerap terhambat oleh silo data, antarmuka kaku, dan beban pelaporan manual (ESG & kepatuhan publik).
- Sekota memerlukan portal web modern berstandar enterprise dengan performa tinggi (Kotlin Multiplatform/Wasm), keamanan level ISO/IEC 27001:2022, dan identitas visual editorial berwibawa (*Trusted Intelligence*).

### 2.2 Tujuan Proyek (Project Goals)
1. Membangun portal web responsif berbasis **Compose Multiplatform (Wasm & JS)** yang menyajikan landing page, katalog pengetahuan digital, dan intelligence suite.
2. Mengintegrasikan shared logic Clean Architecture (`shared/`) untuk autentikasi, manajemen profil, persistensi lokal (SQLDelight), dan sinkronisasi real-time.
3. Menyediakan backend terpadu berbasis **Ktor Server** dengan persistensi data relasional (Exposed ORM & PostgreSQL), Swagger/OpenAPI, dan WebSocket sync.
4. Menerapkan tata kelola manajemen proyek berbasis SMT Sekota (Harmonisasi ISO 21502, ISO 9001, ISO 27001, PMBOK 8 X3).

---

## 3. Ruang Lingkup Proyek (Scope Baseline)

### Termasuk (In-Scope):
- **Web Frontend (`composeApp`)**:
  - Landing Page lengkap (Hero, Feature Grid, Intelligence Suite: Veridia, Ascendio, Sociara, Ecoflow, Value Loop, Value Prop, E-Book Promo, Contact, Footer).
  - Sistem Navigasi & Filter Sidebar katalog.
  - Halaman Katalog Digital & Detail Buku (*Book Details*).
  - Halaman Katalog Merchandise.
  - Layar Autentikasi (Login & Registrasi Pengguna) dan Profil Pengguna.
- **Shared Business Logic (`shared`)**:
  - Fitur Autentikasi (Entities, Use Cases, Ktor Network Client, Token Storage).
  - Fitur Profil Pengguna (Entities, Use Cases, Repository).
  - Persistensi Lokal SQLDelight (`SekotaDatabase`) untuk token sesi dan cache.
  - WebSocket Sync Client untuk sinkronisasi state UI real-time.
- **Server Backend (`server`)**:
  - Netty Engine binding pada port 8080.
  - Ktor Auth JWT plugin untuk perlindungan endpoint.
  - Exposed ORM + PostgreSQL untuk persistensi user credentials dan profile data.
  - WebSocket Server (`/ws/sync`) dan Static Web Assets Hosting (`/web`).
  - Dokumentasi Swagger UI (`/swagger`) dan OpenAPI (`/openapi`).
- **DevOps & Containerization**:
  - Containerization multi-stage Docker & Podman Compose.

### Di Luar Lingkup (Out-of-Scope):
- Payment Gateway integrasi pihak ketiga untuk transaksi checkout langsung (dialokasikan ke rilis mendatang).
- Migrasi database legacy skala besar milik instansi eksternal.

---

## 4. Batas Toleransi Proyek (Tolerance Thresholds)

Sesuai ketentuan SMT Sekota:
- **Ambang Waktu**: Deviasi sprint $\le 3$ hari kerja diselesaikan secara otonom oleh Scrum Master.
- **Ambang Biaya & Effort**: Re-alokasi story point internal disetujui Scrum Master dan PO. Deviasi kapasitas $\ge 25\%$ memicu eskalasi Direksi.
- **Ambang Mutu & Keamanan**: Zero tolerance untuk celah keamanan kritis (OWASP Top 10), data leakage (`SMT-IK-04`), dan ketiadaan preview `@Preview(device = DESKTOP)`.

---

## 5. Otorisasi & Tanda Tangan Gerbang G1

| Peran | Nama | Keputusan | Tanggal |
|---|---|---|---|
| **Product Owner** | Agy Ahmad | **APPROVED (GATE 1 PASSED)** | 2026-05-12 |
| **Lead PM / SM** | Antigravity AI | **ACCEPTED** | 2026-05-12 |
