package com.aowen.monolith.data

data class BuildListItem(
    val id: Int,
    val title: String,
    val author: String,
    val role: String,
    val description: String?,
    val heroId: Int,
    val crest: Int,
    val buildItems: List<Int> = emptyList(),
    val skillOrder: List<Int>? = null,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val createdAt: String?,
    val updatedAt: String?,
)

data class ItemModule(
    val title: String = "",
    val items: List<Int?> = emptyList(),
)

fun BuildDto.create(): BuildListItem {
    return BuildListItem(
        id = id,
        title = title,
        author = author,
        role = role,
        description = description,
        heroId = heroId,
        crest = crestId,
        buildItems = listOf(item1Id, item2Id, item3Id, item4Id, item5Id),
        skillOrder = skillOrder,
        upvotes = upvotesCount,
        downvotes = downvotesCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun ModuleDto.create(): ItemModule {
    return ItemModule(
        title = title,
        items = listOfNotNull(item1Id, item2Id, item3Id, item4Id, item5Id, item6Id),
    )
}
