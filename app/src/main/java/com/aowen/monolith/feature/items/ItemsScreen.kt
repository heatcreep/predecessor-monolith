package com.aowen.monolith.feature.items

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.core.ui.dropdown.PredCompanionFilterDropdown
import com.aowen.monolith.core.ui.dropdown.PredCompanionSortDropdown
import com.aowen.monolith.core.ui.filters.PredCompanionChipFilter
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.StatDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.items.itemdetails.navigation.navigateToItemDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.dropDownDefaults
import com.aowen.monolith.ui.theme.inputFieldDefaults
import kotlinx.coroutines.launch

@Composable
fun ItemsScreenRoute(
    navController: NavController,
    viewModel: ItemsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    ItemsScreen(
        uiState = uiState,
        navigateToItemDetails = navController::navigateToItemDetails,
        navigateToSearch = navController::navigateToSearch,
        onSelectTier = viewModel::onSelectTier,
        onSelectStat = viewModel::onSelectStat,
        onSelectHeroClass = viewModel::onSelectHeroClass,
        onClearSelectedHeroClass = viewModel::onClearHeroClass,
        onFilterItems = viewModel::getFilteredItems,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    uiState: ItemsUiState,
    navigateToItemDetails: (String) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    onSelectTier: (String) -> Unit = {},
    onSelectHeroClass: (HeroClass) -> Unit = {},
    onClearSelectedHeroClass: () -> Unit = {},
    onSelectStat: (String) -> Unit = {},
    onFilterItems: () -> Unit = {}
) {

    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp

    val isTablet = screenWidthDp >= 600

    var expanded by rememberSaveable { mutableStateOf(false) }
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

    LaunchedEffect(
        uiState.selectedHeroClass,
        uiState.selectedTierFilter,
        uiState.selectedStatFilters,
        uiState.searchFieldValue
    ) {
        onFilterItems()
    }

    if (uiState.isLoading) {
        FullScreenLoadingIndicator("Items")
    } else {

        if (uiState.itemsError != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.itemsError)
            }
        } else {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    MonolithTopAppBar(
                        title = "Items",
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        item {
                            PredCompanionChipFilter(
                                text = "All",
                                iconRes = R.drawable.all_roles,
                                selected = uiState.selectedHeroClass == null,
                                onClick = onClearSelectedHeroClass
                            )
                        }
                        items(HeroClass.entries.dropLast(1)) {
                            PredCompanionChipFilter(
                                text = it.label,
                                iconRes = it.iconRes,
                                selected = uiState.selectedHeroClass == it,
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
                            selectedOptions = uiState.selectedStatFilters,
                            filterOptions = uiState.allStats,
                            onClickOption = onSelectStat
                        )
                        PredCompanionSortDropdown(
                            selectedOption = uiState.selectedTierFilter ?: "Tier",
                            sortOptions = listOf("Tier I", "Tier II", "Tier III"),
                            onClickOption = onSelectTier
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    if (uiState.filteredItems.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Items list is empty")
                        }
                    } else {
                        LazyVerticalGrid(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(if (isTablet) 5 else 3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.filteredItems) { item ->
                                ItemCard(
                                    itemDetails = item,
                                    onClick = { navigateToItemDetails(item.name) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.TierFilterDropdown(
    selectedTierFilter: String?,
    onSelectTier: (String) -> Unit,
    onClearTierFilter: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val tierFilters = listOf("Tier I", "Tier II", "Tier III")
    Row(
        modifier = Modifier.weight(1f)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = if (!selectedTierFilter.isNullOrEmpty()) selectedTierFilter else "Tier",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(targetState = expanded, label = "") {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .selectableGroup(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tierFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedTierFilter == filter,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary,
                                    ),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = filter)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            expanded = false
                            onSelectTier(filter)
                        }
                    )
                }
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearTierFilter)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.StatFilterDropdown(
    selectedStatFilters: List<String>,
    allStats: List<String>,
    onSelectStat: (String) -> Unit,
    onClearStatsFilters: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier.weight(1f)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = "Stats",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(targetState = expanded, label = "") {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allStats.forEach { stat ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Checkbox(
                                    checked = selectedStatFilters.contains(stat),
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                    )
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = stat)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            onSelectStat(stat)
                        }
                    )
                }
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearStatsFilters)
            }
        }
    }
}

@Composable
fun ItemCard(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
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
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = getItemImage(itemDetails.id)),
                contentDescription = "image for ${itemDetails.displayName}"
            )

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
    name = "Loading",
    group = "No Items/Loading/Error",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemsScreenLoadingPreview() {
    MonolithTheme {
        ItemsScreen(
            uiState = ItemsUiState(
                isLoading = true,
                selectedTierFilter = "Tier 1",
                allStats = listOf("Max Health", "Max Mana", "Health Regen", "Mana Regen"),
                allItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,
                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
            )
        )
    }
}


@Preview(
    showBackground = true,
    name = "No Items",
    group = "No Items/Loading/Error",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemsScreenNoItemsPreview() {
    MonolithTheme {
        ItemsScreen(
            uiState = ItemsUiState(
                isLoading = false,
                selectedTierFilter = "Tier 1",
                allStats = listOf("Max Health", "Max Mana", "Health Regen", "Mana Regen"),
                allItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,
                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
            )
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemsScreenScreenSizePreview() {
    MonolithTheme {
        ItemsScreen(
            uiState = ItemsUiState(
                isLoading = false,
                selectedTierFilter = "Tier 1",
                allStats = listOf("Max Health", "Max Mana", "Health Regen", "Mana Regen"),
                allItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 2,
                        displayName = "Witchstalker",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 3,
                        displayName = "Item 3",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 4,
                        displayName = "Item 4",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 5,
                        displayName = "Item 5",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 6,
                        displayName = "Item 6",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
                filteredItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 2,
                        displayName = "The Witchstalker",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 3,
                        displayName = "Item 3",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 4,
                        displayName = "Item 4",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 5,
                        displayName = "Item 5",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    ),
                    ItemDetails(
                        id = 6,
                        displayName = "Item 6",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
            )

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsScreenPreview() {
    MonolithTheme {
        ItemsScreen(
            uiState = ItemsUiState(
                isLoading = false,
                selectedTierFilter = "Tier 1",
                allStats = listOf("Max Health", "Max Mana", "Health Regen", "Mana Regen"),
                allItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
                filteredItems = listOf(
                    ItemDetails(
                        id = 1,
                        displayName = "Ashbringer",
                        heroClass = HeroClass.FIGHTER,

                        stats = listOf(
                            StatDetails(
                                name = "Max Health",
                                value = "400",
                                iconId = R.drawable.max_health
                            ),
                        )
                    )
                ),
            )

        )
    }
}