package com.example.bachelorwork.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "organisations")
data class OrganisationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val currency: String,
    val description: String,
    @ColumnInfo(name = "logo_url")
    val logoUrl: String?,
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
