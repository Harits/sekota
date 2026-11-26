# 06 — Flow Diagram

## 1. Navigasi aplikasi

```mermaid
flowchart TD
    A[App start] --> B{Route?}
    B -->|Home| C[LandingScreen]
    B -->|Product slug| D[ProductDetailScreen]
    B -->|Books anchor?| E[BooksScreen]
    B -->|Contact preset?| F[ContactScreen]
    B -->|Privacy / Terms| L[LegalScreen]
    C -->|kartu layanan / suite / platform| D
    C -->|kartu buku| E
    C -->|CTA / nav Hubungi Kami| F
    D -->|demo / CTA| F
    D -->|platform lain| D
    D -->|breadcrumb Platform| C
    E -->|CTA| F
    E -->|indeks / trilogi| E
    F -->|logo / nav| C
    F -->|selesai / tulis ulang| F
    F -->|catatan privasi| L
    L -->|Baca juga / tautan silang| L
    L -->|email / Hubungi Kami| F
    L -->|logo / nav| C
```

## 2. Language switch

```mermaid
sequenceDiagram
    participant U as User
    participant H as SiteHeader
    participant S as AppState
    participant UI as Semua layar
    U->>H: klik EN
    H->>S: changeLanguage(EN)
    S-->>UI: recomposition (language = EN)
    UI->>UI: baca LText[EN] untuk semua copy
    Note over UI: Pesan formulir yang sudah diketik tidak diubah
```

## 3. FAQ accordion

```mermaid
sequenceDiagram
    participant U as User
    participant R as FaqRow
    participant VM as LandingViewModel
    U->>R: klik pertanyaan
    R->>VM: toggleFaq(id)
    alt id == openFaqId
        VM-->>R: openFaqId = -1 (tutup)
    else id lain
        VM-->>R: openFaqId = id (satu terbuka)
    end
    R->>R: AnimatedVisibility + rotasi ikon
```

## 4. Formulir kontak

```mermaid
flowchart TD
    A[Isi form] --> B[Klik Kirim Pesan]
    B --> C{ContactValidator}
    C -->|invalid| D[Tampilkan error per field]
    D --> A
    C -->|valid| E[SubmitContactMessage]
    E --> F[Bangun subject/body]
    F --> G[SendContactMessage -> POST /api/contact]
    G -->|2xx| H[sent = true, SuccessPanel]
    G -->|gagal/timeout/503| I[failed = true + pesan error]
    I --> A
    H --> J[Kirim pesan lain -> reset]
```

Label subjek: `[Website] {topik} – {nama}`. Body:

```
Nama: ...
Email: ...
Topik: ...

{pesan}
```

Server memvalidasi ulang nama/email, lalu `SmtpMailer` mengirim ke `SEKOTA_MAIL_TO` (default `info@sekota.id`) dengan `Reply-To` pengunjung. UI tidak pernah menampilkan sukses untuk kegagalan.

## 5. Preset deep-link

```mermaid
flowchart LR
    A[CTA demo produk] -->|Route.Contact demo-slug| B[ContactScreen]
    C[CTA buku] -->|Route.Contact buku-anchor| B
    D[CTA mitra] -->|Route.Contact mitra-awal| B
    B --> E[ContactCatalog.prefill]
    E --> F{pesan kosong?}
    F -->|ya| G[isi topik + pesan]
    F -->|tidak| H[pertahankan pesan pengguna]
```

## 6. Galeri produk

```mermaid
stateDiagram-v2
    [*] --> Tab0
    Tab0 --> Tab1: klik tab 2
    Tab1 --> Tab2: klik tab 3 (Sociara)
    Tab2 --> Tab0: klik tab 1
    Tab1 --> Tab0: klik tab 1
```

State disimpan di `ProductViewModel.selectedScreen`; indeks di luar rentang diabaikan.

## 7. Scroll & chrome

```mermaid
sequenceDiagram
    participant L as LazyListState
    participant LS as LandingScreen
    participant AS as AppState
    participant H as SiteHeader
    L-->>LS: (firstVisibleItemIndex, offset)
    LS->>AS: chromeSolid = index>0 atau offset>12
    LS->>AS: activeAnchor = section key
    AS-->>H: recompose (solid, underline)
```

## 8. Alur sprint

```mermaid
flowchart LR
    S0[Sprint 0<br/>Discovery & docs] --> S1[Sprint 1<br/>Fondasi & design system]
    S1 --> S2[Sprint 2<br/>Landing page]
    S2 --> S3[Sprint 3<br/>Produk, buku, kontak]
    S3 --> S4[Sprint 4<br/>Test & hardening]
    S4 --> S5[Sprint 5<br/>URL routing, backend form, OpenProject]
```

## 9. Verifikasi lokal

```mermaid
flowchart TD
    A[Ubah kode] --> B[scripts/test-report.ps1]
    B --> C{Test JVM/Wasm/server lulus?}
    C -->|tidak| D[Perbaiki]
    D --> B
    C -->|ya| E[Build web distribution]
    E --> F[Serve + tools/visual/shots.mjs]
    F --> G[Review screenshot 1440 & 390]
    G --> H[Update docs/test-report]
```
