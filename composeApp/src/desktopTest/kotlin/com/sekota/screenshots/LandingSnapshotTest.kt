package com.sekota.screenshots

import com.sekota.*
import com.sekota.features.companyprofile.presentation.*
import kotlin.test.Test
import kotlin.test.assertTrue

/** Renders the landing page, navbar, footer and interim pages for review (#22667, #22671). */
class LandingSnapshotTest {
    @Test
    fun renderLanding() {
        listOf(
            snapshot("landing_desktop_id", 1280, 7600) { CompanyProfileLoadedPreview() },
            snapshot("landing_desktop_en", 1280, 7600) { CompanyProfileEnglishPreview() },
            snapshot("landing_compact_id", 390, 13000) { CompanyProfileCompactPreview() },
            snapshot("landing_books_loading", 1280, 7600) { CompanyProfileBooksLoadingPreview() },
            snapshot("landing_loading", 1280, 1100) { CompanyProfileLoadingPreview() },
            snapshot("landing_empty", 1280, 1100) { CompanyProfileEmptyPreview() },
            snapshot("landing_error", 1280, 1100) { CompanyProfileErrorPreview() },
            snapshot("navbar_transparent", 1280, 100) { NavbarTransparentOverHeroPreview() },
            snapshot("navbar_scrolled", 1280, 100) { NavbarScrolledActiveSectionPreview() },
            snapshot("navbar_en", 1280, 100) { NavbarEnglishPreview() },
            snapshot("navbar_compact_open", 390, 640) { NavbarCompactMenuOpenPreview() },
            snapshot("page_product", 1280, 1700) { ProductPreviewPagePreview() },
            snapshot("page_books", 1280, 2000) { BooksPreviewPagePreview() }
        ).forEach { assertTrue(it.length() > 0, it.name) }
    }
}
