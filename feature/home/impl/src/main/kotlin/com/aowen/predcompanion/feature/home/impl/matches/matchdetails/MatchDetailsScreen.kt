package com.aowen.predcompanion.feature.home.impl.matches.matchdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aowen.predcompanion.feature.home.impl.matches.ScoreboardPanel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchPlayerCardUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchTeamUiModel
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.theme.Dawn
import com.aowen.predcompanion.ui.theme.Dusk
import com.aowen.predcompanion.ui.theme.WarmWhite
import kotlinx.coroutines.launch
import com.aowen.predcompanion.core.resources.R as coreResources

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchDetailsRoute(
    modifier: Modifier = Modifier,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit,
    viewModel: MatchDetailsViewModel
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
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is MatchDetailsUiState.Loading -> {
                FullScreenLoadingIndicator("Match Details")
            }

            is MatchDetailsUiState.Error -> {
                FullScreenErrorWithRetry(
                    errorMessage = state.matchDetailsErrors.errorMessage,
                ) {
                    viewModel.initViewModel()
                }
            }

            is MatchDetailsUiState.Success -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TabRow(
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
                                match = state.match,
                                onItemClicked = navigateToItemDetails,
                                navigateToPlayerDetails = navigateToPlayerDetails
                            )

                            1 -> MatchStatsTab(
                                selectedTeam = state.selectedTeam,
                                onSelectedTeamChanged = viewModel::onTeamSelected,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MatchDetailsTab(
    match: MatchDetailsUiModel,
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit,
    navigateToPlayerDetails: (String) -> Unit = {}
) {
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
                isWinningTeam = match.winningTeam == "Dusk",
                matchPlayerCards = match.duskTeam.players,
                openItemDetails = onItemClicked,
                navigateToPlayerDetails = navigateToPlayerDetails,
            )
            ScoreboardPanel(
                teamName = "Dawn",
                isWinningTeam = match.winningTeam == "Dawn",
                matchPlayerCards = match.dawnTeam.players,
                openItemDetails = onItemClicked,
                navigateToPlayerDetails = navigateToPlayerDetails,
            )
        }
    }
}

@Composable
fun MatchStatsTab(
    modifier: Modifier = Modifier,
    selectedTeam: MatchTeamUiModel,
    onSelectedTeamChanged: (Boolean) -> Unit = {},
) {

    val isDusk = selectedTeam is MatchTeamUiModel.Dusk

    val backgroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (isDusk) Dusk else Dawn,
        label = "MatchStatsTabBackground"
    )

    val duskTextBackGroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (isDusk) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "MatchStatsTabTeamBackground"
    )

    val dawnTextBackGroundColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (!isDusk) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "MatchStatsTabTeamBackground"
    )

    val duskTextColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (isDusk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "MatchStatsTabTeamBackground"
    )

    val dawnTextColor by animateColorAsState(
        animationSpec = tween(1000),
        targetValue = if (!isDusk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
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
                checked = isDusk,
                thumbContent = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = coreResources.drawable.ic_monolith),
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
                        player = player
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
                    stats = team.players.map { it.kills })
                MatchStatsRow(
                    label = "Deaths",
                    stats = team.players.map { it.deaths })
                MatchStatsRow(
                    label = "Assists",
                    stats = team.players.map { it.assists })
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
                    stats = team.players.map { it.creepScorePerMinute })
                MatchStatsRow(
                    label = "Total Gold Earned",
                    stats = team.players.map { it.goldEarned }
                )
                MatchStatsRow(
                    label = "Gold / min",
                    stats = team.players.map { it.goldEarnedPerMinute })
                MatchStatsRow(
                    label = "Total Damage Dealt",
                    stats = team.players.map { it.totalDamageDealt.toString() }
                )
                MatchStatsRow(
                    label = "Total Damage Dealt to Heroes",
                    stats = team.players.map { it.totalDamageDealtToHeroes.toString() }
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
    player: MatchPlayerCardUiModel
) {
    Box(
        modifier = Modifier
            .weight(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            model = player.heroImageUrl,
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(start = 4.dp, top = 20.dp, end = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = player.playerName,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.bodySmall,
                color = WarmWhite
            )
        }
    }
}
