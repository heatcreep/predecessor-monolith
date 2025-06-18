package com.aowen.monolith.data.repository.builds

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.network.Resource

interface BuildRepository {

    suspend fun fetchAllBuilds(
        name: String? = null,
        role: String? = null,
        order: String? = null,
        heroId: Int? = null,
        skillOrder: Int? = null,
        currentVersion: Int? = null,
        modules: Int? = null,
        page: Int? = 1,

        ): Resource<List<BuildListItem>>

    suspend fun fetchBuildById(
        buildId: String
    ): Resource<BuildListItem>
}