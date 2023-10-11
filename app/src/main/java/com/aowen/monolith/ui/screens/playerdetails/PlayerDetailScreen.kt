package com.aowen.monolith.ui.screens.playerdetails

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.logDebug
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.HeroSelectDropdown
import com.aowen.monolith.ui.components.PlayerCard
import com.aowen.monolith.ui.theme.MonolithTheme
import kotlinx.coroutines.launch


@Composable
internal fun PlayerDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayerDetailsViewModel = hiltViewModel(),
    navigateToMatchDetails: (String) -> Unit = { _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()

    PlayerDetailScreen(
        uiState = uiState,
        handleRetry = viewModel::initViewModel,
        modifier = modifier,
        handleSavePlayer = viewModel::handleSavePlayer,
        navigateToMatchDetails = navigateToMatchDetails
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerDetailScreen(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    handleSavePlayer: suspend (Boolean) -> Unit = {},
    navigateToMatchDetails: (String) -> Unit = { _ -> }
) {

    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf("Player Stats", "Hero Stats")
    val pageCount = tabs.size

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = 0,
    )
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Player Details")
        } else {
            if (uiState.playerErrors != null) {
                val errorMessage = uiState.playerErrors.playerInfoErrorMessage
                    ?: uiState.playerErrors.matchesErrorMessage
                    ?: uiState.playerErrors.statsErrorMessage
                    ?: "Something went wrong."
                val errorLog = uiState.playerErrors.playerInfoError
                    ?: uiState.playerErrors.matchesError
                    ?: uiState.playerErrors.statsError
                    ?: "No error log available."
                logDebug(errorLog)
                FullScreenErrorWithRetry(
                    errorMessage = errorMessage
                ) {
                    handleRetry()
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(
                                    tabPositions[pagerState.currentPage]
                                )
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                text = { Text(text = tab) },
                                unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                                selectedContentColor = MaterialTheme.colorScheme.secondary,
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> PlayerStatsTab(
                                uiState = uiState,
                                handleSavePlayer = handleSavePlayer,
                                navigateToMatchDetails = navigateToMatchDetails
                            )

                            1 -> PlayerHeroStatsTab(uiState = uiState)
                        }
                    }

                }
            }
        }


    }
}

@Composable
fun PlayerStatsTab(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handleSavePlayer: suspend (Boolean) -> Unit = {},
    navigateToMatchDetails: (String) -> Unit = { _ -> }
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        uiState.player.let { playerDetails ->
            PlayerCard(
                player = playerDetails,
                isClaimed = uiState.isClaimed,
                handleSavePlayer = handleSavePlayer,
                stats = uiState.stats
            )
            Spacer(modifier = Modifier.size(32.dp))
            MatchesList(
                playerId = uiState.playerId,
                matches = uiState.matches,
                navigateToMatchDetails = navigateToMatchDetails
            )

        }
    }
}

@Composable
fun PlayerHeroStatsTab(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        HeroSelectDropdown(heroes = uiState.heroes)
    }
}

@Preview(
    name = "Dark Mode",
    group = "Default",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreview() {
    MonolithTheme {
        PlayerDetailScreen(
            uiState = PlayerDetailsUiState(
                isLoading = false,
                player = PlayerDetails(
                    playerName = "heatcreep.tv"
                )
            )
        )
    }
}

@Preview(
    name = "Light Mode",
    group = "Default",
    showBackground = true
)
@Composable
fun HomeScreenPreview2() {
    MonolithTheme {
        PlayerDetailScreen(
            uiState = PlayerDetailsUiState(
                isLoading = false,
                player = PlayerDetails(
                    playerName = "heatcreep.tv"
                )
            )
        )
    }
}