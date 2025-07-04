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
import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
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

data class PlayerDetailsUiState(
    val isLoading: Boolean = true,
    val claimedPlayerName: String? = null,
    val isEditingPlayerName: Boolean = false,
    val playerNameField: String = "",
    val errorMessage: String? = null,
    val player: PlayerDetails? = null,
    val heroStats: List<PlayerHeroStats> = emptyList(),
    val selectedHeroStats: PlayerHeroStats? = null,
    val stats: PlayerStats? = null,
    val matches: List<MatchDetails> = emptyList(),
    val heroes: List<HeroDetails> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false
)

@HiltViewModel
class PlayerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityMatchRepository: MatchRepository,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val authRepository: AuthRepository,
    private val userClaimedPlayerRepository: UserClaimedPlayerRepository,
) : ViewModel() {

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

    fun handlePlayerHeroStatsSelect(heroId: Long) {
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
                val userProfileDeferred = async { authRepository.getPlayer() }
                val playerInfoDeferred =
                    async { omedaCityPlayerRepository.fetchPlayerInfo(playerId) }
                val playerHeroStatsDeferred =
                    async { omedaCityPlayerRepository.fetchAllPlayerHeroStats(playerId) }
                val matchesDeferred = async { omedaCityMatchRepository.fetchMatchesById(playerId) }
                val heroesDeferred = async { omedaCityHeroRepository.fetchAllHeroes() }


                val heroes = heroesDeferred.await().getOrThrow()
                val matchesDetails = matchesDeferred.await().getOrThrow()
                val playerInfo = playerInfoDeferred.await().getOrThrow()
                val playerHeroStats = playerHeroStatsDeferred.await().getOrThrow()
                val userProfile = userProfileDeferred.await().getOrThrow()

                val isClaimed = userProfile?.playerId == playerId


                _uiState.update {
                    it.copy(
                        isLoading = false,
                        claimedPlayerName = claimedPlayerName,
                        errorMessage = null,
                        playerId = playerId,
                        isClaimed = isClaimed,
                        player = playerInfo.playerDetails,
                        heroStats = playerHeroStats,
                        stats = playerInfo.playerStats,
                        matches = matchesDetails.matches,
                        heroes = heroes
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                    )
                }
            }

        }
    }
}