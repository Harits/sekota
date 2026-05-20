package com.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.screens.*

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Landing) }
    
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            val scrollState = rememberScrollState()
            
            // Re-sync scroll on screen change
            LaunchedEffect(currentScreen) {
                scrollState.scrollTo(0)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                
                when (currentScreen) {
                    Screen.Landing -> LandingScreen()
                    Screen.Catalog -> CatalogScreen(
                        onBookClick = { currentScreen = Screen.Details },
                        onNavigate = { currentScreen = it }
                    )
                    Screen.Details -> BookDetailsScreen()
                    Screen.Merchandise -> MerchandiseScreen(
                        onNavigate = { currentScreen = it }
                    )
                }
            }

            // Sticky Navbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Navbar(onNavigate = { screen -> currentScreen = screen })
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AppPreview() {
    App()
}
