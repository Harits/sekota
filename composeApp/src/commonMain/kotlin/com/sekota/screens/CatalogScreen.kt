package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CatalogScreen(onBookClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().heightIn(min = 1000.dp)) {
            Box(modifier = Modifier.width(280.dp).fillMaxHeight().background(Color.White)) {
                SidebarFilter()
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(48.dp)
            ) {
                Text(
                    text = "eBook Catalogue",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Membawa semangat sinergi ke dalam genggaman. E-book katalog ini adalah kurasi instrumen taktis yang dirancang untuk memperkuat konektivitas tim dan mitra strategis Anda. Mari orkestrasi identitas profesional bersama Sekota.",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.widthIn(max = 800.dp),
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(48.dp))

                val books = listOf(
                    Triple("Blind Spot Radar", "PUTU AAN J.", Color(0xFF1A1A1A)),
                    Triple("The Book", "ALF LOWANS", Color(0xFF2D2D2D)),
                    Triple("The Midnight Library", "MATT HAIG", Color(0xFFE2E8F0)),
                    Triple("Dune: Part One", "FRANK HERBERT", Color(0xFFFDFCFB)),
                    Triple("Klara and the Sun", "KAZUO ISHIGURO", Color(0xFFCBD5E1)),
                    Triple("The 7 Habits", "STEPHEN COVEY", Color(0xFF0F172A))
                )

                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    books.chunked(3).forEach { rowBooks ->
                        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            rowBooks.forEach { (title, author, color) ->
                                ProductCard(
                                    title = title,
                                    authorOrSubtitle = author,
                                    rating = 4.8,
                                    imageColor = color,
                                    modifier = Modifier.weight(1f),
                                    onClick = onBookClick
                                )
                            }
                            repeat(3 - rowBooks.size) {
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

@Composable
fun Pagination() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Arrow Left
        PaginationArrow("<")
        
        Spacer(modifier = Modifier.width(12.dp))
        
        val pages = listOf("1", "2", "3", "...", "10")
        pages.forEach { page ->
            val isActive = page == "1"
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(40.dp)
                    .background(
                        if (isActive) Color(0xFF00BFA5) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page,
                    color = if (isActive) Color.White else Color.Gray,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Arrow Right
        PaginationArrow(">")
    }
}

@Composable
fun PaginationArrow(text: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogScreen(onBookClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PaginationPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Pagination()
        }
    }
}
