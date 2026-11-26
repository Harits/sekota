# Dokumentasi Proyek — Sekota Landing Page (Compose Multiplatform)

Folder ini adalah sumber kebenaran untuk requirement, arsitektur, desain, pengujian, dan perencanaan sprint.

| Dokumen | Isi |
|---|---|
| [00-analisis-desain.md](00-analisis-desain.md) | Analisis situs statis sumber, inventaris section, token, dan keputusan porting |
| [01-requirements.md](01-requirements.md) | Functional & non-functional requirements, scope, acceptance criteria |
| [02-use-case-diagram.md](02-use-case-diagram.md) | Aktor, diagram use case, daftar use case |
| [03-use-case-specification.md](03-use-case-specification.md) | Spesifikasi detail setiap use case |
| [04-architecture.md](04-architecture.md) | Clean architecture, scream architecture, DI, MVVM, navigasi, ADR |
| [05-design-system.md](05-design-system.md) | Token warna, tipografi, spacing, komponen, motion, aksesibilitas |
| [06-flow-diagrams.md](06-flow-diagrams.md) | Diagram alur navigasi, FAQ, kontak, bahasa, galeri, sprint |
| [07-test-strategy.md](07-test-strategy.md) | Strategi pengujian berlapis, matriks, quality gate, cara menjalankan |
| [08-sprint-plan.md](08-sprint-plan.md) | Rencana & status sprint, backlog, definition of done |
| [09-traceability.md](09-traceability.md) | Keterlacakan requirement → use case → layar → test |
| [10-openproject-sync.md](10-openproject-sync.md) | Rencana sinkronisasi ke OpenProject (belum dijalankan) |
| [11-test-report.md](11-test-report.md) | Laporan hasil test terakhir (dibuat otomatis oleh `scripts/test-report.ps1`) |
| [12-runbook.md](12-runbook.md) | Cara build, run, test, dan verifikasi visual |
| [13-deployment.md](13-deployment.md) | Deployment development/staging/production dengan Docker/Podman Compose, TLS, SMTP, rollout |

## Ringkasan status

- Sprint 0–5b selesai: fondasi, design system, landing page, 4 halaman produk, halaman buku, halaman kontak, ID/EN, halaman legal, serta deployment Compose untuk development/staging/produksi.
- Test otomatis: **152 test, 0 gagal** — 77 JVM (termasuk 8 UI Compose), 69 Wasm, 6 server; verifikasi visual desktop/tablet/mobile dan pemeriksaan keyboard/`prefers-reduced-motion` tersimpan di [`screenshots/`](screenshots/).
- Sinkronisasi OpenProject belum dilakukan; lihat [10-openproject-sync.md](10-openproject-sync.md).
