# 01 — Requirements

## 1. Tujuan produk

Menghadirkan ulang situs company profile Sekota yang ada di `sekota-landing-page` sebagai aplikasi **Compose Multiplatform** yang berjalan di web lebih dulu (Wasm), lalu Android, iOS, dan Desktop dari basis kode yang sama, dengan UI/UX yang setara dengan desain sumber, arsitektur yang mudah dirawat, dan jaringan test otomatis.

## 2. Ruang lingkup

**Termasuk**
- Landing page lengkap (11 bagian) dalam bahasa Indonesia dan Inggris.
- 4 halaman detail produk (Clarivio, Ascendio, Sociara, Evidara).
- Halaman Wawasan berisi 6 buku + trilogi Sustainability Series.
- Halaman Hubungi Kami dengan formulir tervalidasi, preset topik, pengiriman API + SMTP, status terkirim/gagal, dan email langsung.
- Navigasi anchor, menu mobile, toggle bahasa, galeri produk, akordeon FAQ.
- Dokumentasi requirement, arsitektur, design system, use case, flow, test strategy, sprint.
- Test otomatis unit, view model, katalog konten, UI Compose, server.

**Tidak termasuk (saat ini)**
- CMS atau API konten; seluruh konten statis di kode.
- Pengiriman formulir ke backend/email service (API + SMTP), bukan `mailto:`.
- SEO, SSR, analytics, cookie consent.
- Halaman legal Privasi/Syarat.
- Sinkronisasi otomatis OpenProject (direncanakan, lihat dok. 10).

## 3. Functional requirements

Prioritas: **M** = Must, **S** = Should, **C** = Could.

| ID | Requirement | Prioritas | Acceptance criteria |
|---|---|---|---|
| FR-01 | Pengunjung dapat membuka landing page dan melihat hero (chip, judul dua baris dengan baris kedua bergradien, lead, 2 CTA, statistik 4/6). | M | Semua elemen tampil; statistik berakhir di 4 dan 6; CTA dapat diklik. |
| FR-02 | Landing menampilkan marquee 6 sektor yang berulang tanpa jeda. | M | Enam label sektor tampil dan bergerak; tidak ada ruang kosong di akhir loop. |
| FR-03 | Landing menampilkan Tentang, 4 Layanan, 4 tahap Cara Kerja, 4 Platform, 6 Buku, CTA Mitra, FAQ, Footer sesuai desain. | M | Semua section ada; urutan sama; konten ID/EN benar. |
| FR-04 | Kartu suite di hero menyorot satu platform secara bergantian setiap ±2,6 detik dan dapat diklik menuju detail produk. | S | Highlight berpindah 0→1→2→3→0; klik membuka produk yang sesuai. |
| FR-05 | Navigasi atas menampilkan anchor dan menyorot section aktif saat scroll; latar menjadi solid setelah scroll. | M | Klik anchor menggulir ke section; underline mengikuti section; header solid saat `scrollY > 12dp` atau di halaman non-home. |
| FR-06 | Pada layar <860dp, nav berubah menjadi tombol burger dengan menu dropdown; toggle bahasa tetap tersedia. | M | Link nav tersembunyi; burger membuka/menutup menu; memilih link menutup menu dan menggulir. |
| FR-07 | Pengunjung dapat mengganti bahasa ID/EN di semua halaman tanpa reload. | M | Semua copy (nav, section, produk, buku, formulir, footer) berubah; pilihan berlaku sampai aplikasi ditutup. |
| FR-08 | Setiap kartu layanan membuka halaman produk yang sesuai. | M | Klik Clarivio/Ascendio/Sociara/Evidara membuka detail masing-masing. |
| FR-09 | Halaman produk menampilkan breadcrumb, logo, pill layanan, headline, lead, CTA demo & fitur, tangkapan layar, 3 masalah, 6 fitur, galeri tab, alur 4 langkah, audiens, layanan terkait, platform lain, CTA. | M | Semua bagian ada; tab galeri berganti gambar; CTA demo membuka kontak dengan preset yang benar. |
| FR-10 | Tombol "Lihat Fitur" menggulir ke bagian fitur. | S | Setelah klik, bagian Fitur menjadi terlihat. |
| FR-11 | Halaman Wawasan menampilkan hero, indeks 6 buku, dan blok detail tiap buku (penulis, judul, subjudul, kutipan, paragraf, audiens, isi buku). | M | Enam blok lengkap; tautan indeks menggulir ke blok yang tepat. |
| FR-12 | Trilogi Sustainability Series tampil sebagai band gelap dengan 3 tautan buku. | M | Band muncul setelah buku ketiga; tautan menggulir ke buku terkait. |
| FR-13 | Kartu buku di landing membuka halaman Wawasan pada anchor buku tersebut. | M | Klik sampul/judul membuka halaman Wawasan dan menggulir ke blok benar. |
| FR-14 | Halaman Kontak menampilkan hero, 3 langkah, email langsung, dan formulir (nama, email, topik, pesan). | M | Semua field dan tombol tampil; email `info@sekota.id` dapat diklik. |
| FR-15 | Formulir memvalidasi nama (≥2 karakter), email (format valid), pesan (≥3 karakter) dan menampilkan pesan error per field. | M | Submit kosong menampilkan 3 error; input valid menghapus error terkait. |
| FR-16 | Submit valid membangun subjek/body, mengirim ke `POST /api/contact`, lalu menampilkan "Pesan terkirim." dengan nama/email pengunjung dan tombol kirim pesan lain. | M | Subjek `[Website] {topik} – {nama}`; sukses hanya setelah respons 2xx; gagal menampilkan error + email langsung dan mempertahankan isian; tidak ada preview/mailto. |
| FR-17 | Preset dari tautan (`demo-clarivio`, `mitra-awal`, `buku-*`) mengisi topik dan pesan awal. | S | Preset mengisi topik yang tepat dan pesan yang menyebut produk/buku; pesan pengguna tidak ditimpa. |
| FR-18 | FAQ menggunakan akordeon satu-terbuka; pertanyaan pertama terbuka secara default. | M | Klik pertanyaan lain menutup yang lama; ikon plus berputar 45°. |
| FR-19 | Footer menampilkan deskripsi perusahaan, kolom Navigasi/Platform/Kontak, copyright, serta tautan Privasi dan Syarat & Ketentuan. | M | Semua tautan berfungsi dan membuka halaman legal yang sesuai; layout tidak bertumpuk di desktop maupun mobile. |
| FR-20 | Semua halaman responsif pada lebar 390dp (mobile) hingga ≥1100dp (desktop) tanpa scroll horizontal. | M | Screenshot 390 & 1440 diverifikasi; tidak ada elemen terpotong/overlap. |
| FR-21 | Konten ID dan EN tersedia untuk seluruh halaman. | M | Tidak ada teks kosong/placeholder; test katalog memeriksa kedua bahasa. |
| FR-22 | Halaman produk dan buku dapat diakses dari landing tanpa error saat kembali ke Home. | M | Logo/tautan "Platform" mengembalikan ke landing; anchor tujuan digulir. |
| FR-23 | Setiap halaman dan bahasa punya URL hash yang dapat dibuka langsung, di-refresh, dan dinavigasi dengan browser back. | M | `#/id/product/clarivio`, `#/en`, `#/id/books/{anchor}`, `#/id/contact/{preset}`, `#/id/privacy`, `#/id/terms`; browser back/forward bekerja. |
| FR-24 | Pengguna keyboard dapat menggulir halaman dan melihat fokus. | S | PageUp/PageDown/Home/End/Arrow menggulir; fokus terlihat pada kontrol utama. |
| FR-25 | Animasi menghormati `prefers-reduced-motion`. | S | Marquee/highlight/count-up/reveal berhenti atau tampil final saat reduce motion. |
| FR-26 | Halaman Kebijakan Privasi dan Syarat & Ketentuan tersedia dalam ID/EN, dengan daftar isi, penomoran bagian, tautan internal/email, dan draf tanggal pembaruan. | M | Kedua halaman dapat dibuka dari footer dan URL langsung; 10/12 bagian tampil; tautan Privasi↔Syarat dan email bekerja. |

## 4. Non-functional requirements

| ID | Requirement | Target / cara verifikasi |
|---|---|---|
| NFR-01 | Responsif | 4 breakpoint; screenshot otomatis 1440×1000 dan 390×844; tidak ada overflow horizontal. |
| NFR-02 | Kinerja web | Build Wasm produksi berhasil; ukuran bundle dicatat (wasm app ±3,45 MiB + skiko ±8,25 MiB + JS 521 KiB). Code splitting/asset lazy-load menjadi backlog. |
| NFR-03 | Aksesibilitas | Kontras teks utama ≥ 4.5:1; kontrol punya label/state; scroll & fokus keyboard; `lang` dokumen; reduced-motion. Gradien brand + teks putih masih di bawah AA (mengikuti desain) dan dicatat sebagai risiko. |
| NFR-04 | Internasionalisasi | Semua copy melalui `LText`; tidak ada string UI hardcoded kecuali label teknis ("Platform", "Layanan:"). |
| NFR-05 | Testability | Domain & view model murni; 100% use case dan validator diuji; UI smoke test untuk landing, FAQ, kontak, produk, buku. |
| NFR-06 | Maintainability | Dependency rule clean architecture; feature-first package; katalog konten terpisah; tidak ada akses data langsung dari UI. |
| NFR-07 | Offline-first | Semua konten & aset dibundel; tidak ada panggilan jaringan saat runtime. |
| NFR-08 | Privasi | Tidak ada data pengunjung dikirim ke server; pesan hanya dibuka di aplikasi email pengunjung. |
| NFR-09 | Portabilitas | Kode common berjalan di Android, iOS, JVM Desktop, JS, Wasm; build JVM/Wasm/Android/Desktop diverifikasi. |
| NFR-10 | Build reproducibility | Gradle wrapper 9.5.1, version catalog terpusat, configuration cache aktif. |
| NFR-11 | Observability | Laporan test XML/HTML + laporan Markdown otomatis; screenshot sebagai bukti visual. |
| NFR-12 | Keamanan konten | Tidak ada HTML/JS dari desain sumber yang dieksekusi; aset dikonversi dan dibundel. |

## 5. Batasan & asumsi

- Desain sumber adalah HTML statis tanpa API; konten dianggap final kecuali catatan README sumber (FAQ perlu review, istilah "Analisis Strategis & Risiko" menunggu persetujuan direksi).
- Formulir tidak mengirim email dari aplikasi; pengguna menyelesaikan pengiriman di klien email.
- Web adalah target utama; target lain harus tetap dapat dikompilasi, tetapi verifikasi visual diprioritaskan web.
- Belum ada pipeline CI; verifikasi dijalankan lokal melalui skrip.
- Bahasa utama dokumentasi: Indonesia; identifier kode: Inggris.

## 6. Definisi selesai (ringkas)

1. Requirement terkait punya use case & test.
2. Test JVM + Wasm + server lulus.
3. Build web produksi berhasil.
4. Screenshot desktop & mobile diperbarui.
5. Dokumentasi (design system/arsitektur) diperbarui bila ada perubahan perilaku.
