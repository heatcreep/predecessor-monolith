package com.aowen.predcompanion.core.model.data

import com.aowen.predcompanion.core.resources.R as coreResources

enum class HeroClass(val label: String, val iconRes: Int) {
    MAGE(label = "Mage", iconRes = coreResources.drawable.simple_mid),
    SUPPORT(label = "Support", iconRes = coreResources.drawable.simple_support),
    SHARPSHOOTER(label = "Sharpshooter", iconRes = coreResources.drawable.simple_carry),
    TANK(label = "Tank", iconRes = coreResources.drawable.simple_tank),
    FIGHTER(label = "Fighter", iconRes = coreResources.drawable.simple_fighter),
    ASSASSIN(label = "Assassin", iconRes = coreResources.drawable.simple_assassin),
    UNKNOWN(label = "Unknown", iconRes = coreResources.drawable.unknown)
}