package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.eternals.EternalRepository
import com.aowen.predcompanion.core.model.data.EternalDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeEternalRepository : EternalRepository {

    private val _allEternals = MutableStateFlow<Map<String, EternalDetails>>(emptyMap())
    override val allEternals: StateFlow<Map<String, EternalDetails>> = _allEternals

    var eternalsToReturn: List<EternalDetails> = emptyList()

    override suspend fun fetchAllEternals(): List<EternalDetails> = eternalsToReturn

    override fun getEternalById(eternalId: String): EternalDetails? =
        _allEternals.value[eternalId]

    fun emitEternals(eternals: List<EternalDetails>) {
        _allEternals.value = eternals.associateBy { it.id }
    }
}
