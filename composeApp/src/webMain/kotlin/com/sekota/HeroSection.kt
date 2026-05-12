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

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Trusted Intelligence.",
            fontSize = 88.sp,
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
            text = "Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            fontFamily = getDmSansFontFamily(),
            color = Color.Black.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Button(
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF60BD65), Color(0xFF02B6CF))
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Konsultasi Strategis",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 16.sp
                )
            }
        }
    }
}
