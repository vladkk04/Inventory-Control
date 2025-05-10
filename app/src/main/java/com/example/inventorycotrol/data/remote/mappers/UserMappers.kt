package com.example.inventorycotrol.data.remote.mappers

import com.example.inventorycotrol.data.local.entities.UserEntity
import com.example.inventorycotrol.data.remote.dto.UserDto

fun UserDto.mapToEntity() = UserEntity(
    id = this.id,
    fullName = this.fullName,
    email = this.email,
    imageUrl = this.imageUrl
)