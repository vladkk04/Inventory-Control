package com.example.inventorycotrol.domain.usecase.profile

data class ProfileUseCases(
    val getProfile: GetProfileUseCase,
    val changeEmail: ChangeEmailUseCase,
    val changePassword: ChangePasswordUseCase,
    val changeUserInfo: ChangeUserInfoUseCase,
    val getOrganisationsInviting: GetOrganisationsInviting,
    val acceptOrganisationInvitation: AcceptOrganisationInvitation,
    val declineOrganisationInvitation: DeclineOrganisationInvitation
)
