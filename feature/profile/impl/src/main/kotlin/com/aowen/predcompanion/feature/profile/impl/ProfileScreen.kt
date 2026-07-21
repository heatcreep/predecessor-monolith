package com.aowen.predcompanion.feature.profile.impl

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.CurrentUser
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.ui.components.MatchPlayerCard
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.feature.profile.impl.ui.ConsoleDropdownMenu
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout
import com.aowen.predcompanion.feature.profile.impl.ui.ThemeDropdownMenu
import com.aowen.predcompanion.core.ui.model.toPlayerProfileCardUiModel
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import com.aowen.predcompanion.ui.theme.WarmWhite
import com.aowen.predcompanion.ui.theme.YellowHighlight
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import com.aowen.predcompanion.core.datastore.Theme as ThemeDataStore
import com.aowen.predcompanion.core.resources.R as coreResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    modifier: Modifier = Modifier,
    console: Console,
    theme: ThemeDataStore,
    sheetState: SheetState,
    handleSaveConsole: (Console) -> Unit,
    handleSaveTheme: (ThemeDataStore) -> Unit,
    handleSignOut: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            IconButton(
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) onDismissRequest()
                    }
                },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Spacer(modifier = Modifier.size(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ConsoleDropdownMenu(
                console = console,
                handleSaveConsole = handleSaveConsole
            )
            ThemeDropdownMenu(
                theme = theme,
                handleSaveTheme = handleSaveTheme
            )
            Text(
                text = "App Version: ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            ClickableText(
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                ),
                text = AnnotatedString("Privacy Policy"),
                onClick = {
                    uriHandler.openUri("https://monolith-app.dev/privacy")
                }
            )
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = handleSignOut
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign Out",
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Sign Out")
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenRoute(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    navigateToSearch: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // Show toast for delete account
    LaunchedEffect(true) {
        viewModel.showProfileToast.collect { show ->
            when (show) {
                ProfileToastState.DELETE -> {
                    showSnackbar("Account deleted successfully", SnackbarDuration.Short)
                }

                ProfileToastState.LOGOUT -> {
                    showSnackbar("Successfully logged out", SnackbarDuration.Short)
                }

                ProfileToastState.ERROR -> {
                    showSnackbar(
                        "There was an issue processing your request. Please try again later",
                        SnackbarDuration.Short
                    )
                }

                else -> {}
            }
            viewModel.onShowToastComplete()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val startAuth = rememberAuthLauncher(viewModel::loginIntent, viewModel::onLoginResult)
    val matches = viewModel.matchHistory.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Profile",
                actions = {
                    IconButton(onClick = navigateToSearch) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        if (showBottomSheet) {
            SettingsBottomSheet(
                sheetState = sheetState,
                console = uiState.console,
                theme = uiState.theme,
                handleSaveConsole = viewModel::saveConsole,
                handleSaveTheme = viewModel::saveTheme,
                handleSignOut = viewModel::handleLogout,
                onDismissRequest = { showBottomSheet = false }
            )
        }
        ProfileScreen(
            uiState = uiState,
            matches = matches,
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            submitLogin = startAuth,
            handleRetry = {}
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileScreenState,
    matches: LazyPagingItems<MatchListItemUiModel>,
    modifier: Modifier = Modifier,
    submitLogin: () -> Unit,
    handleRetry: () -> Unit,
) {

    val tabList = listOf(
        coreResources.string.core_resources_player_profile_tab_matches,
        coreResources.string.core_resources_player_profile_tab_heroes,
        coreResources.string.core_resources_player_profile_tab_friends_enemies,
    )
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        when (val userState = uiState.user) {
            is UserUiState.Loading -> {
                FullScreenLoadingIndicator("Profile")
            }

            is UserUiState.Error -> {
                FullScreenErrorWithRetry(
                    errorMessage = userState.message
                ) {
                    handleRetry()
                }
            }

            is UserUiState.SignedOut -> {
                ElevatedButton(
                    onClick = submitLogin,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = WarmWhite
                    ),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(id = coreResources.drawable.predgg_icon_only),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = "Sign in to Pred.gg",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            is UserUiState.UserInfoLoaded -> {
                if (userState.userInfo != null) {
                    PlayerProfileLayout(
                        profileCard = userState.userInfo.players.first()
                            .toPlayerProfileCardUiModel(),
                        tabLabels = tabList,
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                    ) { tab ->
                        when (tab) {
                            0 -> {
                                LazyColumn(
                                    state = rememberLazyListState(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(matches.itemCount) {
                                        matches[it]?.let { matchItem ->
                                            MatchPlayerCard(
                                                matchListItem = matchItem,
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {}
                            2 -> {}
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Warning",
                                tint = YellowHighlight
                            )
                            Text(
                                text = "Data stored locally will be different from data stored in the cloud.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
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
fun ProfileCardPreview() {

    val fakePagingData = flowOf(PagingData.empty<MatchListItemUiModel>())
    val fakeMatches = fakePagingData.collectAsLazyPagingItems()
    MonolithTheme {
        ProfileScreen(
            uiState = ProfileScreenState(
                user = UserUiState.UserInfoLoaded(
                    userInfo = CurrentUser(
                        id = "preview-id",
                        name = "Test User",
                        players = emptyList(),
                    )
                )
            ),
            matches = fakeMatches,
            submitLogin = {},
            handleRetry = { /*TODO*/ }
        )
    }
}