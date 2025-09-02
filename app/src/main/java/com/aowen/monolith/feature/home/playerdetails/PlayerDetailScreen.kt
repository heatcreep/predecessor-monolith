package com.aowen.monolith.feature.home.playerdetails

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.core.ui.cards.playerprofile.PlayerProfilePlayerStatsCard
import com.aowen.monolith.core.ui.dropdown.HeroSelectDropdown
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerHeroStats
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.feature.matches.MatchesList
import com.aowen.monolith.feature.matches.morematches.navigation.navigateToMoreMatches
import com.aowen.monolith.feature.matches.navigation.navigateToMatchDetails
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.components.UnclaimPlayerDialog
import com.aowen.monolith.ui.model.StatLine
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.animateHorizontalAlignmentAsState
import kotlinx.coroutines.launch


@Composable
internal fun PlayerDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayerDetailsViewModel = hiltViewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()

    PlayerDetailScreen(
        uiState = uiState,
        handleRetry = viewModel::initViewModel,
        modifier = modifier,
        handleClaimPlayerStatus = viewModel::handleClaimPlayerStatus,
        handleSavePlayerName = viewModel::handleSaveClaimedPlayerName,
        handlePlayerNameChange = viewModel::handlePlayerNameFieldChange,
        handlePlayerHeroStatsSelect = viewModel::handlePlayerHeroStatsSelect,
        navigateToMatchDetails = navController::navigateToMatchDetails,
        navigateToMoreMatches = navController::navigateToMoreMatches,
        navigateBack = navController::navigateUp,
        onEditPlayerName = viewModel::onEditPlayerName
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PlayerDetailScreen(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    handleClaimPlayerStatus: (Boolean) -> Unit = {},
    handleSavePlayerName: () -> Unit = {},
    handlePlayerNameChange: (String) -> Unit = {},
    handlePlayerHeroStatsSelect: (Long) -> Unit = { },
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> },
    navigateToMoreMatches: (String) -> Unit = { },
    navigateBack: () -> Unit = {},
    onEditPlayerName: () -> Unit = { }
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

    var isUnclaimPlayerDialogOpen by remember { mutableStateOf(false) }


    if (isUnclaimPlayerDialogOpen) {
        UnclaimPlayerDialog(
            onDismissRequest = { isUnclaimPlayerDialogOpen = false },
            handleSavePlayer = {
                coroutineScope.launch {
                    handleClaimPlayerStatus(true)
                }
                isUnclaimPlayerDialogOpen = false
            }
        )
    }

    RefreshableContainer(isRefreshing = isRefreshing, pullRefreshState = pullRefreshState) {
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
                        IconButton(onClick = {
                            if(uiState.isClaimed) {
                                isUnclaimPlayerDialogOpen = true
                            } else {
                                handleClaimPlayerStatus(false)
                            }
                        }) {
                            if(uiState.isClaimed) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "unclaim player",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.FavoriteBorder,
                                    contentDescription = "claim player",
                                )
                            }

                        }
                    }
                )
            },
        ) {
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                if (uiState.errorMessage != null) {
                    val errorMessage = uiState.errorMessage
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
                            uiState.player?.let {
                                TabRow(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedTabIndex = pagerState.currentPage,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.SecondaryIndicator(
                                            Modifier.tabIndicatorOffset(
                                                tabPositions[pagerState.currentPage]
                                            ),
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                ) {
                                    tabs.forEachIndexed { index, tab ->
                                        Tab(
                                            text = {
                                                Text(
                                                    text = tab,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            },
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
                            }
                            HorizontalPager(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface),
                                state = pagerState
                            ) { page ->
                                when (page) {
                                    0 -> PlayerStatsTab(
                                        uiState = uiState,
                                        navigateToMatchDetails = navigateToMatchDetails,
                                        handleSavePlayerName = handleSavePlayerName,
                                        handlePlayerNameChange = handlePlayerNameChange,
                                        navigateToMoreMatches = navigateToMoreMatches,
                                        onEditPlayerName = onEditPlayerName
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
}

@Composable
fun PlayerStatsTab(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handlePlayerNameChange: (String) -> Unit = {},
    handleSavePlayerName: () -> Unit = {},
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> },
    navigateToMoreMatches: (String) -> Unit = { },
    onEditPlayerName: () -> Unit = { }
) {


    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            uiState.player?.let { playerDetails ->
                PlayerProfilePlayerStatsCard(
                    playerDetails = playerDetails,
                    stats = listOf(
                        StatLine.SingleStatLine("Win rate", uiState.stats?.winRate ?: "0%"),
                        StatLine.SingleStatLine(
                            "Matches played",
                            uiState.stats?.matchesPlayed ?: "0"
                        ),
                        StatLine.SingleStatLine(
                            "Favorite hero",
                            uiState.stats?.favoriteHero ?: "N/A"
                        ),
                        StatLine.SingleStatLine(
                            "Favorite role",
                            uiState.stats?.favoriteRole ?: "N/A"
                        ),
                        StatLine.MultiStatLine(
                            "Average KDA",
                            uiState.stats?.averageKda ?: listOf("0", "0", "0")
                        ),
                        StatLine.SingleStatLine(
                            "Average PS",
                            uiState.stats?.averagePerformanceScore ?: "0 PS"
                        )
                    ),
                    handleSavePlayerName = handleSavePlayerName,
                    onPlayerNameChange = handlePlayerNameChange,
                    onEditPlayerName = onEditPlayerName,
                    isClaimed = uiState.isClaimed,
                    playerNameField = uiState.playerNameField,
                    claimedPlayerName = uiState.claimedPlayerName,
                    isEditingPlayerName = uiState.isEditingPlayerName

                )
            }
        }
        item {
            MatchesList(
                playerId = uiState.playerId,
                matches = uiState.matches,
                navigateToMatchDetails = navigateToMatchDetails,
                navigateToMoreMatches = navigateToMoreMatches
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerHeroStatsTab(
    uiState: PlayerDetailsUiState,
    modifier: Modifier = Modifier,
    handlePlayerHeroStatsSelect: (Long) -> Unit = { }
) {
    var selectedHero by remember {
        mutableStateOf(
            uiState.allHeroes.firstOrNull {
                it.name == uiState.stats?.favoriteHero
            }
        )
    }

    LaunchedEffect(selectedHero) {
        selectedHero?.let {
            handlePlayerHeroStatsSelect(it.heroId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeroSelectDropdown(
            heroName = selectedHero?.name ?: "Select Hero",
            heroImageId = selectedHero?.imageId,
            onSelect = {
                selectedHero = uiState.allHeroes.firstOrNull { hero ->
                    hero.name == it
                }
            },
            heroes = uiState.allHeroes
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
                        uiState.selectedHeroStats?.avgDamageDealtToStructures.toString()
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

    val alignment by animateHorizontalAlignmentAsState(horizontalBias)


    LaunchedEffect(isExpanded) {
        horizontalBias = if (isExpanded) 0f else -1f
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(floatAnimator.value),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = alignment
                ) {
                    Text(
                        text = statTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
                    allHeroes = listOf(
                        HeroUiModel(
                            heroId = 1,
                            name = "Narbash",
                            imageId = R.drawable.narbash
                        )
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