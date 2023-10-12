package com.aowen.monolith.ui.screens.playerdetails

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

data class PlayerErrors(
    val playerInfoErrorMessage: String? = null,
    val playerInfoError: String? = null,
    val heroStatsErrorMessage: String? = null,
    val heroStatsError: String? = null,
    val heroStatsErrorMessages: String? = null,
    val matchesErrorMessage: String? = null,
    val matchesError: String? = null,
    val statsErrorMessage: String? = null,
    val statsError: String? = null,
    val heroesError: String? = null,
    val heroesErrorMessage: String? = null
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleTimeSinceMatch(endTime: String): String {
        val zonedDateTime = ZonedDateTime.parse(endTime)
        val startOfDay = zonedDateTime.toLocalDate().atStartOfDay(zonedDateTime.zone)
        val now = ZonedDateTime.now(zonedDateTime.zone)
        val duration = Duration.between(startOfDay, now).toHours()
        val durationDays = Duration.between(startOfDay, now).toDays()
        val daysText = if (durationDays == 1L) "day ago" else "days ago"
        return if (duration < 24) "${duration}h ago" else "$durationDays $daysText"
    }

    suspend fun handleSavePlayer(isRemoving: Boolean = false) {
        val id = if (isRemoving) "" else playerId
        viewModelScope.launch {
            try {
                val userInfoDeferred = async { authRepository.handleSavePlayer(id) }
                val userInfo = userInfoDeferred.await()

                if (userInfo.isSuccess) {
                    userRepository.setClaimedUser(uiState.value.stats, uiState.value.player)
                    _uiState.update {
                        it.copy(
                            isClaimed = !uiState.value.isClaimed
                        )
                    }
                    logDebug("Successfully saved player")
                } else {
                    logDebug("Failed to save player")
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
                playerIdResult.isSuccess
            ) {
                val playerInfo = playerInfoResult.getOrNull()
                val playerHeroStats = playerHeroStatsResult.getOrNull() ?: emptyList()
                val heroes = heroesResult.getOrNull() ?: emptyList()
                val userPlayerId = playerIdResult.getOrNull()?.playerId ?: ""
                val isClaimed = userPlayerId == playerId
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = null,
                        playerId = playerId,
                        isClaimed = isClaimed,
                        player = playerInfo?.playerDetails ?: PlayerDetails(),
                        heroStats = playerHeroStats,
                        stats = playerInfo?.playerStats ?: PlayerStats(),
                        matches = matchesResult.getOrNull() ?: listOf(),
                        heroes = heroes,
                        playerRankUrl = playerInfo?.playerDetails?.rankImage ?: "no image"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playerErrors = PlayerErrors(
                            playerInfoErrorMessage = "Unable to fetch player info.",
                            playerInfoError = playerInfoResult.exceptionOrNull()?.message,
                            heroStatsErrorMessage = "Unable to fetch player hero stats.",
                            heroStatsError = playerHeroStatsResult.exceptionOrNull()?.message,
                            matchesErrorMessage = "Unable to fetch player matches.",
                            matchesError = matchesResult.exceptionOrNull()?.message,
                            statsErrorMessage = "Unable to fetch player stats.",
                            statsError = playerInfoResult.exceptionOrNull()?.message,
                            heroesErrorMessage = "Unable to fetch heroes.",
                        )
                    )
                }
            }
        }
    }
}