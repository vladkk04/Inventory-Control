package com.example.bachelorwork.data.local.dao.converters

import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.converters.BaseConverter
import java.util.Date

class DateConverter : BaseConverter<Date, Long> {
    @TypeConverter
    override fun convertTo(value: Long): Date = Date(value)

    @TypeConverter
    override fun convertFrom(value: Date): Long = value.time
}