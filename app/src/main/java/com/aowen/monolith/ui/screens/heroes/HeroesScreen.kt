package com.aowen.monolith.ui.screens.heroes

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroImage
import com.aowen.monolith.ui.screens.search.SearchBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HeroesScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HeroesScreenViewModel = hiltViewModel()
) {

    val heroesScreenUiState by viewModel.uiState.collectAsState()

    HeroesScreen(
        modifier = modifier,
        onFilterRole = viewModel::updateRoleOption,
        setSearchValue = viewModel::setSearchValue,
        onFilterHeroes = viewModel::getFilteredHeroes,
        uiState = heroesScreenUiState
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroesScreen(
    uiState: HeroesScreenUiState,
    onFilterRole: (HeroRole, Boolean) -> Unit,
    setSearchValue: (text: String) -> Unit,
    onFilterHeroes: () -> Unit,
    modifier: Modifier = Modifier,
) {

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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SearchBar(
                        searchLabel = "Hero lookup",
                        searchValue = uiState.searchFieldValue,
                        setSearchValue = setSearchValue,
                        modifier = Modifier.weight(1f)
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
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "Roles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HeroRole.values().forEach { role ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = uiState.selectedRoleFilters.contains(role),
                                        onCheckedChange = { isChecked ->
                                            onFilterRole(role, isChecked)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.secondary,
                                        )
                                    )
                                    Text(
                                        text = role.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
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
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.currentHeroes) { hero ->
                        HeroCard(hero = hero)
                    }

                }
            }
        }
    }

}

@Composable
fun HeroCard(
    hero: HeroDetails,
    modifier: Modifier = Modifier
) {

    val image = hero.imageId ?: HeroImage.NARBASH.drawableId
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomCenter,
        propagateMinConstraints = true
    ) {
        Image(
            painter = painterResource(id = image),
            contentScale = ContentScale.FillWidth,
            contentDescription = hero.displayName
        )
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
                style = MaterialTheme.typography.bodyMedium,
                color = WarmWhite
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeroCardPreview() {
    MonolithTheme {
        Surface {
            HeroCard(
                hero = HeroDetails(
                    imageId = HeroImage.NARBASH.drawableId,
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    group = "HeroesScreen"
)
@Composable
fun HeroesScreenPreview() {
    MonolithTheme {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    allHeroes = listOf(
                        HeroDetails(
                            imageId = HeroImage.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = HeroImage.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = HeroImage.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = HeroImage.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    )
                ),
                onFilterRole = { _, _ -> },
                setSearchValue = {},
                onFilterHeroes = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "HeroesScreen"
)
@Composable
fun HeroesScreenPreviewDark() {
    MonolithTheme {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    allHeroes = listOf(
                        HeroDetails(
                            imageId = HeroImage.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = HeroImage.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = HeroImage.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = HeroImage.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    )
                ),
                onFilterRole = { _, _ -> },
                setSearchValue = {},
                onFilterHeroes = {}
            )
        }
    }
}
