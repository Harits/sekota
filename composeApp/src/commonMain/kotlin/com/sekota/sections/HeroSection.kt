package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.getDmSansFontFamily
import com.sekota.getMontserratFontFamily

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Decorative Blurs (Subtle)
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(Color(0xFF02B6CF).copy(alpha = 0.05f), CircleShape)
                .blur(80.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp)
                .padding(top = 100.dp, bottom = 140.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Content
            Column(
                modifier = Modifier.weight(1.1f),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Trusted",
                        fontSize = 88.sp,
                        lineHeight = 96.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = getMontserratFontFamily(),
                        color = Color(0xFF122329)
                    )
                    Text(
                        text = "Intelligence.",
                        fontSize = 88.sp,
                        lineHeight = 96.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = getMontserratFontFamily(),
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                            )
                        )
                    )
                }

                Text(
                    text = "Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI yang bertata kelola untuk keputusan strategis yang berkelanjutan.",
                    fontSize = 20.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = getDmSansFontFamily(),
                    color = Color(0xFF122329).copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122329)),
                        modifier = Modifier.height(64.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "Konsultasi Strategis",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily()
                            )
                            Text(
                                text = "→",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        modifier = Modifier.height(64.dp)
                    ) {
                        Text(
                            text = "Lihat Produk Kami",
                            color = Color(0xFF122329),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }

            // Right Content (Visuals)
            Box(
                modifier = Modifier.weight(0.9f),
                contentAlignment = Alignment.Center
            ) {
                MetricsGraphic()
            }
        }
    }
}

@Composable
fun MetricsGraphic() {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(450.dp)
    ) {
//        // TODO unhide and refine if there is reala data
//        // Background Circle (Top Left)
//        androidx.compose.foundation.Canvas(
//            modifier = Modifier
//                .size(120.dp)
//                .offset(x = 20.dp, y = 80.dp)
//        ) {
//            drawCircle(
//                color = Color(0xFF02B6CF).copy(alpha = 0.8f),
//                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
//            )
//        }

        // Intelligence Suite Box (Bottom Left - behind the card)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 40.dp, y = (-80).dp)
                .size(width = 330.dp, height = 220.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65)),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),
                    RoundedCornerShape(32.dp)
                )
                .padding(32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "4",
                    color = Color.White,
                    fontSize = 80.sp,
                    lineHeight = 80.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily()
                )
                Text(
                    "INTELLIGENCE SUITE\nPRODUCTS",
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily()
                )
            }
        }
//        //TODO unhide and refine this if there is real data
//        // Live Metrics Card (Front and Center-Right)
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .offset(x = (-20).dp, y = 0.dp)
//                .shadow(
//                    elevation = 40.dp,
//                    shape = RoundedCornerShape(24.dp),
//                    ambientColor = Color.Black.copy(alpha = 0.1f),
//                    spotColor = Color.Black.copy(alpha = 0.1f)
//                )
//                .background(Color.White, shape = RoundedCornerShape(24.dp))
//                .padding(32.dp)
//                .width(360.dp)
//        ) {
//            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "LIVE METRICS",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Gray,
//                        fontFamily = getMontserratFontFamily(),
//                        letterSpacing = 1.sp
//                    )
//                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                        Box(Modifier.size(8.dp).background(Color(0xFF02B6CF), CircleShape))
//                        Box(Modifier.size(8.dp).background(Color(0xFF60BD65).copy(alpha = 0.4f), CircleShape))
//                    }
//                }
//
//                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            "Data Accuracy",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                        Text(
//                            "99.8%",
//                            color = Color(0xFF60BD65),
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(10.dp)
//                            .background(Color(0xFFF3F4F6), RoundedCornerShape(5.dp))
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(0.998f)
//                                .fillMaxHeight()
//                                .background(
//                                    Brush.linearGradient(listOf(Color(0xFF02B6CF), Color(0xFF60BD65))),
//                                    RoundedCornerShape(5.dp)
//                                )
//                        )
//                    }
//                }
//
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column {
//                        Text(
//                            "100+",
//                            fontWeight = FontWeight.ExtraBold,
//                            fontSize = 28.sp,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                        Text(
//                            "KLIEN & MITRA",
//                            fontSize = 11.sp,
//                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                    }
//                    Column {
//                        Text(
//                            "2021",
//                            fontWeight = FontWeight.ExtraBold,
//                            fontSize = 28.sp,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                        Text(
//                            "TAHUN BERDIRI",
//                            fontSize = 11.sp,
//                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = getMontserratFontFamily()
//                        )
//                    }
//                }
//            }
//        }
    }
}

@Preview(backgroundColor = 0xfff, showBackground = true, device = DESKTOP)
@Composable
fun HeroSectionPreview() {
    HeroSection()
}


