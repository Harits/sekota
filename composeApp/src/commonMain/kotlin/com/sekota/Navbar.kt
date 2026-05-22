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
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Navbar(
    isLoggedIn: Boolean = false,
    onNavigate: (Screen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
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
                modifier = Modifier.height(50.dp)
            )
        }

        // Navigation Links
        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = mutableListOf(
                "Solusi" to Screen.Landing,
                "Produk" to Screen.Landing,
                "E-Book" to Screen.Catalog,
                "Mengapa Sekota" to Screen.Landing,
                "Kontak" to Screen.Landing
            )
            if (isLoggedIn) {
                navItems.add("Profile" to Screen.Profile)
            } else {
                navItems.add("Login" to Screen.Login)
            }
            
            navItems.forEach { (name, screen) ->
                Text(
                    text = name,
                    fontFamily = getDmSansFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF4A5568),
                    modifier = Modifier
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
                            colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 32.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Konsultasi Strategis",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun NavbarPreview() {
    Navbar(onNavigate = {})
}
