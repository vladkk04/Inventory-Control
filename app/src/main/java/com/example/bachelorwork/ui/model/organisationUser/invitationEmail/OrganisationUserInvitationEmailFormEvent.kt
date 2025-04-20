package com.example.bachelorwork.ui.model.organisationUser.invitationEmail

import com.example.bachelorwork.domain.model.organisation.OrganisationRole

sealed class OrganisationUserInvitationEmailFormEvent {
    data class OrganisationUserNameChanged(val organisationUserName: String) : OrganisationUserInvitationEmailFormEvent()
    data class EmailChanged(val email: String) : OrganisationUserInvitationEmailFormEvent()
    data class RoleChanged(val role: OrganisationRole) : OrganisationUserInvitationEmailFormEvent()
    data object Invite: OrganisationUserInvitationEmailFormEvent()
}