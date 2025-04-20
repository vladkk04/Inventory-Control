package com.example.bachelorwork.ui.model.organisationUser.invitationUserId

sealed class OrganisationUserInvitationUserIdFormEvent {
    data class OrganisationUserNameChanged(val organisationUserName: String) : OrganisationUserInvitationUserIdFormEvent()
    data class UserIdChanged(val userId: String) : OrganisationUserInvitationUserIdFormEvent()
    data class RoleChanged(val roleId: String) : OrganisationUserInvitationUserIdFormEvent()
    data object Invite: OrganisationUserInvitationUserIdFormEvent()
}
