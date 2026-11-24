package com.sekota.features.companyprofile.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sekota.Footer
import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.companyprofile.presentation.sections.*
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.localization.AppLocale
import com.sekota.localization.stringResource
import com.sekota.ui.brand.*

/**
 * Company profile landing page (WEB-07-02 / #22654): Hero, Tentang, Layanan,
 * Cara Kerja, Platform, Wawasan, Mitra, FAQ and Footer.
 *
 * Each section carries a BringIntoViewRequester "component tag" and reports its
 * top offset so the navigation coordinator can scroll to it and highlight the
 * active navbar link.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompanyProfileScreen(
    state: CompanyProfileUiState,
    locale: AppLocale,
    onIntent: (CompanyProfileIntent) -> Unit,
    onRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    navInset: Dp = 80.dp,
    anchorRequesters: Map<LandingAnchor, BringIntoViewRequester> = remember { LandingAnchor.entries.associateWith { BringIntoViewRequester() } },
    onAnchorPositioned: (LandingAnchor, Int) -> Unit = { _, _ -> }
) {
    fun Modifier.anchor(anchor: LandingAnchor): Modifier = this
        .bringIntoViewRequester(anchorRequesters.getValue(anchor))
        .onGloballyPositioned { onAnchorPositioned(anchor, it.positionInParent().y.toInt()) }

    val toProduct: (String) -> Unit = { slug -> onRoute("/produk/$slug") }

    Column(modifier.fillMaxWidth()) {
        when (state) {
            is CompanyProfileUiState.Loaded -> {
                val c = state.content
                HeroSection(c, locale, onServicesClick = { onRoute("#${LandingAnchor.LAYANAN.id}") }, onContactClick = { onRoute("/kontak") }, onProductClick = toProduct, topInset = navInset)
                if (!c.about.isEmpty) AboutSection(c.about, locale, Modifier.anchor(LandingAnchor.TENTANG))
                if (c.services.isNotEmpty()) ServicesSection(c.services, c.products, c.about.additionalOfferings, locale, toProduct, Modifier.anchor(LandingAnchor.LAYANAN))
                if (c.process.steps.isNotEmpty()) ProcessSection(c.process, locale, Modifier.anchor(LandingAnchor.CARA_KERJA))
                if (c.products.isNotEmpty()) PlatformSection(c.products, locale, toProduct, Modifier.anchor(LandingAnchor.PLATFORM))
                InsightsSection(c.books, state.booksLoading, locale, onBookClick = { slug -> onRoute("/buku#$slug") }, modifier = Modifier.anchor(LandingAnchor.WAWASAN))
                PartnerCtaSection(c.partnerCta, locale, onRoute, Modifier.anchor(LandingAnchor.MITRA))
                if (c.faqs.isNotEmpty()) FaqSection(c.faqs, c.contactEmail, locale, onRoute, Modifier.anchor(LandingAnchor.FAQ))
                Footer(c.footer, locale, c.contactEmail, onRoute)
            }
            CompanyProfileUiState.Loading -> {
                StatusBlock(navInset) {
                    CircularProgressIndicator(color = BrandColors.Cyan, strokeWidth = 3.dp, modifier = Modifier.size(36.dp))
                    Text(stringResource("cp_state_loading"), style = brandType().body, color = BrandColors.Ink2)
                }
                Footer(FooterConfig(), locale, StrategicConversationConfig().email, onRoute)
            }
            CompanyProfileUiState.Empty -> {
                StatusBlock(navInset) {
                    Text(stringResource("cp_state_empty"), style = brandType().lead, color = BrandColors.Ink2, textAlign = TextAlign.Center)
                    BrandButton(stringResource("cp_cta_contact"), onClick = { onRoute("/kontak") }, variant = BrandButtonVariant.Secondary)
                }
                Footer(FooterConfig(), locale, StrategicConversationConfig().email, onRoute)
            }
            is CompanyProfileUiState.Error -> {
                StatusBlock(navInset) {
                    Text(stringResource("cp_state_error"), style = brandType().h3, color = BrandColors.Ink, textAlign = TextAlign.Center)
                    Text(state.message, style = brandType().small, color = BrandColors.Ink2, textAlign = TextAlign.Center)
                    BrandButton(stringResource("cp_state_retry"), onClick = { onIntent(CompanyProfileIntent.Retry) })
                }
                Footer(FooterConfig(), locale, StrategicConversationConfig().email, onRoute)
            }
        }
    }
}

@Composable
private fun StatusBlock(navInset: Dp, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().heightIn(min = 560.dp).padding(top = navInset + 64.dp, bottom = 64.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        content = content
    )
}

// =========================================================================
// DESKTOP PREVIEWS (WEB-07-02-04 / #22667)
// =========================================================================

@Composable
private fun ScreenPreview(state: CompanyProfileUiState, locale: AppLocale = AppLocale.ID) {
    BrandTheme(reduceMotion = true) { CompanyProfileScreen(state, locale, onIntent = {}, onRoute = {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 7600)
@Composable
fun CompanyProfileLoadedPreview() = ScreenPreview(CompanyProfileUiState.Loaded(CompanyProfilePreviewData.content))

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 7600)
@Composable
fun CompanyProfileEnglishPreview() = ScreenPreview(CompanyProfileUiState.Loaded(CompanyProfilePreviewData.content), AppLocale.EN)

@Preview(device = DESKTOP, showBackground = true, widthDp = 390, heightDp = 13000)
@Composable
fun CompanyProfileCompactPreview() = ScreenPreview(CompanyProfileUiState.Loaded(CompanyProfilePreviewData.content))

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 7600)
@Composable
fun CompanyProfileBooksLoadingPreview() =
    ScreenPreview(CompanyProfileUiState.Loaded(CompanyProfilePreviewData.content.copy(books = emptyList()), booksLoading = true))

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 1100)
@Composable
fun CompanyProfileLoadingPreview() = ScreenPreview(CompanyProfileUiState.Loading)

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 1100)
@Composable
fun CompanyProfileEmptyPreview() = ScreenPreview(CompanyProfileUiState.Empty)

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 1100)
@Composable
fun CompanyProfileErrorPreview() = ScreenPreview(CompanyProfileUiState.Error("Tidak dapat membaca penyimpanan konten."))
