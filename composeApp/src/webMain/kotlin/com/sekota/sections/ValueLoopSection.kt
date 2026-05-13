package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            text = "PROSES KAMI",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF60BD65),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Strategic Value Loop",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Kami mengintegrasikan data, AI, dan strategi dalam satu siklus yang berkelanjutan untuk menciptakan nilai nyata bagi organisasi Anda.",
            fontSize = 18.sp,
            color = Color.Gray,
            fontFamily = getDmSansFontFamily(),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 800.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoopStep("1", "PETA", "Navigasi Data")
            LoopDivider()
            LoopStep("2", "IDENTITAS", "Validasi Aset")
            LoopDivider()
            LoopStep("3", "ALUR", "Orkestrasi AI")
            LoopDivider()
            LoopStep("4", "VALIDASI", "Audit Strategis")
            LoopDivider()
            LoopStep("5", "LAPOR", "Impak Terukur")
        }
    }
}

@Composable
fun LoopStep(number: String, title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF60BD65).copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60BD65))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = getDmSansFontFamily())
        Text(text = subtitle, fontSize = 14.sp, color = Color.Gray, fontFamily = getDmSansFontFamily())
    }
}

@Composable
fun LoopDivider() {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(2.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    )
}
