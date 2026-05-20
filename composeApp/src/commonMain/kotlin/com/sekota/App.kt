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
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import com.sekota.screens.*
import com.sekota.features.sync.data.repository.SyncService

// Global or DI injected instance for simplicity in this example
val syncService = SyncService()

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Landing) }
    val coroutineScope = rememberCoroutineScope()
    val syncState by syncService.syncState.collectAsState()

    LaunchedEffect(Unit) {
        syncService.connect(coroutineScope)
    }
    
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
                    Screen.Login -> LoginScreen(
                        onLoginSuccess = { currentScreen = Screen.Catalog },
                        onNavigateToSignup = { currentScreen = Screen.Signup }
                    )
                    Screen.Signup -> SignupScreen(
                        onSignupSuccess = { currentScreen = Screen.Catalog },
                        onNavigateToLogin = { currentScreen = Screen.Login }
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
                // Simple sync status overlay
                Text(
                    text = "Sync: $syncState",
                    fontSize = 10.sp,
                    color = if (syncState == "Connected" || syncState.startsWith("Sync update:")) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AppPreview() {
    App()
}
