# 07 — Test Strategy

Tujuan: setiap perubahan menghasilkan laporan otomatis yang bisa dibaca tanpa menjalankan UI manual, sementara risiko visual tetap ditutup oleh screenshot otomatis.

## 1. Piramida pengujian

```
                 ┌──────────────────────────┐
                 │  Visual / E2E (Puppeteer) │  screenshot desktop & mobile
                 ├──────────────────────────┤
                 │  UI Compose (runComposeUiTest) │  6 smoke/interaction tests
                 ├──────────────────────────┤
                 │  ViewModel & katalog     │  StateFlow, bilingual, navigasi
                 ├──────────────────────────┤
                 │  Unit domain             │  validator, submit, use case
                 └──────────────────────────┘
```

| Level | Cakupan | Alat | Lokasi |
|---|---|---|---|
| Unit domain | validasi kontak, pembentukan draft, use case produk, navigasi | `kotlin.test` | `commonTest/.../domain`, `commonTest/.../app` |
| ViewModel | state FAQ, highlight, tab galeri, form lifecycle, prefill | `kotlin.test` + StateFlow | `commonTest/.../presentation` |
| Katalog/integrasi | jumlah section, kelengkapan ID/EN, preset, anchor, draf legal | `kotlin.test` | `commonTest/.../content`, `commonTest/.../legal` |
| UI Compose | render hero, toggle bahasa, FAQ, submit form, produk, buku, privasi, syarat | `runComposeUiTest` (desktop) | `jvmTest/.../SekotaUiTest.kt` |
| Cross-target | seluruh unit/VM/katalog dijalankan di Wasm (browser) | Kotlin/Wasm test + Karma | `wasmJsBrowserTest` |
| Server | `/health` + `POST /api/contact` (terkirim, validasi, gagal, belum dikonfigurasi) + `FormSubmitMailer` (body/origin) | `testApplication` + fake mailer + `HttpServer` lokal | `server/src/test` |
| Visual | layout 1440 & 390, scroll, halaman produk/buku/kontak | Puppeteer + Chrome headless | `tools/visual/*.mjs` |

## 2. Matriks test

| Area | Test | Level |
|---|---|---|
| Validasi nama/email/pesan | `ContactValidatorTest` (7) | Unit |
| Draft email & label bahasa | `SubmitContactMessageTest` (3) | Unit |
| State form, prefill, reset, kirim sukses/gagal | `ContactViewModelTest` (7) | VM |
| Highlight & FAQ | `LandingViewModelTest` (6) | VM |
| Tab galeri, guard aset & tur otomatis | `ProductViewModelTest` (5) | VM |
| Stack navigasi & anchor | `AppNavigatorTest` (7) | Unit |
| Kelengkapan konten ID/EN | `ContentCatalogTest` (6) | Integrasi katalog |
| Deep link & URL | `HashRouteTest` (8) | Unit |
| Kelengkapan dokumen legal ID/EN, ID unik, tautan | `LegalCatalogTest` (4) | Integrasi katalog |
| Scroll-spy TOC: akhir dokumen, reading line, klik, dokumen pendek | `ActiveSectionTest` (13) | Unit |
| Landing render | `SekotaUiTest.landingRendersHero` | UI |
| Toggle bahasa | `SekotaUiTest.languageToggleSwitchesToEnglish` | UI |
| FAQ interaksi | `SekotaUiTest.faqOpensOnClick` | UI |
| Form kontak validasi + status terkirim | `SekotaUiTest.contactFormValidatesAndSubmits` (fake gateway) | UI |
| API kontak + SMTP (mock mailer) | `server/.../ApplicationTest` | Integration |
| Keyboard scroll, document lang, browser back | `tools/visual/a11y.mjs` | E2E a11y |
| Preset demo/mitra/buku & anchor deep link | `tools/visual/pages.mjs` (`data-route` assertions) | E2E visual |
| Halaman produk | `SekotaUiTest.productPageRendersHeadline` | UI |
| Halaman buku | `SekotaUiTest.booksPageRendersHero` | UI |
| Halaman privasi | `SekotaUiTest.privacyPageRendersPolicy` | UI |
| Server health + kontak + FormSubmit | `server/.../ApplicationTest` (6) | Integration |
| Visual landing | `tools/visual/shots.mjs` | E2E visual |
| Visual produk/kontak/buku/EN | `tools/visual/pages.mjs` | E2E visual |

## 3. Hasil terakhir

| Suite | Jumlah | Lulus | Gagal |
|---|---|---|---|
| Shared JVM (unit + VM + katalog + 8 UI) | 77 | 77 | 0 |
| Shared Wasm (unit + VM + katalog) | 69 | 69 | 0 |
| Server (health + kontak + FormSubmit) | 6 | 6 | 0 |

Detail per suite ada di [11-test-report.md](11-test-report.md) (dibuat ulang oleh skrip).

## 4. Cara menjalankan

```powershell
# Semua test inti + laporan Markdown
.\scripts\test-report.ps1

# Sertakan test Wasm (butuh Chrome/Edge headless)
.\scripts\test-report.ps1 -IncludeWasm

# Manual
.\gradlew.bat :app:shared:jvmTest :server:test
.\gradlew.bat :app:shared:wasmJsTest
.\gradlew.bat :app:shared:jvmTest --tests "id.sekota.SekotaUiTest"
.\gradlew.bat :server:test --tests "id.sekota.ApplicationTest"

# HTML report
# app/shared/build/reports/tests/jvmTest/index.html
# app/shared/build/reports/tests/wasmJsBrowserTest/index.html
# server/build/reports/tests/test/index.html
```

## 5. Verifikasi visual

```powershell
.\gradlew.bat :app:webApp:wasmJsBrowserDistribution
# jalankan server statis dari app/webApp/build/dist/wasmJs/productionExecutable
cd tools\visual
node shots.mjs     # landing 1920, 1440, 900, 390
node pages.mjs     # produk, kontak, preset, buku, EN, anchor, product switch (assert data-route)
node a11y.mjs      # keyboard scroll, document lang, browser back, reduced motion
```

Screenshot disimpan di `docs/screenshots/` untuk 390dp (mobile), 900dp (tablet), 1440dp (desktop), dan 1920dp (referensi desain). Kriteria lulus:
- Tidak ada scroll horizontal pada 390dp.
- Tidak ada teks terpotong pada konten penting (suite row mobile sudah full text).
- Header/footer tidak overlap.
- Semua section landing muncul berurutan.
- Halaman produk/buku/kontak menampilkan konten sesuai bahasa.

## 6. Quality gate (Definition of Done)

1. `:app:shared:jvmTest` dan `:server:test` hijau.
2. `:app:shared:wasmJsTest` hijau untuk perubahan `commonMain`.
3. Build web produksi berhasil.
4. Screenshot desktop & mobile diperbarui bila ada perubahan layout.
5. Tidak ada error kompilasi di Android/Desktop (`assembleDebug`, `compileKotlin`).
6. Requirement/use case/traceability diperbarui bila perilaku berubah.

## 7. Target cakupan & risiko

| Area | Target | Status |
|---|---|---|
| Domain & view model | 100% use case/validator | ✅ |
| Katalog konten | 100% kunci utama diperiksa | ✅ (representatif) |
| UI | Smoke + interaksi kritis | ✅ 6 test |
| E2E browser | Semua rute & form | ⚠️ visual + submit kontak manual terbukti (API 202, email masuk SMTP sink) |
| iOS/Android UI | Smoke di device | ❌ backlog |
| Aksesibilitas | Keyboard, lang, reduced motion | ✅ otomatis dasar; audit screen reader lanjutan backlog |
| Kinerja | Budget ukuran & LCP | ❌ backlog (bundle dicatat) |

## 8. Rencana penguatan

- Tambah test UI untuk preset kontak, galeri tab, menu mobile.
- Tambah Playwright end-to-end yang mengklik rute nyata (bukan koordinat).
- Tambah screenshot diff (baseline) agar regresi visual terdeteksi.
- Perluas screenshot reduced-motion menjadi baseline diff (saat ini hanya cek hero terlihat).
- Tambah test Android instrumented untuk `MainActivity`.
- Ukur ukuran bundle & waktu muat di CI.
