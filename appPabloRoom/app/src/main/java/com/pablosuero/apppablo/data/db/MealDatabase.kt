package com.pablosuero.apppablo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pablosuero.apppablo.data.db.Converters

@Database(
    entities = [MealDB::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}
