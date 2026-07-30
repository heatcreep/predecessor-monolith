package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.data.model.asHeroDetails
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import com.aowen.predcompanion.core.network.safeGraphQlCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityHeroRepository @Inject constructor(
    private val retrofitOmedaCityNetwork: PredCompanionNetworkDataSource,
    private val predGGNetwork: PredGGNetworkDataSource,
) : HeroRepository {

    private val _allHeroes: MutableStateFlow<Map<String, HeroDetails>> = MutableStateFlow(emptyMap())
    override val allHeroes: StateFlow<Map<String, HeroDetails>> = _allHeroes

    override suspend fun fetchAllHeroes() {
        val result = safeGraphQlCall(
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

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getAllHeroStatistics(timeFrame) },
            transform = { stats ->
                stats.heroStatistics.mapNotNull { networkHeroStatistics ->
                    val heroFromMap = allHeroes.first { it.isNotEmpty() }[networkHeroStatistics.heroId.toString()]
                    heroFromMap?.let {
                        HeroStatistics(
                            heroId = heroFromMap.id,
                            heroName = heroFromMap.displayName,
                            heroImageSrc = heroFromMap.imageUrl,
                            name = heroFromMap.displayName,
                            winRate = networkHeroStatistics.winRate,
                            pickRate = networkHeroStatistics.pickRate,
                        )
                    }
                }
            }
        )

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getHeroStatisticsById(heroId) },
            transform = { stats -> stats.heroStatistics.firstOrNull()?.let { networkHeroStatistics ->
                HeroStatistics(
                    heroId = networkHeroStatistics.heroId.toString(),
                    heroName = networkHeroStatistics.displayName,
                    heroImageSrc = getHeroImageSrcById(networkHeroStatistics.heroId.toString()),
                    name = networkHeroStatistics.displayName,
                    winRate = networkHeroStatistics.winRate,
                    pickRate = networkHeroStatistics.pickRate,
                )
            } },
        )

}