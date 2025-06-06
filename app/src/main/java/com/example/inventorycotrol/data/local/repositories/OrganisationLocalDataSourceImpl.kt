package com.example.inventorycotrol.data.local.repositories

import com.example.inventorycotrol.data.local.dao.OrganisationDao
import com.example.inventorycotrol.data.local.entities.OrganisationDetail
import com.example.inventorycotrol.data.local.entities.OrganisationEntity
import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganisationLocalDataSourceImpl @Inject constructor(
    private val dao: OrganisationDao
) : OrganisationLocalDataSource {

    override fun getAll(): Flow<List<OrganisationEntity>> = dao.getAll()

    override fun getById(id: String): Flow<OrganisationEntity> = dao.getById(id)

    override fun getOrganisationDetail(id: String): Flow<OrganisationDetail> = dao.getOrganisationDetail(id)

    override suspend fun refresh(remoteOrganisation: List<OrganisationEntity>) = dao.refresh(remoteOrganisation)

    override suspend fun insert(obj: OrganisationEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: OrganisationEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: OrganisationEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: OrganisationEntity) = dao.updateAll(*obj)

    override suspend fun upsert(obj: OrganisationEntity) = dao.upsert(obj)

    override suspend fun upsertAll(vararg obj: OrganisationEntity) = dao.upsertAll(*obj)

    override suspend fun delete(obj: OrganisationEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: OrganisationEntity) = dao.deleteAll(*obj)


}