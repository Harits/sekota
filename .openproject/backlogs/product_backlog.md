# Sekota Web - Product Backlog

This file contains the high-level roadmap, user stories, and architectural boundaries for the Sekota Web platform.

---

## 🏛️ Scope & Architectural Governance: Industrial CMS Master vs Web Metadata Enrichment

> [!IMPORTANT]
> **Separation of Concerns & Dual-Track Inventory Strategy**:
> - **Master Inventory Source (`bookinteractiontool` / Sekota Industrial CMS)**:
>   - Acts as the single source of truth for base book records: canonical `id`, `title`, `author`, `isbn`, and original cover asset.
>   - Master naskah authoring, legal ISBN issuance, and physical inventory lifecycles are managed by the **Operation Department**.
> - **Web Presentation & Metadata Enrichment (Sekota CMS & Web)**:
>   - In **Sekota CMS** (Desktop/Backoffice), operators enrich imported books with web-specific attributes:
>     - **Category** (`SMART CITY`, `ESG`, `INTELLIGENCE`, `GOVERNANCE`)
>     - **Web Synopsis / Description**
>     - **Reader Star Rating & Reader Counts** (e.g. `4.8` stars, `1,240` readers)
>     - **PDF Download / Reading URL** (`pdfUrl`)
>     - **Reading Time & Page Count** (e.g. `3H 45M`, `240 Hal`)
>     - **Custom Cover Upload** via base64 file picker.
>   - In **Web Sekota**, clicking any book in [`CatalogScreen`](file:///Users/macbook/StudioProjects/Sekota/composeApp/src/commonMain/kotlin/com/sekota/screens/CatalogScreen.kt) dynamically routes to [`BookDetailsScreen`](file:///Users/macbook/StudioProjects/Sekota/composeApp/src/commonMain/kotlin/com/sekota/screens/BookDetailsScreen.kt) rendering exact dynamic metadata, reader stars, and direct PDF access.

---

## 🚀 Phase 1: Foundation (Sprint 1 - Closed)
- [x] **US-1: Responsive Navigation Bar** (OpenProject ID: 21679 / WP-001)
  - *Description*: Users should be able to navigate the platform on any device using Sekota design tokens.
- [x] **US-2: Setup Ktor Web Client** (OpenProject ID: 21686 / WP-002)
  - *Description*: Initialize multiplatform Ktor client for JS/Wasm target in `shared` module.
- [x] **US-3: Landing Page Hero & Sections Implementation** (OpenProject ID: 21681, 21687, 21689 / WP-003, WP-005)
  - *Description*: Full landing page implementation (Hero, Feature Grid, Intelligence Suite, Value Loop, E-Book Promo, Contact Form, Footer).
- [x] **US-4: Sidebar Filter System & E-Book Catalog** (OpenProject ID: 21682, 21690, 21691 / WP-004, WP-006, WP-007)
  - *Description*: Browsable grid of eBooks with filtering, category sorting, and detailed view.
- [x] **US-5: Merchandise Catalog Page & Global Footer** (OpenProject ID: 21692 / WP-008, US-10)
  - *Description*: Browsable grid of official Sekota merchandise with filtering, product cards, and comprehensive footer.

---

## ⚡ Phase 2: Interaction & Sync (Sprint 2 - Closed)
- [x] **US-6: JWT Authentication on Server & Shared Client** (OpenProject ID: 21680, 21693, 21694, 21695 / WP-009, WP-010, WP-011)
  - *Description*: Setup JWT authentication plugin, registration/login endpoints, and `AuthService` in `shared` module.
- [x] **US-7: Responsive Auth UI (Login & Signup)** (OpenProject ID: 21696, 21697 / WP-012, WP-013)
  - *Description*: Responsive login and signup screens conforming to Sekota typography and color tokens.
- [x] **US-8: Real-time Data Sync & WebSocket Infrastructure** (OpenProject ID: 21684, 21698, 21699 / WP-014, WP-015)
  - *Description*: Configure WebSocket broadcast server and client listener to sync data updates across sessions.

---

## 🔒 Phase 3: Persistence & Profile (Sprint 3 - Closed)
- [x] **US-9: Multiplatform Local Persistence (SQLDelight & LocalStore)** (OpenProject ID: 21708 / WP-016)
  - *Description*: Persist auth tokens, user state, and session offline via SQLDelight and platform storage interop.
- [x] **US-10: Database Backend & Exposed ORM** (OpenProject ID: 21709 / WP-017)
  - *Description*: Integrate PostgreSQL database and Exposed ORM DSL for secure, parameterized data handling.
- [x] **US-11: User Profile API & Responsive Management Screen** (OpenProject ID: 21710, 21711 / WP-018, WP-019)
  - *Description*: Full profile retrieval and editing capabilities with role indicators (`ADMIN`, `BOD`, `BD`, `READER`).
- [x] **US-12: Persistent Auto-Login & Design Refinements** (OpenProject ID: 21712, 21716 / WP-020, WP-021)
  - *Description*: Automatic login session restoration on app restart, and Figma `0:1` design alignment.

---

## 🛠️ Phase 1 Refinement: CMS Tri-Platform & Auth-Gating (Sprint 4 - Closed)
- [x] **WP-023: UC-GATE-01 - Inline Auth Modal Dialog in Web Sekota** (OpenProject ID: 22399)
  - *Description*: AuthGateDialog on BookDetailsScreen and MerchandiseScreen to enforce authentication before full reading or orders.
- [x] **WP-024: UC-CMS-01 - Shared Domain & Repository for Admin Books, Tools & Inquiries** (OpenProject ID: 22400)
  - *Description*: Pure Kotlin domain UseCases, ClientInquiry, LiveMetrics, dynamic `AdminBook` metadata enrichment (`pdfUrl`, `rating`, `category`), and real-time Cross-Platform SyncService.
- [x] **WP-025: UC-CMS-02 - Kotlin Admin CLI (`sekota-cli`)** (OpenProject ID: 22401)
  - *Description*: Command line interface for catalog inspection, merchandise, and system diagnostics.
- [x] **WP-026: UC-CMS-03 - Compose Desktop JVM & Android Admin Window** (OpenProject ID: 22402)
  - *Description*: Visual CMS workbench with **Web Metadata Enrichment Dialog** (`✏️ Kelola Tampilan Web`), master catalog sync with `bookinteractiontool`, image upload file picker, Live Metrics editor, and inquiry reviewer.
- [x] **WP-027: UC-CMS-04 - Products, Merchandise & Live Sync Integration** (OpenProject ID: 22403)
  - *Description*: Intelligence Suite CRUD, Merchandise store with image picker, dynamic Web Catalog to Book Details navigation, and real-time cross-platform sync.
- [x] **WP-028: [WEB-04-01] - Web Sekota UX/UI Responsive, Navigation & Consultation Pipeline** (OpenProject ID: 22413)
  - *Description*: Implementasi eksplorasi interaktif Hero, navigasi mulus antar section & konsultasi, pipeline ClientInquiry CMS Workbench, dan multi-state Compose Previews komprehensif.
  - *Child Tasks*:
    - `#22435` [WEB-04-01-01] Refactor Hero Exploration CTA & M3 Action Buttons
    - `#22436` [WEB-04-01-02] Cross-Screen Consultation Navigation & Scroll Targeting
    - `#22437` [WEB-04-01-03] Full-Stack Client Inquiries Pipeline & CMS Inquiries Suite
    - `#22438` [WEB-04-01-04] Author Comprehensive Multi-State Desktop & Mobile Compose Previews

---

## 🔄 Phase 2 Refinement: Dynamic Content & Ecosystem Orchestration (Sprint 5 - Closed)
- [x] **WP-029: [CMS-05-01] - Strategic Value Loop Dynamic Content Management & Cross-Sync** (OpenProject ID: 22439)
  - *Description*: Sebagai Administrator Sekota CMS, Saya ingin Mengelola dan memperbarui teks, judul, subjudul, dan tahapan Strategic Value Loop (01 Intent, 02 Execution, 03 Value, 04 Measurement, 05 Learning) melalui tab khusus di CMS Workbench, Sehingga Pesan metodologi solusi Sekota di portal publik dapat diperbarui secara dinamis dan real-time tanpa perlu deployment ulang kode.
  - *Child Tasks*:
    - `#22440` [CMS-05-01-01] Implement Shared Domain Model, UseCases & Multiplatform Storage for Value Loop Steps
    - `#22441` [CMS-05-01-02] Build Desktop JVM & Android CMS Workbench Tab & Card Editor for Value Loop
    - `#22442` [CMS-05-01-03] Integrate CmsSyncServer & SyncService Real-Time WebSocket/Polling for Value Loop
    - `#22443` [CMS-05-01-04] Bind ValueLoopSection to Dynamic Repository with Mandatory Multi-State Desktop Previews
- [x] **WP-030: [CMS-05-02] - Dynamic Image, Custom Icon & Accent Customization for Intelligence Suite** (OpenProject ID: 22462)
  - *Description*: Sebagai Administrator Sekota CMS, Saya ingin Mengunggah logo/ikon kustom dan menentukan kode warna aksen heksadesimal untuk masing-masing produk Intelligence Suite melalui dialog edit modul, Sehingga Modul produk baru maupun yang diperbarui dapat menampilkan identitas visual ikon kustomnya secara dinamis dan real-time di portal Web Sekota serta Product Details Screen (atau tidak menampilkan ikon bila tidak disediakan di CMS).
  - *Child Tasks*:
    - `#22463` [CMS-05-02-01] Shared Domain Model & Storage Enrichment for Product Icons & Accents
    - `#22464` [CMS-05-02-02] CMS Workbench Image Picker & In-Memory Preview Dialog
    - `#22465` [CMS-05-02-03] Dynamic Web Bitmap Rendering for IntelligenceSuite & ProductDetailsScreen (Hide Icon When Not Provided)
    - `#22466` [CMS-05-02-04] Author Multi-State Compose Previews and Verify Desktop & Wasm Compilations
- [x] **WP-031: [CMS-05-03] - Dynamic Catalog Category Management (Add & Remove) & Unified Book Cover Background Styling** (OpenProject ID: 22467)
  - *Description*: Sebagai Administrator Sekota CMS, Saya ingin Menambah kategori baru atau menghapus kategori buku langsung dari modal Kelola Tampilan Web dan memastikan warna latar belakang sampul buku konsisten di seluruh aplikasi (Perpustakaan Digital, E-Book Catalog, dan Book Details), Sehingga Taksonomi kategori buku dapat berkembang secara fleksibel dan tampilan sampul buku tampil harmonis serta profesional.
  - *Child Tasks*:
    - `#22468` [CMS-05-03-01] Shared Domain Model, Dynamic Categories & Synchronized Cover Color Repository
    - `#22469` [CMS-05-03-02] CMS WebMetadataDialog: Add & Remove Category Chips UI and Sync Trigger
    - `#22470` [CMS-05-03-03] Unified Book Cover Background & Dynamic Category Filtering Across Web Sekota
    - `#22471` [CMS-05-03-04] Author Multi-State Compose Previews and Verify Desktop & Wasm Compilations
- [x] **WP-032: [WEB-05-01] - Dynamic Icon Asset Binding & Contextual Rendering Fix Across Platform** (OpenProject ID: 22472)
  - *Description*: Sebagai Pengguna Web & Operator Sekota CMS, Saya ingin Seluruh ikon di seluruh modul dan tampilan publik (khususnya What's Inside di Book Details dan icon selector di CMS) terikat langsung ke resource resmi (`Res.drawable.*`) atau vektor native yang relevan dengan konteksnya tanpa emoji mentah atau placeholder rusak, Sehingga Tampilan antarmuka profesional, tajam, dan bebas dari icon broken di Skiko Wasm maupun Desktop.
- [x] **WP-033: [CMS-05-04] - Dynamic Content Management for Why Sekota Section** (OpenProject ID: 22473)
  - *Description*: Sebagai Content Administrator / Marketing Team, Saya ingin Mengelola seluruh teks, tautan CTA, serta poin-poin diferensiasi pada seksi "Mengapa Sekota" secara dinamis melalui CMS Admin, Sehingga Tim bisnis dapat memperbarui value proposition dan materi komunikasi publik secara mandiri dan cepat tanpa ketergantungan deployment developer.
- [x] **WP-034: [CMS-05-05] - Dynamic Content & Lead Ingestion for Strategic Conversation Section** (OpenProject ID: 22474)
  - *Description*: Sebagai Admin / Tim Marketing, Saya ingin Mengelola konten teks dan placeholder pada section "Mulai Percakapan Strategis" secara dinamis melalui CMS/Backend, serta menerima data leads dari formulir kontak secara real-time, Sehingga Pesan komunikasi tetap relevan dengan campaign terbaru tanpa perlu perubahan kode (hardcoded).
- [ ] **WP-036: [CMS-05-06] - Content Mapping Documentation & Workbench In-App Guide** (OpenProject ID: 22476)
  - *Description*: Sebagai Content Editor / CMS Operator, Saya ingin melihat dokumentasi pemetaan (mapping) yang jelas antara setiap field yang diedit di CMS Workbench dengan komponen tampilan spesifik yang menerimanya di portal Web Sekota, Sehingga saya dapat memprediksi secara akurat bagaimana perubahan data akan tampil bagi pembaca/klien publik, serta mencegah kesalahan tata letak (layout break) dan ketidaksesuaian konten.
  - *Child Tasks*:
    - `#22477` [CMS-05-06-01] Content Entities & Web Components Audit
    - `#22478` [CMS-05-06-02] Create Content Mapping Artifact (content_mapping.md)
    - `#22479` [CMS-05-06-03] CMS Workbench UI Integration: Contextual Guide Dialog
- [ ] **WP-037: [CMS-05-07] - Multi-Environment Awareness Badge & Production Safety Guardrails in CMS** (OpenProject ID: 22480)
  - *Description*: Sebagai Administrator atau CMS Operator, Saya ingin CMS Workbench menampilkan indikator status lingkungan koneksi aktif secara eksplisit (Development, Staging, atau Production) dan memberikan konfirmasi pengamanan pada aksi tulis, Sehingga saya terhindar dari kesalahan fatal mengedit data live production saat berniat melakukan uji coba atau eksperimen di lingkungan staging/development.
  - *Child Tasks*:
    - `#22481` [CMS-05-07-01] Implement Shared Environment Enum & Config Resolver
    - `#22482` [CMS-05-07-02] Build EnvironmentBadge Component & TopAppBar Integration
    - `#22483` [CMS-05-07-03] Production Safety Interceptor & Confirmation Dialog
    - `#22484` [CMS-05-07-04] Author Multi-State Compose Previews & Verify Desktop Build
- [ ] **WP-038: [CMS-05-08] - Dynamic Main Navigation Bar (AppBar) Content Management** (OpenProject ID: 22486)
  - *Description*: Sebagai Content Manager / CMS Operator, Saya ingin mengelola item-item tautan pada navigasi utama (AppBar) melalui CMS Workbench, Sehingga saya dapat menambah, menghapus, menyusun ulang urutan (reorder), atau mengubah label tautan navigasi (seperti 'Solusi', 'Produk', 'E-Book', dll.) secara dinamis tanpa perlu deploy ulang kode aplikasi maupun update versi portal Web Sekota.
  - *Child Tasks*:
    - `#22487` [CMS-05-08-01] Define Navigation Item Domain Model & CMS Schema
    - `#22488` [CMS-05-08-02] Implement Network Service, Storage Cache & Fallback Navigation
    - `#22489` [CMS-05-08-03] Decouple AppBar to Dynamic List & Route State Handling
    - `#22490` [CMS-05-08-04] Author Unit Tests, Multi-Item Compose Previews & Styling Verification
- [ ] **WP-039: [CMS-05-09] - Shared CMS UI Components Standardization & Presentation Decoupling** (OpenProject ID: 22495)
  - *Description*: Sebagai Developer & Maintainer Sekota CMS, Saya ingin mengekstrak komponen UI repetitif (Form Input, Image Uploader, Action Header, Empty & Loading States) ke package `com.sekota.components.cms` dan menyiapkan kerangka arsitektur kluster CMS modular, Sehingga kompleksitas `AdminDashboardScreen.kt` berkurang drastis, kode komponen dapat digunakan ulang secara konsisten di seluruh cluster, dan mendukung multi-platform responsive preview (Desktop JVM & Android).
  - *Child Tasks*:
    - `#22496` [CMS-05-09-01] Extract & Author Reusable Atomic CMS Input & Form Components
    - `#22497` [CMS-05-09-02] Standardize Multiplatform Image Uploader & Media Preview Component
    - `#22498` [CMS-05-09-03] Implement CmsActionHeader, Confirmation Bar & Production Safety Wrappers
    - `#22499` [CMS-05-09-04] Author Multi-State Compose Previews for Reusable CMS Components
- [ ] **WP-040: [CMS-05-10] - Product Registry (Unified Catalog Workspace)** (OpenProject ID: 22500)
  - *Description*: Sebagai Administrator / Operator Konten Sekota, Saya ingin mengelola seluruh katalog produk (Buku, Intelligence Suite, dan Merchandise) dalam satu layar terpadu "Product Registry" dengan filter tipe entitas, Sehingga saya tidak perlu berpindah-pindah antar tab terpisah saat mengelola portofolio produk, sementara integritas logika domain masing-masing model (`AdminBook`, `AdminProduct`, `AdminMerch`) tetap terjaga rapi.
  - *Child Tasks*:
    - `#22501` [CMS-05-10-01] Design Product Registry Contract & Dedicated ViewModel
    - `#22502` [CMS-05-10-02] Build ProductRegistryScreen & Filterable Adaptive Grid Layout
    - `#22503` [CMS-05-10-03] Integrate Dynamic CRUD & Modals (WebMetadata, Suite Editor, Merch Dialog)
    - `#22504` [CMS-05-10-04] Verify Real-Time Sync Triggers & Author Multi-State Compose Previews
- [ ] **WP-041: [CMS-05-11] - Brand & Story Workspace (Marketing Narrative Content)** (OpenProject ID: 22505)
  - *Description*: Sebagai Content Marketing / Tim Narasi Sekota, Saya ingin mengelola seluruh materi komunikasi publik (Why Sekota, Strategic Value Loop 01-05, dan Intelligence Suite Descriptions) dalam satu workspace "Brand & Story", Sehingga narasi merek dan value proposition solusi Sekota dapat dikurasi secara kohesif dalam satu alur kerja tanpa fragmentasi menu.
  - *Child Tasks*:
    - `#22506` [CMS-05-11-01] Define BrandStoryContract & Modular ViewModel
    - `#22507` [CMS-05-11-02] Build BrandStoryScreen with Tabbed / Segmented Narrative Workspaces
    - `#22508` [CMS-05-11-03] Author Multi-State Desktop Previews & Sync Verification
- [ ] **WP-042: [CMS-05-12] - Site Configuration & Global Settings Panel** (OpenProject ID: 22509)
  - *Description*: Sebagai Administrator Sistem Sekota CMS, Saya ingin mengelola pengaturan global situs (Dynamic AppBar Navigation, Global Footer Links, dan Legal/Copyright Policy) di panel terpusat "Site Configuration", Sehingga pembaruan struktur navigasi dan tautan situs dapat dikelola dari satu panel kendali tanpa harus membuka tab terpisah.
  - *Child Tasks*:
    - `#22510` [CMS-05-12-01] Create SiteConfigContract & Consolidate Config ViewModels
    - `#22511` [CMS-05-12-02] Build SiteConfigScreen with Navigation & Footer Sub-Panels
    - `#22512` [CMS-05-12-03] Desktop Previews & Graceful Fallback Validation
- [ ] **WP-043: [CMS-05-13] - Insights & Feedback Integrated Dashboard** (OpenProject ID: 22513)
  - *Description*: Sebagai Administrator / Business Development Sekota, Saya ingin melihat metrik interaksi pembaca (Live Telemetry Metrics) berdampingan dengan daftar pertanyaan prospek (Client Inquiries) pada dashboard "Insights & Feedback", Sehingga tim bisnis dan editorial dapat memperoleh gambaran holistik antara tren keterbacaan konten publik dengan kebutuhan nyata dari calon klien.
  - *Child Tasks*:
    - `#22514` [CMS-05-13-01] Create InsightsFeedbackContract & Integrated ViewModel
    - `#22515` [CMS-05-13-02] Build Dual-Pane InsightsFeedbackScreen Layout
    - `#22516` [CMS-05-13-03] Author Multi-State Compose Previews & Verify Telemetry Read-Only Invariants
- [ ] **WP-044: [CMS-05-14] - Refactor & Slim Down AdminDashboardScreen.kt God File** (OpenProject ID: 22517)
  - *Description*: Sebagai Lead Architect Sekota, Saya ingin merefaktor `AdminDashboardScreen.kt` menjadi root container tipis yang hanya mengelola NavigationRail/Drawer dan routing ke 4 cluster modular, Sehingga ukuran file berkurang >80%, recomposition lebih deterministik, dan bebas dari technical debt "God File".
  - *Child Tasks*:
    - `#22518` [CMS-05-14-01] Update CMS NavigationRail & Routing Enums for Clustered Workspaces
    - `#22519` [CMS-05-14-02] Migrate Legacy Inlined Composables to Feature Screens & Remove Dead Code

- [ ] **WP-045: [UI-05-15] - Type-Safe Icon System, Resource Migration & Raw Emoji Deprecation** (OpenProject ID: 22522)
  - *Description*: Sebagai Pengguna & Pengembang Sekota Web, Saya ingin memigrasikan seluruh icon hardcoded, raw emoji, dan literal karakter teks ke sistem icon berbasis Compose Resource (Res.drawable) dan native vector mappers, Sehingga identitas visual tampil tajam, konsisten, dan profesional di semua platform (Desktop JVM, Android, dan Web Wasm) tanpa risiko glyph rusak atau tampilan emoji yang berbeda antar-sistem operasi.
  - *Child Tasks*:
    - `#22523` [UI-05-15-01] Provision Missing Action & UI Vector Drawables in composeResources
    - `#22524` [UI-05-15-02] Refactor CMS Modals, Action Buttons & Feedback Banners
    - `#22525` [UI-05-15-03] Migrate Glyphs & Fallback Emojis in ProductCard & Reader Screens

---









