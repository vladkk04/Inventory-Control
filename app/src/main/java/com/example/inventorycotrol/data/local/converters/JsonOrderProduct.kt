package com.example.inventorycotrol.data.local.converters

import androidx.room.TypeConverter
import com.example.inventorycotrol.data.local.converters.common.BaseJsonConverter
import com.example.inventorycotrol.domain.model.order.OrderProduct
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class JsonOrderProduct: BaseJsonConverter<String, OrderProduct> {

    @TypeConverter
    override fun convertTo(value: List<OrderProduct>): String =
        Json.encodeToString(ListSerializer(OrderProduct.serializer()), value)

    @TypeConverter
    override fun convertFrom(value: String): List<OrderProduct> =
        Json.decodeFromString(ListSerializer(OrderProduct.serializer()), value)
}