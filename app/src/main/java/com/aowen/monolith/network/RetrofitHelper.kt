package com.aowen.monolith.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "https://omeda.city/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val playersApi: OmedaCityApi by lazy {
        retrofit.create(OmedaCityApi::class.java)
    }

    fun getRankImageUrl(imagePath: String): String =
        baseUrl + imagePath

}