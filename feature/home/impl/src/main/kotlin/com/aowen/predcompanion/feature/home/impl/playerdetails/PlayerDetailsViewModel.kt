package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.model.HeroUiModel
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserClaimedPlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.core.ui.model.RankDetails
import com.aowen.predcompanion.data.PlayerHeroStats
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import com.aowen.predcompanion.logDebug
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlayerDetailsUiModel(
    val playerName: String,
    val isConsolePlayer: Boolean,
    val rankDetails: RankDetails
)

sealed interface PlayerDetailsState {
    data object Loading: PlayerDetailsState
    data class Loaded(val uiState: PlayerDetailsUiState): PlayerDetailsState
    data class Error(val errorMessage: String?): PlayerDetailsState
}

data class PlayerDetailsUiState(
    val claimedPlayerName: String? = null,
    val isEditingPlayerName: Boolean = false,
    val playerNameField: String = "",
    val errorMessage: String? = null,
    val player: PlayerInfo.PlayerDetails? = null,
    val heroStats: List<PlayerHeroStats> = emptyList(),
    val selectedHeroStats: PlayerHeroStats? = null,
    val stats: PlayerInfo.PlayerStats? = null,
    val matches: List<MatchListItemUiModel> = emptyList(),
    val allHeroes: List<HeroUiModel> = emptyList(),
    val allHeroIds: List<Long> = emptyList(),
    val playerId: String = "",
    val isClaimed: Boolean = false
)

@HiltViewModel(assistedFactory = PlayerDetailsViewModel.Factory::class)
class PlayerDetailsViewModel @AssistedInject constructor(
    @Assisted private val playerId: String,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityMatchRepository: MatchRepository,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val userRepository: UserRepository,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val userClaimedPlayerRepository: UserClaimedPlayerRepository,
    private val matchListItemUiMapper: MatchListItemUiMapper,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted playerId: String): PlayerDetailsViewModel
    }

    // Internal state only holds the one-shot data
    private val _uiState = MutableStateFlow<PlayerDetailsState>(PlayerDetailsState.Loading)

    // Public state layers the heroes flow on top reactively
    val uiState: StateFlow<PlayerDetailsState> = combine(
        _uiState,
        omedaCityHeroRepository.allHeroes
    ) { state, heroes ->
        val heroUiModels = heroes.values.map {
            HeroUiModel(
                heroId = it.id,
                name = it.displayName,
                imageSrc = it.imageUrl
            )
        }
        // Only enrich the Loaded state — pass Loading and Error through untouched
        when (state) {
            is PlayerDetailsState.Loaded -> state.copy(
                uiState = state.uiState.copy(allHeroes = heroUiModels)
            )
            else -> state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlayerDetailsState.Loading
    )

    init {
        loadPlayerDetails()
    }

    fun handleRetry() {
        loadPlayerDetails()
    }

    private fun loadPlayerDetails() {
        viewModelScope.launch {
            try {
                val resolvedPlayerId = playerId.ifEmpty { getFreshPlayerId() }
                if (resolvedPlayerId == null) {
                    _uiState.value = PlayerDetailsState.Error("No player ID found. Please claim a player.")
                    return@launch
                }

                val claimedPlayerName = userPreferencesManager.claimedPlayerName.firstOrNull()

                val claimedPlayerIdDeferred = async { claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull() }
                val playerInfoDeferred = async { omedaCityPlayerRepository.fetchPlayerInfo(resolvedPlayerId) }
                val playerHeroStatsDeferred = async { omedaCityPlayerRepository.fetchAllPlayerHeroStats(resolvedPlayerId) }
                val matchesDeferred = async { omedaCityMatchRepository.fetchMatchesById(resolvedPlayerId) }

                val matchesDetails = matchesDeferred.await().getOrThrow()
                val playerInfo = playerInfoDeferred.await().getOrThrow()
                val playerHeroStats = playerHeroStatsDeferred.await().getOrThrow()
                val claimedPlayerId = claimedPlayerIdDeferred.await()

                _uiState.value = PlayerDetailsState.Loaded(
                    PlayerDetailsUiState(
                        claimedPlayerName = claimedPlayerName,
                        playerId = resolvedPlayerId,
                        isClaimed = claimedPlayerId == resolvedPlayerId,
                        player = playerInfo.playerDetails,
                        heroStats = playerHeroStats,
                        stats = playerInfo.playerStats,
                        matches = matchesDetails.matches.mapNotNull {
                            matchListItemUiMapper.buildFrom(it, resolvedPlayerId)
                        }
                    )
                )
            } catch (e: Exception) {
                _uiState.value = PlayerDetailsState.Error(e.message)
            }
        }
    }

    // Helper to read the current Loaded state safely for update functions
    private fun updateLoaded(block: PlayerDetailsUiState.() -> PlayerDetailsUiState) {
        val current = _uiState.value
        if (current is PlayerDetailsState.Loaded) {
            _uiState.value = current.copy(uiState = block(current.uiState))
        }
    }

    fun onEditPlayerName() {
        updateLoaded {
            copy(isEditingPlayerName = !isEditingPlayerName)
        }
    }

    fun handlePlayerNameFieldChange(playerName: String) {
        updateLoaded { copy(playerNameField = playerName) }
    }

    fun handleSaveClaimedPlayerName() {
        viewModelScope.launch {
            val playerName = ((_uiState.value as? PlayerDetailsState.Loaded)
                ?.uiState?.playerNameField).orEmpty().ifEmpty { null }
            userPreferencesManager.saveClaimedPlayerName(playerName)
            userClaimedPlayerRepository.setClaimedPlayerName(playerName)
            updateLoaded {
                copy(
                    claimedPlayerName = playerNameField,
                    isEditingPlayerName = false
                )
            }
        }
    }

    fun handleClaimPlayerStatus(isRemoving: Boolean = false) {
        viewModelScope.launch {
            handleSavePlayer(isRemoving = isRemoving)
        }
    }

    fun handlePlayerHeroStatsSelect(heroId: Long) {
        updateLoaded {
            copy(selectedHeroStats = heroStats.find { it.heroId == heroId })
        }
    }

    private suspend fun handleSavePlayer(isRemoving: Boolean = false) {
        try {
            val state = (_uiState.value as? PlayerDetailsState.Loaded)?.uiState ?: return
            userClaimedPlayerRepository.setClaimedUser(
                isRemoving = isRemoving,
                state.stats,
                state.player
            )
            userClaimedPlayerRepository.setClaimedPlayerName(null)
            updateLoaded { copy(isClaimed = !isRemoving) }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }

    private suspend fun getFreshPlayerId(): String? {
        return claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
    }
}