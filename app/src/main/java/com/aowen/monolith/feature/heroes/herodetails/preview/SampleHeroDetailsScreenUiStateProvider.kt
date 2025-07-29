package com.aowen.monolith.feature.heroes.herodetails.preview

import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.HeroBaseStats
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.feature.heroes.herodetails.HeroDetailsUiState
import java.math.BigDecimal

val heroBuildsLoadingState = HeroDetailsUiState(
    isLoading = false,
    statistics = HeroStatistics(
        winRate = 50f,
        pickRate = 62.13f
    ),
    hero = HeroDetails(
        name = "Narbash",
        displayName = "Narbash",
        stats = listOf(1, 2, 3, 4, 5),
        abilities = listOf(
            AbilityDetails(
                displayName = "Wallop",
                menuDescription = "Melee basic attack dealing 55 (+90% Physical Power) physical damage.\\r\\n\\r\\nGrants 2 Rhythm stacks on hit.",
                image = "",
                cooldown = listOf(),
                cost = listOf(),
                gameDescription = ""
            )
        ),
        baseStats = HeroBaseStats(
            maxHealth = listOf(BigDecimal.TEN),
            healthRegen = listOf(BigDecimal.TEN),
            maxMana = listOf(BigDecimal.TEN),
            manaRegen = listOf(BigDecimal.TEN),
            attackSpeed = listOf(BigDecimal.TEN),
            physicalArmor = listOf(BigDecimal.TEN),
            magicalArmor = listOf(BigDecimal.TEN),
            physicalPower = listOf(BigDecimal.TEN),
            movementSpeed = 11f,
            cleave = 1f,
            attackRange = 1f
        ),
        roles = listOf(
            HeroRole.Support
        ),
        classes = listOf(
            HeroClass.FIGHTER,
            HeroClass.TANK
        ),
    ),
)

val heroBuildsState = HeroDetailsUiState(
    isLoading = false,
    isLoadingBuilds = false,
    statistics = HeroStatistics(
        winRate = 50f,
        pickRate = 62.13f
    ),
    heroBuilds = listOf(
        BuildListItem(
            id = 1,
            title = "Muriel Support Build [0.13.1]",
            description = "Test Build Description",
            heroId = 15,
            buildItems = listOf(1, 1, 1, 1, 1),
            createdAt = "2021-01-01",
            updatedAt = "2021-01-01",
            author = "heatcreep.tv",
            crest = 1,
            role = "Support"
        )
    ),
    hero = HeroDetails(
        name = "Narbash",
        displayName = "Narbash",
        stats = listOf(1, 2, 3, 4, 5),
        abilities = listOf(
            AbilityDetails(
                displayName = "Wallop",
                menuDescription = "Melee basic attack dealing 55 (+90% Physical Power) physical damage.\\r\\n\\r\\nGrants 2 Rhythm stacks on hit.",
                image = "",
                cooldown = listOf(),
                cost = listOf(),
                gameDescription = ""
            )
        ),
        baseStats = HeroBaseStats(
            maxHealth = listOf(BigDecimal.TEN),
            healthRegen = listOf(BigDecimal.TEN),
            maxMana = listOf(BigDecimal.TEN),
            manaRegen = listOf(BigDecimal.TEN),
            attackSpeed = listOf(BigDecimal.TEN),
            physicalArmor = listOf(BigDecimal.TEN),
            magicalArmor = listOf(BigDecimal.TEN),
            physicalPower = listOf(BigDecimal.TEN),
            movementSpeed = 11f,
            cleave = 1f,
            attackRange = 1f
        ),
        roles = listOf(
            HeroRole.Support
        ),
        classes = listOf(
            HeroClass.FIGHTER,
            HeroClass.TANK
        ),
    ),
)

