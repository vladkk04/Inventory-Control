package com.example.inventorycotrol.domain.usecase.organisation

data class OrganisationUseCases(
    val get: GetOrganisationsUseCase,
    val create: CreateOrganisationUseCase,
    val delete: DeleteOrganisationUseCase,
    val update: UpdateOrganisationUseCase,
    val switch: SwitchOrganisationUseCase
)
