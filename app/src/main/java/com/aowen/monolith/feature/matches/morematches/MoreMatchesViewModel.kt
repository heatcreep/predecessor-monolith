package com.aowen.monolith.feature.matches.morematches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

enum class TimeFrame(val code: String, val description: String) {
    ALL("ALL", "All Time"),
    LAST_3_MONTHS(code = "3M", description = "Last 3 Months"),
    LAST_2_MONTHS(code = "2M", description = "Last 2 Months"),
    LAST_MONTH(code = "1M", description = "Last Month"),
    LAST_3_WEEKS(code = "3W", description = "Last 3 Weeks"),
    LAST_2_WEEKS(code = "2W", description = "Last 2 Weeks"),
    LAST_WEEK(code = "1W", description = "Last Week"),
    LAST_DAY(code = "1D", description = "Last Day"),
}

data class MoreMatchesUiState(
    val isLoading: Boolean = false,
    val matches: List<MatchDetails> = emptyList(),
    val searchFieldValue: String = "",
    val hero: Hero? = null,
    val role: HeroRole? = null,
    val timeFrame: TimeFrame = TimeFrame.ALL,
    val error: String = "",
)

@HiltViewModel
class MoreMatchesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val omedaCityMatchRepository: MatchRepository
) : ViewModel() {

    val playerId: String = checkNotNull(savedStateHandle["playerId"])

    private val _uiState = MutableStateFlow(MoreMatchesUiState())
    val uiState = _uiState

    lateinit var matchesPagingSource: MatchesPagingSource

    val matchesPager = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE)
    ) {
        MatchesPagingSource(
            getMatchesById = { page, perPage ->
                omedaCityMatchRepository.fetchMatchesById(
                    playerId = playerId,
                    heroId = uiState.value.hero?.heroId,
                    role = uiState.value.role?.roleName,
                    timeFrame = uiState.value.timeFrame.code,
                    playerName = uiState.value.searchFieldValue,
                    page = page,
                    perPage = perPage
                ).getOrThrow()
            }
        ).also {
            matchesPagingSource = it
        }
    }.flow

    fun onSearchFieldUpdated(searchFieldValue: String) {
        _uiState.value = uiState.value.copy(searchFieldValue = searchFieldValue)
        matchesPagingSource.invalidate()
    }

    fun onHeroFilterUpdated(hero: Hero) {
        _uiState.value = uiState.value.copy(hero = hero)
        matchesPagingSource.invalidate()
    }

    fun onClearHeroFilter() {
        _uiState.value = uiState.value.copy(hero = null)
        matchesPagingSource.invalidate()
    }

    fun onRoleFilterUpdated(role: HeroRole) {
        _uiState.value = uiState.value.copy(role = role)
        matchesPagingSource.invalidate()
    }

    fun onClearRoleFilter() {
        _uiState.value = uiState.value.copy(role = null)
        matchesPagingSource.invalidate()
    }

    fun onTimeFrameFilterUpdated(timeFrame: TimeFrame) {
        _uiState.value = uiState.value.copy(timeFrame = timeFrame)
        matchesPagingSource.invalidate()
    }

}