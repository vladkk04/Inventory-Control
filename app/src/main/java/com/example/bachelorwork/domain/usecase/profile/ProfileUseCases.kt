package com.example.bachelorwork.domain.usecase.profile

data class ProfileUseCases(
    val getProfile: GetProfileUseCase,
    val getOrganisationsInviting: GetOrganisationsInviting,
    val acceptOrganisationInvitation: AcceptOrganisationInvitation,
    val declineOrganisationInvitation: DeclineOrganisationInvitation
)
