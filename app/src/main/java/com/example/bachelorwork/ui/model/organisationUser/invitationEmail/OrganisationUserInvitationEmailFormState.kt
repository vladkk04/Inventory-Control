package com.example.bachelorwork.ui.model.organisationUser.invitationEmail

data class OrganisationUserInvitationEmailFormState(
    val organisationUserName: String = "",
    val roleName: String = "",
    val email: String = "",
    val organisationUserNameError: String? = null,
    val roleIdError: String? = null,
    val emailError: String? = null,
)