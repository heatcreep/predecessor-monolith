package com.aowen.monolith.ui.screens.matches

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.data.Team
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.theme.Dawn
import com.aowen.monolith.ui.theme.Dusk
import com.aowen.monolith.ui.theme.MonolithTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: MatchDetailsViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf("Overview", "Match Stats")
    val pageCount = tabs.size

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = 0,
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Match Details")
        } else {
            if (uiState.matchDetailsErrors != null) {
                FullScreenErrorWithRetry(
                    errorMessage = uiState.matchDetailsErrors!!.errorMessage,
                ) {
                    viewModel.initViewModel()
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(
                                    tabPositions[pagerState.currentPage]
                                ),
                                color = MaterialTheme.colorScheme.secondary
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
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        when (page) {
                            0 -> MatchDetailsTab(
                                uiState = uiState,
                                modifier = modifier,
                                getCreepScorePerMinute = viewModel::getCreepScorePerMinute,
                                getGoldEarnedPerMinute = viewModel::getGoldEarnedPerMinute,
                                onItemClicked = viewModel::onItemClicked
                            )

                            1 -> MatchStatsTab(
                                selectedTeam = uiState.selectedTeam,
                                onSelectedTeamChanged = viewModel::onTeamSelected,
                                modifier = modifier,
                                getCreepScorePerMinute = viewModel::getCreepScorePerMinute,
                                getGoldEarnedPerMinute = viewModel::getGoldEarnedPerMinute
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsTab(
    uiState: MatchDetailsUiState,
    modifier: Modifier = Modifier,
    getCreepScorePerMinute: (Int) -> String,
    getGoldEarnedPerMinute: (Int) -> String,
    onItemClicked: (ItemDetails) -> Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    fun openItemDetailsBottomSheet(itemDetails: ItemDetails) {
        onItemClicked(itemDetails)
        openBottomSheet = true
    }

    if (openBottomSheet && uiState.selectedItemDetails != null) {
        ItemDetailsBottomSheet(
            closeBottomSheet = closeBottomSheet,
            sheetState = bottomSheetState,
            itemDetails = uiState.selectedItemDetails
        )
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            ScoreboardPanel(
                teamName = "Dusk",
                isWinningTeam = uiState.match.winningTeam == "Dusk",
                teamDetails = uiState.match.dusk.players,
                openItemDetails = ::openItemDetailsBottomSheet,
                getCreepScorePerMinute = getCreepScorePerMinute,
                getGoldEarnedPerMinute = getGoldEarnedPerMinute
            )
            ScoreboardPanel(
                teamName = "Dawn",
                isWinningTeam = uiState.match.winningTeam == "Dawn",
                teamDetails = uiState.match.dawn.players,
                openItemDetails = ::openItemDetailsBottomSheet,
                getCreepScorePerMinute = getCreepScorePerMinute,
                getGoldEarnedPerMinute = getGoldEarnedPerMinute
            )
        }
    }
}

@Composable
fun MatchStatsTab(
    modifier: Modifier = Modifier,
    selectedTeam: Team = Team.Dawn(emptyList()),
    onSelectedTeamChanged: (Boolean) -> Unit = {},
    getCreepScorePerMinute: (Int) -> String = { _ -> "0.0" },
    getGoldEarnedPerMinute: (Int) -> String = { _ -> "0.0" }
) {

    val backgroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (selectedTeam is Team.Dusk) Dusk else Dawn,
        label = "MatchStatsTabBackground"
    )

    val duskTextBackGroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (selectedTeam is Team.Dusk) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "MatchStatsTabTeamBackground"
    )

    val dawnTextBackGroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (selectedTeam is Team.Dawn) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "MatchStatsTabTeamBackground"
    )

    val duskTextColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (selectedTeam is Team.Dusk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "MatchStatsTabTeamBackground"
    )

    val dawnTextColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (selectedTeam is Team.Dawn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "MatchStatsTabTeamBackground"
    )

    val colorStops = arrayOf(
        0.0f to MaterialTheme.colorScheme.background,
        0.9f to backgroundColor
    )



    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = colorStops, startY = 0f, endY = 500f
                    )
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = dawnTextBackGroundColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dawn",
                    style = MaterialTheme.typography.bodyMedium,
                    color = dawnTextColor,
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
            Switch(
                checked = selectedTeam is Team.Dusk,
                thumbContent = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_monolith),
                        tint = backgroundColor,
                        contentDescription = null
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = MaterialTheme.colorScheme.secondary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedIconColor = MaterialTheme.colorScheme.tertiary
                ),
                onCheckedChange = {
                    onSelectedTeamChanged(it)
                }

            )
            Spacer(modifier = Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = duskTextBackGroundColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dusk",
                    style = MaterialTheme.typography.bodyMedium,
                    color = duskTextColor,
                )
            }
        }
        Crossfade(
            targetState = selectedTeam, label = "MatchStats",
            modifier = Modifier.fillMaxWidth()
        ) { team ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
                team.players.forEach { player ->
                    MatchStatsPlayerImageRow(
                        playerHeroId = player.heroId,
                    )
                }

            }
        }
        Crossfade(
            targetState = selectedTeam, label = "MatchStats",
            modifier = Modifier.fillMaxWidth()
        ) { team ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                MatchStatsRow(
                    label = "Kills",
                    stats = team.players.map { it.kills.toString() })
                MatchStatsRow(
                    label = "Deaths",
                    stats = team.players.map { it.deaths.toString() })
                MatchStatsRow(
                    label = "Assists",
                    stats = team.players.map { it.assists.toString() })
                MatchStatsRow(
                    label = "Minions Killed",
                    stats = team.players.map { it.minionsKilled.toString() })
                MatchStatsRow(
                    label = "Lane Minions",
                    stats = team.players.map { it.laneMinionsKilled.toString() })
                MatchStatsRow(
                    label = "Jungle Minions (Enemy)",
                    stats = team.players.map { it.neutralMinionsEnemyJungle.toString() })
                MatchStatsRow(
                    label = "Jungle Minions (Team)",
                    stats = team.players.map { it.neutralMinionsTeamJungle.toString() })
                MatchStatsRow(
                    label = "CS / min",
                    stats = team.players.map { getCreepScorePerMinute(it.minionsKilled) })
                MatchStatsRow(
                    label = "Total Gold Earned",
                    stats = team.players.map { it.goldEarned.toString() }
                )
                MatchStatsRow(
                    label = "Gold / min",
                    stats = team.players.map { getGoldEarnedPerMinute(it.goldEarned) })
                MatchStatsRow(
                    label = "Total Damage Dealt",
                    stats = team.players.map { it.totalDamageDealt.toString() }
                )
                MatchStatsRow(
                    label = "Physical Damage Dealt",
                    stats = team.players.map { it.physicalDamageDealt.toString() }
                )
                MatchStatsRow(
                    label = "Physical Damage Dealt to Heroes",
                    stats = team.players.map { it.physicalDamageDealtToHeroes.toString() }
                )
                MatchStatsRow(
                    label = "Magic Damage Dealt",
                    stats = team.players.map { it.magicalDamageDealt.toString() }
                )
                MatchStatsRow(
                    label = "Magic Damage Dealt to Heroes",
                    stats = team.players.map { it.magicalDamageDealtToHeroes.toString() }
                )
                MatchStatsRow(
                    label = "True Damage Dealt",
                    stats = team.players.map { it.trueDamageDealt.toString() }
                )
                MatchStatsRow(
                    label = "True Damage Dealt to Heroes",
                    stats = team.players.map { it.trueDamageDealtToHeroes.toString() }
                )
                MatchStatsRow(
                    label = "Damage Done to Structures",
                    stats = team.players.map { it.totalDamageDealtToStructures.toString() }
                )
                MatchStatsRow(
                    label = "Damage Done to Objectives",
                    stats = team.players.map { it.totalDamageDealtToObjectives.toString() }
                )
                MatchStatsRow(
                    label = "Total Damage Taken",
                    stats = team.players.map { it.totalDamageTaken.toString() }
                )
                MatchStatsRow(
                    label = "Physical Damage Taken",
                    stats = team.players.map { it.physicalDamageTaken.toString() }
                )
                MatchStatsRow(
                    label = "Magic Damage Taken",
                    stats = team.players.map { it.magicalDamageTaken.toString() }
                )
                MatchStatsRow(
                    label = "True Damage Taken",
                    stats = team.players.map { it.trueDamageTaken.toString() }
                )
                MatchStatsRow(
                    label = "Total Damage Taken from Heroes",
                    stats = team.players.map { it.totalDamageTakenFromHeroes.toString() }
                )
                MatchStatsRow(
                    label = "Physical Damage Taken from Heroes",
                    stats = team.players.map { it.physicalDamageTakenFromHeroes.toString() }
                )
                MatchStatsRow(
                    label = "Magic Damage Taken from Heroes",
                    stats = team.players.map { it.magicalDamageTakenFromHeroes.toString() }
                )
                MatchStatsRow(
                    label = "True Damage Taken from Heroes",
                    stats = team.players.map { it.trueDamageTakenFromHeroes.toString() }
                )
                MatchStatsRow(
                    label = "Total Damage Mitigated",
                    stats = team.players.map { it.totalDamageMitigated.toString() }
                )
                MatchStatsRow(
                    label = "Total Healing Done",
                    stats = team.players.map { it.totalHealingDone.toString() }
                )
                MatchStatsRow(
                    label = "Item Healing Done",
                    stats = team.players.map { it.itemHealingDone.toString() }
                )
                MatchStatsRow(
                    label = "Crest Healing Done",
                    stats = team.players.map { it.crestHealingDone.toString() }
                )
                MatchStatsRow(
                    label = "Utility Healing Done",
                    stats = team.players.map { it.utilityHealingDone.toString() }
                )
                MatchStatsRow(
                    label = "Total Shielding Received",
                    stats = team.players.map { it.totalShieldingReceived.toString() }
                )
                MatchStatsRow(
                    label = "Wards Placed",
                    stats = team.players.map { it.wardsPlaced.toString() }
                )
                MatchStatsRow(
                    label = "Wards Destroyed",
                    stats = team.players.map { it.wardsDestroyed.toString() }
                )
            }

        }
    }


}

@Composable
fun MatchStatsRow(label: String, stats: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        stats.forEach { stat ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stat,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun RowScope.MatchStatsPlayerImageRow(
    playerHeroId: Int,
) {
    Box(
        modifier = Modifier
            .weight(1f)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            painter = painterResource(
                id = getHeroImage(playerHeroId).drawableId
            ),
            contentDescription = null
        )
    }
}

@Composable
fun MatchPlayerStatValue(value: String) {
    Box(
        modifier = Modifier
            .size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MatchDetailsScreenPreview() {
    MonolithTheme {
        MatchDetailsTab(
            uiState = MatchDetailsUiState(
                isLoading = false,
                match = MatchDetails(
                    winningTeam = "Dusk",
                    gameDuration = 2135,
                    dusk = Team.Dusk(
                        players = listOf(
                            MatchPlayerDetails(
                                mmr = "1234.5",
                                mmrChange = "+11.1",
                                playerName = "Player 1",
                                heroId = 14,
                                role = "support",
                                performanceTitle = "Annihilator",
                                performanceScore = "143.6",
                                kills = 7,
                                deaths = 3,
                                assists = 3,
                                minionsKilled = 119,
                                goldEarned = 15434
                            )
                        )
                    ),
                    dawn = Team.Dawn(
                        players = listOf(
                            MatchPlayerDetails(
                                mmr = "1234.5",
                                mmrChange = "-11.1",
                                playerName = "Player 2",
                                heroId = 13,
                                role = "carry",
                                performanceTitle = "Sentinel",
                                performanceScore = "104.6",
                            )
                        )
                    )
                )
            ),
            getCreepScorePerMinute = { "2.8 CS/min" },
            getGoldEarnedPerMinute = { "267.8 Gold/min" },
            onItemClicked = {}
        )

    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable()
fun MatchStatsTabPreview() {
    MonolithTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MatchStatsTab(
                selectedTeam = Team.Dawn(
                    players = listOf(
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "+11.1",
                            playerName = "Player 1",
                            heroId = 14,
                            role = "support",
                            performanceTitle = "Annihilator",
                            performanceScore = "143.6",
                            kills = 7,
                            deaths = 3,
                            assists = 3,
                            minionsKilled = 119,
                            goldEarned = 15434
                        ),
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "-11.1",
                            playerName = "Player 2",
                            kills = 4,
                            deaths = 13,
                            assists = 7,
                            heroId = 13,
                            minionsKilled = 320,
                            role = "carry",
                            performanceTitle = "Sentinel",
                            performanceScore = "104.6",
                        ),
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "-11.1",
                            playerName = "Player 2",
                            heroId = 31,
                            kills = 2,
                            deaths = 3,
                            assists = 17,
                            role = "carry",
                            minionsKilled = 123,
                            performanceTitle = "Sentinel",
                            performanceScore = "104.6",
                        ),
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "-11.1",
                            playerName = "Player 2",
                            heroId = 12,
                            kills = 2,
                            deaths = 3,
                            assists = 17,
                            role = "carry",
                            minionsKilled = 123,
                            performanceTitle = "Sentinel",
                            performanceScore = "104.6",
                        ),
                        MatchPlayerDetails(
                            mmr = "1234.5",
                            mmrChange = "-11.1",
                            playerName = "Player 2",
                            heroId = 7,
                            kills = 2,
                            deaths = 3,
                            assists = 17,
                            role = "carry",
                            minionsKilled = 123,
                            performanceTitle = "Sentinel",
                            performanceScore = "104.6",
                        )
                    )
                ),
                getCreepScorePerMinute = { "30" },
                getGoldEarnedPerMinute = { "267.8" }
            )
        }
    }
}