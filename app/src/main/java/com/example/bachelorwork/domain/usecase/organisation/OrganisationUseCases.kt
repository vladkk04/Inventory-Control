package com.example.bachelorwork.domain.usecase.organisation

data class OrganisationUseCases(
    val get: GetOrganisationsUseCase,
    val create: CreateOrganisationUseCase,
    val delete: DeleteOrganisationUseCase,
    val switch: SwitchOrganisationUseCase
)
