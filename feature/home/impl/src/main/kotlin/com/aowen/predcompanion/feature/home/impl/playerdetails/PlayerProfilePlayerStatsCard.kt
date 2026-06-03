package com.aowen.predcompanion.feature.home.impl.playerdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.ui.components.StatListItem
import com.aowen.predcompanion.core.ui.model.mapper.StatLine
import com.aowen.predcompanion.core.ui.components.KDAText
import com.aowen.predcompanion.core.ui.model.toRankDetails
import com.aowen.predcompanion.ui.theme.inputFieldDefaults
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun PlayerProfilePlayerStatsCard(
    modifier: Modifier = Modifier,
    playerDetails: PlayerInfo.PlayerDetails,
    stats: List<StatLine>,
    claimedPlayerName: String? = null,
    playerNameField: String = "",
    onPlayerNameChange: (String) -> Unit = {},
    handleSavePlayerName: () -> Unit = {},
    isEditingPlayerName: Boolean = false,
    onEditPlayerName: () -> Unit = {},
    isClaimed: Boolean = false,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val rankDetails = playerDetails.rank.toRankDetails()
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        text = rankDetails.rankText,
                    )

                }
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp),
                    model = rankDetails.rankImageAssetId,
                    contentDescription = null
                )
            }
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
                                text = claimedPlayerName ?: playerDetails.playerName,
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
                        text = if (isClaimed && playerDetails.isConsolePlayer) {
                            claimedPlayerName ?: playerDetails.playerName
                        } else {
                            playerDetails.playerName
                        }
                    )
                }
                if (isClaimed && playerDetails.isConsolePlayer) {
                    IconButton(onClick = onEditPlayerName) {
                        if (isEditingPlayerName) {
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
            stats.forEach { stat ->
                when (stat) {
                    is StatLine.MultiStatLine -> {
                        StatListItem(
                            modifier = modifier,
                            statLabel = stringResource(coreResources.string.core_resources_player_profile_average_kda),
                            statValue = { KDAText(averageKda = stat.values) }
                        )
                    }

                    is StatLine.SingleStatLine -> {
                        StatListItem(
                            modifier = modifier,
                            statLabel = stat.label,
                            statValue = {
                                Text(
                                    text = stat.value,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        )
                    }
                }
                if (stat != stats.last()) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable()
fun PlayerProfilePlayerStatsCardPreview() {
    MonolithTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            PlayerProfilePlayerStatsCard(
                modifier = Modifier.padding(16.dp),
                playerDetails = PlayerInfo.PlayerDetails(
                    playerName = "heatcreep.tv",
                    vpCurrent = 63,
                    rankTitle = "Gold III",
                    rankImage = "",
                ),
                stats = listOf(
                    StatLine.MultiStatLine(label = "KDA", values = listOf("3.5", "4.0", "2.8")),
                    StatLine.SingleStatLine(label = "Matches Played", value = "1500"),
                    StatLine.SingleStatLine(label = "Win Rate", value = "52%"),
                    StatLine.SingleStatLine(label = "Average Score", value = "2450"),
                )
            )
        }
    }
}