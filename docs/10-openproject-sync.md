# 10 — Rencana Sinkronisasi OpenProject

Status: **belum dijalankan**. Dokumen ini menyiapkan pemetaan agar sinkronisasi dapat dilakukan setelah disetujui.

Skill yang tersedia di sesi ini: `sekotasinergiindonesia-pmo` (OpenProject PMO — baca project, wiki/SOP, anggota, dan aksi terkait). Skill tersebut belum dipanggil; tidak ada data yang dikirim ke OpenProject.

## 1. Informasi yang dibutuhkan sebelum sinkronisasi

| Item | Contoh / catatan |
|---|---|
| URL instance | mis. `https://pmo.blackeye.id/` (sesuai skill) |
| Project identifier / ID | harus dipilih atau dibuat |
| Struktur work package | Epic → Task → Sub-task |
| Status workflow | New → In progress → In review → Done |
| Anggota & assignee | dari daftar member project |
| Custom field (opsional) | `Requirement ID`, `Use Case`, `Sprint`, `Test Evidence` |
| Apakah boleh membuat project baru | konfirmasi eksplisit dari pemilik project |

## 2. Pemetaan yang diusulkan

| Artefak lokal | Tipe work package | Judul |
|---|---|---|
| Sprint 0–5 | Epic / Milestone | `Sprint N — <tujuan>` |
| Setiap FR (M/S) | Task | `FR-xx <ringkasan requirement>` |
| Setiap use case | Sub-task atau deskripsi task | `UC-xx <nama>` |
| Setiap bug/regresi | Bug | `<gejala> — <layar>` |
| Backlog Sprint 5 | Task | `SP5-xx <item>` |
| Dokumen | Wiki page / attachment | `Analisis Desain`, `Arsitektur`, `Design System`, `Test Strategy` |

**Hubungan:** Epic Sprint → Task FR → Sub-task UC. Task FR menautkan dokumen traceability dan test yang membuktikannya.

## 3. Konten task (template)

```
Judul: FR-14 Halaman Kontak menampilkan formulir lengkap
Deskripsi:
- Requirement: docs/01-requirements.md#FR-14
- Use case: docs/03-use-case-specification.md#uc-08
- Acceptance: semua field tampil; email dapat diklik; form dapat disubmit.
- Bukti test: SekotaUiTest.contactFormValidatesAndSubmits
- Bukti visual: docs/screenshots/contact-top.png
Sprint: 3
Status: Done
```

## 4. Urutan sinkronisasi yang diusulkan

1. Baca project & workflow yang ada (`sekotasinergiindonesia-pmo`).
2. Konfirmasi project tujuan dan izin menulis.
3. Buat Epic Sprint 0–4 (status Done) dan Sprint 5 (New).
4. Buat task FR-01..FR-22, hubungkan ke Epic sprint.
5. Lampirkan/tautkan dokumen wiki: analisis, requirement, arsitektur, design system, test strategy.
6. Buat backlog SP5-01..SP5-12 sebagai task Sprint 5.
7. Verifikasi jumlah & status; simpan ID work package di `docs/openproject-map.md` (dibuat saat sinkronisasi).

## 5. Yang tidak akan dilakukan tanpa persetujuan

- Membuat project baru.
- Mengubah workflow/status global.
- Menghapus atau menimpa work package yang sudah ada.
- Mengunggah data sensitif/rahasia (dokumen ini hanya berisi konten publik situs).
- Menyimpan kredensial di repo.

## 6. Perintah yang mungkin dipakai nanti

Setelah persetujuan, cukup minta:

> Sinkronkan backlog dan sprint plan proyek ini ke OpenProject menggunakan skill PMO.

Agen akan membaca skill, menampilkan project yang tersedia, dan meminta konfirmasi sebelum menulis.
