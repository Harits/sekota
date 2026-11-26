package id.sekota.feature.landing.presentation

import id.sekota.feature.landing.domain.GetLandingContent
import id.sekota.feature.landing.domain.LandingContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LandingUiState(
    val highlightedRow: Int = 0,
    val openFaqId: Int = 0,
)

/**
 * MVVM state holder for the landing page. The only continuous interaction is the
 * highlight cycling through the four platforms and the FAQ accordion, both exposed
 * as intent-style methods so they are easy to test without a UI.
 */
class LandingViewModel(
    getContent: GetLandingContent,
) {
    val content: LandingContent = getContent()

    private val _state = MutableStateFlow(LandingUiState())
    val state: StateFlow<LandingUiState> = _state.asStateFlow()

    fun highlightNextRow() {
        _state.update { it.copy(highlightedRow = (it.highlightedRow + 1) % content.hero.suiteRows.size) }
    }

    fun highlightRow(index: Int) {
        if (index in content.hero.suiteRows.indices) {
            _state.update { it.copy(highlightedRow = index) }
        }
    }

    fun toggleFaq(id: Int) {
        _state.update { current ->
            current.copy(openFaqId = if (current.openFaqId == id) -1 else id)
        }
    }
}
