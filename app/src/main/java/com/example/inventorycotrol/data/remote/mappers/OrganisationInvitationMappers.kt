package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.remote.dto.OrganisationInvitationDto
import com.example.inventorycotrol.domain.model.profile.OrganisationInvitation


fun OrganisationInvitationDto.mapToDomain() =
    OrganisationInvitation(
        id = this.id,
        organisationName = this.organisationName,
        organisationRole = this.organisationRole,
        invitedBy = this.invitedBy,
        status = this.status,
        expireAt = this.expireAt
    )