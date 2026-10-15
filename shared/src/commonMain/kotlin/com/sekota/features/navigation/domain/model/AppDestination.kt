package com.sekota.features.navigation.domain.model

/**
 * Domain-level Navigation Destination representing business capabilities
 * rather than hardcoded infrastructure URL strings (Clean & Screaming Architecture).
 */
sealed class AppDestination {
    data object Landing : AppDestination()
    data object SolusiSection : AppDestination()
    data object ProdukSection : AppDestination()
    data object KontakSection : AppDestination()
    data object Catalog : AppDestination()
    data object Merchandise : AppDestination()
    data object Login : AppDestination()
    data object Signup : AppDestination()
    data object Profile : AppDestination()
    data object Admin : AppDestination()
    data class BookReader(val bookId: String, val chapterIndex: Int = 0) : AppDestination()
    data class External(val url: String) : AppDestination()
    data class Unknown(val raw: String) : AppDestination()
}

/**
 * Centralized Navigation Router responsible for mapping Domain Destinations
 * to implementation routes, URL hashes, and vice versa.
 */
object NavigationRouter {
    fun getRoute(destination: AppDestination): String = when (destination) {
        AppDestination.Landing -> "/"
        AppDestination.SolusiSection -> "#solusi"
        AppDestination.ProdukSection -> "#produk"
        AppDestination.KontakSection -> "#kontak"
        AppDestination.Catalog -> "/catalog"
        AppDestination.Merchandise -> "/merchandise"
        AppDestination.Login -> "/login"
        AppDestination.Signup -> "/signup"
        AppDestination.Profile -> "/profile"
        AppDestination.Admin -> "/admin"
        is AppDestination.BookReader -> "/reader/${destination.bookId}"
        is AppDestination.External -> destination.url
        is AppDestination.Unknown -> destination.raw
    }

    fun parseDestination(rawRouteOrCode: String): AppDestination {
        val clean = rawRouteOrCode.trim()
        val upper = clean.uppercase()
        return when {
            clean.startsWith("http://") || clean.startsWith("https://") -> AppDestination.External(clean)
            clean.startsWith("/reader/") -> {
                val id = clean.removePrefix("/reader/").trim('/')
                AppDestination.BookReader(id)
            }
            clean.startsWith("/ebook/") && clean.endsWith("/baca") -> {
                val id = clean.removePrefix("/ebook/").removeSuffix("/baca").trim('/')
                AppDestination.BookReader(id)
            }
            clean == "#solusi" || clean.equals("solusi", ignoreCase = true) -> AppDestination.SolusiSection
            clean == "#produk" || clean.equals("produk", ignoreCase = true) -> AppDestination.ProdukSection
            clean == "#kontak" || clean.equals("kontak", ignoreCase = true) || upper == "CONTACT" -> AppDestination.KontakSection
            clean == "/catalog" || clean == "catalog" || clean == "#catalog" || upper == "EBOOK" -> AppDestination.Catalog
            clean == "/merchandise" || clean == "merchandise" || clean == "#merchandise" || upper == "MERCH" -> AppDestination.Merchandise
            clean == "/login" || clean.equals("login", ignoreCase = true) || upper == "AUTH" -> AppDestination.Login
            clean == "/signup" || clean.equals("signup", ignoreCase = true) -> AppDestination.Signup
            clean == "/profile" || clean.equals("profile", ignoreCase = true) -> AppDestination.Profile
            clean == "/admin" || clean.equals("admin", ignoreCase = true) -> AppDestination.Admin
            clean == "/" || clean == "#beranda" || clean.equals("home", ignoreCase = true) -> AppDestination.Landing
            else -> AppDestination.Unknown(clean)
        }
    }
}
