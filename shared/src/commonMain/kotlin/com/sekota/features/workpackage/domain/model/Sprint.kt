package com.sekota.features.workpackage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Sprint(
    val sprintName: String,
    val startDate: String,
    val endDate: String
)
