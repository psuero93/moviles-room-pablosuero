package com.pablosuero.apppablo.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
// Representa la tabla de comidas almacenadas en la base de datos
data class MealDB(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val ingredients: List<String> // Lista de ingredientes, se guarda con un TypeConverter
)