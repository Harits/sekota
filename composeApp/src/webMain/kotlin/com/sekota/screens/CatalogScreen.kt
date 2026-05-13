package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import com.sekota.components.ProductCard

@Composable
fun CatalogScreen(onBookClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.width(280.dp)) {
            SidebarFilter()
        }
        
        Column(modifier = Modifier.weight(1f).padding(48.dp)) {
            Text("eBook Catalogue", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, fontFamily = getMontserratFontFamily())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Membawa semangat sinergi ke dalam genggaman. E-book katalog ini adalah kurasi instrumen taktis yang dirancang untuk memperkuat konektivitas tim dan mitra strategis Anda.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = getDmSansFontFamily(),
                modifier = Modifier.widthIn(max = 800.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // 3-column Grid
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                repeat(3) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        repeat(3) { colIndex ->
                            ProductCard(
                                title = "Blind Spot Radar",
                                authorOrSubtitle = "PUTU AAN J.",
                                rating = 4.8,
                                modifier = Modifier.weight(1f),
                                onClick = onBookClick
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

@Composable
fun Pagination() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Simple numeric pagination placeholder
        val pages = listOf("1", "2", "3", "...", "10")
        pages.forEach { page ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(36.dp)
                    .background(if (page == "1") Color(0xFF60BD65) else Color.Transparent, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = page, color = if (page == "1") Color.White else Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
