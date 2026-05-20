package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.caret_down

@Composable
fun MerchandiseScreen(onNavigate: (Screen) -> Unit) {
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
                            color = Color.Gray,
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Newest",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00B7D1),
                            fontFamily = getDmSansFontFamily()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(Res.drawable.caret_down),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF00B7D1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                val products = listOf(
                    Triple("Black Totebag", "THE URBAN COLLABORATOR", 5.0),
                    Triple("Matte Black Tumbler", "THE SINERGI EXECUTIVE", 4.5),
                    Triple("Black T Shirt", "SINERGI EVERYDAY", 4.8),
                    Triple("Note Book A5", "STRATEGIC FORESIGHT", 4.0),
                    Triple("Keychain", "CONNECTIVITY", 3.0)
                )

                // 3-column Grid
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    products.chunked(3).forEach { rowProducts ->
                        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            rowProducts.forEach { (title, subtitle, rating) ->
                                ProductCard(
                                    title = title,
                                    authorOrSubtitle = subtitle,
                                    rating = rating,
                                    buttonText = "View Details",
                                    modifier = Modifier.weight(1f),
                                    onClick = { /* TODO */ }
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
                Spacer(modifier = Modifier.height(64.dp))

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
