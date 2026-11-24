package com.sekota.features.companyprofile.presentation.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sekota.features.admin.domain.model.AboutConfig
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.LocalizedText
import com.sekota.features.admin.domain.model.ServiceOffering
import com.sekota.features.admin.domain.model.ValueLoopConfig
import com.sekota.localization.AppLocale
import com.sekota.localization.stringResource
import com.sekota.ui.brand.*

// =====================================================================================
// Tentang (WEB-07-02-01 / #22664)
// =====================================================================================

/** "Tentang Sekota": header on the left, the three reasons on the right. */
@Composable
fun AboutSection(about: AboutConfig, locale: AppLocale, modifier: Modifier = Modifier) {
    Band(modifier) {
        val stacked = BrandLayout.isNarrow(maxWidth)
        val compact = maxWidth <= 600.dp
        BrandContainer {
            val header: @Composable (Modifier) -> Unit = { m ->
                SectionHeader(
                    overline = about.eyebrow.resolve(locale),
                    title = about.title.resolve(locale),
                    lead = about.description.resolve(locale),
                    compact = compact,
                    modifier = m.reveal()
                )
            }
            val reasons: @Composable (Modifier) -> Unit = { m ->
                Column(m, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    about.pillars.forEachIndexed { i, pillar ->
                        val source = remember { MutableInteractionSource() }
                        val hovered by source.collectIsHoveredAsState()
                        val border by animateColorAsState(if (hovered) BrandColors.Cyan.copy(alpha = 0.45f) else BrandColors.Line, tween(400), label = "reasonBorder")
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .reveal(i + 1)
                                .hoverLift(0.dp, source)
                                .clip(BrandShapes.Faq)
                                .background(Color.White)
                                .border(1.dp, border, BrandShapes.Faq)
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconTile(BrandIcons.named(pillar.iconName), tilted = hovered)
                            Column(Modifier.weight(1f)) {
                                Text(pillar.title.resolve(locale), style = brandType().h4, color = BrandColors.Ink)
                                Text(pillar.description.resolve(locale), style = brandType().small, color = BrandColors.Ink2)
                            }
                        }
                    }
                }
            }
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(40.dp)) { header(Modifier); reasons(Modifier.fillMaxWidth()) }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(80.dp), verticalAlignment = Alignment.CenterVertically) {
                    header(Modifier.weight(1f)); reasons(Modifier.weight(1f))
                }
            }
        }
    }
}

// =====================================================================================
// Layanan (WEB-07-02-01 / #22664)
// =====================================================================================

/** Four service cards, each linked to the platform that powers it. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServicesSection(
    services: List<ServiceOffering>,
    products: List<AdminProduct>,
    additionalOfferings: List<LocalizedText>,
    locale: AppLocale,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Band(modifier, color = BrandColors.Subtle) {
        val columns = BrandLayout.fourUpColumns(maxWidth)
        val compact = maxWidth <= 600.dp
        BrandContainer {
            SectionHeader(stringResource("cp_services_overline"), stringResource("cp_services_title"), compact = compact, modifier = Modifier.reveal())
            BrandGrid(services, columns, Modifier.padding(top = BrandLayout.SectionHeaderGap)) { index, service, cellModifier ->
                ServiceCard(
                    service = service,
                    index = index,
                    product = products.firstOrNull { it.code == service.productCode },
                    locale = locale,
                    onProductClick = onProductClick,
                    modifier = cellModifier.reveal(index % columns)
                )
            }
            if (additionalOfferings.isNotEmpty()) {
                FlowRow(
                    Modifier.padding(top = 32.dp).reveal(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource("cp_services_also"), style = brandType().smallStrong, color = BrandColors.Ink2)
                    additionalOfferings.forEach { BrandChip(it.resolve(locale)) }
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceOffering,
    index: Int,
    product: AdminProduct?,
    locale: AppLocale,
    onProductClick: (String) -> Unit,
    modifier: Modifier
) {
    val type = brandType()
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val reduce = LocalReduceMotion.current
    Column(
        modifier
            .hoverLift(6.dp, source)
            .clip(BrandShapes.Card)
            .background(Color.White)
            .then(
                if (hovered && !reduce) Modifier.border(1.dp, BrandBrushes.Primary, BrandShapes.Card)
                else Modifier.border(1.dp, BrandColors.Line, BrandShapes.Card)
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Text(service.number, style = type.mono, color = BrandColors.Muted)
            IconTile(BrandIcons.forService(index), tilted = hovered)
        }
        Column {
            Text(service.title.resolve(locale), style = type.h3, color = BrandColors.Ink, modifier = Modifier.semantics { heading() })
            Text(service.summary.resolve(locale), style = type.small, color = BrandColors.Ink2, modifier = Modifier.padding(top = 8.dp))
        }
        HorizontalDivider(color = BrandColors.Line2)
        Column(Modifier.weight(1f, fill = true), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            service.highlights.forEach { CheckItem(it.resolve(locale)) }
        }
        if (product != null) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(BrandShapes.Toggle)
                    .background(BrandColors.Subtle)
                    .clickable(role = Role.Button) { onProductClick(product.routeSlug) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource("cp_services_platform"), style = type.mono, color = BrandColors.Muted)
                ProductLogo(product, onDark = false, modifier = Modifier.height(19.dp), textStyle = type.smallStrong)
            }
        }
    }
}

// =====================================================================================
// Cara Kerja (WEB-07-02-02 / #22665)
// =====================================================================================

private fun localized(id: String, en: String?, locale: AppLocale) =
    if (locale == AppLocale.EN && !en.isNullOrBlank()) en else id

/** Four process stages and the ways to engage Sekota. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProcessSection(process: ValueLoopConfig, locale: AppLocale, modifier: Modifier = Modifier) {
    Band(modifier) {
        val columns = BrandLayout.fourUpColumns(maxWidth)
        val compact = maxWidth <= 600.dp
        val type = brandType()
        BrandContainer {
            SectionHeader(
                overline = localized(process.sectionEyebrow, process.sectionEyebrowEn, locale),
                title = localized(process.title, process.titleEn, locale),
                lead = localized(process.description, process.descriptionEn, locale),
                compact = compact,
                modifier = Modifier.reveal()
            )
            BrandGrid(process.steps, columns, Modifier.padding(top = BrandLayout.SectionHeaderGap + 16.dp)) { index, step, cellModifier ->
                val source = remember { MutableInteractionSource() }
                val hovered by source.collectIsHoveredAsState()
                val border by animateColorAsState(if (hovered) BrandColors.Cyan.copy(alpha = 0.45f) else BrandColors.Line, tween(400), label = "stepBorder")
                Column(
                    cellModifier
                        .reveal(index % columns)
                        .hoverLift(4.dp, source)
                        .clip(BrandShapes.Card)
                        .background(Color.White)
                        .border(1.dp, border, BrandShapes.Card)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        GradientText(step.stepNumber, style = type.stepNumber)
                        IconTile(BrandIcons.forStage(index), size = 44.dp, tilted = hovered)
                    }
                    Text(localized(step.title, step.titleEn, locale), style = type.h3, color = BrandColors.Ink)
                    Text(localized(step.subtitle, step.subtitleEn, locale), style = type.small, color = BrandColors.Ink2)
                }
            }
            if (process.engagementModels.isNotEmpty()) {
                Column(Modifier.padding(top = 40.dp).reveal(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Overline(stringResource("cp_process_ways"))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        process.engagementModels.forEach { BrandChip(it.resolve(locale)) }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ previews

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun AboutSectionPreview() {
    BrandTheme(reduceMotion = true) { AboutSection(CompanyProfilePreviewData.content.about, AppLocale.ID) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun ServicesSectionPreview() {
    BrandTheme(reduceMotion = true) {
        val c = CompanyProfilePreviewData.content
        ServicesSection(c.services, c.products, c.about.additionalOfferings, AppLocale.ID, {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 900)
@Composable
fun ServicesSectionTwoColumnEnglishPreview() {
    BrandTheme(reduceMotion = true) {
        val c = CompanyProfilePreviewData.content
        ServicesSection(c.services, c.products, c.about.additionalOfferings, AppLocale.EN, {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun ServicesSectionWithoutPlatformPreview() {
    BrandTheme(reduceMotion = true) {
        val c = CompanyProfilePreviewData.content
        ServicesSection(c.services.take(2), products = emptyList(), additionalOfferings = emptyList(), locale = AppLocale.ID, onProductClick = {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun ProcessSectionPreview() {
    BrandTheme(reduceMotion = true) { ProcessSection(ValueLoopConfig(), AppLocale.ID) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun ProcessSectionCompactEnglishPreview() {
    BrandTheme(reduceMotion = true) { ProcessSection(ValueLoopConfig(), AppLocale.EN) }
}
