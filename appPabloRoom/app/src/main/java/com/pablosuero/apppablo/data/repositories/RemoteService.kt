package com.pablosuero.apppablo.data.repositories

import com.pablosuero.apppablo.data.repositories.models.RemoteResult
import retrofit2.http.GET

interface RemoteService {
    @GET("random.php")
    suspend fun getRandomMeal(): RemoteResult
}