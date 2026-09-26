package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminValueLoopStep(
    val stepNumber: String,
    val title: String,
    val subtitle: String
)

@Serializable
data class ValueLoopConfig(
    val sectionEyebrow: String = "SOLUSI KAMI",
    val title: String = "Strategic Value Loop",
    val description: String = "Setiap solusi Sekota dirancang dalam satu siklus tertutup yang memastikan data berubah menjadi keputusan strategis.",
    val steps: List<AdminValueLoopStep> = defaultSteps
) {
    companion object {
        val defaultSteps: List<AdminValueLoopStep> = listOf(
            AdminValueLoopStep("01", "INTENT", "Definisi Strategis"),
            AdminValueLoopStep("02", "EXECUTION", "Implementasi Data"),
            AdminValueLoopStep("03", "VALUE", "Penciptaan Nilai"),
            AdminValueLoopStep("04", "MEASUREMENT", "Audit Dampak"),
            AdminValueLoopStep("05", "LEARNING", "Optimasi Berkelanjutan")
        )
    }
}
