package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.OrganisationUserEntity
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser

fun OrganisationUserEntity.mapToDomain() = OrganisationUser(
    id = this.id,
    organisationUserName = this.organisationUserName,
    organisationRole = this.organisationRole,
    organisationUserStatus = this.organisationUserStatus,
    userId = this.userId,
    email = this.email
)