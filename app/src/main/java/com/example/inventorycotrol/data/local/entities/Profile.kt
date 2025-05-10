package com.example.inventorycotrol.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserStatus
import com.example.inventorycotrol.domain.model.user.Profile

data class ProfileDetail(
    @Embedded
    val user: UserEntity,

    @ColumnInfo(name = "organisation_user_name")
    val organisationUserName: String,
    @ColumnInfo(name = "organisation_role")
    val organisationRole: OrganisationRole,
    @ColumnInfo(name = "organisation_user_status")
    val organisationUserStatus: OrganisationUserStatus

)

fun ProfileDetail.mapToDomain() = Profile(
    id = this.user.id,
    fullName = this.user.fullName,
    imageUrl = this.user.imageUrl,
    email = this.user.email,
    organisationRole = this.organisationRole
)