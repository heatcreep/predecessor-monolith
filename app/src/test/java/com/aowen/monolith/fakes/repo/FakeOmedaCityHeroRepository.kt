package com.aowen.predcompanion.fakes.repo

import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.data.HeroStatistics
import com.aowen.monolith.data.asHeroDetails
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.fakes.data.fakeHeroDto
import com.aowen.predcompanion.fakes.data.fakeHeroDto2
import com.aowen.predcompanion.fakes.data.fakeHeroStatisticsDto
import com.aowen.predcompanion.fakes.data.fakeHeroStatisticsResult
import com.aowen.predcompanion.core.network.Resource

class FakeOmedaCityHeroRepository : HeroRepository {
    override suspend fun fetchAllHeroes(): Resource<List<HeroDetails>> =
        Resource.Success(
            listOf(
                fakeHeroDto.asHeroDetails(),
                fakeHeroDto2.asHeroDetails()
            )
        )

    override suspend fun fetchHeroByName(heroName: String): Resource<HeroDetails?> =
        Resource.Success(fakeHeroDto.asHeroDetails())

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        Resource.Success(fakeHeroStatisticsResult)

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        Resource.Success(fakeHeroStatisticsDto.create())
}