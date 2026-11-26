package id.sekota.app.navigation

import id.sekota.core.model.BookSlug
import id.sekota.core.model.Language
import id.sekota.core.model.ProductSlug

/** A parsed deep link: language + route + optional landing section. */
data class HashTarget(
    val language: Language,
    val route: Route,
    val homeAnchor: String? = null,
)

/**
 * URL format:
 *   #/id | #/en
 *   #/id/home/{anchor}
 *   #/id/product/{slug}
 *   #/id/books[/{bookAnchor}]
 *   #/id/contact[/{preset}]
 *   #/id/privacy | #/id/terms
 */
fun parseHash(hash: String?): HashTarget {
    val parts = hash
        ?.removePrefix("#")
        ?.trim('/')
        ?.split('/')
        ?.filter { it.isNotEmpty() }
        ?: emptyList()

    val language = when (parts.firstOrNull()) {
        "en" -> Language.EN
        else -> Language.ID
    }
    val rest = if (parts.firstOrNull() == "en" || parts.firstOrNull() == "id") parts.drop(1) else parts

    return when (rest.firstOrNull()) {
        null -> HashTarget(language, Route.Home)
        "home" -> HashTarget(language, Route.Home, rest.getOrNull(1))
        "product" -> {
            val slug = rest.getOrNull(1)?.let(ProductSlug::fromPath)
            HashTarget(language, if (slug != null) Route.Product(slug) else Route.Home)
        }
        "books" -> HashTarget(
            language,
            Route.Books(rest.getOrNull(1)?.let(BookSlug::fromAnchor)),
        )
        "contact" -> HashTarget(language, Route.Contact(rest.getOrNull(1)))
        "privacy", "privasi" -> HashTarget(language, Route.Privacy)
        "terms", "syarat-ketentuan" -> HashTarget(language, Route.Terms)
        else -> HashTarget(language, Route.Home)
    }
}

fun encodeHash(language: Language, route: Route, homeAnchor: String?): String {
    val prefix = "#/${language.code}"
    return when (route) {
        Route.Home -> if (homeAnchor.isNullOrBlank() || homeAnchor == "hero") {
            prefix
        } else {
            "$prefix/home/$homeAnchor"
        }
        is Route.Product -> "$prefix/product/${route.slug.path}"
        is Route.Books -> buildString {
            append(prefix).append("/books")
            route.anchor?.let { append('/').append(it.anchor) }
        }
        is Route.Contact -> buildString {
            append(prefix).append("/contact")
            route.preset?.let { append('/').append(it) }
        }
        Route.Privacy -> "$prefix/privacy"
        Route.Terms -> "$prefix/terms"
    }
}
