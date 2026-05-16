package com.sekota.screens

import androidx.compose.foundation.layout.*
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

@Composable
fun MerchandiseScreen() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.width(280.dp)) {
            SidebarFilter()
        }
        
        Column(modifier = Modifier.weight(1f).padding(48.dp)) {
            Text("Merchandise", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, fontFamily = getMontserratFontFamily())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Di Sekota, setiap instrumen adalah bagian dari strategi. Miliki koleksi eksklusif yang merepresentasikan nilai Trusted Intelligence dan Urban Sinergy di meja kerja atau aktivitas lapangan Anda.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.widthIn(max = 800.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // 3-column Grid
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                repeat(2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        repeat(3) { colIndex ->
                            ProductCard(
                                title = if (colIndex == 0) "Black Totebag" else "Matte Black Tumbler",
                                authorOrSubtitle = if (colIndex == 0) "THE URBAN COLLABORATOR" else "THE SINERGI EXECUTIVE",
                                rating = 4.9,
                                buttonText = "View Details",
                                modifier = Modifier.weight(1f),
                                onClick = { /* TODO */ }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            Pagination()
            Spacer(modifier = Modifier.height(64.dp))
            Footer()
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun MerchandiseScreenPreview() {
    MaterialTheme {
        MerchandiseScreen()
    }
}
