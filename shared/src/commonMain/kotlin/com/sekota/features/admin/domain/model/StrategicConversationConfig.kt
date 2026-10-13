package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StrategicConversationConfig(
    val title: String = "Mulai Percakapan\nStrategis.",
    val subtitle: String = "Tim kami akan merespons dalam 1x24 jam kerja untuk mendiskusikan kebutuhan Anda.",
    val address: String = "Jl. H. Rasam No.96, Parigi Baru,\nPd. Aren, Tangerang Selatan 15228",
    val email: String = "sales@sekota.id",
    val website: String = "www.sekota.id",
    val nameLabel: String = "NAMA LENGKAP",
    val namePlaceholder: String = "Budi Santoso",
    val emailLabel: String = "EMAIL BISNIS",
    val emailPlaceholder: String = "budi@instansi.go.id",
    val messageLabel: String = "PESAN / KEBUTUHAN STRATEGIS",
    val messagePlaceholder: String = "Ceritakan tantangan Anda...",
    val submitButtonLabel: String = "Kirim & Mulai Konsultasi"
)
