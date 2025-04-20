package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.OrganisationUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganisationUserDao: BaseDao<OrganisationUserEntity> {

    @Query("SELECT * FROM organisation_user")
    fun getAll(): Flow<List<OrganisationUserEntity>>

    @Query("SELECT * FROM organisation_user WHERE id = :id")
    fun getById(id: String): Flow<OrganisationUserEntity>

    @Query("SELECT * FROM organisation_user WHERE user_id = :id")
    fun getByUserId(id: String): Flow<OrganisationUserEntity>

    @Query("DELETE FROM organisation_user WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Transaction
    suspend fun refresh(organisationUser: List<OrganisationUserEntity>) {
        val validIds = organisationUser.map { it.id }
        deleteExcept(validIds)
        upsertAll(*organisationUser.toTypedArray())
    }
}