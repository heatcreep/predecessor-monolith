package com.aowen.monolith.feature.matches.morematches

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.feature.matches.navigation.navigateToMatchDetails
import com.aowen.monolith.ui.common.MonolithCollapsableListColumn
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.feature.builds.FilterDropdown
import com.aowen.monolith.feature.matches.MatchPlayerCard
import com.aowen.monolith.feature.search.SearchBar
import com.aowen.monolith.ui.utils.handleTimeSinceMatch
import kotlinx.coroutines.launch

@Composable
fun MoreMatchesRoute(
    navController: NavController,
    viewModel: MoreMatchesViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val matches = viewModel.matchesPager.collectAsLazyPagingItems()

    MoreMatchesScreen(
        uiState = uiState,
        matches = matches,
        playerId = viewModel.playerId,
        onSearchFieldUpdate = viewModel::onSearchFieldUpdated,
        onSelectRoleFilter = viewModel::onRoleFilterUpdated,
        onSelectHeroFilter = viewModel::onHeroFilterUpdated,
        onSelectTimeFrameFilter = viewModel::onTimeFrameFilterUpdated,
        onClearRoleFilter = viewModel::onClearRoleFilter,
        onClearHeroFilter = viewModel::onClearHeroFilter,
        onMatchClicked = navController::navigateToMatchDetails
    )
}

@Composable
fun MoreMatchesScreen(
    uiState: MoreMatchesUiState,
    matches: LazyPagingItems<MatchDetails>,
    playerId: String,
    onSearchFieldUpdate: (String) -> Unit,
    onSelectRoleFilter: (HeroRole) -> Unit,
    onSelectTimeFrameFilter: (TimeFrame) -> Unit,
    onClearRoleFilter: () -> Unit,
    onSelectHeroFilter: (Hero) -> Unit,
    onClearHeroFilter: () -> Unit,
    onMatchClicked: (String, String) -> Unit
) {

    val scrollState = rememberLazyListState()

    var expanded by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        MonolithCollapsableListColumn(
            modifier = Modifier.padding(vertical = 16.dp),
            listState = scrollState
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SearchBar(
                    searchLabel = "Search by player name",
                    searchValue = uiState.searchFieldValue,
                    setSearchValue = { searchValue ->
                        onSearchFieldUpdate(searchValue)
                    },
                    modifier = Modifier.weight(1f),
                    handleClearSearch = {
                        onSearchFieldUpdate("")
                    }
                )
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(rotationAngle.value),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Box(
                    modifier = Modifier.zIndex(5f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Filters:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            FilterDropdown(
                                dropdownTitle = "Role",
                                filterOptions = HeroRole.entries.map { role -> role.roleName.replaceFirstChar { it.uppercase() } },
                                selectedFilter = uiState.role?.roleName?.replaceFirstChar { it.uppercase() },
                                onSelectOption = {
                                    onSelectRoleFilter(HeroRole.valueOf(it))
                                },
                                onClearOption = onClearRoleFilter
                            )
                            FilterDropdown(
                                dropdownTitle = "Hero",
                                filterOptions = Hero.entries.map { hero -> hero.heroName },
                                selectedFilter = uiState.hero?.heroName,
                                onSelectOption = {
                                    onSelectHeroFilter(Hero.valueOf(it.uppercase()))
                                },
                                onClearOption = onClearHeroFilter
                            )
                        }
                        Row {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterDropdown(
                                    filterOptions = TimeFrame.entries.map { it.description },
                                    selectedFilter = uiState.timeFrame.description,
                                    onSelectOption = {
                                        onSelectTimeFrameFilter(
                                            TimeFrame.entries.first {
                                                    timeFrame -> timeFrame.description == it
                                            }
                                        )
                                    },
                                ) {

                                }
                            }
                        }
                        ElevatedButton(
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            onClick = {
                                expanded = false
                            }
                        ) {
                            Text(text = "Close")
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }

            }
        }
        when (matches.loadState.refresh) {
            is LoadState.Loading -> {
                FullScreenLoadingIndicator()
            }

            is LoadState.Error -> {
                FullScreenErrorWithRetry(
                    errorMessage = "Failed to load builds"
                ) {
                    matches.retry()
                }
            }

            else -> {
                LazyColumn(
                    state = scrollState,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(matches.itemCount) { index ->
                        val matchListItem = matches[index]
                        matchListItem?.let { match ->
                            val allPlayers = match.dusk.players + match.dawn.players
                            val playerHero = allPlayers.firstOrNull { it.playerId == playerId }
                            val playerTeam =
                                if (match.dusk.players.contains(playerHero)) "Dusk" else "Dawn"
                            val isWinner = playerTeam == match.winningTeam
                            MatchPlayerCard(
                                modifier = Modifier.clickable {
                                    onMatchClicked(playerId, match.matchId)
                                },
                                isWinner = isWinner,
                                timeSinceMatch = handleTimeSinceMatch(match.endTime),
                                playerHero = playerHero
                            )
                        }
                    }
                    item {
                        when (matches.loadState.append) {
                            is LoadState.Loading -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(48.dp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        strokeWidth = 8.dp
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}