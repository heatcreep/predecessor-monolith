package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.network.apollo.fragment.MatchResultsFragment
import com.aowen.predcompanion.core.network.apollo.type.GameMode
import com.aowen.predcompanion.core.network.apollo.type.Role
import com.aowen.predcompanion.core.resources.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


fun MatchResultsFragment.Result.asMatchHistoryItem(): MatchHistoryItem {
    val isWinner = this.team == this.match.winningTeam
    val isRanked = this.match.gameMode == GameMode.RANKED
    val vpChange =
        this.rating?.newPoints?.let { newPoints ->
            this.rating?.points?.let { points ->
                newPoints - points
            }
        }?.toString() ?: "-"

    return MatchHistoryItem(
        matchId = this.match.id,
        playerId = this.player.id,
        isWinner = isWinner,
        gameModeStringRes = getGameModeStringRes(this.match.gameMode),
        isRanked = isRanked,
        vpChange = vpChange,
        timeSinceMatch = handleTimeSinceMatch(this.match.endTime),
        heroImageSrc = this.heroData?.icon?.let { ImageHelpers.buildAssetsUrl(it) } ?: "",
        heroName = this.heroData?.displayName ?: "",
        heroRoleDrawableId = getHeroRoleDrawableId(this.role),
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        kdaText = this.getKda()
    )
}

private fun getGameModeStringRes(gameMode: GameMode): Int {
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

fun handleTimeSinceMatch(endTime: Instant): String {

    val tz = TimeZone.currentSystemDefault()

    val pastLocalInstant = endTime.toLocalDateTime(tz).toInstant(tz)

    val nowInstant = Clock.System.now()
    val nowLocalInstant = nowInstant.toLocalDateTime(tz).toInstant(tz)

    val duration = nowLocalInstant.minus(pastLocalInstant)

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