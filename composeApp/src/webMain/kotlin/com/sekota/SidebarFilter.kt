package com.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SidebarFilter() {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("T-Shirt", "Pin", "Sticker", "Others")

    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search", fontWeight = FontWeight.Bold, fontFamily = getDmSansFontFamily(), fontSize = 18.sp)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...", fontFamily = getDmSansFontFamily()) },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Categories", fontWeight = FontWeight.Bold, fontFamily = getDmSansFontFamily(), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        categories.forEach { category ->
            Text(category, fontFamily = getDmSansFontFamily(), modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
