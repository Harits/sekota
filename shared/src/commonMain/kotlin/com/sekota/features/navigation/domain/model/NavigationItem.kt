package com.sekota.features.navigation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigationItem(
    val id: String,
    val label: String,
    val route: String,
    val iconUrl: String? = null,
    val order: Int = 0,
    val isVisible: Boolean = true
) {
    companion object {
        val CANONICAL_ITEMS = listOf(
            NavigationItem(
                id = "nav_solusi",
                label = "Solusi",
                route = "#solusi",
                order = 1,
                isVisible = true
            ),
            NavigationItem(
                id = "nav_produk",
                label = "Produk",
                route = "#produk",
                order = 2,
                isVisible = true
            ),
            NavigationItem(
                id = "nav_ebook",
                label = "E-Book",
                route = "/catalog",
                order = 3,
                isVisible = true
            ),
            NavigationItem(
                id = "nav_merchandise",
                label = "Merchandise",
                route = "/merchandise",
                order = 4,
                isVisible = true
            ),
            NavigationItem(
                id = "nav_kontak",
                label = "Kontak",
                route = "#kontak",
                order = 5,
                isVisible = true
            )
        )
    }
}
