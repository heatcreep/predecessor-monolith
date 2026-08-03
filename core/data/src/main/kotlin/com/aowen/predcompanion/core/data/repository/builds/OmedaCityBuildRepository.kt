package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.apollo.fragment.GuideFragment
import com.aowen.predcompanion.core.network.safeGraphQlCall
import java.util.Locale
import javax.inject.Inject

class OmedaCityBuildRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : BuildRepository {

    private val pageSize = 20

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: String?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?,
        limit : Int?,
    ): Resource<List<HeroBuild>> =
        safeGraphQlCall(
            apiCall = {
                predGGNetwork.getBuilds(
                    heroId = heroId,
                    limit = limit ?: pageSize,
                    offset = ((page ?: 1) - 1) * (limit ?: pageSize)
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
    val score = averageScore?.coerceIn(0.0, 100.0)?.div(100f)?.times(5f)?.toFloat()
    return HeroBuild(
        id = id,
        title = title,
        author = author.name,
        role = role.name.lowercase(),
        heroId = hero.id.toLongOrNull() ?: 999L,
        buildItemIds = primaryModule?.items?.mapNotNull { it.id.toIntOrNull() } ?: emptyList(),
        fiveStarScore = String.format(Locale.US, "%.1f", score ?: 0.0),
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
