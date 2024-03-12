package com.aowen.monolith.feature.search.playerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerErrors(
    val playerIdErrorMessage: String? =
        PlayerDetailsViewModel.FetchPlayerIdErrorMessage,
    val playerIdError: String? = null,
    val playerInfoErrorMessage: String? =
        PlayerDetailsViewModel.FetchPlayerInfoErrorMessage,
    val playerInfoError: String? = null,
    val heroStatsErrorMessage: String? =
        PlayerDetailsViewModel.FetchPlayerHeroStatsErrorMessage,
    val heroStatsError: String? = null,
    val matchesErrorMessage: String? =
        PlayerDetailsViewModel.FetchMatchesErrorMessage,
    val matchesError: String? = null,
    val statsErrorMessage: String? =
        PlayerDetailsViewModel.FetchPlayerHeroStatsErrorMessage,
    val statsError: String? = null,
    val heroesErrorMessage: String? =
        PlayerDetailsViewModel.FetchHeroesErrorMessage,
    val heroesError: String? = null,
)

data class PlayerDetailsUiState(
    val isLoading: Boolean = true,
    val playerErrors: PlayerErrors? = null,
    val player: PlayerDetails = PlayerDetails(),
    val heroStats: List<PlayerHeroStats> = emptyList(),
    val selectedHeroStats: PlayerHeroStats? = null,
    val stats: PlayerStats = PlayerStats(),
    val matches: List<MatchDetails> = emptyList(),
    val heroes: List<HeroDetails> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false,
    val playerRankUrl: String? = "no image"
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        const val FetchPlayerIdErrorMessage = "Unable to fetch player id."
        const val FetchPlayerInfoErrorMessage = "Unable to fetch player info."
        const val FetchPlayerHeroStatsErrorMessage = "Unable to fetch player hero stats."
        const val FetchMatchesErrorMessage = "Unable to fetch player matches."
        const val FetchHeroesErrorMessage = "Unable to fetch heroes."
    }

    private val _uiState = MutableStateFlow(PlayerDetailsUiState())
    val uiState: StateFlow<PlayerDetailsUiState> = _uiState

    private val playerId: String = checkNotNull(savedStateHandle["playerId"])

    fun handlePlayerHeroStatsSelect(heroId: Int) {
        _uiState.update {
            it.copy(
                selectedHeroStats = uiState.value.heroStats.find { stat ->
                    stat.heroId == heroId
                }
            )
        }
    }

    suspend fun handleSavePlayer(isRemoving: Boolean = false) {
        val id = if (isRemoving) "" else playerId
        viewModelScope.launch {
            try {
                val userInfoDeferred = async { authRepository.handleSavePlayer(id) }
                val userInfo = userInfoDeferred.await()

                if (userInfo.isSuccess) {
                    if(isRemoving) {
                        userRepository.setClaimedUser(null, null)
                    } else {
                        userRepository.setClaimedUser(uiState.value.stats, uiState.value.player)
                    }
                    _uiState.update {
                        it.copy(
                            isClaimed = !uiState.value.isClaimed
                        )
                    }
                }

            } catch (e: Exception) {
                logDebug(e.toString())
            }
        }
    }

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val playerIdDeferred = async { authRepository.getPlayer() }
            val playerInfoDeferred = async { repository.fetchPlayerInfo(playerId) }
            val playerHeroStatsDeferred = async { repository.fetchAllPlayerHeroStats(playerId) }
            val matchesDeferred = async { repository.fetchMatchesById(playerId) }
            val heroesDeferred = async { repository.fetchAllHeroes() }

            val playerIdResult = playerIdDeferred.await()
            val playerInfoResult = playerInfoDeferred.await()
            val playerHeroStatsResult = playerHeroStatsDeferred.await()
            val matchesResult = matchesDeferred.await()
            val heroesResult = heroesDeferred.await()

            if (playerInfoResult.isSuccess &&
                playerHeroStatsResult.isSuccess &&
                matchesResult.isSuccess &&
                playerIdResult.isSuccess &&
                heroesResult.isSuccess
            ) {
                val playerInfo = playerInfoResult.getOrNull()
                val playerHeroStats = playerHeroStatsResult.getOrNull()
                val heroes = heroesResult.getOrNull()
                val userPlayerId = playerIdResult.getOrNull()?.playerId ?: ""
                val isClaimed = userPlayerId == playerId
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = null,
                        playerId = playerId,
                        isClaimed = isClaimed,
                        player = playerInfo?.playerDetails ?: PlayerDetails(),
                        heroStats = playerHeroStats ?: emptyList(),
                        stats = playerInfo?.playerStats ?: PlayerStats(),
                        matches = matchesResult.getOrNull()?.matches ?: emptyList(),
                        heroes = heroes ?: emptyList(),
                        playerRankUrl = playerInfo?.playerDetails?.rankImage ?: "no image"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = PlayerErrors(
                            playerIdError = playerIdResult.exceptionOrNull()?.message,
                            playerInfoError = playerInfoResult.exceptionOrNull()?.message,
                            heroStatsError = playerHeroStatsResult.exceptionOrNull()?.message,
                            matchesError = matchesResult.exceptionOrNull()?.message,
                            statsError = playerInfoResult.exceptionOrNull()?.message,
                            heroesError = heroesResult.exceptionOrNull()?.message
                        )
                    )
                }
            }
        }
    }
}