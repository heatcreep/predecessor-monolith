package com.aowen.predcompanion.feature.profile.impl

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout
import com.aowen.predcompanion.core.ui.components.KDAText
import com.aowen.predcompanion.core.ui.components.MatchPlayerCard
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.feature.profile.impl.ui.ConsoleDropdownMenu
import com.aowen.predcompanion.feature.profile.impl.ui.ThemeDropdownMenu
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import com.aowen.predcompanion.ui.theme.WarmWhite
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
            val privacyPolicy =
                stringResource(id = coreResources.string.core_resources_privacy_policy)
            Text(
                text = buildAnnotatedString {
                    withLink(
                        LinkAnnotation.Url(
                            url = "https://monolith-app.dev/privacy",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                    ) {
                        append(privacyPolicy)
                    }
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
    navigateToMatchDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
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
    val heroStatsState by viewModel.heroStats.collectAsStateWithLifecycle()

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
            heroStatsState = heroStatsState,
            navigateToMatchDetails = navigateToMatchDetails,
            navigateToHeroDetails = navigateToHeroDetails,
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
    heroStatsState: HeroStatsUiState,
    modifier: Modifier = Modifier,
    submitLogin: () -> Unit,
    handleRetry: () -> Unit,
    navigateToMatchDetails: (String) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
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
                if (userState.userInfo != null && userState.userInfo.players.isNotEmpty()) {
                    PlayerProfileLayout(
                        profileCard = userState.userInfo.players.first(),
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
                                                modifier = Modifier.clickable {
                                                    navigateToMatchDetails(matchItem.matchId)
                                                },
                                                navigateToHeroDetails = navigateToHeroDetails,
                                                matchListItem = matchItem,
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                when (heroStatsState) {
                                    is HeroStatsUiState.Loading -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(48.dp),
                                                color = MaterialTheme.colorScheme.tertiary,
                                                strokeWidth = 8.dp
                                            )
                                        }
                                    }

                                    is HeroStatsUiState.Error -> {
                                        FullScreenErrorWithRetry(heroStatsState.message) { }
                                    }

                                    is HeroStatsUiState.Loaded -> {
                                        LazyColumn(
                                            state = rememberLazyListState(),
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {

                                            itemsIndexed(
                                                heroStatsState.stats,
                                                key = { _, heroStat -> heroStat.id }) { index, heroStat ->
                                                Column(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth(),
                                                        Arrangement.SpaceBetween
                                                    ) {
                                                        Box(
                                                            modifier = Modifier.weight(1f)
                                                        ) {
                                                            Text(
                                                                text = heroStat.name,
                                                                color = MaterialTheme.colorScheme.secondary,
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                        }
                                                        Box(
                                                            modifier = Modifier.weight(0.5f)
                                                        ) {
                                                            Text(
                                                                text = heroStat.totalMatches,
                                                                color = MaterialTheme.colorScheme.secondary,
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                        }
                                                        Box(
                                                            modifier = Modifier.weight(0.5f)
                                                        ) {
                                                            Text(
                                                                text = heroStat.winRate,
                                                                color = MaterialTheme.colorScheme.secondary,
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                        }
                                                        Box(
                                                            modifier = Modifier.weight(1f),
                                                            contentAlignment = Alignment.CenterEnd
                                                        ) {
                                                            KDAText(
                                                                averageKda = listOf(
                                                                    heroStat.averageKills,
                                                                    heroStat.averageDeaths,
                                                                    heroStat.averageAssists
                                                                )
                                                            )
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.size(8.dp))
                                                    if (index != heroStatsState.stats.count() - 1) {
                                                        HorizontalDivider(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            color = MaterialTheme.colorScheme.secondary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                            2 -> {
                                // TODO: Implement friends and enemies
                                Text(
                                    text = stringResource(id = coreResources.string.core_resources_player_profile_coming_soon),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
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
                        Text(
                            text = stringResource(id = coreResources.string.core_resources_player_profile_claim_player),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val claimInstructionsBulletOne =
                                stringResource(id = coreResources.string.core_resources_player_profile_claim_instructions_visit_predgg)
                            val claimInstructionsBulletTwo =
                                stringResource(id = coreResources.string.core_resources_player_profile_claim_instructions_search_player)
                            val claimInstructionsBulletThree =
                                stringResource(id = coreResources.string.core_resources_player_profile_claim_instructions_claim_player)
                            val claimInstructionsBulletFour =
                                stringResource(id = coreResources.string.core_resources_player_profile_claim_instructions_complete_claim)
                            BulletItem {

                                append(claimInstructionsBulletOne)

                                withLink(
                                    LinkAnnotation.Url(
                                        url = "https://pred.gg",
                                        styles = TextLinkStyles(
                                            style = SpanStyle(
                                                textDecoration = TextDecoration.Underline
                                            )
                                        )
                                    )
                                ) {
                                    append("https://pred.gg")
                                }
                            }
                            BulletItem {
                                append(claimInstructionsBulletTwo)

                            }
                            BulletItem {
                                append(claimInstructionsBulletThree)
                            }
                            BulletItem {
                                append(claimInstructionsBulletFour)

                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun BulletItem(content: AnnotatedString.Builder.() -> Unit) {
    Row {
        Text(
            text = "•  ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = buildAnnotatedString { content() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
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
                    userInfo = CurrentUserUiModel(
                        name = "heatcreep.tv",
                        players = emptyList()
                    )
                )
            ),
            matches = fakeMatches,
            submitLogin = {},
            handleRetry = { /*TODO*/ },
            heroStatsState = HeroStatsUiState.Loading,
            navigateToMatchDetails = { /*TODO*/ },
            navigateToHeroDetails = { /*TODO*/ },
        )
    }
}