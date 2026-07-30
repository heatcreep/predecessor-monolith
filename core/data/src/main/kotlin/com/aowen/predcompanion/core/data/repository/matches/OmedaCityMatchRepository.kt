package com.aowen.predcompanion.core.data.repository.matches

import com.aowen.predcompanion.core.data.model.asMatchDetails
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.apollo.ApolloKotlinPredGGNetwork
import com.aowen.predcompanion.core.network.safeGraphQlCall
import javax.inject.Inject

class OmedaCityMatchRepository @Inject constructor(
    private val predGGNetwork: ApolloKotlinPredGGNetwork,
) : MatchRepository {

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getMatchById(matchId) },
            transform = { matchResponse -> matchResponse.match?.asMatchDetails() }
        )
}