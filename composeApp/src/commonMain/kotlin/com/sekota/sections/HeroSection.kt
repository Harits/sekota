package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
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
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.usecase.GetAdminLiveMetricsUseCase
import com.sekota.features.admin.domain.usecase.GetAdminProductsUseCase

@Composable
fun HeroSection() {
    val repository = remember { AdminRepositoryImpl() }
    val getLiveMetricsUseCase = remember { GetAdminLiveMetricsUseCase(repository) }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    var liveMetrics by remember { mutableStateOf(AdminLiveMetrics()) }
    var productCount by remember { mutableStateOf(4) }

    LaunchedEffect(Unit) {
        liveMetrics = getLiveMetricsUseCase()
        productCount = getProductsUseCase().size
    }

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
            // Left Content (Text)
            Column(
                modifier = Modifier.weight(1.1f),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Eyebrow Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF02B6CF), CircleShape)
                    )
                    Text(
                        text = "TRUSTED INTELLIGENCE. MEASURED IMPACT.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF02B6CF),
                        fontFamily = getDmSansFontFamily(),
                        letterSpacing = 2.sp
                    )
                }

                // Headline
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Ekosistem Produk",
                        fontSize = 52.sp,
                        lineHeight = 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111827),
                        fontFamily = getMontserratFontFamily()
                    )
                    Text(
                        text = "Intelligence Suite",
                        fontSize = 52.sp,
                        lineHeight = 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = getMontserratFontFamily(),
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65)),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            )
                        )
                    )
                    Text(
                        text = "Sekota Sinergi Indonesia",
                        fontSize = 52.sp,
                        lineHeight = 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111827),
                        fontFamily = getMontserratFontFamily()
                    )
                }

                // Description
                Text(
                    text = "Sekota menghadirkan Intelligence Suite: ekosistem produk berbasis data yang dirancang untuk membantu korporasi memetakan risiko sosial, mengukur dampak keberlanjutan, dan memperkuat relasi pemangku kepentingan.",
                    fontSize = 17.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4B5563),
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.width(540.dp)
                )

                // CTA Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF02B6CF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(
                            text = "Eksplorasi Produk",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF374151)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFE5E7EB)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(
                            text = "Hubungi Tim Ahli",
                            fontSize = 15.sp,
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
                MetricsGraphic(liveMetrics = liveMetrics, productCount = productCount)
            }
        }
    }
}

@Composable
fun MetricsGraphic(liveMetrics: AdminLiveMetrics, productCount: Int) {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(450.dp)
    ) {
        // Intelligence Suite Box (Bottom Left - behind the card)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = (-60).dp)
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
                    "$productCount",
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

        // Live Metrics Card (Front and Center-Right) bound to AdminLiveMetrics
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-10).dp, y = 20.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(28.dp)
                .width(320.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "LIVE METRICS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontFamily = getMontserratFontFamily(),
                        letterSpacing = 1.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.size(8.dp).background(Color(0xFF02B6CF), CircleShape))
                        Box(Modifier.size(8.dp).background(Color(0xFF60BD65).copy(alpha = 0.4f), CircleShape))
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Data Accuracy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            fontFamily = getMontserratFontFamily()
                        )
                        Text(
                            liveMetrics.dataAccuracy,
                            color = Color(0xFF60BD65),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            fontFamily = getMontserratFontFamily()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.998f)
                                .fillMaxHeight()
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF02B6CF), Color(0xFF60BD65))),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            liveMetrics.totalClients,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            fontFamily = getMontserratFontFamily(),
                            color = Color(0xFF111827)
                        )
                        Text(
                            "KLIEN & MITRA",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getMontserratFontFamily()
                        )
                    }
                    Column {
                        Text(
                            liveMetrics.establishedYear,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            fontFamily = getMontserratFontFamily(),
                            color = Color(0xFF111827)
                        )
                        Text(
                            "TAHUN BERDIRI",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getMontserratFontFamily()
                        )
                    }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xfff, showBackground = true, device = DESKTOP)
@Composable
fun HeroSectionPreview() {
    HeroSection()
}
