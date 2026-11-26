# 11 - Laporan Test

> Dibuat otomatis oleh `scripts/test-report.ps1` pada 2026-11-26 14:50.

- **Status:** OK LULUS
- **Perintah:** `gradlew :app:shared:jvmTest :server:test :app:shared:wasmJsTest`
- **Total:** 152 test, 0 gagal, 0 error, 0 dilewati

## Ringkasan per target

| Target | Suite | Test | Gagal | Error | Dilewati |
|---|---:|---:|---:|---:|---:|
| Shared JVM | 13 | 77 | 0 | 0 | 0 |
| Shared Wasm | 12 | 69 | 0 | 0 | 0 |
| Server | 1 | 6 | 0 | 0 | 0 |

## Detail per suite

| Target | Suite | Test | Gagal | Error | Dilewati | Waktu |
|---|---|---:|---:|---:|---:|---:|
| Server | `id.sekota.ApplicationTest` | 6 | 0 | 0 | 0 | 1.149 s |
| Shared JVM | `ActiveSectionTest` | 13 | 0 | 0 | 0 | 0.001 s |
| Shared JVM | `AppNavigatorTest` | 7 | 0 | 0 | 0 | 0.004 s |
| Shared JVM | `ContactValidatorTest` | 7 | 0 | 0 | 0 | 0.001 s |
| Shared JVM | `ContactViewModelTest` | 7 | 0 | 0 | 0 | 0.013 s |
| Shared JVM | `ContentCatalogTest` | 6 | 0 | 0 | 0 | 0.002 s |
| Shared JVM | `HashRouteTest` | 8 | 0 | 0 | 0 | 0.002 s |
| Shared JVM | `LandingViewModelTest` | 6 | 0 | 0 | 0 | 0.002 s |
| Shared JVM | `LegalCatalogTest` | 4 | 0 | 0 | 0 | 0.002 s |
| Shared JVM | `ProductViewModelTest` | 5 | 0 | 0 | 0 | 0.002 s |
| Shared JVM | `SekotaUiTest` | 8 | 0 | 0 | 0 | 4.919 s |
| Shared JVM | `SharedCommonTest` | 2 | 0 | 0 | 0 | 0.006 s |
| Shared JVM | `SharedLogicDesktopTest` | 1 | 0 | 0 | 0 | 0.001 s |
| Shared JVM | `SubmitContactMessageTest` | 3 | 0 | 0 | 0 | 0.002 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.app.AppNavigatorTest` | 7 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.app.HashRouteTest` | 8 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.content.ContentCatalogTest` | 6 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.contact.ContactValidatorTest` | 7 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.contact.ContactViewModelTest` | 7 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.contact.SubmitContactMessageTest` | 3 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.landing.LandingViewModelTest` | 6 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.legal.LegalCatalogTest` | 4 | 0 | 0 | 0 | 0.002 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.legal.presentation.ActiveSectionTest` | 13 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.feature.product.ProductViewModelTest` | 5 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.SharedCommonTest` | 2 | 0 | 0 | 0 | 0.0 s |
| Shared Wasm | `wasmJsBrowserTest.id.sekota.SharedLogicWebTest` | 1 | 0 | 0 | 0 | 0.0 s |

## Artefak

- HTML JVM: `app/shared/build/reports/tests/jvmTest/index.html`
- HTML Wasm: `app/shared/build/reports/tests/wasmJsBrowserTest/index.html`
- HTML server: `server/build/reports/tests/test/index.html`
- XML: `app/shared/build/test-results`, `server/build/test-results`
- Screenshot: `docs/screenshots/`

Ukuran distribusi web: 658820f72b4617efb380.wasm 8.25 MiB, bde3bbf45d0bb67b1ff9.wasm 3.85 MiB, webApp.js 527 KiB.

## Catatan

- Jalankan dengan `-IncludeWasm` untuk menyertakan pengujian browser Wasm.
- Jika suite tidak muncul, jalankan Gradle tanpa cache: `gradlew --rerun-tasks <tasks>`.
- Laporan ini hanya mencakup test otomatis; verifikasi visual dilakukan oleh `tools/visual/*.mjs`.