package com.example.bachelorwork.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val email: String,
    @ColumnInfo("image_url")
    val imageUrl: String?
)
