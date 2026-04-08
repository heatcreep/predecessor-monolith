package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.data.BuildListItem
import com.aowen.predcompanion.data.asBuildListItem
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import javax.inject.Inject

class OmedaCityBuildRepository @Inject constructor(
    private val omedaCityService: OmedaCityService
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
    ): Resource<List<BuildListItem>> =
        safeApiCall(
            apiCall = {
                omedaCityService.getBuilds(
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
            transform = { builds -> builds.map { it.asBuildListItem() } }
        )

    override suspend fun fetchBuildById(buildId: String): Resource<BuildListItem> =
        safeApiCall(
            apiCall = { omedaCityService.getBuildById(buildId) },
            transform = { build -> build.asBuildListItem() }
        )
}