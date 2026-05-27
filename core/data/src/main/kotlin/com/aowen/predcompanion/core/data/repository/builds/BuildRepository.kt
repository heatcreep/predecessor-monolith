package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.Resource

interface BuildRepository {

    suspend fun fetchAllBuilds(
        name: String? = null,
        role: String? = null,
        order: String? = null,
        heroId: Long? = null,
        skillOrder: Int? = null,
        currentVersion: Int? = null,
        modules: Int? = null,
        page: Int? = 1,

        ): Resource<List<HeroBuild>>

    suspend fun fetchBuildById(
        buildId: String
    ): Resource<HeroBuild>
}