package com.example.bachelorwork.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorwork.data.local.dao.common.BaseDao
import com.example.bachelorwork.data.local.entities.OrderEntity
import com.example.bachelorwork.data.local.entities.OrderProductJoin
import com.example.bachelorwork.data.local.entities.RawOrderData
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    @Insert
    suspend fun insert(obj: OrderProductJoin)

    @Query("""
        SELECT 
            o.*,
            
            p.id as product_id,
            p.name as product_name,
            p.image_url as product_image_url,
            p.unit as product_unit,
            
            op.quantity,
            op.price
        FROM orders o
        JOIN order_product_join op ON o.id = op.order_id
        JOIN products p ON op.product_id = p.id
        WHERE o.id = :orderId
    """)
    suspend fun getRawOrderData(orderId: String): List<RawOrderData>


    @Query("""
        SELECT 
            o.*,
            p.id as product_id,
            p.name as product_name,
            p.image_url as product_image_url,
            p.unit as product_unit,
            
            op.quantity,
            op.price
        FROM orders o
        JOIN order_product_join op ON o.id = op.order_id
        JOIN products p ON op.product_id = p.id
    """)
    fun getAll(): Flow<List<RawOrderData>>

    @Query("DELETE FROM orders WHERE id NOT IN (:validIds)")
    suspend fun deleteExcept(validIds: List<String>)

    @Transaction
    suspend fun refresh(orderRemote: List<OrderEntity>) {
        val validIds = orderRemote.map { it.id }
        deleteExcept(validIds)
        upsertAll(*orderRemote.toTypedArray())
    }

}