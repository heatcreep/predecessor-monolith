package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.SlotType
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.CrestGroupDetails
import com.aowen.monolith.feature.builds.addbuild.CrestType
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.itemselect.navigation.ItemType
import com.aowen.monolith.feature.items.StatFilterDropdown
import com.aowen.monolith.feature.items.TierFilterDropdown
import com.aowen.monolith.feature.items.defaultStats
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsBottomSheet
import com.aowen.monolith.feature.search.SearchBar
import com.aowen.monolith.ui.common.MonolithCollapsableGridColumn
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import com.aowen.monolith.ui.utils.filterOrOriginal
import kotlinx.coroutines.launch

@Composable
fun ItemSelectListRoute(
    itemType: ItemType,
    itemPosition: Int? = null,
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    when (itemType) {
        ItemType.Crest -> CrestSelectListScreen(
            crestGroups = uiState.crestsList,
            onCrestSelected = viewModel::onCrestSelected,
            navigateBack = navController::navigateUp
        )

        ItemType.Item -> ItemSelectListScreen(
            allItems = uiState.items,
            itemPosition = itemPosition,
            onAddSelectedItem = viewModel::onAddSelectedItem,
            onReplaceSelectedItem = viewModel::onReplaceSelectedItem,
            onSelectStatFilter = viewModel::onSelectStatFilter,
            selectedStatFilters = uiState.selectedStatFilters,
            onClearStatFilters = viewModel::onClearStatFilters,
            navigateBack = navController::navigateUp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrestSelectListScreen(
    crestGroups: List<CrestGroupDetails>,
    onCrestSelected: (ItemDetails) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Crest",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(crestGroups) { crestGroup ->
                    CrestGroup(
                        crestGroup = crestGroup,
                        onCrestSelected = onCrestSelected,
                        navigateBack = navigateBack
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun CrestGroup(
    modifier: Modifier = Modifier,
    crestGroup: CrestGroupDetails = CrestGroupDetails(
        crestType = CrestType.Support,
        finalCrests = listOf(
            ItemDetails(
                id = 34,
                displayName = "Leafsong"
            ),
            ItemDetails(
                id = 36,
                displayName = "Rift Walkers"
            ),
            ItemDetails(
                id = 37,
                displayName = "Sanctification"
            ),
        ),
        baseCrest = ItemDetails(
            id = 32,
            displayName = "Guardian Crest"
        ),
        secondCrest = ItemDetails(
            id = 40,
            displayName = "Warden Crest"
        )


    ),
    onCrestSelected: (ItemDetails) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${crestGroup.crestType.name} Crests",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )
            Row {
                crestGroup.baseCrest?.let { crest ->
                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = getItemImage(crest.id)),
                            contentDescription = crest.displayName
                        )
                    }
                }
                crestGroup.secondCrest?.let { crest ->
                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Image(
                            painter = painterResource(id = getItemImage(crest.id)),
                            contentDescription = crest.displayName
                        )
                    }
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            crestGroup.finalCrests.forEach { crest ->
                IconButton(
                    modifier = Modifier.size(96.dp),
                    onClick = {
                        onCrestSelected(crest)
                        navigateBack()
                    }
                ) {
                    Image(
                        modifier = Modifier.size(96.dp),
                        painter = painterResource(id = getItemImage(crest.id)),
                        contentDescription = crest.displayName
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@LightDarkPreview
@Composable
fun ItemSelectListScreen(
    allItems: List<ItemDetails> = emptyList(),
    itemPosition: Int? = null,
    onAddSelectedItem: (ItemDetails) -> Unit = {},
    onReplaceSelectedItem: (ItemDetails, Int) -> Unit = { _, _ -> },
    selectedStatFilters: List<String> = emptyList(),
    onSelectStatFilter: (String) -> Unit = {},
    onClearStatFilters: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {

    var itemDetailsBottomSheetOpen by remember { mutableStateOf(false) }
    var currentlyOpenItem by remember { mutableStateOf<ItemDetails?>(null) }

    val listState = rememberLazyGridState()
    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    var selectedTierFilter by rememberSaveable { mutableStateOf("") }

    val closeBottomSheet = { itemDetailsBottomSheetOpen = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    fun getFilteredItems() : List<ItemDetails> {
        val itemsByTier =
            allItems.filterOrOriginal {
                it.rarity.value == selectedTierFilter
            }


        val itemsByStats = itemsByTier.filter { item ->
            selectedStatFilters.all { listItem ->
                item.stats.map { it.name }.contains(listItem)
            }
        }

        val itemsBySearch = itemsByStats.filter { item ->
            item.displayName.contains(searchFieldValue, ignoreCase = true)
        }

        return itemsBySearch
    }

    if (itemDetailsBottomSheetOpen && currentlyOpenItem != null) {
        currentlyOpenItem?.let {
            ItemDetailsBottomSheet(
                itemDetails = it,
                sheetState = bottomSheetState,
                closeBottomSheet = closeBottomSheet
            )
        }
    }

    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Item",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                MonolithCollapsableGridColumn(listState = listState) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchBar(
                            searchLabel = "Item lookup",
                            searchValue = searchFieldValue,
                            setSearchValue = {
                                searchFieldValue = it
                            },
                            modifier = Modifier.weight(1f),
                            handleClearSearch = { searchFieldValue = "" }
                        )
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .rotate(rotationAngle.value),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Filters:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TierFilterDropdown(
                                    selectedTierFilter = selectedTierFilter,
                                    onSelectTier = {
                                        selectedTierFilter = it
                                    },
                                    onClearTierFilter = {
                                        selectedTierFilter = ""
                                    }
                                )
                                StatFilterDropdown(
                                    allStats = defaultStats,
                                    selectedStatFilters = selectedStatFilters,
                                    onSelectStat = onSelectStatFilter,
                                    onClearStatsFilters = onClearStatFilters
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }

                }
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(getFilteredItems()
                        .filter { it.slotType != SlotType.CREST }
                    ) {
                        ItemSelectListItem(
                            item = it,
                            onItemClicked = { itemDetails ->
                                if(itemPosition != null) {
                                    onReplaceSelectedItem(itemDetails, itemPosition)
                                } else {
                                    onAddSelectedItem(itemDetails)
                                }
                                navigateBack()
                            },
                            onItemLongPressed = {
                                currentlyOpenItem = it
                                itemDetailsBottomSheetOpen = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemSelectListItem(
    item: ItemDetails,
    onItemClicked: (ItemDetails) -> Unit = {},
    onItemLongPressed: (ItemDetails) -> Unit = {},
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .combinedClickable(
                    onLongClick = { onItemLongPressed(item) },
                    onClick = { onItemClicked(item) }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = getItemImage(item.id)),
                contentDescription = item.displayName,
                modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = item.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}