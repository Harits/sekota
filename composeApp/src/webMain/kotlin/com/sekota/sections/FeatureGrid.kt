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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun FeatureGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp, horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MASALAH YANG KITA HADAPI",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF60BD65),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Data Melimpah,\nKeputusan Masih Kabur.",
            fontSize = 48.sp,
            lineHeight = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = getMontserratFontFamily(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Di era informasi ini, tantangan terbesar bukan lagi mencari data, melainkan bagaimana mengubah data menjadi instrumen strategis yang dapat diandalkan.",
            fontSize = 18.sp,
            color = Color.Gray,
            fontFamily = getDmSansFontFamily(),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 700.dp)
        )
        Spacer(modifier = Modifier.height(64.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            FeatureCard(
                title = "Data Tidak Terintegrasi",
                description = "Informasi tersebar di berbagai silo, membuat sulit untuk mendapatkan pandangan holistik yang diperlukan untuk strategi jangka panjang.",
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "AI Tanpa Transparansi",
                description = "Model AI yang sering kali 'kotak hitam' tanpa penjelasan logis yang memadai, menyulitkan audit dan validasi keputusan kritis.",
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "ESG Hanya Beban Administrasi",
                description = "Kepatuhan terhadap standar keberlanjutan sering dianggap sebagai beban pelaporan, bukan sebagai keunggulan kompetitif yang strategis.",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FeatureCard(title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Placeholder for Icon
            Box(modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)))
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily()
            )
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily(),
                lineHeight = 24.sp
            )
        }
    }
}
