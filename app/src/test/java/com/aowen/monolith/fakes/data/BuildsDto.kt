package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.BuildDto
import com.aowen.monolith.data.GameVersionDto
import com.aowen.monolith.data.ModuleDto

val fakeModuleDto = ModuleDto(
    title = "Module Title",
    item1Id = 301,
    item2Id = 302,
    item3Id = 303,
    item4Id = 304,
    item5Id = 305,
    item6Id = 306
)

val fakeGameVersionDto = GameVersionDto(
    id = 1,
    name = "1.0.0",
    release = "2022-01-01T00:00:00Z",
    displayBadge = true,
    createdAt = "2022-01-01T00:00:00Z",
    updatedAt = "2022-01-01T00:00:00Z"
)

val fakeBuildDto = BuildDto(
    id = 1,
    title = "Title",
    description = "Description",
    heroId = 101,
    role = "Role",
    crestId = 201,
    item1Id = 301,
    item2Id = 302,
    item3Id = 303,
    item4Id = 304,
    item5Id = 305,
    skillOrder = listOf(1, 2, 3),
    upvotesCount = 100,
    downvotesCount = 50,
    createdAt = "2022-01-01T00:00:00Z",
    updatedAt = "2022-01-01T00:00:00Z",
    author = "Author",
    modules = listOf(fakeModuleDto),
    gameVersion = fakeGameVersionDto
)

