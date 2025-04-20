package com.example.bachelorwork.domain.repository.local

import com.example.bachelorwork.data.local.entities.OrganisationDetail
import com.example.bachelorwork.data.local.entities.OrganisationEntity
import com.example.bachelorwork.domain.repository.base.BaseRoomRepository
import kotlinx.coroutines.flow.Flow

interface OrganisationLocalDataSource: BaseRoomRepository<OrganisationEntity> {

    fun getAll(): Flow<List<OrganisationEntity>>

    fun getById(id: Int): Flow<OrganisationEntity>

    suspend fun getOrganisationDetail(id: String): OrganisationDetail

    suspend fun refresh(remoteOrganisation: List<OrganisationEntity>)

}