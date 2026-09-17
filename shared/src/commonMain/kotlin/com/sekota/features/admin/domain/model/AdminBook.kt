package com.sekota.features.admin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null,
    val category: String = "SMART CITY",
    val description: String = "",
    val rating: Double = 0.0,
    val ratingCount: Int = 0, // Registered Readers (Active Collaborators with Accounts)
    val interactions: Int = 0, // Total interactions / tool usages
    val pdfUrl: String? = null,
    val readingTime: String = "3H 45M",
    val pages: Int = 240,
    val publishedDate: String = "Nov 2025",
    val language: String = "Indonesia",
    val year: String = "2025"
)

