# Functional Specification Document (FSD) & System Architecture
**Dokumen Referensi: PM-FRM-06 / ISO 21502:2020 Gate 2 (G2 Baseline) & SNI ISO 9001:2015**  
**Master Artwork: Figma `Sekota-Landing-Page` (`oZqXuSsN7u7S73vfjWXQIN`, Frames `15:1465`, `0:1`)**  
**Entitas Pemilik: PT Sekota Sinergi Indonesia (eBdesk Group Affiliation)**

---

## 1. Arsitektur Solusi & Komponen Sistem

Sistem **Sekota Web Platform** dibangun di atas fondasi **Kotlin Multiplatform (KMP)** dengan menerapkan pemisahan tanggung jawab yang tegas sesuai prinsip *Clean Architecture* dan *Screaming Architecture*. Arsitektur dibagi menjadi 3 modul utama:

```
┌───────────────────────────────────────────────────────────────────────────────────┐
│                        composeApp (Frontend Web CMP)                              │
│   - Target Kompilasi: Kotlin/Wasm (wasmJs), Legacy Fallback (js), Android Native  │
│   - Antarmuka (UI): Screens (Landing, Catalog, BookDetails, Merchandise, Auth)    │
│   - Komponen & Desain: Bento Grid, SidebarFilter, Navigation, Header, Footer      │
│   - Desain Token: Ink Navy (#0D1F2D), Brand Teal (#00B5C8), Montserrat & DM Sans │
│   - Navigasi & Sesi: Reactive State Management, Local Preferences, i18n Switcher  │
└─────────────────────────────────────────┬─────────────────────────────────────────┘
                                          │
                                          ▼
┌───────────────────────────────────────────────────────────────────────────────────┐
│                        shared (Core Business Logic)                               │
│   - Domain (Pure Kotlin): Entities, Value Objects, Use Cases, Repository Interface│
│   - Data: Ktor HTTP Client (REST), WebSocket Client (/ws/sync), SQLDelight       │
│   - Persistence Cache: TokenStorage, Local Shelf Cache, User Session Storage      │
│   - Mappers: Data Transfer Objects (DTO) to Domain Entity Mappers                 │
└─────────────────────────────────────────┬─────────────────────────────────────────┘
                                          │
                                          ▼ (JVM Target via Netty)
┌───────────────────────────────────────────────────────────────────────────────────┐
│                        server (Ktor Backend & Static Host)                        │
│   - Engine: Ktor Netty Runtime (Port 8080), Docker/Podman Distroless Container    │
│   - Autentikasi: JWT Auth Provider (HMAC256, Claims, Token Expiry, Roles)         │
│   - RESTful API: /auth/register, /auth/login, /profile, /leads/consultation       │
│   - Dokumentasi API: Swagger UI (/swagger) & OpenAPI 3.0 (/openapi/documentation) │
│   - Real-Time Hub: Ktor WebSockets Engine (/ws/sync) untuk sinkronisasi state     │
│   - Persistence Layer: Exposed ORM + HikariCP Connection Pool + PostgreSQL DB    │
│   - Static Asset Hosting: Penyajian bundle Wasm (/web) & media assets             │
└───────────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Kanon Aktor Sistem

Sistem Sekota mendefinisikan 4 aktor formal dengan profil kewenangan dan ruang lingkup interaksi yang saling melengkapi:

| Aktor | Tipe | Peran & Karakteristik Interaksi |
|---|---|---|
| **Pembaca Publik (Reader)** | Manusia (Eksternal) | Pengguna internet dan pembaca antusias yang mengakses landing page, menelusuri katalog eBook, melakukan interaksi bacaan digital (*"Read"*), menyimpan buku ke rak pribadi (*"Add to Library"*), menjelajahi bento grid bab buku, memilih merchandise resmi, beralih bahasa antarmuka, serta mendaftarkan identitas akun baru dengan peran default `READER`. |
| **Klien Korporat B2B/B2G** | Manusia (Eksternal) | Pengambil keputusan tingkat institusi pemerintah atau korporat (direpresentasikan oleh profil mitra *Budi Santoso*, `budi@instansi.go.id`). Berinteraksi dengan formulir konsultasi strategis, mengeksplorasi ekosistem produk *Intelligence Suite* (Veridia, Ascendio, Sociara, Ecoflow), mengajukan proposal kustom (*"Customize Solution"*), dan mengelola profil terverifikasi. |
| **Admin Sistem** | Manusia (Internal) | Pengelola teknis platform internal yang bertanggung jawab menjaga integritas konfigurasi sistem, memantau penerbitan token JWT dan sesi persisten, mengaudit log konsumsi API, mengelola metadata katalog, serta menginspeksi integrasi lead konsultasi institusional. |
| **Sync Engine** | Sistem (Internal) | Layanan proses latar belakang berbasis Ktor WebSocket (`/ws/sync`) yang menjaga konsistensi data secara real-time dan bidireksional antara client CMP dan server backend, memvalidasi masa aktif sesi, dan menyiarkan event pembaharuan state ke seluruh klien aktif. |

---

## 3. Matriks Spesifikasi Use Case Terperinci (FSD)

### 3.1 Modul Autentikasi & Manajemen Akun Pengguna (Auth & Identity)

#### UC-AUTH-01: Registrasi Pembaca Baru (Default READER Identity via /auth/register)
- **Aktor**: Pembaca Publik (Reader)
- **Referensi Visual Figma**: Frame `0:1` / `SignupScreen` ("New Identity", Surface Alt `#F7FAFB`, Card Radius 24dp, Whisper Hairline).
- **Deskripsi Fungsional**: Pengguna mendaftarkan akun baru pada platform Sekota. Sistem secara otomatis menginisialisasi identitas pengguna dengan peran bawaan (*default role*) `READER`.
- **Prekondisi**: Pengguna berada pada layar pendaftaran (`SignupScreen`); konektivitas jaringan ke endpoint backend aktif.
- **Alur Utama**:
  1. Pembaca mengakses antarmuka `New Identity`.
  2. Pembaca memasukkan nama pengguna unik pada kolom **CHOOSE A USERNAME** dan kata sandi pada kolom **CREATE A PASSWORD**.
  3. Client CMP melakukan validasi format input secara lokal (panjang minimal karakter, tidak boleh memuat spasi, sanitasi input).
  4. Pengguna menekan tombol primer **"Create Identity"** (warna `--ink` `#0D1F2D`).
  5. `SignupUseCase` mengirimkan permintaan HTTP POST ke `/auth/register` (kompatibel dengan `/auth/signup`) dengan payload `AuthRequest(username, password)`.
  6. Backend Ktor Server memvalidasi keunikan username di database, mengenkripsi kata sandi menggunakan algoritma hashing aman, dan menetapkan peran default `READER`.
  7. Server menerbitkan token otentikasi JWT dan mengembalikan respons HTTP 200 OK dengan payload `AuthResponse(token, User(username, role = "READER"))`.
  8. Client menerima respons, menyimpan token secara persisten ke dalam SQLite lokal via SQLDelight `TokenStorage`, dan mengarahkan pembaca ke status akun aktif.
- **Alur Alternatif & Eksepsi**:
  - *Username telah terdaftar*: Server mengembalikan kode `HTTP 409 Conflict` dengan pesan *"User already exists"*. UI menampilkan pesan kesalahan inline berwarna merah pada form.
  - *Validasi lokal gagal*: Indikator teks kesalahan muncul di bawah kolom yang tidak valid dan tombol submit dinonaktifkan sementara.
- **Pascasyarat**: Identitas pembaca baru tercatat di database server, token JWT tersimpan di cache lokal, dan pengguna siap mengakses fitur pustaka pembaca.

---

#### UC-AUTH-02: Masuk Akun Pengguna & Penerbitan JWT (/auth/login)
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G, Admin Sistem
- **Referensi Visual Figma**: Frame `0:1` / `LoginScreen` ("Sign In - Access Trusted Intelligence", Ink Navy typography, Brand Teal accents).
- **Deskripsi Fungsional**: Pengguna terdaftar mengautentikasi kredensial login untuk memperoleh JSON Web Token (JWT) yang ditandatangani secara kriptografis oleh server.
- **Prekondisi**: Akun pengguna telah terdaftar di database sistem.
- **Alur Utama**:
  1. Pengguna membuka antarmuka `LoginScreen`.
  2. Pengguna memasukkan **USERNAME** dan **PASSWORD**.
  3. Pengguna menekan tombol primer **"Authenticate"** / **"Sign In"**.
  4. `LoginUseCase` memvalidasi input lokal dan mengeksekusi panggilan HTTP POST ke endpoint `/auth/login` dengan payload `AuthRequest`.
  5. Ktor Server memverifikasi kecocokan kredensial terhadap data terenkripsi di PostgreSQL / memory store.
  6. Server men-generate token JWT dengan algoritma HMAC256 (`sekota-secret`), memuat klaim identitas (`username`, `issuer`, `audience`, dan masa berlaku 1 jam).
  7. Server mengembalikan respons HTTP 200 OK berisi objek `AuthResponse(token, user)`.
  8. Client memperbarui state autentikasi global dan menyimpan token ke `TokenStorage` (SQLDelight).
- **Alur Alternatif & Eksepsi**:
  - *Kredensial tidak valid*: Server mengembalikan `HTTP 401 Unauthorized` dengan pesan *"Invalid credentials"*. UI menampilkan banner peringatan *"Invalid username or password"*.
  - *Koneksi jaringan terputus*: Client menangani timeout/error dan menampilkan notifikasi kegagalan koneksi jaringan.
- **Pascasyarat**: Sesi terautentikasi aktif; seluruh permintaan HTTP selanjutnya menyertakan header `Authorization: Bearer <token>`.

---

#### UC-AUTH-03: Manajemen Profil & Sesi Persisten
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: `ProfileScreen` ("COLLECTOR PROFILE", Card Radius 32dp, Brand Teal Eyebrow, Pill Buttons).
- **Deskripsi Fungsional**: Pengguna terautentikasi mengelola data identitas profil mereka (Full Name, Email Identity/Bisnis), mempertahankan login persisten lintas siklus buka-tutup aplikasi, atau melakukan terminasi sesi (Logout).
- **Prekondisi**: Pengguna telah terautentikasi dengan token JWT tersimpan di SQLDelight.
- **Alur Utama**:
  1. Saat aplikasi dibuka kembali, `GetTokenUseCase` memverifikasi keberadaan token JWT pada storage lokal. Jika valid, sesi login dipulihkan secara otomatis (*persistent session*).
  2. Pada layar `ProfileScreen`, client memanggil `GetProfileUseCase` yang mengirimkan HTTP GET ke `/profile` dengan header `Bearer <token>`.
  3. Server memvalidasi klaim token melalui `JWTPrincipal` dan mengembalikan data `UserProfile` (username, fullName, email).
  4. Pengguna meninjau dan memperbarui informasi (misalnya mengubah nama lengkap atau email).
  5. Pengguna menekan tombol **"Commit Changes"**.
  6. `UpdateProfileUseCase` mengirimkan HTTP POST ke `/profile` berisi data profil termutakhir. Server memperbarui basis data dan merespons sukses.
  7. Untuk keluar, pengguna menekan tombol **"Terminate Session"**.
  8. `ClearTokenUseCase` menghapus token dari SQLDelight dan memutus koneksi sesi, mengembalikan antarmuka ke mode Guest.
- **Alur Alternatif & Eksepsi**:
  - *Token kedaluwarsa atau tidak valid*: Server merespons `HTTP 401 Unauthorized`. Client secara otomatis memicu pembersihan token lokal dan mengarahkan pengguna kembali ke `LoginScreen`.
- **Pascasyarat**: Informasi profil termutakhir disimpan pada server dan sinkron dengan status penyimpanan lokal.

---

### 3.2 Modul B2B/B2G Leads & Konsultasi Strategis

#### UC-LEAD-01: Pengajuan Permintaan Konsultasi Strategis (Formulir Kontak Landing Page Budi Santoso)
- **Aktor**: Klien Korporat B2B/B2G
- **Referensi Visual Figma**: Section 5.8 `ContactFormSection` ("Mulai Percakapan Strategis", Card 512px, Radius 40dp, Hairline Inputs).
- **Deskripsi Fungsional**: Klien korporat atau institusi pemerintah mengajukan inisiasi konsultasi intelijen strategis melalui formulir kontak resmi dengan placeholder representatif persona B2G *Budi Santoso* (`budi@instansi.go.id`).
- **Prekondisi**: Klien membuka Landing Page Sekota dan menavigasi ke seksi kontak atau menekan CTA *"Konsultasi Strategis"* pada navbar/hero.
- **Alur Utama**:
  1. Klien melihat headline editorial *"Mulai Percakapan Strategis"* dan komitmen SLA respons (*"Tim kami akan merespons dalam 1×24 jam kerja"*).
  2. Klien mengisi kolom **NAMA LENGKAP** (placeholder: *Budi Santoso*).
  3. Klien mengisi kolom **EMAIL BISNIS** (placeholder: *budi@instansi.go.id*).
  4. Klien menguraikan latar belakang tantangan pada kolom textarea **PESAN / KEBUTUHAN STRATEGIS** (placeholder: *Ceritakan tantangan Anda...*).
  5. Klien menekan tombol aksi primer berbentuk kapsul **"Kirim & Mulai Konsultasi"** (warna `--ink` `#0F172A`/`#0D1F2D`, tinggi 72dp, radius 36dp).
  6. Client memvalidasi format email korporat/instansi dan memastikan kolom pesan tidak kosong.
  7. Sistem mengirimkan payload lead konsultasi ke backend Sekota, mencatat waktu masuk, dan mengirim konfirmasi penerimaan pengajuan ke antarmuka klien.
- **Alur Alternatif & Eksepsi**:
  - *Format email tidak valid*: Kolom email menampilkan sorotan batas merah dan peringatan format email instansi/bisnis.
  - *Kolom pesan kosong*: Tombol kirim menahan transmisi formulir hingga teks pesan terisi.
- **Pascasyarat**: Lead konsultasi tersimpan pada antrian sistem penanganan B2B/B2G Sekota dengan target respons 1×24 jam kerja.

---

### 3.3 Modul Thought Leadership, E-Book & Keterlibatan Pembaca (Reader Experience)

#### UC-BOOK-01: Interaksi Pembaca: Aksi "Read" dan "Add to Library"
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: `BookDetailsScreen` (Hero Info Section: Large Book Cover, Title *"Blind Spot Radar"*, Button *"Read"* `#46B778`, Button *"Add to Library"* `#F1F4F7` + `#00B7D1`).
- **Deskripsi Fungsional**: Pembaca berinteraksi secara mendalam dengan publikasi digital unggulan melalui dua aksi operasional utama: membaca naskah langsung (*"Read"*) atau mengamankan publikasi ke dalam perpustakaan pribadi pembaca (*"Add to Library"*).
- **Prekondisi**: Layar rincian publikasi (`BookDetailsScreen`) aktif dan menampilkan data buku (misal: *"Blind Spot Radar: Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar"*).
- **Alur Utama**:
  1. Pengguna menelaah informasi hero buku: sampul berbayang tebal, kategori *"Self-Improvement / Mindfulness"*, rating *"4.8/5 from 1,240 readers"*, dan sinopsis eksekutif.
  2. **Kasus A - Membaca Publikasi ("Read")**:
     - Pengguna menekan tombol primer hijau **"Read"** (warna `#46B778`, font Montserrat/DM Sans Bold).
     - Sistem membuka viewer pembaca interaktif (e-reader/PDF viewer mode) untuk menampilkan konten lengkap.
  3. **Kasus B - Menambahkan ke Perpustakaan ("Add to Library")**:
     - Pengguna menekan tombol sekunder **"Add to Library"** (background `#F1F4F7`, teks Brand Teal `#00B7D1`, lebar 200dp).
     - Sistem memeriksa status sesi pembaca.
     - Jika terautentikasi (READER), sistem mendaftarkan ID buku ke relasi perpustakaan pengguna di penyimpanan lokal SQLDelight dan mengirimkan mutasi data ke server backend.
     - Label tombol bertransisi menjadi indikator visual konfirmasi *"Added to Library"* dengan ikon ceklis.
     - Jika belum terautentikasi, sistem menyimpan status buku ke rak sementara (*guest shelf*) dan menawarkan navigasi login via `UC-AUTH-02`.
- **Pascasyarat**: Buku tercatat di koleksi pembaca aktif dan dapat diakses cepat melalui profil pembaca.

---

#### UC-BOOK-02: Eksplorasi Bento Grid (Bab Kunci & Profil Penulis Putu Aan J.)
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: `BookDetailsScreen` (Card *"What's Inside"*, 4 Cards Bento Layout, Progress Bar *"Average Reading Time"*, Metadata Sidebar).
- **Deskripsi Fungsional**: Pengguna menelusuri silabus dan struktur pemikiran buku melalui tata letak Bento Grid multi-komponen, memverifikasi bab-bab kunci kurikulum, metrik durasi baca, rincian metadata teknis, dan profil penulis *Putu Aan J.*.
- **Prekondisi**: Pengguna berada pada `BookDetailsScreen`.
- **Alur Utama**:
  1. Pengguna mengidentifikasi atribusi kepenulisan buku: *"by Putu Aan J."*.
  2. Pengguna menginspeksi seksi kartu Bento Grid berbingkai putih (`Card Radius 16dp`, padding 40dp) berjudul **"What's Inside"**:
     - **Bab 1 (Icon 6)**: *The Attention Economy* — "Understanding the mechanics behind digital distractions."
     - **Bab 2 (Icon 7)**: *Deep Work Protocols* — "Actionable steps to enter flow state on command."
     - **Bab 3 (Icon 8)**: *Mindful Tech Integration* — "Setting boundaries that stick without the guilt."
     - **Bab 4 (Icon 5)**: *Cognitive Recovery* — "Restoring your mental energy after intense digital usage."
  3. Pengguna meninjau bar indikator durasi baca di bawah bento grid: label *"AVERAGE READING TIME"* dengan estimasi waktu *"3H 45M"* dan progress bar hijau `#46B778` (rasio 70%).
  4. Pengguna memeriksa kartu **Metadata** di panel kanan (warna latar `#F1F4F7`, radius 16dp) yang merangkum spesifikasi fisik buku:
     - **PUBLISHED**: *"Nov 2025"*
     - **PAGES**: *"240"*
     - **LANGUAGE**: *"Indonesia"*
     - **FORMAT**: *"eBook, PDF"*
- **Pascasyarat**: Pengguna memperoleh pemahaman holistik atas isi naskah dan kurikulum materi sebelum mengambil keputusan bacaan.

---

#### UC-CAT-01: Pencarian & Filter Multikategori E-Book (Genre, Tahun, Sortir)
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: `CatalogScreen` & `SidebarFilter` (Search textfield, Sort By dropdown, Checkbox list Genre & Year, 3-column Book Grid).
- **Deskripsi Fungsional**: Pengguna melakukan kurasi, penelusuran, dan pemilahan koleksi katalog pengetahuan digital Sekota menggunakan filter multi-kriteria tanpa reload halaman (SPA UX).
- **Prekondisi**: Halaman Katalog E-Book (`CatalogScreen`) dimuat pada peramban.
- **Alur Utama**:
  1. Pengguna mengetikkan kata kunci pencarian pada kolom **Search..** di bilah sisi kiri (`SidebarFilter`).
  2. Pengguna memilih mode pengurutan pada menu dropdown **Sort By** (misal: *Newest*, *Rating Tertinggi*, *A-Z*).
  3. Pengguna memilih opsi filter **Genre** melalui checkbox reaktif:
     - *All*, *Self-Improvement*, *Social-Improvement*, *Sustainability*, *Other*.
  4. Pengguna memilih opsi filter **Year** publikasi:
     - *2025*, *2024*, *2023*.
  5. State filter diperbarui seketika dan memicu penyaringan data pada `CatalogScreen`.
  6. Sistem menampilkan grid buku 3 kolom responsif yang memuat sampul buku, visual bintang rating (5 bintang), judul, nama penulis, dan tautan *"View Details"*.
  7. Pengguna dapat berpindah halaman menggunakan kontrol navigasi paginasi numerik di bagian bawah grid.
- **Alur Alternatif & Eksepsi**:
  - *Tidak ada buku yang memenuhi kriteria*: Sistem menampilkan visual status kosong (*Empty State*) ramah pengguna dengan tombol *"Reset Filter"*.
- **Pascasyarat**: Daftar koleksi yang relevan tersaji secara instan sesuai kriteria eksplorasi pengguna.

---

### 3.4 Modul Merchandise & Solusi Kustomisasi

#### UC-MERCH-01: Eksplorasi Merchandise & Pengajuan Solusi Kustom (Customize Solution)
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: `MerchandiseScreen` & `SidebarFilter` (Grid Merchandise 3 kolom, Banner Promo *"Customize Solution"*, Banner Promo *"Executive Presence"*).
- **Deskripsi Fungsional**: Pengguna menjelajahi lini merchandise resmi Sekota (*"The Urban Collaborator"* series: T-Shirt, Pin, Sticker) dan mengakses kartu promosi interaktif *"Customize Solution"* untuk mengajukan pemesanan paket kustom skala organisasi.
- **Prekondisi**: Pengguna membuka halaman Merchandise atau melihat promosi merchandise di Sidebar Filter katalog.
- **Alur Utama**:
  1. Pengguna membuka halaman `MerchandiseScreen`.
  2. Pengguna memfilter barang berdasarkan kategori: *All, T-Shirt, Pin, Sticker, Others*.
  3. Grid menampilkan item merchandise resmi dengan gambar produk, label seri (*"THE URBAN COLLABORATOR"*), rating bintang, dan judul produk.
  4. Pengguna meninjau kartu promosi samping **"Customize Solution"** yang memuat judul *"Customize Solution"*, subjudul *"Hubungi kami untuk proposal khusus"*, dan tombol CTA **"Contact Us"** (warna `#4DB6AC`).
  5. Pengguna mengeklik tombol **"Contact Us"**.
  6. Sistem menavigasikan pengguna secara mulus ke formulir kontak konsultasi strategis (`UC-LEAD-01`), dengan parameter form terkonfigurasi untuk kebutuhan paket kustom korporat.
- **Alur Alternatif**:
  - Dari halaman E-Book Catalog, pengguna yang melihat kartu promosi **"Executive Presence"** (*"Lebih dari Sekadar Merchandise, Ini Adalah Pesan Strategis"*) dapat mengeklik tombol **"Get Merchandise"** (warna `#00ACC1`) untuk langsung beralih ke katalog merchandise.
- **Pascasyarat**: Permintaan solusi kustom terhubung langsung dengan alur perolehan prospek strategis Sekota.

---

### 3.5 Modul Pengalaman Global & Lokalisasi

#### UC-I18N-01: Pengalihan Bahasa Antarmuka (English / Bahasa Indonesia)
- **Aktor**: Pembaca Publik (Reader), Klien Korporat B2B/B2G
- **Referensi Visual Figma**: Section 5.9 `Footer` (Bottom Bar: Copyright + Language Selector `ENGLISH` / `BAHASA INDONESIA`).
- **Deskripsi Fungsional**: Pengguna beralih antara Bahasa Indonesia (bahasa editorial default platform) dan Bahasa Inggris secara dinamis tanpa me-refresh sesi aplikasi.
- **Prekondisi**: Halaman web Sekota aktif pada peramban klien.
- **Alur Utama**:
  1. Pengguna menggulir halaman ke bagian Global Footer (`Footer.kt`).
  2. Pada baris copyright bawah di sisi kanan, pengguna melihat kontrol pengalih bahasa: **ENGLISH** dan **BAHASA INDONESIA**.
  3. Bahasa yang aktif ditandai dengan font tebal (`FontWeight.Bold`) dan warna kontras (`Color.Black` / `#0D1F2D`), sedangkan bahasa tidak aktif berwarna abu-abu (`Color.Gray`).
  4. Pengguna mengklik opsi bahasa yang diinginkan (misal memilih *"ENGLISH"*).
  5. Sistem memperbarui state bahasa global aplikasi (`LocaleState`).
  6. Seluruh teks antarmuka, navigasi menu (*Products, E-Books, Why Sekota, Contact*), label formulir, dan tombol bertransisi ke bahasa terpilih.
  7. Preferensi bahasa disimpan ke penyimpanan persisten lokal peramban / SQLDelight agar tetap aktif saat pengguna membuka tab baru.
- **Pascasyarat**: Seluruh string antarmuka dirender dalam bahasa yang dipilih dan state tetap konsisten di seluruh layar.

---

### 3.6 Modul Layanan Latar Belakang & Tata Kelola Sistem

#### UC-SYNC-01: Sinkronisasi Status Sesi & State Real-Time
- **Aktor**: Sync Engine (WebSocket System)
- **Deskripsi Fungsional**: Client membentuk koneksi persisten dua arah ke endpoint Ktor WebSocket `/ws/sync`. Sesi otentikasi, validitas token, dan event perubahan status disiarkan secara real-time untuk menjaga konsistensi state multi-klien.
- **Prekondisi**: Koneksi internet stabil; modul WebSocket backend Ktor aktif.
- **Alur Utama**:
  1. Saat client CMP diinisialisasi, client membuat koneksi WebSocket ke `ws://host:8080/ws/sync`.
  2. Client mengirimkan heartbeat berkala dan payload handshake berisi token sesi.
  3. Sync Engine memverifikasi identitas sesi pembaca/klien.
  4. Jika terjadi perubahan status (misal: token kedaluwarsa, sesi di-terminate di perangkat lain), Sync Engine menyiarkan frame JSON event perubahan ke client.
  5. Client merespons event dan memperbarui UI secara reaktif tanpa perlu pemuatan ulang halaman.
- **Pascasyarat**: State sistem antara server dan klien tersinkronisasi secara real-time.

---

#### UC-ADM-01: Pemantauan Sesi, Audit Keamanan & Tata Kelola Platform
- **Aktor**: Admin Sistem
- **Deskripsi Fungsional**: Administrator sistem memantau integritas platform, mengaudit penerbitan token JWT, mengecek dokumentasi OpenAPI/Swagger UI (`/swagger`), dan mengelola reliabilitas layanan.
- **Prekondisi**: Admin memiliki akses terverifikasi ke portal administrasi dan konfigurasi backend.
- **Alur Utama**:
  1. Admin mengakses portal monitoring dan endpoint OpenAPI (`/openapi/documentation.yaml`).
  2. Admin memverifikasi kontrak API untuk autentikasi, profil, dan leads.
  3. Admin menginspeksi log autentikasi (CallLogging) untuk mendeteksi potensi serangan brute force atau token forging.
  4. Admin memelihara tabel database via Exposed ORM migrations jika terjadi pembaruan skema.
- **Pascasyarat**: Sistem beroperasi dalam koridor keamanan ISO 27001 dan kepatuhan arsitektur Clean Architecture.

---

## 4. Matriks Ketertelusuran Persyaratan (Traceability Matrix)

Matriks berikut memetakan keterkaitan antara 4 Aktor Sistem dengan seluruh Use Case:

| Kode Use Case | Nama Use Case | Pembaca Publik (Reader) | Klien Korporat B2B/B2G | Admin Sistem | Sync Engine |
|---|---|:---:|:---:|:---:|:---:|
| **UC-AUTH-01** | Registrasi Pembaca Baru (Default READER via /auth/register) | **[Primary]** | — | [Audit] | — |
| **UC-AUTH-02** | Masuk Akun Pengguna & Penerbitan JWT (/auth/login) | **[Primary]** | **[Primary]** | [Audit] | — |
| **UC-AUTH-03** | Manajemen Profil & Sesi Persisten | **[Primary]** | **[Primary]** | [Audit] | [Informed] |
| **UC-LEAD-01** | Pengajuan Permintaan Konsultasi Strategis (Budi Santoso) | — | **[Primary]** | [Review] | — |
| **UC-BOOK-01** | Interaksi Pembaca: Aksi "Read" & "Add to Library" | **[Primary]** | **[Primary]** | — | — |
| **UC-BOOK-02** | Eksplorasi Bento Grid (Bab Kunci & Profil Putu Aan J.) | **[Primary]** | **[Primary]** | — | — |
| **UC-CAT-01** | Pencarian & Filter Multikategori E-Book | **[Primary]** | **[Primary]** | — | — |
| **UC-MERCH-01**| Eksplorasi Merchandise & Customize Solution | **[Primary]** | **[Primary]** | — | — |
| **UC-I18N-01** | Pengalihan Bahasa Antarmuka (EN / ID) | **[Primary]** | **[Primary]** | — | — |
| **UC-SYNC-01** | Sinkronisasi Status Sesi Real-Time (WebSocket) | [Client] | [Client] | [Monitor] | **[Primary]** |
| **UC-ADM-01**  | Pemantauan Sesi, Audit Keamanan & Tata Kelola | — | — | **[Primary]** | [Assists] |

---

## 5. Spesifikasi Integrasi Kontrak API & Endpoints

| Method | Endpoint | Use Case Terkait | Deskripsi Payload & Respons |
|---|---|---|---|
| `POST` | `/auth/register` (alias `/auth/signup`) | UC-AUTH-01 | **Req**: `{ "username": "...", "password": "..." }`<br>**Res**: `200 OK` `{ "token": "...", "user": { "username": "...", "id": "..." } }`<br>**Err**: `409 Conflict` ("User already exists") |
| `POST` | `/auth/login` | UC-AUTH-02 | **Req**: `{ "username": "...", "password": "..." }`<br>**Res**: `200 OK` `{ "token": "...", "user": { "username": "..." } }`<br>**Err**: `401 Unauthorized` ("Invalid credentials") |
| `GET` | `/profile` | UC-AUTH-03 | **Header**: `Authorization: Bearer <jwt>`<br>**Res**: `200 OK` `{ "id": "...", "username": "...", "fullName": "...", "email": "..." }` |
| `POST` | `/profile` | UC-AUTH-03 | **Header**: `Authorization: Bearer <jwt>`<br>**Req**: `{ "id": "...", "username": "...", "fullName": "...", "email": "..." }`<br>**Res**: `200 OK` updated profile object |
| `GET` | `/protected` | UC-AUTH-02/03 | **Header**: `Authorization: Bearer <jwt>`<br>**Res**: `200 OK` ("Hello, <username>!") |
| `WS` | `/ws/sync` | UC-SYNC-01 | **Protocol**: Ktor WebSockets bidirectional session sync & notification frame |
| `GET` | `/swagger` | UC-ADM-01 | Swagger UI interactive documentation viewer |
| `GET` | `/openapi/documentation.yaml`| UC-ADM-01 | OpenAPI 3.0 specification contract file |

---

## 6. Standar Desain Sistem & Kepatuhan Non-Fungsional

1. **Aturan Warna Desain Editorial Sekota**:
   - **Tinta Primer (Ink Navy)**: `#0D1F2D` — Digunakan untuk seluruh tipografi utama, tombol primer, dan kartu gelap. **Larangan Mutlak: Tidak boleh menggunakan warna `#000000` murni di seluruh antarmuka**.
   - **Tinta Sekunder**: `#143244` pada opasitas 40%–60% untuk teks paragraf dan deskripsi.
   - **Brand Teal**: `#00B5C8` / `#02B6CF` — Digunakan untuk teks aksen, link aktif, dan elemen branding.
   - **Brand Green**: `#60BD65` / `#46B778` — Digunakan untuk tombol primer *"Read"*, rating bintang, dan progress bar membaca.
   - **Surface Pure & Alt**: `#FFFFFF` (permukaan putih bersih) berselang-seling dengan `#F7FAFB` (Surface Alt) untuk ritme visual teratur.

2. **Hirarki Tipografi**:
   - **Montserrat**: Digunakan eksklusif untuk Display Headings (H1 Hero 88px, H2 Seksi 48px/36px, Judul Buku).
   - **DM Sans**: Digunakan untuk teks isi (*body copy*), tombol, form fields, label *eyebrow* (ALL CAPS dengan letter spacing +2px), dan navigasi.

3. **Mandatory Desktop Previews**:
   - Setiap berkas composable top-level (Screen, Section, Component) **wajib** menyertakan fungsi `@Preview(device = DESKTOP)` yang dibungkus dalam `MaterialTheme`.

4. **Kepatuhan Keamanan Sistem (ISO 27001 & OWASP)**:
   - **ISO 27001 A.8.28**: Pencegahan injeksi SQL melalui penggunaan *Exposed ORM Parameterized DSL*.
   - **ISO 27001 A.8.12**: Pencegahan kebocoran kredensial dengan penyimpanan token terenkripsi pada SQLDelight dan isolasi variabel lingkungan.
   - **OWASP ASVS 4.0**: Autentikasi berbasis JWT bertanda tangan HMAC256 dengan batas waktu kedaluwarsa terukur (1 jam).
