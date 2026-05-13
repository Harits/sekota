package com.sekota

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sekota.composeapp.generated.resources.*

@Composable
fun PartnerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TRUSTED BY LEADING ORGANIZATIONS",
            fontSize = 14.sp,
            fontFamily = getDmSansFontFamily(),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.logo_ecoflow),
                contentDescription = "Ecoflow",
                modifier = Modifier.height(40.dp)
            )
            Image(
                painter = painterResource(Res.drawable.logo_sociara),
                contentDescription = "Sociara",
                modifier = Modifier.height(40.dp)
            )
            Image(
                painter = painterResource(Res.drawable.logo_veridia),
                contentDescription = "Veridia",
                modifier = Modifier.height(40.dp)
            )
            Image(
                painter = painterResource(Res.drawable.logo_ascendio),
                contentDescription = "Ascendio",
                modifier = Modifier.height(40.dp)
            )
        }
    }
}
