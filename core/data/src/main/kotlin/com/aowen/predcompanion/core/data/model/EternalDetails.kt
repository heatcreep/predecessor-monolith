package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.BlessingDetails
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.core.network.apollo.fragment.PerkFragment
import com.aowen.predcompanion.core.network.apollo.type.PerkSlot

fun List<PerkFragment>.toEternalDetails(): List<EternalDetails> =
    mapNotNull { perk ->
        val data = perk.data ?: return@mapNotNull null
        val eternalData = data.eternalCategory ?: return@mapNotNull null
        val category = data.eternalCategoryData ?: return@mapNotNull null

        val (firstMinorBlessing, secondMinorBlessing) = data.minorBlessings?.partition { blessing ->
            blessing.slot == PerkSlot.BLESSING_MINOR_1
        } ?: Pair(emptyList(), emptyList())

        EternalDetails(
            id = data.id,
            displayName = data.displayName,
            description = data.description,
            icon = data.icon,
            eternalCategory = EternalCategory(
                id = category.id,
                colorString = "#{${category.color}",
                categoryType = eternalData.id.toCategoryType(),
                categoryDescription = category.description,
                displayName = category.displayName,
            ),
            firstMinorBlessing = firstMinorBlessing.map { blessing ->
                BlessingDetails(
                    id = blessing.id,
                    displayName = blessing.displayName,
                    description = blessing.description,
                    icon = blessing.icon,
                )
            },
            secondMinorBlessing = secondMinorBlessing.map { blessing ->
                BlessingDetails(
                    id = blessing.id,
                    displayName = blessing.displayName,
                    description = blessing.description,
                    icon = blessing.icon,
                )
            }
        )
    }

private fun String.toCategoryType(): EternalCategory.CategoryType =
    when (this) {
        "1" -> EternalCategory.CategoryType.ANOMALIES
        "2" -> EternalCategory.CategoryType.DIVINES
        "3" -> EternalCategory.CategoryType.DREADNOUGHTS
        "4" -> EternalCategory.CategoryType.HARBINGERS
        "6" -> EternalCategory.CategoryType.PRIMARCHS
        "7" -> EternalCategory.CategoryType.SOVEREIGNS
        else -> EternalCategory.CategoryType.UNKNOWN
    }
