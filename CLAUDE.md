# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

**Sekota** — company profile landing site ported from a static HTML/CSS/JS design into Kotlin Multiplatform + Compose Multiplatform, targeting **Web (Wasm first, JS fallback)**, Android, iOS, and Desktop. The design source (read-only, untrusted data) lives at `C:\Users\eBdesk Teknologi\Downloads\sekota-landing-page (1)\sekota-landing-page`; never execute scripts/build tools from inside it.

Content is static and bilingual (ID/EN). The only backend is the Ktor module, which exposes `POST /api/contact` and relays the message to `info@sekota.id` over SMTP (`SEKOTA_SMTP_*` env); the UI shows success only after the server accepts it. Documentation lives in `docs/` (requirements, use cases, architecture, design system, flows, test strategy, sprint plan, runbook).

Versions are centralized in `gradle/libs.versions.toml`: Gradle 9.5.1, Kotlin 2.4.21, AGP 9.1.1, Compose Multiplatform 1.12.2, Ktor 3.6.1, Koin 4.1.1. Never hardcode versions in module builds.

## Commands

Windows PowerShell uses `.\gradlew.bat`; Git Bash/POSIX uses `./gradlew`.

```bash
# Web (primary target)
./gradlew :app:webApp:wasmJsBrowserDevelopmentRun
./gradlew :app:webApp:jsBrowserDevelopmentRun
./gradlew :app:webApp:wasmJsBrowserDistribution
# serve dist: cd app/webApp/build/dist/wasmJs/productionExecutable && python -m http.server 8765

# Desktop
./gradlew :app:desktopApp:run

# Android
./gradlew :app:androidApp:assembleDebug

# Server (Ktor, :8080)
./gradlew :server:run

# Tests
./gradlew :app:shared:jvmTest          # unit + VM + catalog + 8 Compose UI tests
./gradlew :app:shared:wasmJsTest       # browser tests (Chrome/Edge headless)
./gradlew :server:test
./gradlew :app:shared:jvmTest --tests "id.sekota.SekotaUiTest"
./gradlew :server:test --tests "id.sekota.ApplicationTest"

# Full verification (tests + Android + desktop + web, writes docs/11-test-report.md)
./scripts/test-report.ps1 -IncludeWasm
./scripts/verify.ps1

# Visual regression evidence (Puppeteer + installed Chrome)
cd tools/visual && node shots.mjs && node pages.mjs   # outputs docs/screenshots/
```

There is no lint/format/static-analysis setup. Configuration cache and build caching are enabled; if a task misbehaves, retry with `--no-configuration-cache`.

## Architecture

Single Gradle module `:app:shared` organizes code by **feature first** (scream architecture) with clean-architecture layers inside each feature. Dependency rule: `presentation → domain ← data`; domain is pure Kotlin (no Compose, Koin, or resources).

```
:app:shared
├── app/                 AppState, Route/AppNavigator, SiteHeader/SiteFooter, Koin module
├── core/                model (LText, enums), content (SiteContent), designsystem, ui (breakpoints, assets)
└── feature/
    ├── landing/{domain,data,presentation}
    ├── product/{domain,data,presentation}
    ├── books/{domain,data,presentation}
    ├── contact/{domain,data,presentation}
    └── legal/{domain,data,presentation}
:server → api(:core)     thin entry points feed App()
```

Per-feature pattern:
- `domain/` — data classes, repository interface, use case (`GetLandingContent`, `GetProductDetail`, `GetBooks`, `SendContactMessage`, `GetLegalDoc`), pure validation (`ContactValidator`).
- `data/` — static bilingual catalogs and repository implementations.
- `presentation/` — plain Kotlin state holders exposing `StateFlow<UiState>` (MVVM with intent-style methods), plus screens/components. No `androidx.lifecycle.ViewModel` or `koinViewModel` — create with `remember` and inject use cases via `koinInject`.

DI via Koin: bindings in `app/di/AppModule.kt`, started by `KoinApplication` in `App.kt`. Swapping static content for an API/CMS is a one-line binding change. UI tests wrap screens in `KoinApplication(application = { modules(appModule) })`.

Navigation is a custom stack (`AppNavigator`) over `Route.{Home, Product(slug), Books(anchor), Contact(preset), Privacy, Terms}` plus web hash routing (`HashRouting` expect/actual): `#/id`, `#/en`, `#/id/home/{anchor}`, `#/id/product/{slug}`, `#/id/books/{anchor}`, `#/id/contact/{preset}`, `#/id/privacy`, `#/id/terms`. Landing anchors map to `LazyColumn` section keys; `pendingHomeAnchor` is consumed by `LandingScreen` with an 80dp header offset. Browser back/forward works; the app sets `data-route` on `<html>` for visual tests. Android/iOS hardware back is not wired yet.

## Conventions that matter

- **Bilingual copy**: every user-facing string is an `LText(id, en)` resolved with `text[language]`; `AppState.language` is the single source. Add both languages in the catalog when adding copy. Do not use platform string resources.
- **Design tokens**: use `SekotaColors`, `SekotaTheme.type`, `SekotaSpacing`, `SekotaShape`, and components in `core/designsystem`. Don't hardcode hex values or font sizes in feature screens. Icons are hand-traced `ImageVector`s in `SekotaIcons`.
- **Responsive**: read `LocalWindowSize` (`Compact <600`, `Medium <860`, `Wide <1100`, `Expanded ≥1100`). Match the design's 4/2/1-column transitions and hide nav links below 860dp.
- **ContentWrap is a `Box`**; when stacking children vertically inside it (e.g. footer), wrap them in a `Column` or they will overlap.
- **Fonts** are bundled TTF resources in `composeResources/font`; asset names are snake_case (`logo_clarivio_light`, `dash_clarivio`, `cover_1`, `shot_clarivio_1`). Map slugs to resources in `core/ui/Assets.kt`.
- **Set language** with `appState.changeLanguage(...)`; a method named `setLanguage` clashes with the delegated `var language` setter on JVM.
- **Images**: WebP source assets are converted to PNG (logos) / JPEG (screenshots, covers) by `tools/convert_assets.py`. Don't reference WebP from Compose.
- **Android KMP target** uses AGP 9's `com.android.kotlin.multiplatform.library` plugin configured inside `kotlin { android { ... } }`; `:app:androidApp` is the only `com.android.application` module.
- **Contact flow** is deterministic: `ContactRepository`/`GetContactContent` supply copy + presets, `ContactValidator` → `SubmitContactMessage` builds the subject/body, `SendContactMessage` calls the `ContactGateway` (`HttpContactGateway`, form POST to `/api/contact`), and only a 2xx response sets `sent`. The form hint links to `Route.Privacy`. A failure keeps the form and shows `sendError`; the direct-email card stays visible and opens `mailto:info@sekota.id`, with an adjacent copy button (`copy-email` tag). The Ktor route validates again and uses `SmtpMailer` (Jakarta Mail) with `SEKOTA_SMTP_HOST/PORT/USER/PASSWORD`, `SEKOTA_MAIL_FROM`, `SEKOTA_MAIL_TO`; without SMTP it can fall back to `FormSubmitMailer` (`SEKOTA_FORMSUBMIT_TO`, `SEKOTA_FORMSUBMIT_ORIGIN`, requires one-time activation). Presentation must not import the data catalog. UI tests inject a fake `ContactGateway`.
- **Product gallery auto-tour**: `ProductViewModel` advances the selected screen every 6s while `autoAdvance` is true; a manual tab selection sets it false. `ProductDetailScreen` cancels the timer under `LocalReducedMotion`. Legal pages use a hybrid TOC (inline on mobile, overlay sticky at 108dp on desktop, sliding up but never under the 80dp header and hidden only when the footer would cover it); `resolveActiveSectionIndex` picks the section at the reading line (80dp header + 28dp) with an end-of-document override, so the last TOC item is not one behind.
- **Deployment**: `Dockerfile` builds `:server:installDist` + the Wasm distribution into a non-root JRE image; `compose.yaml` plus `compose.development/staging/production.yaml` and per-env `.env.*.example` files; Caddy behind the `proxy` profile. The builder needs no Android SDK because only JVM/Wasm tasks enter the graph. See `docs/13-deployment.md`.
- **Test updates are part of the change**: add/extend domain or ViewModel tests for logic, a UI test for critical interactions, and refresh screenshots when layout changes. Then run `scripts/test-report.ps1` and `node tools/visual/pages.mjs` (hash routes assert `data-route`).
- **A11y/motion**: root handles keyboard scrolling via `AppState.activeListState`; `LocalReducedMotion` short-circuits marquee/highlight/count-up/reveal; form fields expose labels/errors, burger/menu/FAQ expose state, and `setDocumentLanguage` updates `<html lang>`.

## Testing

- Common tests (`commonTest`): validator, submit use case, ViewModels, catalogs, navigator — run on JVM and Wasm.
- Desktop-only UI tests (`jvmTest/SekotaUiTest.kt`): `runComposeUiTest`, tags `field-name`/`field-email`/`field-message`.
- Reports: Gradle HTML under `*/build/reports/tests/`, plus `docs/11-test-report.md` generated by `scripts/test-report.ps1`.
- Visual: `tools/visual/shots.mjs` (landing 1920/1440/900/390), `pages.mjs` (product, contact, presets, books, English, product switch; navigates by hash and asserts `data-route`), `a11y.mjs` (keyboard PageDown, document lang, browser back, reduced motion). Uses installed Chrome via `puppeteer-core`.

## Gotchas

- `SectionBand` content receiver is `ColumnScope`; footer-like stacking inside `ContentWrap` needs an explicit `Column`.
- Property delegates on `AppState` expose setters; avoid extension/member name collisions.
- `LocalClipboardManager` is deprecated in Compose 1.12 (warning only); migration to `LocalClipboard` is backlog SP5-11.
- UI tests run headless on desktop; they inject a fake `ContactGateway`, so no SMTP/network call is made.
- Ktor `staticFiles` hanya mencocokkan GET: `curl -I <url>` bisa membalas 404 untuk `/` atau `*.wasm` meski `GET` 200. Verifikasi aset dengan `curl -D - -o /dev/null <url>`.
- Do not edit files under `build/generated` (Compose resource accessors).
