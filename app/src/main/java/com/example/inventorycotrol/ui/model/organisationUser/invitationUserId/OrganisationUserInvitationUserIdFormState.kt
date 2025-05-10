package com.example.inventorycotrol.ui.model.organisationUser.invitationUserId

data class OrganisationUserInvitationUserIdFormState(
    val organisationUserName: String = "",
    val userId: String = "",
    val roleId: String = "",
    val organisationUserNameError: String? = null,
    val roleIdError: String? = null,
    val userIdError: String? = null,
)