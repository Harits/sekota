package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

@Composable
fun ContactFormSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 120.dp, horizontal = 48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left: Contact Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "HUBUNGI KAMI",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60BD65),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Mulai Percakapan\nStrategis.",
                    fontSize = 48.sp,
                    lineHeight = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily()
                )
                Spacer(modifier = Modifier.height(64.dp))
                
                ContactInfoItem("📍 Jakarta, Indonesia")
                ContactInfoItem("✉️ hello@sekota.id")
                ContactInfoItem("📞 +62 21 1234 5678")
            }

            // Right: Form
            Card(
                modifier = Modifier.weight(1f).padding(start = 64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(48.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    var name by remember { mutableStateOf("") }
                    var company by remember { mutableStateOf("") }
                    var message by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("Nama Perusahaan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Pesan Strategis Anda") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )
                    
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Text("KIRIM PESAN", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfoItem(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontFamily = getDmSansFontFamily(),
        modifier = Modifier.padding(bottom = 24.dp)
    )
}
