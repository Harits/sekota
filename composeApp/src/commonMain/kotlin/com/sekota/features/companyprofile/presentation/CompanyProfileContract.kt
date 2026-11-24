package com.sekota.features.companyprofile.presentation

import com.sekota.features.companyprofile.domain.model.CompanyProfileContent

/** MVI contract of the company profile landing page (WEB-07-02 / #22654). */
sealed interface CompanyProfileUiState {
    data object Loading : CompanyProfileUiState

    /** [booksLoading] is true while the slower book catalogue is still on its way. */
    data class Loaded(val content: CompanyProfileContent, val booksLoading: Boolean = false) : CompanyProfileUiState

    /** The CMS returned no content at all. */
    data object Empty : CompanyProfileUiState

    data class Error(val message: String) : CompanyProfileUiState
}

sealed interface CompanyProfileIntent {
    data object Retry : CompanyProfileIntent
    /** Content changed in the CMS (sync event); refresh without a loading flash. */
    data object Refresh : CompanyProfileIntent
}
