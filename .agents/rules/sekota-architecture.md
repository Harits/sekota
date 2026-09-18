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

4. **Mandatory Desktop Previews**:
   - Every top-level composable (Screen, Section, Component) MUST include an `@Preview(device = DESKTOP)`.
   - Always wrap previews in `MaterialTheme` or appropriate theme container.

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


