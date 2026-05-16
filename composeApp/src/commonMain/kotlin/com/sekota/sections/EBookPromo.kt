package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*

@Composable
fun EBookPromo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 48.dp)
    ) {
        // Subtitle
        Text(
            text = "PERPUSTAKAAN DIGITAL",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF26A69A),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Title and Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "E-Book & Panduan\nStrategis.",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = getMontserratFontFamily(),
                lineHeight = 56.sp
            )

            // Filters
            Row(
                modifier = Modifier
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill("Semua", active = true)
                FilterPill("Smart City", active = false)
                FilterPill("ESG", active = false)
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            EBookCard(
                title = "Transformasi\nDigital Pemerintah\nDaerah",
                description = "Strategi dan framework implementasi Smart City...",
                tag = "SMART CITY",
                color = Color(0xFF1976D2),
                modifier = Modifier.weight(1f)
            )
            EBookCard(
                title = "Strategi Pemilu\nBerbasis Data\nAnalytics",
                description = "Panduan untuk caleg dan tim sukses memanfaatkan...",
                tag = "POLITIK",
                color = Color(0xFFBF360C),
                modifier = Modifier.weight(1f)
            )
            EBookCard(
                title = "Implementasi Satu\nData Kab/Kota",
                description = "Langkah teknis membangun datawarehouse lintas OPD...",
                tag = "DATA",
                color = Color(0xFF388E3C),
                modifier = Modifier.weight(1f)
            )
            EBookCard(
                title = "OSINT & Data\nIntelligence\nModern",
                description = "Metodologi Open Source Intelligence untuk...",
                tag = "INTELLIGENCE",
                color = Color(0xFF673AB7),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Footer Button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = {},
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF26A69A)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                modifier = Modifier.height(56.dp).padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Lihat Katalog",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily()
                )
            }
        }
    }
}

@Composable
fun FilterPill(text: String, active: Boolean) {
    Surface(
        color = if (active) Color.White else Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = if (active) 2.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            fontSize = 14.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            color = if (active) Color.Black else Color.Gray,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Composable
fun EBookCard(
    title: String,
    description: String,
    tag: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(420.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Colored Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .background(color, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(24.dp)
            ) {
                // Tag
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Title
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = getMontserratFontFamily(),
                    lineHeight = 28.sp
                )
            }

            // Bottom Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIHAT DETAIL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = getDmSansFontFamily()
                    )
                    Text(
                        text = "›",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun EBookPromoPreview() {
    EBookPromo()
}
