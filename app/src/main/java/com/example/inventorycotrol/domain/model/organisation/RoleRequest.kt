package com.example.inventorycotrol.domain.model.organisation

import kotlinx.serialization.Serializable

@Serializable
data class RoleRequest(
    val name: String,
    val description: String,
)
