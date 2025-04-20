package com.example.bachelorwork.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserStatus
import com.example.bachelorwork.domain.model.user.Profile

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
    organisationRole = this.organisationRole
)