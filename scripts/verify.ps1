param(
    [switch]$SkipWebBuild,
    [switch]$IncludeWasm
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Push-Location $root

try {
    Write-Host "== 1/4 Test + laporan ==" -ForegroundColor Cyan
    & "$root\scripts\test-report.ps1" -IncludeWasm:$IncludeWasm
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Test gagal. Verifikasi dihentikan." -ForegroundColor Red
        exit $LASTEXITCODE
    }

    Write-Host "== 2/4 Build Android ==" -ForegroundColor Cyan
    & "$root\gradlew.bat" :app:androidApp:assembleDebug --console=plain
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

    Write-Host "== 3/4 Compile Desktop ==" -ForegroundColor Cyan
    & "$root\gradlew.bat" :app:desktopApp:compileKotlin --console=plain
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

    if (-not $SkipWebBuild) {
        Write-Host "== 4/4 Build web produksi ==" -ForegroundColor Cyan
        & "$root\gradlew.bat" :app:webApp:wasmJsBrowserDistribution --console=plain
        if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    } else {
        Write-Host "== 4/4 Build web dilewati ==" -ForegroundColor Yellow
    }

    Write-Host "Verifikasi selesai. Lihat docs/11-test-report.md dan docs/screenshots/." -ForegroundColor Green
    exit 0
}
finally {
    Pop-Location
}
