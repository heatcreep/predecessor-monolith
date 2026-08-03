package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.cards.playerprofile.HeroStatsTabState
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.ui.model.PlayerProfileUiModel
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import kotlinx.coroutines.flow.flowOf


@Composable
internal fun PlayerDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayerDetailsViewModel,
    navigateBack: () -> Unit,
    navigateToMatchDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val matches = viewModel.matchHistory.collectAsLazyPagingItems()

    PlayerDetailScreen(
        playerDetailsState = uiState,
        matches = matches,
        handleRetry = viewModel::handleRetry,
        modifier = modifier,
        navigateToMatchDetails = navigateToMatchDetails,
        navigateToHeroDetails = navigateToHeroDetails,
        navigateToItemDetails = navigateToItemDetails,
        navigateBack = navigateBack,
    )
}

@Composable
fun PlayerDetailScreen(
    playerDetailsState: PlayerDetailsState,
    matches: LazyPagingItems<MatchListItemUiModel>,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    navigateToMatchDetails: (String) -> Unit = { },
    navigateToHeroDetails: (String) -> Unit = { },
    navigateToItemDetails: (String) -> Unit = { },
    navigateBack: () -> Unit = {},
) {

    val isRefreshing by remember { mutableStateOf(false) }

    when (playerDetailsState) {
        is PlayerDetailsState.Loading -> FullScreenLoadingIndicator("Player Details")
        is PlayerDetailsState.Error -> FullScreenErrorWithRetry(
            errorMessage = playerDetailsState.errorMessage
        ) {
            handleRetry()
        }

        is PlayerDetailsState.Loaded -> {
            val uiState = playerDetailsState.playerProfileUiModel.profileCardUiModel
            Scaffold(
                topBar = {
                    MonolithTopAppBar(
                        title = "Player Details",
                        titleStyle = MaterialTheme.typography.bodyLarge,
                        backAction = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "navigate up"
                                )
                            }
                        },
                        actions = {
                        }
                    )
                },
            ) { paddingValues ->
                Surface(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PullToRefreshBox(
                        modifier = Modifier.fillMaxSize(),
                        isRefreshing = isRefreshing,
                        onRefresh = handleRetry
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp)
                        ) {
                            var selectedTab by rememberSaveable { mutableIntStateOf(0) }

                            PlayerProfileLayout(
                                profileCard = uiState,
                                matches = matches,
                                heroStatsTabState = HeroStatsTabState.Loaded(emptyList()),
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it },
                                navigateToMatchDetails = navigateToMatchDetails,
                                navigateToHeroDetails = navigateToHeroDetails,
                                navigateToItemDetails = navigateToItemDetails,
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PlayerDetailsScreenPreview() {
    val fakePagingData = flowOf(PagingData.empty<MatchListItemUiModel>())
    val fakeMatches = fakePagingData.collectAsLazyPagingItems()
    MonolithTheme {
        PlayerDetailScreen(
            playerDetailsState = PlayerDetailsState.Loaded(
                playerProfileUiModel = PlayerProfileUiModel(
                    profileCardUiModel = PlayerProfileCardUiModel(
                        rankIconUrl = "",
                        playerName = "",
                        rankPoints = "",
                        rankTitle = "",
                        winPercentage = "",
                        region = "",
                        favoriteHeroIconUrl = "",
                    ),
                    heroStatisticsList = emptyList()
                )
            ),
            matches = fakeMatches
        )
    }
}