package com.example.inventorycotrol.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventorycotrol.domain.model.order.Attachment
import com.example.inventorycotrol.domain.model.order.OrderDiscount

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(
    @PrimaryKey
    val id: String,
    @Embedded
    val discount: OrderDiscount,
    val comment: String? = null,
    val attachments: List<Attachment>,
    @ColumnInfo(name = "organisation_id")
    val organisationId: String,
    @ColumnInfo(name = "ordered_at")
    val orderedAt: Long,
    @ColumnInfo(name = "ordered_by")
    val orderedBy: String,
) {
    companion object {
        const val TABLE_NAME = "orders"
    }
}
