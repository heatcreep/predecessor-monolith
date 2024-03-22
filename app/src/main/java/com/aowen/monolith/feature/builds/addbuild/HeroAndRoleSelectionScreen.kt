package com.aowen.monolith.feature.builds.addbuild

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.feature.builds.addbuild.navigation.navigateToAddBuildDetails
import com.aowen.monolith.feature.heroes.HeroCard
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import java.util.Locale

@Composable
fun HeroAndRoleSelectionRoute(
    navController: NavController,
    viewModel: AddBuildViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()

    HeroAndRoleSelectionScreen(
        uiState = uiState,
        onHeroSelected = viewModel::onHeroSelected,
        onRoleSelected = viewModel::onRoleSelected,
        navigateToBuildDetails = navController::navigateToAddBuildDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroAndRoleSelectionScreen(
    uiState: AddBuildState,
    onHeroSelected: (HeroDetails) -> Unit,
    onRoleSelected: (HeroRole) -> Unit,
    navigateToBuildDetails: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add New Build",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
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
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Select a Role:")
                RoleSelection(
                    selectedRole = uiState.selectedRole,
                    onRoleSelected = onRoleSelected
                )
                Text(text = "Select a Hero:")
                if (uiState.isLoadingHeroes) {
                    FullScreenLoadingIndicator("Heroes")
                } else {
                    HeroSelection(
                        modifier = Modifier.weight(1f),
                        selectedHero = uiState.selectedHero,
                        heroes = uiState.heroes,
                        onHeroSelected = onHeroSelected
                    )
                }
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedHero != null && uiState.selectedRole != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = { navigateToBuildDetails() }
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
fun RoleSelection(
    modifier: Modifier = Modifier,
    selectedRole: HeroRole? = HeroRole.Unknown,
    onRoleSelected: (HeroRole) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeroRole.entries.filter {
            it != HeroRole.Unknown

        }.forEach {
            RoleButton(
                modifier = Modifier.weight(1f),
                isSelected = it.roleName == selectedRole?.name?.lowercase(),
                role = it,
                label = it.roleName,
                onClick = { onRoleSelected(HeroRole.valueOf(it.name)) }
            )
        }
    }
}

@Composable
fun HeroSelection(
    modifier: Modifier = Modifier,
    selectedHero: HeroDetails? = null,
    heroes: List<HeroDetails> = emptyList(),
    onHeroSelected: (HeroDetails) -> Unit
) {

    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = listState,
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(heroes) { hero ->
            HeroCard(
                hero = hero,
                isSelected = hero.id == selectedHero?.id,
                labelTextStyle = MaterialTheme.typography.bodySmall,
                onClick = { onHeroSelected(hero) }
            )

        }
    }
}

@Composable
fun RoleButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    role: HeroRole,
    label: String,
    onClick: () -> Unit
) {

    IconButton(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        onClick = onClick,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = role.drawableId),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentDescription = role.roleName
            )
            Text(
                text = label.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }
    }


}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HeroAndRoleSelectionScreenPreview() {
    MonolithTheme {
        HeroAndRoleSelectionScreen(
            uiState = AddBuildState(
                isLoadingHeroes = false,
                heroes = listOf(
                    HeroDetails(id = 3, imageId = Hero.NARBASH.drawableId, displayName = "Narbash"),
                    HeroDetails(
                        id = 4,
                        imageId = Hero.BELICA.drawableId,
                        displayName = "Lt. Belica"
                    ),
                    HeroDetails(
                        id = 5,
                        imageId = Hero.COUNTESS.drawableId,
                        displayName = "Countess"
                    ),
                ),
                selectedRole = HeroRole.Carry,
                selectedHero = HeroDetails(id = 3)
            ),
            onHeroSelected = {},
            onRoleSelected = {},
            navigateToBuildDetails = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RoleSelectionPreview() {
    MonolithTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                RoleSelection(
                    selectedRole = HeroRole.Jungle
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@LightDarkPreview
@Composable
fun HeroSelectionPreview() {
    MonolithTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                HeroSelection(
                    selectedHero = HeroDetails(id = 3),
                    heroes = listOf(
                        HeroDetails(
                            id = 3,
                            imageId = Hero.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            id = 4,
                            imageId = Hero.BELICA.drawableId,
                            displayName = "Lt. Belica"
                        ),
                        HeroDetails(
                            id = 5,
                            imageId = Hero.COUNTESS.drawableId,
                            displayName = "Countess"
                        ),
                    ),
                ) {

                }
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}