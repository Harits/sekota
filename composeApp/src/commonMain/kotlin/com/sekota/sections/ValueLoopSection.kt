package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private val LoopSteps = listOf(
    Triple("01", "INTENT", "Definisi Strategis"),
    Triple("02", "EXECUTION", "Implementasi Data"),
    Triple("03", "VALUE", "Penciptaan Nilai"),
    Triple("04", "MEASUREMENT", "Audit Dampak"),
    Triple("05", "LEARNING", "Optimasi Berkelanjutan")
)

@Composable
fun ValueLoopSection() {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        ValueLoopContent(windowWidthOf(maxWidth), contentHorizontalPadding(maxWidth))
    }
}

@Composable
private fun ValueLoopContent(windowWidth: WindowWidth, horizontalPadding: Dp) {
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
            text = "SOLUSI KAMI",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF14B8A6),
            fontFamily = getDmSansFontFamily(),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Strategic Value Loop",
            fontSize = if (windowWidth.isCompact) 32.sp else 48.sp,
            lineHeight = if (windowWidth.isCompact) 40.sp else 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            fontFamily = getMontserratFontFamily(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Setiap solusi Sekota dirancang dalam satu siklus tertutup yang memastikan data berubah menjadi keputusan strategis.",
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
                LoopSteps.forEach { (number, title, subtitle) ->
                    LoopStep(number, title, subtitle, modifier = Modifier.fillMaxWidth())
                }
            }

            WindowWidth.Medium -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LoopSteps.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        pair.forEach { (number, title, subtitle) ->
                            LoopStep(number, title, subtitle, modifier = Modifier.weight(1f).fillMaxHeight())
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
                LoopSteps.forEachIndexed { index, (number, title, subtitle) ->
                    if (index > 0) LoopDivider()
                    LoopStep(number, title, subtitle, modifier = Modifier.weight(1f).fillMaxHeight())
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

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ValueLoopSectionPreview() {
    ValueLoopSection()
}
