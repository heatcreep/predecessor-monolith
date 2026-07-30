package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.apollo.fragment.GuideFragment
import com.aowen.predcompanion.core.network.safeGraphQlCall
import javax.inject.Inject

class OmedaCityBuildRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : BuildRepository {

    private val pageSize = 20

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
        safeGraphQlCall(
            apiCall = {
                predGGNetwork.getBuilds(
                    limit = pageSize,
                    offset = ((page ?: 1) - 1) * pageSize
                )
            },
            transform = { data ->
                data.guidesPaginated.results.map { it.guideFragment.asHeroBuild() }
            }
        )

    override suspend fun fetchBuildById(buildId: String): Resource<HeroBuild> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getBuildById(buildId) },
            transform = { data ->
                data.guide?.guideFragment?.asHeroBuild()
                    ?: throw IllegalStateException("Build with id $buildId not found")
            }
        )
}

private fun GuideFragment.asHeroBuild(): HeroBuild {
    val primaryModule = modules.firstOrNull { it.primary } ?: modules.firstOrNull()
    val score = averageScore?.let { kotlin.math.round(it).toInt() } ?: 0
    return HeroBuild(
        id = id.toIntOrNull() ?: 0,
        title = title,
        author = author.name,
        role = role.name.lowercase(),
        heroId = hero.id.toLongOrNull() ?: 999L,
        buildItemIds = primaryModule?.items?.mapNotNull { it.id.toIntOrNull() } ?: emptyList(),
        netVotes = score,
        upvotes = score.coerceAtLeast(0),
        downvotes = if (score < 0) -score else 0,
        modules = modules.map { module ->
            HeroBuild.ItemModule(
                id = module.id,
                title = module.title ?: "",
                itemIds = module.items?.mapNotNull { item -> item.id.toIntOrNull() } ?: emptyList(),
            )
        },
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        version = version.name,
    )
}
