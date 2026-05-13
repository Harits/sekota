package com.sekota

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.blur

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        // Decorative Blurs (Background Layer)
        Box(
            modifier = Modifier
                .size(600.dp)
                .offset(x = (-200).dp, y = (-100).dp)
                .background(Color(0xFF60BD65).copy(alpha = 0.15f), shape = RoundedCornerShape(300.dp))
                .blur(140.dp)
        )
        Box(
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 200.dp, y = 100.dp)
                .background(Color(0xFF02B6CF).copy(alpha = 0.15f), shape = RoundedCornerShape(300.dp))
                .blur(140.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 120.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Trusted Intelligence.",
                fontSize = 88.sp,
                lineHeight = 96.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = getMontserratFontFamily(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF60BD65), Color(0xFF02B6CF))
                    )
                )
            )

            Text(
                text = "Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI untuk pengambilan keputusan yang lebih cepat, tepat, dan strategis.",
                fontSize = 20.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Light,
                fontFamily = getDmSansFontFamily(),
                color = Color.Black.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 800.dp)
            )

            Button(
                onClick = { /* TODO: Scroll to Contact or open modal */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF60BD65), Color(0xFF02B6CF))
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 40.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Konsultasi Strategis",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
