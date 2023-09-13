package com.aowen.monolith.data

enum class AbilityKey(val key: String) {
    LMB("LMB"),
    RMB("RMB"),
    Q("Q"),
    E("E"),
    R("R"),
    PASSIVE("Passive")
}

fun getAbilityKey(index: Int) = AbilityKey.values()[index].key