package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.OrganisationUserEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrganisationUserLocalDataSource: BaseRoomRepository<OrganisationUserEntity> {

    fun getAll(): Flow<List<OrganisationUserEntity>>

    fun getById(id: String): Flow<OrganisationUserEntity>

    fun getByUserId(id: String): Flow<OrganisationUserEntity>

    suspend fun refresh(organisationUsers: List<OrganisationUserEntity>)

   /* suspend fun refresh(productRemote: List<ProductEntity>)

    suspend fun deleteById(id: String)*/
}
