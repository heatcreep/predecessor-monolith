package com.aowen.monolith.feature.builds

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aowen.monolith.BuildConfig
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.core.ui.cards.builds.BuildListCard
import com.aowen.monolith.core.ui.dropdown.HeroSelectDropdown
import com.aowen.monolith.core.ui.dropdown.PredCompanionSortDropdown
import com.aowen.monolith.core.ui.filters.PredCompanionChipFilter
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.feature.builds.addbuild.navigation.navigateToAddBuildFlow
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.ui.common.MonolithCollapsableFabButton
import com.aowen.monolith.ui.common.MonolithCollapsableListColumn
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.model.BuildUiListItem
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
        onSelectRoleFilter = viewModel::updateSelectedRole,
        onClearRoleFilter = viewModel::clearSelectedRole,
        onSelectHeroFilter = viewModel::updateSelectedHero,
        onClearHeroFilter = viewModel::clearSelectedHero,
        onSelectSortFilter = viewModel::updateSelectedSortOrder,
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
    builds: LazyPagingItems<BuildUiListItem>,
    onSelectRoleFilter: (HeroRole) -> Unit,
    onClearRoleFilter: () -> Unit,
    onSelectHeroFilter: (String) -> Unit,
    onClearHeroFilter: () -> Unit,
    onSelectSortFilter: (String) -> Unit,
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            MonolithTopAppBar(
                title = "Builds",
                titleSuffix = {
                    Row(
                        modifier = Modifier
                    ) {
                        HeroSelectDropdown(
                            modifier = Modifier.weight(1f),
                            heroName = uiState.selectedHeroFilter?.heroName ?: "All Heroes",
                            heroes = uiState.allHeroes,
                            heroImageId = uiState.selectedHeroFilter?.drawableId,
                            onSelect = onSelectHeroFilter
                        )
                        AnimatedVisibility(visible = uiState.selectedHeroFilter != null) {
                            IconButton(onClick = onClearHeroFilter) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = "Clear Hero Filter",
                                )
                            }
                        }
                    }

                },
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
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                item {
                    PredCompanionChipFilter(
                        text = "All",
                        iconRes = R.drawable.all_roles,
                        selected = uiState.selectedRoleFilter == null,
                        onClick = onClearRoleFilter
                    )
                }
                items(HeroRole.entries.dropLast(1)) { role ->
                    PredCompanionChipFilter(
                        text = role.roleName,
                        iconRes = role.simpleDrawableId,
                        selected = uiState.selectedRoleFilter == role,
                        onClick = { onSelectRoleFilter(role) }
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                PredCompanionSortDropdown(
                    selectedOption = uiState.selectedSortOrder,
                    sortOptions = listOf("Popular", "Trending", "Latest"),
                    onClickOption = onSelectSortFilter
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(builds.itemCount) { index ->
                            val buildListItem = builds[index]
                            buildListItem?.let { build ->
                                BuildListCard(
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
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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