package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun ProductCard(
    title: String,
    authorOrSubtitle: String,
    rating: Double,
    buttonText: String = "View Details",
    imageColor: Color = Color(0xFFEEEEEE),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth().height(520.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(imageColor),
                contentAlignment = Alignment.Center
            ) {
                // If we had images, we'd use them here. 
                // Using a color as a placeholder to distinguish cards.
            }

            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        val isFilled = index < rating.toInt()
                        Text(
                            text = if (isFilled) "★" else "☆",
                            color = if (isFilled) Color(0xFF00BFA5) else Color(0xFF00BFA5).copy(alpha = 0.5f),
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "($rating)", fontSize = 12.sp, color = Color.Gray, fontFamily = getDmSansFontFamily())
                }
                
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getDmSansFontFamily(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                Text(
                    text = authorOrSubtitle.uppercase(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily(),
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = buttonText,
                        color = Color(0xFF00BFA5),
                        fontWeight = FontWeight.Bold,
                        fontFamily = getDmSansFontFamily(),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    MaterialTheme {
        ProductCard(
            title = "Black Totebag",
            authorOrSubtitle = "THE URBAN COLLABORATOR",
            rating = 4.9
        ) {}
    }
}
