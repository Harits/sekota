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
import com.sekota.ui.WindowWidth
import com.sekota.ui.contentGridColumns
import com.sekota.ui.gridSpacing
import com.sekota.ui.sectionHorizontalPadding
import com.sekota.ui.windowWidthOf
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
        syncService.syncEventFlow.collect {
            merchList = getMerchUseCase()
        }
    }

    BoxWithConstraints {
        val windowWidth = windowWidthOf(maxWidth)
        // Same rule as the catalog: the filter rail appears from Expanded only.
        val isCompact = !windowWidth.isAtLeastExpanded

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            if (isCompact) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        SidebarFilter(
                            modifier = Modifier.fillMaxWidth(),
                            onNavigate = onNavigate,
                            isMerchandise = true
                        )
                    }
                    MerchContent(
                        windowWidth = windowWidth,
                        merchList = merchList,
                        orderMessage = orderMessage,
                        onOrderMessageChange = { orderMessage = it },
                        isLoggedIn = isLoggedIn,
                        onRequestAuth = onRequestAuth
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Box(modifier = Modifier.width(280.dp)) {
                        SidebarFilter(onNavigate = onNavigate, isMerchandise = true)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        MerchContent(
                            windowWidth = windowWidth,
                            merchList = merchList,
                            orderMessage = orderMessage,
                            onOrderMessageChange = { orderMessage = it },
                            isLoggedIn = isLoggedIn,
                            onRequestAuth = onRequestAuth
                        )
                    }
                }
            }
            Footer()
        }
    }
}

@Composable
fun MerchContent(
    windowWidth: WindowWidth,
    merchList: List<AdminMerch>, 
    orderMessage: String?, 
    onOrderMessageChange: (String?) -> Unit,
    isLoggedIn: Boolean,
    onRequestAuth: (onSuccess: () -> Unit) -> Unit
) {
    // The rail is hidden below Expanded, so the pane is wide enough for a 2-up grid
    // at Medium even though the header still stacks.
    val isCompact = !windowWidth.isAtLeastExpanded
    val columns = if (windowWidth.isAtLeastExpanded) 3 else windowWidth.contentGridColumns
    val spacing = windowWidth.gridSpacing

    Column(modifier = Modifier.fillMaxWidth().padding(windowWidth.sectionHorizontalPadding)) {
        if (isCompact) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Merchandise",
                    fontSize = if (windowWidth.isCompact) 32.sp else 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color.Black
                )
                Text(
                    text = "Di Sekota, setiap instrumen adalah bagian dari strategi. Miliki koleksi eksklusif yang merepresentasikan nilai Trusted Intelligence dan Urban Sinergy di meja kerja atau aktivitas lapangan Anda.",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 24.sp
                )
                
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.End)) {
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
        } else {
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

        // Grid bound to real Merch list from repository
        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            merchList.chunked(columns).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    rowProducts.forEach { item ->
                        ProductCard(
                            title = item.title,
                            authorOrSubtitle = "${item.seriesName.uppercase()}  •  $${item.price}",
                            rating = item.rating,
                            buttonText = "Order Now",
                            imageUrlOrBase64 = item.imageUrl,
                            cardHeight = if (columns >= 3) 520.dp else 480.dp,
                            coverHeight = if (columns >= 3) 340.dp else 300.dp,
                            modifier = Modifier.weight(1f),
                            onClick = { 
                                if (isLoggedIn) {
                                    onOrderMessageChange("Order initiated for ${item.title}. Our merchandising team will contact you.")
                                } else {
                                    onRequestAuth {
                                        onOrderMessageChange("Authentication verified. Order initiated for ${item.title} ($${item.price})!")
                                    }
                                }
                            }
                        )
                    }
                    // Fill empty spaces if row is not full
                    repeat(columns - rowProducts.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
        Pagination()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun MerchandiseScreenPreview() {
    MaterialTheme {
        MerchandiseScreen(onNavigate = {})
    }
}
