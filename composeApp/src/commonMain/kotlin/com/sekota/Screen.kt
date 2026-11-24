package com.sekota

/**
 * Pages of Web Sekota after the company profile revamp (WEB-07-03 / #22655).
 *
 * [Product], [Books] and [Contact] are rendered by the Sprint 6 screens until their
 * redesigned pages ship in Sprint 8 (WEB-08-01..03).
 */
sealed interface Screen {
    data object Landing : Screen
    data class Product(val slug: String) : Screen
    data class Books(val bookSlug: String? = null) : Screen
    data class Contact(val anchor: String? = null) : Screen
    data object Admin : Screen
}
