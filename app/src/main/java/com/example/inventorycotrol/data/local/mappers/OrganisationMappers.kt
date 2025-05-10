package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.OrganisationDetail
import com.example.inventorycotrol.data.local.entities.OrganisationEntity
import com.example.inventorycotrol.domain.model.organisation.Organisation

fun OrganisationEntity.mapToDomain() = Organisation(
    id = this.id,
    name = this.name,
    currency = this.currency,
    description = this.description,
    logoUrl = this.logoUrl,
    createdBy = this.createdBy,
    createdAt = this.createdAt
)

fun OrganisationDetail.mapToDomain() = Organisation(
    id = organisation.id,
    name = organisation.name,
    currency = organisation.currency,
    description = organisation.description,
    logoUrl = organisation.logoUrl,
    createdBy = organisationUserName,
    createdAt = organisation.createdAt
)

fun Organisation.mapToEntity() = OrganisationEntity(
    id = this.id,
    name = this.name,
    currency = this.currency,
    description = this.description,
    logoUrl = this.logoUrl,
    createdBy = this.createdBy,
    createdAt = this.createdAt
)