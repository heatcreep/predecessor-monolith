package com.aowen.predcompanion.feature.home.impl.matches.model.mapper

import androidx.compose.ui.graphics.Color
import com.aowen.predcompanion.core.data.model.getKda
import com.aowen.predcompanion.core.data.model.toDecimal
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.ui.model.toRankColor
import com.aowen.predcompanion.core.ui.model.toRankTitle
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchPlayerCardUiModel
import javax.inject.Inject

class MatchPlayerCardUiModelMapper @Inject constructor(
    private val itemRepository: ItemRepository,
    private val heroRepository: HeroRepository,
) {

    operator fun invoke(
        matchPlayers: List<MatchDetails.MatchPlayerDetails>,
        gameDuration: Int
    ): List<MatchPlayerCardUiModel> {
        return matchPlayers.map { matchPlayer ->
            MatchPlayerCardUiModel(
                playerId = matchPlayer.playerId,
                playerName = matchPlayer.playerName,
                rank = matchPlayer.rank?.toRankTitle() ?: "Unranked",
                rankColor = matchPlayer.rank?.toRankColor() ?: Color.Transparent,
                vpTotal = matchPlayer.vpTotal,
                vpChange = matchPlayer.vpChange,
                heroImageUrl = heroRepository.getHeroImageSrcById(matchPlayer.heroId),
                items = matchPlayer.itemIds.mapNotNull {
                    val item = itemRepository.getItemById(it)
                    item?.let { itemDetails ->
                        MatchPlayerCardUiModel.PlayerItem(
                            itemName = item.name,
                            imageSrc = itemDetails.imageSrc
                        )
                    }
                },
                heroRoleImageId = HeroRole.entries.find { it.roleName == matchPlayer.role }?.drawableId ?: 0,
                performanceTitle = matchPlayer.performanceTitle,
                performanceScore = matchPlayer.performanceScore,
                minionsKilled = matchPlayer.minionsKilled,
                kills = matchPlayer.kills.toString(),
                deaths = matchPlayer.deaths.toString(),
                assists = matchPlayer.assists.toString(),
                kdaValue = matchPlayer.getKda(),
                goldEarned = matchPlayer.goldEarned.toString(),
                creepScorePerMinute = computePerMinute(gameDuration, matchPlayer.minionsKilled),
                goldEarnedPerMinute = computePerMinute(gameDuration, matchPlayer.goldEarned),
                laneMinionsKilled = matchPlayer.laneMinionsKilled,
                neutralMinionsEnemyJungle = matchPlayer.neutralMinionsEnemyJungle,
                neutralMinionsTeamJungle = matchPlayer.neutralMinionsTeamJungle,
                totalDamageDealt = matchPlayer.totalDamageDealt,
                totalDamageDealtToHeroes = matchPlayer.totalDamageDealtToHeroes,
                physicalDamageDealt = matchPlayer.physicalDamageDealt,
                physicalDamageDealtToHeroes = matchPlayer.physicalDamageDealtToHeroes,
                magicalDamageDealt = matchPlayer.magicalDamageDealt,
                magicalDamageDealtToHeroes = matchPlayer.magicalDamageDealtToHeroes,
                trueDamageDealt = matchPlayer.trueDamageDealt,
                trueDamageDealtToHeroes = matchPlayer.trueDamageDealtToHeroes,
                totalDamageDealtToStructures = matchPlayer.totalDamageDealtToStructures,
                totalDamageDealtToObjectives = matchPlayer.totalDamageDealtToObjectives,
                totalDamageTaken = matchPlayer.totalDamageTaken,
                physicalDamageTaken = matchPlayer.physicalDamageTaken,
                magicalDamageTaken = matchPlayer.magicalDamageTaken,
                trueDamageTaken = matchPlayer.trueDamageTaken,
                totalDamageTakenFromHeroes = matchPlayer.totalDamageTakenFromHeroes,
                physicalDamageTakenFromHeroes = matchPlayer.physicalDamageTakenFromHeroes,
                magicalDamageTakenFromHeroes = matchPlayer.magicalDamageTakenFromHeroes,
                trueDamageTakenFromHeroes = matchPlayer.trueDamageTakenFromHeroes,
                totalDamageMitigated = matchPlayer.totalDamageMitigated,
                totalHealingDone = matchPlayer.totalHealingDone,
                itemHealingDone = matchPlayer.itemHealingDone,
                crestHealingDone = matchPlayer.crestHealingDone,
                utilityHealingDone = matchPlayer.utilityHealingDone,
                totalShieldingReceived = matchPlayer.totalShieldingReceived,
                wardsPlaced = matchPlayer.wardsPlaced,
                wardsDestroyed = matchPlayer.wardsDestroyed,
            )
        }
    }

    private fun computePerMinute(gameDuration: Int, value: Int): String {
        if (gameDuration == 0) return "0"
        return ((60f / gameDuration.toFloat()) * value.toFloat()).toDecimal()
    }
}