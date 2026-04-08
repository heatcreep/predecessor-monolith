package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.data.HeroStatistics
import com.aowen.predcompanion.core.network.Resource

interface HeroRepository {
    suspend fun fetchAllHeroes(): Resource<List<HeroDetails>>
    suspend fun fetchHeroByName(heroName: String): Resource<HeroDetails?>
    suspend fun fetchAllHeroStatistics(timeFrame: String? = "1M"): Resource<List<HeroStatistics>>
    suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?>
}