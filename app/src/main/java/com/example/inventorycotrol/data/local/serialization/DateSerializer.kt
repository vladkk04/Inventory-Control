package com.example.inventorycotrol.data.local.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateSerializer: KSerializer<Date> {
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)

    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
}