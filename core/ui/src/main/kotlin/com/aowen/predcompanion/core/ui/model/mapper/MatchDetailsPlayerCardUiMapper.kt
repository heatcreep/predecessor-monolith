package com.aowen.predcompanion.core.ui.model.mapper

import android.content.Context
import com.aowen.predcompanion.core.data.model.getKda
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.ui.R
import com.aowen.predcompanion.core.ui.model.HeroAndItemDetailsUiModel
import com.aowen.predcompanion.core.ui.model.MatchDetailsPlayerCardUiModel
import com.aowen.predcompanion.core.ui.model.PlayerPerkUiModel
import com.aowen.predcompanion.core.ui.model.PlayerStatsUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class MatchDetailsPlayerCardUiMapper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val heroRepository: HeroRepository,
    private val itemRepository: ItemRepository
) {

    val heroRoleMap = HeroRole.entries.associateBy { it.roleName }

    fun buildFrom(
        matchPlayerDetails: MatchDetails.MatchPlayerDetails
    ): MatchDetailsPlayerCardUiModel {
        val csPerMin = String.format(Locale.US, "%.1f", matchPlayerDetails.minionsKilledPerMin)
        val heroImageSrc = heroRepository.getHeroImageSrcById(matchPlayerDetails.heroId)
        val playerName = matchPlayerDetails.playerName
        return MatchDetailsPlayerCardUiModel(
            heroAndItemDetails = HeroAndItemDetailsUiModel(
                playerName = playerName,
                heroImageSrc = heroImageSrc,
                heroRoleDrawableId = heroRoleMap[matchPlayerDetails.role?.lowercase()]?.drawableId,
                kills = matchPlayerDetails.kills.toString(),
                deaths = matchPlayerDetails.deaths.toString(),
                assists = matchPlayerDetails.assists.toString(),
                augment = matchPlayerDetails.augment?.toPlayerPerkUiModel(),
                eternal = matchPlayerDetails.eternal?.toPlayerPerkUiModel(),
                minorBlessing1 = matchPlayerDetails.minorBlessing1?.toPlayerPerkUiModel(),
                minorBlessing2 = matchPlayerDetails.minorBlessing2?.toPlayerPerkUiModel(),
                crestImageUrl = matchPlayerDetails.crestId?.let { itemRepository.getItemImageSrcById(it) },
                trinketImageUrl = matchPlayerDetails.trinketId?.let { itemRepository.getItemImageSrcById(it) },
                itemsImageUrls = matchPlayerDetails.itemIds.map {
                    itemRepository.getItemImageSrcById(
                        it
                    )
                },
                kdaValue = matchPlayerDetails.getKda(),
                minionsKilled = context.getString(
                    R.string.core_ui_minions_killed,
                    matchPlayerDetails.minionsKilled
                ),
                csPerMin = context.getString(
                    R.string.core_ui_cs_per_min,
                    csPerMin
                ),
            ),
            stats = PlayerStatsUiModel(
                heroImageSrc = heroImageSrc,
                playerName = playerName,
                kills = matchPlayerDetails.kills.toString(),
                deaths = matchPlayerDetails.deaths.toString(),
                assists = matchPlayerDetails.assists.toString(),
                minionsKilled = matchPlayerDetails.minionsKilled.toString(),
                minionsKilledPerMin = csPerMin,
                goldEarned = matchPlayerDetails.goldEarned.toString(),
                goldEarnedPerMin = String.format(Locale.US, "%.1f", matchPlayerDetails.goldEarnedPerMin),
                laneMinionsKilled = matchPlayerDetails.laneMinionsKilled.toString(),
                neutralMinionsKilled = matchPlayerDetails.neutralMinionsKilled.toString(),
                neutralMinionsTeamJungle = matchPlayerDetails.neutralMinionsTeamJungle.toString(),
                neutralMinionsEnemyJungle = matchPlayerDetails.neutralMinionsEnemyJungle.toString(),
                largestKillingSpree = matchPlayerDetails.largestKillingSpree.toString(),
                largestMultiKill = matchPlayerDetails.largestMultiKill.toString(),
                totalDamageDealt = matchPlayerDetails.totalDamageDealt.toString(),
                physicalDamageDealt = matchPlayerDetails.physicalDamageDealt.toString(),
                magicalDamageDealt = matchPlayerDetails.magicalDamageDealt.toString(),
                trueDamageDealt = matchPlayerDetails.trueDamageDealt.toString(),
                largestCriticalStrike = matchPlayerDetails.largestCriticalStrike.toString(),
                totalDamageDealtToHeroes = matchPlayerDetails.totalDamageDealtToHeroes.toString(),
                physicalDamageDealtToHeroes = matchPlayerDetails.physicalDamageDealtToHeroes.toString(),
                magicalDamageDealtToHeroes = matchPlayerDetails.magicalDamageDealtToHeroes.toString(),
                trueDamageDealtToHeroes = matchPlayerDetails.trueDamageDealtToHeroes.toString(),
                totalDamageDealtToStructures = matchPlayerDetails.totalDamageDealtToStructures.toString(),
                totalDamageDealtToObjectives = matchPlayerDetails.totalDamageDealtToObjectives.toString(),
                totalDamageTaken = matchPlayerDetails.totalDamageTaken.toString(),
                physicalDamageTaken = matchPlayerDetails.physicalDamageTaken.toString(),
                magicalDamageTaken = matchPlayerDetails.magicalDamageTaken.toString(),
                trueDamageTaken = matchPlayerDetails.trueDamageTaken.toString(),
                totalDamageTakenFromHeroes = matchPlayerDetails.totalDamageTakenFromHeroes.toString(),
                physicalDamageTakenFromHeroes = matchPlayerDetails.physicalDamageTakenFromHeroes.toString(),
                magicalDamageTakenFromHeroes = matchPlayerDetails.magicalDamageTakenFromHeroes.toString(),
                trueDamageTakenFromHeroes = matchPlayerDetails.trueDamageTakenFromHeroes.toString(),
            )
        )
    }

    private fun MatchDetails.Perk.toPlayerPerkUiModel(): PlayerPerkUiModel {
        return PlayerPerkUiModel(
            iconUrl = this.iconUrl,
            name = this.name,
            description = this.description,
        )
    }
}
