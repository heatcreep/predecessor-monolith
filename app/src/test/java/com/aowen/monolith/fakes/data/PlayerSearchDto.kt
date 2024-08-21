package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.PlayerSearchDto
import java.util.UUID

val fakeExistingPlayerSearchDto = PlayerSearchDto(
    createdAt = "createdAt",
    id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514"),
    playerId = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57515"),
    displayName = "displayName",
    rank = 123,
    mmr = 123.45f,
    region = "naeast"
)

val fakeNewPlayerSearchDto = PlayerSearchDto(
    createdAt = "createdAt",
    id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514"),
    playerId = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf5751a"),
    displayName = "displayName",
    rank = 123,
    mmr = 123.45f,
    region = "naeast"
)