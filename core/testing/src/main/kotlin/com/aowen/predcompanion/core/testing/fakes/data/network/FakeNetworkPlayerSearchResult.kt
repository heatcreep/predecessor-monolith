package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkPlayerSearchResult
import java.util.UUID

val fakeNetworkExistingPlayerSearch = NetworkPlayerSearchResult(
    createdAt = "createdAt",
    id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514"),
    playerId = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57515"),
    displayName = "displayName",
    rank = 123,
    mmr = 123.45f,
    region = "naeast",
    rankTitle = "Gold I",
    rankImage = "Gold I",
    isRanked = true
)

val fakeNetworkNewPlayerSearch = NetworkPlayerSearchResult(
    createdAt = "createdAt",
    id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514"),
    playerId = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf5751a"),
    displayName = "displayName",
    rank = 123,
    mmr = 123.45f,
    region = "naeast",
    rankTitle = "Gold I",
    rankImage = "Gold I",
    isRanked = true
)