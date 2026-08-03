package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.InventoryItem
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.network.apollo.fragment.MatchResultsFragment
import com.aowen.predcompanion.core.network.apollo.type.GameMode
import com.aowen.predcompanion.core.network.apollo.type.PerkSlot
import com.aowen.predcompanion.core.network.apollo.type.Role
import com.aowen.predcompanion.core.network.apollo.type.SlotType
import com.aowen.predcompanion.core.resources.R
import kotlin.time.Clock
import kotlin.time.Instant


fun MatchResultsFragment.Result.asMatchHistoryItem(): MatchHistoryItem {
    val isWinner = this.team == this.match.winningTeam
    val isRanked = this.match.gameMode == GameMode.RANKED
    val vpChange =
        this.rating?.newPoints?.let { newPoints ->
            this.rating?.points?.let { points ->
                val diff = (newPoints - points).toInt()
                if (diff >= 0) "+$diff" else "$diff"
            }
        } ?: "-"
    val augmentImageSrc =
        this.perkData?.firstOrNull { it?.slot == PerkSlot.HERO_SPECIFIC_1 }?.icon?.let {
            ImageHelpers.buildAssetsUrl(it)
        }
    val eternalImageSrc = this.perkData?.firstOrNull { it?.slot == PerkSlot.ETERNAL_1 }?.icon?.let {
        ImageHelpers.buildAssetsUrl(it)
    }
    val crest =
        inventoryItemData?.firstOrNull { it?.itemDataFragment?.item?.data?.slotType == SlotType.CREST }
            ?.let {
                InventoryItem.Crest(
                    name = it.itemDataFragment.item.name,
                    imageSrc = ImageHelpers.buildAssetsUrl(it.itemDataFragment.item.data?.icon ?: "")
                )
            }
    val trinket =
        inventoryItemData?.firstOrNull { it?.itemDataFragment?.item?.data?.slotType == SlotType.TRINKET }
            ?.let {
                InventoryItem.Trinket(
                    name = it.itemDataFragment.item.name,
                    imageSrc = ImageHelpers.buildAssetsUrl(it.itemDataFragment.item.data?.icon ?: "")
                )
            }
    val items =
        List(6) { index ->
            inventoryItemData?.filter { it?.itemDataFragment?.item?.data?.slotType == SlotType.PASSIVE }
                ?.getOrNull(index)?.let {
                    InventoryItem.Passive(
                        name = it.itemDataFragment.item.name,
                        imageSrc = ImageHelpers.buildAssetsUrl(it.itemDataFragment.item.data?.icon ?: "")
                    )
                }
        }

    return MatchHistoryItem(
        matchId = this.match.id,
        playerId = this.player.id,
        isWinner = isWinner,
        gameModeStringRes = getGameModeStringRes(this.match.gameMode),
        isRanked = isRanked,
        vpChange = vpChange,
        augmentImageSrc = augmentImageSrc,
        eternalImageSrc = eternalImageSrc,
        crest = crest,
        trinket = trinket,
        items = items,
        matchDurationInMinutes = minutesBetween(this.match.startTime, this.match.endTime),
        minionsKilled = this.minionsKilled,
        timeSinceMatch = handleTimeSinceMatch(this.match.endTime),
        heroImageSrc = this.heroData?.icon?.let { ImageHelpers.buildAssetsUrl(it) } ?: "",
        heroName = this.heroData?.displayName ?: "",
        heroId = this.heroData?.hero?.id ?: "",
        heroRoleDrawableId = getHeroRoleDrawableId(this.role),
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        kdaText = this.getKda()
    )
}

fun getGameModeStringRes(gameMode: GameMode): Int {
    return when (gameMode) {
        GameMode.RANKED -> R.string.core_resources_match_type_ranked
        GameMode.ARAM -> R.string.core_resources_match_type_aram
        GameMode.DAYBREAK -> R.string.core_resources_match_type_daybreak
        GameMode.PRACTICE -> R.string.core_resources_match_type_practice
        else -> R.string.core_resources_match_type_unranked
    }
}

private fun getHeroRoleDrawableId(role: Role?): Int? {
    return when (role) {
        Role.CARRY -> R.drawable.carry
        Role.OFFLANE -> R.drawable.offlane
        Role.MIDLANE -> R.drawable.midlane
        Role.SUPPORT -> R.drawable.support
        Role.JUNGLE -> R.drawable.jungle
        else -> null
    }
}

private fun MatchResultsFragment.Result.getKda(): String {
    val deaths = if (this.deaths == 0) 1 else this.deaths
    val kda = (this.kills.toFloat() + this.assists.toFloat()) / deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}

private fun minutesBetween(start: Instant, end: Instant): Int {
    val duration = end - start
    return duration.inWholeMinutes.toInt()
}

private fun handleTimeSinceMatch(endTime: Instant): String {
    val duration = Clock.System.now() - endTime

    return when {
        duration.inWholeDays > 1 -> "${duration.inWholeDays} days ago"
        duration.inWholeDays.toInt() == 1 -> "1 day ago"
        duration.inWholeHours >= 2 -> "${duration.inWholeHours}hrs ago"
        duration.inWholeHours.toInt() == 1 -> "1h ago"
        duration.inWholeMinutes >= 2 -> "${duration.inWholeMinutes} mins ago"
        duration.inWholeMinutes.toInt() == 1 -> "1 min ago"
        duration.inWholeSeconds in 5..59 -> "${duration.inWholeSeconds} sec ago"
        else -> "Just now"
    }
}