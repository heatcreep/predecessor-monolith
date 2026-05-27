package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.data.repository.BuildListItemDataMapper
import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import javax.inject.Inject

class OmedaCityBuildRepository @Inject constructor(
    private val retrofitOmedaCityNetwork: PredCompanionNetworkDataSource,
    private val buildListItemDataMapper: BuildListItemDataMapper,
) : BuildRepository {
    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Resource<List<HeroBuild>> =
        safeApiCall(
            apiCall = {
                retrofitOmedaCityNetwork.getBuilds(
                    name = name,
                    role = role,
                    order = order,
                    heroId = heroId,
                    skillOrder = skillOrder,
                    modules = modules,
                    currentVersion = currentVersion,
                    page = page
                )
            },
            transform = { builds -> builds.map { buildListItemDataMapper.createBuildListItem(it) } }
        )

    override suspend fun fetchBuildById(buildId: String): Resource<HeroBuild> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getBuildById(buildId) },
            transform = { build -> buildListItemDataMapper.createBuildListItem(build) }
        )
}