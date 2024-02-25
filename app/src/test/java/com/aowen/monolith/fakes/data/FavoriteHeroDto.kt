package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.FavoriteHeroDto

val fakeFavoriteHeroDto = FavoriteHeroDto(
    id = 123,
    gameId = 456,
    name = "test",
    displayName = "Test",
    stats = listOf(3, 4, 5, 6),
    classes = listOf("class1", "class2"),
    roles = listOf("role1", "role2"),
    visible = true,
    enabled = true,
    createdAt = "fakeTime",
    updatedAt = "fakeTime",
)