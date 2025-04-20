package com.example.bachelorwork.data.local.entities

import androidx.room.Embedded
import com.example.bachelorwork.domain.model.organisation.Organisation

data class OrganisationDetail(
    @Embedded
    val organisation: OrganisationEntity,
    val organisationUserName: String
)

fun OrganisationDetail.mapToDomain() = Organisation(
    id = organisation.id,
    name = organisation.name,
    currency = organisation.currency,
    description = organisation.description,
    logoUrl = organisation.logoUrl,
    createdBy = organisationUserName
)
