package com.sekota.features.companyprofile.presentation.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.features.companyprofile.domain.model.CompanyProfileContent
import com.sekota.localization.AppLocale
import com.sekota.localization.stringResource
import com.sekota.ui.brand.*
import kotlinx.coroutines.delay

/**
 * Hero (WEB-07-02-01 / #22664): headline, lead, CTAs and stats on the left; the
 * "Sekota Intelligence Suite" card on the right, highlighting one platform at a
 * time; the sectors Sekota serves underneath. Platform and book counts come
 * from the CMS so they never drift from the actual catalogue.
 *
 * @param topInset space reserved for the overlaying navbar.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroSection(
    content: CompanyProfileContent,
    locale: AppLocale,
    onServicesClick: () -> Unit,
    onContactClick: () -> Unit,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    topInset: Dp = 80.dp
) {
    BoxWithConstraints(modifier.fillMaxWidth().background(BrandColors.Background).heroGrid()) {
        val stacked = BrandLayout.isStacked(maxWidth)
        val compact = maxWidth <= 600.dp
        val type = brandType(compact)
        BrandContainer(
            Modifier.padding(top = topInset + if (stacked) 56.dp else 92.dp, bottom = if (stacked) 64.dp else 96.dp)
        ) {
            val text: @Composable (Modifier) -> Unit = { m ->
                Column(m, verticalArrangement = Arrangement.spacedBy(28.dp)) {
                    BrandChip(stringResource("cp_hero_eyebrow"), Modifier.reveal(0))
                    Column(Modifier.reveal(1).semantics { heading() }) {
                        Text(stringResource("cp_hero_title_1"), style = type.display, color = BrandColors.Ink)
                        GradientText(stringResource("cp_hero_title_2"), style = type.display)
                    }
                    Text(
                        stringResource("cp_hero_lead"), style = type.lead, color = BrandColors.Ink2,
                        modifier = Modifier.widthIn(max = 540.dp).reveal(2)
                    )
                    FlowRow(Modifier.reveal(3), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BrandButton(stringResource("cp_hero_cta_services"), onServicesClick, trailingIcon = BrandIcons.ArrowRight)
                        BrandButton(stringResource("cp_cta_contact"), onContactClick, variant = BrandButtonVariant.Secondary)
                    }
                    Row(Modifier.reveal(4), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                        if (content.products.isNotEmpty()) HeroStat(content.products.size.toString(), stringResource("cp_stat_platforms"), compact)
                        // Shown once the book catalogue has loaded, so the count is never guessed.
                        if (content.books.isNotEmpty()) HeroStat(content.books.size.toString(), stringResource("cp_stat_books"), compact)
                    }
                }
            }
            val card: @Composable (Modifier) -> Unit = { m ->
                IntelligenceSuiteCard(content, locale, onProductClick, m.reveal(2, distance = 40.dp))
            }
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
                    text(Modifier.fillMaxWidth())
                    card(Modifier.fillMaxWidth())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
                    text(Modifier.weight(1.2f))
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) { card(Modifier.widthIn(max = 472.dp).fillMaxWidth()) }
                }
            }
            val sectors = content.about.sectors
            if (sectors.isNotEmpty()) {
                FlowRow(
                    Modifier.padding(top = if (stacked) 48.dp else 72.dp).reveal(5),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    sectors.forEach { BrandChip(it.resolve(locale)) }
                }
            }
        }
    }
}

@Composable
private fun HeroStat(value: String, label: String, compact: Boolean) {
    Column {
        GradientText(value, style = brandType(compact).h2.copy(fontSize = if (compact) 28.sp else 36.sp, lineHeight = if (compact) 34.sp else 42.sp))
        Text(label, style = brandType().small, color = BrandColors.Ink2)
    }
}

/** The design's faint 64dp grid behind the hero, fading towards the edges. */
private fun Modifier.heroGrid(): Modifier = drawBehind {
    val step = 64.dp.toPx()
    val line = BrandColors.Line2
    var x = 0f
    while (x < size.width) { drawLine(line, Offset(x, 0f), Offset(x, size.height), 1f); x += step }
    var y = 0f
    while (y < size.height) { drawLine(line, Offset(0f, y), Offset(size.width, y), 1f); y += step }
    drawRect(
        Brush.radialGradient(
            0.2f to Color.Transparent, 1f to Color.White,
            center = Offset(size.width * 0.55f, size.height * 0.45f),
            radius = maxOf(size.width, size.height) * 0.6f
        )
    )
}

/** "Sekota Intelligence Suite" card: one row per platform, the highlight rotating every 2.6 s. */
@Composable
fun IntelligenceSuiteCard(
    content: CompanyProfileContent,
    locale: AppLocale,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    rotate: Boolean = true
) {
    val type = brandType()
    val products = content.products
    var active by remember(products.size) { mutableStateOf(0) }
    val reduce = LocalReduceMotion.current
    if (rotate && !reduce && products.size > 1) {
        LaunchedEffect(products.size) {
            while (true) { delay(2600); active = (active + 1) % products.size }
        }
    }
    BoxWithConstraints(modifier) {
    val narrowCard = maxWidth < 440.dp
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(24.dp, BrandShapes.CardLarge, ambientColor = BrandColors.Ink.copy(alpha = 0.18f), spotColor = BrandColors.Ink.copy(alpha = 0.18f))
            .clip(BrandShapes.CardLarge)
            .background(Color.White)
            .border(1.dp, BrandColors.Line, BrandShapes.CardLarge)
            .padding(24.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f)) {
                Text(stringResource("cp_suite_title"), style = type.h4, color = BrandColors.Ink)
                Text(stringResource("cp_suite_subtitle"), style = type.caption.copy(fontSize = 13.sp, lineHeight = 18.sp), color = BrandColors.Ink2)
            }
            BrandTag(stringResource("cp_suite_tag", products.size), tone = TagTone.Cyan)
        }
        products.forEachIndexed { index, product ->
            val on = index == active
            val bg by animateColorAsState(if (on) Color.White else BrandColors.Subtle, tween(500), label = "prowBg")
            val border by animateColorAsState(if (on) BrandColors.Cyan.copy(alpha = 0.35f) else Color.Transparent, tween(500), label = "prowBorder")
            val shift by animateDpAsState(if (on && !reduce) (-6).dp else 0.dp, tween(500, easing = BrandEasing), label = "prowShift")
            val service = content.serviceFor(product)
            Row(
                Modifier
                    .padding(top = 8.dp)
                    .offset(x = shift)
                    .fillMaxWidth()
                    .then(if (on) Modifier.shadow(8.dp, BrandShapes.Tile, ambientColor = BrandColors.Cyan, spotColor = BrandColors.Cyan) else Modifier)
                    .clip(BrandShapes.Tile)
                    .background(bg)
                    .border(1.dp, border, BrandShapes.Tile)
                    .clickable(role = Role.Button) { onProductClick(product.routeSlug) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val logo: @Composable () -> Unit = {
                    ProductLogo(product, onDark = false, modifier = Modifier.height(22.dp), textStyle = type.smallStrong)
                }
                val tag: @Composable () -> Unit = {
                    if (!product.flowLabel.isBlank) BrandTag(product.flowLabel.resolve(locale), tone = TagTone.Green)
                }
                val texts: @Composable (Modifier) -> Unit = { m ->
                    Column(m) {
                        Text(
                            service?.title?.resolve(locale) ?: product.categoryEyebrow,
                            style = type.small.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold),
                            color = BrandColors.Ink
                        )
                        Text(product.tagline.resolve(locale), style = type.caption, color = BrandColors.Ink2, modifier = Modifier.padding(top = 4.dp))
                    }
                }
                if (narrowCard) {
                    // ≤600px layout of the design: logo and tag on top, copy underneath.
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { logo(); tag() }
                        texts(Modifier.fillMaxWidth())
                    }
                } else {
                    Box(Modifier.width(96.dp)) { logo() }
                    texts(Modifier.weight(1f))
                    tag()
                }
            }
        }
    }
    }
}

// ------------------------------------------------------------------ previews

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 900)
@Composable
fun HeroSectionDesktopPreview() {
    BrandTheme(reduceMotion = true) {
        HeroSection(CompanyProfilePreviewData.content, AppLocale.ID, {}, {}, {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 900)
@Composable
fun HeroSectionEnglishPreview() {
    BrandTheme(reduceMotion = true) {
        HeroSection(CompanyProfilePreviewData.content, AppLocale.EN, {}, {}, {})
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390, heightDp = 1500)
@Composable
fun HeroSectionCompactPreview() {
    BrandTheme(reduceMotion = true) {
        HeroSection(CompanyProfilePreviewData.content, AppLocale.ID, {}, {}, {}, topInset = 64.dp)
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280, heightDp = 900)
@Composable
fun HeroSectionEmptyCatalogPreview() {
    BrandTheme(reduceMotion = true) {
        HeroSection(
            CompanyProfilePreviewData.content.copy(products = emptyList(), books = emptyList(), about = CompanyProfilePreviewData.content.about.copy(sectors = emptyList())),
            AppLocale.ID, {}, {}, {}
        )
    }
}
