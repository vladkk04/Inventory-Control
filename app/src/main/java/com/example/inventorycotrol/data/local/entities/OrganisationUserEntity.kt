package com.example.inventorycotrol.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserStatus

@Entity(tableName = "organisation_user")
data class OrganisationUserEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String?,
    val email: String?,
    @ColumnInfo(name = "organisation_user_name")
    val organisationUserName: String,
    @ColumnInfo(name = "organisation_role")
    val organisationRole: OrganisationRole,
    @ColumnInfo(name = "organisation_user_status")
    val organisationUserStatus: OrganisationUserStatus
)
