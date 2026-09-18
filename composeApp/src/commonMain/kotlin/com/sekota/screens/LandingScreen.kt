package com.sekota.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekota.*

import com.sekota.sections.*

import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent

@Composable
fun LandingScreen(
    onNavigate: (Screen) -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onConsultationClick: () -> Unit = {},
    onExplorationClick: () -> Unit = {},
    onSolusiPositioned: (Int) -> Unit = {},
    onProdukPositioned: (Int) -> Unit = {},
    onKontakPositioned: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HeroSection(
            onExplorationClick = onExplorationClick,
            onContactClick = onConsultationClick
        )
        FeatureGrid()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onSolusiPositioned(coordinates.positionInParent().y.toInt())
                }
        ) {
            ValueLoopSection()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onProdukPositioned(coordinates.positionInParent().y.toInt())
                }
        ) {
            IntelligenceSuite(
                onProductClick = { product ->
                    onProductClick(product.code)
                }
            )
        }
        EBookPromo(
            onNavigateToCatalog = { onNavigate(Screen.Catalog) },
            onBookClick = { book -> onBookClick(book.id) }
        )
        ValuePropDark(
            onConsultationClick = onConsultationClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onKontakPositioned(coordinates.positionInParent().y.toInt())
                }
        ) {
            ContactFormSection()
        }
        Footer(
            onNavigate = onNavigate,
            onProductClick = onProductClick,
            onKontakClick = onConsultationClick
        )
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

@Preview(device = DESKTOP)
@Composable
fun LandingScreenPreview() {
    MaterialTheme {
        LandingScreen()
    }
}
