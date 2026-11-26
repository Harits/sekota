# 05 — Design System

Sumber kebenaran implementasi: `core/designsystem/` (`SekotaColors.kt`, `SekotaTheme.kt`, `SekotaComponents.kt`, `Surfaces.kt`, `SekotaIcons.kt`) dan `core/ui/WindowSize.kt`.

## 1. Warna

| Token Compose | Hex | Pemakaian |
|---|---|---|
| `SekotaColors.Ink` | `#0D1F2D` | Teks utama, judul, ikon nav aktif |
| `Ink2` | `#4B6474` | Lead, teks sekunder |
| `Muted` | `#52707F` | Caption, nomor, placeholder footer |
| `BrandInk` | `#077C8E` | Overline, tautan brand |
| `Bg` | `#FFFFFF` | Latar halaman/kartu |
| `Subtle` | `#F6F9FB` | Band selang-seling, kartu row suite |
| `MutedBg` | `#EDF2F5` | Chip netral, toggle bahasa |
| `Line` | `#DFE6EB` | Border kartu/input |
| `Line2` | `#EDF2F5` | Garis halus, grid hero |
| `Dark` | `#06131D` | Band gelap, footer |
| `Dark2` | `#0D1F2D` | Gradien band gelap |
| `OnDarkMuted` | `#9FB3C1` | Teks di atas gelap |
| `Green` | `#60BD65` | Ujung gradien hijau |
| `Cyan` | `#02B6CF` | Ujung gradien sian, fokus, aksen |
| `Teal` | `#0298AD` | Ikon pada tile |
| `Success`/`SuccessInk`/`SuccessBg` | `#43A34A` / `#2E7D32` / `#E7F5E8` | Checklist, tag hijau |
| `CyanBg` | `#E6F8FB` | Tag sian |
| `Required` | `#C2410C` | Asterisk & error |

Brush:
- `BrandGradient` = linear 120°, Green → Cyan.
- `BrandGradientSoft` = sama dengan alpha 12% (latar tile, pill layanan).
- `DarkGradient` = Dark → `#0F2A3B`.
- `CtaGradient` = Green → Cyan → `#2FBA9A` → Green (banner CTA).

Kontras: Ink/Bg = 14,7:1; Ink2/Bg ≈ 6,1:1; OnDarkMuted/Dark ≈ 7,5:1; Cyan pada putih dipakai untuk aksen/besar, bukan teks kecil.

## 2. Tipografi

| Style | Family | Size / line | Weight | Pemakaian |
|---|---|---|---|---|
| `hero` | Plus Jakarta Sans | 56/64 (compact 36/42) | SemiBold | H1 landing |
| `headline` | Plus Jakarta Sans | 40/48 (compact 30/36) | SemiBold | H2 section, H1 buku |
| `h3` | Plus Jakarta Sans | 20/28 | SemiBold | Judul kartu |
| `h4` | Plus Jakarta Sans | 17/24 | SemiBold | Judul kecil, FAQ |
| `body` | Plus Jakarta Sans | 16/25.6 | Normal | Paragraf |
| `lead` | Plus Jakarta Sans | 18/30 | Normal | Lead section |
| `small` | Plus Jakarta Sans | 14/22 | Normal | Teks kartu |
| `caption` | Plus Jakarta Sans | 13/18 | Normal | Label, tag |
| `nav` | Plus Jakarta Sans | 14/20 | Medium | Link nav |
| `button` | Plus Jakarta Sans | 15/20 | SemiBold | Tombol |
| `monoOverline` | IBM Plex Mono | 12/16, tracking 1.56sp | Medium | Overline uppercase |
| `monoSmall` | IBM Plex Mono | 13/18 | Normal | Meta, copyright |
| `number` | IBM Plex Mono | 13/18 | Normal | Nomor kartu |
| `bigNumber` | Plus Jakarta Sans | 40/48, tracking -1.2 | SemiBold | Nomor tahap |

Skala responsif mengikuti `clamp()` desain:

| Style | Compact | Medium | Wide | Expanded |
|---|---|---|---|---|
| Hero H1 | 36/42 | 36/42 | 40/46 | 56/64 |
| Headline H2 | 30/36 | 30/36 | 32/38 | 40/48 |
| H1 produk | 32/38 | 32/38 | 34/40 | 46/52 |

Huruf memakai `FontWeight.Normal/Medium/SemiBold/Bold` dengan 4 file Plus Jakarta Sans dan 2 file IBM Plex Mono. `responsiveHeadlineStyle()` menjadi helper untuk H2.

## 3. Spacing & layout

| Token | Nilai |
|---|---|
| `xxs` | 4dp |
| `xs` | 8dp |
| `sm` | 12dp |
| `md` | 16dp |
| `lg` | 20dp |
| `xl` | 24dp |
| `xxl` | 32dp |
| `xxxl` | 40dp |
| `huge` | 56dp |
| `section` | 112dp (72dp compact) |
| `navHeight` | 80dp |
| `maxContentWidth` | 1200dp |
| `gutter` | 24dp (20dp compact) |

Breakpoint (`WindowSize`): `Compact <600`, `Medium <860`, `Wide <1100`, `Expanded ≥1100`.

| Elemen | Expanded | Wide/Medium | Compact |
|---|---|---|---|
| Nav links | tampil | tampil bila ≥860 | burger |
| Hero | 2 kolom (1.2 : 1) | 1 kolom | 1 kolom |
| Layanan/Cara kerja/Fitur | 4 / 3 kolom | 2 kolom | 1 kolom |
| Platform | 2 kolom | 2 kolom | 1 kolom |
| Buku | 6 kolom | 3 kolom | 2 kolom |
| FAQ/Tentang | 2 kolom | 1 kolom | 1 kolom |
| Footer | 4 kolom | 4 kolom | 1 kolom |

## 4. Radius, elevasi, border

| Token | Nilai |
|---|---|
| `xs` | 7dp |
| `sm` | 10dp |
| `md` | 12dp |
| `lg` | 16dp |
| `xl` | 20dp |
| `xxl` | 24dp |
| `pill` | 50% |

- Kartu layanan: radius 20, border 1 `Line`; hover lift 6dp + border gradien 1dp, tile ikon rotasi −8° dan skala 1,06.
- Kartu alasan & tahap: hover border sian 45%, kartu bergeser/lift, tile ikon rotasi −8° dan skala 1,06.
- Kartu suite: radius 24, shadow 24dp (ink 18%), border `Line`.
- Tombol: radius 12, tinggi 52 (44 small), shadow sian untuk primer.
- Input: radius 10, border `Line`; error `Required`.
- Band gelap: radius 0; tile produk radius 24.

## 5. Komponen

| Komponen | File | Catatan |
|---|---|---|
| `ContentWrap` | `SekotaComponents.kt` | Kontainer 1200dp + gutter; dipakai semua section |
| `SekotaButton` | idem | Primary: gradien bergeser 160% + sheen diagonal + shadow sian + tekan 0,98; Secondary: putih, border gelap saat hover, lift 1dp; ikon trailing 3dp saat hover |
| `SekotaChip` | idem | Pill dengan dot gradien; `dark` untuk band gelap |
| `Overline` | idem | Mono uppercase, warna brand/di atas gelap |
| `SectionHeader` | idem | Overline + judul + subtitle, max 680dp |
| `IconTile` | idem | 48/44dp, latar gradien 12%, border line2 |
| `GradientText` | idem | Teks gradien (baris kedua hero) |
| `Reveal` | idem | Alpha + translasi 16dp, durasi 700ms, delay bertingkat |
| `SekotaCard` | idem | Kartu putih + hover lift + border sian |
| `SectionBand` | `Surfaces.kt` | Band full-width; varian `subtle`, `dark` (glow radial) |
| `BrowserFrame` | idem | Bingkai browser 34dp + 3 dot + URL + gambar |
| `CtaBanner` | idem | Gradien 4 stop beranimasi 14s + 3 cincin pulse (520/360/200dp, pusat 120dp dari kanan); tombol putih 220dp; statis saat reduced motion |
| `SiteHeader` | `app/chrome/SiteHeader.kt` | Fixed 80dp, nav space-between, underline gradien tumbuh dari tengah, toggle ID/EN 34dp, CTA 44dp, focus ring hanya mode keyboard |
| `SiteFooter` | `app/chrome/SiteFooter.kt` | 4 kolom responsif, copyright/legal |
| `FaqRow` | `landing/.../BooksPartnerFaqSections.kt` | Akordeon dengan role/state, border sian saat terbuka, ikon plus rotasi 45° |
| `FormField` / `TopicField` | `contact/presentation/ContactScreen.kt` | `BasicTextField` kustom, error, placeholder, dropdown topik |
| `SuccessPanel` | idem | Tile centang gradien 48dp, "Pesan terkirim.", nama/email pengunjung, tombol "Kirim pesan lain" (tanpa preview/mailto) |
| `SekotaIcons` | `SekotaIcons.kt` | ±30 ikon stroke 1.75 + 6 ikon sektor filled gradient |
| Book detail | `feature/books/presentation/BooksScreen.kt` | Cover 340dp (150/120 di tablet/mobile), author di bawah cover, kutipan panel sian, body 17/29, box heading mono uppercase, CTA primer + hint |
| `LegalScreen` | `feature/legal/presentation/LegalScreen.kt` | Hero legal, TOC 240dp di kolom kiri (kartu di mobile), dokumen 720dp, heading bernomor mono, bullet gradien, tepi kiri 2dp aktif; TOC mulai di posisi alami, menempel di 108dp saat scroll, dan disembunyikan sebelum footer agar tidak bentrok |

## 6. Motion

| Interaksi | Durasi / easing |
|---|---|
| Reveal masuk | 700ms, delay 60–70ms per item |
| Hover lift kartu | 400–450ms, FastOutSlowIn |
| Tile ikon saat kartu hover | 500ms, rotasi −8°, skala 1,06 |
| Tombol hover | gradien 500ms, sheen 800ms, lift 300ms, panah 3dp; tekan 0,98 dalam 140ms |
| Sampul buku hover | tilt pointer rotY ±10°/rotX ±8°, lift 6dp 150ms, shine 1000ms, shadow 600ms |
| Kartu fitur produk | lift 4dp + border sian 400ms; tile ikon −8°/1,06 500ms |
| Tab galeri produk | hover latar putih; aktif putih + border sian + shadow halus |
| Kartu platform lain | lift 4dp + border sian 500ms; panah "Pelajari" 2dp diagonal |
| Banner CTA | gradien 14s bolak-balik; cincin pulse 6s (3s per arah) |
| Marquee | 42s linear, loop |
| Highlight suite | 2.600ms |
| Count-up | 1.200ms setelah delay 900ms |
| Akordeon FAQ | 450ms ukuran + 350ms expand |
| Menu mobile | 250ms expand + 200ms fade |

Easing global `SekotaEase = FastOutSlowInEasing` mendekati `cubic-bezier(.2,.7,.2,1)`.

Reduced motion: `LocalReducedMotion` (web membaca `prefers-reduced-motion`) mematikan marquee, highlight otomatis, count-up, dan reveal.

## 7. Aksesibilitas

- Semua tombol/link memiliki teks; ikon dekoratif `contentDescription = null`.
- Logo Sekota memakai `contentDescription = "Sekota"`; logo produk memakai nama produk.
- Form: label teks di atas field, pesan error teks merah, asterisk wajib.
- Kontras warna diuji manual (lihat tabel).
- Fokus keyboard: tombol/kartu/nav menampilkan ring 2dp; scroll keyboard (PageUp/PageDown/Home/End/Arrow) aktif di root.
- Target sentuh minimum 44×44 untuk burger, tombol small 44dp.
- Backlog: audit TalkBack/VoiceOver, heading roles, kontras gradien brand (putih pada gradien belum AA).

## 8. Aset

| Aset | Resource | Format |
|---|---|---|
| Logo Sekota terang/gelap | `logo_sekota_light` / `logo_sekota_dark` | PNG transparan |
| Logo produk ×4 (light/dark) | `logo_{product}_{light,dark}` | PNG |
| Tangkapan dasbor | `dash_{product}` | JPEG 1064×592 |
| Tangkapan produk | `shot_{product}_{n}` | JPEG 1440×900 |
| Sampul buku | `cover_1..6` | JPEG 360×496 |
| Font | `plus_jakarta_sans_{400..700}`, `ibm_plex_mono_{400,500}` | TTF |
| Ikon | `SekotaIcons` | `ImageVector` dari path SVG |

Konversi dari WebP dilakukan oleh `tools/convert_assets.py` (dokumentasi di README alat).

## 9. Prinsip UI/UX

1. Satu pesan per section; hierarki judul → lead → bukti.
2. Gradien hanya untuk aksi/aksen, bukan latar teks panjang.
3. Band gelap sebagai jeda visual (Platform, trilogi, footer).
4. Kartu memiliki hover halus di pointer device, tetap datar di sentuh.
5. Mobile: satu kolom, teks penuh (tanpa elipsis pada konten penting), target sentuh besar.
