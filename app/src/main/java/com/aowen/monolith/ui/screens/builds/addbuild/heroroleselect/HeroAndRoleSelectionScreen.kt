package com.aowen.monolith.ui.screens.builds.addbuild.heroroleselect

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.data.HeroImage
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildState
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.screens.builds.addbuild.navigation.navigateToBuildDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.inputFieldDefaults

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
        navigateToBuildDetails = navController::navigateToBuildDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroAndRoleSelectionScreen(
    uiState: AddBuildState,
    onHeroSelected: (Int) -> Unit,
    onRoleSelected: (HeroRole) -> Unit,
    navigateToBuildDetails: () -> Unit
) {

    var isHeroDropdownOpen by remember { mutableStateOf(false) }
    var isRoleDropdownOpen by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Default.ArrowBack,
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
                .padding(16.dp)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isHeroDropdownOpen,
                            onExpandedChange = { isHeroDropdownOpen = !isHeroDropdownOpen }
                        ) {
                            TextField(
                                value = HeroImage.entries.firstOrNull {
                                    it.heroId == uiState.selectedHero
                                }?.heroName ?: "Select a hero",
                                onValueChange = {},
                                readOnly = true,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                trailingIcon = {
                                    AnimatedContent(targetState = isHeroDropdownOpen, label = "") {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                                    }
                                },
                                colors = inputFieldDefaults(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectableGroup(),
                                expanded = isHeroDropdownOpen,
                                onDismissRequest = {
                                    isHeroDropdownOpen = false
                                }
                            ) {
                                HeroImage.entries.forEach { hero ->
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = {
                                            Text(
                                                text = hero.heroName,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            onHeroSelected(hero.heroId)
                                            isHeroDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isRoleDropdownOpen,
                            onExpandedChange = { isRoleDropdownOpen = !isRoleDropdownOpen }
                        ) {
                            TextField(
                                value = HeroRole.entries.firstOrNull {
                                    it == uiState.selectedRole
                                }?.name ?: "Select a role",
                                onValueChange = {},
                                readOnly = true,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                trailingIcon = {
                                    AnimatedContent(targetState = isRoleDropdownOpen, label = "") {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                                    }
                                },
                                colors = inputFieldDefaults(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectableGroup(),
                                expanded = isRoleDropdownOpen,
                                onDismissRequest = {
                                    isRoleDropdownOpen = false
                                }
                            ) {
                                HeroRole.entries.forEach { role ->
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = {
                                            Text(
                                                text = role.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            onRoleSelected(role)
                                            isRoleDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ElevatedButton(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = navigateToBuildDetails
                    ) {
                        Text(text = "Next")
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
fun HeroAndRoleSelectionScreenPreview() {
    MonolithTheme {
        HeroAndRoleSelectionScreen(
            uiState = AddBuildState(),
            onHeroSelected = {},
            onRoleSelected = {},
            navigateToBuildDetails = {}
        )
    }
}