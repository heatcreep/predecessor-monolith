package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.RankDetails
import com.aowen.monolith.glance.PlayerStatsAppWidget
import com.aowen.monolith.glance.worker.UpdatePlayerStatsWorker
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.inputFieldDefaults
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnclaimPlayerDialog(
    modifier: Modifier = Modifier,
    handleSavePlayer: () -> Unit = {},
    onDismissRequest: () -> Unit = { }
) {
    BasicAlertDialog(
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

@Composable
fun PlayerCard(
    player: PlayerDetails?,
    playerNameField: String,
    claimedPlayerName: String?,
    isEditingPlayerName: Boolean,
    stats: PlayerStats?,
    modifier: Modifier = Modifier,
    isClaimed: Boolean = false,
    handleSavePlayer: suspend (Boolean) -> Unit = {},
    onPlayerNameChange: (String) -> Unit = {},
    handleSavePlayerName: () -> Unit = {},
    onEditPlayerName: () -> Unit = {}
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var isDialogOpen by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

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

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (player != null && stats != null) {
            // Player Name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditingPlayerName) {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(intrinsicSize = IntrinsicSize.Min),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = claimedPlayerName ?: player.playerName,
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        value = playerNameField,
                        colors = inputFieldDefaults(),
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = onPlayerNameChange,
                        trailingIcon = {
                            IconButton(onClick = handleSavePlayerName) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save Player Name",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                    )
                } else {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        text = if (isClaimed && player.isConsolePlayer) {
                            claimedPlayerName ?: player.playerName
                        } else {
                            player.playerName
                        }
                    )
                }
                if (isClaimed && player.isConsolePlayer) {
                    IconButton(onClick = onEditPlayerName) {
                        if(isEditingPlayerName) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Edit Player Name",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Player Name",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.offset(y = (-8).dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = player.vpTotal.toString(),
                )
                Image(
                    modifier = Modifier
                        .size(200.dp),
                    painter = painterResource(id = player.rankDetails.rankImageAssetId),
                    contentDescription = null
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = player.rankDetails.rankText,
                    color = player.rankDetails.rankColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
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
                                async { handleSavePlayer(false) }.await()
                                val glanceId = GlanceAppWidgetManager(context).getGlanceIds(
                                    PlayerStatsAppWidget::class.java
                                ).firstOrNull()
                                if (glanceId != null) {
                                    UpdatePlayerStatsWorker.enqueue(context, glanceId, force = true)
                                }
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
                // WinRate
                StatListItem(
                    modifier = modifier,
                    statLabel = "WinRate:",
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
                    rankDetails = RankDetails.GOLD_I,
                    vpTotal = 476
                ),
                isEditingPlayerName = false,
                playerNameField = "heatcreep.tv",
                claimedPlayerName = "heatcreep.tv",
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