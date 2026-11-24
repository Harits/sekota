package com.sekota.navigation

import com.sekota.Screen
import com.sekota.features.navigation.domain.model.AppDestination
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.localization.AppLocale

/**
 * Model-View-Intent contract for Web Sekota navigation (WEB-07-03-01 / #22668).
 */
data class NavigationState(
    val currentScreen: Screen = Screen.Landing,
    /** Landing section currently in view; null while the hero is on screen. */
    val activeAnchor: LandingAnchor? = null,
    val locale: AppLocale = AppLocale.ID
) {
    /** Display name kept for the Sprint 6 screens that still take a language string. */
    val selectedLanguage: String get() = locale.displayName
}

sealed interface NavigationIntent {
    /** Go to a domain destination (section, page, external link). */
    data class Navigate(val destination: AppDestination) : NavigationIntent
    /** Go to a route or CMS link code; parsed by NavigationRouter. */
    data class OpenRoute(val route: String) : NavigationIntent
    data class SetLocale(val locale: AppLocale) : NavigationIntent
    /** Landing scroll position and the measured top offset of each section. */
    data class ScrollPositionChanged(val scrollY: Int, val anchorOffsets: Map<LandingAnchor, Int>) : NavigationIntent
}

sealed interface NavigationEffect {
    data class ScrollToAnchor(val anchor: LandingAnchor) : NavigationEffect
    data object ScrollToTop : NavigationEffect
    data class OpenExternal(val url: String) : NavigationEffect
}
