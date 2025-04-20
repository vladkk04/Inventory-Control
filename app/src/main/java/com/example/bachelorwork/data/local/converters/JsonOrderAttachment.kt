package com.example.bachelorwork.data.local.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.converters.common.BaseJsonConverter
import com.example.bachelorwork.domain.model.order.Attachment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonOrderAttachment: BaseJsonConverter<String, Attachment> {

    @TypeConverter
    override fun convertTo(value: List<Attachment>): String = Json.encodeToString(value)

    @TypeConverter
    override fun convertFrom(value: String): List<Attachment> = Json.decodeFromString(value)
}