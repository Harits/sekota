package com.sekota.navigation

import com.sekota.NavbarActiveSection
import com.sekota.Screen

/**
 * Model-View-Intent (MVI) Contract for Sekota Web Presentation.
 */

data class NavigationState(
    val currentScreen: Screen = Screen.Landing,
    val activeLandingSection: NavbarActiveSection = NavbarActiveSection.SOLUSI,
    val selectedProductCode: String = "VRD",
    val selectedBookId: String? = "blind-spot-radar",
    val isAuthGateOpen: Boolean = false,
    val pendingAction: (() -> Unit)? = null
)

sealed interface NavigationIntent {
    data class NavigateTo(val screen: Screen) : NavigationIntent
    data class TargetLandingSection(val section: NavbarActiveSection) : NavigationIntent
    data class RequestConsultation(val fromScreen: Screen? = null) : NavigationIntent
    data class SelectProduct(val productCode: String) : NavigationIntent
    data class SelectBook(val bookId: String) : NavigationIntent
    data class OpenAuthGate(val onAuthenticated: () -> Unit) : NavigationIntent
    data object CloseAuthGate : NavigationIntent
    data class ScrollPositionChanged(val scrollY: Int, val produkOffsetY: Int, val kontakOffsetY: Int) : NavigationIntent
}

sealed interface NavigationEffect {
    data class BringSectionIntoView(val section: NavbarActiveSection) : NavigationEffect
}
