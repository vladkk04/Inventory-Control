package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.remote.dto.OrganisationSettingsDto
import com.example.inventorycotrol.domain.model.organisation.settings.OrganisationSettings

fun OrganisationSettingsDto.mapToDomain(): OrganisationSettings =
    OrganisationSettings(
        id = this.id,
        notificationSettings = this.notificationSettings,
        thresholdSettings = this.thresholdSettings,
        organisationId = this.organisationId
    )