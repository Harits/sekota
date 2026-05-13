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

import androidx.compose.foundation.clickable

@Composable
fun Navbar(onNavigate: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White.copy(alpha = 0.85f)) // Glassmorphism-ish background
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onNavigate(Screen.Landing) }
        ) {
            Image(
                painter = painterResource(Res.drawable.logo_sekota),
                contentDescription = "Sekota Logo",
                modifier = Modifier.height(40.dp)
            )
        }

        // Navigation Links
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                "Produk" to Screen.Landing,
                "Solusi" to Screen.Landing,
                "E-Book" to Screen.Catalog,
                "Merchandise" to Screen.Merchandise,
                "Kontak" to Screen.Landing
            )
            navItems.forEach { (name, screen) ->
                Text(
                    text = name,
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onNavigate(screen) }
                )
            }
        }

        // Action Button
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
                    .padding(horizontal = 24.dp, vertical = 12.dp),
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
