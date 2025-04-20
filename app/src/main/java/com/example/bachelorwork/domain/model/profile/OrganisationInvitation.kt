package com.example.bachelorwork.domain.model.profile

import com.example.bachelorwork.data.remote.dto.StatusInvitation
import com.example.bachelorwork.domain.model.organisation.OrganisationRole

data class OrganisationInvitation(
    val id: String,
    val organisationName: String,
    val organisationRole: OrganisationRole,
    val status: StatusInvitation,
    val invitedBy: String,
    val expireAt: Long
)
