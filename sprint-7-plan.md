# Sprint 7 Plan (DRAFT): Revamp Web Sekota ke Desain Company Profile Baru

> Status: Sprint 7 dibuat di OpenProject, version 853 (project Web Sekota): 4 user story (#22652–#22655) dan 16 task (#22656–#22671).
> Sumber desain: `Downloads/sekota-landing-page` (static HTML final, ID + EN). Desain Sprint 6 ditolak.

## 1. Kesimpulan: REVAMP (bukan refactor, bukan rewrite total)

Patokannya: kalau layer domain berubah, hampir semua layer di atasnya ikut berubah. **Domain ikut berubah**, jadi
seluruh situs publik perlu dibangun ulang. Fondasi arsitekturnya tetap bisa dipakai ulang:
Clean Architecture, repository, MVI navigation, `AppContainer`, `LocalizationManager`, `SyncService`, dan kerangka CMS.

| Layer | Status | Keterangan |
|---|---|---|
| Domain | **Berubah** | Ada entitas baru, field baru, dan field lama yang tidak dipakai lagi (lihat 2) |
| Data | Berubah mengikuti domain | Skema repository, sync payload, dan seed data ikut menyesuaikan |
| Presentation (publik) | **Dibangun ulang** | Struktur halaman, navigasi, tipografi, dan semua section berbeda |
| Presentation (CMS) | Diperluas | Editor untuk entitas baru, sedangkan editor lama disesuaikan |
| Infrastruktur (auth, sync, i18n, DI, nav pattern) | Tetap | Dipakai ulang |

## 2. Perbedaan desain lama dan baru

### Struktur informasi
| Kode sekarang (Sprint 6) | Desain baru |
|---|---|
| Hero "Trusted Intelligence" | Hero "Data jadi keputusan. Keputusan jadi dampak." |
| Problem grid (3 kartu) | **Tentang**: "Mitra analisis yang tumbuh bersama Anda" |
| — | **Layanan** (baru): 4 layanan, yaitu Analisis Strategis & Risiko, Kesiapan & Kompetensi SDM, Dampak Sosial & CSR, Kinerja ESG |
| Strategic Value Loop, 5 langkah (Intent…Learning) | **Cara Kerja**, 4 tahap: Diagnosis, Perancangan, Penerapan, Keberlanjutan |
| Intelligence Suite: Veridia, Ascendio, Sociara, Ecoflow | **Platform** (dark): Clarivio, Ascendio, Sociara, Evidara |
| E-Book promo + Catalog + Book Details + Reader | **Wawasan**: 6 buku, termasuk seri "Trilogi CSR, ESG, dan Kepemimpinan Keberlanjutan", plus halaman `buku` |
| Why Sekota (dark) | **Mitra**: CTA "Mari tumbuh dan berkembang bersama" |
| — | **FAQ** (baru, accordion) |
| Contact form di landing | Halaman **Kontak** terpisah: nama, email, **topik**, pesan |
| — | **Halaman detail produk** ×4: masalah, kapabilitas, galeri screenshot, 4 langkah pemakaian, pengguna, layanan terkait |
| Merchandise, Login/Signup, Auth gate, Reader | **Tidak ada di desain baru** (perlu keputusan, lihat 5) |

### Design system
- Font: **Plus Jakarta Sans + IBM Plex Mono**, menggantikan Montserrat + DM Sans.
- Logo dan aset tersedia dalam varian light/dark (WebP).
- Animasi reveal dimatikan saat "reduce motion".

### Dampak ke domain
| Model | Perubahan |
|---|---|
| `Service` (baru) | Judul, deskripsi, ikon, produk terkait (ID/EN) |
| `Faq` (baru) | Pertanyaan dan jawaban, urutan (ID/EN) |
| `AboutConfig`, `PartnerCtaConfig` (baru) | Konten section Tentang dan Mitra |
| `AdminProduct` | Tambah `slug`, headline, daftar masalah, kapabilitas (+ikon), screenshot, 4 langkah pemakaian, target pengguna, layanan terkait, logo light/dark. Roster data berubah: tambah Clarivio dan Evidara, hapus Veridia dan Ecoflow |
| `AdminBook` | Tambah `slug` (anchor), tagline, blurb, "Untuk siapa", seri. Field rating, readers, pdfUrl, readingTime, dan pages tidak dipakai di desain baru |
| `ClientInquiry` | Tambah `topic` |
| `ValueLoopConfig` | 5 langkah menjadi 4 tahap, ditambah deskripsi per tahap |
| `NavigationItem` / `AppDestination` | Rute baru: anchor landing, `produk/{slug}`, `buku#slug`, `kontak`. Tombol ID/EN tetap di halaman yang sama |

## 3. Usulan backlog (dibagi 2 sprint, karena terlalu besar untuk 1 sprint)

### Sprint 7: Fondasi + Landing Page
| Kode | User story |
|---|---|
| CORE-07-01 | Revamp domain model dan data layer (Service, FAQ, About, Partner, field produk/buku/inquiry, 4 tahap), lalu migrasi seed data ke konten baru |
| WEB-07-01 | Design system baru: tipografi, token warna, aset light/dark, reveal animation + reduce motion, multi-state previews |
| WEB-07-02 | Rebuild landing page: Hero, Tentang, Layanan, Cara Kerja, Platform, Wawasan, Mitra, FAQ, Footer (ID/EN) |
| WEB-07-03 | Navigasi dan routing baru: nav anchor, rute halaman, switch bahasa di halaman yang sama |

### Sprint 8: Halaman detail + CMS
| Kode | User story |
|---|---|
| WEB-08-01 | Template halaman detail produk (Clarivio, Ascendio, Sociara, Evidara) |
| WEB-08-02 | Halaman Wawasan/Buku dengan anchor per buku |
| WEB-08-03 | Halaman Kontak: field topik, kirim ke inquiry pipeline yang sudah ada |
| CMS-08-01 | Editor CMS untuk Service, FAQ, About/Partner, detail produk, dan field buku baru |
| WEB-08-04 | Pensiunkan Catalog dan Book Details lama setelah halaman Wawasan/Buku siap |

## 4. Yang dibatalkan dari draft sebelumnya
Draft lama (animasi hero, polish e-book cover, dll.) memoles desain Sprint 6 yang sudah ditolak, jadi dibatalkan.

## 5. Keputusan
- **Diputuskan (24 Nov 2026):** Merchandise (termasuk tab CMS-nya), Login/Signup, AuthGate, Profile, dan Book Reader **dihapus**. Pekerjaannya ada di task #22670. Login admin CMS tetap dipertahankan. Kode lama masih tersimpan di branch `sprint-6` dan di `master` sebelum revamp.
- **Diputuskan:** pembagian 2 sprint disetujui; child task Sprint 7 sudah dibuat.
- **Masih terbuka:** copy/terminologi (jawaban FAQ, istilah "Analisis Strategis & Risiko") menunggu persetujuan direksi. Untuk sementara dikerjakan dengan copy dari desain.
- **Masih terbuka:** halaman Kontak memakai `mailto:` di desain. Rekomendasinya tetap memakai inquiry pipeline + CMS yang sudah ada (Sprint 8).
