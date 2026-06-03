package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.data.model.mapper.HeroMapper
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityHeroRepository @Inject constructor(
    private val retrofitOmedaCityNetwork: PredCompanionNetworkDataSource,
    private val heroMapper: HeroMapper,
) : HeroRepository {

    private val _allHeroes: MutableStateFlow<Map<Long, HeroDetails>> = MutableStateFlow(emptyMap())
    override val allHeroes: StateFlow<Map<Long, HeroDetails>> = _allHeroes

    override suspend fun fetchAllHeroes() {
        val result = safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getAllHeroes() },
            transform = { heroes -> heroes.map { heroMapper.getHeroDetailsFrom(it) } }
        )
        if (result is Resource.Success) {
            _allHeroes.update { result.data.associateBy { it.id } }
        }
    }


    override fun getHeroByName(heroName: String): HeroDetails? =
        allHeroes.value.values.firstOrNull {
            it.displayName == heroName
        }

    override fun getHeroImageSrcById(heroId: Long): String =
        allHeroes.value[heroId]?.imageUrl ?: ""

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getAllHeroStatistics(timeFrame) },
            transform = { stats ->
                stats.heroStatistics.mapNotNull { networkHeroStatistics ->
                    val heroFromMap = allHeroes.first { it.isNotEmpty() }[networkHeroStatistics.heroId]
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
                    heroId = networkHeroStatistics.heroId,
                    heroName = networkHeroStatistics.displayName,
                    heroImageSrc = getHeroImageSrcById(networkHeroStatistics.heroId),
                    name = networkHeroStatistics.displayName,
                    winRate = networkHeroStatistics.winRate,
                    pickRate = networkHeroStatistics.pickRate,
                )
            } },
        )

}