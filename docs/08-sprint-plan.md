# 08 — Sprint Plan

Metode: sprint satu minggu (dapat dikompres menjadi beberapa hari), hasil setiap sprint harus dapat dijalankan lokal. OpenProject belum disinkronkan; rencana ada di [10-openproject-sync.md](10-openproject-sync.md).

## Sprint 0 — Discovery & dokumentasi

**Tujuan:** memahami desain sumber dan menetapkan arah teknis.

| Deliverable | Status |
|---|---|
| Analisis desain, token, breakpoint, interaksi | ✅ `docs/00-analisis-desain.md` |
| Requirement fungsional/non-fungsional | ✅ `docs/01-requirements.md` |
| Use case diagram & spesifikasi | ✅ `docs/02`, `docs/03` |
| Arsitektur & ADR | ✅ `docs/04` |
| Design system | ✅ `docs/05` |
| Flow diagram | ✅ `docs/06` |
| Test strategy | ✅ `docs/07` |
| Ekstraksi konten ID/EN & konversi aset | ✅ `tools/extract_content.py`, `tools/convert_assets.py` |

**Definition of Done:** dokumen disetujui; tidak ada pertanyaan terbuka yang menghambat implementasi.

## Sprint 1 — Fondasi & design system

**Tujuan:** kerangka aplikasi yang bisa dijalankan di web dengan token dan komponen inti.

| Deliverable | Status |
|---|---|
| Tema (warna, tipografi, spacing, radius, shape) | ✅ `core/designsystem` |
| Font Plus Jakarta Sans + IBM Plex Mono dibundel | ✅ |
| Komponen: button, chip, overline, section header, icon tile, card, reveal, band, browser frame, CTA | ✅ |
| Ikon vektor dari desain | ✅ |
| Breakpoint `WindowSize` + `ProvideWindowSize` | ✅ |
| Header, footer, bahasa ID/EN | ✅ |
| Router internal + `AppState` | ✅ |
| DI Koin + modul | ✅ |
| Aset gambar dikonversi & dipetakan | ✅ |

**DoD:** `:app:shared:compileKotlinJvm` & `:app:webApp:wasmJsBrowserDistribution` sukses.

## Sprint 2 — Landing page

**Tujuan:** seluruh 11 section landing sesuai desain dan responsif.

| Deliverable | Status |
|---|---|
| Hero + kartu suite berotasi + count-up | ✅ |
| Marquee sektor | ✅ |
| Tentang, Layanan, Cara Kerja | ✅ |
| Platform (band gelap, 4 tile) | ✅ |
| Wawasan (6 buku) | ✅ |
| CTA Mitra | ✅ |
| FAQ akordeon | ✅ |
| Footer 4 kolom | ✅ |
| Nav solid, anchor aktif, menu mobile | ✅ |
| Screenshot 1440 & 390 | ✅ `docs/screenshots/home-*` |

**DoD:** tidak ada overflow horizontal; semua section tampil; test landing & konten hijau.

## Sprint 3 — Halaman produk, buku, kontak

**Tujuan:** melengkapi halaman selain landing dan alur kontak.

| Deliverable | Status |
|---|---|
| 4 produk dengan konten masing-masing | ✅ |
| Galeri tab + CTA preset demo | ✅ |
| Halaman buku: hero, indeks, 6 blok, trilogi, CTA | ✅ |
| Halaman kontak: form, validasi, API + SMTP, status sukses/gagal, reset | ✅ |
| Preset `demo-*`, `mitra-awal`, `buku-*` | ✅ |
| Footer di semua halaman | ✅ |
| Screenshot produk/kontak/buku/EN | ✅ |

**DoD:** semua rute dapat diakses dari UI; form valid & invalid diuji; konten ID/EN lengkap.

## Sprint 4 — Test & hardening

**Tujuan:** laporan otomatis dan kestabilan lintas target.

| Deliverable | Status |
|---|---|
| Unit test domain & view model | ✅ 69 test non-UI (JVM) |
| UI test Compose | ✅ 8 test |
| Test Wasm | ✅ 69 test |
| Test server (health + kontak + FormSubmit) | ✅ 6 test |
| Skrip laporan Markdown | ✅ `scripts/test-report.ps1` |
| Build Android + Desktop | ✅ `assembleDebug`, `compileKotlin` |
| Verifikasi visual otomatis + assertion `data-route` | ✅ Puppeteer scripts |
| Hash routing / deep link / browser back (FR-23) | ✅ |
| Preset `mitra-awal` + per-book CTA (FR-17) | ✅ |
| Keyboard scroll, focus ring, form/menu semantics, `lang` | ✅ |
| `prefers-reduced-motion` | ✅ |
| Perbaikan container 1200px, nav centering, suite card, equal-height services, progress rail, footer | ✅ |

**DoD:** semua test hijau; build web/Android/desktop sukses; screenshot final tersimpan.

## Sprint 5 — Fidelity round 2 & pengiriman kontak (selesai)

**Tujuan:** menutup temuan UI/UX klien dan mengganti alur `mailto:` dengan pengiriman nyata.

| Deliverable | Status |
|---|---|
| Marquee memakai 6 ikon sektor filled 256×256 + gradien | ✅ |
| Hover tile rotasi −8°/skala 1,06 pada alasan, layanan, tahap | ✅ |
| Layanan: lift 6dp + border gradien 1dp | ✅ |
| Header: toggle ID/EN 34dp, underline dari tengah, focus ring hanya keyboard | ✅ |
| Tombol: gradien bergeser, sheen, tekan 0,98, shadow | ✅ |
| Sampul buku: tilt pointer, shine, shadow, spine | ✅ |
| Banner CTA: gradien 4 stop beranimasi + cincin pulse | ✅ |
| API `POST /api/contact` + SMTP + status terkirim/gagal | ✅ |
| Halaman Privasi & Syarat Ketentuan (ID/EN, TOC, 10/12 bagian) | ✅ |
| Test: 77 JVM + 69 Wasm + 6 server (152) | ✅ |
| Verifikasi visual hover + submit kontak nyata (API 202, email masuk SMTP sink) | ✅ |

**DoD:** semua test hijau; screenshot hover/kontak tersimpan; tidak ada status sukses palsu.

## Sprint 5b — UX fidelity round 3 (selesai)

**Tujuan:** menutup temuan UX terakhir pada galeri produk, daftar isi legal, tautan "Baca juga", dan kartu email langsung.

| Deliverable | Status |
|---|---|
| Tur otomatis galeri produk tiap 6 detik; berhenti saat tab diklik; nonaktif dengan reduced motion | ✅ |
| Daftar isi Privasi/Syarat sticky saat scroll, tetap tampil sampai footer, tidak pernah di bawah header; pindah ke kartu statis di mobile | ✅ |
| Tautan "Baca juga" selebar konten, tanpa ripple/abu-abu, panah bergeser saat hover | ✅ |
| Kartu email langsung: klik membuka aplikasi email (`mailto:`), tombol salin alamat + status "Tersalin" | ✅ |
| Mailer alternatif FormSubmit (`SEKOTA_FORMSUBMIT_TO`) untuk uji tanpa SMTP, dengan catatan aktivasi/Spam | ✅ |
| Perbaikan highlight TOC item terakhir + scroll-spy berbasis reading line | ✅ |
| Deployment Docker/Podman Compose untuk development, staging, produksi | ✅ |
| Test tur otomatis, FormSubmit, dan scroll-spy; total 152 test hijau | ✅ |

## Sprint 6 — Backlog (belum dikerjakan)

| ID | Item | Nilai | Estimasi |
|---|---|---|---|
| SP6-01 | Android/iOS hardware back + focus trap menu | Back konsisten di semua target | S |
| SP6-02 | Rate limit/captcha endpoint kontak + serverless deploy | Perlindungan spam & produksi statis | M |
| SP6-03 | CMS/headless content | Konten diubah tanpa rilis | L |
| SP5-04 | Sinkronisasi OpenProject (work packages, sprint, status) | Tata kelola proyek | S |
| SP5-05 | SEO/metadata & SSR strategy untuk Compose Web | Ditemukan mesin pencari | L |
| SP5-06 | Audit aksesibilitas lanjutan (kontras gradien, heading roles, screen reader) | Kepatuhan & inklusi | S |
| SP5-07 | Code splitting & optimasi bundle Wasm | Waktu muat | M |
| SP5-08 | Screenshot diff baseline di CI | Deteksi regresi visual | M |
| SP5-09 | iOS & Android visual/interaction test | Keyakinan multiplatform | L |
| SP5-10 | Analytics & consent | Pengukuran | M |
| SP5-11 | Migrasi `LocalClipboardManager` → `LocalClipboard` | Hilangkan deprecation | XS |
| SP5-12 | Halaman Privasi & Syarat | Legal | S |

## Risiko & mitigasi

| Risiko | Prob. | Dampak | Mitigasi |
|---|---|---|---|
| Bundle Wasm besar | Tinggi | Muat lambat | Kompresi hosting, lazy asset, code splitting |
| Perbedaan rendering antar target | Sedang | UI tidak konsisten | Screenshot per target, test UI Compose |
| Konten berubah tanpa proses | Sedang | Rilis berulang | Rencana CMS di Sprint 5 |
| SMTP/API belum dikonfigurasi di produksi | Sedang | Lead hilang | UI menampilkan error + email langsung; siapkan kredensial & rate limit |
| OpenProject belum sinkron | Rendah | Status proyek tidak terpusat | Dokumen sprint siap diimpor |

## Ritme kerja

1. **Plan:** pilih item backlog, tulis acceptance criteria.
2. **Build:** implementasi domain → data → presentation → test.
3. **Verify:** `scripts/test-report.ps1 -IncludeWasm` + build web + screenshot.
4. **Review:** bandingkan screenshot dengan desain; catat deviasi.
5. **Close:** perbarui dokumen, laporan test, dan status sprint.
