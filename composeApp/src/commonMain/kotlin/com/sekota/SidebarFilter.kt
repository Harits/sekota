package com.sekota

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.Res
import sekota.composeapp.generated.resources.bag
import sekota.composeapp.generated.resources.caret_down
import sekota.composeapp.generated.resources.check
import sekota.composeapp.generated.resources.magnifying_glass

@Composable
fun SidebarFilter(onNavigate: (Screen) -> Unit, isMerchandise: Boolean = false) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filterTitle = if (isMerchandise) "Category" else "Genre"
    val options = if (isMerchandise) {
        listOf("All", "T-Shirt", "Pin", "Sticker", "Others")
    } else {
        listOf("All", "Self-Improvement", "Social-Improvement", "Sustainablity", "Other")
    }
    
    val selectedOptions = remember { mutableStateListOf("All") }
    val years = listOf("2025", "2024", "2023")
    val selectedYears = remember { mutableStateListOf("2025") }

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Search
        SearchSection(searchQuery) { searchQuery = it }

        // Sort By
        SortSection()

        // Filter Section (Genre or Category)
        FilterSection(title = filterTitle, options = options, selectedOptions = selectedOptions)

        if (!isMerchandise) {
            // Year (only for E-Books)
            FilterSection(title = "Year", options = years, selectedOptions = selectedYears)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Promo 1: Customize Solution
        PromoBox(
            title = "Customize Solution",
            description = "Hubungi kami untuk proposal khusus",
            buttonText = "Contact Us",
            buttonColor = Color(0xFF4DB6AC),
            onClick = { /* TODO */ }
        )

        if (!isMerchandise) {
            // Promo 2: Executive Presence (only for E-Books to lead to Merchandise)
            PromoBox(
                title = "Executive Presence",
                description = "Lebih dari Sekadar Merchandise, Ini Adalah Pesan Strategis.",
                buttonText = "Get Merchandise",
                buttonColor = Color(0xFF00ACC1),
                showIcon = true,
                onClick = { onNavigate(Screen.Merchandise) }
            )
        }
    }
}

@Composable
fun SearchSection(query: String, onQueryChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search..", fontSize = 14.sp, fontFamily = getDmSansFontFamily(), color = Color.Black.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.magnifying_glass),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black.copy(alpha = 0.6f)
                )
            }
        )
    }
}

@Composable
fun SortSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Sort by",
            fontWeight = FontWeight.Bold,
            fontFamily = getDmSansFontFamily(),
            fontSize = 24.sp,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Newest",
                    fontSize = 18.sp,
                    fontFamily = getDmSansFontFamily(),
                    color = Color.Black
                )
                Icon(
                    painter = painterResource(Res.drawable.caret_down),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                brush = if (checked) {
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF00BFA5), Color(0xFF66BB6A))
                    )
                } else {
                    Brush.linearGradient(listOf(Color.White, Color.White))
                },
                shape = RoundedCornerShape(6.dp)
            )
            .then(
                if (!checked) {
                    Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                } else Modifier
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(Res.drawable.check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOptions: MutableList<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontFamily = getDmSansFontFamily(),
            fontSize = 20.sp,
            color = Color.Black
        )
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedOptions.contains(option)) {
                            selectedOptions.remove(option)
                        } else {
                            selectedOptions.add(option)
                        }
                    }
                    .padding(vertical = 4.dp)
            ) {
                CustomCheckbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = { checked ->
                        if (checked) {
                            selectedOptions.add(option)
                        } else {
                            selectedOptions.remove(option)
                        }
                    }
                )
                Text(
                    text = option,
                    fontFamily = getDmSansFontFamily(),
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun PromoBox(
    title: String,
    description: String,
    buttonText: String,
    buttonColor: Color,
    showIcon: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (showIcon) {
                // Shopping bag icon placeholder
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.bag), // Placeholder
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Black
                    )
                }
            }
            Text(title, fontWeight = FontWeight.Bold, fontFamily = getDmSansFontFamily(), fontSize = 16.sp)
            Text(
                text = description,
                fontSize = 12.sp,
                fontFamily = getDmSansFontFamily(),
                color = Color.Gray,
                lineHeight = 18.sp
            )
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(buttonText, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun FilterSectionPreview() {
    val options = listOf("All", "Self-Improvement", "Social-Improvement", "Sustainablity", "Other")
    val selectedOptions = remember { mutableStateListOf("All") }
    MaterialTheme {
        Surface(color = Color.Black) {
            FilterSection(
                title = "Genre",
                options = options,
                selectedOptions = selectedOptions,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SearchSectionPreview() {
    MaterialTheme {
        Surface(color = Color.Black) {
            SearchSection(
                query = "",
                onQueryChange = {}
            )
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SortSectionPreview() {
    MaterialTheme {
            SortSection()
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun SidebarFilterPreview() {
    MaterialTheme {
        Row {
            SidebarFilter(onNavigate = {}, isMerchandise = false)
            Spacer(modifier = Modifier.width(16.dp))
            SidebarFilter(onNavigate = {}, isMerchandise = true)
        }
    }
}

@Preview(device = DESKTOP, showBackground = true)
@Composable
fun PromoBoxPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PromoBox(
                title = "Customize Solution",
                description = "Hubungi kami untuk proposal khusus",
                buttonText = "Contact Us",
                buttonColor = Color(0xFF4DB6AC),
                onClick = {}
            )

            PromoBox(
                title = "Executive Presence",
                description = "Lebih dari Sekadar Merchandise, Ini Adalah Pesan Strategis.",
                buttonText = "Get Merchandise",
                buttonColor = Color(0xFF00ACC1),
                showIcon = true,
                onClick = {}
            )
        }
    }
}
