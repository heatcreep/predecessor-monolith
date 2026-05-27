package com.aowen.predcompanion.core.data.repository.matches

import com.aowen.predcompanion.core.data.model.asMatchDetails
import com.aowen.predcompanion.core.data.model.asMatchesDetails
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchesDetails
import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import javax.inject.Inject

class OmedaCityMatchRepository @Inject constructor(
    private val retrofitOmedaCityNetwork: PredCompanionNetworkDataSource
) : MatchRepository {

    override suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Resource<MatchesDetails> =
        safeApiCall(
            apiCall = {
                retrofitOmedaCityNetwork.getPlayerMatchesById(
                    playerId = playerId,
                    perPage = perPage,
                    timeFrame = timeFrame,
                    heroId = heroId,
                    role = role,
                    playerName = playerName,
                    page = page
                )
            },
            transform = { matchesResponse -> matchesResponse.asMatchesDetails() }
        )

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> =
        safeApiCall(
            apiCall = { retrofitOmedaCityNetwork.getMatchById(matchId) },
            transform = { matchResponse -> matchResponse.asMatchDetails() }
        )
}