package id.sekota.app

import id.sekota.app.navigation.Route
import id.sekota.app.navigation.encodeHash
import id.sekota.app.navigation.parseHash
import id.sekota.core.model.BookSlug
import id.sekota.core.model.Language
import id.sekota.core.model.ProductSlug
import kotlin.test.Test
import kotlin.test.assertEquals

class HashRouteTest {

    @Test
    fun emptyHashDefaultsToIndonesianHome() {
        val target = parseHash(null)
        assertEquals(Language.ID, target.language)
        assertEquals(Route.Home, target.route)
        assertEquals(null, target.homeAnchor)
    }

    @Test
    fun languagePrefixIsParsed() {
        assertEquals(Language.EN, parseHash("#/en").language)
        assertEquals(Language.ID, parseHash("#/id").language)
        assertEquals(Language.EN, parseHash("#/en/product/clarivio").language)
    }

    @Test
    fun productAndBookRoutesRoundTrip() {
        val product = Route.Product(ProductSlug.Sociara)
        assertEquals(product, parseHash(encodeHash(Language.ID, product, null)).route)

        val books = Route.Books(BookSlug.BlindSpotRadar)
        assertEquals(books, parseHash(encodeHash(Language.EN, books, null)).route)
    }

    @Test
    fun contactPresetsRoundTrip() {
        val contact = Route.Contact("buku-blind-spot-radar")
        val hash = encodeHash(Language.ID, contact, null)
        assertEquals("#/id/contact/buku-blind-spot-radar", hash)
        assertEquals(contact, parseHash(hash).route)
    }

    @Test
    fun homeAnchorIsPreserved() {
        val hash = encodeHash(Language.ID, Route.Home, "layanan")
        assertEquals("#/id/home/layanan", hash)
        val target = parseHash(hash)
        assertEquals(Route.Home, target.route)
        assertEquals("layanan", target.homeAnchor)
    }

    @Test
    fun heroAnchorIsCanonicalisedToBareHome() {
        assertEquals("#/en", encodeHash(Language.EN, Route.Home, "hero"))
    }

    @Test
    fun legalRoutesRoundTrip() {
        assertEquals("#/id/privacy", encodeHash(Language.ID, Route.Privacy, null))
        assertEquals("#/en/terms", encodeHash(Language.EN, Route.Terms, null))
        assertEquals(Route.Privacy, parseHash("#/id/privacy").route)
        assertEquals(Route.Terms, parseHash("#/en/terms").route)
        assertEquals(Route.Privacy, parseHash("#/id/privasi").route)
        assertEquals(Route.Terms, parseHash("#/id/syarat-ketentuan").route)
    }

    @Test
    fun unknownSegmentsFallBackToHome() {
        assertEquals(Route.Home, parseHash("#/id/product/unknown").route)
        assertEquals(Route.Home, parseHash("#/id/nonsense").route)
    }
}
