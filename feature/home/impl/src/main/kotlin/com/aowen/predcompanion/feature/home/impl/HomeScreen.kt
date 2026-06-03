package com.aowen.predcompanion.feature.home.impl

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.cards.HeroUiInfo
import com.aowen.predcompanion.core.ui.cards.HomeScreenHeroesCard
import com.aowen.predcompanion.core.ui.cards.builds.BuildListCard
import com.aowen.predcompanion.core.ui.model.mapper.ClaimedPlayerCardUiModel
import com.aowen.predcompanion.ui.components.MonolithAlertDialog
import com.aowen.predcompanion.ui.components.PlayerLoadingCard

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    navigateToSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Long, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    navigateToBuildDetails: (Int) -> Unit
) {
    val homeUiState by homeScreenViewModel.uiState.collectAsState()

    HomeScreen(
        uiState = homeUiState,
        navigateToSearch = navigateToSearch,
        navigateToPlayerDetails = navigateToPlayerDetails,
        navigateToHeroDetails = navigateToHeroDetails,
        navigateToHeroWinPickRate = navigateToHeroWinPickRate,
        navigateToBuildDetails = navigateToBuildDetails,
        handlePullRefresh = homeScreenViewModel::initViewModel,
        handleRemoveAllFavoriteBuilds = homeScreenViewModel::handleRemoveAllFavoriteBuilds,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenUiState,
    navigateToSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Long, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    navigateToBuildDetails: (Int) -> Unit,
    handlePullRefresh: () -> Unit,
    handleRemoveAllFavoriteBuilds: () -> Unit = {},
) {

    val isRefreshing by remember { mutableStateOf(false) }

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

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = handlePullRefresh,
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
                            text = stringResource(R.string.feature_home_impl_claim_player_on_console),
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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ClaimedPlayerSection(
                        claimedPlayerUiState = uiState.claimedPlayerUiState,
                        navigateToPlayerDetails = navigateToPlayerDetails,
                        onOpenBottomSheet = {
                            openBottomSheet = true
                        }
                    )
                }
                item {
                    FavoriteBuildsSection(
                        modifier = Modifier.animateItem(),
                        isLoading = uiState.isLoading,
                        favoriteBuildsUiState = uiState.favoriteBuildsUiState,
                        navigateToBuildDetails = navigateToBuildDetails,
                        handleOpenDialog = {
                            clearAllFavoriteBuildsDialogIsOpen = true
                        },
                    )
                }
                item {
                    HomeScreenHeroesCard(
                        modifier = Modifier.animateItem(),
                        cardTitle = stringResource(R.string.feature_home_impl_top_heroes),
                        heroUiInfo = uiState.topFiveHeroesByWinRate.map { heroStats ->
                            HeroUiInfo(
                                heroName = heroStats.heroName,
                                heroPathName = heroStats.name,
                                heroImageSrc = heroStats.heroImageSrc ?: "",
                                heroImageId = heroStats.heroId.toInt(),
                                winRate = heroStats.winRate
                            )
                        },
                        onHeroClick = navigateToHeroDetails,
                        onTitleActionClick = { navigateToHeroWinPickRate(_root_ide_package_.com.aowen.predcompanion.feature.home.impl.winrate.WIN_RATE) }
                    )
                }
                item {
                    HomeScreenHeroesCard(
                        modifier = Modifier.animateItem(),
                        cardTitle = stringResource(R.string.feature_home_impl_most_played_heroes),
                        heroUiInfo = uiState.topFiveHeroesByPickRate.map { heroStats ->
                            HeroUiInfo(
                                heroName = heroStats.heroName,
                                heroPathName = heroStats.name,
                                heroImageSrc = heroStats.heroImageSrc ?: "",
                                heroImageId = heroStats.heroId.toInt(),
                                winRate = heroStats.winRate
                            )
                        },
                        onHeroClick = navigateToHeroDetails,
                        onTitleActionClick = { navigateToHeroWinPickRate(_root_ide_package_.com.aowen.predcompanion.feature.home.impl.winrate.PICK_RATE) }
                    )
                }
            }

        }
    }
}

@Composable
fun FavoriteBuildsSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    favoriteBuildsUiState: FavoriteBuildsUiState,
    handleOpenDialog: () -> Unit = {},
    navigateToBuildDetails: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            if (favoriteBuildsUiState is FavoriteBuildsUiState.Success) {
                TextButton(onClick = handleOpenDialog) {
                    Text(
                        text = "Clear All",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
        AnimatedContent(targetState = isLoading, label = "") {
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
                when (favoriteBuildsUiState) {
                    is FavoriteBuildsUiState.Error -> {
                        Text(
                            text = favoriteBuildsUiState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    is FavoriteBuildsUiState.Empty -> {
                        Text(
                            text = "No favorite builds found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    is FavoriteBuildsUiState.Success -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            favoriteBuildsUiState.favoriteBuilds.forEach { build ->
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

@PreviewLightDark
@Composable
fun ClaimedPlayerSectionPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)
        ) {
            ClaimedPlayerSection(
                claimedPlayerUiState = ClaimedPlayerUiState.Claimed(
                    playerName = "heatcreep.tv",
                    player = ClaimedPlayerCardUiModel(
                        playerId = "1",
                        heroImageUrl = null,
                        winRate = "55%",
                        rankText = "Gold (+100)",
                        rankColor = Color.Yellow,
                        rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.gold_200
                    )
                ),
                navigateToPlayerDetails = {},
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
                    isLoading = false,
                    favoriteBuildsUiState = FavoriteBuildsUiState.Success(emptyList()),
                    claimedPlayerUiState = ClaimedPlayerUiState.NoClaimed,
                ),
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
                    favoriteBuildsUiState = FavoriteBuildsUiState.Success(
                        emptyList()
                    ),
                    claimedPlayerUiState = ClaimedPlayerUiState.Claimed(
                        playerName = "heatcreep.tv",
                        player = ClaimedPlayerCardUiModel(
                            playerId = "1",
                            heroImageUrl = null,
                            winRate = "55%",
                            rankText = "Paragon (+100)",
                            rankColor = Color.Yellow,
                            rankImageModel = com.aowen.predcompanion.core.resources.R.drawable.paragon_200
                        )
                    ),
                ),
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