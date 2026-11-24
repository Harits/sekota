package com.sekota.features.companyprofile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekota.features.companyprofile.domain.usecase.GetCompanyProfileUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * State holder of the company profile page (WEB-07-02 / #22654).
 *
 * Page content is shown as soon as the local store answers; the book shelf follows
 * when the book API responds, or empty after [booksTimeoutMs] so a slow API never
 * blocks the page. CMS sync events refresh the page in place.
 */
class CompanyProfileViewModel(
    private val getCompanyProfile: GetCompanyProfileUseCase,
    syncEvents: Flow<Unit>? = null,
    private val booksTimeoutMs: Long = 8_000
) : ViewModel() {

    private val _state = MutableStateFlow<CompanyProfileUiState>(CompanyProfileUiState.Loading)
    val state: StateFlow<CompanyProfileUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load(showLoading = true)
        syncEvents?.let { events -> viewModelScope.launch { events.collect { onIntent(CompanyProfileIntent.Refresh) } } }
    }

    fun onIntent(intent: CompanyProfileIntent) {
        when (intent) {
            CompanyProfileIntent.Retry -> load(showLoading = true)
            CompanyProfileIntent.Refresh -> load(showLoading = false)
        }
    }

    private fun load(showLoading: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showLoading) _state.value = CompanyProfileUiState.Loading
            val content = runCatching { getCompanyProfile(includeBooks = false) }.getOrElse { error ->
                println("CompanyProfileViewModel: load failed: ${error.message}")
                _state.value = CompanyProfileUiState.Error(error.message ?: "Unknown error")
                return@launch
            }
            val previousBooks = (_state.value as? CompanyProfileUiState.Loaded)?.content?.books.orEmpty()
            _state.value = CompanyProfileUiState.Loaded(content.copy(books = previousBooks), booksLoading = true)

            val books = runCatching { withTimeoutOrNull(booksTimeoutMs) { getCompanyProfile.books() } }
                .onFailure { println("CompanyProfileViewModel: books failed: ${it.message}") }
                .getOrNull() ?: previousBooks
            val finalContent = content.copy(books = books)
            _state.update {
                if (finalContent.isEmpty) CompanyProfileUiState.Empty
                else CompanyProfileUiState.Loaded(finalContent, booksLoading = false)
            }
        }
    }
}
