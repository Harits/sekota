package com.sekota

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sekota.features.admin.domain.model.FooterColumnConfig
import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.localization.AppLocale
import com.sekota.localization.LocalizationManager
import com.sekota.localization.stringResource
import com.sekota.ui.brand.*
import org.jetbrains.compose.resources.painterResource

/**
 * Global footer of the company profile (WEB-07-02-03 / #22666): brand column,
 * Navigasi / Platform / Kontak columns, copyright and legal links. Every link is a
 * route or CMS code resolved by NavigationRouter through [onRoute].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Footer(
    config: FooterConfig,
    locale: AppLocale,
    contactEmail: String,
    onRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val type = brandType()
    BoxWithConstraints(modifier.fillMaxWidth().background(BrandColors.Dark)) {
        val stacked = BrandLayout.isNarrow(maxWidth)
        BrandContainer(Modifier.padding(top = if (stacked) 64.dp else 80.dp, bottom = 40.dp)) {
            val brand: @Composable (Modifier) -> Unit = { m ->
                Column(m, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Image(
                        painterResource(BrandAssets.SekotaLogoDark), contentDescription = "Sekota Sinergi Indonesia",
                        modifier = Modifier.height(36.dp).clickable(role = Role.Button) { onRoute("/") }.pointerHoverIcon(PointerIcon.Hand)
                    )
                    val tagline = if (locale == AppLocale.EN) config.taglineEn?.takeIf { it.isNotBlank() } ?: config.tagline else config.tagline
                    Text(tagline, style = type.small, color = BrandColors.OnDarkMuted, modifier = Modifier.widthIn(max = 360.dp))
                    val socials = config.socialLinks.filter { it.isVisible && it.url.isNotBlank() }
                    if (socials.isNotEmpty()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            socials.forEach { FooterLink(it.platform.lowercase().replaceFirstChar { c -> c.uppercase() }) { onRoute(it.url) } }
                        }
                    }
                }
            }
            val columns: @Composable (Modifier) -> Unit = { m ->
                FlowRow(m, horizontalArrangement = Arrangement.spacedBy(if (stacked) 40.dp else 64.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    FooterColumn(config.companyColumn, locale, onRoute)
                    FooterColumn(config.productColumn, locale, onRoute)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        FooterHeading(stringResource("cp_footer_contact"))
                        FooterLink(stringResource("cp_cta_contact")) { onRoute("/kontak") }
                        if (contactEmail.isNotBlank()) FooterLink(contactEmail) { onRoute("mailto:$contactEmail") }
                    }
                }
            }
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(40.dp)) { brand(Modifier); columns(Modifier) }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) { brand(Modifier.weight(1f)); columns(Modifier) }
            }
            HorizontalDivider(Modifier.padding(top = 56.dp, bottom = 24.dp), color = Color.White.copy(alpha = 0.1f))
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val copyright = if (locale == AppLocale.EN) config.copyrightTextEn?.takeIf { it.isNotBlank() } ?: config.copyrightText else config.copyrightText
                Text(copyright, style = type.caption.copy(fontSize = androidx.compose.ui.unit.TextUnit(13f, androidx.compose.ui.unit.TextUnitType.Sp)), color = BrandColors.OnDarkMuted)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    config.legalColumn.items.filter { it.isVisible }.sortedBy { it.sortOrder }.forEach { item ->
                        FooterLink(if (locale == AppLocale.EN) item.labelEn ?: item.label else item.label) { onRoute(item.urlOrCode) }
                    }
                }
            }
        }
    }
}

/**
 * Footer for pages without company profile state (the Sprint 6 pages kept until
 * Sprint 8). Loads its own content and follows the active language.
 */
@Composable
fun Footer(onRoute: (String) -> Unit, modifier: Modifier = Modifier) {
    val locale by LocalizationManager.currentLocale.collectAsState()
    var config by remember { mutableStateOf(FooterConfig()) }
    var email by remember { mutableStateOf(StrategicConversationConfig().email) }
    LaunchedEffect(Unit) {
        runCatching {
            config = defaultAppContainer.getFooterUseCase()
            email = defaultAppContainer.getStrategicConversationUseCase().email
        }.onFailure { println("Footer: using defaults, load failed: ${it.message}") }
    }
    BrandTheme { Footer(config, locale, email, onRoute, modifier) }
}

@Composable
private fun FooterHeading(text: String) {
    Text(text, style = brandType().smallStrong.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold), color = Color.White)
}

@Composable
private fun FooterColumn(column: FooterColumnConfig, locale: AppLocale, onRoute: (String) -> Unit) {
    val items = column.items.filter { it.isVisible }.sortedBy { it.sortOrder }
    if (items.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FooterHeading(if (locale == AppLocale.EN) column.titleEn ?: column.title else column.title)
        items.forEach { item ->
            FooterLink(if (locale == AppLocale.EN) item.labelEn ?: item.label else item.label) { onRoute(item.urlOrCode) }
        }
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val color by animateColorAsState(if (hovered) Color.White else BrandColors.OnDarkMuted, tween(250), label = "footerLink")
    Text(
        text, style = brandType().small, color = color,
        modifier = Modifier
            .hoverable(source)
            .clickable(interactionSource = source, indication = null, role = Role.Button, onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
    )
}

// =========================================================================
// DESKTOP PREVIEWS
// =========================================================================

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun FooterDefaultPreview() {
    BrandTheme(reduceMotion = true) { Footer(FooterConfig(), AppLocale.ID, "info@sekota.id", onRoute = {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun FooterEnglishPreview() {
    BrandTheme(reduceMotion = true) { Footer(FooterConfig(), AppLocale.EN, "info@sekota.id", onRoute = {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun FooterCompactPreview() {
    BrandTheme(reduceMotion = true) { Footer(FooterConfig(), AppLocale.ID, "info@sekota.id", onRoute = {}) }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 1280)
@Composable
fun FooterMinimalConfigPreview() {
    BrandTheme(reduceMotion = true) {
        Footer(
            FooterConfig(productColumn = FooterColumnConfig("Platform"), socialLinks = emptyList()),
            AppLocale.ID, contactEmail = "", onRoute = {}
        )
    }
}
