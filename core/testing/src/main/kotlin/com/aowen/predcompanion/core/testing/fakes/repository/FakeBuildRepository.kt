package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.Resource

class FakeBuildRepository : BuildRepository {

    var buildsToReturn: Resource<List<HeroBuild>> = Resource.Success(emptyList())
    var buildByIdToReturn: Resource<HeroBuild> = Resource.GenericError("Not configured")

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: String?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?,
        limit: Int?,
    ): Resource<List<HeroBuild>> = buildsToReturn

    override suspend fun fetchBuildById(buildId: String): Resource<HeroBuild> = buildByIdToReturn
}
