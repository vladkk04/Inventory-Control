package com.example.bachelorwork.data.local.dao.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.converters.BaseJsonConverter
import com.example.bachelorwork.domain.model.product.ProductTag
import com.google.gson.Gson

class JsonProductTagConverter: BaseJsonConverter<String, ProductTag> {
    @TypeConverter
    override fun convertTo(value: List<ProductTag>): String = Gson().toJson(value)

    @TypeConverter
    override fun convertFrom(value: String): List<ProductTag> =
        Gson().fromJson(value, Array<ProductTag>::class.java).toList()
}