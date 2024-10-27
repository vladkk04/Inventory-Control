package com.example.bachelorwork.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bachelorwork.domain.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM productList")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM productList WHERE id = :id")
    fun getProductById(id: Int): Product

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}