package com.sekota.navigation

import com.sekota.Screen
import com.sekota.features.navigation.domain.model.AppDestination
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.features.navigation.domain.model.NavigationRouter
import com.sekota.localization.LocalizationManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * MVI navigation coordinator (WEB-07-03-01 / #22668). The UI only emits intents;
 * this class owns [NavigationState] and sends one-off [NavigationEffect]s such as
 * "scroll this section into view" or "open this URL".
 */
class NavigationCoordinator {

    private val _state = MutableStateFlow(NavigationState(locale = LocalizationManager.currentLocale.value))
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    private val _effects = Channel<NavigationEffect>(Channel.BUFFERED)
    val effects: Flow<NavigationEffect> = _effects.receiveAsFlow()

    fun processIntent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.OpenRoute -> navigate(NavigationRouter.parseDestination(intent.route))
            is NavigationIntent.Navigate -> navigate(intent.destination)
            is NavigationIntent.SetLocale -> {
                LocalizationManager.setLocale(intent.locale)
                _state.update { it.copy(locale = intent.locale) }
            }
            is NavigationIntent.ScrollPositionChanged -> updateActiveAnchor(intent)
        }
    }

    private fun navigate(destination: AppDestination) {
        when (destination) {
            AppDestination.Landing -> {
                _state.update { it.copy(currentScreen = Screen.Landing, activeAnchor = null) }
                _effects.trySend(NavigationEffect.ScrollToTop)
            }
            is AppDestination.Section -> {
                _state.update { it.copy(currentScreen = Screen.Landing, activeAnchor = destination.anchor) }
                _effects.trySend(NavigationEffect.ScrollToAnchor(destination.anchor))
            }
            is AppDestination.Product -> openPage(Screen.Product(destination.slug))
            is AppDestination.Books -> openPage(Screen.Books(destination.bookSlug))
            is AppDestination.Contact -> openPage(Screen.Contact(destination.anchor))
            AppDestination.Admin -> openPage(Screen.Admin)
            is AppDestination.External -> _effects.trySend(NavigationEffect.OpenExternal(destination.url))
            is AppDestination.Unknown -> println("NavigationCoordinator: ignoring unknown route '${destination.raw}'")
        }
    }

    private fun openPage(screen: Screen) {
        _state.update { it.copy(currentScreen = screen, activeAnchor = null) }
        _effects.trySend(NavigationEffect.ScrollToTop)
    }

    private fun updateActiveAnchor(intent: NavigationIntent.ScrollPositionChanged) {
        if (_state.value.currentScreen != Screen.Landing) return
        val active = activeAnchorFor(intent.scrollY, intent.anchorOffsets)
        if (_state.value.activeAnchor != active) _state.update { it.copy(activeAnchor = active) }
    }

    companion object {
        /** A section counts as active once its top is within this distance of the navbar. */
        const val ACTIVATION_OFFSET_PX = 160

        /** The last section (in page order) whose top has scrolled past the activation line. */
        fun activeAnchorFor(scrollY: Int, anchorOffsets: Map<LandingAnchor, Int>): LandingAnchor? =
            LandingAnchor.entries
                .filter { anchor -> anchorOffsets[anchor]?.let { it > 0 && scrollY + ACTIVATION_OFFSET_PX >= it } == true }
                .lastOrNull()
    }
}
