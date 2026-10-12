package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WhySekotaCard(
    val id: String = "",
    val title: String,
    val description: String,
    val iconName: String = "trending"
)

@Serializable
data class WhySekotaConfig(
    val preTitle: String = "MENGAPA SEKOTA",
    val title: String = "Sekota Bukan Sekadar Vendor, Bukan Sekadar Konsultan.",
    val description: String = "Kami berada di persimpangan unik antara teknologi, konteks lokal, dan strategic foresight.",
    val ctaLabel: String = "MULAI KONSULTASI STRATEGIS",
    val ctaTarget: String = "#kontak",
    val cards: List<WhySekotaCard> = defaultCards
) {
    companion object {
        val defaultCards: List<WhySekotaCard> = listOf(
            WhySekotaCard(
                id = "c1",
                title = "Lebih Strategis dari Vendor IT",
                description = "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.",
                iconName = "trending"
            ),
            WhySekotaCard(
                id = "c2",
                title = "Lebih Teknis dari Konsultan",
                description = "Implementasi data real-time, bukan sekadar slide deck rekomendasi.",
                iconName = "chip"
            )
        )
    }
}
