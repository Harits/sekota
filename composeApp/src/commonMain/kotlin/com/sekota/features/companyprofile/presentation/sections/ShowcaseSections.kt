package com.sekota.features.companyprofile.presentation.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.Faq
import com.sekota.features.admin.domain.model.PartnerCtaConfig
import com.sekota.localization.AppLocale
import com.sekota.localization.stringResource
import com.sekota.ui.brand.*
import org.jetbrains.compose.resources.painterResource

// =====================================================================================
// Platform — dark band (WEB-07-02-02 / #22665)
// =====================================================================================

@Composable
fun PlatformSection(products: List<AdminProduct>, locale: AppLocale, onProductClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().clipToBounds().background(BrandBrushes.DarkBand).platformGlows()) {
        Band(color = Color.Transparent) {
            val stacked = BrandLayout.isNarrow(maxWidth)
            val compact = maxWidth <= 600.dp
            BrandContainer {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    SectionHeader(
                        stringResource("cp_platform_overline"), stringResource("cp_platform_title"),
                        lead = stringResource("cp_platform_lead"), onDark = true, compact = compact,
                        modifier = Modifier.weight(1f, fill = false).reveal()
                    )
                    if (!stacked) {
                        Text(stringResource("cp_platform_tech"), style = brandType().mono, color = BrandColors.OnDarkMuted, modifier = Modifier.padding(start = 24.dp, bottom = 8.dp))
                    }
                }
                BrandGrid(products, if (stacked) 1 else 2, Modifier.padding(top = BrandLayout.SectionHeaderGap)) { index, product, cellModifier ->
                    PlatformTile(product, locale, onProductClick, cellModifier.reveal(index % 2))
                }
            }
        }
    }
}

/** Soft green (top-left) and cyan (bottom-right) glows of the dark band (.glow.a / .glow.b). */
private fun Modifier.platformGlows(): Modifier = drawBehind {
    fun glow(color: Color, center: Offset, radius: Float) = drawCircle(
        Brush.radialGradient(listOf(color.copy(alpha = 0.2f), Color.Transparent), center = center, radius = radius),
        radius = radius, center = center
    )
    glow(BrandColors.Green, Offset(size.width * 0.1f, size.height * 0.05f), 640.dp.toPx())
    glow(BrandColors.Cyan, Offset(size.width * 0.9f, size.height * 0.95f), 720.dp.toPx())
}

@Composable
private fun PlatformTile(product: AdminProduct, locale: AppLocale, onProductClick: (String) -> Unit, modifier: Modifier) {
    val type = brandType()
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val reduce = LocalReduceMotion.current
    val border by animateColorAsState(if (hovered) BrandColors.Cyan.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.1f), tween(500), label = "ptBorder")
    val shotScale by animateFloatAsState(if (hovered && !reduce) 1.03f else 1f, tween(1200, easing = BrandEasing), label = "ptShot")
    Column(
        modifier
            .hoverLift(0.dp, source)
            .clip(BrandShapes.CardLarge)
            .background(Color.White.copy(alpha = if (hovered) 0.07f else 0.05f))
            .border(1.dp, border, BrandShapes.CardLarge)
            .clickable(interactionSource = source, indication = null, role = Role.Button) { onProductClick(product.routeSlug) }
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            ProductLogo(product, onDark = true, modifier = Modifier.height(32.dp), textStyle = type.h3)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(stringResource("cp_platform_more"), style = type.smallStrong, color = Color.White)
                Icon(BrandIcons.ArrowUpRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        Text(product.platformSummary.resolve(locale), style = type.lead, color = BrandColors.OnDarkMuted, modifier = Modifier.weight(1f, fill = true))
        BrandAssets.drawable(product.dashboardAsset)?.let { shot ->
            Image(
                painterResource(shot),
                contentDescription = stringResource("cp_platform_dashboard", product.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1064f / 592f)
                    .shadow(30.dp, RoundedCornerShape(14.dp), ambientColor = Color.Black, spotColor = Color.Black)
                    .clip(RoundedCornerShape(14.dp))
                    .graphicsLayer { scaleX = shotScale; scaleY = shotScale }
            )
        }
    }
}

// =====================================================================================
// Wawasan — book shelf (WEB-07-02-02 / #22665)
// =====================================================================================

@Composable
fun InsightsSection(
    books: List<AdminBook>,
    booksLoading: Boolean,
    locale: AppLocale,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Band(modifier) {
        val columns = BrandLayout.shelfColumns(maxWidth)
        val compact = maxWidth <= 600.dp
        BrandContainer {
            SectionHeader(
                stringResource("cp_insights_overline"), stringResource("cp_insights_title"),
                lead = stringResource("cp_insights_lead"), compact = compact, modifier = Modifier.reveal()
            )
            val shelfModifier = Modifier.padding(top = BrandLayout.SectionHeaderGap)
            when {
                books.isNotEmpty() -> BrandGrid(books, columns, shelfModifier, gap = if (compact) 20.dp else 24.dp, equalHeight = false) { index, book, cellModifier ->
                    BookCard(book, locale, onBookClick, cellModifier.reveal(index % columns))
                }
                booksLoading -> BrandGrid(List(columns) { it }, columns, shelfModifier, equalHeight = false) { _, _, cellModifier ->
                    Column(cellModifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.fillMaxWidth().aspectRatio(180f / 248f).clip(BrandShapes.Cover).background(BrandColors.MutedBg))
                        Box(Modifier.fillMaxWidth(0.8f).height(16.dp).clip(BrandShapes.Pill).background(BrandColors.MutedBg))
                        Box(Modifier.fillMaxWidth(0.5f).height(12.dp).clip(BrandShapes.Pill).background(BrandColors.Line2))
                    }
                }
                else -> Text(stringResource("cp_state_empty"), style = brandType().small, color = BrandColors.Ink2, modifier = shelfModifier)
            }
        }
    }
}

@Composable
private fun BookCard(book: AdminBook, locale: AppLocale, onBookClick: (String) -> Unit, modifier: Modifier) {
    val type = brandType()
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val reduce = LocalReduceMotion.current
    val tilt by animateFloatAsState(if (hovered && !reduce) -4f else 0f, tween(600, easing = BrandEasing), label = "coverTilt")
    Column(
        modifier
            .hoverLift(0.dp, source)
            .clickable(interactionSource = source, indication = null, role = Role.Button) { onBookClick(book.anchorSlug) }
            .pointerHoverIcon(PointerIcon.Hand),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        BookCover(
            book = book,
            contentDescription = stringResource("cp_insights_cover", book.title),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(180f / 248f)
                .graphicsLayer { rotationY = tilt; cameraDistance = 12f * density }
                .shadow(if (hovered) 20.dp else 2.dp, BrandShapes.Cover, ambientColor = BrandColors.Ink, spotColor = BrandColors.Ink)
                .clip(BrandShapes.Cover),
            titleStyle = type.h4
        )
        Spacer(Modifier.height(10.dp))
        Text(book.title, style = type.h4, color = BrandColors.Ink, modifier = Modifier.semantics { heading() })
        if (!book.flowLabel.isBlank) Text(book.flowLabel.resolve(locale), style = type.mono, color = BrandColors.BrandInk)
        val tagline = book.tagline.resolve(locale).ifBlank { book.description }
        if (tagline.isNotBlank()) Text(tagline, style = type.small, color = BrandColors.Ink2, maxLines = 3, overflow = TextOverflow.Ellipsis)
        Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource("cp_insights_more"), style = type.smallStrong.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold), color = BrandColors.BrandInk)
            Icon(BrandIcons.ArrowRight, contentDescription = null, tint = BrandColors.BrandInk, modifier = Modifier.size(15.dp))
        }
    }
}

// =====================================================================================
// Mitra CTA (WEB-07-02-03 / #22666)
// =====================================================================================

@Composable
fun PartnerCtaSection(cta: PartnerCtaConfig, locale: AppLocale, onRoute: (String) -> Unit, modifier: Modifier = Modifier) {
    if (cta.isEmpty) return
    Band(modifier, topPadding = false) {
        val stacked = BrandLayout.isNarrow(maxWidth)
        val compact = maxWidth <= 600.dp
        val type = brandType(compact)
        BrandContainer {
            val body: @Composable (Modifier) -> Unit = { m ->
                Column(m) {
                    Text(cta.title.resolve(locale), style = type.h2, color = Color.White, modifier = Modifier.widthIn(max = 640.dp).semantics { heading() })
                    Text(cta.description.resolve(locale), style = type.lead, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.padding(top = 16.dp).widthIn(max = 620.dp))
                }
            }
            val button: @Composable () -> Unit = {
                BrandButton(
                    cta.ctaLabel.resolve(locale), onClick = { onRoute(cta.ctaRoute) },
                    variant = BrandButtonVariant.OnGradient, trailingIcon = BrandIcons.ArrowRight,
                    modifier = Modifier.widthIn(min = 220.dp)
                )
            }
            Box(Modifier.fillMaxWidth().reveal().clip(BrandShapes.Banner).background(BrandBrushes.PartnerCta)) {
                if (stacked) {
                    Column(Modifier.padding(horizontal = 28.dp, vertical = 40.dp), verticalArrangement = Arrangement.spacedBy(28.dp)) { body(Modifier); button() }
                } else {
                    Row(Modifier.padding(64.dp), horizontalArrangement = Arrangement.spacedBy(48.dp), verticalAlignment = Alignment.CenterVertically) {
                        body(Modifier.weight(1f)); button()
                    }
                }
            }
        }
    }
}

// =====================================================================================
// FAQ (WEB-07-02-03 / #22666)
// =====================================================================================

@Composable
fun FaqSection(faqs: List<Faq>, contactEmail: String, locale: AppLocale, onRoute: (String) -> Unit, modifier: Modifier = Modifier) {
    Band(modifier, color = BrandColors.Subtle) {
        val stacked = BrandLayout.isNarrow(maxWidth)
        val compact = maxWidth <= 600.dp
        var open by remember(faqs) { mutableStateOf(faqs.firstOrNull()?.id) }
        BrandContainer {
            val side: @Composable (Modifier) -> Unit = { m ->
                Column(m.reveal(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    SectionHeader(stringResource("cp_faq_overline"), stringResource("cp_faq_title"), compact = compact)
                    if (contactEmail.isNotBlank()) {
                        Column {
                            Text(stringResource("cp_faq_other"), style = brandType().small, color = BrandColors.Ink2)
                            Row(
                                Modifier.clickable(role = Role.Button) { onRoute("mailto:$contactEmail") }.pointerHoverIcon(PointerIcon.Hand).padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(BrandIcons.Mail, contentDescription = null, tint = BrandColors.BrandInk, modifier = Modifier.size(18.dp))
                                Text(contactEmail, style = brandType().bodyStrong, color = BrandColors.BrandInk)
                            }
                        }
                    }
                }
            }
            val list: @Composable (Modifier) -> Unit = { m ->
                Column(m, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    faqs.forEachIndexed { i, faq ->
                        FaqItem(
                            question = faq.question.resolve(locale),
                            answer = faq.answer.resolve(locale),
                            expanded = open == faq.id,
                            onToggle = { open = if (open == faq.id) null else faq.id },
                            modifier = Modifier.reveal(i)
                        )
                    }
                }
            }
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(40.dp)) { side(Modifier); list(Modifier.fillMaxWidth()) }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(80.dp)) {
                    side(Modifier.widthIn(max = 400.dp).weight(0.36f)); list(Modifier.weight(0.64f))
                }
            }
        }
    }
}

// ------------------------------------------------------------------ previews

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun PlatformSectionPreview() {
    BrandTheme(reduceMotion = true) { PlatformSection(CompanyProfilePreviewData.content.products, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun PlatformSectionCompactEnglishPreview() {
    BrandTheme(reduceMotion = true) { PlatformSection(CompanyProfilePreviewData.content.products, AppLocale.EN, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun InsightsSectionPreview() {
    BrandTheme(reduceMotion = true) { InsightsSection(CompanyProfilePreviewData.content.books, false, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun InsightsSectionLoadingPreview() {
    BrandTheme(reduceMotion = true) { InsightsSection(emptyList(), true, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun InsightsSectionEmptyPreview() {
    BrandTheme(reduceMotion = true) { InsightsSection(emptyList(), false, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun InsightsSectionCompactEnglishPreview() {
    BrandTheme(reduceMotion = true) { InsightsSection(CompanyProfilePreviewData.content.books, false, AppLocale.EN, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun PartnerCtaSectionPreview() {
    BrandTheme(reduceMotion = true) { PartnerCtaSection(CompanyProfilePreviewData.content.partnerCta, AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun PartnerCtaSectionCompactPreview() {
    BrandTheme(reduceMotion = true) { PartnerCtaSection(CompanyProfilePreviewData.content.partnerCta, AppLocale.EN, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun FaqSectionPreview() {
    BrandTheme(reduceMotion = true) { FaqSection(CompanyProfilePreviewData.content.faqs, "info@sekota.id", AppLocale.ID, {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun FaqSectionCompactEnglishPreview() {
    BrandTheme(reduceMotion = true) { FaqSection(CompanyProfilePreviewData.content.faqs, "info@sekota.id", AppLocale.EN, {}) }
}
