package com.example.inventorycotrol.domain.model.organisation

data class Organisation(
    val id: String,
    val name: String,
    val currency: String,
    val description: String,
    val logoUrl: String? = null,
    val createdBy: String,
    val createdAt: Long
)



