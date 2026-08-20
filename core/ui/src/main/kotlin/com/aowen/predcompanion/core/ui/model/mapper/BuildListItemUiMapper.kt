package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
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
}

private fun HeroBuild.ItemModule.toUi(items: Map<String, ItemDetails>): ItemModuleUi =
    ItemModuleUi(
        id = id,
        title = title,
        items = itemIds.mapNotNull { items[it.toString()] },
    )
