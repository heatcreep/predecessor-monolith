package com.aowen.monolith.feature.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.home.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.home.winrate.navigation.navigateToHeroWinPickRate
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.network.FavoriteBuildsSharedState
import com.aowen.monolith.network.firebase.Feedback
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val homeUiState by homeScreenViewModel.uiState.collectAsState()
    val favoriteBuildsState by homeScreenViewModel.favoriteBuildsState.collectAsState()

    Feedback()

    HomeScreen(
        uiState = homeUiState,
        favoriteBuildsSharedState = favoriteBuildsState,
        navigateToSearch = navController::navigateToSearch,
        navigateToPlayerDetails = navController::navigateToPlayerDetails,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToHeroWinPickRate = navController::navigateToHeroWinPickRate,
        navigateToBuildDetails = navController::navigateToBuildDetails,
        handlePullRefresh = homeScreenViewModel::initViewModel,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    favoriteBuildsSharedState: FavoriteBuildsSharedState,
    navigateToSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Int, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    navigateToBuildDetails: (Int) -> Unit,
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
            Column(
                modifier = modifier
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
                FavoriteBuildsSection(
                    uiState = uiState,
                    favoriteBuildsSharedState = favoriteBuildsSharedState,
                    navigateToBuildDetails = navigateToBuildDetails
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

@Composable
fun FavoriteBuildsSection(
    uiState: HomeScreenUiState,
    favoriteBuildsSharedState: FavoriteBuildsSharedState,
    navigateToBuildDetails: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Favorite Builds",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
        AnimatedContent(targetState = uiState.isLoading, label = "") {
            if (it) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlayerLoadingCard(
                        avatarSize = 64.dp,
                        titleHeight = 16.dp,
                        subtitleHeight = 12.dp,
                        titleWidth = 100.dp,
                        subtitleWidth = 200.dp,
                    )
                }
            } else {
                val error = uiState.homeScreenError.firstOrNull { errorType -> errorType is HomeScreenError.FavoriteBuildsErrorMessage }
                val errorMessage = error?.errorMessage
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else if (
                    favoriteBuildsSharedState.favoriteBuilds.isEmpty()
                ) {
                    Text(
                        text = "No favorite builds found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        favoriteBuildsSharedState.favoriteBuilds.forEach { build ->
                            FavoriteBuildListItem(
                                build = build,
                                navigateToBuildDetails = navigateToBuildDetails
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteBuildListItem(
    modifier: Modifier = Modifier,
    build: FavoriteBuildListItem,
    navigateToBuildDetails: (Int) -> Unit
) {

    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(4.dp)
            )
            .clickable {
                navigateToBuildDetails(build.buildId)

            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,

            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayerIcon(
                    heroImageId = getHeroRole(build.heroId).drawableId,
                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .align(Alignment.BottomEnd),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                        painter = painterResource(
                            id = getHeroRole(build.role.lowercase()).drawableId
                        ),
                        contentDescription = null
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = build.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Author: ${build.author}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        build.gameVersion.let { version ->
                            Badge(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = version,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Row {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = getItemImage(build.crestId)),
                            contentDescription = null
                        )
                        build.itemIds.forEach {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = getItemImage(it)),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${build.upvotesCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    IconButton(onClick = {
                        // TODO: Replace with actual upvote logic
                        Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show()
                    }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "thumbs up",
                            tint = GreenHighlight
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${build.downvotesCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    IconButton(onClick = {
                        // TODO: Replace with actual downvote logic
                        Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.ThumbDown,
                            contentDescription = "thumbs down",
                            tint = RedHighlight
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
                favoriteBuildsSharedState = FavoriteBuildsSharedState(emptyList()),
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                navigateToBuildDetails = {},
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
                favoriteBuildsSharedState = FavoriteBuildsSharedState(
                    emptyList()
                ),
                navigateToSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                navigateToBuildDetails = {},
                handlePullRefresh = {}
            )
        }
    }
}