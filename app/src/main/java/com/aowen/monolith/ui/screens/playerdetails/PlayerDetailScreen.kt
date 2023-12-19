package com.aowen.monolith.ui.screens.playerdetails

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.logDebug
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.HeroSelectDropdown
import com.aowen.monolith.ui.components.PlayerCard
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.theme.MonolithTheme
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun PlayerDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayerDetailsViewModel = hiltViewModel(),
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()

    PlayerDetailScreen(
        uiState = uiState,
        handleRetry = viewModel::initViewModel,
        modifier = modifier,
        timeSinceMatch = viewModel::handleTimeSinceMatch,
        handleSavePlayer = viewModel::handleSavePlayer,
        handlePlayerHeroStatsSelect = viewModel::handlePlayerHeroStatsSelect,
        navigateToMatchDetails = navigateToMatchDetails
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PlayerDetailScreen(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    timeSinceMatch: (String) -> String = { _ -> "" },
    handleSavePlayer: suspend (Boolean) -> Unit = {},
    handlePlayerHeroStatsSelect: (Int) -> Unit = { },
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> }
) {

    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf("Player Stats", "Hero Stats")
    val pageCount = tabs.size

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = 0,
    )

    val isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = handleRetry
    )

    RefreshableContainer(isRefreshing = isRefreshing, pullRefreshState = pullRefreshState) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {

            if (uiState.playerErrors != null) {
                val errorMessage = uiState.playerErrors.playerInfoErrorMessage
                    ?: uiState.playerErrors.matchesErrorMessage
                    ?: uiState.playerErrors.statsErrorMessage
                    ?: uiState.playerErrors.heroesErrorMessage

                    ?: "Something went wrong."
                val errorLog = uiState.playerErrors.playerInfoError
                    ?: uiState.playerErrors.matchesError
                    ?: uiState.playerErrors.statsError
                    ?: uiState.playerErrors.heroesError
                    ?: uiState.playerErrors.heroStatsError
                    ?: "No error log available."
                logDebug(errorLog)
                FullScreenErrorWithRetry(
                    errorMessage = errorMessage
                ) {
                    handleRetry()
                }
            } else {
                Column(modifier = Modifier.fillMaxHeight()) {
                    if (uiState.isLoading) {
                        FullScreenLoadingIndicator("Player Details")
                    } else {
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
                                    timeSinceMatch = timeSinceMatch,
                                    navigateToMatchDetails = navigateToMatchDetails
                                )

                                1 -> PlayerHeroStatsTab(
                                    uiState = uiState,
                                    handlePlayerHeroStatsSelect = handlePlayerHeroStatsSelect
                                )
                            }
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
    timeSinceMatch: (String) -> String = { _ -> "" },
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> }
) {

    Column(
        modifier = modifier
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
                timeSinceMatch = timeSinceMatch,
                navigateToMatchDetails = navigateToMatchDetails
            )

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerHeroStatsTab(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handlePlayerHeroStatsSelect: (Int) -> Unit = { }
) {
    var selectedHero by remember {
        mutableStateOf(
            uiState.heroes.find { it.displayName == uiState.stats.favoriteHero }
                ?: uiState.heroes[0]
        )
    }

    LaunchedEffect(selectedHero) {
        handlePlayerHeroStatsSelect(selectedHero.id)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeroSelectDropdown(
            selectedHero = selectedHero,
            onSelect = { selectedHero = it },
            heroes = uiState.heroes
        )
        Spacer(modifier = Modifier.size(32.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            // Matches Played
            HeroStatCard(
                statLabel = "Matches Played:",
                statValue = uiState.selectedHeroStats?.matchCount.toString()
            )
            HeroStatCard(
                statLabel = "Win Rate:",
                statValue = "${uiState.selectedHeroStats?.winRate.toString()}%"
            )
            HeroStatCard(
                statLabel = "Average CS/min:",
                statValue = uiState.selectedHeroStats?.csMin.toString()
            )
            HeroStatCard(
                statLabel = "Average Gold/min:",
                statValue = uiState.selectedHeroStats?.goldMin.toString()
            )
            LongHeroStatCard(
                statTitle = "Performance Score:",
                statValue = uiState.selectedHeroStats?.avgPerformanceScore.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.totalPerformanceScore.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.avgPerformanceScore.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxPerformanceScore.toString()
                    )
                )
            )
            LongHeroStatCard(
                statTitle = "Damage dealt to heroes:",
                statValue = uiState.selectedHeroStats?.avgDamageDealtToHeroes.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.totalDamageDealtToHeroes.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.avgDamageDealtToHeroes.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxDamageDealtToHeroes.toString()
                    )
                )
            )
            LongHeroStatCard(
                statTitle = "Damage dealt to structures:",
                statValue = uiState.selectedHeroStats?.avgDamageDealtToStructures.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.totalDamageDealtToStructures.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.maxDamageDealtToStructures.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxDamageDealtToStructures.toString()
                    )
                )
            )
            LongHeroStatCard(
                statTitle = "Damage dealt to objectives:",
                statValue = uiState.selectedHeroStats?.avgDamageDealtToObjectives.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.totalDamageDealtToObjectives.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.avgDamageDealtToObjectives.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxDamageDealtToObjectives.toString()
                    )
                )
            )
            LongHeroStatCard(
                statTitle = "Healing done:",
                statValue = uiState.selectedHeroStats?.avgHealingDone.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.totalHealingDone.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.avgHealingDone.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxHealingDone.toString()
                    )
                )
            )
            LongHeroStatCard(
                statTitle = "Wards placed:",
                statValue = uiState.selectedHeroStats?.avgWardsPlaced.toString(),
                subStats = listOf(
                    StatPair(
                        "Total:",
                        uiState.selectedHeroStats?.wardsPlaced.toString()
                    ),
                    StatPair(
                        "Average:",
                        uiState.selectedHeroStats?.avgWardsPlaced.toString()
                    ),
                    StatPair(
                        "Highest:",
                        uiState.selectedHeroStats?.maxWardsPlaced.toString()
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.HeroStatCard(
    statLabel: String,
    statValue: String,
) {
    ElevatedCard(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = statValue,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

data class StatPair(
    val statLabel: String,
    val statValue: String,
)

@Composable
fun LongHeroStatCard(
    statTitle: String,
    statValue: String,
    subStats: List<StatPair>
) {
    var isExpanded by remember { mutableStateOf(false) }
    var horizontalBias by remember { mutableFloatStateOf(-1f) }

    val floatAnimator = animateFloatAsState(targetValue = 1f, label = "statTitle")

    LaunchedEffect(isExpanded) {
        horizontalBias = if (isExpanded) 0f else -1f
    }
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    isExpanded = !isExpanded
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(floatAnimator.value),
                    textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start,
                    text = statTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.size(24.dp))
                AnimatedVisibility(visible = !isExpanded) {
                    Text(
                        text = "$statValue (avg)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null
                )

            }
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.size(16.dp))
                    subStats.forEach { stat ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stat.statLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = stat.statValue,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }


        }
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
                    playerName = "heatcreep.tv",
                    rankTitle = "Platinum I"
                ),
                stats = PlayerStats(
                    favoriteHero = "Narbash"
                ),
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

@Preview(
    name = "Dark Mode",
    group = "Default",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PlayerHeroStatsPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PlayerHeroStatsTab(
                uiState = PlayerDetailsUiState(
                    isLoading = false,
                    player = PlayerDetails(
                        playerName = "heatcreep.tv"
                    ),
                    heroes = listOf(
                        HeroDetails(
                            id = 1,
                            displayName = "Narbash",
                            imageId = R.drawable.narbash
                        ),
                    ),
                    heroStats = listOf(
                        PlayerHeroStats(
                            heroId = 1,
                            displayName = "Narbash",
                        )
                    ),
                    selectedHeroStats = PlayerHeroStats(
                        heroId = 1,
                        displayName = "Narbash",
                        matchCount = 132,
                        winRate = 52.56,
                        csMin = 0.93,
                        goldMin = 299.24,
                        totalDamageDealtToHeroes = 967000,
                        avgDamageDealtToHeroes = 7322,
                        maxDamageDealtToHeroes = 20685
                    )
                )
            )
        }

    }
}