package com.sekota.features.sync.domain.model

import com.sekota.features.admin.domain.model.AdminBook
import com.sekota.features.admin.domain.model.AdminLiveMetrics
import com.sekota.features.admin.domain.model.AdminProduct
import com.sekota.features.admin.domain.model.ClientInquiry
import com.sekota.features.admin.domain.model.StrategicConversationConfig
import com.sekota.features.admin.domain.model.WhySekotaConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class SyncDataPayload(
    val books: List<AdminBook> = emptyList(),
    val products: List<AdminProduct> = emptyList(),
    val metrics: AdminLiveMetrics = AdminLiveMetrics(),
    val inquiries: List<ClientInquiry> = emptyList(),
    val valueLoop: com.sekota.features.admin.domain.model.ValueLoopConfig = com.sekota.features.admin.domain.model.ValueLoopConfig(),
    val categories: List<String> = emptyList(),
    val whySekota: WhySekotaConfig = WhySekotaConfig(),
    val contactConfig: StrategicConversationConfig = StrategicConversationConfig(),
    val footerConfig: com.sekota.features.admin.domain.model.FooterConfig = com.sekota.features.admin.domain.model.FooterConfig(),
    val navigationItems: List<com.sekota.features.navigation.domain.model.NavigationItem> = com.sekota.features.navigation.domain.model.NavigationItem.CANONICAL_ITEMS,
    val version: Long = 0L,
    // Company profile (CORE-07-01-03 / #22658). Empty values mean "not sent" (older
    // CMS build) and must never overwrite stored content.
    val services: List<com.sekota.features.admin.domain.model.ServiceOffering> = emptyList(),
    val faqs: List<com.sekota.features.admin.domain.model.Faq> = emptyList(),
    val about: com.sekota.features.admin.domain.model.AboutConfig = com.sekota.features.admin.domain.model.AboutConfig(),
    val partnerCta: com.sekota.features.admin.domain.model.PartnerCtaConfig = com.sekota.features.admin.domain.model.PartnerCtaConfig(),
    val contentSchemaVersion: Int = 0
)

/**
 * Writes the company profile part of a payload to storage, skipping sections the
 * sender did not include. Shared by the web client (SyncService) and the desktop
 * CMS sync server so both apply payloads the same way.
 */
fun SyncDataPayload.applyCompanyProfileTo(
    storage: com.sekota.core.storage.AdminDataStorage,
    json: kotlinx.serialization.json.Json
) {
    if (services.isNotEmpty()) storage.saveJson("services", json.encodeToString(services))
    if (faqs.isNotEmpty()) storage.saveJson("faqs", json.encodeToString(faqs))
    if (!about.isEmpty) storage.saveJson("about", json.encodeToString(about))
    if (!partnerCta.isEmpty) storage.saveJson("partner_cta", json.encodeToString(partnerCta))
    if (contentSchemaVersion > 0) storage.saveJson("content_schema_version", contentSchemaVersion.toString())
}
