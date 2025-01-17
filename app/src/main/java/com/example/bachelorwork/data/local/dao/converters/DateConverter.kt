package com.example.bachelorwork.data.local.dao.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.converters.BaseConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

class DateConverter : BaseConverter<Date, Long> {

    @TypeConverter
    override fun convertTo(value: Long): Date = Date(value)

    @TypeConverter
    override fun convertFrom(value: Date): Long = value.time
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateSerializer: KSerializer<Date> {

    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)


    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())

}