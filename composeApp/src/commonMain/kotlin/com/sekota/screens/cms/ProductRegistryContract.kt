package com.sekota.screens.cms

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct

enum class ProductTypeFilter(val title: String) {
    ALL("Semua Entitas"),
    BOOKS("Books Registry"),
    SUITE("Intelligence Suite"),
    MERCHANDISE("Merchandise")
}

sealed class RegistryItem {
    abstract val id: String
    abstract val title: String
    abstract val subtitle: String
    abstract val category: String
    abstract val imageUrl: String?

    data class Book(val book: AdminBook) : RegistryItem() {
        override val id: String get() = book.id
        override val title: String get() = book.title
        override val subtitle: String get() = book.author
        override val category: String get() = book.category
        override val imageUrl: String? get() = book.coverImage
    }

    data class SuiteProduct(val product: AdminProduct) : RegistryItem() {
        override val id: String get() = product.id
        override val title: String get() = "${product.name} (${product.code})"
        override val subtitle: String get() = product.description
        override val category: String get() = product.categoryEyebrow
        override val imageUrl: String? get() = product.iconUrlOrBase64
    }

    data class Merch(val merch: AdminMerch) : RegistryItem() {
        override val id: String get() = merch.id
        override val title: String get() = merch.title
        override val subtitle: String get() = "$${merch.price} · ${merch.seriesName}"
        override val category: String get() = merch.category
        override val imageUrl: String? get() = merch.imageUrl
    }
}
