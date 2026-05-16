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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*

@Composable
fun ValueLoopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 48.dp),
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
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            fontFamily = getMontserratFontFamily()
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
        Spacer(modifier = Modifier.height(80.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoopStep("01", "INTENT", "Definisi Strategis", modifier = Modifier.weight(1f))
            LoopDivider()
            LoopStep("02", "EXECUTION", "Implementasi Data", modifier = Modifier.weight(1f))
            LoopDivider()
            LoopStep("03", "VALUE", "Penciptaan Nilai", modifier = Modifier.weight(1f))
            LoopDivider()
            LoopStep("04", "MEASUREMENT", "Audit Dampak", modifier = Modifier.weight(1f))
            LoopDivider()
            LoopStep("05", "LEARNING", "Optimasi Berkelanjutan", modifier = Modifier.weight(1f))
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
