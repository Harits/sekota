# Project Control Book & Gate Registry
**Kode Dokumen: PM-REG-01 / SMT Sekota (Harmonisasi ISO 21502 & PMBOK 8)**

Proyek: **Sekota Web Platform (`SEKOTA-WEB-2026`)**  
Tier: **Tier M (Standar)** | Branch: **`sprint-3`** | Lead PM: **Antigravity AI**

---

## 1. Log Siklus Hidup Gerbang (Phase Gate Lifecycle Log)

| Gerbang | Nama Gerbang | Tanggal Review | Status | Catatan Keputusan / Bukti Artefak |
|---|---|---|---|---|
| **G0** | Kualifikasi Peluang (Pitching) | 2026-05-02 | **PASSED (WON)** | Solution Brief & Arsitektur Wasm disahkan. |
| **G1** | Otorisasi Piagam & Handover BD $\rightarrow$ Ops | 2026-05-12 | **PASSED** | `PROJECT_CHARTER.md` & `DEFINITION_OF_DONE.md` disahkan. |
| **G2** | Baseline Lingkup & FSD | 2026-05-15 | **PASSED** | `FUNCTIONAL_SPECIFICATION_DOCUMENT.md` & Use Case PUML disahkan. |
| **G3** | Kesiapan Terima (Sprint 1-3 Review) | 2026-09-15 | **CONDITIONALLY ACCEPTED** | 88.4% story points selesai. Menunggu finalisasi WP-021. |
| **G4** | Serah Terima & BAST | TBD (Sprint 3 End) | **PENDING** | Dijadwalkan pasca penyelesaian WP-021 & verifikasi UAT. |
| **G5** | Tinjauan Realisasi Manfaat | TBD (Post Go-Live) | **PLANNED** | Evaluasi adopsi portal klien & konversi konsultasi. |

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
