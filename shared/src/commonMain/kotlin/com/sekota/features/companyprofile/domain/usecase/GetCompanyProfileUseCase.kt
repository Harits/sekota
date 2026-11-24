package com.sekota.features.companyprofile.domain.usecase

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.repository.AdminRepository
import com.sekota.features.companyprofile.domain.model.CompanyProfileContent

/**
 * Loads the public company profile, hiding items switched off in the CMS.
 *
 * Books come from the bookinteractiontool API, which can be slow, so callers may
 * load them separately with [books] and pass `includeBooks = false` here.
 */
class GetCompanyProfileUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(includeBooks: Boolean = true): CompanyProfileContent = CompanyProfileContent(
        about = repository.getAbout(),
        services = repository.getServices().filter { it.isVisible }.sortedBy { it.sortOrder },
        process = repository.getValueLoop(),
        products = repository.getProducts().filter { it.isVisible }.sortedBy { it.sortOrder },
        books = if (includeBooks) books() else emptyList(),
        partnerCta = repository.getPartnerCta(),
        faqs = repository.getFaqs().filter { it.isVisible }.sortedBy { it.sortOrder },
        footer = repository.getFooter(),
        contactEmail = repository.getStrategicConversation().email
    )

    /** Books in display order: curated sort order first, then title. */
    suspend fun books(): List<AdminBook> =
        repository.getBooks().sortedWith(compareBy({ it.sortOrder == 0 }, { it.sortOrder }, { it.title }))
}
