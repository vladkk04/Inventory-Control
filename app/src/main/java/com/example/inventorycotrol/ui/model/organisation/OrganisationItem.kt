package com.example.inventorycotrol.ui.model.organisation

data class OrganisationItem(
    val id: String,
    val name: String,
    val currency: String,
    val description: String,
    val logoUrl: String? = null,
    val createdBy: String,
    val createdAt: Long,
    val isSelected: Boolean = false
)