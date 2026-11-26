package id.sekota.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Stack-based router with browser-hash support. `pendingHomeAnchor` is a
 * one-shot scroll request; `homeAnchor` stays in the URL so a deep link such as
 * `#/id/home/layanan` can be shared and restored.
 */
@Stable
class AppNavigator(start: Route = Route.Home, startAnchor: String? = null) {
    private val backStack = mutableStateListOf(start)

    val current: Route get() = backStack.last()
    val canGoBack: Boolean get() = backStack.size > 1

    var pendingHomeAnchor by mutableStateOf(startAnchor)
        private set

    var homeAnchor by mutableStateOf(startAnchor)
        private set

    fun navigate(route: Route) {
        if (route == current) return
        backStack.add(route)
        if (route !is Route.Home) {
            pendingHomeAnchor = null
            homeAnchor = null
        }
    }

    fun navigateHome(anchor: String? = null) {
        if (current !is Route.Home) {
            val homeIndex = backStack.indexOfFirst { it is Route.Home }
            if (homeIndex >= 0) {
                while (backStack.lastIndex > homeIndex) backStack.removeAt(backStack.lastIndex)
            } else {
                backStack.add(Route.Home)
            }
        }
        val target = if (anchor.isNullOrBlank()) "hero" else anchor
        pendingHomeAnchor = target
        homeAnchor = target
    }

    fun consumeAnchor() {
        pendingHomeAnchor = null
    }

    /** Applies a hashchange/browser-back event without writing the hash again. */
    fun applyExternal(route: Route, anchor: String?) {
        backStack.clear()
        backStack.add(route)
        if (route is Route.Home) {
            val target = if (anchor.isNullOrBlank()) "hero" else anchor
            pendingHomeAnchor = target
            homeAnchor = target
        } else {
            pendingHomeAnchor = null
            homeAnchor = null
        }
    }

    fun back() {
        if (canGoBack) backStack.removeAt(backStack.lastIndex)
        pendingHomeAnchor = null
        homeAnchor = if (current is Route.Home) "hero" else null
    }
}
