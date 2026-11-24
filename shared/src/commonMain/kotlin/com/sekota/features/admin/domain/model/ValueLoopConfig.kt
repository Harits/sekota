package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

/**
 * One stage of "Cara Kerja". The English fields are optional; the Indonesian
 * copy is shown when they are blank.
 */
@Serializable
data class AdminValueLoopStep(
    val stepNumber: String,
    val title: String,
    val subtitle: String,
    val titleEn: String? = null,
    val subtitleEn: String? = null
)

/**
 * "Cara Kerja" content. Since the company profile revamp (CORE-07-01-02 / #22657)
 * Sekota works in four stages instead of the earlier five-step value loop.
 */
@Serializable
data class ValueLoopConfig(
    val sectionEyebrow: String = "Cara Kerja",
    val title: String = "Empat tahap, dari masalah sampai hasil yang bertahan.",
    val description: String = "Prosesnya jelas dan terukur di setiap tahap.",
    val steps: List<AdminValueLoopStep> = defaultSteps,
    val sectionEyebrowEn: String? = "How We Work",
    val titleEn: String? = "Four stages, from problem to lasting results.",
    val descriptionEn: String? = "A clear, measurable process at every stage.",
    /** "Bentuk kerja sama" — the ways clients can engage Sekota. */
    val engagementModels: List<LocalizedText> = defaultEngagementModels
) {
    companion object {
        val defaultSteps: List<AdminValueLoopStep> = listOf(
            AdminValueLoopStep("01", "Diagnosis", "Petakan masalah dengan kerangka buku.", "Diagnose", "Map the problem using our book frameworks."),
            AdminValueLoopStep("02", "Perancangan", "Rancang solusi dan target bersama.", "Design", "Design the solution and targets together."),
            AdminValueLoopStep("03", "Penerapan", "Jalankan platform atau solusi kustom.", "Deploy", "Run the platform or a custom solution."),
            AdminValueLoopStep("04", "Keberlanjutan", "Evaluasi berkala, tim makin mandiri.", "Sustain", "Regular reviews as your team becomes self-reliant.")
        )

        val defaultEngagementModels: List<LocalizedText> = listOf(
            LocalizedText("Proyek pendampingan", "Advisory projects"),
            LocalizedText("Layanan analisis terkelola", "Managed intelligence"),
            LocalizedText("Langganan platform", "Platform subscription"),
            LocalizedText("Program penguatan kapabilitas", "Capability programs")
        )
    }
}
