package com.example.inventorycotrol.data.local.converters

import androidx.room.TypeConverter
import com.example.inventorycotrol.data.local.converters.common.BaseJsonConverter
import com.example.inventorycotrol.domain.model.product.ProductTag
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonProductTagConverter: BaseJsonConverter<String, ProductTag> {

    @TypeConverter
    override fun convertTo(value: List<ProductTag>): String = Json.encodeToString(value)

    @TypeConverter
    override fun convertFrom(value: String): List<ProductTag> = Json.decodeFromString(value)
}