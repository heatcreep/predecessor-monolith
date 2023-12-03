package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.ui.theme.MonolithTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnclaimPlayerDialog(
    modifier: Modifier = Modifier,
    handleSavePlayer: () -> Unit = {},
    onDismissRequest: () -> Unit = { }
) {
    AlertDialog(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Text(
                text = "Are you sure you want to unclaim this player?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = onDismissRequest,
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = "Cancel")
                }
                ElevatedButton(
                    onClick = handleSavePlayer,
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = "Yes")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCard(
    player: PlayerDetails?,
    stats: PlayerStats?,
    modifier: Modifier = Modifier,
    isClaimed: Boolean = false,
    handleSavePlayer: suspend (Boolean) -> Unit = {},
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isDialogOpen by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    var openBottomSheetState by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(isClaimed) {
        rotationAngle.animateTo(
            targetValue = if (isClaimed) 45f else 0f,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        )
    }

    if (isDialogOpen) {
        UnclaimPlayerDialog(
            onDismissRequest = { isDialogOpen = false },
            handleSavePlayer = {
                coroutineScope.launch {
                    handleSavePlayer(true)
                }
                isDialogOpen = false
            }
        )
    }

    if (openBottomSheetState) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheetState = false },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Player Rank & MMR",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "All statistics are calculated based on PVP & ranked matches.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "MMR is calculated by Omeda.city; it may not be the exact MMR used in internal matchmaking. " +
                            "Our rating system uses an unbiased Bayesian rating algorithm based on factor graphs to model player skill. " +
                            "Rating changes may appear unintuitive due to the algorithm's complexity, but the ratings demonstrate a high level of accuracy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Performance Score is calculated by Omeda.city; it may not be the exact Performance Score used in internal matchmaking. " +
                            "Our performance score is calculated by a proprietary algorithm that takes into account a player's KDA, damage dealt, healing done, and other factors. " +
                            "The performance score is a good indicator of a player's skill, but it is not a perfect metric.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (player != null && stats != null) {
            val model = ImageRequest.Builder(context)
                .data(player.rankImage)
                .crossfade(true)
                .build()

            // Player Name
            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                text = player.playerName
            )
            Spacer(modifier = Modifier.size(16.dp))
            SubcomposeAsyncImage(
                model = model,
                contentDescription = player.rankTitle
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Success) {
                    SubcomposeAsyncImageContent()
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = player.rankTitle,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        openBottomSheetState = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            if (isClaimed) {
                                isDialogOpen = true
                            } else {
                                handleSavePlayer(false)
                            }
                        }
                    },
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotationAngle.value),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    AnimatedContent(targetState = isClaimed, label = "ClaimPlayer") {
                        Text(text = if (it) "Unclaim Player" else "Claim Player")
                    }


                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            // Stat List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rank
                StatListItem(
                    modifier = modifier,
                    statLabel = "Rank:",
                    statValue = {
                        Text(
                            text = player.rankTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // MMR
                StatListItem(
                    modifier = modifier,
                    statLabel = "MMR:",
                    statValue = {
                        Text(
                            text = player.mmr ?: "0.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Winrate
                StatListItem(
                    modifier = modifier,
                    statLabel = "Winrate:",
                    statValue = {
                        Text(
                            text = stats.winRate,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Matches Played
                StatListItem(
                    modifier = modifier,
                    statLabel = "Matches Played:",
                    statValue = {
                        Text(
                            text = stats.matchesPlayed,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Favorite Hero
                StatListItem(
                    modifier = modifier,
                    statLabel = "Favorite Hero:",
                    statValue = {
                        Text(
                            text = stats.favoriteHero,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                // Favorite Role
                StatListItem(
                    modifier = modifier,
                    statLabel = "Favorite Role:",
                    statValue = {
                        Text(
                            text = stats.favoriteRole,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                StatListItem(
                    modifier = modifier,
                    statLabel = "Average KDA:",
                    statValue = { KDAText(averageKda = stats.averageKda) }
                )
                // Average Performance Score
                StatListItem(
                    modifier = modifier,
                    statLabel = "Average PS:",
                    statValue = {
                        Text(
                            text = stats.averagePerformanceScore,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
            }
        } else {
            // Error Message
            Text(
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                text = "Error getting player data"
            )
        }
    }
}

@Composable
fun StatListItem(
    modifier: Modifier = Modifier,
    statLabel: String = "",
    statValue: @Composable () -> Unit,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            statValue()
        }
        Spacer(modifier = Modifier.size(8.dp))
        Divider(
            color = MaterialTheme.colorScheme.secondary
        )
    }

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PlayerCardPreview() {
    MonolithTheme {
        Surface {
            PlayerCard(
                player = PlayerDetails(
                    playerName = "heatcreep.tv",
                    rankTitle = "Gold IV",
                ),
                stats = PlayerStats(
                    favoriteHero = "Narbash",
                    favoriteRole = "Support",
                    averageKda = listOf(
                        "2.65",
                        "4.85",
                        "7.02"
                    ),
                    averageKdaRatio = "1.99",
                    hoursPlayed = "175.97",
                    matchesPlayed = "359",
                    averagePerformanceScore = "86.21",
                    winRate = "50%"

                )
            )
        }
    }
}