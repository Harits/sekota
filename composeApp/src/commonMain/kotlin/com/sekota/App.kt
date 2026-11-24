package com.sekota

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sekota.di.AppContainer
import com.sekota.screens.AdminDashboardScreen
import com.sekota.features.companyprofile.presentation.*
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.features.navigation.domain.model.NavigationItem
import com.sekota.localization.LocalLocalization
import com.sekota.localization.LocalizationManager
import com.sekota.navigation.NavigationEffect
import com.sekota.navigation.NavigationIntent
import com.sekota.ui.brand.BrandColors
import com.sekota.ui.brand.BrandTheme
import kotlinx.coroutines.delay

// Default AppContainer singleton instance
val defaultAppContainer by lazy { AppContainer() }

// Accessors used by the CMS workbench, admin login and desktop/Android entry points.
val syncService get() = defaultAppContainer.syncService
val tokenStorage get() = defaultAppContainer.tokenStorage
val authRepository get() = defaultAppContainer.authRepository
val loginUseCase get() = defaultAppContainer.loginUseCase
val getTokenUseCase get() = defaultAppContainer.getTokenUseCase
val clearTokenUseCase get() = defaultAppContainer.clearTokenUseCase
val getValueLoopUseCase get() = defaultAppContainer.getValueLoopUseCase
val saveValueLoopUseCase get() = defaultAppContainer.saveValueLoopUseCase
val getWhySekotaUseCase get() = defaultAppContainer.getWhySekotaUseCase
val saveWhySekotaUseCase get() = defaultAppContainer.saveWhySekotaUseCase
val getStrategicConversationUseCase get() = defaultAppContainer.getStrategicConversationUseCase
val saveStrategicConversationUseCase get() = defaultAppContainer.saveStrategicConversationUseCase
val getFooterUseCase get() = defaultAppContainer.getFooterUseCase
val saveFooterUseCase get() = defaultAppContainer.saveFooterUseCase
val getNavigationItemsUseCase get() = defaultAppContainer.getNavigationItemsUseCase
val saveNavigationItemsUseCase get() = defaultAppContainer.saveNavigationItemsUseCase

/**
 * Web Sekota root (Sprint 7 company profile).
 *
 * MVI flow: UI emits NavigationIntent → NavigationCoordinator updates
 * NavigationState and sends NavigationEffect; this function renders the state and
 * performs the effects (scroll a section tag into view, scroll to top, open a URL).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App(container: AppContainer = remember { defaultAppContainer }) {
    val coroutineScope = rememberCoroutineScope()
    val coordinator = container.navigationCoordinator
    val navState by coordinator.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    var navItems by remember { mutableStateOf(NavigationItem.CANONICAL_ITEMS) }
    LaunchedEffect(container.getNavigationItemsUseCase) {
        runCatching { navItems = container.getNavigationItemsUseCase() }
        container.syncService.syncEventFlow.collect {
            runCatching { navItems = container.getNavigationItemsUseCase() }
        }
    }

    val profileViewModel = viewModel {
        CompanyProfileViewModel(container.getCompanyProfileUseCase, container.syncService.syncEventFlow)
    }
    val profileState by profileViewModel.state.collectAsState()

    val landingScroll = rememberScrollState()
    val pageScroll = rememberScrollState()
    val requesters = remember { LandingAnchor.entries.associateWith { BringIntoViewRequester() } }
    val anchorOffsets = remember { mutableStateMapOf<LandingAnchor, Int>() }
    var viewportHeightPx by remember { mutableStateOf(0f) }

    LaunchedEffect(coordinator) {
        coordinator.effects.collect { effect ->
            when (effect) {
                is NavigationEffect.ScrollToAnchor -> {
                    delay(80) // let the landing page lay out after a page switch
                    // A viewport-tall rect pins the section's top to the top of the screen.
                    val pinned = Rect(0f, 0f, 1f, viewportHeightPx.coerceAtLeast(1f))
                    runCatching { requesters.getValue(effect.anchor).bringIntoView(pinned) }
                        .onFailure { anchorOffsets[effect.anchor]?.let { landingScroll.animateScrollTo(it) } }
                }
                NavigationEffect.ScrollToTop -> {
                    pageScroll.scrollTo(0)
                    landingScroll.animateScrollTo(0)
                }
                is NavigationEffect.OpenExternal -> runCatching { uriHandler.openUri(effect.url) }
                    .onFailure { println("App: cannot open ${effect.url}: ${it.message}") }
            }
        }
    }

    LaunchedEffect(landingScroll.value, navState.currentScreen) {
        if (navState.currentScreen == Screen.Landing) {
            coordinator.processIntent(NavigationIntent.ScrollPositionChanged(landingScroll.value, anchorOffsets.toMap()))
        }
    }

    LaunchedEffect(Unit) { container.syncService.connect(coroutineScope) }

    CompositionLocalProvider(LocalLocalization provides LocalizationManager) {
        SekotaTheme {
            BrandTheme {
                BoxWithConstraints(Modifier.fillMaxSize().background(BrandColors.Background)) {
                    viewportHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
                    val navInset = navbarHeightFor(maxWidth)
                    val onRoute: (String) -> Unit = { coordinator.processIntent(NavigationIntent.OpenRoute(it)) }
                    val locale = navState.locale
                    val loaded = profileState as? CompanyProfileUiState.Loaded

                    when (val screen = navState.currentScreen) {
                        Screen.Landing -> Column(Modifier.fillMaxSize().verticalScroll(landingScroll)) {
                            CompanyProfileScreen(
                                state = profileState,
                                locale = locale,
                                onIntent = profileViewModel::onIntent,
                                onRoute = onRoute,
                                navInset = navInset,
                                anchorRequesters = requesters,
                                onAnchorPositioned = { anchor, y -> anchorOffsets[anchor] = y }
                            )
                        }
                        is Screen.Product, is Screen.Books, is Screen.Contact -> Column(Modifier.fillMaxSize().verticalScroll(pageScroll)) {
                            if (loaded == null) {
                                CompanyProfileScreen(profileState, locale, profileViewModel::onIntent, onRoute, navInset = navInset)
                            } else when (screen) {
                                is Screen.Product -> ProductPreviewPage(screen.slug, loaded.content, locale, onRoute, navInset)
                                is Screen.Books -> BooksPreviewPage(screen.bookSlug, loaded.content, loaded.booksLoading, locale, onRoute, navInset)
                                is Screen.Contact -> ContactPage(loaded.content, locale, onRoute, navInset)
                                else -> Unit
                            }
                        }
                        Screen.Admin -> AdminDashboardScreen()
                    }

                    if (navState.currentScreen != Screen.Admin) {
                        Navbar(
                            items = navItems,
                            locale = locale,
                            activeAnchor = navState.activeAnchor,
                            solid = navState.currentScreen != Screen.Landing || landingScroll.value > 8,
                            onRoute = onRoute,
                            onLocaleChange = { coordinator.processIntent(NavigationIntent.SetLocale(it)) },
                            onContactClick = { onRoute("/kontak") },
                            modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = DESKTOP)
@Composable
fun AppPreview() {
    App()
}
