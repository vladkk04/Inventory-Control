package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.OrganisationDetail
import com.example.bachelorwork.data.local.entities.OrganisationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganisationDao: BaseDao<OrganisationEntity> {

    @Query("SELECT * FROM organisations")
    fun getAll(): Flow<List<OrganisationEntity>>

    @Query("SELECT * FROM organisations WHERE organisations.id = :id")
    fun getById(id: Int): Flow<OrganisationEntity>

    @Query("""
        SELECT 
            o.*,
            ou.organisation_user_name as organisationUserName
        FROM organisations o
        JOIN organisation_user ou ON o.created_by = ou.user_id
        WHERE o.id = :id
    """)
    suspend fun getOrganisationDetail(id: String): OrganisationDetail

    @Query("DELETE FROM organisations WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Transaction
    suspend fun refresh(remoteOrganisation: List<OrganisationEntity>) {
        val remoteIds = remoteOrganisation.map { it.id }
        deleteExcept(remoteIds)
        upsertAll(*remoteOrganisation.toTypedArray())
    }
}