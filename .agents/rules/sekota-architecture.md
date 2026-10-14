---
description: Sekota architectural standards, Clean and Screaming Architecture, and Compose preview rules.
trigger: always_on
---

# Sekota Development & Architecture Standards

1. **Clean & Screaming Architecture**:
   - Place all domain logic in `shared/src/commonMain/kotlin/com/sekota/features/<feature>/domain/`.
   - Domain layer must remain pure Kotlin (entities, usecases, repository interfaces) with zero dependencies on Ktor or outer layers.
   - Implement data repositories and network clients in `shared/.../features/<feature>/data/`. Use mappers to prevent DTO leakage into domain.
   - Structure presentation code in `composeApp/src/commonMain/kotlin/com/sekota/`.

2. **Platform & Target Boundary Isolation**:
   - **Web Target (`wasmJsMain`, `webMain`)**: Exclusively reserved for public readers and corporate clients (Landing, Catalog, Book Details, Merchandise, Auth Gate Dialog). NEVER expose CMS/Admin navigation links on the public Web Navbar or public routes.
   - **CMS Targets (`desktopMain`, `androidMain`, `:cli`)**: Backoffice CMS management must run exclusively via:
     - Desktop JVM (`./gradlew :composeApp:run`)
     - Android Mobile/Tablet (`AdminActivity` with adaptive NavigationBar / NavigationRail)
     - Terminal CLI (`./gradlew :cli:run`)

3. **Compose Multiplatform Layout & Scroll Hierarchy**:
   - NEVER wrap `LazyColumn`, `LazyRow`, or `LazyVerticalGrid` inside a parent `Modifier.verticalScroll(...)` or `Modifier.horizontalScroll(...)`. This triggers fatal infinite measurement crashes.
   - In full-screen destinations or adaptive layouts, let lazy containers manage their own scrolling, or use fixed height / weight constraints (`Modifier.weight(1f)`).

4. **Mandatory Multi-State Desktop & Responsive Previews**:
   - Whenever creating or modifying any Composable (Screen, Section, Component, or Dialog), you MUST author or update `@Preview(device = DESKTOP, showBackground = true)` composables covering key interaction states:
     - **Default / Loaded State**: Standard display with complete data.
     - **Empty / Zero State**: Empty list or zero-rating/new publication (`rating = 0.0` honestly displayed).
     - **Loading / Progress State**: In-flight state with progress indicators active.
     - **Error / Failure State**: Error banners, validation alerts, or network failure feedback.
     - **Compact / Mobile Variant**: Constrained layout preview (e.g. mobile drawer, stacked card layout) where applicable.
   - Always wrap previews in `MaterialTheme` or appropriate design system container.
   - Never consider a UI task complete without verifying that all previews compile cleanly and reflect the newly authored states.

5. **Design System & Typography**:
   - Colors: Ink Navy (`#0D1F2D`), Brand Teal (`#00B5C8`), Brand Green (`#60BD65`), Surface Pure (`#FFFFFF`), Surface Alt (`#F7FAFB`). Never use pure `#000000`.
   - Typography: Headlines use `Montserrat` (bold/track-tight), body copy and tracked uppercase eyebrows use `DM Sans`.

6. **Live Production Integration & Anti-Mocking Guardrail**:
   - Every user-facing feature (Catalog, Book Details, CMS Workbench) must bind to live, persistent state repositories (`AdminDataStorage`, SQLDelight, Ktor HTTP API), never to static dummy lists.
   - Catalog filtering and sorting (Search query, Category chips, Sort by, Year) must reactively filter and order live books fetched from `GetAdminBooksUseCase()`.
   - In `BookDetailsScreen`, all displayed metadata (title, author, category, stars, readers, reading time, total pages, chapters, and PDF reader URL) must reflect dynamic properties from `AdminBook`.
   - When updating data in CMS, trigger real-time sync notification so Web Sekota updates instantly via WebSocket without page reload.
   - **Reader & Telemetry Integrity Protocol (`bookinteractiontool`)**:
     - **Registered Readers (`ratingCount`)**: Must exclusively reflect verified accounts/active collaborators (e.g. `RP-2026-ZQAX` from `activeUsers` / `recentActivities`), NEVER raw anonymous clicks or session counts.
     - **Interactions (`interactions`)**: Represent total sessions and tool usages (`totalInteractions`). Always display separately from registered reader count.
     - **Zero-Mock & Zero-Dummy Invariant for Stars**: If a book has no engagement score (`averageEngagementScore == 0.0`) or no reader interactions, it MUST NOT be assigned artificial fallback ratings (like 4.5 or 4.8 stars) or fabricated reader counts. It must be displayed honestly as "Publikasi Baru" / "Belum ada rating" with rating = 0.0.
     - **CMS Read-Only Telemetry Invariant**: Rating stars and reader counts are calculated strictly by the telemetry engine in `bookinteractiontool`; CMS operators must NOT be allowed to manually input or override star ratings and reader numbers.

7. **Diagram Generation Standard (PlantUML)**:
   - When generating or re-rendering PlantUML diagrams (`.puml` to `.png` / `.svg`), always use the python `plantuml` library (`python3 -c "import plantuml; ..."`) rather than manual deflate/base64 encoders to guarantee valid diagram rendering.

8. **Web Navigation & Material Design 3 (M3) App Bar Standards**:
   - **Zero Collision & Overlay Guardrail**: Status badges (seperti WebSocket Sync, Telemetry) DILARANG keras ditaruh sebagai elemen floating bebas yang menimpa tombol CTA atau elemen app bar. Status pill harus terintegrasi langsung di dalam layout `Surface` / Row app bar.
   - **Section & Cross-Screen Navigation**:
     - Tombol section Landing Page (misal *Solusi*, *Produk*, *Kontak*) harus mendukung navigasi lintas layar (beralih ke `Screen.Landing` bila sedang di layar lain) dan melakukan smooth animated scroll ke posisi section yang dituju.
     - Indikator aktif (*active pill indicator*) harus responsif dan dinamis mencerminkan section atau rute yang aktif, bukan statis.
     - **Component Tag Relocation**: Navigasi scroll ke section wajib memanfaatkan Compose `BringIntoViewRequester` yang di-tag langsung pada Composable target, bukan mengandalkan hardcoded pixel offset yang rentan meleset di berbagai breakpoint resolusi.

9. **Wasm Development Compilation & Linking Invariant**:
   - Ketika memverifikasi perubahan kode pada target Web Wasm, `./gradlew :composeApp:compileKotlinWasmJs` HANYA mengkompilasi modul Kotlin.
   - Untuk memperbarui artefak biner Wasm yang disajikan oleh webpack dev server (`localhost:8080`), SELALU jalankan task linking:
     `./gradlew :composeApp:wasmJsDevelopmentExecutableCompileSync`
     sebelum menguji atau mengambil tangkapan layar browser.

10. **Compose Multiplatform Wasm Rendering & State Safeguards**:
    - **Interactive State Hoisting**: State interaktif seperti toggle menu drawer, dialogs, atau expanding sheets DILARANG didefinisikan di dalam nested layout constraints (`BoxWithConstraints`). Wajib di-hoist ke level fungsi Composable teratas atau state holder untuk menjamin determinisme recomposition pada Skiko Wasm canvas.
    - **Synchronous Breakpoint Measurement**: Gunakan `BoxWithConstraints` di level layout teratas untuk mengukur breakpoint responsif (`isCompact`, `WindowWidth`) secara sinkron pada frame pertama (Frame 0). DILARANG menggunakan fallback `onSizeChanged` dengan initial state dummy (seperti `isCompact = true`), karena memicu 1-frame flash layout mobile pada layar desktop.
    - **Zero-Latency In-Memory Brand Vector Rendering**: Untuk elemen identitas visual utama yang berada di *above-the-fold* (seperti logo icon Sekota pada top app bar), HINDARI ketergantungan pada `painterResource` asinkron melalui jaringan Wasm HTTP. Wajib menggunakan render vektor native Compose berbasis `Canvas` dan `PathParser` in-memory (`SekotaIconMark`) agar brand mark tampil instan pada frame pertama tanpa delay jaringan dan bebas dari bug repaint/invalidation Skiko Wasm.

11. **CMS Workbench Adaptive Layout Standards**:
    - Grid kartu di CMS Workbench (misal `IntelligenceSuiteTab`, `MerchandiseTab`) DILARANG menggunakan `GridCells.Fixed` yang kaku.
    - Wajib menggunakan `GridCells.Adaptive(minSize = 280.dp .. 320.dp)` untuk mendukung multi-form factor (Desktop, Tablet, Mobile) tanpa pemotongan isi kartu atau horizontal overflow.

12. **MVI Presentation Architecture & Idiomatic Pure KMP Dependency Injection**:
    - **Single Source of Truth**: Seluruh state navigasi layar publik maupun workspace kompleks di CMS (seperti `ProductRegistryScreen`) wajib dikelola secara terpusat menggunakan pola MVI:
      - **State**: Data class immutable tunggal (misal `ProductRegistryState`) yang memodelkan seluruh daftar entitas, filter aktif, query pencarian, status loading, dan sealed interface modal aktif (`ActiveModalState: None | BookMetadataModal | SuiteProductModal | MerchModal`) untuk mencegah benturan atau kebocoran state antar-modal.
      - **Intent**: Sealed interface aksi pengguna (misal `ProductRegistryIntent` atau `NavigationIntent`) untuk seluruh mutasi, pergantian filter, input field form, dan aksi CRUD. UI dilarang memutasikan state secara langsung.
      - **Side-Effect Channel**: Efek samping satu kali jalan (seperti Snackbar toast atau interceptor dialog konfirmasi production guard) dialirkan melalui `Channel<Effect>(Channel.BUFFERED)`.
      - **ViewModel**: Dedicated ViewModel mengelola `StateFlow<State>` dan memancarkan efek, memisahkan logika bisnis dari Composable rendering.
    - **Lightweight Idiomatic DI Container**: Dependency injection pada level client KMP diimplementasikan menggunakan `AppContainer` murni berbasis `by lazy` instantiations untuk Service, Repository, UseCase, dan Coordinator. HINDARI overhead library DI eksternal yang menambah ukuran bundle Wasm.

13. **Landing Page & Public Section Horizontal Alignment Standard**:
    - **Single Source of Truth**: Seluruh seksi di Landing Page (Hero, Features, Value Loop, Suite, E-Book, Value Prop, Contact, Footer) WAJIB menggunakan utility function `contentHorizontalPadding(maxWidth)` yang didefinisikan di `com.sekota.ui.Responsive.kt`.
    - **Dilarang Hardcoded Dp**: DILARANG keras menggunakan nilai hardcoded (seperti `120.dp`, `64.dp`, atau `20.dp`) pada padding horizontal container utama seksi.
    - **Scope Measurement**: Nilai `maxWidth` harus diturunkan langsung dari scope `BoxWithConstraints` teratas (`val horizontalPadding = contentHorizontalPadding(maxWidth)`) untuk menjamin sinkronisasi breakpoint gutter pada Compact (20dp/32dp), Expanded/Large (64dp), dan Ultrawide/ExtraLarge (symmetric auto-centering capped at `MaxContentWidth` 1400dp).
    - **Zero Root Pollution**: `LandingScreen` dilarang menerapkan padding horizontal global pada root Column yang dapat mengintervensi atau menggandakan padding individual seksi.

14. **Dynamic Global Footer & CMS Content Governance**:
    - **CMS Dynamic Binding**: Komponen Footer publik (`Footer.kt`) wajib mengonsumsi data secara dinamis dari `FooterConfig` via `GetAdminFooterUseCase` dan mendengarkan stream `syncService.syncEventFlow`.
    - **CMS Workbench Management**: Seluruh kolom tautan (Produk, Perusahaan, Legal), sosial media, tagline, afiliasi, dan hak cipta dikelola melalui modul "Footer Content Management" di CMS tanpa perlu re-deploy kode sumber.
    - **Multi-Language Dynamic Resolution**: Pergantian bahasa via Language Switcher (`BAHASA INDONESIA` / `ENGLISH`) wajib menerjemahkan judul kolom, label navigasi standar, tagline, dan copyright secara dinamis melalui kamus lokal tanpa hardcoding statis terpisah.
    - **Resilience & Graceful Fallback**: Komponen Footer wajib dilengkapi penanganan error/fallback bawaan canonical `FooterConfig()` agar tata letak halaman utama tidak rusak (*zero broken layout*) ketika koneksi sinkronisasi terputus.

15. **Desktop AWT / Swing Interoperability & Non-Blocking Coroutine Invariant**:
    - **Zero-Block Skiko Dispatcher**: Dialog modal berbasis Swing/AWT (seperti `JFileChooser`, `JOptionPane`, atau `JColorChooser`) DILARANG KERAS dipanggil secara sinkron di dalam coroutine thread Skiko (`Dispatchers.Main` / `FlushCoroutineDispatcher`). Pemanggilan modal sinkron akan memblokir AWT event queue dan merusak coroutine continuation machinery (`Fatal exception in coroutines machinery for DispatchedContinuation`).
    - **Background Thread Offload**: Operasi dialog file picker wajib di-offload ke background thread (`Thread { ... }.start()`) dan hasilnya dikembalikan ke thread UI secara aman via `SwingUtilities.invokeLater`.
    - **Explicit Cancellation & Error Recovery**: File picker lintas platform (`FileInterop`) wajib menyediakan callback `onCancel: () -> Unit` (atau memanggil callback dengan nilai null) agar indikator loading / progress spinner (`isUploading`) dapat di-reset deterministik saat operator membatalkan dialog.

16. **CMS Workbench Responsive Dialog & Form Layout Standards**:
    - **Zero Screen Edge Collision on Compact**: Seluruh baris chip filter, switcher subtab pill, dan action bar di CMS wajib dilengkapi `.horizontalScroll(rememberScrollState())` ketika dirender pada layar compact (`windowWidth.isCompact` / <600dp) untuk mencegah terpotongnya elemen UI di tepi layar.
    - **Mandatory Keyboard-Safe Vertical Scroll in Dialogs**: Seluruh modal dialog CMS (`AlertDialog`, `Dialog`, form editor) WAJIB membungkus isi form di dalam `Modifier.verticalScroll(rememberScrollState())`. Hal ini mencegah tombol konfirmasi/batal dan input field bawah terpotong saat keyboard virtual (IME) mobile terbuka atau pada orientasi landscape.
    - **Dialog Responsive Width Bounds**: Dialog kustom dilarang menggunakan `minWidth` hardcoded besar (seperti `min = 600.dp`). Wajib menggunakan lebar responsif dengan batas maksimum (`Modifier.fillMaxWidth(0.92f .. 0.95f).widthIn(max = ...dp)`).

17. **Type-Safe Icon System, Resource Migration & Raw Unicode Emoji Invariant**:
    - **Deprecation of Raw Unicode Emojis**: DILARANG KERAS menggunakan raw unicode emoji (seperti `💾`, `↺`, `➕`, `⚙️`, `👕`, `📖`, `ℹ️`, `✅`, `❌`) atau karakter panah/simbol teks (seperti `←`, `✕`) di dalam label tombol, indikator status, emblem modal, atau fallback kartu visual pada target CMS maupun Web publik. Raw emoji menyebabkan rendering tidak konsisten antar-OS, missing glyph boxes, dan Skiko canvas invalidation issue pada Web Wasm.
    - **Native Vector Icons & Drawables**: Wajib menggunakan vektor in-memory native Compose (`Sekota*VectorIcon` berbasis `Canvas` dan `PathParser`) atau `Res.drawable` resmi.
    - **Structured Status Feedback Model**: DILARANG menggunakan string-based feedback check (seperti `if (status.startsWith("✅"))`). Wajib menggunakan data class immutable `CmsStatusFeedback(isSuccess: Boolean, message: String)` bersama komponen `CmsStatusBanner(feedback: CmsStatusFeedback?)`.

18. **OpenProject Work Package Synchronization & Parent Aggregation Invariant**:
    - **Optimistic Locking**: Setiap mutasi `PATCH /api/v3/work_packages/{id}` wajib menyertakan `lockVersion` terbaru yang diambil dari GET payload sebelum melakukan update.
    - **Parent Work Package Read-Only Fields**: Pada work package bertipe User Story / Feature yang memiliki children tasks, properti `startDate`, `dueDate`, dan `percentageDone` bersifat turunan agregat (*read-only*). Percobaan menulis properti tersebut pada parent akan memicu HTTP 422 `PropertyIsReadOnly`.
    - **Two-Phase Closure Protocol**:
      1. Perbarui seluruh child tasks terlebih dahulu dengan `startDate`, `dueDate`, `percentageDone = 100`, dan status Closed (`/api/v3/statuses/12`).
      2. Perbarui parent User Story HANYA dengan `lockVersion`, status Closed (`/api/v3/statuses/12`), dan `comment` ringkasan jurnal.

19. **Centralized Domain Navigation & Sync Payload Invariants**:
    - **Domain Navigation Intent Decoupling**:
      - Composable UI components (`Navbar.kt`, `Footer.kt`, Drawer/Dialogs) DILARANG keras melakukan matching string URL langsung (seperti `route == "/merchandise"`) atau token teks opaque (seperti `urlOrCode == "MERCH"`).
      - Seluruh penanganan navigasi wajib mengalir melalui domain sealed class `AppDestination` (`Landing`, `SolusiSection`, `ProdukSection`, `KontakSection`, `Catalog`, `Merchandise`, `Login`, `Profile`, dll.) dan dievaluasi via `NavigationRouter.parseDestination(rawRouteOrCode)`.
    - **Bi-Directional Route Mapping**:
      - Pemetaan intent domain ke URL hash atau web route dikelola terpusat di `NavigationRouter.getRoute(destination)` sehingga perubahan skema URL tidak mengubah kode Composable UI.
    - **Sync Server Payload Completeness Invariant**:
      - Setiap kali entitas baru ditambahkan ke CMS (seperti `navigationItems`, `footerConfig`, `valueLoopConfig`), endpoint `GET /api/v1/sync` dan `POST /api/v1/sync` pada `CmsSyncServer.kt` WAJIB menyertakan entitas tersebut di dalam `SyncDataPayload`.
      - Web reader polling dilarang keras menimpa state lokal dengan nilai canonical default akibat ketiadaan field pada payload sinkronisasi.



