package com.aowen.predcompanion.core.data.repository.eternals

import com.aowen.predcompanion.core.model.data.EternalDetails
import kotlinx.coroutines.flow.StateFlow

interface EternalRepository {

    val allEternals: StateFlow<Map<String, EternalDetails>>
    suspend fun fetchAllEternals(): List<EternalDetails>
    fun getEternalById(eternalId: String) : EternalDetails?
}