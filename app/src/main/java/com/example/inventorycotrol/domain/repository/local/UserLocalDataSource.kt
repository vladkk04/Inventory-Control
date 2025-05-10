package com.example.inventorycotrol.domain.repository.local

import com.example.inventorycotrol.data.local.entities.ProfileDetail
import com.example.inventorycotrol.data.local.entities.UserEntity
import com.example.inventorycotrol.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource: BaseRoomRepository<UserEntity> {

    fun getUserById(userId: String): Flow<UserEntity?>

    fun getProfile(userId: String): Flow<ProfileDetail?>

}