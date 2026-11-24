package com.sekota.features.admin

import com.sekota.features.admin.data.seed.CompanyProfileSeed
import com.sekota.features.admin.domain.model.AboutConfig
import com.sekota.features.admin.domain.model.AboutPillar
import com.sekota.features.admin.domain.model.Faq
import com.sekota.features.admin.domain.model.LocalizedText
import com.sekota.features.admin.domain.model.PartnerCtaConfig
import com.sekota.features.admin.domain.model.ServiceOffering
import com.sekota.features.admin.domain.usecase.*
import com.sekota.features.companyprofile.domain.usecase.GetCompanyProfileUseCase
import com.sekota.localization.AppLocale
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** CORE-07-01-04 / #22659 */
class CompanyProfileUseCasesTest {

    @Test
    fun localizedTextFallsBackToIndonesianWhenEnglishMissing() {
        val text = LocalizedText(id = "Layanan", en = "")
        assertEquals("Layanan", text.resolve(AppLocale.ID))
        assertEquals("Layanan", text.resolve(AppLocale.EN))
        assertEquals("Services", LocalizedText("Layanan", "Services").resolve(AppLocale.EN))
        assertTrue(LocalizedText(id = "  ").isBlank)
    }

    @Test
    fun serviceValidationRejectsMissingTitleOrSummary() = runTest {
        val repo = FakeAdminRepository()
        val save = SaveServiceUseCase(repo)
        val valid = CompanyProfileSeed.services.first()

        assertTrue(save(valid.copy(title = LocalizedText(""))).isFailure)
        assertTrue(save(valid.copy(summary = LocalizedText(""))).isFailure)
        assertTrue(save(valid.copy(id = "")).isFailure)
        assertTrue(save(valid).isSuccess)
        assertEquals(listOf(valid.id), GetServicesUseCase(repo)().map { it.id })

        assertTrue(DeleteServiceUseCase(repo)("").isFailure)
        assertTrue(DeleteServiceUseCase(repo)(valid.id).getOrThrow())
        assertTrue(GetServicesUseCase(repo)().isEmpty())
    }

    @Test
    fun servicesAreReturnedInSortOrder() = runTest {
        val repo = FakeAdminRepository()
        CompanyProfileSeed.services.reversed().forEach { SaveServiceUseCase(repo)(it) }
        assertEquals(listOf("01", "02", "03", "04"), GetServicesUseCase(repo)().map { it.number })
    }

    @Test
    fun faqValidationRequiresQuestionAndAnswer() = runTest {
        val repo = FakeAdminRepository()
        val save = SaveFaqUseCase(repo)
        val faq = Faq("faq-1", LocalizedText("Tanya?", "Ask?"), LocalizedText("Jawab.", "Answer."))

        assertTrue(save(faq.copy(question = LocalizedText(""))).isFailure)
        assertTrue(save(faq.copy(answer = LocalizedText(""))).isFailure)
        assertTrue(save(faq).isSuccess)
        assertEquals("Answer.", GetFaqsUseCase(repo)().single().answer.resolve(AppLocale.EN))
        assertTrue(DeleteFaqUseCase(repo)("faq-1").getOrThrow())
    }

    @Test
    fun aboutAndPartnerCtaValidation() = runTest {
        val repo = FakeAdminRepository()
        assertTrue(SaveAboutUseCase(repo)(AboutConfig(title = LocalizedText(""))).isFailure)
        assertTrue(
            SaveAboutUseCase(repo)(
                CompanyProfileSeed.about.copy(pillars = listOf(AboutPillar(LocalizedText(""), LocalizedText("x"))))
            ).isFailure
        )
        assertTrue(SaveAboutUseCase(repo)(CompanyProfileSeed.about).isSuccess)
        assertEquals(CompanyProfileSeed.about, GetAboutUseCase(repo)())

        assertTrue(SavePartnerCtaUseCase(repo)(PartnerCtaConfig()).isFailure)
        assertTrue(SavePartnerCtaUseCase(repo)(CompanyProfileSeed.partnerCta.copy(ctaLabel = LocalizedText(""))).isFailure)
        assertTrue(SavePartnerCtaUseCase(repo)(CompanyProfileSeed.partnerCta).isSuccess)
        assertEquals("/kontak#mitra-awal", GetPartnerCtaUseCase(repo)().ctaRoute)
    }

    @Test
    fun companyProfileHidesInvisibleItemsAndLinksServicesToPlatforms() = runTest {
        val repo = FakeAdminRepository()
        CompanyProfileSeed.services.forEach { repo.saveService(it) }
        repo.saveService(ServiceOffering("hidden", "05", LocalizedText("Tersembunyi"), LocalizedText("x"), isVisible = false, sortOrder = 5))
        CompanyProfileSeed.products.forEach { repo.saveProduct(it) }
        repo.saveProduct(CompanyProfileSeed.products.first().copy(id = "off", code = "OFF", isVisible = false))
        CompanyProfileSeed.faqs.forEach { repo.saveFaq(it) }
        repo.about = CompanyProfileSeed.about
        repo.partnerCta = CompanyProfileSeed.partnerCta

        val content = GetCompanyProfileUseCase(repo)()

        assertEquals(4, content.services.size)
        assertEquals(listOf("Clarivio", "Ascendio", "Sociara", "Evidara"), content.products.map { it.name })
        assertEquals("Clarivio", content.productFor(content.services.first())?.name)
        assertEquals("svc-esg", content.serviceFor(content.products.last())?.id)
        assertEquals(7, content.faqs.size)
        assertEquals("info@sekota.id", content.contactEmail)
        assertFalse(content.isEmpty)
    }

    @Test
    fun emptyStoreIsReportedAsEmpty() = runTest {
        val content = GetCompanyProfileUseCase(FakeAdminRepository())()
        assertTrue(content.isEmpty)
        assertNull(content.products.firstOrNull())
    }

    @Test
    fun seedMatchesApprovedDesign() {
        assertEquals(listOf("CLV", "ASC", "SOC", "EVD"), CompanyProfileSeed.products.map { it.code })
        assertEquals(6, CompanyProfileSeed.books.size)
        assertEquals(3, CompanyProfileSeed.books.count { it.seriesId == CompanyProfileSeed.TRILOGY_SERIES_ID })
        assertTrue(CompanyProfileSeed.books.all { it.author == "Putu Aan J." && !it.tagline.isBlank && it.tagline.en.isNotBlank() })
        assertTrue(CompanyProfileSeed.faqs.all { it.question.en.isNotBlank() && it.answer.en.isNotBlank() })
        // Every service points at a real platform and every platform back at its service.
        val codes = CompanyProfileSeed.products.map { it.code }.toSet()
        assertTrue(CompanyProfileSeed.services.all { it.productCode in codes })
        val serviceIds = CompanyProfileSeed.services.map { it.id }.toSet()
        assertTrue(CompanyProfileSeed.products.all { it.serviceId in serviceIds })
    }
}
