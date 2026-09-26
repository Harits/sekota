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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import com.sekota.ui.WindowWidth
import com.sekota.ui.navbarHeight
import com.sekota.ui.windowWidthOf
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
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

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester

import com.sekota.di.AppContainer
import com.sekota.navigation.NavigationCoordinator
import com.sekota.navigation.NavigationEffect
import com.sekota.navigation.NavigationIntent
import com.sekota.navigation.NavigationState

// Default AppContainer singleton instance
val defaultAppContainer by lazy { AppContainer() }

// Backward compatibility references for preview functions and outer callers
val syncService get() = defaultAppContainer.syncService
val tokenStorage get() = defaultAppContainer.tokenStorage
val authRepository get() = defaultAppContainer.authRepository
val profileRepository get() = defaultAppContainer.profileRepository
val loginUseCase get() = defaultAppContainer.loginUseCase
val signupUseCase get() = defaultAppContainer.signupUseCase
val getTokenUseCase get() = defaultAppContainer.getTokenUseCase
val clearTokenUseCase get() = defaultAppContainer.clearTokenUseCase
val getProfileUseCase get() = defaultAppContainer.getProfileUseCase
val updateProfileUseCase get() = defaultAppContainer.updateProfileUseCase

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun App(
    container: AppContainer = remember { defaultAppContainer }
) {
    val coroutineScope = rememberCoroutineScope()
    val syncState by container.syncService.syncState.collectAsState()
    
    // MVI State Collection
    val navState by container.navigationCoordinator.state.collectAsState()
    var isLoggedIn by remember { mutableStateOf(container.getTokenUseCase() != null) }

    val landingScroll = rememberScrollState()
    var solusiOffsetY by remember { mutableStateOf(0) }
    var produkOffsetY by remember { mutableStateOf(0) }
    var kontakOffsetY by remember { mutableStateOf(0) }

    // Direct BringIntoViewRequester handles ("component tags")
    val solusiRequester = remember { BringIntoViewRequester() }
    val produkRequester = remember { BringIntoViewRequester() }
    val kontakRequester = remember { BringIntoViewRequester() }

    // MVI One-off Side Effect Processing (Single source of truth for scrolling to component tags)
    LaunchedEffect(container.navigationCoordinator) {
        container.navigationCoordinator.effects.collect { effect ->
            when (effect) {
                is NavigationEffect.BringSectionIntoView -> {
                    // Allow UI layout frame to bind and settle
                    kotlinx.coroutines.delay(100)
                    when (effect.section) {
                        NavbarActiveSection.SOLUSI -> {
                            try {
                                solusiRequester.bringIntoView()
                            } catch (e: Exception) {
                                landingScroll.animateScrollTo(solusiOffsetY.coerceAtLeast(0))
                            }
                        }
                        NavbarActiveSection.PRODUK -> {
                            try {
                                produkRequester.bringIntoView()
                            } catch (e: Exception) {
                                val target = if (produkOffsetY > 0) produkOffsetY else 1100
                                landingScroll.animateScrollTo(target)
                            }
                        }
                        NavbarActiveSection.KONTAK -> {
                            try {
                                kontakRequester.bringIntoView()
                            } catch (e: Exception) {
                                val target = if (kontakOffsetY > 0) kontakOffsetY else landingScroll.maxValue
                                landingScroll.animateScrollTo(target)
                            }
                        }
                        NavbarActiveSection.NONE -> {}
                    }
                }
            }
        }
    }

    // Synchronize active nav pill based on scroll position while on LandingScreen
    LaunchedEffect(landingScroll.value, navState.currentScreen) {
        if (navState.currentScreen == Screen.Landing) {
            container.navigationCoordinator.processIntent(
                NavigationIntent.ScrollPositionChanged(
                    scrollY = landingScroll.value,
                    produkOffsetY = produkOffsetY,
                    kontakOffsetY = kontakOffsetY
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        container.syncService.connect(coroutineScope)
    }
    
    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            val windowWidth = windowWidthOf(maxWidth)
            // Screen Content Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = windowWidth.navbarHeight) // Offset for sticky navbar
            ) {
                when (navState.currentScreen) {
                    Screen.Landing -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(landingScroll)
                        ) {
                            LandingScreen(
                                onNavigate = { screen ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(screen))
                                },
                                onProductClick = { code ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.SelectProduct(code))
                                },
                                onBookClick = { bookId ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.SelectBook(bookId))
                                },
                                onConsultationClick = {
                                    container.navigationCoordinator.processIntent(NavigationIntent.RequestConsultation())
                                },
                                onExplorationClick = {
                                    container.navigationCoordinator.processIntent(
                                        NavigationIntent.TargetLandingSection(NavbarActiveSection.PRODUK)
                                    )
                                },
                                onSolusiPositioned = { y -> solusiOffsetY = y },
                                onProdukPositioned = { y -> produkOffsetY = y },
                                onKontakPositioned = { y -> kontakOffsetY = y },
                                solusiRequester = solusiRequester,
                                produkRequester = produkRequester,
                                kontakRequester = kontakRequester
                            )
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
                                onBookClick = { bookId ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.SelectBook(bookId))
                                },
                                onNavigate = { screen ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(screen))
                                },
                                onConsultationClick = {
                                    container.navigationCoordinator.processIntent(NavigationIntent.RequestConsultation())
                                }
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
                                bookId = navState.selectedBookId,
                                isLoggedIn = isLoggedIn,
                                onRequestAuth = { onSuccess ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.OpenAuthGate(onSuccess))
                                }
                            )
                        }
                    }
                    Screen.ProductDetails -> {
                        val productDetailsScroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(productDetailsScroll)
                        ) {
                            com.sekota.screens.ProductDetailsScreen(
                                productCode = navState.selectedProductCode,
                                onNavigateBack = {
                                    container.navigationCoordinator.processIntent(
                                        NavigationIntent.TargetLandingSection(NavbarActiveSection.PRODUK)
                                    )
                                },
                                onConsultationClick = {
                                    container.navigationCoordinator.processIntent(NavigationIntent.RequestConsultation())
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
                                onNavigate = { screen ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(screen))
                                },
                                isLoggedIn = isLoggedIn,
                                onRequestAuth = { onSuccess ->
                                    container.navigationCoordinator.processIntent(NavigationIntent.OpenAuthGate(onSuccess))
                                },
                                onConsultationClick = {
                                    container.navigationCoordinator.processIntent(NavigationIntent.RequestConsultation())
                                }
                            )
                        }
                    }
                    Screen.Login -> LoginScreen(
                        loginUseCase = loginUseCase,
                        onLoginSuccess = { 
                            isLoggedIn = true
                            container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(Screen.Catalog))
                        },
                        onNavigateToSignup = {
                            container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(Screen.Signup))
                        }
                    )
                    Screen.Signup -> SignupScreen(
                        signupUseCase = signupUseCase,
                        updateProfileUseCase = updateProfileUseCase,
                        onSignupSuccess = { 
                            isLoggedIn = true
                            container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(Screen.Catalog))
                        },
                        onNavigateToLogin = {
                            container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(Screen.Login))
                        }
                    )
                    Screen.Profile -> ProfileScreen(
                        getProfileUseCase = getProfileUseCase,
                        updateProfileUseCase = updateProfileUseCase,
                        onLogout = {
                            clearTokenUseCase()
                            isLoggedIn = false
                            container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(Screen.Landing))
                        }
                    )
                    Screen.Admin -> {
                        AdminDashboardScreen()
                    }
                }
            }

            // Sticky Navbar (Material Design 3 with integrated status pill)
            Navbar(
                currentScreen = navState.currentScreen,
                activeLandingSection = navState.activeLandingSection,
                isLoggedIn = isLoggedIn,
                syncState = syncState,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(10f),
                onNavigate = { screen ->
                    container.navigationCoordinator.processIntent(NavigationIntent.NavigateTo(screen))
                },
                onSolusiClick = {
                    container.navigationCoordinator.processIntent(
                        NavigationIntent.TargetLandingSection(NavbarActiveSection.SOLUSI)
                    )
                },
                onProdukClick = {
                    container.navigationCoordinator.processIntent(
                        NavigationIntent.TargetLandingSection(NavbarActiveSection.PRODUK)
                    )
                },
                onKontakClick = {
                    container.navigationCoordinator.processIntent(
                        NavigationIntent.TargetLandingSection(NavbarActiveSection.KONTAK)
                    )
                },
                onConsultationClick = {
                    container.navigationCoordinator.processIntent(NavigationIntent.RequestConsultation())
                }
            )

            // UC-GATE-01: Inline Auth-Gating Modal Dialog
            if (navState.isAuthGateOpen) {
                com.sekota.components.AuthGateDialog(
                    loginUseCase = loginUseCase,
                    signupUseCase = signupUseCase,
                    onDismissRequest = {
                        container.navigationCoordinator.processIntent(NavigationIntent.CloseAuthGate)
                    },
                    onAuthSuccess = {
                        isLoggedIn = true
                        val pending = navState.pendingAction
                        container.navigationCoordinator.processIntent(NavigationIntent.CloseAuthGate)
                        pending?.invoke()
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
