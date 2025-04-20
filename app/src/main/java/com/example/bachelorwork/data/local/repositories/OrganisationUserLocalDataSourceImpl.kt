package com.example.bachelorwork.data.local.repositories

import com.example.bachelorwork.data.local.dao.OrganisationUserDao
import com.example.bachelorwork.data.local.entities.OrganisationUserEntity
import com.example.bachelorwork.domain.repository.local.OrganisationUserLocalDataSource
import kotlinx.coroutines.flow.Flow

class OrganisationUserLocalDataSourceImpl(
    private val dao: OrganisationUserDao,
): OrganisationUserLocalDataSource {

    override fun getAll(): Flow<List<OrganisationUserEntity>> = dao.getAll()

    override fun getById(id: String): Flow<OrganisationUserEntity> = dao.getById(id)

    override fun getByUserId(id: String): Flow<OrganisationUserEntity> = dao.getByUserId(id)

    override suspend fun refresh(organisationUsers: List<OrganisationUserEntity>) = dao.refresh(organisationUsers)

    override suspend fun insert(obj: OrganisationUserEntity) = dao.insert(obj)

    override suspend fun insertAll(vararg obj: OrganisationUserEntity) = dao.insertAll(*obj)

    override suspend fun update(obj: OrganisationUserEntity) = dao.update(obj)

    override suspend fun updateAll(vararg obj: OrganisationUserEntity) = dao.updateAll(*obj)

    override suspend fun upsert(obj: OrganisationUserEntity) = dao.upsert(obj)

    override suspend fun upsertAll(vararg obj: OrganisationUserEntity) = dao.upsertAll(*obj)

    override suspend fun delete(obj: OrganisationUserEntity) = dao.delete(obj)

    override suspend fun deleteAll(vararg obj: OrganisationUserEntity) = dao.deleteAll(*obj)


}