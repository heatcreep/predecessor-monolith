package com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.feature.builds.addbuild.AddBuildViewModel
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.ModuleAddRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.ModuleEditOrderRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.SkillOrderAndModuleSelectRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.itemselect.ItemSelectListRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.itemselect.ItemsOverviewRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.skillorder.SkillOrderRoute
import com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription.TitleAndDescriptionRoute
import com.aowen.predcompanion.feature.builds.api.navigation.BuildSection
import com.aowen.predcompanion.feature.builds.api.navigation.BuildsNavKey
import com.aowen.predcompanion.feature.builds.api.navigation.ItemType
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToAddModule
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToEditModuleOrder
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToItemDetailsSelect
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToItemSelect
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToSkillOrderSelect
import com.aowen.predcompanion.feature.builds.api.navigation.navigateToTitleAndDescription
import com.aowen.predcompanion.navigation.Navigator

@Composable
fun AddBuildDetailsNavigation(
    viewModel: AddBuildViewModel,
    navigator: Navigator
) {
    NavDisplay(
        backStack = rememberNavBackStack(BuildsNavKey.AddBuild.BuildDetails),
        entryProvider = entryProvider {
            entry<BuildsNavKey.AddBuild.BuildDetails.SkillOrderAndModuleMenu> {
                SkillOrderAndModuleSelectRoute(
                    viewModel = viewModel,
                    navigateBack = navigator::goBack,
                    navigateToItemSelect = navigator::navigateToItemSelect,
                    navigateToSkillOrderSelect = navigator::navigateToSkillOrderSelect,
                    navigateToAddModule = navigator::navigateToAddModule,
                    navigateToEditModuleOrder = navigator::navigateToEditModuleOrder,
                    navigateToEditTitleAndDescription = navigator::navigateToTitleAndDescription,
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.ItemsOverview> {
                ItemsOverviewRoute(
                    viewModel = viewModel,
                    navigateBack = navigator::goBack,
                    navigateToItemDetailsSelect = navigator::navigateToItemDetailsSelect
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.ItemDetailsSelect> {
                ItemSelectListRoute(
                    buildSection = BuildSection.valueOf(it.buildSection),
                    itemType = ItemType.valueOf(it.itemType),
                    itemPosition = it.position,
                    viewModel = viewModel,
                    navigateBack = navigator::goBack
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.SkillOrder> {
                SkillOrderRoute(
                    viewModel = viewModel,
                    navigateBack = navigator::goBack
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.AddModule> {
                ModuleAddRoute(
                    viewModel = viewModel,
                    moduleId = it.moduleId,
                    navigateBack = navigator::goBack,
                    navigateToItemDetailsSelect = navigator::navigateToItemDetailsSelect
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.EditModuleOrder> {
                ModuleEditOrderRoute(
                    viewModel = viewModel,
                    navigateBack = navigator::goBack,
                    navigateToAddModule = navigator::navigateToAddModule
                )
            }
            entry<BuildsNavKey.AddBuild.BuildDetails.TitleAndDescription> {
                TitleAndDescriptionRoute(
                    viewModel = viewModel,
                    navigateBack = navigator::goBack
                )
            }
        }
    )
}
