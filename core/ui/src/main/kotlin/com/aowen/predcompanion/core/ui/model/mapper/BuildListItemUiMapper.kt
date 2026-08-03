package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import javax.inject.Inject

data class ItemModuleUi(
    val id: String? = null,
    val title: String = "",
    val items: List<ItemDetails> = emptyList(),
)

data class BuildUiListItem(
    val buildId: String,
    val userId: String? = null,
    val title: String,
    val author: String,
    val role: HeroRole?,
    val description: String? = null,
    val heroId: Long,
    val heroImageUrl: String? = null,
    val crest: ItemDetails = ItemDetails(),
    val buildItems: List<ItemDetails> = emptyList(),
    val skillOrder: List<Int>? = null,
    val fiveStarScore: String = "0.0",
    val modules: List<ItemModuleUi> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val version: String? = null
)


class BuildListItemUiMapper @Inject constructor(
    itemRepository: ItemRepository,
    private val heroRepository: HeroRepository,
) {

    val items = itemRepository.allItems.value

    val heroRoleMap = HeroRole.entries.associateBy { it.roleName }


    fun buildFrom(
        heroBuild: HeroBuild,
    ): BuildUiListItem {
        return BuildUiListItem(
            buildId = heroBuild.id,
            userId = heroBuild.userId,
            title = heroBuild.title,
            author = heroBuild.author,
            role = heroRoleMap[heroBuild.role.lowercase()],
            description = heroBuild.description,
            heroId = heroBuild.heroId,
            heroImageUrl = heroRepository.getHeroImageSrcById(heroBuild.heroId.toString()),
            crest = items[heroBuild.crestId.toString()] ?: ItemDetails(),
            buildItems = heroBuild.buildItemIds.mapNotNull { items[it.toString()] },
            skillOrder = heroBuild.skillOrder,
            fiveStarScore = heroBuild.fiveStarScore,
            modules = heroBuild.modules.map { it.toUi(items) },
            createdAt = heroBuild.createdAt,
            updatedAt = heroBuild.updatedAt,
            version = heroBuild.version
        )
    }

    fun buildFrom(
        favoriteBuildListItem: FavoriteBuildListItem,
    ): BuildUiListItem {
        return BuildUiListItem(
            buildId = favoriteBuildListItem.buildId,
            title = favoriteBuildListItem.title,
            author = favoriteBuildListItem.author,
            role = heroRoleMap[favoriteBuildListItem.role.lowercase()],
            description = favoriteBuildListItem.description,
            heroId = favoriteBuildListItem.heroId,
            heroImageUrl = heroRepository.getHeroImageSrcById(favoriteBuildListItem.heroId.toString()),
            crest = items[favoriteBuildListItem.crestId.toString()] ?: ItemDetails(),
            buildItems = favoriteBuildListItem.itemIds.mapNotNull { items[it.toString()] },
            createdAt = favoriteBuildListItem.createdAt,
            version = favoriteBuildListItem.gameVersion
        )
    }
}

fun BuildUiListItem.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = buildId,
        heroId = heroId,
        role = role?.roleName ?: "",
        title = title,
        description = description,
        author = author,
        crestId = crest.id.toInt(),
        itemIds = buildItems.map { it.id.toInt() },
        upvotesCount = 0,
        downvotesCount = 0,
        createdAt = createdAt,
        gameVersion = version ?: ""
    )
}

private fun HeroBuild.ItemModule.toUi(items: Map<String, ItemDetails>): ItemModuleUi =
    ItemModuleUi(
        id = id,
        title = title,
        items = itemIds.mapNotNull { items[it.toString()] },
    )
