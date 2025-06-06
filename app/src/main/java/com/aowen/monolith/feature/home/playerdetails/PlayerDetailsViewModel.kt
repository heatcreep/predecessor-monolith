package com.aowen.monolith.feature.home.playerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserClaimedPlayerRepository
import com.aowen.monolith.network.UserPreferencesManager
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    val claimedPlayerName: String? = null,
    val isEditingPlayerName: Boolean = false,
    val playerNameField: String = "",
    val playerErrors: PlayerErrors? = null,
    val player: PlayerDetails = PlayerDetails(),
    val heroStats: List<PlayerHeroStats> = emptyList(),
    val selectedHeroStats: PlayerHeroStats? = null,
    val stats: PlayerStats = PlayerStats(),
    val matches: List<MatchDetails> = emptyList(),
    val heroes: List<HeroDetails> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository,
    private val omedaCityHeroRepository: HeroRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val authRepository: AuthRepository,
    private val userClaimedPlayerRepository: UserClaimedPlayerRepository,
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

    fun onEditPlayerName() {
        _uiState.update {
            it.copy(
                isEditingPlayerName = !uiState.value.isEditingPlayerName
            )
        }
    }

    fun handlePlayerNameFieldChange(playerName: String) {
        _uiState.update {
            it.copy(
                playerNameField = playerName
            )
        }
    }

    fun handleSaveClaimedPlayerName() {
        val playerName = uiState.value.playerNameField.ifEmpty { null }
        viewModelScope.launch {
            userPreferencesManager.saveClaimedPlayerName(playerName)
            userClaimedPlayerRepository.setClaimedPlayerName(playerName)
            _uiState.update {
                it.copy(
                    claimedPlayerName = uiState.value.playerNameField,
                    isEditingPlayerName = false
                )
            }
        }
    }

    fun handlePlayerHeroStatsSelect(heroId: Int) {
        _uiState.update {
            it.copy(
                selectedHeroStats = uiState.value.heroStats.find { stat ->
                    stat.heroId == heroId
                }
            )
        }
    }

    fun handleSavePlayer(isRemoving: Boolean = false) {
        viewModelScope.launch {
            try {
                userClaimedPlayerRepository.setClaimedUser(
                    isRemoving = isRemoving,
                    uiState.value.stats,
                    uiState.value.player
                )
                userClaimedPlayerRepository.setClaimedPlayerName(null)
                _uiState.update {
                    it.copy(
                        isClaimed = !uiState.value.isClaimed
                    )
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
            try {
                val claimedPlayerName = userPreferencesManager.claimedPlayerName.firstOrNull()
                val playerIdDeferred = async { authRepository.getPlayer() }
                val playerInfoDeferred = async { repository.fetchPlayerInfo(playerId) }
                val playerHeroStatsDeferred = async { repository.fetchAllPlayerHeroStats(playerId) }
                val matchesDeferred = async { repository.fetchMatchesById(playerId) }
                val heroesDeferred = async { omedaCityHeroRepository.fetchAllHeroes() }

                val playerIdResult = playerIdDeferred.await()
                val playerInfoResult = playerInfoDeferred.await()
                val playerHeroStatsResult = playerHeroStatsDeferred.await()
                val matchesResult = matchesDeferred.await()
                val heroes = heroesDeferred.await().getOrThrow()

                if (playerInfoResult.isSuccess &&
                    playerHeroStatsResult.isSuccess &&
                    matchesResult.isSuccess &&
                    playerIdResult.isSuccess
                ) {
                    val playerInfo = playerInfoResult.getOrNull()
                    val playerHeroStats = playerHeroStatsResult.getOrNull()
                    val userPlayerId = playerIdResult.getOrNull()?.playerId ?: ""
                    val isClaimed = userPlayerId == playerId
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            claimedPlayerName = claimedPlayerName,
                            playerErrors = null,
                            playerId = playerId,
                            isClaimed = isClaimed,
                            player = playerInfo?.playerDetails ?: PlayerDetails(),
                            heroStats = playerHeroStats ?: emptyList(),
                            stats = playerInfo?.playerStats ?: PlayerStats(),
                            matches = matchesResult.getOrNull()?.matches ?: emptyList(),
                            heroes = heroes
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
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = PlayerErrors(
                            heroesError = e.message
                        )
                    )
                }
            }

        }
    }
}