package com.example.inventorycotrol.data.local.converters

import androidx.room.TypeConverter
import com.example.inventorycotrol.data.local.converters.common.BaseConverter
import java.util.Date

class DateConverter : BaseConverter<Date, Long> {

    @TypeConverter
    override fun convertTo(value: Long): Date = Date(value)

    @TypeConverter
    override fun convertFrom(value: Date): Long = value.time
}