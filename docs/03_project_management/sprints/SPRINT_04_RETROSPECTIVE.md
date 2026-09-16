# Sprint 4 Retrospective Report
**Dokumen Referensi: PM-FRM-21 / ISO 21502:2020 & Sekota PM Framework**
**Project: Sekota Web & CMS Platform**
**Sprint: Sprint 4 (Refinement: CMS Tri-Platform & Auth-Gating)**

---

## 1. Ringkasan Retrospektif

Sesi retrospektif Sprint 4 mengevaluasi proses transisi dari purwarupa (*prototype*) ke rekayasa sistem siap produksi (*production engineering*), implementasi isolasi batas target platform (Web vs CMS), dan sinkronisasi berkas tata kelola OpenProject PMO.

---

## 2. Analisis 4L (Liked, Learned, Lacked, Longed For)

### 🟢 Liked (Hal-Hal Positif)
- **Tri-Platform CMS Architecture**: Berhasil menjalankan CMS secara mandiri di Desktop JVM, Android, dan CLI tanpa mengorbankan performa atau mengotori kode Web publik.
- **Clean Architecture Domain Isolation**: Ekstraksi UseCases (`AdminUseCases.kt`) membuat logika bisnis sepenuhnya teruji (*testable*) tanpa bergantung pada Compose atau Ktor.
- **Automated OpenProject Sync**: Skrip Python berhasil menyinkronkan status 6 Work Package (`#22398` s.d `#22403`) menjadi *Closed* dengan akurasi persentase kemajuan 100% dan assignee yang tepat.

### 💡 Learned (Pelajaran Teknis & Manajerial)
- **Multiplatform System Timestamp**: `System.currentTimeMillis()` tidak tersedia pada target `wasmJsMain`. Gunakan generator acak multiplatform seperti `kotlin.random.Random.nextInt(...)` atau `kotlinx-datetime` untuk ID generation di `commonMain`.
- **Ktor Engine Dependency in Multiplatform**: Engine HTTP client seperti `ktor-client-cio` harus dideklarasikan secara eksplisit di target `jvmMain` pada shared module agar JVM runtime tidak melempar `IllegalStateException`.
- **OpenProject Parent WP Immutability**: Work package induk yang memiliki sub-tasks secara otomatis menghitung *Progress* dan *Due Date* dari anak-anaknya. Mengirimkan nilai `percentageDone` atau `dueDate` pada parent mengembalikan status `422 PropertyIsReadOnly`.

### 🟡 Lacked (Kekurangan & Titik Hambat)
- **Persistent Storage pada CMS**: CMS saat ini masih menggunakan repositori in-memory berbasis UseCases. Perlu peningkatan integrasi ke SQLDelight lokal atau API server persisten.
- **Design System Documentation Gap**: Sempat terjadi celah dokumentasi formal Design System di Fase 2 yang baru dilengkapi setelah audit review.

### 🔵 Longed For (Harapan & Inisiatif Berikutnya)
- **SQLDelight Cache untuk CMS**: Memungkinkan data buku dan merchandise tersimpan secara permanen di disk lokal saat dijalankan di Desktop JVM atau Android.
- **Native Packaging**: Pembuatan bundel instalasi mandiri (`.dmg` untuk macOS dan `.apk` rilis untuk Android tablet).
- **JWT Refresh Rotation**: Mekanisme auto-refresh token autentikasi saat sesi login kadaluarsa.

---

## 3. Rencana Aksi Perbaikan (Actionable Kaizen)

| No | Tindakan Perbaikan | Penanggung Jawab | Target Implementasi |
|---|---|---|---|
| 1 | Integrasikan SQLDelight table untuk persistent storage entitas CMS (Books, Products, Merchandise) | Dev Lead / Harits | Sprint 5 |
| 2 | Tambahkan packaging task desktop (`packageDistributionForCurrentOS`) pada CI pipeline | DevOps / Harits | Sprint 5 |
| 3 | Terapkan validasi input form dialog lebih komprehensif (ISBN regex, format mata uang dinamis) | UI/UX & Frontend | Sprint 5 |

---

## 4. Status Penutupan

Sprint 4 dinyatakan **SELESAI dan DITUTUP SECARA RESMI**. Seluruh perubahan kode, spesifikasi, dan dokumen tata kelola siap di-commit ke Git.
