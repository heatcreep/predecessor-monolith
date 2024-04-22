package com.aowen.monolith.feature.builds

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.monolith.BuildConfig
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.addbuild.navigation.navigateToAddBuildFlow
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.ui.common.MonolithCollapsableFabButton
import com.aowen.monolith.ui.common.MonolithCollapsableListColumn
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedHighlight
import com.aowen.monolith.ui.theme.dropDownDefaults
import com.aowen.monolith.ui.theme.inputFieldDefaults
import kotlinx.coroutines.launch

@Composable
fun BuildsScreenRoute(
    navController: NavController,
    viewModel: BuildsScreenViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val builds = viewModel.buildsPager.collectAsLazyPagingItems()

    BuildsScreen(
        uiState = uiState,
        builds = builds,
        onSearchFieldUpdate = viewModel::updateSearchField,
        onSelectRoleFilter = viewModel::updateSelectedRole,
        onClearRoleFilter = viewModel::clearSelectedRole,
        onSelectHeroFilter = viewModel::updateSelectedHero,
        onClearHeroFilter = viewModel::clearSelectedHero,
        onSelectSortFilter = viewModel::updateSelectedSortOrder,
        onClearSortFilter = viewModel::clearSelectedSortOrder,
        onCheckHasSkillOrder = viewModel::updateHasSkillOrder,
        onCheckHasModules = viewModel::updateHasModules,
        onCheckHasCurrentVersion = viewModel::updateHasCurrentVersion,
        navigateToSearch = navController::navigateToSearch,
        navigateToBuildDetails = navController::navigateToBuildDetails,
        navigateToAddBuildFlow = navController::navigateToAddBuildFlow
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildsScreen(
    uiState: BuildsUiState,
    builds: LazyPagingItems<BuildListItem>,
    onSearchFieldUpdate: (String) -> Unit,
    onSelectRoleFilter: (String) -> Unit,
    onClearRoleFilter: () -> Unit,
    onSelectHeroFilter: (String) -> Unit,
    onClearHeroFilter: () -> Unit,
    onSelectSortFilter: (String) -> Unit,
    onClearSortFilter: () -> Unit,
    onCheckHasSkillOrder: (Boolean) -> Unit,
    onCheckHasModules: (Boolean) -> Unit,
    onCheckHasCurrentVersion: (Boolean) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToBuildDetails: (Int) -> Unit,
    navigateToAddBuildFlow: () -> Unit
) {


    val scrollState = rememberLazyListState()

    var expanded by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }


    Scaffold(
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            MonolithTopAppBar(
                title = "Builds",
                actions = {
                    IconButton(
                        onClick = {
                            expanded = !expanded
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(rotationAngle.value),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = navigateToSearch) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (BuildConfig.DEBUG) {
                MonolithCollapsableFabButton(
                    listState = scrollState,
                    icon = Icons.Filled.Add,
                    text = "Add Build",
                    onClick = {
                        navigateToAddBuildFlow()
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 16.dp),
        ) {
            MonolithCollapsableListColumn(
                listState = scrollState
            ) {
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Filters:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            FilterDropdown(
                                dropdownTitle = "Role",
                                filterOptions = HeroRole.entries.map { role -> role.name },
                                selectedFilter = uiState.selectedRoleFilter?.name,
                                onSelectOption = onSelectRoleFilter,
                                onClearOption = onClearRoleFilter
                            )
                            FilterDropdown(
                                dropdownTitle = "Hero",
                                filterOptions = Hero.entries.map { hero -> hero.heroName },
                                selectedFilter = uiState.selectedHeroFilter?.heroName,
                                onSelectOption = onSelectHeroFilter,
                                onClearOption = onClearHeroFilter
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterDropdown(
                                dropdownTitle = "Sort by ${uiState.selectedSortOrder}",
                                filterOptions = listOf("Popular", "Trending", "Latest"),
                                selectedFilter = uiState.selectedSortOrder,
                                onSelectOption = onSelectSortFilter,
                                onClearOption = onClearSortFilter
                            )
                        }
                        FilterSwitchWithLabel(
                            label = "Has skill order",
                            isChecked = uiState.hasSkillOrderSelected,
                            onCheckedChange = onCheckHasSkillOrder
                        )
                        FilterSwitchWithLabel(
                            label = "Has modules",
                            isChecked = uiState.hasModulesSelected,
                            onCheckedChange = onCheckHasModules
                        )
                        FilterSwitchWithLabel(
                            label = "Has current version",
                            isChecked = uiState.hasCurrentVersionSelected,
                            onCheckedChange = onCheckHasCurrentVersion
                        )
                        ElevatedButton(
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            onClick = {
                                expanded = false
                            }
                        ) {
                            Text(text = "Close")
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }

                }
            }
            when (builds.loadState.refresh) {
                is LoadState.Loading -> {
                    FullScreenLoadingIndicator()
                }

                is LoadState.Error -> {
                    FullScreenErrorWithRetry(
                        errorMessage = "Failed to load builds"
                    ) {
                        builds.retry()
                    }
                }

                else -> {
                    LazyColumn(
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(builds.itemCount) { index ->
                            val buildListItem = builds[index]
                            buildListItem?.let { build ->
                                BuildListItem(
                                    build = build,
                                    navigateToBuildDetails = navigateToBuildDetails
                                )
                            }
                        }
                        item {
                            when (builds.loadState.append) {
                                is LoadState.Loading -> {
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

                                else -> {}
                            }
                        }
                    }
                }
            }


        }
    }


}

@Composable
fun FilterSwitchWithLabel(
    label: String,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Switch(
            checked = isChecked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedBorderColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedIconColor = MaterialTheme.colorScheme.tertiary
            ),
            onCheckedChange = {
                onCheckedChange(it)
            })
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.FilterDropdown(
    dropdownTitle: String = "Filter",
    filterOptions: List<String>,
    selectedFilter: String?,
    onSelectOption: (String) -> Unit,
    onClearOption: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
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
                value = selectedFilter ?: dropdownTitle,
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
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearOption)
                filterOptions.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(text = filter) },
                        colors = dropDownDefaults(),
                        onClick = {
                            expanded = false
                            onSelectOption(filter)
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun BuildListItem(
    modifier: Modifier = Modifier,
    build: BuildListItem,
    navigateToBuildDetails: (Int) -> Unit
) {

    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(4.dp)
            )
            .clickable {
                navigateToBuildDetails(build.id)

            },
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
                    heroImageId = getHeroRole(build.heroId).drawableId,
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
                            id = getHeroRole(build.role.lowercase()).drawableId
                        ),
                        contentDescription = null
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = build.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Author: ${build.author}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        build.version?.let { version ->
                            Badge(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = version,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
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
                    IconButton(onClick = {
                        // TODO: Replace with actual upvote logic
                        Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show()
                    }
                    ) {
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
                    IconButton(onClick = {
                        // TODO: Replace with actual downvote logic
                        Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show()
                    }) {
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
            ),
            navigateToBuildDetails = {}
        )
    }
}