package com.example.inventorycotrol.domain.repository.local

import com.example.inventorycotrol.data.local.entities.OrganisationDetail
import com.example.inventorycotrol.data.local.entities.OrganisationEntity
import com.example.inventorycotrol.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrganisationLocalDataSource: BaseRoomRepository<OrganisationEntity> {

    fun getAll(): Flow<List<OrganisationEntity>>

    fun getById(id: String): Flow<OrganisationEntity>

    fun getOrganisationDetail(id: String): Flow<OrganisationDetail>

    suspend fun refresh(remoteOrganisation: List<OrganisationEntity>)

}