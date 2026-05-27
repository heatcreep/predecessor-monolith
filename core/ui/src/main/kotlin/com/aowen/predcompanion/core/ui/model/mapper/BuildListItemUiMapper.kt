package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.data.model.mapper.HeroMapper
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.model.NetworkFavoriteHeroBuild
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

data class ItemModuleUi(
    val id: String? = null,
    val title: String = "",
    val items: List<ItemDetails> = emptyList(),
)

data class BuildUiListItem(
    val buildId: Int,
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
    val netVotes: Int = 0,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val modules: List<ItemModuleUi> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val version: String? = null
)


class BuildListItemUiMapper @Inject constructor(
    itemRepository: ItemRepository,
    private val heroRepository: HeroRepository,
    private val heroMapper: HeroMapper,
) {

    val items = itemRepository.allItems.value

    fun buildFrom(
        heroBuild: HeroBuild,
    ): BuildUiListItem {
        return BuildUiListItem(
            buildId = heroBuild.id,
            userId = heroBuild.userId,
            title = heroBuild.title,
            author = heroBuild.author,
            role = heroMapper.getHeroRole(heroBuild.role),
            description = heroBuild.description,
            heroId = heroBuild.heroId,
            heroImageUrl = heroRepository.getHeroImageSrcById(heroBuild.heroId),
            crest = items[heroBuild.crestId] ?: ItemDetails(),
            buildItems = heroBuild.buildItemIds.mapNotNull { items[it] },
            skillOrder = heroBuild.skillOrder,
            netVotes = heroBuild.netVotes,
            upvotes = heroBuild.upvotes,
            downvotes = heroBuild.downvotes,
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
            role = heroMapper.getHeroRole(favoriteBuildListItem.role),
            description = favoriteBuildListItem.description,
            heroId = favoriteBuildListItem.heroId,
            heroImageUrl = heroRepository.getHeroImageSrcById(favoriteBuildListItem.heroId),
            crest = items[favoriteBuildListItem.crestId] ?: ItemDetails(),
            buildItems = favoriteBuildListItem.itemIds.mapNotNull { items[it] },
            upvotes = favoriteBuildListItem.upvotesCount,
            downvotes = favoriteBuildListItem.downvotesCount,
            createdAt = favoriteBuildListItem.createdAt,
            version = favoriteBuildListItem.gameVersion
        )
    }
}

fun BuildUiListItem.asFavoriteBuildDto(userId: UUID): NetworkFavoriteHeroBuild {
    return NetworkFavoriteHeroBuild(
        id = UUID.randomUUID(),
        createdAt = Timestamp(System.currentTimeMillis()).toString(),
        userId = userId,
        buildId = buildId,
        heroId = heroId,
        role = role?.roleName ?: "",
        title = title,
        description = description,
        author = author,
        crestId = crest.id,
        itemIds = buildItems.map { it.id },
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        gameVersion = version ?: ""
    )
}

fun BuildUiListItem.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = buildId,
        heroId = heroId,
        role = role?.roleName ?: "",
        title = title,
        description = description,
        author = author,
        crestId = crest.id,
        itemIds = buildItems.map { it.id },
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        createdAt = createdAt,
        gameVersion = version ?: ""
    )
}

private fun HeroBuild.ItemModule.toUi(items: Map<Int, ItemDetails>): ItemModuleUi =
    ItemModuleUi(
        id = id,
        title = title,
        items = itemIds.mapNotNull { items[it] },
    )
