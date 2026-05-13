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

import androidx.compose.ui.Alignment
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults

@Composable
fun SidebarFilter() {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("T-Shirt", "Pin", "Sticker", "Others")
    val selectedCategories = remember { mutableStateListOf<String>() }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = selectedCategories.contains(category),
                    onCheckedChange = { checked ->
                        if (checked) selectedCategories.add(category)
                        else selectedCategories.remove(category)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF60BD65))
                )
                Text(
                    text = category,
                    fontFamily = getDmSansFontFamily(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
