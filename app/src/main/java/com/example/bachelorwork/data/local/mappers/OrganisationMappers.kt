package com.example.bachelorwork.data.local.mappers

import com.example.bachelorwork.data.local.entities.OrganisationEntity
import com.example.bachelorwork.domain.model.organisation.Organisation

fun OrganisationEntity.mapToDomain() = Organisation(
    id = this.id,
    name = this.name,
    currency = this.currency,
    description = this.description,
    logoUrl = this.logoUrl,
    createdBy = this.createdBy
)