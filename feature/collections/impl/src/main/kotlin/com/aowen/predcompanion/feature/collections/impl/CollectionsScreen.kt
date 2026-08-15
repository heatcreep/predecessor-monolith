package com.aowen.predcompanion.feature.collections.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.ui.cards.collections.CollectionTileCard
import com.aowen.predcompanion.core.ui.dropdown.PredCompanionFilterDropdown
import com.aowen.predcompanion.core.ui.dropdown.PredCompanionSortDropdown
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalListItemUiModel
import com.aowen.predcompanion.ui.common.MonolithCollapsableGridColumn
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import com.aowen.predcompanion.core.resources.R as coreResources

val defaultStats = listOf(
    "Max Health",
    "Max Mana",
    "Health Regen",
    "Mana Regen",
    "Physical Power",
    "Magical Power",
    "Attack Speed",
    "Physical Armor",
    "Magical Armor",
    "Heal and Shield Increase",
    "Ability Haste",
    "Lifesteal",
    "Magical Lifesteal",
    "Omnivamp",
    "Movement Speed",
    "Physical Penetration",
    "Magical Penetration",
    "Critical Chance",
    "Tenacity",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CollectionsScreen(
    uiState: HeroesScreenUiState,
    onFilterRole: (HeroRole, Boolean) -> Unit,
    onSelectHeroClass: (HeroClass) -> Unit,
    onClearSelectedHeroClass: () -> Unit,
    onSelectItemTier: (String) -> Unit,
    onSelectStatFilter: (String) -> Unit,
    onFilterHeroes: () -> Unit,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit = {},
    navigateToHeroDetails: (heroId: String) -> Unit = { _ -> },
    navigateToEternalDetails: (String) -> Unit,
    navigateToItemDetails: (String) -> Unit = {},
    navigateToSearch: () -> Unit = { }
) {

    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(true) }
    val rotationAngle = remember { Animatable(0f) }

    val tabs = listOf(
        stringResource(R.string.feature_collections_heroes),
        stringResource(R.string.feature_collections_eternals),
        stringResource(R.string.feature_collections_items)
    )
    val pageCount = tabs.size

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = uiState.selectedTabIndex
    )


    LaunchedEffect(pagerState.currentPage) {
        onSelectTab(pagerState.currentPage)
    }

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
            delay(500.milliseconds)
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
                        title = stringResource(R.string.feature_collection_title),
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
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    SecondaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(
                                    pagerState.currentPage,
                                    matchContentSize = false
                                ),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                    ) {
                        tabs.forEachIndexed { index, tabName ->
                            Tab(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = tabName) },
                                unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                                selectedContentColor = MaterialTheme.colorScheme.secondary,
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        state = pagerState,
                        beyondViewportPageCount = 0
                    ) { page ->
                        when (page) {
                            0 -> HeroesCollection(
                                selectedRoleFilters = uiState.selectedRoleFilters,
                                currentHeroes = uiState.currentHeroes,
                                onFilterRole = onFilterRole,
                                navigateToHeroDetails = navigateToHeroDetails
                            )

                            1 -> EternalsCollection(
                                currentEternals = uiState.allEternals,
                                navigateToEternalDetails = navigateToEternalDetails
                            )

                            2 -> ItemsCollection(
                                items = uiState.allItems,
                                selectedHeroClass = uiState.selectedHeroClass,
                                onSelectHeroClass = onSelectHeroClass,
                                onClearSelectedHeroClass = onClearSelectedHeroClass,
                                selectedTierFilter = uiState.selectedTierFilter,
                                onSelectTier = onSelectItemTier,
                                selectedStatFilters = uiState.selectedStatsFilters,
                                onSelectStat = onSelectStatFilter,
                                navigateToItemDetails = navigateToItemDetails

                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun HeroesCollection(
    modifier: Modifier = Modifier,
    selectedRoleFilters: List<HeroRole> = emptyList(),
    currentHeroes: List<HeroDetails> = emptyList(),
    onFilterRole: (HeroRole, Boolean) -> Unit,
    navigateToHeroDetails: (String) -> Unit,
) {

    var expanded by remember { mutableStateOf(true) }

    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    val isTablet = screenWidthDp >= 600.dp

    val listState = rememberLazyGridState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MonolithCollapsableGridColumn(listState = listState) {
            AnimatedVisibility(visible = expanded) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(items = HeroRole.entries.dropLast(1)) { role ->
                        PredCompanionChipFilter(
                            text = role.roleName,
                            selected = selectedRoleFilters.contains(role),
                            iconRes = role.simpleDrawableId,
                            onClick = {
                                onFilterRole(
                                    role,
                                    selectedRoleFilters.contains(role)
                                )
                            }
                        )
                    }
                }
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            columns = GridCells.Fixed(if (isTablet) 6 else 3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(currentHeroes) { hero ->
                hero.posterImageId?.let { posterImage ->
                    CollectionTileCard(
                        title = hero.displayName,
                        imageUrl = posterImage,
                        onClick = {
                            navigateToHeroDetails(hero.id)
                        }
                    )
                }
            }

        }
    }


}

@Composable
fun EternalsCollection(
    modifier: Modifier = Modifier,
    currentEternals: List<EternalListItemUiModel>,
    navigateToEternalDetails: (String) -> Unit,
) {

    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    val isTablet = screenWidthDp >= 600.dp

    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        columns = GridCells.Fixed(if (isTablet) 6 else 3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(currentEternals) { eternal ->

            CollectionTileCard(
                title = eternal.name,
                imageUrl = eternal.imageSrc,
                onClick = {
                    navigateToEternalDetails(eternal.id)
                }
            )

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsCollection(
    modifier: Modifier = Modifier,
    items: List<ItemDetails> = emptyList(),
    selectedHeroClass: HeroClass? = null,
    onSelectHeroClass: (HeroClass) -> Unit = {},
    onClearSelectedHeroClass: () -> Unit = {},
    selectedTierFilter: String? = "Tier III",
    onSelectTier: (String) -> Unit = {},
    selectedStatFilters: List<String> = emptyList(),
    onSelectStat: (String) -> Unit = {},
    navigateToItemDetails: (String) -> Unit = {},
) {

    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    val isTablet = screenWidthDp >= 600.dp

    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    val itemsListState = rememberLazyGridState()
    val listState = rememberLazyListState()

    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                PredCompanionChipFilter(
                    text = "All",
                    iconRes = coreResources.drawable.all_roles,
                    selected = selectedHeroClass == null,
                    onClick = onClearSelectedHeroClass
                )
            }
            items(HeroClass.entries.dropLast(1)) {
                PredCompanionChipFilter(
                    text = it.label,
                    iconRes = it.iconRes,
                    selected = selectedHeroClass == it,
                    onClick = {
                        onSelectHeroClass(it)
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PredCompanionFilterDropdown(
                selectedOptions = selectedStatFilters,
                filterOptions = defaultStats,
                onClickOption = onSelectStat
            )
            PredCompanionSortDropdown(
                selectedOption = selectedTierFilter ?: "Tier",
                sortOptions = listOf("Tier I", "Tier II", "Tier III"),
                onClickOption = onSelectTier
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (items.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Items list is empty")
            }
        } else {
            LazyVerticalGrid(
                state = itemsListState,
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(if (isTablet) 5 else 3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageSrc)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Item image",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ItemCard(
                            itemDetails = item,
                            onClick = { navigateToItemDetails(item.name) }
                        ) {
                            SubcomposeAsyncImageContent(
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }

                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

    }

}

@Composable
fun ItemCard(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    imageContent: @Composable () -> Unit,
) {

    Card(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        ) {
            imageContent()
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = itemDetails.displayName,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )

        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun CollectionsScreenPreview() {
    MonolithTheme {
        Surface {
            CollectionsScreen(
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
                onFilterHeroes = {},
                onSelectHeroClass = {},
                onSelectItemTier = {},
                onSelectStatFilter = {},
                onSelectTab = {},
                handleRetry = { },
                navigateToHeroDetails = { },
                navigateToEternalDetails = {},
                navigateToSearch = { },
                onClearSelectedHeroClass = {},
                navigateToItemDetails = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionsScreenPreviewDeviceSizes() {
    MonolithTheme {
        Surface {
            CollectionsScreen(
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
                onFilterHeroes = {},
                onSelectHeroClass = {},
                onSelectItemTier = {},
                onSelectStatFilter = {},
                onSelectTab = {},
                handleRetry = { },
                navigateToHeroDetails = { },
                navigateToSearch = { },
                onClearSelectedHeroClass = {},
                navigateToItemDetails = {},
                navigateToEternalDetails = {}
            )
        }
    }
}

