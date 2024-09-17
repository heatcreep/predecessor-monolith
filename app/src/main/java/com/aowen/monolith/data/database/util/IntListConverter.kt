package com.aowen.monolith.data.database.util

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun intListToString(intList: List<Int>): String {
        return intList.joinToString(",")
    }

    @TypeConverter
    fun stringToIntList(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }
}