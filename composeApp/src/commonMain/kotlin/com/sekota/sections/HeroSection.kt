package com.sekota.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import com.sekota.components.SekotaIconMark

private val InkNavy = Color(0xFF0D1F2D)

@Composable
fun HeroSection(
    onExplorationClick: () -> Unit = {},
    onContactClick: () -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getLiveMetricsUseCase = remember { GetAdminLiveMetricsUseCase(repository) }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    var liveMetrics by remember { mutableStateOf(AdminLiveMetrics()) }
    var productCount by remember { mutableStateOf(4) }
    var activeCardIndex by remember { mutableStateOf(0) } // 0 = Live Metrics front, 1 = Intelligence Suite front

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

                    // CTA Buttons (Material Design 3: Primary Filled + Secondary Outlined)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                    ) {
                        Button(
                            onClick = onExplorationClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF02B6CF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .fillMaxWidth()
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text(
                                text = "Eksplorasi Produk",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getDmSansFontFamily()
                            )
                        }

                        OutlinedButton(
                            onClick = onContactClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF0D1F2D)
                            ),
                            border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .fillMaxWidth()
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text(
                                text = "Hubungi Tim Ahli",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
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
                    MetricsGraphic(
                        liveMetrics = liveMetrics,
                        productCount = productCount,
                        isCompact = true,
                        activeCardIndex = activeCardIndex,
                        onActiveCardChange = { activeCardIndex = it }
                    )
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

                    // CTA Buttons (Material Design 3: Primary Filled + Secondary Outlined)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Button(
                            onClick = onExplorationClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF02B6CF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text(
                                 text = "Eksplorasi Produk",
                                 fontSize = 15.sp,
                                 fontWeight = FontWeight.Bold,
                                 fontFamily = getDmSansFontFamily(),
                                 modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = onContactClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF0D1F2D)
                            ),
                            border = BorderStroke(1.5.dp, Color(0xFFD1D5DB)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text(
                                text = "Hubungi Tim Ahli",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
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
                    MetricsGraphic(
                        liveMetrics = liveMetrics,
                        productCount = productCount,
                        isCompact = false,
                        activeCardIndex = activeCardIndex,
                        onActiveCardChange = { activeCardIndex = it }
                    )
                }
            }
        }
    }
}

@Composable
fun MetricsGraphic(
    liveMetrics: AdminLiveMetrics,
    productCount: Int,
    isCompact: Boolean,
    activeCardIndex: Int = 0,
    onActiveCardChange: (Int) -> Unit = {}
) {

    val boxWidth = if (isCompact) 320.dp else 500.dp
    val boxHeight = if (isCompact) 350.dp else 450.dp
    
    val suiteBoxWidth = if (isCompact) 280.dp else 330.dp
    val suiteBoxHeight = if (isCompact) 180.dp else 220.dp

    val cardWidth = if (isCompact) 280.dp else 320.dp

    // Smooth animated transitions for card positions, scale, rotation, and elevation (deck shuffle feel)
    val metricsZIndex by animateFloatAsState(targetValue = if (activeCardIndex == 0) 3f else 1f, animationSpec = tween(450))
    val suiteZIndex by animateFloatAsState(targetValue = if (activeCardIndex == 1) 3f else 1f, animationSpec = tween(450))

    val metricsScale by animateFloatAsState(targetValue = if (activeCardIndex == 0) 1.0f else 0.93f, animationSpec = tween(450))
    val suiteScale by animateFloatAsState(targetValue = if (activeCardIndex == 1) 1.0f else 0.93f, animationSpec = tween(450))

    val metricsRotation by animateFloatAsState(targetValue = if (activeCardIndex == 0) 0f else 2.5f, animationSpec = tween(450))
    val suiteRotation by animateFloatAsState(targetValue = if (activeCardIndex == 1) 0f else -2.5f, animationSpec = tween(450))

    val metricsOffsetX by animateFloatAsState(
        targetValue = if (activeCardIndex == 0) {
            if (isCompact) 16f else -10f
        } else {
            if (isCompact) 36f else 50f
        },
        animationSpec = tween(450)
    )
    val metricsOffsetY by animateFloatAsState(
        targetValue = if (activeCardIndex == 0) {
            if (isCompact) 40f else 30f
        } else {
            if (isCompact) -10f else -40f
        },
        animationSpec = tween(450)
    )

    val suiteOffsetX by animateFloatAsState(
        targetValue = if (activeCardIndex == 1) {
            if (isCompact) 20f else 20f
        } else {
            if (isCompact) -10f else 0f
        },
        animationSpec = tween(450)
    )
    val suiteOffsetY by animateFloatAsState(
        targetValue = if (activeCardIndex == 1) {
            if (isCompact) 40f else 30f
        } else {
            if (isCompact) -20f else -40f
        },
        animationSpec = tween(450)
    )

    Box(
        modifier = Modifier
            .width(boxWidth)
            .height(boxHeight)
            .pointerInput(Unit) {
                var totalDragX = 0f
                detectDragGestures(
                    onDragStart = { totalDragX = 0f },
                    onDragEnd = {
                        if (totalDragX < -30f) {
                            onActiveCardChange(1)
                        } else if (totalDragX > 30f) {
                            onActiveCardChange(0)
                        }
                    },
                    onDragCancel = { totalDragX = 0f },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        totalDragX += dragAmount.x
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Intelligence Suite Box (Product Count Card)
        Box(
            modifier = Modifier
                .align(if (isCompact) Alignment.TopStart else Alignment.BottomStart)
                .offset(x = suiteOffsetX.dp, y = suiteOffsetY.dp)
                .zIndex(suiteZIndex)
                .graphicsLayer {
                    scaleX = suiteScale
                    scaleY = suiteScale
                    rotationZ = suiteRotation
                }
                .size(width = suiteBoxWidth, height = suiteBoxHeight)
                .shadow(
                    elevation = if (activeCardIndex == 1) 24.dp else 6.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Black.copy(alpha = 0.15f),
                    spotColor = Color.Black.copy(alpha = 0.15f)
                )
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65)),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),
                    RoundedCornerShape(32.dp)
                )
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onActiveCardChange(if (activeCardIndex == 1) 0 else 1)
                }
                .padding(if (isCompact) 22.dp else 28.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header row with product count, Sekota brand mark, and card switcher indicator dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "$productCount",
                        color = Color.White,
                        fontSize = if (isCompact) 54.sp else 72.sp,
                        lineHeight = if (isCompact) 54.sp else 72.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getMontserratFontFamily()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Selected Card Indicator dots on Suite Card
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(if (activeCardIndex == 0) 10.dp else 7.dp)
                                    .background(
                                        if (activeCardIndex == 0) Color.White else Color.White.copy(alpha = 0.45f),
                                        CircleShape
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onActiveCardChange(0) }
                            )
                            Box(
                                modifier = Modifier
                                    .size(if (activeCardIndex == 1) 10.dp else 7.dp)
                                    .background(
                                        if (activeCardIndex == 1) Color.White else Color.White.copy(alpha = 0.45f),
                                        CircleShape
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onActiveCardChange(1) }
                            )
                        }

                        // Sekota Icon Mark inside frosted pill badge
                        Surface(
                            color = Color.White.copy(alpha = 0.25f),
                            shape = CircleShape,
                            modifier = Modifier.size(if (isCompact) 36.dp else 42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                SekotaIconMark(
                                    size = if (isCompact) 20.dp else 24.dp,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Text(
                    "INTELLIGENCE SUITE\nPRODUCTS",
                    color = Color.White,
                    fontSize = if (isCompact) 13.sp else 15.sp,
                    lineHeight = if (isCompact) 17.sp else 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getMontserratFontFamily()
                )
            }
        }

        // Live Metrics Card bound to AdminLiveMetrics
        Box(
            modifier = Modifier
                .align(if (isCompact) Alignment.BottomEnd else Alignment.CenterEnd)
                .offset(x = metricsOffsetX.dp, y = metricsOffsetY.dp)
                .zIndex(metricsZIndex)
                .graphicsLayer {
                    scaleX = metricsScale
                    scaleY = metricsScale
                    rotationZ = metricsRotation
                }
                .shadow(
                    elevation = if (activeCardIndex == 0) 30.dp else 6.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.12f),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onActiveCardChange(if (activeCardIndex == 0) 1 else 0)
                }
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
                    // Clickable indicator dots to switch card
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (activeCardIndex == 0) 10.dp else 7.dp)
                                .background(
                                    if (activeCardIndex == 0) Color(0xFF02B6CF) else Color(0xFF02B6CF).copy(alpha = 0.35f),
                                    CircleShape
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onActiveCardChange(0) }
                        )
                        Box(
                            modifier = Modifier
                                .size(if (activeCardIndex == 1) 10.dp else 7.dp)
                                .background(
                                    if (activeCardIndex == 1) Color(0xFF60BD65) else Color(0xFF60BD65).copy(alpha = 0.35f),
                                    CircleShape
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onActiveCardChange(1) }
                        )
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
