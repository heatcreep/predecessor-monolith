package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchDetails.Perk
import com.aowen.predcompanion.core.network.apollo.MatchByIdQuery
import com.aowen.predcompanion.core.network.apollo.fragment.MatchPlayerFragment
import com.aowen.predcompanion.core.network.apollo.type.MatchPlayerTeam
import com.aowen.predcompanion.core.network.apollo.type.PerkSlot
import com.aowen.predcompanion.core.network.apollo.type.SlotType
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlin.math.roundToLong

fun MatchPlayerFragment.asMatchPlayerDetails(duration: Int = 0): MatchDetails.MatchPlayerDetails? {

    val playerFragment = player.playerFragment
    val heroFragment = hero?.heroFragment ?: return null
    val crestId =
        inventoryItemData?.firstOrNull { it?.itemDataFragment?.item?.data?.slotType == SlotType.CREST }
            ?.itemDataFragment?.item?.id
    val trinketId =
        inventoryItemData?.firstOrNull { it?.itemDataFragment?.item?.data?.slotType == SlotType.TRINKET }
            ?.itemDataFragment?.item?.id
    val itemIds =
        inventoryItemData?.filter { it?.itemDataFragment?.item?.data?.slotType == SlotType.PASSIVE }
            ?.mapNotNull { it?.itemDataFragment?.item?.id } ?: emptyList()

    val newPoints = rating?.newPoints
    val points = rating?.points

    return MatchDetails.MatchPlayerDetails(
        playerId = playerFragment.id,
        playerName = playerFragment.name ?: "🎮 user-${playerFragment.id}",
        vpTotal = rating?.points?.coerceAtLeast(0.0)?.toInt() ?: 0,
        vpChange = if (newPoints != null && points != null) {
            (newPoints - points).roundToLong()
        } else null,
        rank = rating?.rank?.name ?: "",
        heroId = heroFragment.id,
        role = role?.name?.lowercase(),
        kills = kills,
        deaths = deaths,
        assists = assists,
        minionsKilled = minionsKilled,
        minionsKilledPerMin = (minionsKilled.toFloat() / duration.toFloat()),
        laneMinionsKilled = this.laneMinionsKilled,
        neutralMinionsKilled = this.neutralMinionsKilled,
        neutralMinionsTeamJungle = this.neutralMinionsTeamJungle,
        neutralMinionsEnemyJungle = this.neutralMinionsEnemyJungle,
        largestKillingSpree = this.largestKillingSpree,
        largestMultiKill = this.multiKill,
        totalDamageDealt = this.totalDamageDealt,
        physicalDamageDealt = this.physicalDamageDealt,
        magicalDamageDealt = this.magicalDamageDealt,
        trueDamageDealt = this.trueDamageDealt,
        largestCriticalStrike = this.largestCriticalStrike ?: 0,
        totalDamageDealtToHeroes = this.heroDamage,
        physicalDamageDealtToHeroes = this.physicalDamageDealtToHeroes,
        magicalDamageDealtToHeroes = this.magicalDamageDealtToHeroes,
        trueDamageDealtToHeroes = this.trueDamageDealtToHeroes,
        totalDamageDealtToStructures = this.totalDamageDealtToStructures,
        totalDamageDealtToObjectives = this.totalDamageDealtToObjectives,
        totalDamageTaken = this.totalDamageTaken,
        physicalDamageTaken = this.physicalDamageTaken,
        magicalDamageTaken = this.magicalDamageTaken,
        trueDamageTaken = this.trueDamageTaken,
        totalDamageTakenFromHeroes = this.heroDamageTaken,
        physicalDamageTakenFromHeroes = this.physicalDamageTakenFromHeroes,
        magicalDamageTakenFromHeroes = this.magicalDamageTakenFromHeroes,
        trueDamageTakenFromHeroes = this.trueDamageTakenFromHeroes,
        totalDamageMitigated = this.totalDamageMitigated,
        totalHealingDone = this.totalHealingDone,
        itemHealingDone = this.itemHealingDone ?: 0,
        crestHealingDone = this.crestHealingDone ?: 0,
        utilityHealingDone = this.utilityHealingDone ?: 0,
        totalShieldingReceived = this.totalShieldingReceived ?: 0,
        wardsPlaced = this.wardsPlaced,
        wardsDestroyed = this.wardsDestroyed,
        goldEarned = this.gold,
        goldEarnedPerMin = (this.gold.toFloat() / duration.toFloat()),
        goldSpent = this.goldSpent,
        itemIds = itemIds,
        crestId = crestId,
        trinketId = trinketId,
        augment = this.perkData?.toPerk(PerkSlot.HERO_SPECIFIC_1),
        eternal = this.perkData?.toPerk(PerkSlot.ETERNAL_1),
        minorBlessing1 = this.perkData?.toPerk(PerkSlot.BLESSING_MINOR_1),
        minorBlessing2 = this.perkData?.toPerk(PerkSlot.BLESSING_MINOR_2)
    )
}

private fun List<MatchPlayerFragment.PerkDatum?>?.toPerk(slot: PerkSlot): Perk? {
    return this?.firstOrNull { it?.slot == slot }?.let {
        Perk(
            iconUrl = ImageHelpers.buildAssetsUrl(it.icon),
            name = it.displayName,
            description = it.description,
        )
    }
}

fun MatchByIdQuery.Match.asMatchDetails(): MatchDetails =
    MatchDetails(
        matchId = id,
        gameMode = getGameModeStringRes(gameMode),
        startTime = startTime.format(DateTimeComponents.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            day()
        }),
        endTime = endTime.format(DateTimeComponents.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            day()
        }),
        gameDuration = duration,
        region = region.toString(),
        winningTeam = winningTeam.toString(),
        dawn = MatchDetails.Team.Dawn(matchPlayers.filter { it.matchPlayerFragment.team == MatchPlayerTeam.DAWN }
            .mapNotNull { it.matchPlayerFragment.asMatchPlayerDetails(duration) }),
        dusk = MatchDetails.Team.Dusk(matchPlayers.filter { it.matchPlayerFragment.team == MatchPlayerTeam.DUSK }
            .mapNotNull { it.matchPlayerFragment.asMatchPlayerDetails(duration) })
    )

fun MatchDetails.MatchPlayerDetails.getKda(): String {
    val deaths = if (this.deaths == 0) 1 else this.deaths
    val kda = (this.kills.toFloat() + this.assists.toFloat()) / deaths.toFloat()
    return if (kda.isNaN()) 0.0.toString() else kda.toDecimal("#.##")
}