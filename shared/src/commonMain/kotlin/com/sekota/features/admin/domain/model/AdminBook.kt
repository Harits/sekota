package com.sekota.features.admin.domain.model

data class AdminBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverImage: String? = null
)
