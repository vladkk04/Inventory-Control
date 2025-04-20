package com.example.bachelorwork.domain.usecase.organisationUser

data class OrganisationUserUseCases(
    val getOrganisationUsersUseCase: GetOrganisationUsersUseCase,
    val assignRoleOrganisationUser: AssignRoleOrganisationUser,
    val deleteOrganisationUser: DeleteOrganisationUser,
    val updateOrganisationUser: UpdateOrganisationUser,
    val chaneStatusOrganisationUserUseCase: ChaneStatusOrganisationUserUseCase,
    val invitationUserUseCase: InvitationUserUseCase,
)
