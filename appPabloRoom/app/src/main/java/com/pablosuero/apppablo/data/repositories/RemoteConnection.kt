package com.pablosuero.apppablo.data.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RemoteConnection {
    private val builder = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: RemoteService = builder.create(RemoteService::class.java)
}