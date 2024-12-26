package com.example.bachelorwork.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
) {
    companion object{
        const val TABLE_NAME = "users"
    }
}
