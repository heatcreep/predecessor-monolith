package com.aowen.predcompanion.feature.builds.addbuild.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.aowen.monolith.navigation.Navigator
import com.aowen.predcompanion.feature.heroes.addbuild.AddBuildViewModel
import com.aowen.predcompanion.feature.builds.addbuild.HeroAndRoleSelectionRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.navigation.AddBuildDetailsNavigation
import com.aowen.predcompanion.feature.heroes.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.heroes.navigation.navigateToAddBuildDetails
import java.util.Map.entry

@Composable
fun AddBuildNavigation(
    navigator: Navigator,
    addBuildViewModel: AddBuildViewModel = hiltViewModel<AddBuildViewModel>()
) {
    NavDisplay(
        backStack = rememberNavBackStack(BuildsNavKey.AddBuild),
        entryProvider = entryProvider {
            entry<BuildsNavKey.AddBuild.HeroAndRoleSelect> {
                _root_ide_package_.com.aowen.predcompanion.feature.builds.addbuild.HeroAndRoleSelectionRoute(
                    viewModel = addBuildViewModel,
                    navigateBack = navigator::goBack,
                    navigateToAddBuildDetails = navigator::navigateToAddBuildDetails
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails> {
                _root_ide_package_.com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.navigation.AddBuildDetailsNavigation(
                    viewModel = addBuildViewModel,
                    navigator = navigator
                )
            }
        }
    )
}
