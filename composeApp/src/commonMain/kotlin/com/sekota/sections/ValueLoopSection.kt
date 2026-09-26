package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.WindowWidth
import com.sekota.ui.windowWidthOf
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.sectionVerticalPadding

@Composable
fun ValueLoopSection(
    config: com.sekota.features.admin.domain.model.ValueLoopConfig? = null
) {
    var liveConfig by remember {
        mutableStateOf(config ?: com.sekota.features.admin.domain.model.ValueLoopConfig())
    }

    LaunchedEffect(config) {
        if (config != null) {
            liveConfig = config
        } else {
            liveConfig = com.sekota.getValueLoopUseCase()
            com.sekota.syncService.syncEventFlow.collect {
                liveConfig = com.sekota.getValueLoopUseCase()
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        ValueLoopContent(liveConfig, windowWidthOf(maxWidth), contentHorizontalPadding(maxWidth))
    }
}

@Composable
private fun ValueLoopContent(
    config: com.sekota.features.admin.domain.model.ValueLoopConfig,
    windowWidth: WindowWidth,
    horizontalPadding: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = windowWidth.sectionVerticalPadding,
                horizontal = horizontalPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = config.sectionEyebrow,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF14B8A6),
            fontFamily = getDmSansFontFamily(),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = config.title,
            fontSize = if (windowWidth.isCompact) 32.sp else 48.sp,
            lineHeight = if (windowWidth.isCompact) 40.sp else 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            fontFamily = getMontserratFontFamily(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = config.description,
            fontSize = 18.sp,
            color = Color(0xFF64748B),
            fontFamily = getDmSansFontFamily(),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 700.dp),
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(if (windowWidth.isCompact) 48.dp else 80.dp))

        when (windowWidth) {
            // A five-across rail needs roughly 170dp per card to stay legible.
            // Below Expanded it wraps instead of crushing each step.
            WindowWidth.Compact -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                config.steps.forEach { step ->
                    LoopStep(step.stepNumber, step.title, step.subtitle, modifier = Modifier.fillMaxWidth())
                }
            }

            WindowWidth.Medium -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                config.steps.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        pair.forEach { step ->
                            LoopStep(step.stepNumber, step.title, step.subtitle, modifier = Modifier.weight(1f).fillMaxHeight())
                        }
                        // Keeps the trailing odd card at half width instead of stretching it.
                        repeat(2 - pair.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }

            else -> Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                config.steps.forEachIndexed { index, step ->
                    if (index > 0) LoopDivider()
                    LoopStep(step.stepNumber, step.title, step.subtitle, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    }
}

@Composable
fun LoopStep(number: String, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF2DD4BF), Color(0xFF10B981))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = getDmSansFontFamily()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF0F172A),
                fontFamily = getDmSansFontFamily(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                fontFamily = getDmSansFontFamily(),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun LoopDivider() {
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(1.dp)
            .background(Color(0xFFE2E8F0))
    )
}

// -------------------------------------------------------------
// Mandatory Multi-State Compose Previews (Rule #4)
// -------------------------------------------------------------

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValueLoopSectionDefaultPreview() {
    MaterialTheme {
        ValueLoopSection(
            config = com.sekota.features.admin.domain.model.ValueLoopConfig()
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValueLoopSectionCustomPreview() {
    MaterialTheme {
        ValueLoopSection(
            config = com.sekota.features.admin.domain.model.ValueLoopConfig(
                sectionEyebrow = "KERANGKA KERJA",
                title = "Metodologi Intelijen Sekota",
                description = "Platform intelijen kota berbasis AI yang menghubungkan observasi lapangan dengan analitik prediktif.",
                steps = listOf(
                    com.sekota.features.admin.domain.model.AdminValueLoopStep("01", "INGESTION", "Koneksi Sensor IoT"),
                    com.sekota.features.admin.domain.model.AdminValueLoopStep("02", "NORMALIZATION", "Standardisasi Data"),
                    com.sekota.features.admin.domain.model.AdminValueLoopStep("03", "ANALYTICS", "Pemodelan Prediktif"),
                    com.sekota.features.admin.domain.model.AdminValueLoopStep("04", "DECISION", "Rekomendasi Kebijakan")
                )
            )
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun LoopStepDefaultPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Strategic Value Loop Step Preview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoopStep(
                    number = "01",
                    title = "INTENT",
                    subtitle = "Definisi Strategis",
                    modifier = Modifier.weight(1f)
                )
                LoopDivider()
                LoopStep(
                    number = "02",
                    title = "EXECUTION",
                    subtitle = "Implementasi Data",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


