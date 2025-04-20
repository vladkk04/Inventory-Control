package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.ProfileDetail
import com.example.bachelorwork.data.local.entities.UserEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource: BaseRoomRepository<UserEntity> {

    fun getProfile(userId: String): Flow<ProfileDetail?>

}