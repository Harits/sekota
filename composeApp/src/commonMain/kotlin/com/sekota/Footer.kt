package com.sekota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.ui.WindowWidth
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.container
import sekota.composeapp.generated.resources.logo_sekota

import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.FooterLinkItem
import com.sekota.features.admin.domain.model.FooterColumnConfig
import com.sekota.features.admin.domain.model.FooterSocialLink

@Composable
fun Footer(
    config: FooterConfig? = null,
    onNavigate: (Screen) -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onKontakClick: () -> Unit = {},
    selectedLanguage: String = "BAHASA INDONESIA",
    onLanguageChange: (String) -> Unit = {}
) {
    var liveConfig by remember { mutableStateOf(config ?: FooterConfig()) }

    LaunchedEffect(config) {
        if (config != null) {
            liveConfig = config
        } else {
            try {
                liveConfig = com.sekota.getFooterUseCase()
            } catch (_: Exception) {
                // AC 4: Fallback to default canonical config if network or repo fails
                liveConfig = FooterConfig()
            }
            try {
                com.sekota.syncService.syncEventFlow.collect {
                    try {
                        liveConfig = com.sekota.getFooterUseCase()
                    } catch (_: Exception) {}
                }
            } catch (_: Exception) {}
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        FooterContent(
            config = liveConfig,
            windowWidth = windowWidthOf(maxWidth),
            horizontalPadding = contentHorizontalPadding(maxWidth),
            onNavigate = onNavigate,
            onProductClick = onProductClick,
            onKontakClick = onKontakClick,
            selectedLanguage = selectedLanguage,
            onLanguageChange = onLanguageChange
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FooterContent(
    config: FooterConfig,
    windowWidth: WindowWidth,
    horizontalPadding: Dp,
    onNavigate: (Screen) -> Unit,
    onProductClick: (String) -> Unit,
    onKontakClick: () -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val isCompact = windowWidth.isCompact

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                horizontal = horizontalPadding,
                vertical = if (isCompact) 48.dp else 64.dp
            )
    ) {
        val isEn = selectedLanguage.equals("ENGLISH", ignoreCase = true)
        val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

        val localizedTagline = if (isEn) {
            "Trusted Intelligence for Sustainable Impact. Empowering strategic decisions through data."
        } else {
            config.tagline
        }

        val localizedAffiliation = if (isEn) "eBdesk Group Affiliation" else config.affiliationText
        val localizedAffiliationSubtext = if (isEn) "NATIONWIDE PARTNER" else config.affiliationSubtext
        val localizedCopyright = if (isEn) {
            "© 2025 PT. SEKOTA SINERGI INDONESIA. PART OF EBDESK GROUP."
        } else {
            config.copyrightText
        }

        // Column Titles
        val productTitle = if (isEn) "PRODUCTS" else config.productColumn.title
        val companyTitle = if (isEn) "COMPANY" else config.companyColumn.title
        val legalTitle = if (isEn) "LEGAL" else config.legalColumn.title

        // Column Items sorted and filtered by visibility
        val visibleProducts = config.productColumn.items.filter { it.isVisible }.sortedBy { it.sortOrder }
        val visibleCompany = config.companyColumn.items.filter { it.isVisible }.sortedBy { it.sortOrder }
        val visibleLegal = config.legalColumn.items.filter { it.isVisible }.sortedBy { it.sortOrder }
        val visibleSocials = config.socialLinks.filter { it.isVisible }

        fun resolveItemLabel(rawLabel: String): String {
            if (!isEn) return rawLabel
            return when (rawLabel.trim().lowercase()) {
                "tentang kami" -> "About Us"
                "kontak" -> "Contact"
                "privasi" -> "Privacy Policy"
                "syarat & ketentuan" -> "Terms & Conditions"
                else -> rawLabel
            }
        }

        val branding = @Composable { modifier: Modifier ->
            Column(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable { onNavigate(Screen.Landing) }
                ) {
                    FooterLogo()
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = localizedTagline,
                    fontSize = 14.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color(0xFF64748B),
                    lineHeight = 22.sp,
                    modifier = Modifier.widthIn(max = 320.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.container),
                        contentDescription = localizedAffiliation,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = localizedAffiliation,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5A6972),
                            fontFamily = getDmSansFontFamily()
                        )
                        Text(
                            text = localizedAffiliationSubtext,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8),
                            fontFamily = getDmSansFontFamily(),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                if (visibleSocials.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        visibleSocials.forEach { social ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable {
                                        if (social.url.isNotBlank()) {
                                            try { uriHandler.openUri(social.url) } catch (_: Exception) {}
                                        }
                                    }
                            ) {
                                com.sekota.components.SekotaSocialVectorIcon(
                                    platform = social.platform,
                                    size = 14.dp,
                                    tint = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = social.platform.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF94A3B8),
                                    fontFamily = getDmSansFontFamily()
                                )
                            }
                        }
                    }
                }
            }
        }

        // Responsive layout: 4-column grid on desktop, wrapped FlowRow on tablet/mobile
        if (windowWidth.isAtMostMedium) {
            branding(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(40.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Column 2: Produk
                FooterLinkColumn(
                    title = productTitle,
                    items = visibleProducts,
                    onItemClick = { item ->
                        val code = when (item.urlOrCode.uppercase()) {
                            "VRD", "VERIDIA" -> "VRD"
                            "ASC", "ASCENDIO" -> "ASC"
                            "SOC", "SOCIARA" -> "SOC"
                            "ECO", "ECOFLOW" -> "ECO"
                            else -> item.urlOrCode.ifBlank { "VRD" }
                        }
                        onProductClick(code)
                    },
                    labelTransformer = ::resolveItemLabel
                )

                // Column 3: Perusahaan
                FooterLinkColumn(
                    title = companyTitle,
                    items = visibleCompany,
                    onItemClick = { item ->
                        when (item.urlOrCode.uppercase()) {
                            "EBOOK", "CATALOG" -> onNavigate(Screen.Catalog)
                            "MERCH", "MERCHANDISE" -> onNavigate(Screen.Merchandise)
                            "CONTACT", "KONTAK" -> onKontakClick()
                            else -> {
                                if (item.urlOrCode.startsWith("http://") || item.urlOrCode.startsWith("https://")) {
                                    try { uriHandler.openUri(item.urlOrCode) } catch (_: Exception) {}
                                } else {
                                    onNavigate(Screen.Landing)
                                }
                            }
                        }
                    },
                    labelTransformer = ::resolveItemLabel
                )

                // Column 4: Legal
                FooterLinkColumn(
                    title = legalTitle,
                    items = visibleLegal,
                    onItemClick = { item ->
                        if (item.urlOrCode.startsWith("http://") || item.urlOrCode.startsWith("https://")) {
                            try { uriHandler.openUri(item.urlOrCode) } catch (_: Exception) {}
                        } else {
                            onNavigate(Screen.Landing)
                        }
                    },
                    labelTransformer = ::resolveItemLabel
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left Column: Brand & Tagline & Affiliation & Social
                branding(Modifier.weight(1.4f).padding(end = 48.dp))

                // Right Section: 3 Link Columns spread to the right edge
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Column 2: PRODUK
                    FooterLinkColumn(
                        title = productTitle,
                        items = visibleProducts,
                        onItemClick = { item ->
                            val code = when (item.urlOrCode.uppercase()) {
                                "VRD", "VERIDIA" -> "VRD"
                                "ASC", "ASCENDIO" -> "ASC"
                                "SOC", "SOCIARA" -> "SOC"
                                "ECO", "ECOFLOW" -> "ECO"
                                else -> item.urlOrCode.ifBlank { "VRD" }
                            }
                            onProductClick(code)
                        },
                        labelTransformer = ::resolveItemLabel
                    )

                    // Column 3: PERUSAHAAN
                    FooterLinkColumn(
                        title = companyTitle,
                        items = visibleCompany,
                        onItemClick = { item ->
                            when (item.urlOrCode.uppercase()) {
                                "EBOOK", "CATALOG" -> onNavigate(Screen.Catalog)
                                "MERCH", "MERCHANDISE" -> onNavigate(Screen.Merchandise)
                                "CONTACT", "KONTAK" -> onKontakClick()
                                else -> {
                                    if (item.urlOrCode.startsWith("http://") || item.urlOrCode.startsWith("https://")) {
                                        try { uriHandler.openUri(item.urlOrCode) } catch (_: Exception) {}
                                    } else {
                                        onNavigate(Screen.Landing)
                                    }
                                }
                            }
                        },
                        labelTransformer = ::resolveItemLabel
                    )

                    // Column 4: LEGAL (Flush with right edge)
                    FooterLinkColumn(
                        title = legalTitle,
                        items = visibleLegal,
                        onItemClick = { item ->
                            if (item.urlOrCode.startsWith("http://") || item.urlOrCode.startsWith("https://")) {
                                try { uriHandler.openUri(item.urlOrCode) } catch (_: Exception) {}
                            } else {
                                onNavigate(Screen.Landing)
                            }
                        },
                        labelTransformer = ::resolveItemLabel
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 40.dp else 64.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(if (isCompact) 24.dp else 32.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = localizedCopyright,
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "ENGLISH",
                    fontSize = 12.sp,
                    fontWeight = if (isEn) FontWeight.Bold else FontWeight.Normal,
                    color = if (isEn) Color(0xFF0D1F2D) else Color(0xFF94A3B8),
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onLanguageChange("ENGLISH") }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "BAHASA INDONESIA",
                    fontSize = 12.sp,
                    fontWeight = if (!isEn) FontWeight.Bold else FontWeight.Normal,
                    color = if (!isEn) Color(0xFF0D1F2D) else Color(0xFF94A3B8),
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onLanguageChange("BAHASA INDONESIA") }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FooterLinkColumn(
    title: String,
    items: List<FooterLinkItem>,
    onItemClick: (FooterLinkItem) -> Unit = {},
    labelTransformer: (String) -> String = { it }
) {
    Column(modifier = Modifier.widthIn(min = 140.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1F2D),
            fontFamily = getDmSansFontFamily(),
            modifier = Modifier.padding(bottom = 24.dp),
            letterSpacing = 0.5.sp
        )
        items.forEach { item ->
            Text(
                text = labelTransformer(item.label),
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                modifier = Modifier
                    .padding(bottom = 18.dp)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable { onItemClick(item) },
                fontFamily = getDmSansFontFamily()
            )
        }
    }
}

@Composable
fun FooterColumn(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit = {}
) {
    FooterLinkColumn(
        title = title,
        items = items.mapIndexed { idx, it -> FooterLinkItem(id = "item_$idx", label = it, urlOrCode = it, sortOrder = idx) },
        onItemClick = { onItemClick(it.label) }
    )
}

@Composable
fun FooterLogo() {
    SekotaBrandLogo(
        iconSize = 32.dp,
        textSize = 22.sp
    )
}

@Preview(showBackground = true)
@Composable
fun FooterColumnPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FooterColumn(
                title = "PRODUK",
                items = listOf("VERIDIA", "ASCENDIO", "SOCIARA", "ECOFLOW")
            )
            FooterColumn(
                title = "PERUSAHAAN",
                items = listOf("Tentang Kami", "E-Book", "Merchandise", "Insights", "Kontak")
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun FooterDefaultPreview() {
    MaterialTheme {
        Footer(selectedLanguage = "BAHASA INDONESIA")
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun FooterEnglishActivePreview() {
    MaterialTheme {
        Footer(selectedLanguage = "ENGLISH")
    }
}

@Preview(showBackground = true)
@Composable
fun FooterCompactPreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(375.dp)) {
            Footer(selectedLanguage = "BAHASA INDONESIA")
        }
    }
}


