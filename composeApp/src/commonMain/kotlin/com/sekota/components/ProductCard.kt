package com.sekota.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.filled_star
import androidx.compose.ui.layout.ContentScale
import com.sekota.utils.decodeBase64ToBitmap
import sekota.composeapp.generated.resources.stroke_star

@Composable
fun ProductCard(
    title: String,
    authorOrSubtitle: String,
    rating: Double,
    buttonText: String = "View Details",
    imageColor: Color = Color(0xFFEEEEEE),
    imageUrlOrBase64: String? = null,
    modifier: Modifier = Modifier,
    // Sized by the caller from the window size class: a card in a 3-up desktop grid
    // is much narrower than a full-bleed compact card and needs a shorter cover.
    cardHeight: androidx.compose.ui.unit.Dp = 520.dp,
    coverHeight: androidx.compose.ui.unit.Dp = 340.dp,
    onClick: () -> Unit,
) {
    val bitmap: ImageBitmap? = remember(imageUrlOrBase64) {
        if (!imageUrlOrBase64.isNullOrBlank()) {
            decodeBase64ToBitmap(imageUrlOrBase64)
        } else null
    }

    Card(
        modifier = modifier.fillMaxWidth().height(cardHeight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image / Cover Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(coverHeight)
                    .background(imageColor),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (!imageUrlOrBase64.isNullOrBlank() && imageUrlOrBase64.length < 10) {
                    // Render emoji icon like 👕 or ☕
                    Text(text = imageUrlOrBase64, fontSize = 64.sp)
                } else {
                    // Fallback sleek placeholder
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (title.isNotBlank()) title.take(1).uppercase() else "✦",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }


            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        val isFilled = index < rating.toInt()
                        Image(
                            painter = painterResource(if (isFilled) Res.drawable.filled_star else Res.drawable.stroke_star),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF00BFA5))
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
                    lineHeight = 24.sp,
                    maxLines = 2,
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

@Preview(device = DESKTOP, showBackground = true)
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
