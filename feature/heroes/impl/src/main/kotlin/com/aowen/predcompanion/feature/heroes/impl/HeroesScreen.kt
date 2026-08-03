package com.aowen.predcompanion.feature.heroes.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.ui.cards.heroes.HeroTileCard
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.ui.common.MonolithCollapsableGridColumn
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HeroesScreenRoute(
    modifier: Modifier = Modifier,
    navigateToHeroDetails: (heroId: String) -> Unit = { _ -> },
    navigateToSearch: () -> Unit,
    viewModel: HeroesScreenViewModel = hiltViewModel()
) {

    val heroesScreenUiState by viewModel.uiState.collectAsState()

    HeroesScreen(
        uiState = heroesScreenUiState,
        onFilterRole = viewModel::updateRoleOption,
        onFilterHeroes = viewModel::getFilteredHeroes,
        modifier = modifier,
        navigateToHeroDetails = navigateToHeroDetails,
        navigateToSearch = navigateToSearch
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroesScreen(
    uiState: HeroesScreenUiState,
    onFilterRole: (HeroRole, Boolean) -> Unit,
    onFilterHeroes: () -> Unit,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    navigateToHeroDetails: (heroId: String) -> Unit = { _ -> },
    navigateToSearch: () -> Unit = { }
) {
    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    val isTablet = screenWidthDp >= 600.dp

    var expanded by remember { mutableStateOf(true) }
    val rotationAngle = remember { Animatable(0f) }

    val listState = rememberLazyGridState()



    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }

    LaunchedEffect(uiState.searchFieldValue) {
        this.launch {
            delay(500)
            onFilterHeroes()
        }
    }

    LaunchedEffect(uiState.selectedRoleFilters) {
        onFilterHeroes()
    }

    if (uiState.isLoading) {
        FullScreenLoadingIndicator("Heroes")
    } else {
        if (uiState.error != null) {
            FullScreenErrorWithRetry(
                errorMessage = uiState.error
            ) {
                handleRetry()
            }
        } else {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    MonolithTopAppBar(
                        title = "Heroes",
                        actions = {
                            IconButton(onClick = navigateToSearch) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    MonolithCollapsableGridColumn(listState = listState) {
                        AnimatedVisibility(visible = expanded) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                items(items = HeroRole.entries.dropLast(1)) { role ->
                                    PredCompanionChipFilter(
                                        text = role.roleName,
                                        selected = uiState.selectedRoleFilters.contains(role),
                                        iconRes = role.simpleDrawableId,
                                        onClick = {
                                            onFilterRole(
                                                role,
                                                !uiState.selectedRoleFilters.contains(role)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.currentHeroes.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No heroes matched your search.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            columns = GridCells.Fixed(if (isTablet) 6 else 3),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(uiState.currentHeroes) { hero ->
                                HeroTileCard(
                                    hero = hero,
                                    onClick = {
                                        navigateToHeroDetails(hero.id)
                                    }
                                )
                            }

                        }
                    }
                }
            }
        }

    }

}

@Preview(
    showBackground = true,
)
@Composable
fun HeroesScreenPreview() {
    MonolithTheme {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    isLoading = true,
                    allHeroes = listOf(
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/belica_200.png",
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/morigesh_200.png",
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/twinblast_200.png",
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/greystone_200.png",
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/grux_200.png",
                            displayName = "Grux"
                        ),
                    ),
                    currentHeroes = listOf(
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/belica_200.png",
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/morigesh_200.png",
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/twinblast_200.png",
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/greystone_200.png",
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/grux_200.png",
                            displayName = "Grux"
                        ),
                    )
                ),
                onFilterRole = { _, _ -> },
                onFilterHeroes = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeroesScreenPreviewDeviceSizes() {
    MonolithTheme {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    isLoading = false,
                    allHeroes = listOf(
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/belica_200.png",
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/morigesh_200.png",
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/twinblast_200.png",
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/greystone_200.png",
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/grux_200.png",
                            displayName = "Grux"
                        ),
                    ),
                    currentHeroes = listOf(
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/narbash_200.png",
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/belica_200.png",
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/morigesh_200.png",
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/twinblast_200.png",
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/greystone_200.png",
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageUrl = "https://cdn.monolith.gg/assets/heroes/grux_200.png",
                            displayName = "Grux"
                        ),
                    )
                ),
                onFilterRole = { _, _ -> },
                onFilterHeroes = {}
            )
        }
    }
}

