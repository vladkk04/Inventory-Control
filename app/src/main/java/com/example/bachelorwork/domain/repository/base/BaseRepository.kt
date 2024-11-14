package com.example.bachelorwork.domain.repository.base

import kotlinx.coroutines.flow.Flow

interface BaseRepository<T> {

    suspend fun insert(obj: T)

    suspend fun insertAll(vararg obj: T)

    suspend fun update(obj: T)

    suspend fun updateAll(vararg obj: T)

    suspend fun delete(obj: T)

    suspend fun deleteAll(vararg obj: T)

    fun getAll(): Flow<Array<T>>

}