package com.example.inventorycotrol.data.local.mappers

import com.example.inventorycotrol.data.local.entities.UserEntity
import com.example.inventorycotrol.domain.model.user.User


fun UserEntity.mapToDomain() = User(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    imageUrl = this.imageUrl
)