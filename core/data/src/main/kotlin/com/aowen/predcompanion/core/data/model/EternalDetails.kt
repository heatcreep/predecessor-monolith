package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.BlessingDetails
import com.aowen.predcompanion.core.model.data.EternalCategory
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.core.network.apollo.fragment.PerkFragment

fun List<PerkFragment>.toEternalDetails(): List<EternalDetails> =
    mapNotNull { perk ->
        val data = perk.data ?: return@mapNotNull null
        val category = data.eternalCategoryData ?: return@mapNotNull null
        EternalDetails(
            id = data.id,
            displayName = data.displayName,
            description = data.description,
            icon = data.icon,
            eternalCategory = EternalCategory(
                id = category.id,
                displayName = category.displayName,
            ),
            blessings = data.minorBlessings?.map { blessing ->
                BlessingDetails(
                    id = blessing.id,
                    displayName = blessing.displayName,
                    description = blessing.description,
                    icon = blessing.icon,
                )
            } ?: emptyList()
        )
    }
