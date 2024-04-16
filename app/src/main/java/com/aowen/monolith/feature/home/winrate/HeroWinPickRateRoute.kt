package com.aowen.monolith.feature.home.winrate

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.home.HomeScreenUiState
import com.aowen.monolith.feature.home.HomeScreenViewModel
import com.aowen.monolith.feature.home.TimeFrame
import com.aowen.monolith.ui.components.HeroInlineStatsRateBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

const val WIN_RATE = "winRate"
const val PICK_RATE = "pickRate"

@Composable
fun HeroWinPickRateRoute(
    navController: NavController,
    viewModel: HomeScreenViewModel,
    selectedStat: String
) {
    val uiState by viewModel.uiState.collectAsState()
    HeroWinPickRateScreen(
        uiState = uiState,
        selectedStatFromNav = selectedStat,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        updateHeroStatsByTime = viewModel::updateHeroStatsByTime,
        navigateBack = navController::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroWinPickRateScreen(
    uiState: HomeScreenUiState,
    selectedStatFromNav: String,
    navigateToHeroDetails: (heroId: Int, heroName: String) -> Unit,
    updateHeroStatsByTime: (TimeFrame) -> Unit,
    navigateBack: () -> Unit
) {

    var selectedTimeFrame by rememberSaveable { mutableStateOf(TimeFrame.ONE_MONTH) }
    var selectedStat by rememberSaveable { mutableStateOf(selectedStatFromNav) }
    var listDescending by rememberSaveable { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0,0,0,0),
                title = {
                    Text(
                        text = "Top Heroes by Win Rate",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) {
        val heroStatsState = if (listDescending) {
            uiState.heroStats.sortedByDescending { stat -> if (selectedStat == WIN_RATE) stat.winRate else stat.pickRate }
        } else {
            uiState.heroStats.sortedBy { stat -> if (selectedStat == WIN_RATE) stat.winRate else stat.pickRate }
        }
        Crossfade(targetState = heroStatsState, label = "AnimatedWinStatsList") { animatedStats ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeFrame.entries.forEach { timeFrame ->
                            TextButton(onClick = {
                                selectedTimeFrame = timeFrame
                                updateHeroStatsByTime(timeFrame)
                            }) {
                                Text(
                                    text = timeFrame.value,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (timeFrame == selectedTimeFrame) FontWeight.ExtraBold else FontWeight.Normal,
                                    color = if (timeFrame == selectedTimeFrame) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {}
                        Row(
                            modifier = Modifier.weight(1.5f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Row(
                                    modifier = Modifier.clickable {
                                        if (selectedStat != WIN_RATE) {
                                            selectedStat = WIN_RATE
                                            listDescending = true
                                        } else {
                                            listDescending = !listDescending
                                        }
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Win Rate",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    if (selectedStat == WIN_RATE) {
                                        Icon(
                                            imageVector = if (listDescending) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                            contentDescription = "list in ${if (listDescending) "descending" else "ascending"} order",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Row(
                                    modifier = Modifier.clickable {
                                        if (selectedStat != PICK_RATE) {
                                            selectedStat = PICK_RATE
                                            listDescending = true
                                        } else {
                                            listDescending = !listDescending
                                        }
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pick Rate",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    if (selectedStat == PICK_RATE) {
                                        Icon(
                                            imageVector = if (listDescending) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                            contentDescription = "list in ${if (listDescending) "descending" else "ascending"} order",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                items(animatedStats) { heroStats ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigateToHeroDetails(heroStats.heroId, heroStats.name)
                            }
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(4.dp)
                            ),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,

                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Player Favorite Hero
                                Image(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        ),
                                    contentScale = ContentScale.Crop,
                                    painter = painterResource(id = getHeroImage(heroStats.heroId)),
                                    contentDescription = null
                                )
                                Text(
                                    text = heroStats.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                            }
                            Row(
                                modifier = Modifier.weight(1.5f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                HeroInlineStatsRateBar(
                                    modifier = Modifier.weight(1f),
                                    rate = heroStats.winRate
                                )
                                HeroInlineStatsRateBar(
                                    modifier = Modifier.weight(1f),
                                    rate = heroStats.pickRate
                                )
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
fun HeroWinRateScreenPreview() {
    MonolithTheme {
        HeroWinPickRateScreen(
            uiState = HomeScreenUiState(
                heroStats = listOf(
                    HeroStatistics(
                        1,
                        "Countess",
                        56.45f,
                        18.56f
                    ),
                    HeroStatistics(
                        23,
                        "Iggy & Scorch",
                        12.45f,
                        72.56f
                    )
                )
            ),
            navigateToHeroDetails = { _, _ -> },
            updateHeroStatsByTime = {},
            navigateBack = {},
            selectedStatFromNav = WIN_RATE
        )
    }
}