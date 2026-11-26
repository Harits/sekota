package id.sekota.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyListState
import id.sekota.app.navigation.AppNavigator
import id.sekota.app.navigation.HashTarget
import id.sekota.app.navigation.Route
import id.sekota.core.model.Language

/** App-wide state: language choice, router and the chrome scroll flag. */
@Stable
class AppState(
    initialLanguage: Language = Language.ID,
    initialRoute: Route = Route.Home,
    initialAnchor: String? = null,
) {
    var language by mutableStateOf(initialLanguage)
        private set

    var chromeSolid by mutableStateOf(false)

    var activeAnchor by mutableStateOf("hero")

    /** The list currently on screen; used by keyboard scrolling on web/desktop. */
    var activeListState: LazyListState? by mutableStateOf(null)

    val navigator = AppNavigator(initialRoute, initialAnchor)

    fun changeLanguage(language: Language) {
        this.language = language
    }

    fun toggleLanguage() {
        language = language.other()
    }

    fun applyHash(target: HashTarget) {
        language = target.language
        navigator.applyExternal(target.route, target.homeAnchor)
    }
}
