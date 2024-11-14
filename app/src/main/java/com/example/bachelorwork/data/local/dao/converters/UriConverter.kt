package com.example.bachelorwork.data.local.dao.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.example.bachelorwork.data.local.dao.base.convertors.BaseConvertor

class UriConverter: BaseConvertor<Uri, String> {
    @TypeConverter
    override fun convertTo(value: String): Uri = Uri.parse(value)

    @TypeConverter
    override fun convertFrom(value: Uri): String = value.toString()
}