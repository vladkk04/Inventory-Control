package com.example.bachelorwork.data.local.dao.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.converters.BaseJsonConverter
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class JsonProductTimelineHistoryConverter: BaseJsonConverter<String, ProductTimelineHistory> {

    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(ProductTimelineHistory::class) {
                subclass(ProductTimelineHistory.ProductTimelineCreate::class)
                subclass(ProductTimelineHistory.ProductTimelineUpdate::class)
            }
        }
    }

    @TypeConverter
    override fun convertTo(value: List<ProductTimelineHistory>): String =
        json.encodeToString(ListSerializer(ProductTimelineHistory.serializer()), value)

    @TypeConverter
    override fun convertFrom(value: String): List<ProductTimelineHistory> =
        json.decodeFromString(ListSerializer(ProductTimelineHistory.serializer()), value)
}