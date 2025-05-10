package com.example.inventorycotrol.domain.model.profile

import com.example.inventorycotrol.data.remote.dto.StatusInvitation
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole

data class OrganisationInvitation(
    val id: String,
    val organisationName: String,
    val organisationRole: OrganisationRole,
    val status: StatusInvitation,
    val invitedBy: String,
    val expireAt: Long
)
