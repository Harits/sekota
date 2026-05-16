package com.sekota.sections

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun ValuePropDark() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B141B))
            .padding(vertical = 120.dp, horizontal = 64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Text(
                    text = "MENGAPA SEKOTA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Kami berada di persimpangan unik antara teknologi, konteks lokal, dan strategic foresight.",
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.widthIn(max = 500.dp)
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
                                    colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 32.dp, vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MULAI KONSULTASI STRATEGIS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ValuePropCard(
                    title = "Lebih Strategis dari Vendor IT",
                    description = "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.",
                    icon = Res.drawable.icon_3
                )
                ValuePropCard(
                    title = "Lebih Teknis dari Konsultan",
                    description = "Implementasi data real-time, bukan sekadar slide deck rekomendasi.",
                    icon = Res.drawable.icon_4
                )
            }
        }
    }
}

@Composable
fun ValuePropCard(title: String, description: String, icon: DrawableResource) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF111E26), shape = RoundedCornerShape(24.dp))
            .padding(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(top = 4.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = description,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
            }
        }
    }
}

@Preview(device=DESKTOP)
@Composable
fun ValuePropDarkPreview() {
    ValuePropDark()
}
