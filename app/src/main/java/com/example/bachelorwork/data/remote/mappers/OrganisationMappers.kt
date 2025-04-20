package com.example.bachelorwork.data.remote.mappers

import com.example.bachelorwork.data.local.entities.OrganisationEntity
import com.example.bachelorwork.data.remote.dto.OrganisationDto
import com.example.bachelorwork.domain.model.organisation.Organisation
import com.example.bachelorwork.domain.model.organisation.OrganisationRequest


fun OrganisationDto.mapToDomain() =
    Organisation(
        id = this.id,
        name = this.name,
        currency = this.currency,
        description = this.description,
        logoUrl = this.logoUrl,
        createdBy = this.createdBy
    )


fun OrganisationDto.mapToEntity() =
    OrganisationEntity(
        id = this.id,
        name = this.name,
        currency = this.currency,
        description = this.description,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        logoUrl = this.logoUrl,
    )

fun Organisation.mapToRequest() = OrganisationRequest(
    name = this.name,
    currency = this.currency,
    description = this.description,
    logoUrl = this.logoUrl
)
