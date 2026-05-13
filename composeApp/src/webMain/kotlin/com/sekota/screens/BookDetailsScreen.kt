package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun BookDetailsScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            // Left: Book Cover
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(550.dp)
                    .background(Color(0xFF121212), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("BOOK COVER", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Right: Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Self-Improvement / Mindfulness",
                    fontSize = 14.sp,
                    color = Color(0xFF60BD65),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Blind Spot Radar",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily()
                )
                Text(
                    text = "by Putu Aan J.",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐⭐⭐⭐⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "4.8/5 from 1,240 readers", fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Buku 'Blind Spot Radar: Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar' merupakan karya thought leadership yang menyoroti fenomena di mana para pengambil keputusan tingkat atas sering kali gagal mendeteksi ancaman nyata atau peluang strategis...",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray,
                    fontFamily = getDmSansFontFamily()
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFF60BD65), Color(0xFF02B6CF))
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 32.dp, vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Read", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(56.dp).padding(horizontal = 32.dp)
                    ) {
                        Text("Add to Library", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // What's Inside & Metadata
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text("What's Inside", fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = getMontserratFontFamily())
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    InsideCard("The Attention Economy", modifier = Modifier.weight(1f))
                    InsideCard("Deep Work Protocols", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    InsideCard("Mindful Tech Integration", modifier = Modifier.weight(1f))
                    InsideCard("Cognitive Recovery", modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(64.dp))
                Text("AVERAGE READING TIME", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.7f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF60BD65),
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                MetadataCard()
            }
        }
        
        Footer()
    }
}

@Composable
fun InsideCard(title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = getDmSansFontFamily())
        }
    }
}

@Composable
fun MetadataCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            MetadataItem("PUBLISHED", "Nov 2025")
            MetadataItem("PAGES", "240")
            MetadataItem("LANGUAGE", "Indonesia")
            MetadataItem("FORMAT", "eBook, PDF")
        }
    }
}

@Composable
fun MetadataItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
