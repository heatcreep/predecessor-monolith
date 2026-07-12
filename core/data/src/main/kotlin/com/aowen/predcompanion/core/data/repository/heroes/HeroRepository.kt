package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.Resource
import kotlinx.coroutines.flow.StateFlow

interface HeroRepository {
    val allHeroes: StateFlow<Map<Long, HeroDetails>>
    fun getHeroName(heroId: Long): String
    fun getHeroByName(heroName: String): HeroDetails?
    fun getHeroImageSrcById(heroId: Long): String
    suspend fun fetchAllHeroes()
    suspend fun fetchAllHeroStatistics(timeFrame: String? = "1M"): Resource<List<HeroStatistics>>
    suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?>
}