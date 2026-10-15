# Sekota Web

Website PT Sekota Sinergi Indonesia, dibangun dari desain Figma **Sekota Landing Page** memakai **React + Vite**.

---

## 1. Menjalankan project (pertama kali)

Yang perlu di-install sekali saja di komputer:

1. **Node.js** versi 18 ke atas — unduh di https://nodejs.org (pilih versi LTS).
2. **VS Code** (editor kode) — https://code.visualstudio.com

Lalu:

```bash
# 1. Buka folder project ini di terminal (di VS Code: menu Terminal → New Terminal)

# 2. Install semua library yang dibutuhkan (sekali saja, butuh internet)
npm install

# 3. Jalankan website di komputer sendiri
npm run dev
```

Buka alamat yang muncul di terminal (biasanya **http://localhost:5173**).
Setiap kali file disimpan, browser otomatis ikut berubah.

Perintah lain:

| Perintah          | Fungsi                                                         |
| ----------------- | -------------------------------------------------------------- |
| `npm run dev`     | Menjalankan website untuk development                          |
| `npm run build`   | Membuat versi siap online di folder `dist/`                    |
| `npm run preview` | Melihat hasil `build` di komputer sebelum di-upload            |

---

## 2. Struktur folder

```
sekota-web/
├── index.html              ← Kerangka HTML + link Google Fonts
├── package.json            ← Daftar library & perintah npm
├── vite.config.js          ← Pengaturan Vite (alias "@" = folder src)
├── public/
│   └── favicon.png         ← Ikon di tab browser
└── src/
    ├── main.jsx            ← Titik awal aplikasi
    ├── App.jsx             ← Daftar halaman (routing / URL)
    │
    ├── assets/             ← Semua gambar
    │   ├── logos/          ← Logo Sekota & 4 produk
    │   ├── icons/          ← Ikon UI (panah, bintang, kontak, dll)
    │   └── images/         ← Foto buku, sampul, merchandise
    │
    ├── styles/
    │   ├── tokens.css      ← ★ DESIGN TOKENS: warna, font, radius, shadow, spacing
    │   └── globals.css     ← Reset + class umum (.container, .section, .grad-text)
    │
    ├── data/               ← ★ ISI KONTEN (teks, daftar produk, buku, dll)
    │   ├── site.js         ← Menu navigasi, kontak, link footer
    │   ├── products.js     ← 4 produk Intelligence Suite
    │   ├── catalog.js      ← E-book, merchandise, halaman detail
    │   └── reader.js       ← Isi bab di halaman baca
    │
    ├── hooks/              ← Logika kecil yang dipakai ulang
    │   ├── useScrollToHash.js   ← Gulir ke section saat klik menu (/#kontak)
    │   └── useActiveSection.js  ← Menandai menu aktif saat menggulir
    │
    ├── components/         ← Potongan UI yang bisa dipakai ulang
    │   ├── ui/             ← Komponen dasar: Button, Stars, Modal, Pagination, Form
    │   ├── layout/         ← Navbar, Footer, Layout (kerangka semua halaman)
    │   ├── home/           ← Section-section di halaman utama
    │   ├── catalog/        ← Kartu & sidebar halaman katalog
    │   ├── detail/         ← Tampilan detail buku / merchandise
    │   ├── reader/         ← Daftar isi & area baca
    │   └── product/        ← Section di halaman produk
    │
    └── pages/              ← Satu file = satu halaman
        ├── HomePage.jsx
        ├── EbookCatalogPage.jsx
        ├── BookDetailPage.jsx
        ├── ReaderPage.jsx
        ├── MerchandisePage.jsx
        ├── MerchDetailPage.jsx
        ├── ProductPage.jsx
        └── NotFoundPage.jsx
```

Setiap komponen punya file `.jsx` (struktur) dan `.css` (tampilan) dengan nama yang sama, di folder yang sama.

---

## 3. Halaman & URL

| URL                              | Halaman            | Frame Figma               |
| -------------------------------- | ------------------ | ------------------------- |
| `/`                              | Landing Page       | Landing Page              |
| `/ebook`                         | Katalog e-book     | eBook Catalog             |
| `/ebook/blind-spot-radar`        | Detail buku        | Book - Details            |
| `/ebook/blind-spot-radar/baca`   | Halaman baca       | E-Book Reader             |
| `/merchandise`                   | Katalog merch      | Merchandise               |
| `/merchandise/black-totebag`     | Detail merch       | Merchandise - Details     |
| `/produk/veridia` (dst.)         | Detail produk      | Product - Veridia, dll.   |

---

## 4. Cara mengubah yang paling sering

**Ganti warna / font di seluruh website** → edit `src/styles/tokens.css`.
Contoh: ubah `--color-azure-11` untuk mengganti warna navy di semua tempat.

**Ganti teks, produk, buku, harga** → edit file di `src/data/`. Tidak perlu menyentuh komponen.

**Ganti gambar** → taruh gambar baru di `src/assets/...` dengan nama yang sama, atau ubah `import` di file data.

**Tambah produk baru** → tambahkan satu objek di `src/data/products.js` (beserta logonya di `assets/logos/`). Kartu di Home, halaman `/produk/nama-baru`, dan link footer akan ikut muncul.

**Ubah tampilan satu komponen** → buka file `.css` di sebelah komponennya, misal `src/components/home/Hero.css`.

---

## 5. Design tokens (dari Figma)

| Token                  | Nilai       | Dipakai untuk                         |
| ---------------------- | ----------- | ------------------------------------- |
| `--color-azure-11`     | `#0d1f2d`   | Judul, tombol gelap, section Mengapa  |
| `--color-azure-17`     | `#143244`   | Teks paragraf                         |
| `--color-grey-98`      | `#f7fafb`   | Latar section lembut                  |
| `--color-cyan-active`  | `#02b6cf`   | Menu aktif, tombol sekunder           |
| `--color-teal`         | `#35a190`   | Aksen Veridia / link                  |
| `--gradient-primary`   | hijau → cyan| Style Figma "Gradient/Primary"        |
| `--font-display`       | Montserrat  | Judul (style "Title" 48px / 0.83)     |
| `--font-body`          | DM Sans     | Teks utama                            |
| `--font-ui`            | Inter       | Halaman katalog & detail              |

Daftar lengkap ada di `src/styles/tokens.css`.

---

## 6. Hal yang masih perlu dilengkapi developer

- **Form kontak** belum terkirim ke mana pun. Sambungkan ke API di `src/components/home/ContactSection.jsx` (cari komentar `TODO`).
- **Data contoh**: harga, stok, nomor tiket, dan isi bab di reader masih placeholder.
- Semua kartu buku membuka detail *Blind Spot Radar* dan semua kartu merchandise membuka *Black Totebag* (sesuai prototype Figma). Untuk detail per item, tambahkan data di `catalog.js` lalu baca `slug` dari URL dengan `useParams()`.
- Logo dan ikon berupa PNG resolusi tinggi. Bila perlu SVG, ekspor dari Figma dengan nama file yang sama.

---

## 7. Upload online (deploy)

Paling mudah lewat **Vercel** atau **Netlify** (gratis):

1. Upload project ini ke GitHub.
2. Hubungkan repository di Vercel/Netlify.
3. Build command: `npm run build` · Output folder: `dist`.

File `vercel.json` dan `public/_redirects` sudah disiapkan supaya URL seperti `/ebook` tetap bisa dibuka langsung.
