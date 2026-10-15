package com.sekota.screens.cms

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminMerch
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.AppEnvironment
import com.sekota.features.admin.domain.model.EnvironmentConfigResolver
import com.sekota.features.admin.domain.usecase.*
import com.sekota.syncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * MVI ViewModel for Product Registry Workspace.
 * Handles state, intents, validation, and side-effects for Books, Intelligence Suite, and Merchandise.
 */
class ProductRegistryViewModel(
    private val getBooksUseCase: GetAdminBooksUseCase,
    private val saveBookUseCase: SaveAdminBookUseCase,
    private val getCategoriesUseCase: GetAdminCategoriesUseCase,
    private val getProductsUseCase: GetAdminProductsUseCase,
    private val saveProductUseCase: SaveAdminProductUseCase,
    private val deleteProductUseCase: DeleteAdminProductUseCase,
    private val getMerchUseCase: GetAdminMerchUseCase,
    private val saveMerchUseCase: SaveAdminMerchUseCase,
    private val deleteMerchUseCase: DeleteAdminMerchUseCase,
    private val scope: CoroutineScope,
    private val environment: AppEnvironment = EnvironmentConfigResolver.resolveEnvironment()
) {
    private val _uiState = MutableStateFlow(ProductRegistryState())
    val uiState: StateFlow<ProductRegistryState> = _uiState.asStateFlow()

    private val _effect = Channel<ProductRegistryEffect>(Channel.BUFFERED)
    val effect: Flow<ProductRegistryEffect> = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun onIntent(intent: ProductRegistryIntent) {
        when (intent) {
            is ProductRegistryIntent.Refresh -> loadData()
            is ProductRegistryIntent.SetFilter -> _uiState.update { it.copy(selectedFilter = intent.filter) }
            is ProductRegistryIntent.SetSearchQuery -> _uiState.update { it.copy(searchQuery = intent.query) }

            // Modal Lifecycle
            is ProductRegistryIntent.OpenCreateModal -> handleOpenCreateModal()
            is ProductRegistryIntent.OpenCreateTypePicker -> _uiState.update { it.copy(activeModal = ActiveModalState.SelectCreateTypeModal(it.selectedFilter)) }
            is ProductRegistryIntent.OpenBookInfoDialog -> _uiState.update { it.copy(activeModal = ActiveModalState.BookInfoModal) }
            is ProductRegistryIntent.OpenEditBookModal -> handleOpenEditBookModal(intent.book)
            is ProductRegistryIntent.OpenEditProductModal -> handleOpenEditProductModal(intent.product)
            is ProductRegistryIntent.OpenEditMerchModal -> handleOpenEditMerchModal(intent.merch)
            is ProductRegistryIntent.DismissModal -> _uiState.update { it.copy(activeModal = ActiveModalState.None) }

            // Book Edits
            is ProductRegistryIntent.UpdateBookCategory -> updateBookModal { it.copy(category = intent.value, validationError = null) }
            is ProductRegistryIntent.UpdateBookDescription -> updateBookModal { it.copy(description = intent.value) }
            is ProductRegistryIntent.UpdateBookPdfUrl -> updateBookModal { it.copy(pdfUrl = intent.value) }
            is ProductRegistryIntent.UpdateBookReadingTime -> updateBookModal { it.copy(readingTime = intent.value) }
            is ProductRegistryIntent.UpdateBookPages -> updateBookModal { it.copy(pages = intent.value) }
            is ProductRegistryIntent.UpdateBookYear -> updateBookModal { it.copy(year = intent.value) }
            is ProductRegistryIntent.UpdateBookLanguage -> updateBookModal { it.copy(language = intent.value) }
            is ProductRegistryIntent.UpdateBookPublishedDate -> updateBookModal { it.copy(publishedDate = intent.value) }

            // Suite Edits
            is ProductRegistryIntent.SwitchSuiteModalLanguage -> updateSuiteModal { it.copy(activeLanguageTab = intent.lang) }
            is ProductRegistryIntent.UpdateProductName -> updateSuiteModal { it.copy(name = intent.value, validationError = null) }
            is ProductRegistryIntent.UpdateProductCode -> updateSuiteModal { it.copy(code = intent.value, validationError = null) }
            is ProductRegistryIntent.UpdateProductEyebrow -> updateSuiteModal { it.copy(categoryEyebrow = intent.value) }
            is ProductRegistryIntent.UpdateProductDescription -> updateSuiteModal { it.copy(description = intent.value) }
            is ProductRegistryIntent.UpdateProductFeaturesText -> updateSuiteModal { it.copy(featuresText = intent.value) }
            is ProductRegistryIntent.UpdateProductCoverImage -> updateSuiteModal { it.copy(coverImageBase64 = intent.base64) }
            is ProductRegistryIntent.UpdateProductEyebrowEn -> updateSuiteModal { it.copy(categoryEyebrowEn = intent.value) }
            is ProductRegistryIntent.UpdateProductDescriptionEn -> updateSuiteModal { it.copy(descriptionEn = intent.value) }
            is ProductRegistryIntent.UpdateProductFeaturesTextEn -> updateSuiteModal { it.copy(featuresTextEn = intent.value) }

            // Merch Edits
            is ProductRegistryIntent.UpdateMerchTitle -> updateMerchModal { it.copy(title = intent.value, validationError = null) }
            is ProductRegistryIntent.UpdateMerchCategory -> updateMerchModal { it.copy(category = intent.value) }
            is ProductRegistryIntent.UpdateMerchSeriesName -> updateMerchModal { it.copy(seriesName = intent.value) }
            is ProductRegistryIntent.UpdateMerchPrice -> updateMerchModal { it.copy(price = intent.value, validationError = null) }
            is ProductRegistryIntent.UpdateMerchImage -> updateMerchModal { it.copy(imageBase64 = intent.base64) }

            // Actions
            is ProductRegistryIntent.SaveActiveModal -> handleSaveActiveModal()
            is ProductRegistryIntent.RequestDeleteProduct -> handleDeleteProduct(intent.product)
            is ProductRegistryIntent.RequestDeleteMerch -> handleDeleteMerch(intent.merch)
        }
    }

    private fun loadData() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val b = getBooksUseCase()
                val c = getCategoriesUseCase()
                val p = getProductsUseCase()
                val m = getMerchUseCase()
                _uiState.update {
                    it.copy(
                        books = b,
                        categories = c,
                        products = p,
                        merchList = m,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                _effect.trySend(ProductRegistryEffect.ShowToast("Gagal memuat katalog: ${e.message}", isError = true))
            }
        }
    }

    private fun handleOpenCreateModal() {
        when (_uiState.value.selectedFilter) {
            ProductTypeFilter.ALL -> {
                _uiState.update { it.copy(activeModal = ActiveModalState.SelectCreateTypeModal(ProductTypeFilter.ALL)) }
            }
            ProductTypeFilter.SUITE -> {
                _uiState.update { it.copy(activeModal = ActiveModalState.SuiteProductModal()) }
            }
            ProductTypeFilter.MERCHANDISE -> {
                _uiState.update { it.copy(activeModal = ActiveModalState.MerchModal()) }
            }
            ProductTypeFilter.BOOKS -> {
                _uiState.update { it.copy(activeModal = ActiveModalState.BookInfoModal) }
            }
        }
    }

    private fun handleOpenEditBookModal(book: AdminBook) {
        _uiState.update {
            it.copy(
                activeModal = ActiveModalState.BookMetadataModal(
                    targetBook = book,
                    category = book.category,
                    description = book.description,
                    pdfUrl = book.pdfUrl ?: "",
                    readingTime = book.readingTime,
                    pages = book.pages.toString(),
                    year = book.year,
                    language = book.language,
                    publishedDate = book.publishedDate,
                    whatsInside = book.whatsInside
                )
            )
        }
    }

    private fun handleOpenEditProductModal(product: AdminProduct) {
        _uiState.update {
            it.copy(
                activeModal = ActiveModalState.SuiteProductModal(
                    editingProductId = product.id,
                    name = product.name,
                    code = product.code,
                    categoryEyebrow = product.categoryEyebrow,
                    description = product.description,
                    coverImageBase64 = product.iconUrlOrBase64 ?: "",
                    featuresText = product.features.joinToString(", "),
                    activeLanguageTab = "ID",
                    categoryEyebrowEn = product.categoryEyebrowEn ?: "",
                    descriptionEn = product.descriptionEn ?: "",
                    featuresTextEn = product.featuresEn.joinToString(", ")
                )
            )
        }
    }

    private fun handleOpenEditMerchModal(merch: AdminMerch) {
        _uiState.update {
            it.copy(
                activeModal = ActiveModalState.MerchModal(
                    editingMerchId = merch.id,
                    title = merch.title,
                    category = merch.category,
                    seriesName = merch.seriesName,
                    price = merch.price.toString(),
                    imageBase64 = merch.imageUrl ?: ""
                )
            )
        }
    }

    private inline fun updateBookModal(crossinline transform: (ActiveModalState.BookMetadataModal) -> ActiveModalState.BookMetadataModal) {
        val current = _uiState.value.activeModal as? ActiveModalState.BookMetadataModal ?: return
        _uiState.update { it.copy(activeModal = transform(current)) }
    }

    private inline fun updateSuiteModal(crossinline transform: (ActiveModalState.SuiteProductModal) -> ActiveModalState.SuiteProductModal) {
        val current = _uiState.value.activeModal as? ActiveModalState.SuiteProductModal ?: return
        _uiState.update { it.copy(activeModal = transform(current)) }
    }

    private inline fun updateMerchModal(crossinline transform: (ActiveModalState.MerchModal) -> ActiveModalState.MerchModal) {
        val current = _uiState.value.activeModal as? ActiveModalState.MerchModal ?: return
        _uiState.update { it.copy(activeModal = transform(current)) }
    }

    private fun handleSaveActiveModal() {
        when (val modal = _uiState.value.activeModal) {
            is ActiveModalState.BookMetadataModal -> saveBookMetadata(modal)
            is ActiveModalState.SuiteProductModal -> saveSuiteProduct(modal)
            is ActiveModalState.MerchModal -> saveMerch(modal)
            is ActiveModalState.SelectCreateTypeModal,
            ActiveModalState.BookInfoModal,
            ActiveModalState.None -> Unit
        }
    }

    private fun saveBookMetadata(modal: ActiveModalState.BookMetadataModal) {
        val updated = modal.targetBook.copy(
            category = modal.category.trim(),
            description = modal.description.trim(),
            pdfUrl = modal.pdfUrl.trim().ifBlank { null },
            readingTime = modal.readingTime.trim(),
            pages = modal.pages.trim().toIntOrNull() ?: modal.targetBook.pages,
            year = modal.year.trim(),
            language = modal.language.trim(),
            publishedDate = modal.publishedDate.trim(),
            whatsInside = modal.whatsInside
        )
        executeWithProductionGuard("Simpan Metadata Web Buku", updated.title, false) {
            scope.launch {
                saveBookUseCase(updated)
                syncService.triggerLocalUpdate()
                loadData()
                _uiState.update { it.copy(activeModal = ActiveModalState.None) }
                _effect.trySend(ProductRegistryEffect.ShowToast("Metadata buku '${updated.title}' disimpan"))
            }
        }
    }

    private fun saveSuiteProduct(modal: ActiveModalState.SuiteProductModal) {
        if (modal.name.isBlank() || modal.code.isBlank()) {
            updateSuiteModal { it.copy(validationError = "Nama dan Kode produk tidak boleh kosong") }
            return
        }
        val id = modal.editingProductId ?: "p-${Random.nextInt(1000, 9999)}"
        val feats = modal.featuresText.split(",").map { it.trim() }.filter { it.isNotBlank() }
        val featsEn = modal.featuresTextEn.split(",").map { it.trim() }.filter { it.isNotBlank() }
        val prod = AdminProduct(
            id = id,
            code = modal.code.trim().uppercase(),
            name = modal.name.trim(),
            categoryEyebrow = modal.categoryEyebrow.trim(),
            description = modal.description.trim(),
            features = feats,
            iconUrlOrBase64 = modal.coverImageBase64.ifBlank { null },
            categoryEyebrowEn = modal.categoryEyebrowEn.trim().ifBlank { null },
            descriptionEn = modal.descriptionEn.trim().ifBlank { null },
            featuresEn = featsEn
        )
        val actionLabel = if (modal.editingProductId == null) "Tambah Modul Suite" else "Edit Modul Suite"
        executeWithProductionGuard(actionLabel, prod.name, false) {
            scope.launch {
                saveProductUseCase(prod)
                syncService.triggerLocalUpdate()
                loadData()
                _uiState.update { it.copy(activeModal = ActiveModalState.None) }
                _effect.trySend(ProductRegistryEffect.ShowToast("Modul '${prod.name}' disimpan"))
            }
        }
    }

    private fun saveMerch(modal: ActiveModalState.MerchModal) {
        val price = modal.price.toDoubleOrNull()
        if (modal.title.isBlank()) {
            updateMerchModal { it.copy(validationError = "Nama item merchandise tidak boleh kosong") }
            return
        }
        if (price == null || price <= 0.0) {
            updateMerchModal { it.copy(validationError = "Harga harus berupa angka valid > 0") }
            return
        }
        val id = modal.editingMerchId ?: "m-${Random.nextInt(1000, 9999)}"
        val merch = AdminMerch(
            id = id,
            title = modal.title.trim(),
            category = modal.category.trim(),
            seriesName = modal.seriesName.trim(),
            price = price,
            rating = 5.0,
            imageUrl = modal.imageBase64.ifBlank { null }
        )
        val actionLabel = if (modal.editingMerchId == null) "Tambah Merchandise" else "Edit Merchandise"
        executeWithProductionGuard(actionLabel, merch.title, false) {
            scope.launch {
                saveMerchUseCase(merch)
                syncService.triggerLocalUpdate()
                loadData()
                _uiState.update { it.copy(activeModal = ActiveModalState.None) }
                _effect.trySend(ProductRegistryEffect.ShowToast("Merchandise '${merch.title}' disimpan"))
            }
        }
    }

    private fun handleDeleteProduct(product: AdminProduct) {
        executeWithProductionGuard("Hapus Modul Suite", product.name, true) {
            scope.launch {
                deleteProductUseCase(product.id)
                syncService.triggerLocalUpdate()
                loadData()
                _effect.trySend(ProductRegistryEffect.ShowToast("Modul '${product.name}' dihapus"))
            }
        }
    }

    private fun handleDeleteMerch(merch: AdminMerch) {
        executeWithProductionGuard("Hapus Merchandise", merch.title, true) {
            scope.launch {
                deleteMerchUseCase(merch.id)
                syncService.triggerLocalUpdate()
                loadData()
                _effect.trySend(ProductRegistryEffect.ShowToast("Merchandise '${merch.title}' dihapus"))
            }
        }
    }

    private fun executeWithProductionGuard(
        actionName: String,
        entityName: String,
        isCriticalDelete: Boolean,
        executeAction: () -> Unit
    ) {
        if (environment.isProduction) {
            _effect.trySend(
                ProductRegistryEffect.RequestProductionGuard(
                    actionName = actionName,
                    entityName = entityName,
                    isCriticalDelete = isCriticalDelete,
                    executeAction = executeAction
                )
            )
        } else {
            executeAction()
        }
    }
}
