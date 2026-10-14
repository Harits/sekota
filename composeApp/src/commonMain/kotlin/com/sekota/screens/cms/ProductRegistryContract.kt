package com.sekota.screens.cms

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.AdminWhatsInsideItem

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

/**
 * Mutually exclusive active modal state for MVI presentation.
 */
sealed interface ActiveModalState {
    data object None : ActiveModalState

    data class BookMetadataModal(
        val targetBook: AdminBook,
        val category: String,
        val description: String,
        val pdfUrl: String,
        val readingTime: String,
        val pages: String,
        val year: String,
        val language: String,
        val publishedDate: String,
        val whatsInside: List<AdminWhatsInsideItem> = emptyList(),
        val validationError: String? = null
    ) : ActiveModalState

    data class SelectCreateTypeModal(val filterHint: ProductTypeFilter = ProductTypeFilter.ALL) : ActiveModalState
    data object BookInfoModal : ActiveModalState

    data class SuiteProductModal(
        val editingProductId: String? = null,
        val name: String = "",
        val code: String = "",
        val categoryEyebrow: String = "Enterprise",
        val description: String = "",
        val coverImageBase64: String = "",
        val featuresText: String = "",
        val validationError: String? = null
    ) : ActiveModalState

    data class MerchModal(
        val editingMerchId: String? = null,
        val title: String = "",
        val category: String = "Apparel",
        val seriesName: String = "The Urban Collaborator",
        val price: String = "",
        val imageBase64: String = "",
        val validationError: String? = null
    ) : ActiveModalState
}

/**
 * Immutable Single-Source-of-Truth UI State for ProductRegistry.
 */
data class ProductRegistryState(
    val books: List<AdminBook> = emptyList(),
    val categories: List<String> = emptyList(),
    val products: List<AdminProduct> = emptyList(),
    val merchList: List<AdminMerch> = emptyList(),
    val selectedFilter: ProductTypeFilter = ProductTypeFilter.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeModal: ActiveModalState = ActiveModalState.None
) {
    val filteredItems: List<RegistryItem>
        get() {
            val list = mutableListOf<RegistryItem>()
            if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.BOOKS) {
                list.addAll(books.map { RegistryItem.Book(it) })
            }
            if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.SUITE) {
                list.addAll(products.map { RegistryItem.SuiteProduct(it) })
            }
            if (selectedFilter == ProductTypeFilter.ALL || selectedFilter == ProductTypeFilter.MERCHANDISE) {
                list.addAll(merchList.map { RegistryItem.Merch(it) })
            }
            return if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                list.filter {
                    it.title.lowercase().contains(q) ||
                    it.subtitle.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.id.lowercase().contains(q)
                }
            } else {
                list
            }
        }
}

/**
 * User Intents for ProductRegistryScreen.
 */
sealed interface ProductRegistryIntent {
    data object Refresh : ProductRegistryIntent
    data class SetFilter(val filter: ProductTypeFilter) : ProductRegistryIntent
    data class SetSearchQuery(val query: String) : ProductRegistryIntent

    // Modal Lifecycle
    data object OpenCreateModal : ProductRegistryIntent
    data object OpenCreateTypePicker : ProductRegistryIntent
    data object OpenBookInfoDialog : ProductRegistryIntent
    data class OpenEditBookModal(val book: AdminBook) : ProductRegistryIntent
    data class OpenEditProductModal(val product: AdminProduct) : ProductRegistryIntent
    data class OpenEditMerchModal(val merch: AdminMerch) : ProductRegistryIntent
    data object DismissModal : ProductRegistryIntent

    // Book Form Edits
    data class UpdateBookCategory(val value: String) : ProductRegistryIntent
    data class UpdateBookDescription(val value: String) : ProductRegistryIntent
    data class UpdateBookPdfUrl(val value: String) : ProductRegistryIntent
    data class UpdateBookReadingTime(val value: String) : ProductRegistryIntent
    data class UpdateBookPages(val value: String) : ProductRegistryIntent
    data class UpdateBookYear(val value: String) : ProductRegistryIntent
    data class UpdateBookLanguage(val value: String) : ProductRegistryIntent
    data class UpdateBookPublishedDate(val value: String) : ProductRegistryIntent

    // Suite Form Edits
    data class UpdateProductName(val value: String) : ProductRegistryIntent
    data class UpdateProductCode(val value: String) : ProductRegistryIntent
    data class UpdateProductEyebrow(val value: String) : ProductRegistryIntent
    data class UpdateProductDescription(val value: String) : ProductRegistryIntent
    data class UpdateProductFeaturesText(val value: String) : ProductRegistryIntent
    data class UpdateProductCoverImage(val base64: String) : ProductRegistryIntent

    // Merch Form Edits
    data class UpdateMerchTitle(val value: String) : ProductRegistryIntent
    data class UpdateMerchCategory(val value: String) : ProductRegistryIntent
    data class UpdateMerchSeriesName(val value: String) : ProductRegistryIntent
    data class UpdateMerchPrice(val value: String) : ProductRegistryIntent
    data class UpdateMerchImage(val base64: String) : ProductRegistryIntent

    // Actions
    data object SaveActiveModal : ProductRegistryIntent
    data class RequestDeleteProduct(val product: AdminProduct) : ProductRegistryIntent
    data class RequestDeleteMerch(val merch: AdminMerch) : ProductRegistryIntent
}

/**
 * One-off Side Effects dispatched via Channel.
 */
sealed interface ProductRegistryEffect {
    data class ShowToast(val message: String, val isError: Boolean = false) : ProductRegistryEffect
    data class RequestProductionGuard(
        val actionName: String,
        val entityName: String,
        val isCriticalDelete: Boolean,
        val executeAction: () -> Unit
    ) : ProductRegistryEffect
}
