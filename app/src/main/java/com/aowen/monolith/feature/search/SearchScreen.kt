package com.aowen.monolith.feature.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.search.playerdetails.navigation.navigateToPlayerDetails
import com.aowen.monolith.feature.search.winrate.navigation.navigateToHeroWinPickRate
import com.aowen.monolith.network.firebase.Feedback
import com.aowen.monolith.ui.components.MonolithAlertDialog
import com.aowen.monolith.ui.components.RefreshableContainer
import com.aowen.monolith.ui.components.ShimmerCircle
import com.aowen.monolith.ui.components.ShimmerLongText
import com.aowen.monolith.ui.components.ShimmerShortText
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.inputFieldDefaults

@Composable
internal fun SearchScreenRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    searchScreenViewModel: SearchScreenViewModel
) {
    val searchUiState by searchScreenViewModel.uiState.collectAsState()

    Feedback()


    LaunchedEffect(Unit) {
        searchScreenViewModel.initViewModel()
    }


    SearchScreen(
        uiState = searchUiState,
        setSearchValue = searchScreenViewModel::setSearchValue,
        handleSubmitSearch = searchScreenViewModel::handleSubmitSearch,
        handleClearSearch = searchScreenViewModel::handleClearSearch,
        navigateToPlayerDetails = navController::navigateToPlayerDetails,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToHeroWinPickRate = navController::navigateToHeroWinPickRate,
        topFiveHeroesByWinRate = searchScreenViewModel.getTopFiveHeroesByWinRate(),
        topFiveHeroesByPickRate = searchScreenViewModel.getTopFiveHeroesByPickRate(),
        handlePullRefresh = searchScreenViewModel::initViewModel,
        handleAddToRecentSearch = searchScreenViewModel::handleAddToRecentSearch,
        handleClearSingleSearch = searchScreenViewModel::handleClearSingleSearch,
        handleClearAllRecentSearches = searchScreenViewModel::handleClearAllRecentSearches,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    uiState: SearchScreenUiState,
    setSearchValue: (String) -> Unit,
    handleSubmitSearch: () -> Unit,
    handleClearSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    navigateToHeroDetails: (Int, String) -> Unit,
    navigateToHeroWinPickRate: (String) -> Unit,
    topFiveHeroesByWinRate: List<HeroStatistics>,
    topFiveHeroesByPickRate: List<HeroStatistics>,
    handlePullRefresh: () -> Unit,
    handleAddToRecentSearch: (PlayerDetails) -> Unit,
    handleClearSingleSearch: (String) -> Unit,
    handleClearAllRecentSearches: () -> Unit,
    modifier: Modifier = Modifier
) {

    var alertDialogIsOpen by remember { mutableStateOf(false) }
    val isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = handlePullRefresh
    )

    LaunchedEffect(Unit) {
        if (uiState.searchFieldValue.isNotEmpty()) {
            handleClearSearch()
        }
    }
    if (alertDialogIsOpen) {
        MonolithAlertDialog(
            bodyText = "Are you sure you want to clear all recent searches? This action cannot be undone.",
            onDismissRequest = { alertDialogIsOpen = false },
            onConfirm = {
                handleClearAllRecentSearches()
                alertDialogIsOpen = false
            }
        )
    }
    RefreshableContainer(
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchBar(
                    searchLabel = "Player lookup",
                    searchValue = uiState.searchFieldValue,
                    setSearchValue = setSearchValue,
                    handleSubmitSearch = handleSubmitSearch,
                    handleClearSearch = handleClearSearch,
                    modifier = Modifier.fillMaxWidth()
                )
                ClaimedPlayerSection(
                    uiState = uiState,
                    handleAddToRecentSearch = handleAddToRecentSearch,
                    navigateToPlayerDetails = navigateToPlayerDetails
                )
                RecentPlayersSection(
                    uiState = uiState,
                    handleAddToRecentSearch = handleAddToRecentSearch,
                    navigateToPlayerDetails = navigateToPlayerDetails,
                    handleClearSingleSearch = handleClearSingleSearch,
                    handleOpenAlertDialog = { alertDialogIsOpen = true }
                )
                HeroWinRateSection(
                    heroStatsList = topFiveHeroesByWinRate,
                    navigateToHeroDetails = navigateToHeroDetails,
                    navigateToHeroWinRate = navigateToHeroWinPickRate
                )
                HeroPickRateSection(
                    heroStatsList = topFiveHeroesByPickRate,
                    navigateToHeroDetails = navigateToHeroDetails,
                    navigateToHeroPickRate = navigateToHeroWinPickRate
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    searchValue: String,
    setSearchValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchLabel: String = "",
    handleSubmitSearch: (() -> Unit)? = null,
    handleClearSearch: (() -> Unit)? = null
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min),
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text(
                text = searchLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                handleClearSearch?.let {
                    if (searchValue.isNotEmpty()) {
                        IconButton(onClick = {
                            handleClearSearch()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                    }
                }
                handleSubmitSearch?.let {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        handleSubmitSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                handleSubmitSearch?.let {
                    handleSubmitSearch()
                }
            }
        ),
        value = searchValue,
        colors = inputFieldDefaults(),
        singleLine = true,
        maxLines = 1,
        onValueChange = setSearchValue
    )
}

@Composable
fun PlayerResultCard(
    playerDetails: PlayerDetails,
    modifier: Modifier = Modifier,
    navigateToPlayerDetails: (String) -> Unit,
    handleClearSingleSearch: (() -> Unit)? = null
) {

    val context = LocalContext.current

    val model = ImageRequest.Builder(context)
        .data(playerDetails.rankImage)
        .crossfade(true)
        .build()


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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player Rank
                SubcomposeAsyncImage(
                    model = model,
                    contentDescription = playerDetails.rankTitle
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Success) {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    }
                }
                Column {
                    Text(
                        text = playerDetails.playerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    playerDetails.region?.let {
                        Text(
                            text = playerDetails.region,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            handleClearSingleSearch?.let {
                IconButton(onClick = {
                    handleClearSingleSearch()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = null
                    )
                }
            }
        }
    }

}

@Composable
fun PlayerLoadingCard(
    modifier: Modifier = Modifier,
    avatarSize: Dp = 32.dp,
    titleHeight: Dp = 12.dp,
    titleWidth: Dp = 100.dp,
    subtitleHeight: Dp = 8.dp,
    subtitleWidth: Dp = 200.dp,
    clipRadius: Dp = 4.dp

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
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
            // Player Rank
            ShimmerCircle(
                size = avatarSize
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerShortText(
                    height = titleHeight,
                    width = titleWidth,
                    clipRadius = clipRadius
                )
                ShimmerLongText(
                    height = subtitleHeight,
                    width = subtitleWidth,
                    clipRadius = clipRadius
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
                    .size(64.dp)
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

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
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
            SearchScreen(
                uiState = SearchScreenUiState(
                    playersList = listOf(
                        PlayerDetails(
                            playerName = "heatcreep.tv",
                            region = "naeast"
                        )
                    ),
                    searchFieldValue = "heatcreep.tv",
                    initPlayersListText = null,
                    recentSearchesList = listOf(
                        PlayerDetails(
                            playerName = "heatcreep.tv",
                            region = "naeast",
                            mmr = "1379",
                            rankTitle = "Silver III"
                        )
                    ),
                    isLoadingSearch = false,
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
                setSearchValue = {},
                handleSubmitSearch = {},
                handleClearSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                handleAddToRecentSearch = {},
                handleClearSingleSearch = {},
                handlePullRefresh = {},
                handleClearAllRecentSearches = {},
                topFiveHeroesByWinRate = emptyList(),
                topFiveHeroesByPickRate = emptyList()
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
            SearchScreen(
                uiState = SearchScreenUiState(
                    playersList = emptyList(),
                    searchFieldValue = "heatcreep.tv",
                    initPlayersListText = null,
                    recentSearchesList = listOf(
                        PlayerDetails(
                            playerName = "heatcreep.tv",
                            region = "naeast",
                            mmr = "1379",
                            rankTitle = "Silver III"
                        )
                    ),
                    isLoadingSearch = false,
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
                setSearchValue = {},
                handleSubmitSearch = {},
                handleClearSearch = {},
                navigateToPlayerDetails = {},
                navigateToHeroDetails = { _, _ -> },
                navigateToHeroWinPickRate = {},
                handleAddToRecentSearch = {},
                handleClearSingleSearch = {},
                handleClearAllRecentSearches = {},
                handlePullRefresh = {},
                topFiveHeroesByWinRate = emptyList(),
                topFiveHeroesByPickRate = emptyList()
            )
        }
    }
}