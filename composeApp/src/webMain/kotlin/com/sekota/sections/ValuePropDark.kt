package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun ValuePropDark() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(vertical = 120.dp, horizontal = 48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Text(
                    text = "MENGAPA SEKOTA?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60BD65),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Sekota Bukan\nSekadar Vendor,\nBukan Sekadar\nKonsultan.",
                    fontSize = 56.sp,
                    lineHeight = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = getMontserratFontFamily()
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { /* TODO */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF60BD65), Color(0xFF02B6CF))
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "HUBUNGI KAMI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                ValuePropCard(
                    title = "Kolaborasi Strategis",
                    description = "Kami bekerja sebagai mitra, bukan hanya penyedia layanan. Kesuksesan Anda adalah misi kami."
                )
                ValuePropCard(
                    title = "Solusi Berbasis Data",
                    description = "Setiap rekomendasi didasarkan pada analisis data yang mendalam dan bukti empiris yang kuat."
                )
            }
        }
    }
}

@Composable
fun ValuePropCard(title: String, description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(16.dp))
            .padding(32.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Placeholder for Icon
            Box(modifier = Modifier.size(32.dp).background(Color(0xFF60BD65), shape = RoundedCornerShape(6.dp)))
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = getDmSansFontFamily())
            Text(text = description, fontSize = 16.sp, color = Color.Gray, fontFamily = getDmSansFontFamily())
        }
    }
}
