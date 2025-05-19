package com.pablosuero.apppablo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {
    @Query("SELECT * FROM MealDB")
    suspend fun getAll(): List<MealDB>

    @Query("SELECT * FROM MealDB WHERE id = :id")
    suspend fun findById(id: Int): MealDB?

    @Query("SELECT COUNT(id) FROM MealDB")
    suspend fun mealCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeals(meals: List<MealDB>)
}
