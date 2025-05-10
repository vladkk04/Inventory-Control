package com.example.inventorycotrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.inventorycotrol.data.local.dao.common.BaseDao
import com.example.inventorycotrol.data.local.entities.OrderEntity
import com.example.inventorycotrol.data.local.entities.OrderProductJoin
import com.example.inventorycotrol.data.local.entities.RawOrderData
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    @Insert
    suspend fun insertJoin(obj: OrderProductJoin)

    @Upsert
    suspend fun insertAllJoin(vararg obj: OrderProductJoin)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteById(orderId: String)

    @Query("""
        SELECT 
            o.*,
            
            p.id as product_id,
            p.name as product_name,
            p.image_url as product_image_url,
            p.unit as product_unit,
            
            op.quantity as quantity,
            op.price as price
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