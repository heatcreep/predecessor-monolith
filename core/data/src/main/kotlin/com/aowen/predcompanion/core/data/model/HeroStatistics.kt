package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.model.NetworkHeroStatistics
import javax.inject.Inject

class HeroStatisticsMapper @Inject constructor(private val heroRepository: HeroRepository) {

    fun buildFrom(networkHeroStatistics: NetworkHeroStatistics): HeroStatistics {
        return HeroStatistics(
            heroId = networkHeroStatistics.heroId.toString(),
            heroName = networkHeroStatistics.displayName,
            heroImageSrc = heroRepository.getHeroImageSrcById(networkHeroStatistics.heroId.toString()),
            name = networkHeroStatistics.displayName,
            winRate = networkHeroStatistics.winRate,
            pickRate = networkHeroStatistics.pickRate,
        )
    }
}