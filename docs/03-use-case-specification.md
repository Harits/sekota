# 03 — Use Case Specification

Format: aktor, tujuan, prakondisi, pemicu, alur utama, alur alternatif, pascakondisi, data, bukti test.

---

## UC-01 — Menjelajah landing page

- **Aktor:** Pengunjung
- **Tujuan:** Memahami penawaran Sekota dan menemukan jalan ke layanan, platform, buku, atau kontak.
- **Prakondisi:** Aplikasi terbuka; konten katalog tersedia (offline).
- **Pemicu:** Membuka aplikasi/web.
- **Alur utama:**
  1. Sistem menampilkan hero: chip eBdesk, judul dua baris, lead, CTA, statistik 4/6.
  2. Sistem menampilkan marquee sektor.
  3. Pengunjung menggulir; setiap section muncul dengan animasi reveal.
  4. Sistem menampilkan Tentang → Layanan → Cara Kerja → Platform → Wawasan → CTA Mitra → FAQ → Footer.
  5. Header berubah solid dan sorotan anchor mengikuti section yang terlihat.
- **Alternatif:** Pengunjung dapat langsung memakai header (UC-03), mengganti bahasa (UC-02), atau membuka menu mobile (UC-04).
- **Pascakondisi:** Pengunjung dapat memilih tujuan berikutnya.
- **Data:** `LandingCatalog.content` (ID/EN).
- **Bukti:** `LandingViewModelTest`, `ContentCatalogTest.landing*`, `SekotaUiTest.landingRendersHero`, screenshot `home-1440-*`, `home-390-*`.

---

## UC-02 — Mengganti bahasa ID/EN

- **Aktor:** Pengunjung
- **Tujuan:** Membaca seluruh situs dalam bahasa pilihan.
- **Prakondisi:** Halaman apa pun terbuka.
- **Pemicu:** Klik "EN" atau "ID" di header.
- **Alur utama:**
  1. Sistem mengubah `AppState.language`.
  2. Seluruh komposisi membaca ulang `LText` dan menampilkan copy bahasa terpilih.
  3. Pilihan dipertahankan selama sesi aplikasi.
- **Alternatif:** Pada halaman kontak, pesan yang sudah diketik tidak berubah; label/error/subjek mengikuti bahasa saat submit.
- **Pascakondisi:** Seluruh teks (nav, hero, section, produk, buku, FAQ, formulir, footer) konsisten.
- **Data:** Pasangan `LText(id, en)` di setiap katalog.
- **Bukti:** `SekotaUiTest.languageToggleSwitchesToEnglish`, test `nonBlankBoth` di `ContentCatalogTest`, screenshot `home-en-top`.

---

## UC-03 — Navigasi anchor & section aktif

- **Aktor:** Pengunjung
- **Tujuan:** Menuju bagian tertentu di landing.
- **Prakondisi:** Landing terbuka (atau halaman lain yang menyediakan tautan section).
- **Pemicu:** Klik link nav / CTA "Lihat Layanan" / breadcrumb "Platform" / "Lihat semua layanan".
- **Alur utama:**
  1. `AppNavigator.navigateHome(anchor)` mencatat anchor.
  2. `LandingScreen` mengonsumsi anchor dan `animateScrollToItem` ke section.
  3. Saat scroll, `snapshotFlow` memperbarui `chromeSolid` dan `activeAnchor`.
  4. Header menyorot link aktif dan menjadi solid.
- **Alternatif:** Bila sudah di Home, anchor baru menggulir tanpa membuat rute baru.
- **Pascakondisi:** Section tujuan terlihat di bawah header.
- **Bukti:** `AppNavigatorTest`, UI nav pada screenshot, `LandingScreen` effect.

---

## UC-04 — Membuka menu mobile

- **Aktor:** Pengunjung Mobile
- **Tujuan:** Mengakses navigasi pada layar <860dp.
- **Prakondisi:** Lebar jendela <860dp.
- **Pemicu:** Klik burger.
- **Alur utama:**
  1. Header menampilkan burger (+ toggle bahasa) dan menyembunyikan link serta CTA.
  2. Klik burger membuka dropdown berisi 6 link dan CTA.
  3. Memilih link menutup menu lalu menjalankan UC-03.
- **Alternatif:** Klik burger lagi menutup menu; ikon berubah menjadi X.
- **Pascakondisi:** Menu tertutup; navigasi dijalankan.
- **Bukti:** screenshot `home-390-top`, kode `SiteHeader`, breakpoint `WindowSize.Medium`.

---

## UC-05 — Melihat layanan & membuka produk

- **Aktor:** Pengunjung
- **Tujuan:** Memilih layanan yang relevan.
- **Prakondisi:** Landing terbuka.
- **Pemicu:** Klik kartu layanan atau baris suite di hero.
- **Alur utama:**
  1. Sistem menampilkan 4 kartu layanan: nomor, ikon, judul, tagline, 3 poin, pill platform.
  2. Klik kartu/baris membuka `Route.Product(slug)`.
  3. Halaman produk dimuat dari `ProductCatalog`.
- **Alternatif:** Baris suite hanya menyorot saat hover/rotasi, klik tetap membuka produk.
- **Pascakondisi:** Detail produk tampil (UC-06).
- **Bukti:** `ContentCatalogTest.landingHasTheDesignedSections`, `ProductViewModelTest.everyPlatformHasDetail`, screenshot product.

---

## UC-06 — Melihat detail produk & galeri

- **Aktor:** Pengunjung
- **Tujuan:** Menilai kemampuan platform.
- **Prakondisi:** Route produk aktif.
- **Pemicu:** Membuka produk.
- **Alur utama:**
  1. Hero: breadcrumb, logo, pill layanan, headline, lead, CTA demo/fitur, tangkapan layar pertama.
  2. Masalah (3), Fitur (6), Galeri (tab), Alur (4), Audiens & layanan terkait.
  3. Klik tab galeri mengganti judul, deskripsi, dan gambar.
  4. Klik "Lihat Fitur" menggulir ke bagian fitur.
  5. Klik demo membuka kontak dengan preset `demo-{slug}`.
  6. Platform lain menampilkan 3 produk lainnya.
- **Alternatif:** Breadcrumb "Platform" kembali ke landing bagian Platform.
- **Pascakondisi:** Pengunjung dapat menghubungi (UC-08/UC-10).
- **Bukti:** `ProductViewModelTest.selectScreenIsBounded`, `SekotaUiTest.productPageRendersHeadline`, screenshot `product-clarivio-*`.

---

## UC-07 — Menjelajah Wawasan & detail buku

- **Aktor:** Pengunjung
- **Tujuan:** Memahami kerangka berpikir dan buku Sekota.
- **Prakondisi:** Landing terbuka.
- **Pemicu:** Klik kartu buku.
- **Alur utama:**
  1. `Route.Books(anchor)` dibuka; halaman menggulir ke blok buku.
  2. Blok menampilkan sampul, penulis, judul, subjudul, kutipan, paragraf, audiens, isi buku.
  3. Indeks 6 sampul dapat diklik untuk menggulir ke buku lain.
  4. Setelah buku ketiga, band gelap trilogi menampilkan 3 tautan.
  5. CTA bawah menuju kontak.
- **Alternatif:** Halaman dapat dibuka tanpa anchor (dari kode/navigasi masa depan) dan mulai dari hero.
- **Pascakondisi:** Pengunjung dapat menghubungi (UC-08).
- **Bukti:** `ContentCatalogTest.booksAreCompleteAndBilingual`, UI `SekotaUiTest.booksPageRendersHero`, screenshot `books-*`.

---

## UC-08 — Menghubungi Sekota lewat formulir

- **Aktor:** Pengunjung; Server Email (pendukung)
- **Tujuan:** Mengirim pertanyaan/permintaan demo.
- **Prakondisi:** Halaman Kontak terbuka.
- **Pemicu:** Klik "Hubungi Kami" atau CTA demo.
- **Alur utama:**
  1. Sistem menampilkan hero 3 langkah, email langsung, dan formulir.
  2. Pengunjung mengisi nama, email, topik, pesan.
  3. Klik "Kirim Pesan"; `ContactValidator` memvalidasi.
  4. Bila valid, `SubmitContactMessage` membangun subjek `[Website] {topik} – {nama}` dan body.
  5. `SendContactMessage` mengirim form ke `POST /api/contact`; tombol menampilkan "Mengirim…".
  6. Server memvalidasi ulang lalu `SmtpMailer` mengirim ke `info@sekota.id` dengan `Reply-To` pengunjung.
  7. Setelah respons 2xx, UI menampilkan "Pesan terkirim." beserta nama/email dan tombol "Kirim pesan lain".
- **Alur alternatif:**
  - **3a.** Tidak valid → tampilkan error per field; fokus tetap di formulir; tidak ada request.
  - **5a.** API/SMTP gagal/timeout/503 → form tetap terisi, tampil "Pesan belum terkirim. Coba lagi, atau email langsung ke info@sekota.id."
  - **7a.** Pengunjung klik "Kirim pesan lain" → form kosong kembali.
- **Pascakondisi:** Pesan diterima server dan diteruskan ke SMTP, atau pengunjung diberi jalur email langsung saat gagal.
- **Data:** `ContactForm`, `ContactValidation`, `ContactDraft`, `ContactMessage`, `ContactCatalog`.
- **Bukti:** `ContactValidatorTest`, `SubmitContactMessageTest`, `ContactViewModelTest` (sukses/gagal), `SekotaUiTest.contactFormValidatesAndSubmits`, `ApplicationTest` (5), screenshot `contact-*`, email `.eml` hasil SMTP sink lokal.

---

## UC-09 — Mencari jawaban di FAQ

- **Aktor:** Pengunjung
- **Tujuan:** Menemukan jawaban sebelum menghubungi.
- **Prakondisi:** Landing terbuka.
- **Pemicu:** Menggulir ke FAQ atau klik anchor FAQ.
- **Alur utama:**
  1. Pertanyaan pertama terbuka secara default.
  2. Klik pertanyaan lain menutup yang lama dan membuka yang baru.
  3. Klik pertanyaan terbuka menutupnya.
- **Alternatif:** Kartu email di kiri menuju `mailto:`.
- **Pascakondisi:** Pengunjung mendapat jawaban atau menghubungi (UC-08).
- **Bukti:** `SekotaUiTest.faqOpensOnClick`, `LandingViewModelTest.faq*`, screenshot FAQ.

---

## UC-10 — Mendarat dengan preset topik/pesan

- **Aktor:** Pengunjung
- **Tujuan:** Formulir sudah memahami konteks (produk/buku/mitra).
- **Prakondisi:** Pengunjung mengklik CTA demo produk, CTA buku, atau Program Mitra.
- **Pemicu:** `Route.Contact(preset)`.
- **Alur utama:**
  1. `ContactCatalog.prefill(preset)` memetakan preset ke topik + pesan.
  2. `ContactViewModel.prefill` mengisi topik dan pesan **hanya bila pesan masih kosong**.
  3. Pengunjung melengkapi nama/email lalu submit (UC-08).
- **Alur alternatif:** Preset tidak dikenal → formulir default "Konsultasi umum" tanpa pesan.
- **Pascakondisi:** Formulir kontekstual siap dikirim.
- **Data:** `demo-clarivio`, `demo-ascendio`, `demo-sociara`, `demo-evidara`, `mitra-awal`, `buku-{anchor}`.
- **Bukti:** `ContentCatalogTest.contactTopicsAndPrefillsAreStable`, `ContactViewModelTest.prefillAppliesTopicAndMessageOnce`.

---

## UC-11 — Membaca kebijakan privasi & syarat

- **Aktor:** Pengunjung
- **Tujuan:** Memahami pemrosesan data pribadi dan ketentuan penggunaan situs.
- **Prakondisi:** Halaman legal dibuka dari footer, catatan formulir, atau URL langsung.
- **Pemicu:** Klik "Privasi"/"Syarat & Ketentuan" atau `#/id/privacy` / `#/id/terms`.
- **Alur utama:**
  1. `LegalScreen` memuat `LegalDoc` dari `LegalCatalog` sesuai tipe dan bahasa.
  2. Hero menampilkan overline Legal, judul, lead, dan tanggal pembaruan.
  3. Daftar isi menampilkan semua bagian; klik bagian menggulir ke heading bernomor.
  4. Tautan inline membuka email, halaman Kontak, atau dokumen legal pasangannya.
  5. Tautan "Baca juga" di akhir membuka dokumen pasangan.
- **Alur alternatif:** Bahasa diganti → seluruh judul, isi, daftar isi, dan tanggal mengikuti bahasa tanpa reload.
- **Pascakondisi:** Pengunjung membaca dokumen atau berpindah ke dokumen pasangan/kontak.
- **Data:** `LegalDoc`, `LegalSection`, `LegalBlock`, `LegalSpan`, `LegalCatalog`.
- **Bukti:** `LegalCatalogTest`, `HashRouteTest.legalRoutesRoundTrip`, `SekotaUiTest.privacyPageRendersPolicy`, screenshot `privacy-*`, `terms-*`.
- **Catatan:** Kedua dokumen adalah **draf** dari desain dan wajib direview tim legal sebelum tayang.
