package com.aowen.predcompanion.feature.home.impl.matches.model.mapper

import com.aowen.predcompanion.core.data.model.getKda
import com.aowen.predcompanion.core.data.model.mapper.HeroMapper
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchListItemUiModel
import com.aowen.predcompanion.ui.utils.handleTimeSinceMatch
import javax.inject.Inject

class MatchListItemUiMapper @Inject constructor(
    private val heroRepository: HeroRepository,
    private val heroMapper: HeroMapper,
) {
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
            matchTypeText = match.matchType?.text,
            isRanked = match.matchType == MatchDetails.MatchType.RANKED,
            vpChange = playerHero.vpChange,
            timeSinceMatch = handleTimeSinceMatch(match.endTime),
            heroImageUrl = heroRepository.getHeroImageSrcById(playerHero.heroId),
            heroName = heroMapper.getHeroName(playerHero.heroId),
            heroRoleDrawableId = heroMapper.getHeroRole(playerHero.role)?.drawableId,
            performanceScore = playerHero.performanceScore,
            kills = playerHero.kills.toString(),
            deaths = playerHero.deaths.toString(),
            assists = playerHero.assists.toString(),
            kdaValue = playerHero.getKda(),
        )
    }
}
