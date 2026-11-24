package com.sekota.screenshots

import com.sekota.ui.brand.*
import kotlin.test.Test
import kotlin.test.assertTrue

/** Renders the design system previews (#22663) to PNG for review. */
class DesignSystemSnapshotTest {
    @Test
    fun renderDesignSystem() {
        val files = listOf(
            snapshot("ds_colors", 1280, 320) { BrandColorTokensPreview() },
            snapshot("ds_typography", 1280, 620) { BrandTypographyPreview() },
            snapshot("ds_controls", 1280, 420) { BrandControlsPreview() },
            snapshot("ds_icons", 1280, 220) { BrandIconsPreview() },
            snapshot("ds_section_header", 1280, 460) { BrandSectionHeaderPreview() },
            snapshot("ds_faq", 1280, 320) { BrandFaqPreview() },
            snapshot("ds_assets", 1280, 420) { BrandAssetsPreview() }
        )
        files.forEach { assertTrue(it.length() > 0, "${it.name} is empty") }
    }
}
