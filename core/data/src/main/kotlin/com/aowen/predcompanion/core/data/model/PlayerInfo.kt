package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.apollo.fragment.PlayerFragment
import com.aowen.predcompanion.core.network.model.NetworkPlayer
import com.aowen.predcompanion.core.network.model.NetworkPlayerSearchResult
import com.aowen.predcompanion.core.network.model.NetworkPlayerStats
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.UUID

fun PlayerFragment.asPlayerDetails(): PlayerInfo.PlayerDetails {
    return PlayerInfo.PlayerDetails(
        playerId = this.id,
        playerName = this.name ?: "",
    )
}

fun NetworkPlayer.asPlayerDetails(): PlayerInfo.PlayerDetails {
    return PlayerInfo.PlayerDetails(
        playerId = this.id,
        playerName = this.displayName,
        region = this.region,
        rank = this.rank,
        rankTitle = this.rankTitle,
        rankImage = this.rankImage,
        vpTotal = this.vpTotal ?: 0,
        vpCurrent = this.vpCurrent ?: 0,
        mmr = this.mmr?.toDecimal(),
        isMmrDisabled = this.flags.any { it.identifier == "mmr_disabled" },
        isCheater = this.flags.any { it.identifier == "cheater" }
    )
}

fun NetworkPlayerSearchResult.asPlayerDetails(): PlayerInfo.PlayerDetails =
    PlayerInfo.PlayerDetails(
        playerId = this.playerId.toString(),
        playerName = this.displayName,
        region = this.region,
        rank = this.rank,
        rankTitle = this.rankTitle,
        rankImage = this.rankImage,
        isRanked = this.isRanked,
        mmr = this.mmr?.toDecimal(),
        isMmrDisabled = false,
        isCheater = false
    )

fun PlayerInfo.PlayerDetails.asNetworkPlayerSearchResult(): NetworkPlayerSearchResult {
    return NetworkPlayerSearchResult(
        createdAt = Timestamp(System.currentTimeMillis()).toString(),
        id = UUID.fromString(this.playerId),
        playerId = UUID.fromString(this.playerId),
        displayName = this.playerName,
        region = this.region,
        rank = this.rank,
        rankTitle = this.rankTitle,
        rankImage = this.rankImage,
        isRanked = this.isRanked,
        mmr = this.mmr?.toFloat(),
    )
}

fun NetworkPlayerStats.asPlayerStats(): PlayerInfo.PlayerStats {
    return PlayerInfo.PlayerStats(
        matchesPlayed = matchesPlayed.toString(),
        hoursPlayed = hoursPlayed.toString(),
        averagePerformanceScore = averagePerformanceScore.toPlainString(),
        averageKda = averageKda.map { it.toPlainString() },
        averageKdaRatio = averageKdaRatio.toPlainString(),
        favoriteHero = PlayerInfo.PlayerStats.FavoriteHero(
            name = favoriteHero.name,
            imageUrl = "https://omeda.city/${favoriteHero.image}"
        ),
        favoriteRole = favoriteRole?.replaceFirstChar { it.uppercase() } ?: "",
        winRate = winRate.toPercentageString()
    )
}


fun Float?.toDecimal(pattern: String = "#.#"): String {
    val float = this?.toDouble() ?: 0.0
    val symbols = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat(pattern, symbols)
    return df.format(float)
}