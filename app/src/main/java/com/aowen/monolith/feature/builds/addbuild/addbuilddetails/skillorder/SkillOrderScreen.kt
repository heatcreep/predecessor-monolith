package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.skillorder

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.R
import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToItemSelect
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun SkillOrderRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    SkillOrderScreen(
        uiState = uiState,
        onSkillSelected = viewModel::onSkillSelected,
        onSkillDetailsSelected = viewModel::onSkillDetailsSelected,
        navigateToItemSelect = navController::navigateToItemSelect,
        navigateBack = navController::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillOrderScreen(
    uiState: AddBuildState,
    onSkillSelected: (Int, Int) -> Unit,
    onSkillDetailsSelected: (AbilityDetails) -> Unit = {},
    navigateToItemSelect: () -> Unit,
    navigateBack: () -> Unit
) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun openSkillBottomSheet(ability: AbilityDetails) {
        onSkillDetailsSelected(ability)
        openBottomSheet = true

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Skill Order",
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
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            if(openBottomSheet && uiState.selectedSkill != null) {
                SkillBottomSheet(
                    sheetState = bottomSheetState,
                    ability = uiState.selectedSkill,
                    closeBottomSheet = closeBottomSheet
                )
            }
            BuildOrderPicker(
                selectedHeroAbilities = uiState.selectedHero!!.abilities,
                skillOrder = uiState.skillOrder,
                onSkillSelected = onSkillSelected,
                openSkillBottomSheet = ::openSkillBottomSheet,
                navigateBack = navigateBack
            )
        }
    }
}

@Composable
fun BuildOrderPicker(
    modifier: Modifier = Modifier,
    selectedHeroAbilities: List<AbilityDetails>,
    skillOrder: List<Int>,
    openSkillBottomSheet: (AbilityDetails) -> Unit,
    onSkillSelected: (Int, Int) -> Unit,
    navigateBack: () -> Unit

) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            Arrangement.SpaceEvenly,
        ) {
            BuilderHeaderRowItem(
                onClick = openSkillBottomSheet,
                ability = selectedHeroAbilities[2],
                text = "Q"
            )
            BuilderHeaderRowItem(
                onClick = openSkillBottomSheet,
                ability = selectedHeroAbilities[3],
                text = "E"
            )
            BuilderHeaderRowItem(
                onClick = openSkillBottomSheet,
                ability = selectedHeroAbilities[4],
                text = "R"
            )
            BuilderHeaderRowItem(
                onClick = openSkillBottomSheet,
                ability = selectedHeroAbilities[1],
                text = "RMB"
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            itemsIndexed(skillOrder) { index, skill ->
                BuildOrderPickerRow(
                    skillListIndex = index,
                    selectedSkill = skill,
                    onSkillSelected = onSkillSelected,
                )
            }
            item {
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = navigateBack
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
fun RowScope.BuilderHeaderRowItem(
    ability: AbilityDetails,
    onClick: (AbilityDetails) -> Unit = {},
    text: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clickable {
                onClick(ability)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val model = ImageRequest.Builder(context)
            .data(ability.image)
            .crossfade(true)
            .placeholder(R.drawable.unknown_ability)
            .build()
        SubcomposeAsyncImage(
            model = model,
            contentDescription = null,
        ) {

            SubcomposeAsyncImageContent(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.secondary,
                        CircleShape
                    ),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )

        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuildOrderPickerRow(
    skillListIndex: Int,
    selectedSkill: Int = -1,
    onSkillSelected: (Int, Int) -> Unit,
) {

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 4
    ) {
        repeat(4) { skillRowIndex ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable {
                        onSkillSelected(skillListIndex, skillRowIndex + 1)
                    }
                    .background(
                        color = if (selectedSkill == skillRowIndex + 1) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedSkill == skillRowIndex + 1) {
                    Text(
                        text = "${skillListIndex + 1}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
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
@LightDarkPreview
fun SkillOrderPickerScreenPreview() {
    MonolithTheme {
        SkillOrderScreen(
            uiState = AddBuildState(
                selectedHero = HeroDetails(
                    abilities = listOf(
                        AbilityDetails(
                            displayName = "Ability 1",
                            image = "https://via.placeholder.com/150",
                            gameDescription = "Ability 1 description",
                            menuDescription = "Ability 1 description",
                            cooldown = listOf(1f, 2f, 3f),
                            cost = listOf(1f, 2f, 3f)
                        ),
                        AbilityDetails(
                            displayName = "Ability 1",
                            image = "https://via.placeholder.com/150",
                            gameDescription = "Ability 1 description",
                            menuDescription = "Ability 1 description",
                            cooldown = listOf(1f, 2f, 3f),
                            cost = listOf(1f, 2f, 3f)
                        ),
                        AbilityDetails(
                            displayName = "Ability 1",
                            image = "https://via.placeholder.com/150",
                            gameDescription = "Ability 1 description",
                            menuDescription = "Ability 1 description",
                            cooldown = listOf(1f, 2f, 3f),
                            cost = listOf(1f, 2f, 3f)
                        ),
                        AbilityDetails(
                            displayName = "Ability 1",
                            image = "https://via.placeholder.com/150",
                            gameDescription = "Ability 1 description",
                            menuDescription = "Ability 1 description",
                            cooldown = listOf(1f, 2f, 3f),
                            cost = listOf(1f, 2f, 3f)
                        ),
                        AbilityDetails(
                            displayName = "Ability 1",
                            image = "https://via.placeholder.com/150",
                            gameDescription = "Ability 1 description",
                            menuDescription = "Ability 1 description",
                            cooldown = listOf(1f, 2f, 3f),
                            cost = listOf(1f, 2f, 3f)
                        ),

                        )
                ),
                skillOrder = listOf(
                    1,
                    2,
                    1,
                    3,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1
                )
            ),
            onSkillSelected = { _, _ -> },
            navigateToItemSelect = { /*TODO*/ },
            navigateBack = { /*TODO*/ })

    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BuildOrderPickerPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)

        ) {
            BuildOrderPicker(
                onSkillSelected = { _, _ -> },
                navigateBack = { },
                selectedHeroAbilities = listOf(
                    AbilityDetails(
                        displayName = "Ability 1",
                        image = "https://via.placeholder.com/150",
                        gameDescription = "Ability 1 description",
                        menuDescription = "Ability 1 description",
                        cooldown = listOf(1f, 2f, 3f),
                        cost = listOf(1f, 2f, 3f)
                    ),
                    AbilityDetails(
                        displayName = "Ability 1",
                        image = "https://via.placeholder.com/150",
                        gameDescription = "Ability 1 description",
                        menuDescription = "Ability 1 description",
                        cooldown = listOf(1f, 2f, 3f),
                        cost = listOf(1f, 2f, 3f)
                    ),
                    AbilityDetails(
                        displayName = "Ability 1",
                        image = "https://via.placeholder.com/150",
                        gameDescription = "Ability 1 description",
                        menuDescription = "Ability 1 description",
                        cooldown = listOf(1f, 2f, 3f),
                        cost = listOf(1f, 2f, 3f)
                    ),
                    AbilityDetails(
                        displayName = "Ability 1",
                        image = "https://via.placeholder.com/150",
                        gameDescription = "Ability 1 description",
                        menuDescription = "Ability 1 description",
                        cooldown = listOf(1f, 2f, 3f),
                        cost = listOf(1f, 2f, 3f)
                    ),
                    AbilityDetails(
                        displayName = "Ability 1",
                        image = "https://via.placeholder.com/150",
                        gameDescription = "Ability 1 description",
                        menuDescription = "Ability 1 description",
                        cooldown = listOf(1f, 2f, 3f),
                        cost = listOf(1f, 2f, 3f)
                    )
                ),
                skillOrder = listOf(
                    1,
                    2,
                    1,
                    3,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1
                ),
                openSkillBottomSheet = { }
            )
        }
    }
}