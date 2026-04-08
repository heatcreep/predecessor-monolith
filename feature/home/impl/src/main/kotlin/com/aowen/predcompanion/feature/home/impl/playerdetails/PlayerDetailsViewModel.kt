package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.MatchDetails
import com.aowen.predcompanion.core.common.di.IoDispatcher
import com.aowen.predcompanion.core.data.model.HeroUiModel
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.PlayerDetails
import com.aowen.predcompanion.core.model.data.PlayerStats
import com.aowen.predcompanion.core.model.network.UserState
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.data.PlayerHeroStats
import com.aowen.predcompanion.logDebug
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val allHeroes: List<HeroUiModel> = emptyList(),
    val allHeroIds: List<Long> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false
)

@HiltViewModel(assistedFactory = PlayerDetailsViewModel.Factory::class)
class PlayerDetailsViewModel @AssistedInject constructor(
    @param:IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    @Assisted private val playerId: String,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityMatchRepository: MatchRepository,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val userClaimedPlayerRepository: UserClaimedPlayerRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted playerId: String): PlayerDetailsViewModel
    }

    private val _uiState = MutableStateFlow(PlayerDetailsUiState())
    val uiState: StateFlow<PlayerDetailsUiState> = _uiState

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

    fun handleClaimPlayerStatus(isRemoving: Boolean = false) {
        viewModelScope.launch {
            async { handleSavePlayer(isRemoving = isRemoving) }.await()
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

    private suspend fun handleSavePlayer(isRemoving: Boolean = false) {

        try {
            userClaimedPlayerRepository.setClaimedUser(
                isRemoving = isRemoving,
                uiState.value.stats,
                uiState.value.player
            )
            userClaimedPlayerRepository.setClaimedPlayerName(null)
            _uiState.update {
                it.copy(
                    isClaimed = !isRemoving
                )
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }

    }

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            try {
                val playerId = playerId ?: getFreshPlayerId()
                if (playerId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No player ID found. Please claim a player."
                        )
                    }
                    return@launch
                }
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


                _uiState.update { playerDetailsUiState ->
                    playerDetailsUiState.copy(
                        isLoading = false,
                        claimedPlayerName = claimedPlayerName,
                        errorMessage = null,
                        playerId = playerId,
                        isClaimed = isClaimed,
                        player = playerInfo.playerDetails,
                        heroStats = playerHeroStats,
                        stats = playerInfo.playerStats,
                        matches = matchesDetails.matches,
                        allHeroes = heroes.map {
                            HeroUiModel(
                                heroId = it.id,
                                name = it.displayName,
                                imageId = it.imageId
                            )
                        }
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

    private suspend fun getFreshPlayerId(): String? {
        return when (authRepository.userState.value) {
            is UserState.Authenticated -> {
                userRepository.getUser()?.playerId
            }

            is UserState.Unauthenticated -> {
                claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
            }

            else -> null
        }
    }
}