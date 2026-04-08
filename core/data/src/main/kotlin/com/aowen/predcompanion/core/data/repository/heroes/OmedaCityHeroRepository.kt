package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import com.aowen.predcompanion.data.HeroStatistics
import com.aowen.predcompanion.data.create
import javax.inject.Inject

class OmedaCityHeroRepository @Inject constructor(
    private val omedaCityService: OmedaCityService,
) : HeroRepository {
    override suspend fun fetchAllHeroes(): Resource<List<HeroDetails>> = safeApiCall(
        apiCall = { omedaCityService.getAllHeroes() },
        transform = { heroes -> heroes.map { it.asHeroDetails() } }
    )


    override suspend fun fetchHeroByName(heroName: String): Resource<HeroDetails?> = safeApiCall(
        apiCall = { omedaCityService.getHeroByName(heroName) },
        transform = { hero -> hero.asHeroDetails() }
    )

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        safeApiCall(
            apiCall = { omedaCityService.getAllHeroStatistics(timeFrame) },
            transform = { stats -> stats.heroStatistics.map { it.create() } }
        )

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        safeApiCall(
            apiCall = { omedaCityService.getHeroStatisticsById(heroId) },
            transform = { stats -> stats.heroStatistics.firstOrNull()?.create() },
        )

}