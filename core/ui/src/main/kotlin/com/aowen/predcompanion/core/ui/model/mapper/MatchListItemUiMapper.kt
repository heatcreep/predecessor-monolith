package com.aowen.predcompanion.core.ui.model.mapper

import com.aowen.predcompanion.core.data.model.getKda
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.ui.utils.handleTimeSinceMatch
import javax.inject.Inject

class MatchListItemUiMapper @Inject constructor(
    private val heroRepository: HeroRepository,
) {

    val heroRoleMap = HeroRole.entries.associateBy { it.roleName }
    fun buildFrom(
        match: MatchDetails,
        playerId: String,
    ): MatchListItemUiModel? {
        val allPlayers = match.dusk.players + match.dawn.players
        val playerHero = allPlayers.firstOrNull { it.playerId == playerId } ?: return null
        val playerTeam = if (match.dusk.players.contains(playerHero)) "Dusk" else "Dawn"
        val isWinner = playerTeam == match.winningTeam

        return MatchListItemUiModel(
            matchId = match.matchId,
            playerId = playerHero.playerId,
            isWinner = isWinner,
            gameModeStringRes = null,
            isRanked = match.matchType == MatchDetails.MatchType.RANKED,
            vpChange = playerHero.vpChange,
            timeSinceMatch = handleTimeSinceMatch(match.endTime),
            heroImageUrl = heroRepository.getHeroImageSrcById(playerHero.heroId),
            heroName = heroRepository.getHeroName(playerHero.heroId),
            heroRoleDrawableId = heroRoleMap[playerHero.role.lowercase()]?.drawableId,
            kills = playerHero.kills.toString(),
            deaths = playerHero.deaths.toString(),
            assists = playerHero.assists.toString(),
            kdaValue = playerHero.getKda(),
        )
    }

    fun buildFrom(
        matchHistoryItem: MatchHistoryItem
    ): MatchListItemUiModel {
        return MatchListItemUiModel(
            matchId = matchHistoryItem.matchId,
            playerId = matchHistoryItem.playerId,
            isWinner = matchHistoryItem.isWinner,
            gameModeStringRes = matchHistoryItem.gameModeStringRes,
            isRanked = matchHistoryItem.isRanked,
            vpChange = matchHistoryItem.vpChange,
            timeSinceMatch = matchHistoryItem.timeSinceMatch,
            heroImageUrl = matchHistoryItem.heroImageSrc,
            heroName = matchHistoryItem.heroName,
            heroRoleDrawableId = matchHistoryItem.heroRoleDrawableId,
            kills = matchHistoryItem.kills.toString(),
            deaths = matchHistoryItem.deaths.toString(),
            assists = matchHistoryItem.assists.toString(),
            kdaValue = matchHistoryItem.kdaText,
        )
    }
}