package com.aowen.predcompanion.core.model.data

data class Player(
    val id: String = "",
    val name: String = "",
    val winRate: String = "",
    val rankIconUrl: String = "",
    val currentRankTitle: String = "",
    val currentRankPoints: String = "",
    val favoriteRole: String = "",
    val heroStatistics: List<PlayerHeroStatistics>,
    val favoriteHero: HeroDetails? = null,
)