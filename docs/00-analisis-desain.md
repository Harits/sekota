# 00 — Analisis Desain Sumber

Sumber: `C:\Users\eBdesk Teknologi\Downloads\sekota-landing-page (1)\sekota-landing-page`
(situs statis HTML + CSS + JS, tanpa build step, dua bahasa: `*.html` dan `en/*.html`).

## 1. Inventaris halaman

| Halaman | File | Isi utama |
|---|---|---|
| Landing | `index.html` / `en/index.html` | Hero, marquee sektor, Tentang, 4 Layanan, Cara Kerja, 4 Platform, 6 Buku, CTA Mitra, FAQ, Footer |
| Produk | `clarivio.html`, `ascendio.html`, `sociara.html`, `evidara.html` (+ `en/`) | Hero produk, masalah, 6 fitur, galeri tab, alur 4 langkah, audiens, platform lain, CTA |
| Wawasan | `buku.html` (+ `en/`) | Hero + indeks 6 buku, 6 blok detail buku, trilogi Sustainability Series, CTA |
| Kontak | `kontak.html` (+ `en/`) | Hero + 3 langkah, formulir (nama, email, topik, pesan), status "Pesan terkirim", email langsung |
| Privasi | `privasi.html` (+ `en/`) | Hero legal, daftar isi 10 bagian, kebijakan data sesuai UU PDP (draf) |
| Syarat | `syarat-ketentuan.html` (+ `en/`) | Hero legal, daftar isi 12 bagian, ketentuan penggunaan (draf) |
| Aset | `assets/`, `assets/products/` | Logo Sekota/produk (WebP transparan), tangkapan dasbor 1064×592, sampul buku 360×496, tangkapan produk 1440×900 |

Font dari Google Fonts: **Plus Jakarta Sans** 400/500/600/700 dan **IBM Plex Mono** 400/500.

## 2. Design token (dari CSS `:root`)

| Token | Nilai | Pemakaian |
|---|---|---|
| `--ink` | `#0D1F2D` | Teks utama, judul |
| `--ink-2` | `#4B6474` | Teks sekunder / lead |
| `--muted` | `#52707F` | Caption, nomor |
| `--brand-ink` | `#077C8E` | Overline, tautan brand |
| `--bg` | `#FFFFFF` | Latar utama |
| `--subtle` | `#F6F9FB` | Band selang-seling |
| `--muted-bg` | `#EDF2F5` | Chip netral, toggle bahasa |
| `--line` | `#DFE6EB` | Border kartu/input |
| `--line-2` | `#EDF2F5` | Garis halus, border tile |
| `--dark` | `#06131D` | Band gelap, footer |
| `--dark-2` | `#0D1F2D` | Gradien band gelap |
| `--on-dark-muted` | `#9FB3C1` | Teks di atas gelap |
| `--g1` | `#60BD65` | Gradien hijau |
| `--g2` | `#02B6CF` | Gradien sian |
| `--grad` | `linear-gradient(120deg, #60BD65, #02B6CF)` | Tombol primer, teks gradien, garis aksen |
| `--r` | `20px` | Radius kartu layanan |
| `--max` | `1200px` | Lebar kontainer |
| `--ease` | `cubic-bezier(.2,.7,.2,1)` | Easing animasi |

Tipografi: hero `clamp(36px, 4.1vw, 56px)`, H2 `clamp(30px, 3.4vw, 40px)`, H3 20/28, body 16/1.6, lead 18/30, mono overline 12/16 letter-spacing .13em.

## 3. Struktur landing page

1. **Nav fixed** 80px: logo, 6 anchor (Tentang, Layanan, Cara Kerja, Platform, Wawasan, FAQ), toggle ID/EN, CTA.
2. **Hero**: chip eBdesk, judul dua baris (baris 2 gradien), lead, 2 CTA, statistik 4/6 (count-up), kartu *Sekota Intelligence Suite* dengan 4 baris platform yang bergantian menyala, latar grid + glow.
3. **Marquee sektor** (6 sektor, loop tak terbatas, tepi memudar).
4. **Tentang**: 2 kolom; kiri overline/judul/lead, kanan 3 kartu alasan.
5. **Layanan** (band subtle): 4 kartu (ikon, nomor, judul, tagline, 3 poin centang, pill platform), baris "Juga tersedia".
6. **Cara Kerja**: garis progres 4 titik, 4 kartu tahap, banner "Bentuk kerja sama" + chip.
7. **Platform** (band gelap): header + badge teknologi, 4 tile 2×2 berisi logo, deskripsi, tangkapan dasbor.
8. **Wawasan**: 6 kartu buku (sampul, judul, tagline panah, ringkasan, tautan detail).
9. **CTA Mitra**: banner gradien dengan cincin dekoratif + tombol.
10. **FAQ** (band subtle): kiri judul + kartu email, kanan akordeon 7 pertanyaan (satu terbuka).
11. **Footer** gelap: deskripsi perusahaan, 3 kolom tautan, baris copyright/legal.

## 4. Breakpoint sumber

| Media query | Perubahan |
|---|---|
| `max-width:1100px` | Hero 1 kolom; grid 4→2; buku 6→3; Tentang & FAQ 1 kolom; progres disembunyikan |
| `max-width:860px` | Nav link + CTA disembunyikan, burger muncul; menu dropdown; platform 1 kolom; footer 2 kolom |
| `max-width:600px` | Section padding 112→72; kartu 1 kolom; buku 3→2; hero visual menumpuk; footer 1 kolom |

Implementasi Compose memakai `WindowSize`: `Compact <600`, `Medium <860`, `Wide <1100`, `Expanded ≥1100`. Hero, Tentang, FAQ, formulir kontak, hero/galeri produk, dan blok buku menumpuk di bawah 1100 (mengikuti desain); nav runtuh di bawah 860; lebar 900dp diverifikasi lewat screenshot.

## 5. Interaksi yang diporting

| Interaksi | Implementasi Compose |
|---|---|
| Nav solid saat scroll + anchor aktif | `snapshotFlow` pada `LazyListState` → `AppState.chromeSolid` / `activeAnchor` |
| Menu mobile | state lokal header + `AnimatedVisibility` |
| Baris suite bergantian menyala (2,6 detik) | `LandingViewModel.highlightNextRow()` dipicu `LaunchedEffect` |
| Count-up statistik | `Animatable` + `LaunchedEffect` |
| Reveal saat masuk viewport | `Reveal` (alpha + translasi) pada item `LazyColumn` |
| Marquee | `rememberInfiniteTransition` + offset baris terduplikasi |
| Hover lift kartu | `MutableInteractionSource.collectIsHoveredAsState` |
| Akordeon FAQ satu terbuka | `LandingViewModel.toggleFaq` + `AnimatedVisibility` |
| Galeri tab produk | `ProductViewModel.selectScreen` |
| Toggle bahasa | `AppState.language`; seluruh copy dari `LText` |
| Formulir kontak | validasi murni → draft → POST `/api/contact` → SMTP; sukses hanya setelah server menerima |
| Preset deep-link (`demo-*`, `mitra-awal`, `buku-*`) | `ContactCatalog.prefill` → `ContactViewModel.prefill` |

## 6. Pemetaan aset

- Logo WebP transparan → PNG (`logo_sekota_light/dark`, 8 logo produk).
- Tangkapan dasbor & produk + sampul buku → JPEG kualitas 90 (total drawable ±1,8 MB).
- Font Google → TTF yang dibundel di `composeResources/font` (Plus Jakarta Sans 400–700, IBM Plex Mono 400/500).
- Nama resource Compose: huruf kecil + underscore (`logo_clarivio_light`, `dash_clarivio`, `cover_1`, `shot_clarivio_1`, dst.).

## 7. Keputusan porting & deviasi

1. **Formulir kontak memakai API + SMTP.** Desain menyerahkan `sendMessage(data)` ke tim developer; implementasinya adalah `POST /api/contact` di modul `:server` (Jakarta Mail ke `info@sekota.id`). Status sukses hanya tampil setelah server menerima pesan; kegagalan menampilkan error dan email langsung. Kredensial SMTP tidak disimpan di repo.
2. **Routing** memakai router internal (`AppNavigator`) + hash routing web (`#/id/...`, `#/en/...`), sehingga deep link, refresh, dan browser back/forward bekerja.
3. **Animasi** reveal/hover/count-up diporting; blur backdrop nav diganti overlay putih 97% karena dukungan blur lintas target tidak seragam.
4. **Marquee** memakai ikon vektor hasil tracing; tampilan identik secara semantik.
5. **Halaman buku** diakses lewat kartu buku (anchor), sesuai perilaku tautan sumber `buku.html#anchor`.
6. **Bahasa Inggris** memakai copy dari `en/*.html`; istilah yang di sumber tetap Inggris (mis. "Early Partner Program") dipertahankan.
7. **Footer** menautkan ke halaman Privasi dan Syarat & Ketentuan yang diporting dari desain baru (`#/id/privacy`, `#/id/terms`).
8. **Halaman produk** berbagi komponen yang sama; konten per produk dipisahkan di `ProductCatalog`.
9. **Detail buku** mengikuti tipografi, kutipan, CTA, dan layout cover 340/150/120 desain; cover belum benar-benar *sticky* saat scroll (backlog polish).

## 8. Matriks kesetiaan

| Elemen | Status | Catatan |
|---|---|---|
| Warna & tipografi | ✅ | Token dan font sama; ukuran hero mengikuti clamp versi desktop/mobile |
| Layout & breakpoint | ✅ | 4 breakpoint setara media query sumber |
| Section landing | ✅ | Semua 11 bagian hadir |
| 4 halaman produk | ✅ | Struktur & konten dari masing-masing HTML |
| Halaman buku | ✅ | 6 buku + trilogi + CTA |
| Formulir kontak | ✅/⚠️ | Validasi & status sukses sama; pengiriman butuh kredensial SMTP/endpoint di produksi |
| Animasi | ✅/⚠️ | Reveal, hover, marquee, count-up, reduced-motion; parallax grid & tilt 3D belum |
| URL per halaman | ✅ | Hash routing, deep link, browser back/forward; termasuk `#/id/privacy` dan `#/id/terms` |
| SEO/SSR | ⚠️ | Meta dasar ada; Compose Wasm merender di canvas, SSR/prerender belum |
