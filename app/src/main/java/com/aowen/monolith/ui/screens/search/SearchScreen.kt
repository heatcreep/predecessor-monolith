package com.aowen.monolith.ui.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.inputFieldDefaults

@Composable
internal fun SearchScreenRoute(
    modifier: Modifier = Modifier,
    navigateToPlayerDetails: (String) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {

    val searchUiState by searchScreenViewModel.uiState.collectAsState()

    SearchScreen(
        uiState = searchUiState,
        setSearchValue = searchScreenViewModel::setSearchValue,
        handleSubmitSearch = searchScreenViewModel::handleSubmitSearch,
        navigateToPlayerDetails = navigateToPlayerDetails,
        modifier = modifier
    )
}

@Composable
fun SearchScreen(
    uiState: SearchScreenUiState,
    setSearchValue: (String) -> Unit,
    handleSubmitSearch: () -> Unit,
    navigateToPlayerDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        // Search bar
        Column {
            SearchBar(
                searchValue = uiState.searchFieldValue,
                setSearchValue = setSearchValue,
                handleSubmitSearch = handleSubmitSearch,
                modifier = modifier
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Players",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (uiState.isLoading) {
                FullScreenLoadingIndicator()
            } else {
                if (uiState.initPlayersListText != null) {
                    Text(
                        text = uiState.initPlayersListText,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.playersList) { player ->
                            player?.let {
                                PlayerResultCard(
                                    playerDetails = player,
                                    navigateToPlayerDetails = navigateToPlayerDetails
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchValue: String,
    setSearchValue: (String) -> Unit,
    handleSubmitSearch: () -> Unit,
    modifier: Modifier = Modifier
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary
    )
    Row {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            shape = RoundedCornerShape(24.dp),
            placeholder = {
                Text(
                    text = "Player lookup",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                    handleSubmitSearch()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    handleSubmitSearch()
                }
            ),
            value = searchValue,
            colors = inputFieldDefaults(),
            singleLine = true,
            maxLines = 1,
            onValueChange = setSearchValue
        )
    }
}

@Composable
fun PlayerResultCard(
    playerDetails: PlayerDetails,
    navigateToPlayerDetails: (String) -> Unit,
    modifier: Modifier = Modifier
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
                Dp.Hairline,
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
            SubcomposeAsyncImage(
                model = model,
                contentDescription = playerDetails.rank
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
            Column() {
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
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenPreview() {
    MonolithTheme() {
        Surface() {
            SearchScreen(
                uiState = SearchScreenUiState(
                    playersList = listOf(
                        PlayerDetails(
                            playerName = "heatcreep.tv",
                            region = "naeast"
                        )
                    ),
                    initPlayersListText = null
                ),
                setSearchValue = {},
                handleSubmitSearch = {},
                navigateToPlayerDetails = {}
            )
        }
    }
}