package com.gaurav.ainotificationassistant.data.local.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        return value?.split(",") ?: emptyList()
    }

    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
