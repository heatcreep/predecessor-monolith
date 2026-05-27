package com.aowen.predcompanion.core.model.data

enum class AbilityKey(val pc: String? = null, val xbox: String? = null, val ps5: String? = null) {
    Passive,
    BasicAttack("LMB", "RT", "R2"),
    Alternate("RMB", "RB", "R1"),
    Primary("Q", "LB", "L1"),
    Secondary("E", "LT", "L2"),
    Ultimate("R", "LB + RB", "L1 + R1")
}

