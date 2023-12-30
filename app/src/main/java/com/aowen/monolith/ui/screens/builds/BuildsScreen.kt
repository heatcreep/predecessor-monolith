package com.aowen.monolith.ui.screens.builds

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.data.getRoleImage
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight

@Composable
fun BuildsScreenRoute(
    navController: NavController,
    viewModel: BuildsScreenViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initViewModel()
    }

    BuildsScreen(
        uiState = uiState
    )
}

@Composable
fun BuildsScreen(
    uiState: BuildsUiState
) {
    if (uiState.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            if (uiState.error.isNotEmpty()) {
                FullScreenErrorWithRetry {}
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                ) {

                    if (uiState.builds.isEmpty()) {
                        Text(
                            text = "No builds found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(items = uiState.builds) { build ->
                                BuildListItem(build = build)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuildListItem(
    modifier: Modifier = Modifier,
    build: BuildListItem
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayerIcon(
                    heroImageId = getHeroImage(build.heroId).drawableId,
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
                            id = getRoleImage(build.role.lowercase()).drawableId
                        ),
                        contentDescription = null
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = build.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Author: ${build.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = getItemImage(build.crest)),
                            contentDescription = null
                        )
                        build.buildItems.forEach {
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
                        text = "${build.upvotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    IconButton(onClick = { /*TODO*/ }) {
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
                        text = "${build.downvotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    IconButton(onClick = { /*TODO*/ }) {
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

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BuildListItemPreview() {
    MonolithTheme {
        BuildListItem(
            build = BuildListItem(
                id = 1,
                title = "Muriel Support Build [0.13.1]",
                description = "Test Build Description",
                heroId = 15,
                buildItems = listOf(1, 1, 1, 1, 1),
                createdAt = "2021-01-01",
                updatedAt = "2021-01-01",
                author = "heatcreep.tv",
                crest = 1,
                role = "Support"
            )
        )
    }
}