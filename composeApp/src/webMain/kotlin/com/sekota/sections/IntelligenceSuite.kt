package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun IntelligenceSuite() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "LAYANAN UTAMA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60BD65),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Intelligence Suite.",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily()
                )
            }
            Text(
                text = "Pilih instrumen yang tepat untuk orkestrasi bisnis Anda.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(64.dp))

        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                SuiteCard("VERIDIA", "Sustainability & ESG Audit", modifier = Modifier.weight(1f))
                SuiteCard("ASCENDIO", "Market Intelligence & Growth", modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                SuiteCard("SOCIARA", "Social Impact & Community", modifier = Modifier.weight(1f))
                SuiteCard("ECOFLOW", "Supply Chain & Ops Efficiency", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SuiteCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(350.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = getMontserratFontFamily())
                    // Placeholder for Icon
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = subtitle, fontSize = 16.sp, color = Color(0xFF60BD65), fontWeight = FontWeight.Bold, fontFamily = getDmSansFontFamily())
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Deskripsi singkat mengenai bagaimana $title dapat membantu organisasi dalam mencapai tujuan strategis dan operasional yang lebih baik.",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    lineHeight = 22.sp
                )
            }
            
            Text(
                text = "PELAJARI SELENGKAPNYA →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = getDmSansFontFamily()
            )
        }
    }
}
