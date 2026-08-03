package com.aowen.predcompanion.core.ui.model.mapper

import android.content.Context
import com.aowen.predcompanion.core.data.model.getKda
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.ui.R
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.ui.utils.handleTimeSinceMatch
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import com.aowen.predcompanion.core.resources.R as coreResources

class MatchListItemUiMapper @Inject constructor(
    @param:ApplicationContext private val context: Context,
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
            isRanked = match.gameMode == coreResources.string.core_resources_match_type_ranked,
            vpChange = playerHero.vpChange,
            timeSinceMatch = handleTimeSinceMatch(match.endTime),
            heroImageUrl = heroRepository.getHeroImageSrcById(playerHero.heroId),
            heroName = heroRepository.getHeroName(playerHero.heroId),
            heroId = playerHero.heroId,

            heroRoleDrawableId = heroRoleMap[playerHero.role?.lowercase()]?.drawableId,
            kills = playerHero.kills.toString(),
            deaths = playerHero.deaths.toString(),
            assists = playerHero.assists.toString(),
            kdaValue = playerHero.getKda(),
            minionsKilled = context.getString(
                R.string.core_ui_minions_killed,
                playerHero.minionsKilled
            ),
            csPerMin = "",
        )
    }

    fun buildFrom(
        matchHistoryItem: MatchHistoryItem
    ): MatchListItemUiModel {
        val csPerMin = String.format(Locale.US, "%.1f", matchHistoryItem.csPerMin)
        return MatchListItemUiModel(
            matchId = matchHistoryItem.matchId,
            playerId = matchHistoryItem.playerId,
            isWinner = matchHistoryItem.isWinner,
            gameModeStringRes = matchHistoryItem.gameModeStringRes,
            isRanked = matchHistoryItem.isRanked,
            vpChange = context.getString(R.string.core_ui_vp_change, matchHistoryItem.vpChange),
            timeSinceMatch = matchHistoryItem.timeSinceMatch,
            heroImageUrl = matchHistoryItem.heroImageSrc,
            heroName = matchHistoryItem.heroName,
            heroId = matchHistoryItem.heroId,
            augmentImageSrc = matchHistoryItem.augmentImageSrc,
            eternalImageSrc = matchHistoryItem.eternalImageSrc,
            crest = matchHistoryItem.crest?.let {
                MatchListItemUiModel.ItemBoxUiModel(
                    id = it.name,
                    imageSrc = it.imageSrc
                )
            },
            trinket = matchHistoryItem.trinket?.let {
                MatchListItemUiModel.ItemBoxUiModel(
                    id = it.name,
                    imageSrc = it.imageSrc
                )
            },
            items = matchHistoryItem.items.map {
                it?.let {
                    MatchListItemUiModel.ItemBoxUiModel(
                        id = it.name,
                        imageSrc = it.imageSrc
                    )
                }
            },
            heroRoleDrawableId = matchHistoryItem.heroRoleDrawableId,
            kills = matchHistoryItem.kills.toString(),
            deaths = matchHistoryItem.deaths.toString(),
            assists = matchHistoryItem.assists.toString(),
            minionsKilled = context.getString(
                R.string.core_ui_minions_killed,
                matchHistoryItem.minionsKilled
            ),
            csPerMin = context.getString(
                R.string.core_ui_cs_per_min,
                csPerMin
            ),
            kdaValue = matchHistoryItem.kdaText,
        )
    }
}