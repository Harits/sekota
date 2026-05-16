package com.sekota.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.*
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.*

@Composable
fun ContactFormSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 120.dp, horizontal = 120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Contact Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mulai Percakapan\nStrategis.",
                    fontSize = 56.sp,
                    lineHeight = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = getMontserratFontFamily(),
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Tim kami akan merespons dalam 1x24 jam kerja untuk mendiskusikan kebutuhan Anda.",
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF64748B),
                    fontFamily = getDmSansFontFamily()
                )
                Spacer(modifier = Modifier.height(48.dp))
                
                ContactInfoItem(
                    icon = Res.drawable.map_pin,
                    text = "Jl. H. Rasam No.96, Parigi Baru,\nPd. Aren, Tangerang Selatan 15228"
                )
                ContactInfoItem(
                    icon = Res.drawable.envelope_simple,
                    text = "sales@sekota.id"
                )
                ContactInfoItem(
                    icon = Res.drawable.globe_simple,
                    text = "www.sekota.id"
                )
            }

            // Right: Form
            Card(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(start = 100.dp),
                shape = RoundedCornerShape(48.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(64.dp),
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    var name by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }
                    var message by remember { mutableStateOf("") }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        FormTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "NAMA LENGKAP",
                            placeholder = "Budi Santoso",
                            modifier = Modifier.weight(1f)
                        )
                        FormTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "EMAIL BISNIS",
                            placeholder = "budi@instansi.go.id",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    FormTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = "PESAN / KEBUTUHAN STRATEGIS",
                        placeholder = "Ceritakan tantangan Anda...",
                        modifier = Modifier.fillMaxWidth(),
                        isTextArea = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        shape = RoundedCornerShape(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Text(
                            text = "Kirim & Mulai Konsultasi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = getDmSansFontFamily()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfoItem(icon: org.jetbrains.compose.resources.DrawableResource, text: String) {
    Row(
        modifier = Modifier.padding(bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF1F5F9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF475569)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color(0xFF475569),
            fontFamily = getDmSansFontFamily()
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isTextArea: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            fontFamily = getDmSansFontFamily()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { 
                Text(
                    text = placeholder, 
                    color = Color(0xFF94A3B8), 
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 16.sp
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF0F172A)
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = getDmSansFontFamily(),
                color = Color(0xFF0F172A)
            ),
            minLines = if (isTextArea) 4 else 1,
            maxLines = if (isTextArea) 10 else 1
        )
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun ContactFormSectionPreview() {
    ContactFormSection()
}
