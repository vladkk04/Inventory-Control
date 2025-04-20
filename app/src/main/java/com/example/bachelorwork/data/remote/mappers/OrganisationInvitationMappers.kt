package com.example.bachelorwork.data.remote.mappers

import com.example.bachelorwork.data.remote.dto.OrganisationInvitationDto
import com.example.bachelorwork.domain.model.profile.OrganisationInvitation


fun OrganisationInvitationDto.mapToDomain() =
    OrganisationInvitation(
        id = this.id,
        organisationName = this.organisationName,
        organisationRole = this.organisationRole,
        invitedBy = this.invitedBy,
        status = this.status,
        expireAt = this.expireAt
    )