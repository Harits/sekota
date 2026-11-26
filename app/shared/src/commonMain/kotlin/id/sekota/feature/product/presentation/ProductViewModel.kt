package id.sekota.feature.product.presentation

import id.sekota.feature.product.domain.ProductDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProductUiState(
    val selectedScreen: Int = 0,
    /** The design auto-advances the gallery every 6s until the visitor picks a tab. */
    val autoAdvance: Boolean = true,
)

class ProductViewModel(val detail: ProductDetail) {
    private val _state = MutableStateFlow(ProductUiState())
    val state: StateFlow<ProductUiState> = _state.asStateFlow()

    /** A manual selection stops the automatic tour, matching the design. */
    fun selectScreen(index: Int) {
        if (index in detail.screens.indices) {
            _state.update { it.copy(selectedScreen = index, autoAdvance = false) }
        }
    }

    /** Advances only while the visitor has not taken over. */
    fun advanceAuto() {
        _state.update { current ->
            if (!current.autoAdvance || detail.screens.size <= 1) {
                current
            } else {
                current.copy(selectedScreen = (current.selectedScreen + 1) % detail.screens.size)
            }
        }
    }
}
