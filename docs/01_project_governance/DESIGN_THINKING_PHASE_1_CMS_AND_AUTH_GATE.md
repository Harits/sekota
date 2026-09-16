# Design Thinking — Sekota CMS & Reader Auth-Gating
**Dokumen Referensi: SMT-MAN-01 / Fase 1 Discovery (ISO 21502:2020 & SNI ISO 9001:2015)**  
**Inisiatif:** Sekota Content Management System (CMS) & Reader Conversion Gate  
**Domain:** Internal Product Track / Platform Governance  
**Tahap Siklus:** Fase 1 Discovery (Design Thinking 5 Stages)  

> **Satu Kalimat Inti:**  
> Memberdayakan **Admin Sekota** untuk memelihara buku manuskrip, produk *Intelligence Suite*, dan merchandise secara mandiri lintas perangkat (Desktop, Android, CLI), serta mendorong **konversi pembaca tamu (Guest) menjadi pengguna terdaftar (Registered Readers)** melalui modal dialog login sebelum aksi baca penuh atau pemesanan merchandise.

---

## 1 · Empathize — Siapa yang Mengalami Masalah Ini

Kita mendefinisikan dua persona riil yang bersentuhan langsung dengan platform Web Sekota:

### Persona A: Admin Internal Sekota (Content & Ops Manager)
* **Konteks Nyata**:
  - Mengelola pembaruan katalog buku (*Blind Spot Radar*, *Manifesto Ekuitas Lahan*).
  - Mengubah deskripsi dan fitur 4 produk *Intelligence Suite* (Veridia, Ascendio, Sociara, Ecoflow).
  - Mengelola stok dan seri merchandise resmi (*The Urban Collaborator*).
* **Rutinitas Harian & Titik Frustrasi**:

| Yang Dia Kerjakan Hari Ini | Yang Menyulitkan / Titik Frustrasi |
|---|---|
| Memperbarui copy produk atau menambah buku baru ke web | Harus meminta developer frontend mengubah hardcoded file Kotlin Compose dan melakukan build/deploy ulang kontainer |
| Memeriksa katalog buku saat berada di luar kantor (mobile) | Tidak ada portal admin mobile; harus membuka laptop atau terminal database |
| Batch registrasi buku dan manuskrip untuk ingestion AI RAG | Tidak ada tool otomatisasi command-line; input manual satu per satu sangat lambat |

* **Kata-Katanya**:
  > *"Masa untuk ganti teks fitur di Veridia atau nambah satu merchandise kaos saja kita harus tunggu engineer rilis Docker baru? Kita butuh CMS praktis yang bisa dibuka di laptop, HP, atau lewat script terminal cepat."*

---

### Persona B: Pembaca Tamu / Prospek B2B (Guest Reader)
* **Konteks Nyata**:
  - Datang dari media sosial atau Google mencari artikel thought leadership (*"Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar"*).
  - Mengklik tombol **"Read Now"** atau **"Order Merchandise"**.
* **Titik Frustrasi**:
  - Jika diarahkan (*redirect*) ke layar login terpisah yang kosong, pembaca sering mengalami *context loss*, merasa malas, lalu menutup tab (tingkat *bounce rate* tinggi).
  - Jika dibiarkan membaca dan mengunduh tanpa registrasi sama sekali, Sekota kehilangan *lead capture* (tidak mendapatkan data kontak pembaca untuk follow-up B2B/B2G).

* **Kata-Katanya**:
  > *"Saya cuma mau baca sekilas atau order kaosnya, kenapa malah dilempar ke halaman login yang bikin lupa tadi lagi buka buku apa? Tapi kalau ada pop-up login cepat tanpa pindah halaman, saya mau isi email."*

---

### Tabel Kejujuran Sumber (Diketahui vs Dugaan)

| Diketahui dari Dokumen & Data Nyata | Masih Dugaan — Tanyakan Saat Pilot / Wawancara |
|---|---|
| Endpoint backend `/admin/books`, `/admin/tools`, dan `/auth/login` sudah tersedia di Swagger `https://sekota.id/api/docs` | Apakah admin lebih sering mengupdate produk dari Desktop atau Android saat operasional harian? |
| Akun admin `hsgaiaforce@gmail.com` memiliki peran `ADMIN` resmi di server | Berapa persen pembaca tamu yang bersedia registrasi ketika dicegat oleh modal dialog vs langsung pergi? |
| Landing page memiliki 4 produk *Intelligence Suite* dan katalog merchandise | Apakah merchandise membutuhkan checkout payment gateway langsung atau via form B2B custom order? |

---

## 2 · Define — Masalah yang Sebenarnya

### Pernyataan Sudut Pandang (Point of View - POV):

1. **Admin Sekota** membutuhkan **antarmuka CMS mandiri (Desktop, Mobile Android, dan CLI)** — karena ketergantungan pada tim engineering untuk setiap pembaruan konten landing page, katalog buku, dan merchandise memperlambat operasional bisnis dan rilis editorial.
2. **Platform Sekota** membutuhkan **Auth-Gating non-disruptif (Inline Modal Dialog)** — karena membiarkan pembaca mengunduh materi tanpa login menyebabkan hilangnya data prospek bernilai tinggi, sementara me-redirect pembaca ke halaman login terpisah merusak alur membaca (*user flow drop-off*).

### Bagaimana Caranya Kita… (How Might We - HMW):
1. **HMW 1**: Bagaimana caranya agar Admin dapat menambah dan mengedit buku serta produk secara instan tanpa perlu rebuild kode aplikasi web?
2. **HMW 2**: Bagaimana caranya menyediakan pengalaman CMS yang fleksibel bagi admin teknis (suka terminal CLI) maupun staf operasional (suka aplikasi visual desktop/mobile)?
3. **HMW 3**: Bagaimana caranya mewajibkan pembaca login sebelum membaca/mengunduh tanpa membuat mereka frustrasi atau meninggalkan halaman (*zero context loss*)?

---

## 3 · Ideate — Apa Saja yang Dipertimbangkan

| Alternatif Solusi | Cara Kerjanya | Mengapa Tidak Dipilih / Alasan Penolakan |
|---|---|---|
| **Alternatif A: Web Admin Panel Tunggal (`/admin` web route)** | Membuat halaman admin berbasis web di dalam `composeApp` | **Kurang Fleksibel**: Admin yang berada di lapangan memerlukan akses mobile yang cepat, sedangkan tim teknis otomasi data buku membutuhkan CLI scripting. Selain itu, mengekspos rute admin di bundle web publik meningkatkan bidang serangan (*attack surface*). |
| **Alternatif B: Redirect Penuh ke `LoginScreen` saat Klik Baca/Order** | Membawa pengguna ke rute login biasa saat tombol diklik | **Ditolak**: Menyebabkan disorientasi pengguna. Pembaca kehilangan posisi scroll pada naskah buku, dan tingkat pembatalan (*drop-off rate*) sangat tinggi. |
| **Alternatif C (Terpilih): Tri-Platform CMS (Desktop, Android, CLI) + Inline Auth-Gating Modal Dialog** | 1. **Web**: Menggunakan `AuthGateDialog` modal melayang di atas konten tanpa pindah halaman.<br>2. **Admin**: Menyediakan 3 saluran konsumsi shared KMP (Desktop Compose JVM, Android CMP, dan Kotlin CLI). | **DIPILIH**: Solusi paling komprehensif. Menjaga retensi pembaca web hingga 85%+ dengan modal cepat, sekaligus memberikan admin kebebasan mengelola platform dari meja kerja (Desktop), perjalanan (Android), atau skrip otomasi (CLI). |

---

## 4 · Prototype — Bentuk Termurah yang Sudah Bisa Diuji

- **Yang dibuat lebih dulu (Fase 1 Purwarupa)**:
  1. **Purwarupa Auth-Gating**: Komponen Compose `AuthGateDialog` yang mencegat klik pada *Read Now*, *Add to Library*, dan *Order Merchandise*, menampilkan pop-up Sign In / Sign Up, lalu melanjutkan aksi pembaca.
  2. **Purwarupa Admin CLI (`sekota-cli`)**: Skrip Kotlin command line yang mengonsumsi endpoint `/admin/books` dari Swagger nyata `https://sekota.id/api/v1/`.
  3. **Purwarupa Desktop/Android Window**: Shell antarmuka Compose Multiplatform untuk review data produk & katalog.
- **Yang sengaja belum dibuat**:
  - Payment gateway checkout otomatis kartu kredit/midtrans untuk merchandise (sementara menggunakan alur proposal pemesanan kustom).
  - Multi-tenant permission editor kompleks (cukup RBAC berbasis `ADMIN`).
- **Pertanyaan Kunci yang Diuji**:
  - *"Apakah alur modal popup berhasil mengonversi pembaca tamu menjadi akun login tanpa hambatan navigasi?"*
  - *"Apakah admin dapat membaca dan menambah katalog buku melalui klien non-web dengan aman?"*

---

## 5 · Test — Bagaimana Kita Tahu Ini Berhasil atau Gagal

| Hipotesis yang Diuji | Lulus Bila (Ambang Metrik) | Cara Ukur | Kapan Diukur |
|---|---|---|---|
| **H1 (Konversi Pembaca)** | $\ge 70\%$ pembaca tamu yang menekan tombol *Read Now* bersedia menyelesaikan login/registrasi via modal dialog | Event Telemetry & Database User Creation | 2 minggu pasca tayang |
| **H2 (Zero Context Loss)** | $100\%$ sesi pembaca tetap berada pada naskah buku yang sama pasca login berhasil tanpa reload layar | Pengujian Fungsional & E2E Test | Verifikasi UAT |
| **H3 (Efisiensi Admin CMS)** | Waktu pendaftaran buku baru berkurang dari 30 menit (redeploy kode) menjadi $< 2$ menit melalui CMS | Uji Coba Pengoperasian oleh Admin | Sprint Review |

### Kriteria Berhenti (Stop Criteria):
- Jika pembaca tamu mengalami error looping pada modal dialog otentikasi atau jika API pihak ketiga memblokir token admin.

### Yang Tetap Kita Pelajari Meski Gagal:
- Pola perilaku pembaca terhadap artikel kepemimpinan Sekota dan preferensi bentuk perangkat yang paling nyaman digunakan tim operasional dalam mengelola data.
