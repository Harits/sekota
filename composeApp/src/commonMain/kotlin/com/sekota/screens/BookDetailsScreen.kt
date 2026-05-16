package com.sekota.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun BookDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Hero Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            // Left: Book Cover
            BookCover(modifier = Modifier.width(360.dp).height(500.dp))

            // Right: Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Self-Improvement / Mindfulness",
                    fontSize = 14.sp,
                    color = Color(0xFF71717A),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Blind Spot Radar",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color.Black
                )
                Text(
                    text = "by Putu Aan J.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(rating = 5, color = Color(0xFF46B778))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "(4.8/5 from 1,240 readers)",
                        fontSize = 14.sp,
                        color = Color(0xFF71717A),
                        fontFamily = getDmSansFontFamily()
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Buku \"Blind Spot Radar: Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar\" merupakan karya thought leadership yang menyoroti fenomena di mana para pengambil keputusan tingkat atas sering kali gagal mendeteksi ancaman nyata atau peluang strategis, meskipun mereka memiliki kecerdasan dan data yang memadai.",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    color = Color.Black,
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Buku ini ditujukan bagi para pengambil keputusan di berbagai level (CEO, Manajemen Senior, hingga Menengah) yang ingin meningkatkan kualitas keputusan dan menghindari \"biaya\" tak terlihat dari blind spot.",
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    color = Color.Black,
                    fontFamily = getDmSansFontFamily()
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46B778)),
                        modifier = Modifier.height(56.dp).width(160.dp)
                    ) {
                        Text("Read", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF1F4F7),
                            contentColor = Color(0xFF00B7D1)
                        ),
                        modifier = Modifier.height(56.dp).width(200.dp)
                    ) {
                        Text("Add to Library", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        // Details Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            WhatsInsideSection(modifier = Modifier.weight(2f))
            MetadataSection(modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        Footer()
    }
}

@Composable
fun BookCover(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 8.dp,
        color = Color(0xFF121212)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "BLIND SPOT\nRADAR",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                fontFamily = getMontserratFontFamily()
            )
        }
    }
}

@Composable
fun RatingStars(rating: Int, color: Color) {
    Row {
        repeat(rating) {
            Image(
                painter = painterResource(Res.drawable.filled_star),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(color)
            )
        }
    }
}

@Composable
fun WhatsInsideSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(40.dp)) {
            Text(
                text = "What's Inside",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getMontserratFontFamily()
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InsideItem(
                    icon = Res.drawable.icon_6,
                    title = "The Attention Economy",
                    subtitle = "Understanding the mechanics behind digital distractions.",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(32.dp))
                InsideItem(
                    icon = Res.drawable.icon_7,
                    title = "Deep Work Protocols",
                    subtitle = "Actionable steps to enter flow state on command.",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InsideItem(
                    icon = Res.drawable.icon_8,
                    title = "Mindful Tech Integration",
                    subtitle = "Setting boundaries that stick without the guilt.",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(32.dp))
                InsideItem(
                    icon = Res.drawable.icon_5,
                    title = "Cognitive Recovery",
                    subtitle = "Restoring your mental energy after intense digital usage.",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            HorizontalDivider(color = Color(0xFFF1F4F7), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AVERAGE READING TIME",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
                Text(
                    text = "3H 45M",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A99D),
                    fontFamily = getDmSansFontFamily()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = Color(0xFF46B778),
                trackColor = Color(0xFFF1F4F7),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun InsideItem(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getDmSansFontFamily(),
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF71717A),
                fontFamily = getDmSansFontFamily(),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MetadataSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F4F7)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MetadataItem("PUBLISHED", "Nov 2025")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("PAGES", "240")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("LANGUAGE", "Indonesia")
            Spacer(modifier = Modifier.height(32.dp))
            MetadataItem("FORMAT", "eBook, PDF")
        }
    }
}

@Composable
fun MetadataItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF71717A),
            fontWeight = FontWeight.Bold,
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun BookDetailsScreenPreview() {
    MaterialTheme {
        BookDetailsScreen()
    }
}
