package com.example.bachelorwork.data.local.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.converters.common.BaseJsonConverter
import com.example.bachelorwork.domain.model.product.ProductUpdateHistory
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class JsonProductUpdateHistory: BaseJsonConverter<String, ProductUpdateHistory> {

    @TypeConverter
    override fun convertTo(value: List<ProductUpdateHistory>): String =
        Json.encodeToString(ListSerializer(ProductUpdateHistory.serializer()), value)

    @TypeConverter
    override fun convertFrom(value: String): List<ProductUpdateHistory> =
        Json.decodeFromString(ListSerializer(ProductUpdateHistory.serializer()), value)
}