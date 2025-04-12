package com.hillbeater.bookxpert.database

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromMap(value: Map<String, Any>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMap(value: String?): Map<String, Any>? {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(value, type)
    }
}