package com.example.bachelorwork.data.local.dao.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.converters.BaseJsonConverter
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonOrderSubItemConverter: BaseJsonConverter<String, OrderProductSubItem> {

    @TypeConverter
    override fun convertTo(value: List<OrderProductSubItem>): String = Json.encodeToString(value)

    @TypeConverter
    override fun convertFrom(value: String): List<OrderProductSubItem> = Json.decodeFromString(value)
}