package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.model.data.asHeroDetails
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeHeroStatisticsResult
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroStatistics

class FakeOmedaCityHeroRepository : HeroRepository {
    override suspend fun fetchAllHeroes(): Resource<List<HeroDetails>> =
        Resource.Success(
            listOf(
                fakeNetworkHero.asHeroDetails(),
                fakeNetworkHero2.asHeroDetails()
            )
        )

    override suspend fun getHeroByName(heroName: String): Resource<HeroDetails?> =
        Resource.Success(fakeNetworkHero.asHeroDetails())

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        Resource.Success(fakeHeroStatisticsResult)

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        Resource.Success(fakeNetworkHeroStatistics.create())
}