param(
    [switch]$IncludeWasm
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Push-Location $root

try {
    $tasks = @(":app:shared:jvmTest", ":server:test")
    if ($IncludeWasm) { $tasks += ":app:shared:wasmJsTest" }

    Write-Host "Running: gradlew $($tasks -join ' ')" -ForegroundColor Cyan
    & "$root\gradlew.bat" @tasks --console=plain
    $exitCode = $LASTEXITCODE

    $suites = @()
    $targets = @(
        @{ Name = "Shared JVM"; Dir = "app/shared/build/test-results/jvmTest" },
        @{ Name = "Shared Wasm"; Dir = "app/shared/build/test-results/wasmJsBrowserTest" },
        @{ Name = "Server"; Dir = "server/build/test-results/test" }
    )

    foreach ($target in $targets) {
        $dir = Join-Path $root $target.Dir
        if (-not (Test-Path $dir)) { continue }
        Get-ChildItem -Path $dir -Filter "TEST-*.xml" | ForEach-Object {
            try {
                [xml]$doc = Get-Content -Path $_.FullName -Encoding UTF8
                $suite = $doc.testsuite
                $suites += [pscustomobject]@{
                    Target   = $target.Name
                    Name     = ($suite.name -replace "\[.*\]$", "")
                    Tests    = [int]$suite.tests
                    Failures = [int]$suite.failures
                    Errors   = [int]$suite.errors
                    Skipped  = [int]$suite.skipped
                    Time     = $suite.time
                }
            } catch {
                Write-Warning "Tidak bisa membaca $($_.FileName): $($_.Exception.Message)"
            }
        }
    }

    $total = ($suites | Measure-Object -Property Tests -Sum).Sum
    $failed = ($suites | Measure-Object -Property Failures -Sum).Sum
    $errors = ($suites | Measure-Object -Property Errors -Sum).Sum
    $skipped = ($suites | Measure-Object -Property Skipped -Sum).Sum
    if ($null -eq $total) { $total = 0 }
    if ($null -eq $failed) { $failed = 0 }
    if ($null -eq $errors) { $errors = 0 }
    if ($null -eq $skipped) { $skipped = 0 }

    $status = if ($exitCode -eq 0 -and $failed -eq 0 -and $errors -eq 0) { "LULUS" } else { "GAGAL" }
    $icon = if ($status -eq "LULUS") { "OK" } else { "X" }
    $now = Get-Date -Format "yyyy-MM-dd HH:mm"

    $rows = foreach ($s in ($suites | Sort-Object Target, Name)) {
        "| $($s.Target) | ``$($s.Name)`` | $($s.Tests) | $($s.Failures) | $($s.Errors) | $($s.Skipped) | $($s.Time) s |"
    }

    $byTarget = $suites | Group-Object Target | ForEach-Object {
        $t = ($_.Group | Measure-Object -Property Tests -Sum).Sum
        $f = ($_.Group | Measure-Object -Property Failures -Sum).Sum
        $e = ($_.Group | Measure-Object -Property Errors -Sum).Sum
        $sk = ($_.Group | Measure-Object -Property Skipped -Sum).Sum
        "| $($_.Name) | $($_.Count) | $t | $f | $e | $sk |"
    }

    $bundle = ""
    $dist = Join-Path $root "app/webApp/build/dist/wasmJs/productionExecutable"
    if (Test-Path $dist) {
        $wasm = Get-ChildItem -Path $dist -Filter "*.wasm" | Sort-Object Length -Descending | Select-Object -First 2
        $js = Get-ChildItem -Path $dist -Filter "webApp.js" | Select-Object -First 1
        $parts = @()
        foreach ($f in $wasm) { $parts += ("{0} {1:N2} MiB" -f $f.Name, ($f.Length / 1MB)) }
        if ($js) { $parts += ("webApp.js {0:N0} KiB" -f ($js.Length / 1KB)) }
        $bundle = "Ukuran distribusi web: " + ($parts -join ", ") + "."
    }

    $md = @"
# 11 - Laporan Test

> Dibuat otomatis oleh ``scripts/test-report.ps1`` pada $now.

- **Status:** $icon $status
- **Perintah:** ``gradlew $($tasks -join ' ')``
- **Total:** $total test, $failed gagal, $errors error, $skipped dilewati

## Ringkasan per target

| Target | Suite | Test | Gagal | Error | Dilewati |
|---|---:|---:|---:|---:|---:|
$($byTarget -join "`n")

## Detail per suite

| Target | Suite | Test | Gagal | Error | Dilewati | Waktu |
|---|---|---:|---:|---:|---:|---:|
$($rows -join "`n")

## Artefak

- HTML JVM: ``app/shared/build/reports/tests/jvmTest/index.html``
- HTML Wasm: ``app/shared/build/reports/tests/wasmJsBrowserTest/index.html``
- HTML server: ``server/build/reports/tests/test/index.html``
- XML: ``app/shared/build/test-results``, ``server/build/test-results``
- Screenshot: ``docs/screenshots/``

$bundle

## Catatan

- Jalankan dengan ``-IncludeWasm`` untuk menyertakan pengujian browser Wasm.
- Jika suite tidak muncul, jalankan Gradle tanpa cache: ``gradlew --rerun-tasks <tasks>``.
- Laporan ini hanya mencakup test otomatis; verifikasi visual dilakukan oleh ``tools/visual/*.mjs``.
"@

    $outPath = Join-Path $root "docs/11-test-report.md"
    [System.IO.File]::WriteAllText($outPath, $md, (New-Object System.Text.UTF8Encoding($false)))
    Write-Host "Laporan ditulis ke $outPath" -ForegroundColor Green
    Write-Host "Status: $status ($total test, $failed gagal, $errors error)" -ForegroundColor $(if ($status -eq "LULUS") { "Green" } else { "Red" })

    exit $exitCode
}
finally {
    Pop-Location
}
