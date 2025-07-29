package com.aowen.monolith.feature.heroes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.core.ui.filters.PredCompanionChipFilter
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.feature.heroes.herodetails.navigation.navigateToHeroDetails
import com.aowen.monolith.feature.search.navigation.navigateToSearch
import com.aowen.monolith.ui.common.MonolithCollapsableGridColumn
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HeroesScreenRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HeroesScreenViewModel = hiltViewModel()
) {

    val heroesScreenUiState by viewModel.uiState.collectAsState()

    HeroesScreen(
        uiState = heroesScreenUiState,
        handleRetry = viewModel::initViewModel,
        onFilterRole = viewModel::updateRoleOption,
        onFilterHeroes = viewModel::getFilteredHeroes,
        modifier = modifier,
        navigateToHeroDetails = navController::navigateToHeroDetails,
        navigateToSearch = navController::navigateToSearch
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
    navigateToHeroDetails: (heroId: Long, heroName: String) -> Unit = { _, _ -> },
    navigateToSearch: () -> Unit = { }
) {

    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp

    val isTablet = screenWidthDp >= 600

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
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                items(items = HeroRole.entries.dropLast(1)) { role ->
                                    PredCompanionChipFilter(
                                        text = role.roleName,
                                        selected = uiState.selectedRoleFilters.contains(role),
                                        iconRes = role.simpleDrawableId,
                                        onClick = {
                                            onFilterRole(role, !uiState.selectedRoleFilters.contains(role))
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
                                HeroCard(
                                    hero = hero,
                                    onClick = {
                                        navigateToHeroDetails(hero.id, hero.name)
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

@Composable
fun HeroCard(
    hero: HeroDetails,
    modifier: Modifier = Modifier,
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val image = hero.imageId ?: R.drawable.unknown
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.BottomCenter,
        propagateMinConstraints = true
    ) {
        Image(
            painter = painterResource(id = image),
            contentScale = ContentScale.FillWidth,
            contentDescription = hero.displayName
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Column(
                modifier = modifier
                    .aspectRatio(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hero.displayName,
                        style = labelTextStyle,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                        ),
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(top = 20.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = hero.displayName,
                    style = labelTextStyle,
                    color = WarmWhite
                )
            }
        }
    }
}


@Preview(
    group = "Hero Card"
)
@Composable
fun HeroCardPreview() {
    MonolithTheme {
        LazyVerticalGrid(GridCells.Fixed(5)) {
            items(5) {
                HeroCard(
                    hero = HeroDetails(
                        imageId = Hero.NARBASH.drawableId,
                        displayName = "Narbash"
                    ),
                    isSelected = true
                )
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
                            imageId = Hero.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = Hero.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = Hero.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = Hero.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = Hero.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = Hero.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    ),
                    currentHeroes = listOf(
                        HeroDetails(
                            imageId = Hero.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = Hero.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = Hero.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = Hero.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = Hero.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = Hero.GRUX.drawableId,
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
                            imageId = Hero.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = Hero.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = Hero.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = Hero.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = Hero.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = Hero.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    ),
                    currentHeroes = listOf(
                        HeroDetails(
                            imageId = Hero.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = Hero.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = Hero.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = Hero.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = Hero.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = Hero.GRUX.drawableId,
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

