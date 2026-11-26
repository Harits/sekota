package id.sekota.app.navigation

import id.sekota.core.model.BookSlug
import id.sekota.core.model.ProductSlug

/** Every destination in the ported static site. */
sealed interface Route {
    data object Home : Route
    data class Product(val slug: ProductSlug) : Route
    data class Books(val anchor: BookSlug? = null) : Route
    data class Contact(val preset: String? = null) : Route
    data object Privacy : Route
    data object Terms : Route
}
