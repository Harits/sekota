package com.sekota.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun ProductCard(
    title: String,
    authorOrSubtitle: String,
    rating: Double,
    buttonText: String = "View Details",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().height(450.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text("Image", color = Color.LightGray)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐⭐⭐⭐⭐", fontSize = 12.sp) // Simple rating placeholder
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "($rating)", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = authorOrSubtitle,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                TextButton(
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = buttonText,
                        color = Color(0xFF60BD65),
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily()
                    )
                }
            }
        }
    }
}
