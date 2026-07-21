package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.aowen.predcompanion.core.data.model.MatchHistoryTarget
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchHistoryRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.network.apollo.type.PlayerKey
import com.aowen.predcompanion.core.network.model.NetworkUserState
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import com.aowen.predcompanion.core.ui.model.toPlayerProfileCardUiModel
import com.apollographql.apollo.api.Optional
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed interface PlayerDetailsState {
    data object Loading : PlayerDetailsState
    data class Loaded(val playerProfileCardUiModel: PlayerProfileCardUiModel) : PlayerDetailsState
    data class Error(val errorMessage: String?) : PlayerDetailsState
}

@HiltViewModel(assistedFactory = PlayerDetailsViewModel.Factory::class)
class PlayerDetailsViewModel @AssistedInject constructor(
    @Assisted private val playerId: String,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val claimedPlayerDao: ClaimedPlayerDao,
    matchHistoryRepository: MatchHistoryRepository,
    private val matchListItemUiMapper: MatchListItemUiMapper,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted playerId: String): PlayerDetailsViewModel
    }

    // Internal state only holds the one-shot data
    private val _uiState = MutableStateFlow<PlayerDetailsState>(PlayerDetailsState.Loading)

    // Public state layers the heroes flow on top reactively
    val uiState: StateFlow<PlayerDetailsState> = _uiState

    val matchHistory: Flow<PagingData<MatchListItemUiModel>> =
        matchHistoryRepository.getMatchHistoryFor(
            MatchHistoryTarget.SpecificPlayer(
                playerKey = PlayerKey(
                    uuid = Optional.present(playerId)
                )
            )
        ).map { pagingData ->
            pagingData.map {
                matchListItemUiMapper.buildFrom(it)
            }
        }.cachedIn(viewModelScope)


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
                    _uiState.value =
                        PlayerDetailsState.Error("No player ID found. Please claim a player.")
                    return@launch
                }
                val player = omedaCityPlayerRepository.fetchPlayerById(playerId)
                    ?.toPlayerProfileCardUiModel()
                if (player != null) {
                    _uiState.value = PlayerDetailsState.Loaded(
                        player
                    )
                }


            } catch (e: Exception) {
                _uiState.value = PlayerDetailsState.Error(e.message)
            }
        }
    }

    private suspend fun getFreshPlayerId(): String? {
        return when (authRepository.networkUserState.value) {
            is NetworkUserState.Authenticated -> {
                userRepository.sync()
                ""
            }

            is NetworkUserState.Unauthenticated -> claimedPlayerDao.getClaimedPlayerIds()
                .firstOrNull()?.firstOrNull()

            else -> null
        }
    }
}