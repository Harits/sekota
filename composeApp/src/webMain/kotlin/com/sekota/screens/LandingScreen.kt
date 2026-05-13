package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

import com.sekota.sections.*

@Composable
fun LandingScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        HeroSection()
        PartnerSection()
        FeatureGrid()
        ValueLoopSection()
        IntelligenceSuite()
        EBookPromo()
        ValuePropDark()
        ContactFormSection()
        Footer()
    }
}

@Composable
fun SectionPlaceholder(title: String, backgroundColor: Color = Color.Transparent, textColor: Color = Color.Black) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 24.sp, color = textColor, fontFamily = getDmSansFontFamily())
    }
}
