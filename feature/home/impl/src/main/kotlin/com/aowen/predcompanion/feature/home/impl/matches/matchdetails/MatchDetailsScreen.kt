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
import coil3.compose.AsyncImage
import com.aowen.predcompanion.feature.home.impl.matches.ScoreboardPanel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
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
                isWinningTeam = match.winningTeam == "DUSK",
                matchPlayerCards = match.duskTeam.players,
                openItemDetails = onItemClicked,
                navigateToPlayerDetails = navigateToPlayerDetails,
            )
            ScoreboardPanel(
                teamName = "Dawn",
                isWinningTeam = match.winningTeam == "DAWN",
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
                        heroImageSrc = player.heroAndItemDetails.heroImageSrc,
                        playerName = player.heroAndItemDetails.playerName,
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
                    stats = team.players.map { it.stats.kills })
                MatchStatsRow(
                    label = "Deaths",
                    stats = team.players.map { it.stats.deaths })
                MatchStatsRow(
                    label = "Assists",
                    stats = team.players.map { it.stats.assists })
                MatchStatsRow(
                    label = "Minions Killed",
                    stats = team.players.map { it.stats.minionsKilled })
                MatchStatsRow(
                    label = "Lane Minions",
                    stats = team.players.map { it.stats.laneMinionsKilled })
                MatchStatsRow(
                    label = "Jungle Minions (Enemy)",
                    stats = team.players.map { it.stats.neutralMinionsEnemyJungle })
                MatchStatsRow(
                    label = "Jungle Minions (Team)",
                    stats = team.players.map { it.stats.neutralMinionsTeamJungle })
                MatchStatsRow(
                    label = "CS / min",
                    stats = team.players.map { it.stats.minionsKilledPerMin })
                MatchStatsRow(
                    label = "Total Gold Earned",
                    stats = team.players.map { it.stats.goldEarned })
                MatchStatsRow(
                    label = "Gold / min",
                    stats = team.players.map { it.stats.goldEarnedPerMin })
                MatchStatsRow(
                    label = "Total Damage Dealt",
                    stats = team.players.map { it.stats.totalDamageDealt }
                )
                MatchStatsRow(
                    label = "Total Damage Dealt to Heroes",
                    stats = team.players.map { it.stats.totalDamageDealtToHeroes }
                )
                MatchStatsRow(
                    label = "Physical Damage Dealt",
                    stats = team.players.map { it.stats.physicalDamageDealt }
                )
                MatchStatsRow(
                    label = "Physical Damage Dealt to Heroes",
                    stats = team.players.map { it.stats.physicalDamageDealtToHeroes }
                )
                MatchStatsRow(
                    label = "Magic Damage Dealt",
                    stats = team.players.map { it.stats.magicalDamageDealt }
                )
                MatchStatsRow(
                    label = "Magic Damage Dealt to Heroes",
                    stats = team.players.map { it.stats.magicalDamageDealtToHeroes }
                )
                MatchStatsRow(
                    label = "True Damage Dealt",
                    stats = team.players.map { it.stats.trueDamageDealt }
                )
                MatchStatsRow(
                    label = "True Damage Dealt to Heroes",
                    stats = team.players.map { it.stats.trueDamageDealtToHeroes }
                )
                MatchStatsRow(
                    label = "Damage Done to Structures",
                    stats = team.players.map { it.stats.totalDamageDealtToStructures }
                )
                MatchStatsRow(
                    label = "Damage Done to Objectives",
                    stats = team.players.map { it.stats.totalDamageDealtToObjectives }
                )
                MatchStatsRow(
                    label = "Total Damage Taken",
                    stats = team.players.map { it.stats.totalDamageTaken }
                )
                MatchStatsRow(
                    label = "Physical Damage Taken",
                    stats = team.players.map { it.stats.physicalDamageTaken }
                )
                MatchStatsRow(
                    label = "Magic Damage Taken",
                    stats = team.players.map { it.stats.magicalDamageTaken }
                )
                MatchStatsRow(
                    label = "True Damage Taken",
                    stats = team.players.map { it.stats.trueDamageTaken }
                )
                MatchStatsRow(
                    label = "Total Damage Taken from Heroes",
                    stats = team.players.map { it.stats.totalDamageTakenFromHeroes }
                )
                MatchStatsRow(
                    label = "Physical Damage Taken from Heroes",
                    stats = team.players.map { it.stats.physicalDamageTakenFromHeroes }
                )
                MatchStatsRow(
                    label = "Magic Damage Taken from Heroes",
                    stats = team.players.map { it.stats.magicalDamageTakenFromHeroes }
                )
                MatchStatsRow(
                    label = "True Damage Taken from Heroes",
                    stats = team.players.map { it.stats.trueDamageTakenFromHeroes }
                )
                MatchStatsRow(
                    label = "Total Damage Mitigated",
                    stats = team.players.map { it.stats.totalDamageMitigated }
                )
                MatchStatsRow(
                    label = "Total Healing Done",
                    stats = team.players.map { it.stats.totalHealingDone }
                )
                MatchStatsRow(
                    label = "Item Healing Done",
                    stats = team.players.map { it.stats.itemHealingDone }
                )
                MatchStatsRow(
                    label = "Crest Healing Done",
                    stats = team.players.map { it.stats.crestHealingDone }
                )
                MatchStatsRow(
                    label = "Utility Healing Done",
                    stats = team.players.map { it.stats.utilityHealingDone }
                )
                MatchStatsRow(
                    label = "Total Shielding Received",
                    stats = team.players.map { it.stats.totalShieldingReceived }
                )
                MatchStatsRow(
                    label = "Wards Placed",
                    stats = team.players.map { it.stats.wardsPlaced }
                )
                MatchStatsRow(
                    label = "Wards Destroyed",
                    stats = team.players.map { it.stats.wardsDestroyed }
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
    heroImageSrc: String,
    playerName: String,
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
            model = heroImageSrc,
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
                text = playerName,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.bodySmall,
                color = WarmWhite
            )
        }
    }
}
