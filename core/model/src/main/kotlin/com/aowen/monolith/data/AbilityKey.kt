package com.aowen.monolith.data

enum class AbilityKey(val pc: String? = null, val xbox: String? = null, val ps5: String? = null) {
    Passive,
    BasicAttack("LMB", "RT", "R2"),
    Alternate("RMB", "RB", "R1"),
    Primary("Q", "LB", "L1"),
    Secondary("E", "LT", "L2"),
    Ultimate("R", "LB + RB", "L1 + R1")
}

fun getAbilityName(index: Int): String {
    val capitalizationRegex = Regex("(?<=\\p{Ll})(?=\\p{Lu})")
    return AbilityKey.entries[index].name.split(capitalizationRegex).joinToString(" ")
}

fun getLevelingAbilities(console: Console) : List<String> {
    return AbilityKey.entries.filter{
        it != AbilityKey.Passive && it != AbilityKey.BasicAttack
    }.map { entry ->
        when (console) {
            Console.PC -> entry.pc?: ""
            Console.Xbox -> entry.xbox?: ""
            Console.PS5 -> entry.ps5?: ""
        }
    }
}

fun getAbilityKey(index: Int, console: Console): String? {
    return when (console) {
        Console.PC -> AbilityKey.entries[index].pc
        Console.Xbox -> AbilityKey.entries[index].xbox
        Console.PS5 -> AbilityKey.entries[index].ps5
    }
}