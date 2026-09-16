# Definition of Done (DoD): Sekota Web Platform
**Dokumen Referensi: PM-FRM-04 / ISO/IEC 27001:2022 & SNI ISO 9001:2015**

Dokumen ini merupakan kontrak mutu repo-native yang mengikat seluruh hasil kerja (increments) dalam siklus Sprint pembangunan Sekota Web Platform.

---

## 1. Standar Arsitektur & Rekayasa Perangkat Lunak (Clean & Screaming Architecture)

- [ ] **Lapisan Domain Murni (Zero Dependency)**:
  - Seluruh logika bisnis di `shared/src/commonMain/kotlin/com/sekota/features/<feature>/domain/` murni menggunakan Kotlin murni tanpa dependensi Ktor, SQLDelight driver platform, atau framework UI.
  - Setiap use case merupakan interactor mandiri berekstensi `*UseCase` yang mengimplementasikan `operator fun invoke(...)`.
  - Repository interfaces didefinisikan di domain dan mengembalikan `Result<T>`.
- [ ] **Lapisan Data Terisolasi**:
  - Implementasi repository di `shared/.../features/<feature>/data/` mengelola Ktor client, query SQLDelight, dan serialisasi data.
  - Network DTO dipisahkan dari Domain Entities menggunakan mapper.
- [ ] **Screaming Architecture**:
  - Penataan kode berorientasi fitur bisnis (`auth`, `profile`, `catalog`, `merchandise`, `sync`).
- [ ] **Mandatory Desktop Previews**:
  - Setiap top-level Composable (Screen, Section, Component) wajib menyertakan `@Preview(device = DESKTOP)`.
  - Preview wajib dibungkus dalam container tema (`MaterialTheme`).

---

## 2. Standar Desain & Tipografi (Sekota Editorial Design Tokens)

- [ ] **Palet Warna Terbatas**:
  - Menggunakan Ink Navy (`#0D1F2D`), Ink Secondary (`rgba(20, 50, 68, 0.6)`), Surface Pure (`#FFFFFF`), Surface Alt (`#F7FAFB`), Brand Teal (`#00B5C8`), Brand Green (`#60BD65`), dan Whisper Hairline (`rgba(13, 31, 45, 0.1)`).
  - Dilarang keras menggunakan warna hitam pekat (`#000000`) dan efek glow neon bebas.
- [ ] **Tipografi Terstandar**:
  - Headline/Display wajib menggunakan font `Montserrat` (bold / extra-bold track-tight).
  - Body copy dan uppercase tracked category eyebrows wajib menggunakan `DM Sans`.
  - Font `Inter` hanya diizinkan untuk mockup widget data analitik.

---

## 3. Kontrol Keamanan Informasi (ISO/IEC 27001:2022 Annex A)

Mengintegrasikan 11 kontrol baru ISO 27001:2022:
- [ ] **A.5.7 Threat Intelligence**: Dependensi Ktor (`3.4.3`), Compose (`1.10.3`), SQLDelight (`2.1.0`), dan Exposed (`0.47.0`) terverifikasi tanpa CVE kritis.
- [ ] **A.5.23 Information Security for Cloud Services**: Model shared responsibility diterapkan pada infrastruktur host dan container.
- [ ] **A.8.9 Configuration Management**: File environment `.env` diabaikan oleh Git (`.gitignore` & `.dockerignore`).
- [ ] **A.8.10 Information Deletion**: Pembersihan session token saat logout (`ClearTokenUseCase`).
- [ ] **A.8.11 Data Masking**: Penyamaran password dan data sensitif pada antarmuka input login/signup.
- [ ] **A.8.12 Data Leakage Prevention (DLP)**: Tidak ada token atau secret yang di-hardcode ke dalam repository Git atau log terminal.
- [ ] **A.8.16 Monitoring Activities**: Request logging aktif pada Ktor Server menggunakan `CallLogging` dan `Logback`.
- [ ] **A.8.28 Secure Coding**: OWASP Top 10 terpenuhi (pencegahan SQL Injection via Exposed Parameterized Query, XSS & CSRF prevention).

---

## 4. Penjaminan Mutu Digital (SNI ISO 9001:2015 Kl. 7.1.5.1)

- [ ] Kompilasi proyek sukses tanpa error:
  - `./gradlew check` dan kompilasi Wasm `./gradlew :composeApp:wasmJsBrowserDistribution`.
  - Server test suite lolos (`./gradlew :server:test`).
- [ ] Verifikasi containerisasi: Image Docker berhasil dibangun dan dijalankan via `docker compose up --build -d` atau `./run-local-podman.sh`.

---

## 5. Tata Kelola Pelaporan & Sinkronisasi Task (OpenProject)

- [ ] Setiap subtask dan Work Package terkait diperbarui statusnya di OpenProject (`Completed` / `Closed`) dengan `lockVersion` yang telah divalidasi.
- [ ] Berkas sprint lokal (`.openproject/backlogs/sprint_*.json`) dan `product_backlog.md` sinkron dengan status live.
- [ ] Walkthrough dokumentasi diperbarui sebelum serah terima sprint.
