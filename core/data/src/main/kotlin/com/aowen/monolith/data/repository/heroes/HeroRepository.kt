package com.aowen.monolith.data.repository.heroes

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.network.Resource

interface HeroRepository {
    suspend fun fetchAllHeroes(): Resource<List<HeroDetails>>
    suspend fun fetchHeroByName(heroName: String): Resource<HeroDetails?>
    suspend fun fetchAllHeroStatistics(timeFrame: String? = "1M"): Resource<List<HeroStatistics>>
    suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?>
}