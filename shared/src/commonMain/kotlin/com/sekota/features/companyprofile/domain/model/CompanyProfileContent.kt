package com.sekota.features.companyprofile.domain.model

import com.sekota.features.admin.domain.model.AboutConfig
import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.Faq
import com.sekota.features.admin.domain.model.FooterConfig
import com.sekota.features.admin.domain.model.PartnerCtaConfig
import com.sekota.features.admin.domain.model.ServiceOffering
import com.sekota.features.admin.domain.model.ValueLoopConfig

/**
 * Everything the public company profile page renders, already filtered to visible
 * items and sorted in display order (WEB-07-02 / #22654).
 */
data class CompanyProfileContent(
    val about: AboutConfig,
    val services: List<ServiceOffering>,
    val process: ValueLoopConfig,
    val products: List<AdminProduct>,
    val books: List<AdminBook>,
    val partnerCta: PartnerCtaConfig,
    val faqs: List<Faq>,
    val footer: FooterConfig,
    val contactEmail: String
) {
    fun productFor(service: ServiceOffering): AdminProduct? =
        service.productCode?.let { code -> products.firstOrNull { it.code == code } }

    fun serviceFor(product: AdminProduct): ServiceOffering? =
        services.firstOrNull { it.id == product.serviceId }
            ?: services.firstOrNull { it.productCode == product.code }

    /** True when nothing to show was returned (fresh CMS wipe or failed load). */
    val isEmpty: Boolean
        get() = services.isEmpty() && products.isEmpty() && books.isEmpty() && faqs.isEmpty() && about.isEmpty
}
