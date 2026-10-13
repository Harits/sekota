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

@Composable
fun Footer(
    onNavigate: (Screen) -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onKontakClick: () -> Unit = {},
    selectedLanguage: String = "BAHASA INDONESIA",
    onLanguageChange: (String) -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        FooterContent(
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
                    text = "Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.",
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
                        contentDescription = "eBdesk Group Affiliation",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "eBdesk Group Affiliation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5A6972),
                            fontFamily = getDmSansFontFamily()
                        )
                        Text(
                            text = "NATIONWIDE PARTNER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8),
                            fontFamily = getDmSansFontFamily(),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "LINKEDIN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        fontFamily = getDmSansFontFamily(),
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable { /* Social Link Placeholder */ }
                    )
                    Text(
                        text = "TWITTER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        fontFamily = getDmSansFontFamily(),
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable { /* Social Link Placeholder */ }
                    )
                }
            }
        }

        // Responsive layout matching design: 4-column grid on desktop, wrapped FlowRow on tablet/mobile
        if (windowWidth.isAtMostMedium) {
            branding(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(40.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Column 2: Produk
                FooterColumn(
                    title = "PRODUK",
                    items = listOf("VERIDIA", "ASCENDIO", "SOCIARA", "ECOFLOW"),
                    onItemClick = { item ->
                        val code = when (item) {
                            "VERIDIA" -> "VRD"
                            "ASCENDIO" -> "ASC"
                            "SOCIARA" -> "SOC"
                            "ECOFLOW" -> "ECO"
                            else -> "VRD"
                        }
                        onProductClick(code)
                    }
                )

                // Column 3: Perusahaan
                FooterColumn(
                    title = "PERUSAHAAN",
                    items = listOf("Tentang Kami", "E-Book", "Merchandise", "Insights", "Kontak"),
                    onItemClick = { item ->
                        when (item) {
                            "E-Book" -> onNavigate(Screen.Catalog)
                            "Merchandise" -> onNavigate(Screen.Merchandise)
                            "Kontak" -> onKontakClick()
                            else -> onNavigate(Screen.Landing)
                        }
                    }
                )

                // Column 4: Legal
                FooterColumn(
                    title = "LEGAL",
                    items = listOf("Privasi", "Syarat & Ketentuan"),
                    onItemClick = {
                        onNavigate(Screen.Landing)
                    }
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Column 1: Brand & Affiliation (aligned to left)
                branding(Modifier.weight(1.3f).padding(end = 32.dp))

                // Column 2: Produk
                Box(modifier = Modifier.weight(0.9f)) {
                    FooterColumn(
                        title = "PRODUK",
                        items = listOf("VERIDIA", "ASCENDIO", "SOCIARA", "ECOFLOW"),
                        onItemClick = { item ->
                            val code = when (item) {
                                "VERIDIA" -> "VRD"
                                "ASCENDIO" -> "ASC"
                                "SOCIARA" -> "SOC"
                                "ECOFLOW" -> "ECO"
                                else -> "VRD"
                            }
                            onProductClick(code)
                        }
                    )
                }

                // Column 3: Perusahaan
                Box(modifier = Modifier.weight(0.9f)) {
                    FooterColumn(
                        title = "PERUSAHAAN",
                        items = listOf("Tentang Kami", "E-Book", "Merchandise", "Insights", "Kontak"),
                        onItemClick = { item ->
                            when (item) {
                                "E-Book" -> onNavigate(Screen.Catalog)
                                "Merchandise" -> onNavigate(Screen.Merchandise)
                                "Kontak" -> onKontakClick()
                                else -> onNavigate(Screen.Landing)
                            }
                        }
                    )
                }

                // Column 4: Legal (aligned to right edge)
                Box(modifier = Modifier.weight(0.9f), contentAlignment = Alignment.TopEnd) {
                    FooterColumn(
                        title = "LEGAL",
                        items = listOf("Privasi", "Syarat & Ketentuan"),
                        onItemClick = {
                            onNavigate(Screen.Landing)
                        }
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
                text = "© 2025 PT. SEKOTA SINERGI INDONESIA. BAGIAN DARI EBDESK GROUP.",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                val isEn = selectedLanguage.equals("ENGLISH", ignoreCase = true)
                val isId = selectedLanguage.equals("BAHASA INDONESIA", ignoreCase = true) || !isEn

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
                    fontWeight = if (isId) FontWeight.Bold else FontWeight.Normal,
                    color = if (isId) Color(0xFF0D1F2D) else Color(0xFF94A3B8),
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
fun FooterColumn(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit = {}
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
                text = item,
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


