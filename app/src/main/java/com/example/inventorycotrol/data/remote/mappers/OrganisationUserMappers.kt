package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.OrganisationUserEntity
import com.example.inventorycotrol.data.remote.dto.OrganisationUserDto
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser


fun OrganisationUserDto.mapToEntity() = OrganisationUserEntity(
    id = this.id,
    userId = this.userId,
    email = this.email,
    organisationUserName = this.organisationUserName,
    organisationRole = this.organisationRole,
    organisationUserStatus = this.organisationUserStatus,
)

fun OrganisationUserDto.mapToDomain() = OrganisationUser(
    id = this.id,
    organisationUserName = this.organisationUserName,
    organisationRole = this.organisationRole,
    organisationUserStatus = this.organisationUserStatus,
    userId = this.userId,
    email = this.email
)