package com.sekota.features.companyprofile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sekota.Footer
import com.sekota.features.companyprofile.domain.model.CompanyProfileContent
import com.sekota.features.companyprofile.presentation.sections.*
import com.sekota.localization.AppLocale
import com.sekota.localization.stringResource
import com.sekota.sections.ContactFormSection
import com.sekota.ui.brand.*
import org.jetbrains.compose.resources.painterResource

/*
 * Interim pages for Sprint 7 (WEB-07-03 / #22655).
 *
 * The redesigned product, books and contact pages ship in Sprint 8 (WEB-08-01..03).
 * Until then these pages keep every link from the new landing page working, in the
 * new visual language, using the same domain content. Each shows a short note that
 * the full page is coming.
 */

@Composable
private fun InterimNote() {
    Text(
        stringResource("cp_interim_note"),
        style = brandType().caption,
        color = BrandColors.Muted,
        modifier = Modifier.clip(BrandShapes.Pill).background(BrandColors.Subtle).padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun PageFrame(
    content: CompanyProfileContent,
    locale: AppLocale,
    navInset: Dp,
    onRoute: (String) -> Unit,
    body: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxWidth().background(BrandColors.Background)) {
        BrandContainer(Modifier.padding(top = navInset + 56.dp, bottom = 96.dp), content = body)
        Footer(content.footer, locale, content.contactEmail, onRoute)
    }
}

/** /produk/{slug}: platform summary, the service it powers and its dashboard. */
@Composable
fun ProductPreviewPage(
    slug: String,
    content: CompanyProfileContent,
    locale: AppLocale,
    onRoute: (String) -> Unit,
    navInset: Dp = 80.dp
) {
    val product = content.products.firstOrNull { it.routeSlug.equals(slug, ignoreCase = true) }
    PageFrame(content, locale, navInset, onRoute) {
        if (product == null) {
            SectionHeader(stringResource("cp_platform_overline"), stringResource("cp_state_empty"))
            Spacer(Modifier.height(24.dp))
            BrandButton(stringResource("cp_platform_overline"), onClick = { onRoute("#platform") }, variant = BrandButtonVariant.Secondary)
            return@PageFrame
        }
        val service = content.serviceFor(product)
        val type = brandType()
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            InterimNote()
            ProductLogo(product, onDark = false, modifier = Modifier.height(40.dp), textStyle = type.h2)
            Text(
                service?.title?.resolve(locale) ?: product.categoryEyebrow, style = type.h2, color = BrandColors.Ink,
                modifier = Modifier.semantics { heading() }
            )
            Text(product.tagline.resolve(locale), style = type.lead, color = BrandColors.Ink2, modifier = Modifier.widthIn(max = 640.dp))
            if (!product.flowLabel.isBlank) BrandTag(product.flowLabel.resolve(locale), tone = TagTone.Green)
            service?.highlights?.takeIf { it.isNotEmpty() }?.let { highlights ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { highlights.forEach { CheckItem(it.resolve(locale)) } }
            }
            BrandAssets.drawable(product.dashboardAsset)?.let {
                Image(
                    painterResource(it), contentDescription = stringResource("cp_platform_dashboard", product.name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.widthIn(max = 880.dp).fillMaxWidth().aspectRatio(1064f / 592f)
                        .shadow(24.dp, RoundedCornerShape(14.dp)).clip(RoundedCornerShape(14.dp))
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BrandButton(stringResource("cp_cta_contact"), onClick = { onRoute("/kontak") }, trailingIcon = BrandIcons.ArrowRight)
                BrandButton(stringResource("cp_platform_overline"), onClick = { onRoute("#platform") }, variant = BrandButtonVariant.Secondary)
            }
        }
    }
}

/** /buku#slug: the selected book first, then the whole shelf. */
@Composable
fun BooksPreviewPage(
    bookSlug: String?,
    content: CompanyProfileContent,
    booksLoading: Boolean,
    locale: AppLocale,
    onRoute: (String) -> Unit,
    navInset: Dp = 80.dp
) {
    val selected = content.books.firstOrNull { it.anchorSlug == bookSlug } ?: content.books.firstOrNull()
    Column(Modifier.fillMaxWidth().background(BrandColors.Background)) {
      BrandContainer(Modifier.padding(top = navInset + 56.dp)) {
        val type = brandType()
        InterimNote()
        Spacer(Modifier.height(24.dp))
        if (selected != null) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val narrow = BrandLayout.isNarrow(maxWidth)
                val cover: @Composable () -> Unit = {
                    BookCover(
                        selected, stringResource("cp_insights_cover", selected.title),
                        Modifier.width(if (narrow) 200.dp else 260.dp).aspectRatio(180f / 248f).shadow(20.dp, BrandShapes.Cover).clip(BrandShapes.Cover),
                        type.h4
                    )
                }
                val text: @Composable (Modifier) -> Unit = { m ->
                    Column(m, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Overline(stringResource("cp_insights_overline"))
                        Text(selected.title, style = brandType(narrow).h2, color = BrandColors.Ink, modifier = Modifier.semantics { heading() })
                        if (!selected.flowLabel.isBlank) Text(selected.flowLabel.resolve(locale), style = type.mono, color = BrandColors.BrandInk)
                        Text(selected.tagline.resolve(locale).ifBlank { selected.description }, style = type.lead, color = BrandColors.Ink2)
                        if (selected.author.isNotBlank()) Text(selected.author, style = type.smallStrong, color = BrandColors.Ink)
                        BrandButton(stringResource("cp_cta_contact"), onClick = { onRoute("/kontak") }, trailingIcon = BrandIcons.ArrowRight)
                    }
                }
                if (narrow) Column(verticalArrangement = Arrangement.spacedBy(24.dp)) { cover(); text(Modifier) }
                else Row(horizontalArrangement = Arrangement.spacedBy(48.dp), verticalAlignment = Alignment.CenterVertically) { cover(); text(Modifier.weight(1f)) }
            }
        }
      }
      InsightsSection(content.books, booksLoading, locale, onBookClick = { onRoute("/buku#$it") })
      Footer(content.footer, locale, content.contactEmail, onRoute)
    }
}

/** /kontak: contact details and the existing inquiry form (redesign in Sprint 8). */
@Composable
fun ContactPage(content: CompanyProfileContent, locale: AppLocale, onRoute: (String) -> Unit, navInset: Dp = 80.dp) {
    Column(Modifier.fillMaxWidth().background(BrandColors.Background)) {
        BrandContainer(Modifier.padding(top = navInset + 56.dp, bottom = 32.dp)) {
            InterimNote()
            Spacer(Modifier.height(24.dp))
            SectionHeader(stringResource("cp_footer_contact"), stringResource("cp_cta_contact"), lead = content.contactEmail)
        }
        ContactFormSection()
        Footer(content.footer, locale, content.contactEmail, onRoute)
    }
}

// ------------------------------------------------------------------ previews

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 1700)
@Composable
fun ProductPreviewPagePreview() {
    BrandTheme(reduceMotion = true) { ProductPreviewPage("clarivio", CompanyProfilePreviewData.content, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 1100)
@Composable
fun ProductPreviewPageUnknownSlugPreview() {
    BrandTheme(reduceMotion = true) { ProductPreviewPage("veridia", CompanyProfilePreviewData.content, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 2000)
@Composable
fun BooksPreviewPagePreview() {
    BrandTheme(reduceMotion = true) { BooksPreviewPage("accountable-esg", CompanyProfilePreviewData.content, false, AppLocale.EN, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390, heightDp = 3000)
@Composable
fun BooksPreviewPageCompactPreview() {
    BrandTheme(reduceMotion = true) { BooksPreviewPage(null, CompanyProfilePreviewData.content, false, AppLocale.ID, {}) }
}
