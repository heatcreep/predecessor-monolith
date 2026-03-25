package com.aowen.predcompanion.feature.builds.navigation

import androidx.navigation3.runtime.NavKey
import com.aowen.monolith.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
sealed interface BuildsNavKey : NavKey {

    @Serializable
    data object BuildsList : BuildsNavKey, NavKey

    @Serializable
    data class BuildDetails(val buildId: Int) : BuildsNavKey, NavKey {
    }

    @Serializable
    data object AddBuild : BuildsNavKey, NavKey {
        @Serializable
        data object HeroAndRoleSelect : BuildsNavKey, NavKey

        @Serializable
        data object BuildDetails : BuildsNavKey, NavKey {
            @Serializable
            data object SkillOrderAndModuleMenu : BuildsNavKey, NavKey

            @Serializable
            data object ItemsOverview : BuildsNavKey, NavKey

            @Serializable
            data object SkillOrder : BuildsNavKey, NavKey

            @Serializable
            data class AddModule(val moduleId: String?) : BuildsNavKey, NavKey

            @Serializable
            data object EditModuleOrder : BuildsNavKey, NavKey

            @Serializable
            data object TitleAndDescription : BuildsNavKey, NavKey

            @Serializable
            data class ItemDetailsSelect(
                val buildSection: String,
                val itemType: String,
                val position: Int?
            ) : BuildsNavKey, NavKey
        }
    }
}

fun Navigator.navigateToBuilds() {
    navigate(BuildsNavKey.BuildsList)
}

fun Navigator.navigateToBuildDetails(buildId: Int) {
    navigate(BuildsNavKey.BuildDetails(buildId))
}


// Add Build

fun Navigator.navigateToAddBuild() {
    navigate(BuildsNavKey.AddBuild)
}

fun Navigator.navigateToAddBuildDetails() {
    navigate(BuildsNavKey.AddBuild.BuildDetails)
}

fun Navigator.navigateToItemSelect() {
    navigate(BuildsNavKey.AddBuild.BuildDetails.ItemsOverview)
}

fun Navigator.navigateToSkillOrderSelect() {
    navigate(BuildsNavKey.AddBuild.BuildDetails.SkillOrder)
}

fun Navigator.navigateToAddModule(moduleId: String? = null) {
    navigate(BuildsNavKey.AddBuild.BuildDetails.AddModule(moduleId))
}

fun Navigator.navigateToEditModuleOrder() {
    navigate(BuildsNavKey.AddBuild.BuildDetails.EditModuleOrder)
}

fun Navigator.navigateToTitleAndDescription() {
    navigate(BuildsNavKey.AddBuild.BuildDetails.TitleAndDescription)
}

fun Navigator.navigateToItemDetailsSelect(
    buildSection: String,
    itemType: String,
    position: Int?
) {
    navigate(
        BuildsNavKey.AddBuild.BuildDetails.ItemDetailsSelect(
            buildSection, itemType, position
        )
    )
}