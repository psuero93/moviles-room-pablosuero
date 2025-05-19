package com.pablosuero.apppablo

import android.app.Application
import androidx.room.Room
import com.pablosuero.apppablo.data.db.MealDatabase

/**
 * Clase Application para inicializar Room al arrancar la app
 */
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    lateinit var db: MealDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            this,
            MealDatabase::class.java,
            "meal-db"
        )
            .build()
    }
}
