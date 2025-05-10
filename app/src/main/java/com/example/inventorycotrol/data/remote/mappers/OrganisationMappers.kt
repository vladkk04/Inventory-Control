package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.OrganisationEntity
import com.example.inventorycotrol.data.remote.dto.OrganisationDto
import com.example.inventorycotrol.domain.model.organisation.Organisation
import com.example.inventorycotrol.domain.model.organisation.OrganisationRequest


fun OrganisationDto.mapToDomain() =
    Organisation(
        id = this.id,
        name = this.name,
        currency = this.currency,
        description = this.description,
        logoUrl = this.logoUrl,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
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
