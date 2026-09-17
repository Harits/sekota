# Project Control Book & Gate Registry
**Kode Dokumen: PM-REG-01 / SMT Sekota (Harmonisasi ISO 21502 & PMBOK 8)**

Proyek: **Sekota Web Platform (`SEKOTA-WEB-2026`)**  
Tier: **Tier M (Standar)** | Branch: **`sprint-3`** | Lead PM: **Antigravity AI**

---

## 1. Log Siklus Hidup Gerbang (Phase Gate Lifecycle Log)

| Gerbang | Nama Gerbang / Fase SMT | Tanggal Review | Status | Catatan Keputusan / Bukti Artefak |
|---|---|---|---|---|
| **G0** | Kualifikasi Peluang (Pitching) | 2026-05-02 | **PASSED (WON)** | Solution Brief & Arsitektur Wasm disahkan (`PM-FRM-01`, `PM-FRM-02`). |
| **G1** | Otorisasi Piagam & Handover BD $\rightarrow$ Ops | 2026-05-12 | **PASSED** | `PROJECT_CHARTER.md` & `DEFINITION_OF_DONE.md` disahkan (`PM-FRM-03`). |
| **G2** | Baseline Lingkup & Spesifikasi (Fase 2) | 2026-05-15 | **PASSED** | `FUNCTIONAL_SPECIFICATION_DOCUMENT.md`, `DESIGN_SYSTEM.md`, & Use Case PUML disahkan. |
| **G2.5** | Purwarupa Interaktif (Fase 3 & Sprint 0) | 2026-05-19 | **PASSED** | Clickable Compose UI (`BookDetailsScreen`, `CatalogScreen`, `ProductCard`) tervalidasi dengan adaptive breakpoints. |
| **G3** | Kesiapan Terima (Sprint 1-3 Review - Fase 4) | 2026-09-16 | **ACCEPTED** | 100% story points selesai (43/43 SP). Sprint 1-3 Closed & merged ke master. |
| **G3.1** | Phase 1 Refinement (Sprint 4 - CMS Tri-Platform & Auth-Gate) | 2026-09-17 | **PASSED (100%)** | Master book source terhubung ke `bookinteractiontool` (`/api/v1/admin/books` & `/api/v1/dashboard/summary`), CMS Web Metadata Enrichment Dialog (`✏️ Kelola Tampilan Web`), rating bintang pembaca, reader count telemetry, dynamic category filtering, chapters/syllabus, dan auth-gated PDF download terverifikasi & teruji. |
| **G4** | Serah Terima & BAST (Fase 5 UAT) | 2026-06-20 | **READY FOR UAT** | Seluruh deliverables Sprint 1-4 rampung. Siap pengujian penerimaan pengguna. |
| **G5** | Tinjauan Realisasi Manfaat (Fase 6 Harvest) | TBD (Post Go-Live) | **PLANNED** | Evaluasi adopsi portal klien & konversi konsultasi. |

---

## 2. Risk & Issue Register

| ID | Kategori | Deskripsi Risiko / Isu | Prob | Dampak | Strategi Mitigasi / Status |
|---|---|---|---|---|---|
| **RSK-01** | Teknis | Kompilasi Wasm Docker memerlukan pra-kompilasi aset `web-dist` pada host | Sedang | Sedang | Otomasi via script `run-local-podman.sh` (**Closed**). |
| **RSK-02** | Desain | Deviasi token visual pada Auth & Profile screen dari Figma `0:1` | Rendah | Sedang | Dimonitor via `WP-021`, ditugaskan ke Agy Ahmad (**In-Progress**). |
| **RSK-03** | Mutu | Ketiadaan desktop preview pada Auth/Profile karena ketergantungan Use Case | Rendah | Rendah | Tambahkan mock default parameter pada composables (**Open**). |

---

## 3. Assumption Register & Change Log

- **ASM-01**: Target browser utama mendukung Kotlin/Wasm Garbage Collection (Chrome 119+, Firefox 120+, Safari 18+). Fallback `js` disediakan untuk browser warisan.
- **CHG-01**: Penambahan target Android namespace `com.sekota` di `composeApp/build.gradle.kts` diselaraskan ke dalam arsitektur multiplatform terpadu.
