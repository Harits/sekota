package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.components.ProductCard
import com.sekota.features.admin.data.repository.AdminRepositoryImpl
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.usecase.GetAdminMerchUseCase
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.caret_down

@Composable
fun MerchandiseScreen(
    onNavigate: (Screen) -> Unit,
    isLoggedIn: Boolean = false,
    onRequestAuth: (onSuccess: () -> Unit) -> Unit = {}
) {
    val repository = remember { AdminRepositoryImpl() }
    val getMerchUseCase = remember { GetAdminMerchUseCase(repository) }
    var merchList by remember { mutableStateOf<List<AdminMerch>>(emptyList()) }
    var orderMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        merchList = getMerchUseCase()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            Box(modifier = Modifier.width(280.dp)) {
                SidebarFilter(onNavigate = onNavigate, isMerchandise = true)
            }

            Column(modifier = Modifier.weight(1f).padding(48.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Merchandise",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = getMontserratFontFamily(),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Di Sekota, setiap instrumen adalah bagian dari strategi. Miliki koleksi eksklusif yang merepresentasikan nilai Trusted Intelligence dan Urban Sinergy di meja kerja atau aktivitas lapangan Anda.",
                            fontSize = 16.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier.widthIn(max = 800.dp),
                            lineHeight = 24.sp
                        )
                    }

                    // Top Right Sort Indicator
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sort:",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.5f),
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Newest",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(Res.drawable.caret_down),
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }

                if (orderMessage != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE0F7FA),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = orderMessage ?: "",
                            color = Color(0xFF00838F),
                            fontSize = 14.sp,
                            fontFamily = getDmSansFontFamily(),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 3-column Grid bound to real Merch list from repository
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    merchList.chunked(3).forEach { rowProducts ->
                        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            rowProducts.forEach { item ->
                                ProductCard(
                                    title = item.title,
                                    authorOrSubtitle = "${item.seriesName.uppercase()}  •  $${item.price}",
                                    rating = item.rating,
                                    buttonText = "Order Now",
                                    modifier = Modifier.weight(1f),
                                    onClick = { 
                                        if (isLoggedIn) {
                                            orderMessage = "Order initiated for ${item.title}. Our merchandising team will contact you."
                                        } else {
                                            onRequestAuth {
                                                orderMessage = "Authentication verified. Order initiated for ${item.title} ($${item.price})!"
                                            }
                                        }
                                    }
                                )
                            }
                            // Fill empty spaces if row is not full
                            repeat(3 - rowProducts.size) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(64.dp))
                Pagination()
            }
        }
        Footer()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun MerchandiseScreenPreview() {
    MaterialTheme {
        MerchandiseScreen(onNavigate = {})
    }
}
