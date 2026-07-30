package com.aowen.predcompanion.core.data.repository.matches

import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.network.Resource

interface MatchRepository {

    suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?>
}