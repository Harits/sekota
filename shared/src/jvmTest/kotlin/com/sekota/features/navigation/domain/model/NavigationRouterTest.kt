package com.sekota.features.navigation.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

/** WEB-07-03-01 / #22668 and WEB-07-03-04 / #22671 */
class NavigationRouterTest {

    @Test
    fun parsesEveryLandingSectionAnchor() {
        LandingAnchor.entries.forEach { anchor ->
            assertEquals(AppDestination.Section(anchor), NavigationRouter.parseDestination("#${anchor.id}"))
            assertEquals(AppDestination.Section(anchor), NavigationRouter.parseDestination(anchor.id))
        }
        assertEquals(AppDestination.Section(LandingAnchor.CARA_KERJA), NavigationRouter.parseDestination("index.html#cara-kerja"))
    }

    @Test
    fun parsesProductRoutesCodesAndDesignFileNames() {
        assertEquals(AppDestination.Product("clarivio"), NavigationRouter.parseDestination("/produk/clarivio"))
        assertEquals(AppDestination.Product("evidara"), NavigationRouter.parseDestination("evidara.html"))
        assertEquals(AppDestination.Product("sociara"), NavigationRouter.parseDestination("/sociara"))
        assertEquals(AppDestination.Product("ascendio"), NavigationRouter.parseDestination("ASC"))
        assertEquals(AppDestination.Product("clarivio"), NavigationRouter.parseDestination("CLV"))
    }

    @Test
    fun parsesBooksAndContactWithAnchors() {
        assertEquals(AppDestination.Books(), NavigationRouter.parseDestination("/buku"))
        assertEquals(AppDestination.Books("blind-spot-radar"), NavigationRouter.parseDestination("buku.html#blind-spot-radar"))
        assertEquals(AppDestination.Books("accountable-esg"), NavigationRouter.parseDestination("/buku#accountable-esg"))
        assertEquals(AppDestination.Contact(), NavigationRouter.parseDestination("/kontak"))
        assertEquals(AppDestination.Contact("mitra-awal"), NavigationRouter.parseDestination("/kontak#mitra-awal"))
        assertEquals(AppDestination.Contact(), NavigationRouter.parseDestination("CONTACT"))
    }

    @Test
    fun legacyLinksFromTheSprint6DesignStillResolve() {
        assertEquals(AppDestination.Section(LandingAnchor.CARA_KERJA), NavigationRouter.parseDestination("#solusi"))
        assertEquals(AppDestination.Section(LandingAnchor.PLATFORM), NavigationRouter.parseDestination("#produk"))
        assertEquals(AppDestination.Books(), NavigationRouter.parseDestination("/catalog"))
        assertEquals(AppDestination.Books(), NavigationRouter.parseDestination("EBOOK"))
        assertEquals(AppDestination.Section(LandingAnchor.PLATFORM), NavigationRouter.parseDestination("VRD"))
        assertEquals(AppDestination.Section(LandingAnchor.PLATFORM), NavigationRouter.parseDestination("ECO"))
        assertEquals(AppDestination.Section(LandingAnchor.TENTANG), NavigationRouter.parseDestination("ABOUT"))
    }

    @Test
    fun retiredFeaturesLandOnTheHomePage() {
        listOf("/merchandise", "MERCH", "/login", "AUTH", "/signup", "/profile", "/reader/blind-spot-radar", "/ebook/x/baca")
            .forEach { assertEquals(AppDestination.Landing, NavigationRouter.parseDestination(it), it) }
    }

    @Test
    fun externalAdminAndUnknown() {
        assertEquals(AppDestination.External("https://sekota.id"), NavigationRouter.parseDestination("https://sekota.id"))
        assertEquals(AppDestination.External("mailto:info@sekota.id"), NavigationRouter.parseDestination("mailto:info@sekota.id"))
        assertEquals(AppDestination.Admin, NavigationRouter.parseDestination("/admin"))
        assertEquals(AppDestination.Landing, NavigationRouter.parseDestination("/"))
        assertEquals(AppDestination.Unknown("PRIVACY"), NavigationRouter.parseDestination("PRIVACY"))
    }

    @Test
    fun routesRoundTrip() {
        val destinations = listOf(
            AppDestination.Landing,
            AppDestination.Section(LandingAnchor.FAQ),
            AppDestination.Product("evidara"),
            AppDestination.Books(),
            AppDestination.Books("forgetting-enterprise"),
            AppDestination.Contact(),
            AppDestination.Contact("mitra-awal"),
            AppDestination.Admin
        )
        destinations.forEach { assertEquals(it, NavigationRouter.parseDestination(NavigationRouter.getRoute(it))) }
        assertEquals("/produk/clarivio", NavigationRouter.getRoute(AppDestination.Product("clarivio")))
        assertEquals("/kontak#mitra-awal", NavigationRouter.getRoute(AppDestination.Contact("mitra-awal")))
    }

    @Test
    fun canonicalNavigationAndFooterRoutesAllResolve() {
        NavigationItem.CANONICAL_ITEMS.forEach {
            assertEquals(true, NavigationRouter.parseDestination(it.route) is AppDestination.Section, it.route)
        }
        val footer = com.sekota.features.admin.domain.model.FooterConfig()
        (footer.productColumn.items + footer.companyColumn.items).forEach {
            assertEquals(false, NavigationRouter.parseDestination(it.urlOrCode) is AppDestination.Unknown, it.urlOrCode)
        }
        assertEquals(AppDestination.Contact("mitra-awal"), NavigationRouter.parseDestination(com.sekota.features.admin.data.seed.CompanyProfileSeed.partnerCta.ctaRoute))
    }
}
