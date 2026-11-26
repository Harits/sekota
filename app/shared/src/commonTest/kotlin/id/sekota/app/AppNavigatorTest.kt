package id.sekota.app

import id.sekota.app.navigation.AppNavigator
import id.sekota.app.navigation.Route
import id.sekota.core.model.ProductSlug
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppNavigatorTest {

    @Test
    fun startsAtHome() {
        val nav = AppNavigator()
        assertEquals(Route.Home, nav.current)
        assertFalse(nav.canGoBack)
    }

    @Test
    fun navigatePushesAndBackPops() {
        val nav = AppNavigator()
        nav.navigate(Route.Product(ProductSlug.Clarivio))
        assertEquals(Route.Product(ProductSlug.Clarivio), nav.current)
        assertTrue(nav.canGoBack)
        nav.back()
        assertEquals(Route.Home, nav.current)
    }

    @Test
    fun heroAnchorIsExposedOnce() {
        val nav = AppNavigator()
        nav.navigateHome("layanan")
        assertEquals("layanan", nav.pendingHomeAnchor)
        nav.consumeAnchor()
        assertNull(nav.pendingHomeAnchor)
        assertEquals("layanan", nav.homeAnchor)
    }

    @Test
    fun homeNavigationFromProductReturnsToHome() {
        val nav = AppNavigator()
        nav.navigate(Route.Contact())
        nav.navigateHome("platform")
        assertEquals(Route.Home, nav.current)
        assertEquals("platform", nav.pendingHomeAnchor)
    }

    @Test
    fun navigateHomePopsBackToExistingHome() {
        val nav = AppNavigator()
        nav.navigate(Route.Product(ProductSlug.Clarivio))
        nav.navigate(Route.Product(ProductSlug.Ascendio))
        nav.navigateHome("faq")
        assertEquals(Route.Home, nav.current)
        assertEquals("faq", nav.pendingHomeAnchor)
        assertFalse(nav.canGoBack)
    }

    @Test
    fun externalHashApplicationReplacesStack() {
        val nav = AppNavigator()
        nav.navigate(Route.Contact())
        nav.applyExternal(Route.Books(), null)
        assertEquals(Route.Books(), nav.current)
        assertFalse(nav.canGoBack)
        assertNull(nav.pendingHomeAnchor)
    }

    @Test
    fun duplicateRouteIsIgnored() {
        val nav = AppNavigator()
        nav.navigate(Route.Books())
        nav.navigate(Route.Books())
        nav.back()
        assertEquals(Route.Home, nav.current)
        assertFalse(nav.canGoBack)
    }
}
