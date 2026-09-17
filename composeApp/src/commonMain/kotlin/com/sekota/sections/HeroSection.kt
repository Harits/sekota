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
import com.sekota.syncService

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
        syncService.syncEventFlow.collect {
            liveMetrics = getLiveMetricsUseCase()
            productCount = getProductsUseCase().size
        }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val isCompact = maxWidth < 960.dp
        
        // Decorative Blurs (Subtle)
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(Color(0xFF02B6CF).copy(alpha = 0.05f), CircleShape)
                .blur(80.dp)
        )

        val paddingHorizontal = if (isCompact) 20.dp else 64.dp
        val headlineFontSize = if (isCompact) 36.sp else 52.sp
        val headlineLineHeight = if (isCompact) 44.sp else 60.sp

        if (isCompact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingHorizontal)
                    .padding(top = 60.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Content (Text)
                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF02B6CF),
                            fontFamily = getDmSansFontFamily(),
                            letterSpacing = 1.sp
                        )
                    }

                    // Headline
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ekosistem Produk",
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            fontFamily = getMontserratFontFamily()
                        )
                        Text(
                            text = "Intelligence Suite",
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
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
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            fontFamily = getMontserratFontFamily(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    // Description
                    Text(
                        text = "Sekota menghadirkan Intelligence Suite: ekosistem produk berbasis data yang dirancang untuk membantu korporasi memetakan risiko sosial, mengukur dampak keberlanjutan, dan memperkuat relasi pemangku kepentingan.",
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        color = Color(0xFF4B5563),
                        fontFamily = getDmSansFontFamily(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    // CTA Buttons
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF02B6CF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(52.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = "Eksplorasi Produk",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily()
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
                            modifier = Modifier.height(52.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = "Hubungi Tim Ahli",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily()
                            )
                        }
                    }
                }

                // Bottom Content (Visuals)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MetricsGraphic(liveMetrics = liveMetrics, productCount = productCount, isCompact = true)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingHorizontal)
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
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            fontFamily = getMontserratFontFamily()
                        )
                        Text(
                            text = "Intelligence Suite",
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
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
                            fontSize = headlineFontSize,
                            lineHeight = headlineLineHeight,
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
                    MetricsGraphic(liveMetrics = liveMetrics, productCount = productCount, isCompact = false)
                }
            }
        }
    }
}

@Composable
fun MetricsGraphic(liveMetrics: AdminLiveMetrics, productCount: Int, isCompact: Boolean) {
    val boxWidth = if (isCompact) 320.dp else 500.dp
    val boxHeight = if (isCompact) 350.dp else 450.dp
    
    val suiteBoxWidth = if (isCompact) 280.dp else 330.dp
    val suiteBoxHeight = if (isCompact) 180.dp else 220.dp
    val suiteBoxOffsetX = if (isCompact) 0.dp else 20.dp
    val suiteBoxOffsetY = if (isCompact) (-40).dp else (-60).dp

    val cardWidth = if (isCompact) 280.dp else 320.dp
    val cardOffsetX = if (isCompact) 20.dp else (-10).dp
    val cardOffsetY = if (isCompact) 40.dp else 20.dp

    Box(
        modifier = Modifier
            .width(boxWidth)
            .height(boxHeight)
    ) {
        // Intelligence Suite Box (Bottom Left - behind the card)
        Box(
            modifier = Modifier
                .align(if (isCompact) Alignment.TopStart else Alignment.BottomStart)
                .offset(x = suiteBoxOffsetX, y = suiteBoxOffsetY)
                .size(width = suiteBoxWidth, height = suiteBoxHeight)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65)),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),
                    RoundedCornerShape(32.dp)
                )
                .padding(if (isCompact) 24.dp else 32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 16.dp)) {
                Text(
                    "$productCount",
                    color = Color.White,
                    fontSize = if (isCompact) 60.sp else 80.sp,
                    lineHeight = if (isCompact) 60.sp else 80.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily()
                )
                Text(
                    "INTELLIGENCE SUITE\nPRODUCTS",
                    color = Color.White,
                    fontSize = if (isCompact) 14.sp else 16.sp,
                    lineHeight = if (isCompact) 18.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily()
                )
            }
        }

        // Live Metrics Card (Front and Center-Right) bound to AdminLiveMetrics
        Box(
            modifier = Modifier
                .align(if (isCompact) Alignment.BottomEnd else Alignment.CenterEnd)
                .offset(x = cardOffsetX, y = cardOffsetY)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(if (isCompact) 20.dp else 28.dp)
                .width(cardWidth)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(if (isCompact) 16.dp else 20.dp)) {
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
                            fontSize = if (isCompact) 20.sp else 24.sp,
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
                            fontSize = if (isCompact) 20.sp else 24.sp,
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

@Preview(backgroundColor = 0xffffffff, showBackground = true, device = DESKTOP)
@Composable
fun HeroSectionPreview() {
    HeroSection()
}
