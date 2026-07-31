package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeHeroRepository : HeroRepository {

    private val _allHeroes = MutableStateFlow<Map<String, HeroDetails>>(emptyMap())
    override val allHeroes: StateFlow<Map<String, HeroDetails>> = _allHeroes

    var heroesToReturn: List<HeroDetails> = emptyList()
    var heroStatisticsToReturn: Resource<List<HeroStatistics>> = Resource.Success(emptyList())
    var heroStatisticsByIdToReturn: Resource<HeroStatistics?> = Resource.Success(null)

    override fun getHeroName(heroId: String): String =
        _allHeroes.value[heroId]?.name ?: ""

    override fun getHeroByName(heroName: String): HeroDetails? =
        _allHeroes.value.values.firstOrNull { it.displayName == heroName }

    override fun getHeroImageSrcById(heroId: String): String =
        _allHeroes.value[heroId]?.imageUrl ?: ""

    override suspend fun fetchAllHeroes() {
        _allHeroes.value = heroesToReturn.associateBy { it.id }
    }

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        heroStatisticsToReturn

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        heroStatisticsByIdToReturn

    fun emitHeroes(heroes: List<HeroDetails>) {
        _allHeroes.value = heroes.associateBy { it.id }
    }
}
