package com.aowen.monolith.feature.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.R
import com.aowen.monolith.core.ui.cards.HeroUiInfo
import com.aowen.monolith.core.ui.cards.HomeScreenHeroesCard
import com.aowen.monolith.core.ui.cards.builds.BuildListCard
import com.aowen.monolith.core.ui.cards.claimedplayer.ClaimedPlayerCard
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.RankDetails
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.home.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.home.winrate.PICK_RATE
import com.aowen.monolith.feature.home.winrate.WIN_RATE
import com.aowen.monolith.feature.home.winrate.navigation.navigateToHeroWinPickRate
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.network.ClaimedPlayer
import com.aowen.monolith.network.ClaimedPlayerState
import com.aowen.monolith.network.FavoriteBuildsState
import com.aowen.monolith.network.firebase.Feedback
import com.aowen.monolith.ui.components.MonolithAlertDialog
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val homeUiState by homeScreenViewModel.uiState.collectAsState()
    val favoriteBuildsState by homeScreenViewModel.favoriteBuildsState.collectAsState()
    val claimedPlayerState by homeScreenViewModel.claimedPlayerState.collectAsState()
    val claimedPlayerName by homeScreenViewModel.claimedPlayerName.collectAsState()

    Feedback()

    HomeScreen(
        uiState = homeUiState,
        favoriteBuildsSharedState = favoriteBuildsState,
        claimedPlayerState = claimedPlayerState,
        claimedPlayerName = claimedPlayerName,
        navigateToSearch = navController::navigateToSearch,
        navigateToPlayerDetails = navController::navigateToPlayerDetails,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToHeroWinPickRate = navController::navigateToHeroWinPickRate,
        navigateToBuildDetails = navController::navigateToBuildDetails,
        handlePullRefresh = homeScreenViewModel::initViewModel,
        handleRemoveAllFavoriteBuilds = homeScreenViewModel::handleRemoveAllFavoriteBuilds,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenUiState,
    favoriteBuildsSharedState: FavoriteBuildsState,
    claimedPlayerState: ClaimedPlayerState,
    claimedPlayerName: String?,
    navigateToSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Long, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    navigateToBuildDetails: (Int) -> Unit,
    handlePullRefresh: () -> Unit,
    handleRemoveAllFavoriteBuilds: () -> Unit = {},
) {

    val isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = handlePullRefresh
    )

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var clearAllFavoriteBuildsDialogIsOpen by remember {
        mutableStateOf(false)
    }

    // Dialog confirming user wants to clear all recent searches
    if (clearAllFavoriteBuildsDialogIsOpen) {
        MonolithAlertDialog(
            bodyText = "Are you sure you want to clear all favorite builds? This action cannot be undone.",
            onDismissRequest = { clearAllFavoriteBuildsDialogIsOpen = false },
            onConfirm = {
                handleRemoveAllFavoriteBuilds()
                clearAllFavoriteBuildsDialogIsOpen = false
            }
        )
    }

    RefreshableContainer(
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    actions = {
                        IconButton(onClick = navigateToSearch) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    )
                )
            }
        ) {
            if (openBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = closeBottomSheet,
                    sheetState = bottomSheetState
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.claim_player_on_console),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = """
                        Because of privacy issues, Omeda.city is not able to expose the PSN and Xbox usernames of players. To claim your player:
                        
                        - In game, go to replays and find a recent match
                        - Copy the match id
                        - In the app, search for the match by id
                        - Find your player in the match details
                        - Tap on the player and then tap 'Claim Player'
                        
                        After you've claimed your player, you can locally change the display name on the player details page.
                    """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ClaimedPlayerSection(
                            uiState = uiState,
                            claimedPlayerState = claimedPlayerState,
                            claimedPlayerName = claimedPlayerName,
                            navigateToPlayerDetails = navigateToPlayerDetails,
                            onOpenBottomSheet = {
                                openBottomSheet = true
                            }
                        )
                    }
                    item {
                        FavoriteBuildsSection(
                            modifier = Modifier.animateItem(),
                            uiState = uiState,
                            favoriteBuildsSharedState = favoriteBuildsSharedState,
                            navigateToBuildDetails = navigateToBuildDetails,
                            handleOpenDialog = {
                                clearAllFavoriteBuildsDialogIsOpen = true
                            },
                        )
                    }
                    item {
                        HomeScreenHeroesCard(
                            modifier = Modifier.animateItem(),
                            cardTitle = stringResource(R.string.top_heroes),
                            heroUiInfo = uiState.topFiveHeroesByWinRate.map {
                                HeroUiInfo(
                                    heroName = it.heroName,
                                    heroPathName = it.name,
                                    heroImageId = it.heroId.toInt(),
                                    winRate = it.winRate
                                )
                            },
                            onHeroClick = navigateToHeroDetails,
                            onTitleActionClick = { navigateToHeroWinPickRate(WIN_RATE) }
                        )
                    }
                    item {
                        HomeScreenHeroesCard(
                            modifier = Modifier.animateItem(),
                            cardTitle = stringResource(R.string.most_played_heroes),
                            heroUiInfo = uiState.topFiveHeroesByPickRate.map {
                                HeroUiInfo(
                                    heroName = it.heroName,
                                    heroPathName = it.name,
                                    heroImageId = it.heroId.toInt(),
                                    winRate = it.winRate
                                )
                            },
                            onHeroClick = navigateToHeroDetails,
                            onTitleActionClick = { navigateToHeroWinPickRate(PICK_RATE) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteBuildsSection(
    modifier: Modifier = Modifier,
    uiState: HomeScreenUiState,
    favoriteBuildsSharedState: FavoriteBuildsState,
    handleOpenDialog: () -> Unit = {},
    navigateToBuildDetails: (Int) -> Unit
) {

    var isReloadingSection by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favorite Builds",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            if (favoriteBuildsSharedState is FavoriteBuildsState.Success) {
                TextButton(onClick = handleOpenDialog) {
                    Text(
                        text = "Clear All",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
        AnimatedContent(targetState = uiState.isLoading || isReloadingSection, label = "") {
            if (it) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlayerLoadingCard(
                        avatarSize = 64.dp,
                        titleHeight = 16.dp,
                        subtitleHeight = 12.dp,
                        titleWidth = 100.dp,
                        subtitleWidth = 200.dp,
                    )
                }
            } else {
                val error =
                    uiState.homeScreenError.firstOrNull { errorType -> errorType is HomeScreenError.FavoriteBuildsErrorMessage }
                val errorMessage = error?.errorMessage
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    when (favoriteBuildsSharedState) {
                        is FavoriteBuildsState.Error -> {
                            Text(
                                text = "Error loading favorite builds",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        is FavoriteBuildsState.Empty -> {
                            Text(
                                text = "No favorite builds found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        is FavoriteBuildsState.Success -> {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                favoriteBuildsSharedState.favoriteBuilds.forEach { build ->
                                    BuildListCard(
                                        build = build,
                                        navigateToBuildDetails = navigateToBuildDetails
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun ClaimedPlayerCardPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)

        ) {
            ClaimedPlayerCard(
                playerName = "heatcreep.tv",
                playerDetails = PlayerDetails(
                    playerName = "heatcreep.tv",
                    region = "naeast",
                    mmr = "1379",
                ),
                navigateToPlayerDetails = {},
                playerStats = PlayerStats(
                    favoriteHero = "Narbash",
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenPreview() {
    MonolithTheme {
        Surface {
            HomeScreen(
                uiState = HomeScreenUiState(
                    isLoading = false
                ),
                claimedPlayerName = "heatcreep.tv",
                favoriteBuildsSharedState = FavoriteBuildsState.Success(emptyList()),
                claimedPlayerState = ClaimedPlayerState.NoClaimedPlayer,
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                navigateToBuildDetails = {},
                handlePullRefresh = {},
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenRecentSearchPreview() {
    MonolithTheme {
        Surface {
            HomeScreen(
                uiState = HomeScreenUiState(
                    isLoading = false,
                ),
                favoriteBuildsSharedState = FavoriteBuildsState.Success(
                    emptyList()
                ),
                claimedPlayerState = ClaimedPlayerState.Claimed(
                    claimedPlayer = ClaimedPlayer(
                        playerDetails = PlayerDetails(
                            playerName = "heatcreep.tv",
                            region = "naeast",
                            rank = 31,
                            rankDetails = RankDetails.PARAGON,
                            mmr = "1379",
                        ),
                        playerStats = PlayerStats(
                            favoriteHero = "Narbash",
                        )
                    )
                ),
                claimedPlayerName = "heatcreep.tv",
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                navigateToBuildDetails = {},
                handlePullRefresh = {}
            )
        }
    }
}