package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun EBookPromo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 48.dp)
    ) {
        Text(
            text = "E-Book & Panduan Strategis.",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily()
        )
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            EBookCard("Cybersecurity Strategy", "Panduan lengkap menghadapi ancaman digital.", Color(0xFF1E88E5), modifier = Modifier.weight(1f))
            EBookCard("ESG Reporting 101", "Langkah taktis menyusun laporan keberlanjutan.", Color(0xFFD84315), modifier = Modifier.weight(1f))
            EBookCard("AI for Executive", "Mengoptimalkan AI dalam pengambilan keputusan.", Color(0xFF2E7D32), modifier = Modifier.weight(1f))
            EBookCard("Market Expansion", "Strategi ekspansi di pasar berkembang.", Color(0xFF673AB7), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun EBookCard(title: String, description: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "E-BOOK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = getMontserratFontFamily())
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f), fontFamily = getDmSansFontFamily())
            }
            Text(text = "LIHAT DETAIL →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
