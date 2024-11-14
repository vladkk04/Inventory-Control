package com.example.bachelorwork.data.local.dao.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T)

    @Insert
    suspend fun insertAll(vararg obj: T)

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun updateAll(vararg obj: T)

    @Delete
    suspend fun delete(obj: T)

    @Delete
    suspend fun deleteAll(vararg obj: T)
}