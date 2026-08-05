package com.aowen.predcompanion.core.data.repository.eternals

import com.aowen.predcompanion.core.data.model.toEternalDetails
import com.aowen.predcompanion.core.model.data.EternalDetails
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeGraphQlCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PredGGEternalRepository @Inject constructor(
    private val predGGNetworkDataSource: PredGGNetworkDataSource
) : EternalRepository {

    private val _allEternals: MutableStateFlow<Map<String, EternalDetails>> =
        MutableStateFlow(emptyMap())
    override val allEternals: StateFlow<Map<String, EternalDetails>> = _allEternals

    override suspend fun fetchAllEternals(): List<EternalDetails> {
        val result = safeGraphQlCall(
            apiCall = { predGGNetworkDataSource.getAllEternals() },
            transform = { data ->
                data.perks.map { it.perkFragment }.toEternalDetails()
            }
        )
        if (result is Resource.Success) {
            _allEternals.update { result.data.associateBy { it.id } }
        }
        return if (result is Resource.Success) result.data else emptyList()
    }

    override fun getEternalById(eternalId: String): EternalDetails? =
        allEternals.value[eternalId]
}