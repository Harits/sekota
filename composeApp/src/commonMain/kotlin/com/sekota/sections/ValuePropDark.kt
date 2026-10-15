package com.sekota.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import com.sekota.features.admin.domain.model.WhySekotaCard
import com.sekota.features.admin.domain.model.WhySekotaConfig
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.cardPadding
import com.sekota.ui.windowWidthOf
import com.sekota.ui.sectionVerticalPadding
import com.sekota.localization.stringResource
import com.sekota.localization.LocalizationManager
import com.sekota.localization.LocalLocalization
import com.sekota.localization.AppLocale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun ValuePropDark(
    config: WhySekotaConfig? = null,
    onConsultationClick: () -> Unit = {}
) {
    var liveConfig by remember {
        mutableStateOf(config ?: WhySekotaConfig())
    }

    LaunchedEffect(config) {
        if (config != null) {
            liveConfig = config
        } else {
            liveConfig = com.sekota.getWhySekotaUseCase()
            com.sekota.syncService.syncEventFlow.collect {
                liveConfig = com.sekota.getWhySekotaUseCase()
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        ValuePropDarkContent(liveConfig, windowWidthOf(maxWidth), contentHorizontalPadding(maxWidth), onConsultationClick)
    }
}

private fun resolveCardIcon(iconName: String): DrawableResource {
    return when (iconName.lowercase().trim()) {
        "trending", "icon_3" -> Res.drawable.icon_3
        "chip", "icon_4" -> Res.drawable.icon_4
        "shield" -> Res.drawable.shield
        "sparkle", "icon_6" -> Res.drawable.icon_6
        else -> Res.drawable.icon_3
    }
}

@Composable
private fun ValuePropDarkContent(
    config: WhySekotaConfig,
    windowWidth: WindowWidth,
    horizontalPadding: Dp,
    onConsultationClick: () -> Unit
) {
    // The copy column and the proof cards only fit side by side from Expanded;
    // narrower than that the pair stacks beneath the headline.
    val stacked = windowWidth.isAtMostMedium

    val isCanonicalConfig = config.preTitle.equals("MENGAPA SEKOTA", ignoreCase = true) ||
        config.preTitle.equals("WHY SEKOTA", ignoreCase = true) ||
        config.title.contains("Bukan Sekadar", ignoreCase = true) ||
        config.title.contains("Decision Architecture", ignoreCase = true)

    val displayPreTitle = if (isCanonicalConfig) stringResource("why_sekota_eyebrow") else config.preTitle
    val displayTitle = if (isCanonicalConfig) stringResource("why_sekota_title") else config.title
    val displayDesc = if (isCanonicalConfig) stringResource("why_sekota_desc") else config.description
    val displayCtaLabel = if (isCanonicalConfig) stringResource("why_sekota_cta") else config.ctaLabel

    val displayCards = remember(config.cards, isCanonicalConfig) {
        if (isCanonicalConfig) {
            config.cards.mapIndexed { idx, card ->
                val cardIdx = idx + 1
                card.copy(
                    title = LocalizationManager.getString("why_sekota_card_${cardIdx}_title"),
                    description = LocalizationManager.getString("why_sekota_card_${cardIdx}_desc")
                )
            }
        } else {
            config.cards
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B141B))
            .padding(
                vertical = windowWidth.sectionVerticalPadding,
                horizontal = horizontalPadding
            )
    ) {
        val copy = @Composable { modifier: Modifier ->
            Column(modifier = modifier) {
                Text(
                    text = displayPreTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = displayTitle,
                    fontSize = if (windowWidth.isCompact) 34.sp else 52.sp,
                    lineHeight = if (windowWidth.isCompact) 42.sp else 60.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = getMontserratFontFamily()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = displayDesc,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.widthIn(max = 500.dp)
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onConsultationClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier
                ) {
                    Box(
                        modifier = Modifier
                            .then(if (windowWidth.isCompact) Modifier.fillMaxWidth() else Modifier)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayCtaLabel,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        val cards = @Composable { modifier: Modifier ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                displayCards.forEach { card ->
                    ValuePropCard(
                        title = card.title,
                        description = card.description,
                        icon = resolveCardIcon(card.iconName),
                        contentPadding = windowWidth.cardPadding
                    )
                }
            }
        }

        if (stacked) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                copy(Modifier.fillMaxWidth())
                cards(Modifier.fillMaxWidth())
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (windowWidth == WindowWidth.Large) 80.dp else 48.dp)
            ) {
                copy(Modifier.weight(1.2f))
                cards(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ValuePropCard(
    title: String,
    description: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF111E26), shape = RoundedCornerShape(24.dp))
            .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(top = 4.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = description,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValuePropDarkPreview() {
    MaterialTheme {
        ValuePropDark()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValuePropDarkEnglishPreview() {
    val enManager = remember {
        LocalizationManager.apply { setLocale(AppLocale.EN) }
    }
    androidx.compose.runtime.CompositionLocalProvider(
        LocalLocalization provides enManager
    ) {
        MaterialTheme {
            ValuePropDark()
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValuePropDarkDynamicPreview() {
    MaterialTheme {
        val dynamicConfig = WhySekotaConfig(
            preTitle = "NILAI UNGGULAN",
            title = "Transformasi Cerdas Berbasis Ekosistem Nyata",
            description = "Menjembatani keunggulan riset strategis dan implementasi arsitektur digital mutakhir.",
            ctaLabel = "JADWALKAN DISKUSI",
            cards = listOf(
                WhySekotaCard(
                    id = "p1",
                    title = "Lebih Strategis dari Vendor IT",
                    description = "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.",
                    iconName = "trending"
                ),
                WhySekotaCard(
                    id = "p2",
                    title = "Lebih Teknis dari Konsultan",
                    description = "Implementasi data real-time, bukan sekadar slide deck rekomendasi.",
                    iconName = "chip"
                ),
                WhySekotaCard(
                    id = "p3",
                    title = "Keamanan & Tata Kelola Teruji",
                    description = "Enkripsi terdesentralisasi dengan integritas data berstandar institusi tinggi.",
                    iconName = "shield"
                )
            )
        )
        ValuePropDark(config = dynamicConfig)
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValuePropDarkCompactPreview() {
    MaterialTheme {
        Box(modifier = Modifier.width(360.dp)) {
            ValuePropDark()
        }
    }
}

@Preview(device = DESKTOP, showBackground = true, backgroundColor = 0xFF0B141B)
@Composable
fun ValuePropCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFF0B141B))
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "ValuePropCard Preview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily(),
                color = Color.White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ValuePropCard(
                    title = "Lebih Strategis dari Vendor IT",
                    description = "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.",
                    icon = Res.drawable.icon_3,
                    modifier = Modifier.weight(1f)
                )
                ValuePropCard(
                    title = "Lebih Teknis dari Konsultan",
                    description = "Implementasi data real-time, bukan sekadar slide deck rekomendasi.",
                    icon = Res.drawable.icon_4,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
