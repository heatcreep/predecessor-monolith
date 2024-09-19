package com.aowen.monolith.fakes

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.fakes.data.fakeClaimedPlayer
import com.aowen.monolith.network.ClaimedPlayer
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.network.UserClaimedPlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class ClaimedPlayerScenario {
    object PlayerNullOrEmpty : ClaimedPlayerScenario()
    object PlayerInfoResponseFailure : ClaimedPlayerScenario()
    object PlayerInfoError : ClaimedPlayerScenario()
}

class FakeUserClaimedPlayerRepository(
    private val claimedPlayerScenario: ClaimedPlayerScenario? = null
) : UserClaimedPlayerRepository {
    private val _claimedPlayerState: MutableStateFlow<ClaimedPlayerState> = MutableStateFlow(ClaimedPlayerState.NoClaimedPlayer)
    override val claimedPlayerState = _claimedPlayerState

    private val _claimedPlayerName = MutableStateFlow<String?>(null)
    override val claimedPlayerName = _claimedPlayerName
    override suspend fun getClaimedPlayerName() {
        _claimedPlayerName.update { "heatcreep.tv" }
    }

    private val _setClaimedPlayerCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val setClaimedPlayerCounter: MutableStateFlow<Int> = _setClaimedPlayerCounter

    private val _setClaimedPlayerNameCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val setClaimedPlayerNameCounter: MutableStateFlow<Int> = _setClaimedPlayerNameCounter

    override suspend fun getClaimedPlayer(): Result<ClaimedPlayer?> {
        return when (claimedPlayerScenario) {
            ClaimedPlayerScenario.PlayerNullOrEmpty -> {
                _claimedPlayerState.update { ClaimedPlayerState.NoClaimedPlayer }
                Result.success(null)
            }
            ClaimedPlayerScenario.PlayerInfoResponseFailure -> {
                Result.failure(Exception("Player not found"))
            }
            ClaimedPlayerScenario.PlayerInfoError -> {
                Result.failure(Exception("Failed to get player"))
            }
            else -> {
                _claimedPlayerState.update { ClaimedPlayerState.Claimed(fakeClaimedPlayer) }
                Result.success(fakeClaimedPlayer)
            }
        }

    }

    override suspend fun setClaimedPlayerName(playerName: String?) {
        _setClaimedPlayerNameCounter.value++
    }

    override suspend fun setClaimedUser(
        isRemoving: Boolean,
        playerStats: PlayerStats?,
        playerDetails: PlayerDetails?
    ) {
        _setClaimedPlayerCounter.value++
    }
}