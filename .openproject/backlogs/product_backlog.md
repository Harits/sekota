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
