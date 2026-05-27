package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroBuildGameVersion
import com.aowen.predcompanion.core.network.model.NetworkHeroBuildModule

val fakeNetworkHeroBuildModule = NetworkHeroBuildModule(
    id = "123",
    title = "Module Title",
    item1Id = 301,
    item2Id = 302,
    item3Id = 303,
    item4Id = 304,
    item5Id = 305,
    item6Id = 306
)

val fakeNetworkHeroBuildGameVersion = NetworkHeroBuildGameVersion(
    id = 1,
    name = "1.0.0",
    release = "2022-01-01T00:00:00Z",
    displayBadge = true,
    createdAt = "2022-01-01T00:00:00Z",
    updatedAt = "2022-01-01T00:00:00Z"
)

val fakeNetworkHeroBuild = NetworkHeroBuild(
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
    item6Id = 212,
    skillOrder = listOf(1, 2, 3),
    upvotesCount = 100,
    downvotesCount = 50,
    createdAt = "2022-01-01T00:00:00Z",
    updatedAt = "2022-01-01T00:00:00Z",
    author = "Author",
    modules = listOf(fakeNetworkHeroBuildModule),
    gameVersion = fakeNetworkHeroBuildGameVersion
)

