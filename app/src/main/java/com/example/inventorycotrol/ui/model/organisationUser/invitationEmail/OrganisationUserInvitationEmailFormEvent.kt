package com.example.inventorycotrol.ui.model.organisationUser.invitationEmail

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole

sealed class OrganisationUserInvitationEmailFormEvent {
    data class OrganisationUserNameChanged(val organisationUserName: String) : OrganisationUserInvitationEmailFormEvent()
    data class EmailChanged(val email: String) : OrganisationUserInvitationEmailFormEvent()
    data class RoleChanged(val role: OrganisationRole) : OrganisationUserInvitationEmailFormEvent()
    data object Invite: OrganisationUserInvitationEmailFormEvent()
}