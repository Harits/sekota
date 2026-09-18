package com.sekota.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.usecase.GetAdminProductsUseCase
import com.sekota.ui.WindowWidth
import com.sekota.ui.contentHorizontalPadding
import com.sekota.ui.windowWidthOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun ProductDetailsScreen(
    productCode: String? = "VRD",
    onNavigateBack: () -> Unit = {},
    onConsultationClick: () -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getProductsUseCase = remember { GetAdminProductsUseCase(repository) }
    var products by remember { mutableStateOf<List<AdminProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        products = getProductsUseCase()
        syncService.syncEventFlow.collect {
            products = getProductsUseCase()
        }
    }

    val product = remember(products, productCode) {
        products.find { it.code.equals(productCode, ignoreCase = true) }
            ?: products.find { it.id.equals(productCode, ignoreCase = true) }
            ?: AdminProduct(
                id = "prd-veridia",
                code = "VRD",
                name = "Veridia",
                categoryEyebrow = "ESG & Carbon Intelligence",
                description = "Platform analitik komprehensif untuk pemantauan jejak karbon Scope 1, 2, dan 3, kepatuhan regulasi lingkungan, dan pelaporan ESG otomatis sesuai standar global.",
                features = listOf(
                    "Audit Jejak Karbon Scope 1, 2, & 3 Real-time",
                    "Pelaporan Keberlanjutan Otomatis (GRI, ISSB, IDX-ESG)",
                    "Pemodelan Skenario Dekarbonisasi Prediktif",
                    "Dashboard Tata Kelola Pemangku Kepentingan"
                )
            )
    }

    val logo: DrawableResource = when (product.code.uppercase()) {
        "VRD" -> Res.drawable.icon_veridia
        "ASC" -> Res.drawable.icon_acscendio
        "SOC" -> Res.drawable.icon_sociara
        "ECO" -> Res.drawable.icon_ecoflow
        else -> Res.drawable.icon_veridia
    }

    val accentColor = when (product.code.uppercase()) {
        "VRD" -> Color(0xFF00BFA5)
        "ASC" -> Color(0xFF1976D2)
        "SOC" -> Color(0xFF8BC34A)
        "ECO" -> Color(0xFF4CAF50)
        else -> Color(0xFF00B5C8)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth().background(Color(0xFFF8FAFB))) {
        val windowWidth = windowWidthOf(maxWidth)
        val isCompact = windowWidth.isCompact
        val horizontalPadding = contentHorizontalPadding(maxWidth)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = if (isCompact) 28.dp else 48.dp)
        ) {
            // Navigation Back Link
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
            ) {
                TextButton(
                    onClick = onNavigateBack,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "← Kembali ke Ekosistem Produk",
                        fontFamily = getDmSansFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Product Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(if (isCompact) 24.dp else 48.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Header Tag & Logo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = product.categoryEyebrow.uppercase(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                fontFamily = getDmSansFontFamily(),
                                letterSpacing = 1.sp
                            )
                        }

                        Image(
                            painter = painterResource(logo),
                            contentDescription = "${product.name} Logo",
                            modifier = Modifier.size(if (isCompact) 48.dp else 64.dp)
                        )
                    }

                    // Product Name
                    Text(
                        text = product.name,
                        fontSize = if (isCompact) 36.sp else 54.sp,
                        lineHeight = if (isCompact) 42.sp else 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = getMontserratFontFamily(),
                        color = Color(0xFF0F172A)
                    )

                    // Product Overview
                    Text(
                        text = product.description,
                        fontSize = if (isCompact) 16.sp else 18.sp,
                        lineHeight = if (isCompact) 26.sp else 28.sp,
                        color = Color(0xFF475569),
                        fontFamily = getDmSansFontFamily()
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Capabilities / Features Section
                    Text(
                        text = "KAPABILITAS UTAMA & STRUKTUR MODUL",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        fontFamily = getDmSansFontFamily(),
                        letterSpacing = 1.5.sp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        product.features.forEach { feature ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 20.dp, vertical = 14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(accentColor)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = feature,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B),
                                    fontFamily = getDmSansFontFamily()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // CTA Action
                    if (isCompact) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Tertarik mengadopsi ${product.name}?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    fontFamily = getMontserratFontFamily(),
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Konsultasikan arsitektur data institusi Anda dengan tim ahli Sekota.",
                                    fontSize = 14.sp,
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color(0xFF64748B),
                                    lineHeight = 20.sp
                                )
                            }

                            Button(
                                onClick = onConsultationClick,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Konsultasikan Solusi",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = getDmSansFontFamily(),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 32.dp)) {
                                Text(
                                    text = "Tertarik mengadopsi ${product.name}?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontFamily = getMontserratFontFamily(),
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Konsultasikan arsitektur data institusi Anda dengan tim ahli Sekota.",
                                    fontSize = 14.sp,
                                    fontFamily = getDmSansFontFamily(),
                                    color = Color(0xFF64748B)
                                )
                            }

                            Button(
                                onClick = onConsultationClick,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(),
                                modifier = Modifier
                                    .height(52.dp)
                                    .pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(Color(0xFF02B6CF), Color(0xFF60BD65))
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Konsultasikan Solusi",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = getDmSansFontFamily(),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun ProductDetailsScreenPreview() {
    MaterialTheme {
        ProductDetailsScreen()
    }
}
