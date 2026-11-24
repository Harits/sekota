package com.sekota.ui.brand

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sekota.localization.AppLocale
import org.jetbrains.compose.resources.painterResource

// Design system previews (WEB-07-01-04 / #22663)

@Composable
private fun PreviewSurface(background: Color = BrandColors.Background, content: @Composable ColumnScope.() -> Unit) {
    BrandTheme(reduceMotion = true) {
        Column(
            modifier = Modifier.fillMaxWidth().background(background).padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = content
        )
    }
}

@Composable
private fun Swatch(name: String, color: Color? = null, brush: Brush? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier.size(72.dp, 48.dp).clip(BrandShapes.Tile)
                .then(if (brush != null) Modifier.background(brush) else Modifier.background(color ?: Color.Transparent))
                .border(1.dp, BrandColors.Line, BrandShapes.Tile)
        )
        Text(name, style = brandType().caption, color = BrandColors.Ink2)
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandColorTokensPreview() {
    PreviewSurface {
        Text("Color tokens", style = brandType().h3, color = BrandColors.Ink)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Swatch("ink", BrandColors.Ink); Swatch("ink-2", BrandColors.Ink2); Swatch("muted", BrandColors.Muted)
            Swatch("brand-ink", BrandColors.BrandInk); Swatch("subtle", BrandColors.Subtle); Swatch("muted-bg", BrandColors.MutedBg)
            Swatch("line", BrandColors.Line); Swatch("dark", BrandColors.Dark); Swatch("on-dark", BrandColors.OnDarkMuted)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Swatch("g1", BrandColors.Green); Swatch("g2", BrandColors.Cyan)
            Swatch("grad", brush = BrandBrushes.Primary); Swatch("dark band", brush = BrandBrushes.DarkBand)
            Swatch("partner", brush = BrandBrushes.PartnerCta); Swatch("tile", brush = BrandBrushes.Tile)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandTypographyPreview() {
    PreviewSurface {
        val t = brandType()
        Overline("Overline · IBM Plex Mono")
        Text("Data jadi keputusan.", style = t.display, color = BrandColors.Ink)
        GradientText("Keputusan jadi dampak.", style = t.display)
        Text("Empat layanan, satu tujuan.", style = t.h2, color = BrandColors.Ink)
        Text("Analisis Strategis & Risiko", style = t.h3, color = BrandColors.Ink)
        Text("Lead — Sekota adalah mitra analisis yang membantu organisasi membaca risiko.", style = t.lead, color = BrandColors.Ink2)
        Text("Body — Plus Jakarta Sans 16/26.", style = t.body, color = BrandColors.Ink)
        Text("sinyal → keputusan", style = t.mono, color = BrandColors.BrandInk)
    }
}

@Preview(device = DESKTOP, showBackground = true, widthDp = 390)
@Composable
fun BrandTypographyCompactPreview() {
    PreviewSurface {
        val t = brandType(compact = true)
        Text("Data jadi keputusan.", style = t.display, color = BrandColors.Ink)
        Text("Empat layanan, satu tujuan.", style = t.h2, color = BrandColors.Ink)
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandControlsPreview() {
    PreviewSurface {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            BrandButton("Lihat Layanan", onClick = {}, trailingIcon = BrandIcons.ArrowRight)
            BrandButton("Hubungi Kami", onClick = {}, variant = BrandButtonVariant.Secondary)
            BrandButton("Hubungi Kami", onClick = {}, small = true)
            LanguageToggle(selected = AppLocale.ID, onSelect = {})
            LanguageToggle(selected = AppLocale.EN, onSelect = {})
        }
        Box(Modifier.clip(BrandShapes.Banner).background(BrandBrushes.PartnerCta).padding(24.dp)) {
            BrandButton("Hubungi Kami", onClick = {}, variant = BrandButtonVariant.OnGradient, trailingIcon = BrandIcons.ArrowRight)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            BrandChip("Energi, Migas & Pertambangan")
            BrandTag("4 platform AI", tone = TagTone.Cyan)
            BrandTag("Sinyal → keputusan", tone = TagTone.Green)
            BrandTag("Netral")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CheckItem("Pemantauan isu"); CheckItem("Peringatan dini"); CheckItem("Paparan untuk pimpinan")
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandIconsPreview() {
    PreviewSurface {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(BrandIcons.Radar, BrandIcons.Users, BrandIcons.Sprout, BrandIcons.Leaf).forEach { IconTile(it) }
            listOf(BrandIcons.Search, BrandIcons.Layers, BrandIcons.Rocket, BrandIcons.Refresh).forEach { IconTile(it) }
            IconTile(BrandIcons.BookOpen); IconTile(BrandIcons.ShieldCheck); IconTile(BrandIcons.TrendingUp, tilted = true)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(BrandIcons.Check, BrandIcons.ArrowRight, BrandIcons.ArrowUpRight, BrandIcons.Plus, BrandIcons.Menu, BrandIcons.Close, BrandIcons.Mail)
                .forEach { Icon(it, contentDescription = null, tint = BrandColors.Ink, modifier = Modifier.size(24.dp)) }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandSectionHeaderPreview() {
    PreviewSurface {
        SectionHeader("Layanan", "Empat layanan, satu tujuan: keputusan yang lebih tepat.")
        Box(Modifier.fillMaxWidth().clip(BrandShapes.CardLarge).background(BrandBrushes.DarkBand).padding(32.dp)) {
            SectionHeader(
                "Platform", "Didukung platform AI milik Sekota.",
                lead = "Layanan Sekota punya alatnya sendiri — dibangun di atas teknologi eBdesk.", onDark = true
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandFaqPreview() {
    PreviewSurface(background = BrandColors.Subtle) {
        var open by remember { mutableStateOf(0) }
        listOf(
            "Apa hubungan Sekota dan eBdesk?" to "Sekota adalah unit bisnis eBdesk Group.",
            "Bagaimana data organisasi kami dijaga?" to "Layanan Sekota berjalan di atas infrastruktur eBdesk yang tersertifikasi ISO 27001."
        ).forEachIndexed { i, (q, a) ->
            FaqItem(q, a, expanded = open == i, onToggle = { open = if (open == i) -1 else i })
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BrandAssetsPreview() {
    PreviewSurface {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(BrandAssets.SekotaLogoLight), contentDescription = "Sekota", modifier = Modifier.height(36.dp))
            listOf("clarivio", "ascendio", "sociara", "evidara").forEach { key ->
                BrandAssets.drawable("${key}_light")?.let { Image(painterResource(it), contentDescription = key, modifier = Modifier.height(22.dp)) }
            }
        }
        Row(
            Modifier.fillMaxWidth().background(BrandColors.Dark).padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(BrandAssets.SekotaLogoDark), contentDescription = "Sekota", modifier = Modifier.height(36.dp))
            listOf("clarivio", "ascendio", "sociara", "evidara").forEach { key ->
                BrandAssets.drawable("${key}_dark")?.let { Image(painterResource(it), contentDescription = key, modifier = Modifier.height(32.dp)) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            (1..6).forEach { i ->
                BrandAssets.drawable("cover$i")?.let {
                    Image(painterResource(it), contentDescription = null, modifier = Modifier.width(90.dp).aspectRatio(180f / 248f).clip(BrandShapes.Cover))
                }
            }
        }
    }
}
