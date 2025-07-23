package com.aowen.monolith.feature.matches

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchPlayerDetails
import com.aowen.monolith.data.MatchType
import com.aowen.monolith.data.Team
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.data.getHeroName
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.data.getKda
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.KDAText
import com.aowen.monolith.ui.theme.DarkGreenHighlight35
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight
import com.aowen.monolith.ui.utils.handleTimeSinceMatch

@Composable
fun MatchesList(
    modifier: Modifier = Modifier,
    playerId: String? = "",
    matches: List<MatchDetails>? = null,
    navigateToMoreMatches: (String) -> Unit = { },
    navigateToMatchDetails: (String, String) -> Unit = { _, _ -> }
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Match History",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            playerId?.let {
                TextButton(onClick = { navigateToMoreMatches(it) }) {
                    Text(
                        text = "See All Matches",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        matches?.let { it ->
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                it.forEach { match ->
                    val allPlayers = match.dusk.players + match.dawn.players
                    val playerHero = allPlayers.firstOrNull { it.playerId == playerId }
                    val playerTeam = if (match.dusk.players.contains(playerHero)) "Dusk" else "Dawn"
                    val isWinner = playerTeam == match.winningTeam
                    MatchPlayerCard(
                        modifier.clickable {
                            navigateToMatchDetails(playerHero?.playerId!!, match.matchId)
                        },
                        isWinner = isWinner,
                        matchType = match.matchType,
                        timeSinceMatch = handleTimeSinceMatch(match.endTime),
                        playerHero = playerHero
                    )
                }
            }
        }
    }
}

@Composable
fun MatchPlayerCard(
    modifier: Modifier = Modifier,
    isWinner: Boolean,
    matchType: MatchType?,
    timeSinceMatch: String,
    playerHero: MatchPlayerDetails?,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isWinner) GreenHighlight else RedHighlight,
                RoundedCornerShape(4.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(
                    start = 8.dp,
                    bottom = 1.dp
                )
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (isWinner) DarkGreenHighlight35 else RedHighlight,
                            MaterialTheme.colorScheme.primary
                        ),
                        endX = 250f
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Win/Loss + MMR Change
                Column(
                    modifier = Modifier
                        .width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(
                                color = if (isWinner) GreenHighlight else RedHighlight,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = if (isWinner) "Victory" else "Defeat",
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    matchType?.let { type ->
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            text = type.text
                        )
                        if( type == MatchType.RANKED) {
                            Text(
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                text = "${playerHero?.vpChange}"
                            )
                        }
                    }
                    playerHero?.vpChange?.let {vpChange ->

                    }
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        text = timeSinceMatch
                    )
                }
                playerHero?.let {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            PlayerIcon(
                                heroImageId = getHeroImage(playerHero.heroId)
                            ) {
                                getHeroRole(playerHero.role)?.let { role ->
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
                                            id = role.drawableId
                                        ),
                                        contentDescription = null
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(8.dp))
                            Column {
                                Text(
                                    text = getHeroName(playerHero.heroId),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "${it.performanceScore} PS",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
            playerHero?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KDAText(
                        averageKda = listOf(
                            it.kills.toString(),
                            it.deaths.toString(),
                            it.assists.toString()
                        )
                    )
                    Text(
                        text = "${it.getKda()} KDA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
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
fun MatchesListPreview() {
    MonolithTheme {
        Surface(Modifier.fillMaxSize()) {
            MatchesList(
                playerId = "foo",
                matches = listOf(
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dusk",
                        matchType = MatchType.RANKED,
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 12,
                                    role = "support",
                                )

                            )
                        ),
                        dusk = Team.Dusk(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 14,
                                    role = "offlane",
                                    performanceScore = "94.6",
                                    vpChange = "11.4",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )

                    ),
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dusk",
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 12,
                                    role = "support",
                                )
                            )
                        ),
                        dusk = Team.Dusk(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 13,
                                    role = "jungle",
                                    performanceScore = "140.8",
                                    vpChange = "11.4",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )
                    ),
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dawn",
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 12,
                                    role = "support",
                                )
                            )
                        ),
                        dusk = Team.Dusk(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 15,
                                    role = "midlane",
                                    performanceScore = "78.2",
                                    vpChange = "111.4",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )

                    ),
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MatchesListPreviewLightMode() {
    MonolithTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            MatchesList(
                playerId = "foo",
                matches = listOf(
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dusk",
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 11,
                                )
                            )
                        ),
                        dusk = Team.Dusk(
                            listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 16,
                                    role = "offlane",
                                    vpChange = "11.4",
                                    performanceScore = "94.6",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )
                    ),
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dusk",
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 11,
                                )
                            )
                        ),
                        dusk = Team.Dusk(
                            players = listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 14,
                                    role = "support",
                                    vpChange = "11.4",
                                    performanceScore = "140.8",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )
                    ),
                    MatchDetails(
                        matchId = "1",
                        winningTeam = "Dawn",
                        endTime = "2023-10-01T12:00:00Z",
                        dawn = Team.Dawn(
                            listOf(
                                MatchPlayerDetails(
                                    playerId = "bar",
                                    playerName = "bean man",
                                    heroId = 11,
                                )
                            )
                        ),
                        dusk = Team.Dusk(
                            listOf(
                                MatchPlayerDetails(
                                    playerId = "foo",
                                    playerName = "heatcreep.tv",
                                    heroId = 14,
                                    role = "midlane",
                                    vpChange = "111.4",
                                    performanceScore = "78.2",
                                    kills = 7,
                                    deaths = 2,
                                    assists = 12,
                                )
                            )
                        )
                    ),
                )
            )
        }
    }
}