package com.example.inventorycotrol.data.local.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.example.inventorycotrol.data.local.converters.common.BaseConverter

class UriConverter: BaseConverter<Uri, String> {

    @TypeConverter
    override fun convertTo(value: String): Uri = Uri.parse(value)

    @TypeConverter
    override fun convertFrom(value: Uri): String = value.toString()
}