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
import com.sekota.core.storage.TokenStorage
import com.sekota.features.auth.data.repository.AuthRepositoryImpl
import com.sekota.features.auth.domain.usecase.LoginUseCase
import com.sekota.features.auth.domain.usecase.SignupUseCase
import com.sekota.features.auth.domain.usecase.GetTokenUseCase
import com.sekota.features.auth.domain.usecase.ClearTokenUseCase
import com.sekota.features.profile.data.repository.ProfileRepositoryImpl
import com.sekota.features.profile.domain.usecase.GetProfileUseCase
import com.sekota.features.profile.domain.usecase.UpdateProfileUseCase

// Global or DI injected instance for simplicity in this example
val syncService = SyncService()
val tokenStorage = TokenStorage()
val authRepository = AuthRepositoryImpl(tokenStorage)
val profileRepository = ProfileRepositoryImpl(tokenStorage)
val loginUseCase = LoginUseCase(authRepository)
val signupUseCase = SignupUseCase(authRepository)
val getTokenUseCase = GetTokenUseCase(authRepository)
val clearTokenUseCase = ClearTokenUseCase(authRepository)
val getProfileUseCase = GetProfileUseCase(profileRepository)
val updateProfileUseCase = UpdateProfileUseCase(profileRepository)

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Landing) }
    val coroutineScope = rememberCoroutineScope()
    val syncState by syncService.syncState.collectAsState()
    
    var isLoggedIn by remember { mutableStateOf(getTokenUseCase() != null) }
    var showAuthGateDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(Unit) {
        syncService.connect(coroutineScope)
    }
    
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            // Screen Content Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp) // Offset for sticky navbar
            ) {
                when (currentScreen) {
                    Screen.Landing -> {
                        val landingScroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(landingScroll)
                        ) {
                            LandingScreen()
                        }
                    }
                    Screen.Catalog -> {
                        val catalogScroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(catalogScroll)
                        ) {
                            CatalogScreen(
                                onBookClick = { currentScreen = Screen.Details },
                                onNavigate = { currentScreen = it }
                            )
                        }
                    }
                    Screen.Details -> {
                        val detailsScroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(detailsScroll)
                        ) {
                            BookDetailsScreen(
                                isLoggedIn = isLoggedIn,
                                onRequestAuth = { onSuccess ->
                                    pendingAction = onSuccess
                                    showAuthGateDialog = true
                                }
                            )
                        }
                    }
                    Screen.Merchandise -> {
                        val merchScroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(merchScroll)
                        ) {
                            MerchandiseScreen(
                                onNavigate = { currentScreen = it },
                                isLoggedIn = isLoggedIn,
                                onRequestAuth = { onSuccess ->
                                    pendingAction = onSuccess
                                    showAuthGateDialog = true
                                }
                            )
                        }
                    }
                    Screen.Login -> LoginScreen(
                        loginUseCase = loginUseCase,
                        onLoginSuccess = { 
                            isLoggedIn = true
                            currentScreen = Screen.Catalog 
                        },
                        onNavigateToSignup = { currentScreen = Screen.Signup }
                    )
                    Screen.Signup -> SignupScreen(
                        signupUseCase = signupUseCase,
                        updateProfileUseCase = updateProfileUseCase,
                        onSignupSuccess = { 
                            isLoggedIn = true
                            currentScreen = Screen.Catalog 
                        },
                        onNavigateToLogin = { currentScreen = Screen.Login }
                    )
                    Screen.Profile -> ProfileScreen(
                        getProfileUseCase = getProfileUseCase,
                        updateProfileUseCase = updateProfileUseCase,
                        onLogout = {
                            clearTokenUseCase()
                            isLoggedIn = false
                            currentScreen = Screen.Landing
                        }
                    )
                    Screen.Admin -> {
                        AdminDashboardScreen()
                    }
                }
            }

            // Sticky Navbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Navbar(
                    isLoggedIn = isLoggedIn,
                    onNavigate = { screen -> currentScreen = screen }
                )
                // Simple sync status overlay
                Text(
                    text = "Sync: $syncState",
                    fontSize = 10.sp,
                    color = if (syncState == "Connected" || syncState.startsWith("Sync update:")) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 4.dp)
                )
            }

            // UC-GATE-01: Inline Auth-Gating Modal Dialog
            if (showAuthGateDialog) {
                com.sekota.components.AuthGateDialog(
                    loginUseCase = loginUseCase,
                    signupUseCase = signupUseCase,
                    onDismissRequest = {
                        showAuthGateDialog = false
                        pendingAction = null
                    },
                    onAuthSuccess = {
                        isLoggedIn = true
                        showAuthGateDialog = false
                        pendingAction?.invoke()
                        pendingAction = null
                    }
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
