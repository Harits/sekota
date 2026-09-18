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
    var selectedBookId by remember { mutableStateOf<String?>("blind-spot-radar") }
    
    var isLoggedIn by remember { mutableStateOf(getTokenUseCase() != null) }
    var showAuthGateDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val landingScroll = rememberScrollState()
    var activeLandingSection by remember { mutableStateOf(NavbarActiveSection.SOLUSI) }
    var solusiOffsetY by remember { mutableStateOf(0) }
    var produkOffsetY by remember { mutableStateOf(0) }
    var kontakOffsetY by remember { mutableStateOf(0) }

    var selectedProductCode by remember { mutableStateOf("VRD") }
    var pendingLandingSection by remember { mutableStateOf<NavbarActiveSection?>(null) }

    // Deferred scroll effect: When navigating to LandingScreen from other screens,
    // wait for LandingScreen to mount and scroll smoothly to the target section.
    LaunchedEffect(currentScreen, pendingLandingSection) {
        if (currentScreen == Screen.Landing && pendingLandingSection != null) {
            val section = pendingLandingSection
            pendingLandingSection = null
            kotlinx.coroutines.delay(100) // Brief frame delay for layout measurement
            when (section) {
                NavbarActiveSection.SOLUSI -> {
                    val targetY = if (solusiOffsetY > 0) solusiOffsetY else 0
                    landingScroll.animateScrollTo(targetY)
                }
                NavbarActiveSection.PRODUK -> {
                    val targetY = if (produkOffsetY > 0) produkOffsetY else 1100
                    landingScroll.animateScrollTo(targetY)
                }
                NavbarActiveSection.KONTAK -> {
                    val targetY = if (kontakOffsetY > 0) kontakOffsetY else landingScroll.maxValue
                    landingScroll.animateScrollTo(targetY)
                }
                NavbarActiveSection.NONE, null -> {}
            }
        }
    }

    // Synchronize active nav pill based on scroll position while on LandingScreen
    LaunchedEffect(landingScroll.value, currentScreen) {
        if (currentScreen == Screen.Landing) {
            val scrollY = landingScroll.value
            activeLandingSection = when {
                kontakOffsetY > 0 && scrollY >= kontakOffsetY - 250 -> NavbarActiveSection.KONTAK
                produkOffsetY > 0 && scrollY >= produkOffsetY - 250 -> NavbarActiveSection.PRODUK
                else -> NavbarActiveSection.SOLUSI
            }
        }
    }

    LaunchedEffect(Unit) {
        syncService.connect(coroutineScope)
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
                when (currentScreen) {
                    Screen.Landing -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(landingScroll)
                        ) {
                            LandingScreen(
                                onNavigate = { screen -> currentScreen = screen },
                                onProductClick = { code ->
                                    selectedProductCode = code
                                    currentScreen = Screen.ProductDetails
                                },
                                onBookClick = { bookId ->
                                    selectedBookId = bookId
                                    currentScreen = Screen.Details
                                },
                                onConsultationClick = {
                                    activeLandingSection = NavbarActiveSection.KONTAK
                                    coroutineScope.launch {
                                        val targetY = if (kontakOffsetY > 0) kontakOffsetY else landingScroll.maxValue
                                        landingScroll.animateScrollTo(targetY)
                                    }
                                },
                                onSolusiPositioned = { y -> solusiOffsetY = y },
                                onProdukPositioned = { y -> produkOffsetY = y },
                                onKontakPositioned = { y -> kontakOffsetY = y }
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
                                    selectedBookId = bookId
                                    currentScreen = Screen.Details
                                },
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
                                bookId = selectedBookId,
                                isLoggedIn = isLoggedIn,
                                onRequestAuth = { onSuccess ->
                                    pendingAction = onSuccess
                                    showAuthGateDialog = true
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
                                productCode = selectedProductCode,
                                onNavigateBack = {
                                    currentScreen = Screen.Landing
                                    pendingLandingSection = NavbarActiveSection.PRODUK
                                },
                                onConsultationClick = {
                                    currentScreen = Screen.Landing
                                    pendingLandingSection = NavbarActiveSection.KONTAK
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

            // Sticky Navbar (Material Design 3 with integrated status pill)
            Navbar(
                currentScreen = currentScreen,
                activeLandingSection = activeLandingSection,
                isLoggedIn = isLoggedIn,
                syncState = syncState,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(10f),
                onNavigate = { screen -> currentScreen = screen },
                onSolusiClick = {
                    activeLandingSection = NavbarActiveSection.SOLUSI
                    if (currentScreen != Screen.Landing) {
                        pendingLandingSection = NavbarActiveSection.SOLUSI
                        currentScreen = Screen.Landing
                    } else {
                        coroutineScope.launch {
                            val targetY = if (solusiOffsetY > 0) solusiOffsetY else 0
                            landingScroll.animateScrollTo(targetY)
                        }
                    }
                },
                onProdukClick = {
                    activeLandingSection = NavbarActiveSection.PRODUK
                    if (currentScreen != Screen.Landing) {
                        pendingLandingSection = NavbarActiveSection.PRODUK
                        currentScreen = Screen.Landing
                    } else {
                        coroutineScope.launch {
                            val targetY = if (produkOffsetY > 0) produkOffsetY else 1100
                            landingScroll.animateScrollTo(targetY)
                        }
                    }
                },
                onKontakClick = {
                    activeLandingSection = NavbarActiveSection.KONTAK
                    if (currentScreen != Screen.Landing) {
                        pendingLandingSection = NavbarActiveSection.KONTAK
                        currentScreen = Screen.Landing
                    } else {
                        coroutineScope.launch {
                            val targetY = if (kontakOffsetY > 0) kontakOffsetY else landingScroll.maxValue
                            landingScroll.animateScrollTo(targetY)
                        }
                    }
                },
                onConsultationClick = {
                    activeLandingSection = NavbarActiveSection.KONTAK
                    if (currentScreen != Screen.Landing) {
                        pendingLandingSection = NavbarActiveSection.KONTAK
                        currentScreen = Screen.Landing
                    } else {
                        coroutineScope.launch {
                            val targetY = if (kontakOffsetY > 0) kontakOffsetY else landingScroll.maxValue
                            landingScroll.animateScrollTo(targetY)
                        }
                    }
                }
            )

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
