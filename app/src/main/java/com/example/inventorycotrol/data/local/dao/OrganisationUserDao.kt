package com.example.inventorycotrol.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventorycotrol.data.local.dao.common.BaseDao
import com.example.inventorycotrol.data.local.entities.OrganisationUserEntity
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganisationUserDao: BaseDao<OrganisationUserEntity> {

    @Query("SELECT * FROM organisation_user")
    fun getAll(): Flow<List<OrganisationUserEntity>>

    @Query("SELECT * FROM organisation_user WHERE id = :id")
    fun getById(id: String): Flow<OrganisationUserEntity>

    @Query("SELECT * FROM organisation_user WHERE user_id = :id")
    fun getByUserId(id: String): Flow<OrganisationUserEntity>

    @Query("DELETE FROM organisation_user WHERE user_id = :id")
    suspend fun deleteByUserId(id: String)

    @Query("DELETE FROM organisation_user WHERE email = :email")
    suspend fun deleteByEmail(email: String)

    @Query("UPDATE organisation_user SET organisation_user_name = :name WHERE id = :id")
    suspend fun updateUserName(id: String, name: String)

    @Query("DELETE FROM organisation_user WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Query("DELETE FROM organisation_user WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE organisation_user SET organisation_role = :role WHERE id = :id")
    suspend fun updateUserRole(id: String, role: OrganisationRole)

    @Transaction
    suspend fun refresh(organisationUser: List<OrganisationUserEntity>) {
        val validIds = organisationUser.map { it.id }
        deleteExcept(validIds)
        upsertAll(*organisationUser.toTypedArray())
    }
}