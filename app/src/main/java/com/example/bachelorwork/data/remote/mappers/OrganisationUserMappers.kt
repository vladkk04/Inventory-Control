package com.example.bachelorwork.data.remote.mappers

import com.example.bachelorwork.data.local.entities.OrganisationUserEntity
import com.example.bachelorwork.data.remote.dto.OrganisationUserDto
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUser


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