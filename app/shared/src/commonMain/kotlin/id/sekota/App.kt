package id.sekota

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import id.sekota.app.platform.WebKeyboard
import id.sekota.app.platform.prefersReducedMotion
import id.sekota.app.platform.setDocumentLanguage
import id.sekota.core.designsystem.LocalReducedMotion
import kotlinx.coroutines.launch
import id.sekota.app.AppState
import id.sekota.app.chrome.SiteHeader
import id.sekota.app.di.appModule
import id.sekota.app.navigation.Route
import id.sekota.app.navigation.encodeHash
import id.sekota.app.navigation.parseHash
import id.sekota.app.platform.HashRouting
import id.sekota.core.designsystem.SekotaColors
import id.sekota.core.designsystem.SekotaTheme
import id.sekota.core.ui.ProvideWindowSize
import id.sekota.feature.books.presentation.BooksScreen
import id.sekota.feature.contact.presentation.ContactScreen
import id.sekota.feature.landing.presentation.LandingScreen
import id.sekota.feature.legal.domain.LegalDocType
import id.sekota.feature.legal.presentation.LegalScreen
import id.sekota.feature.product.presentation.ProductDetailScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        SekotaTheme {
            ProvideWindowSize {
                val initial = remember { parseHash(HashRouting.read()) }
                val appState = remember {
                    AppState(
                        initialLanguage = initial.language,
                        initialRoute = initial.route,
                        initialAnchor = initial.homeAnchor,
                    )
                }
                val route = appState.navigator.current
                val reducedMotion = remember { prefersReducedMotion() }
                val focusRequester = remember { FocusRequester() }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    // Wait for the layout pass so the root focus target exists.
                    androidx.compose.runtime.withFrameNanos { }
                    runCatching { focusRequester.requestFocus() }
                }
                LaunchedEffect(appState.language) {
                    setDocumentLanguage(appState.language.code)
                }

                // Web DOM fallback: Compose canvas focus is inconsistent across browsers.
                LaunchedEffect(Unit) {
                    WebKeyboard.install { action ->
                        val list = appState.activeListState ?: return@install
                        val page = (list.layoutInfo.viewportSize.height * 0.85f)
                            .toInt().coerceAtLeast(120).toFloat()
                        scope.launch {
                            when (action) {
                                "pagedown", "space" -> list.animateScrollBy(page)
                                "pageup" -> list.animateScrollBy(-page)
                                "down" -> list.animateScrollBy(120f)
                                "up" -> list.animateScrollBy(-120f)
                                "home" -> list.animateScrollToItem(0)
                                "end" -> {
                                    val last = (list.layoutInfo.totalItemsCount - 1).coerceAtLeast(0)
                                    list.animateScrollToItem(last)
                                }
                            }
                        }
                    }
                }

                // Browser back/forward and manual hash edits.
                LaunchedEffect(Unit) {
                    HashRouting.listen { hash ->
                        appState.applyHash(parseHash(hash))
                    }
                }

                // Keep the URL in sync with the in-app router and language.
                LaunchedEffect(
                    appState.language,
                    appState.navigator.current,
                    appState.navigator.homeAnchor,
                ) {
                    val hash = encodeHash(
                        appState.language,
                        appState.navigator.current,
                        appState.navigator.homeAnchor,
                    )
                    if (HashRouting.read() != null || hash != "#/id") {
                        HashRouting.write(hash)
                    }
                }

                CompositionLocalProvider(LocalReducedMotion provides reducedMotion) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(SekotaColors.Bg)
                        .focusRequester(focusRequester)
                        .focusable()
                        .onKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                            val list = appState.activeListState ?: return@onKeyEvent false
                            val page = (list.layoutInfo.viewportSize.height * 0.85f)
                                .toInt().coerceAtLeast(120).toFloat()
                            when (event.key) {
                                Key.PageDown, Key.Spacebar -> {
                                    scope.launch { list.animateScrollBy(page) }
                                    true
                                }
                                Key.PageUp -> {
                                    scope.launch { list.animateScrollBy(-page) }
                                    true
                                }
                                Key.DirectionDown -> {
                                    scope.launch { list.animateScrollBy(120f) }
                                    true
                                }
                                Key.DirectionUp -> {
                                    scope.launch { list.animateScrollBy(-120f) }
                                    true
                                }
                                Key.MoveHome -> {
                                    scope.launch { list.animateScrollToItem(0) }
                                    true
                                }
                                Key.MoveEnd -> {
                                    val last = (list.layoutInfo.totalItemsCount - 1).coerceAtLeast(0)
                                    scope.launch { list.animateScrollToItem(last) }
                                    true
                                }
                                else -> false
                            }
                        },
                ) {
                    when (route) {
                        is Route.Home -> LandingScreen(
                            appState = appState,
                            onProduct = { appState.navigator.navigate(Route.Product(it)) },
                            onBook = { appState.navigator.navigate(Route.Books(it)) },
                            onContact = { preset -> appState.navigator.navigate(Route.Contact(preset)) },
                        )

                        is Route.Product -> key(route.slug) {
                            ProductDetailScreen(
                                appState = appState,
                                slug = route.slug,
                                onProduct = { appState.navigator.navigate(Route.Product(it)) },
                                onContact = { appState.navigator.navigate(Route.Contact(it)) },
                                onHomeSection = { appState.navigator.navigateHome(it) },
                            )
                        }

                        is Route.Books -> key(route) {
                            BooksScreen(
                                appState = appState,
                                initialAnchor = route.anchor,
                                onContact = { preset -> appState.navigator.navigate(Route.Contact(preset)) },
                            )
                        }

                        is Route.Contact -> key(route.preset) {
                            ContactScreen(
                                appState = appState,
                                initialPreset = route.preset,
                                page = HashRouting.read() ?: "",
                            )
                        }

                        Route.Privacy -> key(route) {
                            LegalScreen(appState = appState, type = LegalDocType.Privacy)
                        }

                        Route.Terms -> key(route) {
                            LegalScreen(appState = appState, type = LegalDocType.Terms)
                        }
                    }

                    SiteHeader(
                        appState = appState,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
                }
            }
        }
    }
}
