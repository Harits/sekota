# 12 — Runbook

Semua perintah dari root repo `D:\Sekota\2026\SekotaWithNewDesign`. Di PowerShell gunakan `.\gradlew.bat`; di Git Bash `./gradlew`.

## 1. Menjalankan web (target utama)

```powershell
# Development server (hot reload webpack)
.\gradlew.bat :app:webApp:wasmJsBrowserDevelopmentRun

# Build produksi
.\gradlew.bat :app:webApp:wasmJsBrowserDistribution

# Serve hasil produksi (butuh MIME .wasm yang benar; python http.server memadai di Python 3.13)
cd app\webApp\build\dist\wasmJs\productionExecutable
python -m http.server 8765 --bind 127.0.0.1
# buka http://127.0.0.1:8765/
```

JS fallback (browser lama): `.\gradlew.bat :app:webApp:jsBrowserDevelopmentRun`.

## 2. Desktop

```powershell
.\gradlew.bat :app:desktopApp:run
# hot reload (bila plugin hot reload tersedia)
.\gradlew.bat :app:desktopApp:hotRun --auto
```

## 3. Android

```powershell
.\gradlew.bat :app:androidApp:assembleDebug
# APK: app\androidApp\build\outputs\apk\debug\
```

`local.properties` harus berisi `sdk.dir` (sudah ada di mesin ini).

## 4. iOS

Buka `app/iosApp` di Xcode, jalankan target `iosApp`. Framework `Shared` dibangun oleh Gradle saat build Xcode.

## 5. Server & API kontak

```powershell
# Endpoint: GET /health, POST /api/contact
$env:PORT="8080"                              # opsional (default 8080)
$env:SEKOTA_SMTP_HOST="smtp.contoh.co.id"
$env:SEKOTA_SMTP_PORT="587"
$env:SEKOTA_SMTP_USER="no-reply@sekota.id"
$env:SEKOTA_SMTP_PASSWORD="••••"
$env:SEKOTA_MAIL_FROM="no-reply@sekota.id"
$env:SEKOTA_MAIL_TO="info@sekota.id"          # default
.\gradlew.bat :server:run
# http://localhost:8080/health
```

Tanpa `SEKOTA_SMTP_HOST`, endpoint mengembalikan `503 not_configured`; UI menampilkan error + email langsung. Web statis di `:8765` memanggil `http://127.0.0.1:8090` (ubah `PORT=8090` bila 8080 dipakai proses lain). Jika `SEKOTA_WEB_DIR` diisi, server juga menyajikan build web di `/` sehingga API same-origin.

**Alternatif FormSubmit (tanpa SMTP):** set `SEKOTA_FORMSUBMIT_TO` (wajib aktivasi sekali lewat email dari FormSubmit) dan opsional `SEKOTA_FORMSUBMIT_ORIGIN`. Urutan pemilihan mailer: argumen eksplisit → SMTP (`SEKOTA_SMTP_HOST`) → FormSubmit. FormSubmit hanya untuk uji coba; email bisa masuk Spam dan isi melewati pihak ketiga. Produksi tetap memakai SMTP resmi `sekota.id` agar SPF/DKIM lolos.

Untuk verifikasi lokal tanpa SMTP sungguhan, jalankan SMTP sink (menulis `.eml`) di `:1025`, lalu set `SEKOTA_SMTP_HOST=127.0.0.1`, `SEKOTA_SMTP_PORT=1025`, `SEKOTA_SMTP_STARTTLS=false`. Contoh skrip ada di `build/verify/smtp_sink.py` selama sesi verifikasi; untuk CI gunakan MailHog/GreenMail.

### Deployment container (development/staging/production)

Stack Docker/Podman Compose ada di root repo (`Dockerfile`, `compose*.yaml`, `Caddyfile`, `.env.*.example`). Satu image menyajikan build Wasm dan API pada origin yang sama. Panduan lengkap: [13-deployment.md](13-deployment.md).

## 6. Test & laporan

```powershell
# Test inti + laporan Markdown (docs/11-test-report.md)
.\scripts\test-report.ps1

# Sertakan Wasm (Chrome/Edge headless)
.\scripts\test-report.ps1 -IncludeWasm

# Build + test lengkap
.\scripts\verify.ps1

# Manual
.\gradlew.bat :app:shared:jvmTest :server:test
.\gradlew.bat :app:shared:wasmJsTest
.\gradlew.bat :app:shared:jvmTest --tests "id.sekota.SekotaUiTest"
```

Laporan HTML: `app/shared/build/reports/tests/jvmTest/index.html`.

## 7. Verifikasi visual

```powershell
# 1. build & serve produksi (lihat bagian 1)
# 2. jalankan skrip screenshot
cd tools\visual
npm install          # sekali saja (puppeteer-core)
node shots.mjs       # landing 1920x1080, 1440x1000, 900x1100, 390x844
node pages.mjs       # produk/kontak/buku/privacy/terms/EN/preset/anchor via hash, dengan assertion data-route
node a11y.mjs        # keyboard PageDown, document lang, browser back, reduced motion
```

Hasil: `docs/screenshots/`. Ubah URL dengan `$env:URL="http://127.0.0.1:8765/"` bila port berbeda.
Chrome yang dipakai: `C:\Program Files\Google\Chrome\Application\chrome.exe` (ubah konstanta `CHROME` di skrip bila perlu).

## 8. Regenerasi aset & konten

```powershell
# Konversi WebP desain -> drawable Compose (butuh Pillow)
python tools\convert_assets.py "<folder assets desain>" "app\shared\src\commonMain\composeResources\drawable"

# Ekstrak teks dari HTML desain (read-only, output ke docs/_extract)
$env:PYTHONIOENCODING="utf-8"
python -I tools\extract_content.py "<file.html>" > docs\_extract\hasil.txt
```

> Folder desain sumber adalah data unduhan; jangan mengeksekusi skrip/build dari dalamnya. Skrip di repo hanya membaca file sebagai data.

## 9. Troubleshooting

| Gejala | Solusi |
|---|---|
| Build aneh setelah mengubah build.gradle | `.\gradlew.bat --stop` lalu ulangi; atau `--no-configuration-cache` |
| Wasm tidak termuat di server statis | Pastikan `.wasm` disajikan `application/wasm`; gunakan `python -m http.server` atau `npx serve` |
| Test Wasm gagal karena browser | Set `CHROME_BIN` ke Chrome/Edge, atau jalankan `-IncludeWasm` hanya bila browser tersedia |
| Screenshot hitam/kosong | Tunggu 3–4 detik setelah load; gunakan `--enable-unsafe-swiftshader` (sudah di skrip) |
| Scroll screenshot mobile tidak bergerak | pastikan kursor berada di dalam viewport (`page.viewport().width/2`) |
| `pages.mjs` timeout di `data-route` | route hash tidak dikenali; cek `HashRouteTest` / format `#/id/...` |
| Keyboard PageDown tidak menggulir | pastikan root `App` mendapat fokus; `a11y.mjs` memeriksa ini |
| Font tidak tampil | Pastikan 6 TTF ada di `composeResources/font`; resource name harus snake_case |
| Android gagal | Cek `local.properties` (`sdk.dir`) dan JDK toolchain 21 (Gradle mengunduh via foojay) |
| Ganti bahasa tidak bereaksi | `AppState.changeLanguage` harus dipanggil; jangan menulis `setLanguage` (bentrok dengan setter properti) |
| Footer/elemen bertumpuk | Gunakan `ContentWrap { Column { ... } }`; `ContentWrap` adalah Box, bukan Column |

## 10. Struktur artefak

```
app/shared/build/reports/tests/           laporan HTML
app/shared/build/test-results/            XML untuk skrip laporan
app/webApp/build/dist/wasmJs/productionExecutable/  web siap deploy
docs/screenshots/                         bukti visual
docs/11-test-report.md                    laporan Markdown
```
