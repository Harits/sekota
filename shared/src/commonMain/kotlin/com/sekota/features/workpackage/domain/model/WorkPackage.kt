package com.sekota.features.workpackage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkPackage(
    val localId: String,
    val opId: Int?,
    val subject: String,
    val status: String,
    val lockVersion: Int?,
    val assignee: String?,
    val sprintName: String
)
