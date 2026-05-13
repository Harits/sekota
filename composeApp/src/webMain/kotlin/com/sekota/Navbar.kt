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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.logo_sekota

@Composable
fun Navbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.8f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.logo_sekota),
            contentDescription = "Sekota Logo",
            modifier = Modifier.height(32.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Produk", fontFamily = getDmSansFontFamily())
            Text("Solusi", fontFamily = getDmSansFontFamily())
            Text("Tentang Kami", fontFamily = getDmSansFontFamily())
            Text("Kontak", fontFamily = getDmSansFontFamily())
        }

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
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Konsultasi Strategis",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 14.sp
                )
            }
        }
    }
}
