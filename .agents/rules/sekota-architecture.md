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
