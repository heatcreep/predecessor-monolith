package com.aowen.monolith.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.home.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.home.winrate.navigation.navigateToHeroWinPickRate
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.network.firebase.Feedback
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val homeUiState by homeScreenViewModel.uiState.collectAsState()

    Feedback()

    HomeScreen(
        uiState = homeUiState,
        navigateToSearch = navController::navigateToSearch,
        navigateToPlayerDetails = navController::navigateToPlayerDetails,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToHeroWinPickRate = navController::navigateToHeroWinPickRate,
        handlePullRefresh = homeScreenViewModel::initViewModel,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    navigateToSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Int, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    handlePullRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = handlePullRefresh
    )

    RefreshableContainer(
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0,0,0,0),
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0,0,0,0),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ClaimedPlayerSection(
                    uiState = uiState,
                    navigateToPlayerDetails = navigateToPlayerDetails
                )
                HeroWinRateSection(
                    isLoading = uiState.isLoading,
                    heroStatsList = uiState.topFiveHeroesByWinRate,
                    navigateToHeroDetails = navigateToHeroDetails,
                    navigateToHeroWinRate = navigateToHeroWinPickRate
                )
                HeroPickRateSection(
                    isLoading = uiState.isLoading,
                    heroStatsList = uiState.topFiveHeroesByPickRate,
                    navigateToHeroDetails = navigateToHeroDetails,
                    navigateToHeroPickRate = navigateToHeroWinPickRate
                )
            }
        }
    }
}

@Composable
fun ClaimedPlayerCard(
    playerDetails: PlayerDetails,
    playerStats: PlayerStats,
    navigateToPlayerDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val heroImage = Hero.entries.firstOrNull {
        it.heroName == playerStats.favoriteHero
    } ?: Hero.UNKNOWN

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToPlayerDetails(playerDetails.playerId)
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Favorite Hero
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = heroImage.drawableId),
                contentDescription = null
            )
            Column {
                Text(
                    text = playerDetails.playerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row {
                    Text(
                        text = playerDetails.rankTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " | ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "MMR: ${playerDetails.mmr ?: "Unranked"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " | ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    playerDetails.region?.let {
                        Text(
                            text = playerDetails.region,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun ClaimedPlayerCardPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)

        ) {
            ClaimedPlayerCard(
                playerDetails = PlayerDetails(
                    playerName = "heatcreep.tv",
                    region = "naeast",
                    mmr = "1379",
                    rankTitle = "Silver III"
                ),
                navigateToPlayerDetails = {},
                playerStats = PlayerStats(
                    favoriteHero = "Narbash",
                )
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
                    claimedPlayerDetails = PlayerDetails(
                        playerName = "heatcreep.tv",
                        region = "naeast",
                        mmr = "1379",
                        rankTitle = "Silver III"
                    ),
                    claimedPlayerStats = PlayerStats(
                        favoriteHero = "Narbash",
                    )
                ),
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
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
                    claimedPlayerDetails = PlayerDetails(
                        playerName = "heatcreep.tv",
                        region = "naeast",
                        mmr = "1379",
                        rankTitle = "Silver III"
                    ),
                    claimedPlayerStats = PlayerStats(
                        favoriteHero = "Narbash",
                    )
                ),
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                handlePullRefresh = {}
            )
        }
    }
}