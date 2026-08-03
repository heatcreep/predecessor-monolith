package com.aowen.predcompanion.core.model.data

import java.util.UUID

data class HeroBuild(
    val id: String = "",
    val userId: String? = null,
    val title: String = "",
    val author: String = "",
    val role: String = "unknown",
    val description: String? = "",
    val heroId: Long = 999,
    val crestId: Int = 0,
    val buildItemIds: List<Int> = emptyList(),
    val skillOrder: List<Int>? = null,
    val fiveStarScore: String = "0.0",
    val modules: List<ItemModule> = emptyList(),
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val version: String? = ""
) {
    data class ItemModule(
        val id: String? = UUID.randomUUID().toString(),
        val title: String = "",
        val itemIds: List<Int> = emptyList(),
    )
}







