package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.asHeroDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroDto2
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsResult
import com.aowen.monolith.network.Resource

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