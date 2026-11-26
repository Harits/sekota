# 09 — Traceability Matrix

| Req | Use case | Layar / komponen | Test otomatis | Bukti visual / dokumen |
|---|---|---|---|---|
| FR-01 | UC-01 | `HeroSection` | `LandingViewModelTest`, `SekotaUiTest.landingRendersHero` | `home-1440-top`, `home-390-top` |
| FR-02 | UC-01 | `SectorMarquee` | — (visual) | `home-1440-top`, `home-390-s2` |
| FR-03 | UC-01 | sections landing | `ContentCatalogTest.landing*` | `home-1440-*`, `home-390-*` |
| FR-04 | UC-01/05 | `SuiteCard`, `LandingViewModel` | `LandingViewModelTest.highlight*` | screenshot suite |
| FR-05 | UC-03 | `SiteHeader`, `LandingScreen` | `AppNavigatorTest` | header pada screenshot |
| FR-06 | UC-04 | `SiteHeader` burger | — (visual) | `home-390-top` |
| FR-07 | UC-02 | `AppState`, semua `LText` | `SekotaUiTest.languageToggle...`, `ContentCatalogTest` | `home-en-top` |
| FR-08 | UC-05 | `ServicesSection` | `ContentCatalogTest`, `ProductViewModelTest` | product page |
| FR-09 | UC-06 | `ProductDetailScreen` | `SekotaUiTest.productPageRendersHeadline` | `product-clarivio-*` |
| FR-10 | UC-06 | tombol "Lihat Fitur" | `ProductViewModelTest` + UI manual | product screenshots |
| FR-11 | UC-07 | `BooksScreen`, `BookBlock` | `ContentCatalogTest.books*`, `SekotaUiTest.booksPageRendersHero` | `books-*` |
| FR-12 | UC-07 | `SeriesBand` | `ContentCatalogTest.booksAreComplete...` | `books-top`/scroll |
| FR-13 | UC-07 | `BooksSection`, `Route.Books(anchor)` | `AppNavigatorTest`, UI manual | `books-top` |
| FR-14 | UC-08 | `ContactScreen` | `SekotaUiTest.contactFormValidatesAndSubmits` | `contact-top` |
| FR-15 | UC-08 | `ContactValidator`, `FormField` | `ContactValidatorTest` (7) | `contact-top` |
| FR-16 | UC-08 | `SendContactMessage`, `HttpContactGateway`, `SuccessPanel`, `SmtpMailer` | `ContactViewModelTest`, `ApplicationTest`, UI test, submit browser | `contact-sent`, `contact-failed` |
| FR-17 | UC-10 | `ContactRepository`, `ContactViewModel.prefill`, CTA wiring | `ContentCatalogTest.contactTopics...`, `ContactViewModelTest.prefill...`, `pages.mjs` | `contact-preset-*` |
| FR-18 | UC-09 | `FaqSection` | `LandingViewModelTest.faq*`, `SekotaUiTest.faqOpensOnClick` | `home-1440-s6/s7` |
| FR-19 | UC-01/08 | `SiteFooter`, `LegalLinks` | `SekotaUiTest.privacyPageRendersPolicy` | `home-1440-s8`, `privacy-top` |
| FR-20 | NFR-01 | `WindowSize`, semua layout | UI test + screenshot | `home-390-*`, `home-1440-*` |
| FR-21 | UC-02 | seluruh katalog | `ContentCatalogTest` (`nonBlankBoth`) | `home-en-top` |
| FR-22 | UC-03/05/07 | `AppNavigator`, breadcrumb | `AppNavigatorTest` | product/books screenshots |
| FR-23 | UC-03/05/06/07/10 | `HashRoute`, `HashRouting`, `AppNavigator` | `HashRouteTest`, `AppNavigatorTest`, `pages.mjs`, `a11y.mjs` | `home-anchor-layanan`, `contact-preset-*` |
| FR-24 | UC-01/03 | root key handler, focus rings | `a11y.mjs` (PageDown) | `a11y-reduced-motion` |
| FR-25 | UC-01/04 | `LocalReducedMotion`, `Reveal`, marquee, highlight | `a11y.mjs` (emulated reduce) | `a11y-reduced-motion` |
| FR-26 | UC-11 | `LegalScreen`, `LegalCatalog`, footer links, contact hint | `LegalCatalogTest`, `HashRouteTest.legalRoutesRoundTrip`, `SekotaUiTest.privacyPageRendersPolicy`, `pages.mjs` | `privacy-top`, `terms-top`, `privacy-390-top` |
| NFR-02 | — | build web | build sukses | ukuran bundle di 00 |
| NFR-05 | — | test suite | 63 JVM + 55 Wasm + 5 server | `11-test-report.md` |
| NFR-08 | UC-08 | API kontak + SMTP, status sukses/gagal | test VM, UI, endpoint, submit browser | `SendContactMessage`, `HttpContactGateway`, `SmtpMailer` |
| NFR-09 | — | `App()` common | build Android/desktop/web/wasm | laporan build |

## Keterlacakan terbalik (test → requirement)

| Test | Requirement |
|---|---|
| `ContactValidatorTest` | FR-15 |
| `SubmitContactMessageTest` | FR-16 |
| `ContactViewModelTest` | FR-15, FR-16, FR-17 |
| `LandingViewModelTest` | FR-04, FR-18 |
| `ProductViewModelTest` | FR-08, FR-09 |
| `AppNavigatorTest` | FR-05, FR-13, FR-22, FR-23 |
| `HashRouteTest` | FR-23 |
| `ContentCatalogTest` | FR-03, FR-07, FR-11, FR-17, FR-21 |
| `SekotaUiTest` | FR-01, FR-07, FR-09, FR-11, FR-14, FR-18 |
| `ApplicationTest` (server) | NFR-09 (server tetap sehat) |
