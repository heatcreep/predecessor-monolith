package com.aowen.predcompanion.feature.home.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.cards.HeroUiInfo
import com.aowen.predcompanion.core.ui.cards.HomeScreenHeroesCard
import com.aowen.predcompanion.ui.components.MonolithAlertDialog

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    navigateToSearch: () -> Unit,
    navigateToHeroDetails: (Long, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
) {
    val homeUiState by homeScreenViewModel.uiState.collectAsState()

    HomeScreen(
        uiState = homeUiState,
        navigateToSearch = navigateToSearch,
        navigateToHeroDetails = navigateToHeroDetails,
        navigateToHeroWinPickRate = navigateToHeroWinPickRate,
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
    navigateToHeroDetails: (Long, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    handlePullRefresh: () -> Unit,
    handleRemoveAllFavoriteBuilds: () -> Unit = {},
) {

    val isRefreshing by remember { mutableStateOf(false) }


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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

@PreviewLightDark
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
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                handlePullRefresh = {},
            )
        }
    }
}

