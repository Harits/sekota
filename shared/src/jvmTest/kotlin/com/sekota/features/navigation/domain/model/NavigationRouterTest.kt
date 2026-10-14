package com.sekota.features.navigation.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationRouterTest {

    @Test
    fun testParseSectionDestinations() {
        assertEquals(AppDestination.SolusiSection, NavigationRouter.parseDestination("#solusi"))
        assertEquals(AppDestination.SolusiSection, NavigationRouter.parseDestination("solusi"))
        assertEquals(AppDestination.ProdukSection, NavigationRouter.parseDestination("#produk"))
        assertEquals(AppDestination.ProdukSection, NavigationRouter.parseDestination("produk"))
        assertEquals(AppDestination.KontakSection, NavigationRouter.parseDestination("#kontak"))
        assertEquals(AppDestination.KontakSection, NavigationRouter.parseDestination("kontak"))
        assertEquals(AppDestination.KontakSection, NavigationRouter.parseDestination("CONTACT"))
    }

    @Test
    fun testParseCatalogAndMerchandiseDestinations() {
        assertEquals(AppDestination.Catalog, NavigationRouter.parseDestination("/catalog"))
        assertEquals(AppDestination.Catalog, NavigationRouter.parseDestination("catalog"))
        assertEquals(AppDestination.Catalog, NavigationRouter.parseDestination("#catalog"))
        assertEquals(AppDestination.Catalog, NavigationRouter.parseDestination("EBOOK"))

        assertEquals(AppDestination.Merchandise, NavigationRouter.parseDestination("/merchandise"))
        assertEquals(AppDestination.Merchandise, NavigationRouter.parseDestination("merchandise"))
        assertEquals(AppDestination.Merchandise, NavigationRouter.parseDestination("#merchandise"))
        assertEquals(AppDestination.Merchandise, NavigationRouter.parseDestination("MERCH"))
    }

    @Test
    fun testParseAuthAndProfileDestinations() {
        assertEquals(AppDestination.Login, NavigationRouter.parseDestination("/login"))
        assertEquals(AppDestination.Login, NavigationRouter.parseDestination("login"))
        assertEquals(AppDestination.Login, NavigationRouter.parseDestination("AUTH"))

        assertEquals(AppDestination.Signup, NavigationRouter.parseDestination("/signup"))
        assertEquals(AppDestination.Profile, NavigationRouter.parseDestination("/profile"))
        assertEquals(AppDestination.Admin, NavigationRouter.parseDestination("/admin"))
    }

    @Test
    fun testParseExternalDestinations() {
        val dest1 = NavigationRouter.parseDestination("https://sekota.id")
        assertTrue(dest1 is AppDestination.External)
        assertEquals("https://sekota.id", dest1.url)

        val dest2 = NavigationRouter.parseDestination("http://example.com/docs")
        assertTrue(dest2 is AppDestination.External)
        assertEquals("http://example.com/docs", dest2.url)
    }

    @Test
    fun testGetRouteMappings() {
        assertEquals("/", NavigationRouter.getRoute(AppDestination.Landing))
        assertEquals("#solusi", NavigationRouter.getRoute(AppDestination.SolusiSection))
        assertEquals("#produk", NavigationRouter.getRoute(AppDestination.ProdukSection))
        assertEquals("#kontak", NavigationRouter.getRoute(AppDestination.KontakSection))
        assertEquals("/catalog", NavigationRouter.getRoute(AppDestination.Catalog))
        assertEquals("/merchandise", NavigationRouter.getRoute(AppDestination.Merchandise))
        assertEquals("/login", NavigationRouter.getRoute(AppDestination.Login))
        assertEquals("/signup", NavigationRouter.getRoute(AppDestination.Signup))
        assertEquals("/profile", NavigationRouter.getRoute(AppDestination.Profile))
        assertEquals("/admin", NavigationRouter.getRoute(AppDestination.Admin))
        assertEquals("https://sekota.id", NavigationRouter.getRoute(AppDestination.External("https://sekota.id")))
    }
}
