package com.aowen.monolith.feature.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.data.PlayerDetails

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