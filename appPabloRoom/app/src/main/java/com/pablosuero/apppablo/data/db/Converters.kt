package com.pablosuero.apppablo.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Convierte entre List<String> y su representación JSON para Room.
 * Es necesario para almacenar la lista de ingredientes.
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromList(ingredients: List<String>?): String =
        gson.toJson(ingredients)

    @TypeConverter
    fun toList(data: String?): List<String> {
        if (data.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, type)
    }
}
