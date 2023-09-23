package com.aowen.monolith.network

object RetrofitHelper {

    const val baseUrl = "https://omeda.city/"

    fun getRankImageUrl(imagePath: String): String =
        baseUrl + imagePath

    fun getHeroAbilityImageUrl(imagePath: String): String =
        baseUrl + imagePath

}