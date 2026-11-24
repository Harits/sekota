package com.sekota.features.companyprofile.domain.usecase

import com.sekota.features.admin.domain.repository.AdminRepository
import com.sekota.features.companyprofile.domain.model.CompanyProfileContent

/** Loads the public company profile in one call, hiding items switched off in the CMS. */
class GetCompanyProfileUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): CompanyProfileContent = CompanyProfileContent(
        about = repository.getAbout(),
        services = repository.getServices().filter { it.isVisible }.sortedBy { it.sortOrder },
        process = repository.getValueLoop(),
        products = repository.getProducts().filter { it.isVisible }.sortedBy { it.sortOrder },
        books = repository.getBooks().sortedWith(compareBy({ it.sortOrder == 0 }, { it.sortOrder }, { it.title })),
        partnerCta = repository.getPartnerCta(),
        faqs = repository.getFaqs().filter { it.isVisible }.sortedBy { it.sortOrder },
        footer = repository.getFooter(),
        contactEmail = repository.getStrategicConversation().email
    )
}
