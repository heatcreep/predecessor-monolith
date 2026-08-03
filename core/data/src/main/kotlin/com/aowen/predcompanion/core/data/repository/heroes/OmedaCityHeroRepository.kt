package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.data.model.asHeroDetails
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.safeGraphQlCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityHeroRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : HeroRepository {

    private val _allHeroes: MutableStateFlow<Map<String, HeroDetails>> = MutableStateFlow(emptyMap())
    override val allHeroes: StateFlow<Map<String, HeroDetails>> = _allHeroes

    override suspend fun fetchAllHeroes() {
        val result = safeGraphQlCall<HeroesQuery.Data, List<HeroDetails>>(
            apiCall = { predGGNetwork.getAllHeroes() },
            transform = { data -> data.heroes.map {
                it.heroFragment.asHeroDetails()
            } }
        )
        if (result is Resource.Success) {
            _allHeroes.update { result.data.associateBy { it.id } }
        }
    }

    override fun getHeroName(heroId: String): String =
        allHeroes.value[heroId]?.name ?: ""

    override fun getHeroByName(heroName: String): HeroDetails? =
        allHeroes.value.values.firstOrNull {
            it.displayName == heroName
        }

    override fun getHeroImageSrcById(heroId: String): String =
        allHeroes.value[heroId]?.imageUrl ?: ""

    // The GraphQL API does not expose server-side aggregated hero statistics, so these
    // can no longer be computed client-side.
    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> {
        val heroStats = allHeroes.value.values.map {
            HeroStatistics(
                heroId = it.id,
                heroImageSrc = it.imageUrl,
                name = it.name,
                heroName = it.displayName,
                winRate = it.generalStatistic?.winRate ?: 0f,
                pickRate = 0f,
            )
        }
        return Resource.Success(heroStats)
    }

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        Resource.Success(null)

}