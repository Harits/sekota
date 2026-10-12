package com.sekota.utils

import androidx.compose.ui.graphics.Color

/**
 * Single Source of Truth for Book Cover Visual Identity & Background Colors.
 *
 * Guarantees visual consistency across Perpustakaan Digital (Landing Page),
 * E-Book Catalog Screen, and Book Details Screen (Acceptance Criteria Scenario 3).
 */
object BookCoverColors {
    val Blue = Color(0xFF1976D2)       // Manifesto Ekuitas Lahan
    val BrickRed = Color(0xFFBF360C)   // Blind Spot Radar
    val Green = Color(0xFF388E3C)      // CSR Berdampak
    val Purple = Color(0xFF673AB7)     // ESG Strategic Integration
    val Teal = Color(0xFF00838F)
    val Slate = Color(0xFF455A64)

    val Palette = listOf(
        Blue,
        BrickRed,
        Green,
        Purple,
        Teal,
        Slate
    )

    /**
     * Resolves the canonical background color for a given book based on its ID,
     * title, or optional custom coverColorHex.
     */
    fun getColorForBook(bookId: String, title: String = "", coverColorHex: String? = null): Color {
        if (!coverColorHex.isNullOrBlank()) {
            try {
                val cleanHex = coverColorHex.removePrefix("#")
                return Color(cleanHex.toLong(16) or 0x00000000FF000000)
            } catch (_: Exception) {}
        }

        val normalizedId = bookId.lowercase().trim()
        val normalizedTitle = title.lowercase().trim()

        return when {
            normalizedId.contains("manifesto") || normalizedTitle.contains("manifesto") -> Blue
            normalizedId.contains("blind-spot") || normalizedTitle.contains("blind spot") -> BrickRed
            normalizedId.contains("csr") || normalizedTitle.contains("csr") -> Green
            normalizedId.contains("esg") || normalizedTitle.contains("esg") -> Purple
            else -> {
                val hash = kotlin.math.abs((normalizedId.ifBlank { normalizedTitle }).hashCode())
                Palette[hash % Palette.size]
            }
        }
    }
}
