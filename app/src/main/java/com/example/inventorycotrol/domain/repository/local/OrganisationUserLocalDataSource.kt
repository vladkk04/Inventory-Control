package com.example.inventorycotrol.domain.repository.local

import com.example.inventorycotrol.data.local.entities.OrganisationUserEntity
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrganisationUserLocalDataSource: BaseRoomRepository<OrganisationUserEntity> {

    fun getAll(): Flow<List<OrganisationUserEntity>>

    fun getById(id: String): Flow<OrganisationUserEntity>

    fun getByUserId(id: String): Flow<OrganisationUserEntity>

    suspend fun deleteById(id: String)

    suspend fun updateUserName(id: String, name: String)

    suspend fun updateUserRole(id: String, role: OrganisationRole)

    suspend fun refresh(organisationUsers: List<OrganisationUserEntity>)

    suspend fun deleteByUserId(id: String)

    suspend fun deleteByEmail(id: String)

   /* suspend fun refresh(productRemote: List<ProductEntity>)

    suspend fun deleteById(id: String)*/
}
