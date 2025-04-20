package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.ProfileDetail
import com.example.bachelorwork.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: String): Flow<UserEntity>

    @Query("""
    SELECT 
        u.*,
        ou.organisation_user_name AS organisation_user_name,
        ou.organisation_role AS organisation_role,
        ou.organisation_user_status AS organisation_user_status
    FROM users u 
    JOIN organisation_user ou
    WHERE u.id = :userId
""")
    fun getProfile(userId: String): Flow<ProfileDetail?>

}