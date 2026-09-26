package com.sekota.navigation

import com.sekota.NavbarActiveSection
import com.sekota.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * MVI Navigation Coordinator managing state and one-off side effects.
 * Injected through AppContainer to decouple UI screens from direct state mutation and drilling.
 */
class NavigationCoordinator {

    private val _state = MutableStateFlow(NavigationState())
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    private val _effects = Channel<NavigationEffect>(Channel.BUFFERED)
    val effects: Flow<NavigationEffect> = _effects.receiveAsFlow()

    fun processIntent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.NavigateTo -> {
                _state.update { it.copy(currentScreen = intent.screen) }
            }
            is NavigationIntent.TargetLandingSection -> {
                val previousScreen = _state.value.currentScreen
                _state.update {
                    it.copy(
                        currentScreen = Screen.Landing,
                        activeLandingSection = intent.section
                    )
                }
                _effects.trySend(NavigationEffect.BringSectionIntoView(intent.section))
            }
            is NavigationIntent.RequestConsultation -> {
                _state.update {
                    it.copy(
                        currentScreen = Screen.Landing,
                        activeLandingSection = NavbarActiveSection.KONTAK
                    )
                }
                _effects.trySend(NavigationEffect.BringSectionIntoView(NavbarActiveSection.KONTAK))
            }
            is NavigationIntent.SelectProduct -> {
                _state.update {
                    it.copy(
                        selectedProductCode = intent.productCode,
                        currentScreen = Screen.ProductDetails
                    )
                }
            }
            is NavigationIntent.SelectBook -> {
                _state.update {
                    it.copy(
                        selectedBookId = intent.bookId,
                        currentScreen = Screen.Details
                    )
                }
            }
            is NavigationIntent.OpenAuthGate -> {
                _state.update {
                    it.copy(
                        isAuthGateOpen = true,
                        pendingAction = intent.onAuthenticated
                    )
                }
            }
            is NavigationIntent.CloseAuthGate -> {
                _state.update {
                    it.copy(
                        isAuthGateOpen = false,
                        pendingAction = null
                    )
                }
            }
            is NavigationIntent.ScrollPositionChanged -> {
                if (_state.value.currentScreen == Screen.Landing) {
                    val newSection = when {
                        intent.kontakOffsetY > 0 && intent.scrollY >= intent.kontakOffsetY - 250 -> NavbarActiveSection.KONTAK
                        intent.produkOffsetY > 0 && intent.scrollY >= intent.produkOffsetY - 250 -> NavbarActiveSection.PRODUK
                        else -> NavbarActiveSection.SOLUSI
                    }
                    if (_state.value.activeLandingSection != newSection) {
                        _state.update { it.copy(activeLandingSection = newSection) }
                    }
                }
            }
        }
    }
}
