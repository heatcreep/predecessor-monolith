package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.AbilityKey
import com.aowen.predcompanion.core.model.ui.theme.Console

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