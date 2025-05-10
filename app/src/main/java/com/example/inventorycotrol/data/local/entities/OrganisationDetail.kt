package com.example.inventorycotrol.data.local.entities

import androidx.room.Embedded

data class OrganisationDetail(
    @Embedded
    val organisation: OrganisationEntity,
    val organisationUserName: String
)
