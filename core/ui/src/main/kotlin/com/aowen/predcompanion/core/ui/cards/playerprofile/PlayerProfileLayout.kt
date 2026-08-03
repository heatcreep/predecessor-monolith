package com.aowen.predcompanion.core.ui.cards.playerprofile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.aowen.predcompanion.core.ui.components.KDAText
import com.aowen.predcompanion.core.ui.components.MatchPlayerCard
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.core.ui.model.HeroStatisticsUiModel
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.resources.R as coreResources

sealed interface HeroStatsTabState {
    data object Loading : HeroStatsTabState
    data class Loaded(val stats: List<HeroStatisticsUiModel>) : HeroStatsTabState
    data class Error(val message: String) : HeroStatsTabState
}

@Composable
fun PlayerProfileLayout(
    profileCard: PlayerProfileCardUiModel,
    matches: LazyPagingItems<MatchListItemUiModel>,
    heroStatsTabState: HeroStatsTabState,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navigateToMatchDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabLabels = listOf(
        coreResources.string.core_resources_player_profile_tab_matches,
        coreResources.string.core_resources_player_profile_tab_heroes,
        coreResources.string.core_resources_player_profile_tab_friends_enemies,
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlayerProfileCard(
            playerProfileCardUiModel = profileCard,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(tabLabels) { index, labelRes ->
                PredCompanionChipFilter(
                    text = stringResource(labelRes),
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) }
                )
            }
        }
        when (selectedTab) {
            0 -> MatchesTab(
                matches = matches,
                navigateToMatchDetails = navigateToMatchDetails,
                navigateToHeroDetails = navigateToHeroDetails,
                navigateToItemDetails = navigateToItemDetails,
            )
            1 -> HeroStatsTab(heroStatsTabState)
            2 -> Text(
                text = stringResource(id = coreResources.string.core_resources_player_profile_coming_soon),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun MatchesTab(
    matches: LazyPagingItems<MatchListItemUiModel>,
    navigateToMatchDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
) {
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(matches.itemCount) {
            matches[it]?.let { matchItem ->
                MatchPlayerCard(
                    modifier = Modifier.clickable {
                        navigateToMatchDetails(matchItem.matchId)
                    },
                    navigateToHeroDetails = navigateToHeroDetails,
                    navigateToItemDetails = navigateToItemDetails,
                    matchListItem = matchItem,
                )
            }
        }
    }
}

@Composable
private fun HeroStatsTab(heroStatsTabState: HeroStatsTabState) {
    when (heroStatsTabState) {
        is HeroStatsTabState.Loading -> {
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

        is HeroStatsTabState.Error -> {
            Text(
                text = heroStatsTabState.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        is HeroStatsTabState.Loaded -> {
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(
                    heroStatsTabState.stats,
                    key = { _, heroStat -> heroStat.id }
                ) { index, heroStat ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = heroStat.name,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Box(modifier = Modifier.weight(0.5f)) {
                                Text(
                                    text = heroStat.totalMatches,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Box(modifier = Modifier.weight(0.5f)) {
                                Text(
                                    text = heroStat.winRate,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                KDAText(
                                    averageKda = listOf(
                                        heroStat.averageKills,
                                        heroStat.averageDeaths,
                                        heroStat.averageAssists
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        if (index != heroStatsTabState.stats.count() - 1) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
