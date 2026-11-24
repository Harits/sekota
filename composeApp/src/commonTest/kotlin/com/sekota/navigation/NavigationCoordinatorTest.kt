package com.sekota.navigation

import com.sekota.Screen
import com.sekota.features.navigation.domain.model.AppDestination
import com.sekota.features.navigation.domain.model.LandingAnchor
import com.sekota.localization.AppLocale
import com.sekota.localization.LocalizationManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** WEB-07-03-04 / #22671 */
class NavigationCoordinatorTest {

    @AfterTest
    fun resetLocale() = LocalizationManager.setLocale(AppLocale.ID)

    @Test
    fun sectionRouteStaysOnLandingAndScrollsToTheSection() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.OpenRoute("#cara-kerja"))
        assertEquals(Screen.Landing, coordinator.state.value.currentScreen)
        assertEquals(LandingAnchor.CARA_KERJA, coordinator.state.value.activeAnchor)
        assertEquals(NavigationEffect.ScrollToAnchor(LandingAnchor.CARA_KERJA), coordinator.effects.first())
    }

    @Test
    fun pageRoutesOpenTheirScreensAndScrollToTop() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.OpenRoute("/produk/evidara"))
        assertEquals(Screen.Product("evidara"), coordinator.state.value.currentScreen)
        assertEquals(NavigationEffect.ScrollToTop, coordinator.effects.first())

        coordinator.processIntent(NavigationIntent.OpenRoute("/buku#blind-spot-radar"))
        assertEquals(Screen.Books("blind-spot-radar"), coordinator.state.value.currentScreen)

        coordinator.processIntent(NavigationIntent.OpenRoute("/kontak#mitra-awal"))
        assertEquals(Screen.Contact("mitra-awal"), coordinator.state.value.currentScreen)
        assertNull(coordinator.state.value.activeAnchor)
    }

    @Test
    fun sectionLinkFromAnotherPageReturnsToLanding() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.Navigate(AppDestination.Product("clarivio")))
        coordinator.processIntent(NavigationIntent.Navigate(AppDestination.Section(LandingAnchor.FAQ)))
        assertEquals(Screen.Landing, coordinator.state.value.currentScreen)
        assertEquals(LandingAnchor.FAQ, coordinator.state.value.activeAnchor)
    }

    @Test
    fun externalLinksAreHandedToThePlatform() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.OpenRoute("mailto:info@sekota.id"))
        assertEquals(Screen.Landing, coordinator.state.value.currentScreen)
        assertEquals(NavigationEffect.OpenExternal("mailto:info@sekota.id"), coordinator.effects.first())
    }

    @Test
    fun unknownAndRetiredRoutesNeverBreakNavigation() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.OpenRoute("/produk/sociara"))
        coordinator.processIntent(NavigationIntent.OpenRoute("PRIVACY")) // no page yet: ignored
        assertEquals(Screen.Product("sociara"), coordinator.state.value.currentScreen)
        coordinator.processIntent(NavigationIntent.OpenRoute("/merchandise")) // retired: home
        assertEquals(Screen.Landing, coordinator.state.value.currentScreen)
    }

    @Test
    fun languageSwitchKeepsTheCurrentPage() = runTest {
        val coordinator = NavigationCoordinator()
        coordinator.processIntent(NavigationIntent.OpenRoute("/produk/ascendio"))
        coordinator.processIntent(NavigationIntent.SetLocale(AppLocale.EN))
        assertEquals(Screen.Product("ascendio"), coordinator.state.value.currentScreen)
        assertEquals(AppLocale.EN, coordinator.state.value.locale)
        assertEquals(AppLocale.EN, LocalizationManager.currentLocale.value)
        assertEquals("ENGLISH", coordinator.state.value.selectedLanguage)
    }

    @Test
    fun activeAnchorFollowsScrollPosition() {
        val offsets = mapOf(
            LandingAnchor.TENTANG to 900, LandingAnchor.LAYANAN to 1500, LandingAnchor.CARA_KERJA to 2400,
            LandingAnchor.PLATFORM to 3100, LandingAnchor.WAWASAN to 4300, LandingAnchor.MITRA to 5200, LandingAnchor.FAQ to 5700
        )
        assertNull(NavigationCoordinator.activeAnchorFor(0, offsets))
        assertEquals(LandingAnchor.TENTANG, NavigationCoordinator.activeAnchorFor(800, offsets))
        assertEquals(LandingAnchor.LAYANAN, NavigationCoordinator.activeAnchorFor(1500, offsets))
        assertEquals(LandingAnchor.FAQ, NavigationCoordinator.activeAnchorFor(9000, offsets))
        // Sections not laid out yet (offset 0) are ignored.
        assertNull(NavigationCoordinator.activeAnchorFor(500, mapOf(LandingAnchor.TENTANG to 0)))
    }

    @Test
    fun scrollUpdatesActiveAnchorOnlyOnLanding() {
        val coordinator = NavigationCoordinator()
        val offsets = mapOf(LandingAnchor.TENTANG to 900, LandingAnchor.LAYANAN to 1500)
        coordinator.processIntent(NavigationIntent.ScrollPositionChanged(1500, offsets))
        assertEquals(LandingAnchor.LAYANAN, coordinator.state.value.activeAnchor)

        coordinator.processIntent(NavigationIntent.OpenRoute("/kontak"))
        coordinator.processIntent(NavigationIntent.ScrollPositionChanged(900, offsets))
        assertNull(coordinator.state.value.activeAnchor)
    }
}
