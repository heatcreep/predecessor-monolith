package com.aowen.monolith.ui.screens.matches

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.data.TeamDetails
import com.aowen.monolith.data.getKda
import com.aowen.monolith.ui.components.KDAText
import com.aowen.monolith.ui.theme.DarkGreenHighlight
import com.aowen.monolith.ui.theme.DarkRedHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight
import kotlin.math.roundToInt

@Composable
fun MatchDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: MatchDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    MatchDetailsScreen(
        uiState = uiState,
        modifier = modifier,
        getCreepScorePerMinute = viewModel::getCreepScorePerMinute,
        getGoldEarnedPerMinute = viewModel::getGoldEarnedPerMinute,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(
    uiState: MatchDetailsUiState,
    modifier: Modifier = Modifier,
    getCreepScorePerMinute: (Int) -> String,
    getGoldEarnedPerMinute: (Int) -> String
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    var selectedItemDetails by rememberSaveable { mutableStateOf<ItemDetails?>(null) }
    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    fun openItemDetailsBottomSheet(itemDetails: ItemDetails) {
        selectedItemDetails = itemDetails
        openBottomSheet = true
    }

    if (openBottomSheet && selectedItemDetails != null) {
        ItemDetailsBottomSheet(
            closeBottomSheet = closeBottomSheet,
            sheetState = bottomSheetState,
            itemDetails = selectedItemDetails!!
        )
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
                    .verticalScroll(state = rememberScrollState())
            ) {
                ScoreboardPanel(
                    teamName = "Dusk",
                    isWinningTeam = uiState.match.winningTeam == "Dusk",
                    teamDetails = uiState.match.dusk,
                    openItemDetails = ::openItemDetailsBottomSheet,
                    getCreepScorePerMinute = getCreepScorePerMinute,
                    getGoldEarnedPerMinute = getGoldEarnedPerMinute
                )
                ScoreboardPanel(
                    teamName = "Dawn",
                    isWinningTeam = uiState.match.winningTeam == "Dawn",
                    teamDetails = uiState.match.dawn,
                    openItemDetails = ::openItemDetailsBottomSheet,
                    getCreepScorePerMinute = getCreepScorePerMinute,
                    getGoldEarnedPerMinute = getGoldEarnedPerMinute
                )
            }
        }
    }
}

@Composable
fun ScoreboardPanel(
    teamName: String,
    isWinningTeam: Boolean,
    teamDetails: TeamDetails,
    openItemDetails: (ItemDetails) -> Unit,
    getCreepScorePerMinute: (Int) -> String,
    getGoldEarnedPerMinute: (Int) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isWinningTeam) GreenHighlight else RedHighlight,
                    RoundedCornerShape(4.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        bottom = 1.dp
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                if (isWinningTeam) DarkGreenHighlight else DarkRedHighlight,
                                MaterialTheme.colorScheme.primary
                            ),
                            endX = 500f
                        ),
                        shape = RoundedCornerShape(3.dp),
                    )
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$teamName - ${if (isWinningTeam) "Victory" else "Defeat"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Average MMR: ${teamDetails.averageMmr}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        // Players
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            teamDetails.players.forEach { player ->
                PlayerRow(
                    player = player,
                    playerItems = player.playerItems,
                    openItemDetails = openItemDetails,
                    creepScorePerMinute = getCreepScorePerMinute(player.minionsKilled),
                    goldEarnedPerMinute = getGoldEarnedPerMinute(player.goldEarned)
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerRow(
    player: MatchPlayerDetails,
    playerItems: List<ItemDetails> = emptyList(),
    openItemDetails: (ItemDetails) -> Unit,
    creepScorePerMinute: String = "",
    goldEarnedPerMinute: String = ""
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val model = ImageRequest.Builder(context)
                    .data(player.rankedImage)
                    .crossfade(true)
                    .build()
                SubcomposeAsyncImage(
                    model = model,
                    contentDescription = null
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Success) {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Text(
                    text = "${player.mmr} MMR",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "${player.mmrChange} MMR",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text(
                text = player.playerName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold
            )

            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visible = expanded,
            enter = expandHorizontally(
                expandFrom = Alignment.CenterHorizontally
            ),
            exit = shrinkHorizontally(
                shrinkTowards = Alignment.CenterHorizontally
            )
        ) {
            Divider(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 32.dp),
                color = MaterialTheme.colorScheme.tertiary,
                thickness = 1.dp
            )
        }
        AnimatedVisibility(
            visible = expanded,
            exit = shrinkVertically(
                animationSpec = tween(delayMillis = 300),
                shrinkTowards = Alignment.CenterVertically
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 4
                ) {
                    playerItems.forEach { item ->
                        val painter = rememberAsyncImagePainter(item.image)
                        val state = painter.state

                        val transition by animateFloatAsState(
                            targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f,
                            label = "itemFloatAnimationState",
                        )
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .scale(.8f + (.2f * transition))
                                .alpha(transition)
                                .size(56.dp)
                                .clickable { openItemDetails(item) }
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Performance
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = player.performanceTitle,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${player.performanceScore} PS",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                        // Creep Score
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${player.minionsKilled} CS",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = creepScorePerMinute,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            KDAText(
                                style = MaterialTheme.typography.bodySmall,
                                averageKda = listOf(
                                    player.kills.roundToInt().toString(),
                                    player.deaths.roundToInt().toString(),
                                    player.assists.roundToInt().toString()
                                )
                            )
                            Text(
                                text = "${player.getKda()} KDA",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${player.goldEarned} Gold",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = goldEarnedPerMinute,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
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
fun ItemDetailsBottomSheet(
    itemDetails: ItemDetails,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = closeBottomSheet,
        sheetState = sheetState,
    ) {
        ItemDetailsBottomSheetContent(
            modifier = modifier,
            itemDetails = itemDetails
        )
    }
}

@Composable
fun ItemDetailsBottomSheetContent(
    modifier: Modifier = Modifier,
    itemDetails: ItemDetails
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val model = ImageRequest.Builder(context)
            .data(itemDetails.image)
            .crossfade(true)
            .build()
        Spacer(modifier = Modifier.size(40.dp))
        SubcomposeAsyncImage(
            model = model,
            contentDescription = null
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Success) {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.size(128.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {}
            Text(
                text = itemDetails.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {}
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MatchDetailsScreenPreview() {
    MonolithTheme {
        MatchDetailsScreen(
            uiState = MatchDetailsUiState(
                isLoading = false,
                match = MatchDetails(
                    winningTeam = "Dusk",
                    gameDuration = 2135,
                    dusk = TeamDetails(
                        averageMmr = "1234.5",
                        players = listOf(
                            MatchPlayerDetails(
                                mmr = "1234.5",
                                mmrChange = "+11.1",
                                playerName = "Player 1",
                                performanceTitle = "Annihilator",
                                performanceScore = "143.6",
                                kills = 7f,
                                deaths = 3f,
                                assists = 3f,
                                minionsKilled = 119,
                                goldEarned = 15434
                            )
                        )
                    ),
                    dawn = TeamDetails(
                        averageMmr = "1234.5",
                        players = listOf(
                            MatchPlayerDetails(
                                mmr = "1234.5",
                                mmrChange = "-11.1",
                                playerName = "Player 2",
                                performanceTitle = "Sentinel",
                                performanceScore = "104.6",
                            )
                        )
                    )
                )
            ),
            getCreepScorePerMinute = { "2.8 CS/min" },
            getGoldEarnedPerMinute = { "267.8 Gold/min" }
        )

    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemDetailsBottomSheetPreview() {
    MonolithTheme {
        ItemDetailsBottomSheetContent(
            itemDetails = ItemDetails(
                image = "https://omeda.city/images/items/Refillable-Potion.webp",
                name = "Refillable Potion",
                displayName = "Refillable Potion",
            ),
        )
    }
}