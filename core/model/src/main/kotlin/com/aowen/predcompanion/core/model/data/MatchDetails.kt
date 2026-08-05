package com.aowen.predcompanion.core.model.data


data class MatchDetails(
    val matchId: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val gameDuration: Int = 0,
    val gameMode: Int = 0,
    val region: String = "",
    val winningTeam: String = "",
    val dawn: Team = Team.Dawn(emptyList()),
    val dusk: Team = Team.Dusk(emptyList())
) {

    data class Perk(
        val iconUrl: String,
        val name: String,
        val description: String
    )

    sealed class Team {
        abstract val players: List<MatchPlayerDetails>

        data class Dawn(override val players: List<MatchPlayerDetails>) : Team()
        data class Dusk(override val players: List<MatchPlayerDetails>) : Team()
    }

    data class MatchPlayerDetails(
        val playerId: String = "",
        val playerName: String = "",
        val vpTotal: Int = 0,
        val vpChange: Long? = null,
        val rank: String = "",
        val heroId: String = "",
        val role: String? = null,
        val performanceScore: String = "",
        val performanceTitle: String = "",
        val kills: Int = 0,
        val deaths: Int = 0,
        val assists: Int = 0,
        val minionsKilled: Int = 0,
        val minionsKilledPerMin: Float = 0F,
        val laneMinionsKilled: Int = 0,
        val neutralMinionsKilled: Int = 0,
        val neutralMinionsTeamJungle: Int = 0,
        val neutralMinionsEnemyJungle: Int = 0,
        val largestKillingSpree: Int = 0,
        val largestMultiKill: Int = 0,
        val totalDamageDealt: Int = 0,
        val physicalDamageDealt: Int = 0,
        val magicalDamageDealt: Int = 0,
        val trueDamageDealt: Int = 0,
        val largestCriticalStrike: Int = 0,
        val totalDamageDealtToHeroes: Int = 0,
        val physicalDamageDealtToHeroes: Int = 0,
        val magicalDamageDealtToHeroes: Int = 0,
        val trueDamageDealtToHeroes: Int = 0,
        val totalDamageDealtToStructures: Int = 0,
        val totalDamageDealtToObjectives: Int = 0,
        val totalDamageTaken: Int = 0,
        val physicalDamageTaken: Int = 0,
        val magicalDamageTaken: Int = 0,
        val trueDamageTaken: Int = 0,
        val totalDamageTakenFromHeroes: Int = 0,
        val physicalDamageTakenFromHeroes: Int = 0,
        val magicalDamageTakenFromHeroes: Int = 0,
        val trueDamageTakenFromHeroes: Int = 0,
        val totalDamageMitigated: Int = 0,
        val totalHealingDone: Int = 0,
        val itemHealingDone: Int = 0,
        val crestHealingDone: Int = 0,
        val utilityHealingDone: Int = 0,
        val totalShieldingReceived: Int = 0,
        val wardsPlaced: Int = 0,
        val wardsDestroyed: Int = 0,
        val goldEarned: Int = 0,
        val goldEarnedPerMin: Float = 0F,
        val goldSpent: Int = 0,
        val augment: Perk?,
        val eternal: Perk?,
        val minorBlessing1: Perk?,
        val minorBlessing2: Perk?,
        val crestId: String? = null,
        val trinketId: String? = null,
        val itemIds: List<String> = emptyList(),
        val playerItems: List<ItemDetails> = emptyList()
        // stats

    )
}


