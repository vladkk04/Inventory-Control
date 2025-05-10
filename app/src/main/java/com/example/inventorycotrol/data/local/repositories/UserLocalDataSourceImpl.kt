package com.example.inventorycotrol.data.local.repositories

import com.example.inventorycotrol.data.local.dao.UserDao
import com.example.inventorycotrol.data.local.entities.ProfileDetail
import com.example.inventorycotrol.data.local.entities.UserEntity
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
): UserLocalDataSource {

    override fun getUserById(userId: String): Flow<UserEntity?> = userDao.getById(userId)

    override fun getProfile(userId: String): Flow<ProfileDetail?> = userDao.getProfile(userId)

    override suspend fun insert(obj: UserEntity) = userDao.insert(obj)

    override suspend fun insertAll(vararg obj: UserEntity) = userDao.insertAll(*obj)

    override suspend fun update(obj: UserEntity) = userDao.update(obj)

    override suspend fun updateAll(vararg obj: UserEntity) = userDao.updateAll(*obj)

    override suspend fun upsert(obj: UserEntity) = userDao.upsert(obj)

    override suspend fun upsertAll(vararg obj: UserEntity) = userDao.upsertAll(*obj)

    override suspend fun delete(obj: UserEntity) = userDao.delete(obj)

    override suspend fun deleteAll(vararg obj: UserEntity) = userDao.deleteAll(*obj)
}