package com.aowen.monolith.ui.screens.builds.builddetails

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.data.getRoleImage
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.screens.matches.ItemDetailsBottomSheet
import com.aowen.monolith.ui.theme.MonolithTheme
import com.meetup.twain.MarkdownText

@Composable
fun BuildDetailsRoute(
    viewModel: BuildDetailsScreenViewModel = hiltViewModel()
) {


    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initViewModel()
    }

    BuildDetailsScreen(
        uiState = uiState,
        onItemClicked = viewModel::onItemClicked
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BuildDetailsScreen(
    uiState: BuildDetailsUiState,
    onItemClicked: (Int) -> Unit
) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val closeBottomSheet = { openBottomSheet = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    fun openItemDetailsBottomSheet(itemId: Int) {
        onItemClicked(itemId)
        openBottomSheet = true
    }

    if (openBottomSheet && uiState.selectedItemDetails != null) {
        ItemDetailsBottomSheet(
            closeBottomSheet = closeBottomSheet,
            sheetState = bottomSheetState,
            itemDetails = uiState.selectedItemDetails
        )
    }

    if (uiState.isLoading) {
        FullScreenLoadingIndicator("Build Details")
    } else {
        if (uiState.error != null || uiState.buildDetails == null) {
            FullScreenErrorWithRetry {

            }
        } else {
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    // Title Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerCardWithRole(
                            buildDetails = uiState.buildDetails
                        )
                        Column {
                            Text(
                                text = uiState.buildDetails.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "Author: ${uiState.buildDetails.author}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = "Role: ${uiState.buildDetails.role}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    // Item Row
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    openItemDetailsBottomSheet(uiState.buildDetails.crest)
                                },
                        ) {
                            Image(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(id = getItemImage(uiState.buildDetails.crest)),
                                contentDescription = null
                            )
                        }
                        uiState.buildDetails.buildItems.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        openItemDetailsBottomSheet(item)
                                    },
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(48.dp),
                                    painter = painterResource(id = getItemImage(item)),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    // Skill Order Row
                    Text(text = "Skill Order", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.size(8.dp))
                    uiState.buildDetails.skillOrder?.let {
                        SkillOrderScrollableRow(skillOrder = it)
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    // Modules Section
                    uiState.buildDetails.modules.forEach { module ->
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            module.items.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .clickable {
                                            openItemDetailsBottomSheet(item)
                                        },
                                ) {
                                    Image(
                                        modifier = Modifier.size(48.dp),
                                        painter = painterResource(id = getItemImage(item)),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        if (module != uiState.buildDetails.modules.last()) {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    // Notes Row
                    uiState.buildDetails.description?.let {
                        Text(text = "Notes", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.size(8.dp))
                        MarkdownText(
                            markdown = it
                                .replace("\\n", "\n")
                                .replace("# ", "## ")
                                .replace("_________________________________", "")
                                .replace("---", ""),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }


}

@Composable
fun PlayerCardWithRole(
    buildDetails: BuildListItem
) {
    PlayerIcon(
        heroImageId = getHeroImage(buildDetails.heroId).drawableId,
        heroIconSize = 64.dp,
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
                id = getRoleImage(buildDetails.role).drawableId
            ),
            contentDescription = null
        )
    }
}

@Composable
fun SkillOrderScrollableRow(skillOrder: List<Int>) {

    val scrollState = rememberLazyListState()

    fun LazyListState.isScrolledToEnd(): Boolean {
        return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
    }

    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    var rowHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                rowHeight = with(density) { coordinates.size.height.toDp() }
            }) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SkillOrderHeader("Q")
                SkillOrderHeader("E")
                SkillOrderHeader("R")
                SkillOrderHeader("RMB")
            }
            Spacer(modifier = Modifier.size(4.dp))
            LazyRow(
                state = scrollState,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                itemsIndexed(skillOrder) { index, skill ->
                    SkillLevelColumn(
                        selectedSkill = skill,
                        selectedSkillLevel = index + 1
                    )

                }
            }
        }
        AnimatedVisibility(
            visible = !endOfListReached,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(rowHeight)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primary
                            ),
                            startX = 500f,
                            endX = Float.POSITIVE_INFINITY + 1000f
                        )
                    )
            )
        }

    }

}

@Composable
fun SkillOrderHeader(
    input: String
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = input,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SkillLevelColumn(
    selectedSkill: Int,
    selectedSkillLevel: Int = 1
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val skillOrder = listOf(1, 2, 3, 4)
        skillOrder.forEach { skillChoice ->
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (skillChoice == selectedSkill) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
            ) {
                if (skillChoice == selectedSkill) {
                    Text(
                        text = "$selectedSkillLevel",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.Center)
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
fun BuildDetailsScreenPreview() {
    MonolithTheme {
        BuildDetailsScreen(
            uiState = BuildDetailsUiState(
                buildDetails = BuildListItem(
                    id = 1,
                    title = "Narbash Test Build",
                    author = "heatcreep.tv",
                    role = "Support",
                    description = """
                        # Core Items\n\nFIRST we add ***\"Crystal Tear\"*** with very good base stats and a passive ability that works great on Narbash and boosts healing again.\nSo after ***\"Crystal Tear\"*** you go to ***\"Violet Brooch\"*** and finish building ***\"Truesilver\"***. From ***\"Truesilver\"*** onwards we are a bit safer against CC in the ult for the first time, which we should already have at level 6.  The third item is ***\"Wellspring\"*** every time we use our abilities or mostly the healing , we heal from Wellspring again. We also manage the passive of the item quite well as Narbash, because we also stack our own passive through auto attacks.\n\n*If you have problems with the of Mana then you can also build \"Requiem\" first to stack it quite early*\n___________________________________\n\n# Mid/Late game Items (Flex)\n\nThen first situationally build ***\"Tainted Totem\"*** situationally as a strong anti-healing agent in group fights.\nIf I'm playing with a carry that relies on attack speed or builds more towards attack speed, then I build ***\"Marshall\"*** earlier. You either have to do the carry according to whether he needs or wants the extra attack speed or you look at what kind of build the carry builds, early items etc. We can also build ***\"Windcaller\"*** for a little more mobility and a little more heal power\n___________________________________\n\n# Situational items \n***\"Frosted Lure\"*** we can build to get a shield during the ult after the passive of the item is used\nDepending on the situation, we can now build ***\"Stonewall\"*** against Physical Dmg on you or we can build ***\"Void Helm\"*** against Magical Dmg.\n**\"Frost Guardian\"*** Depending on the situation, opponents should have a lot of atk speed heroes and have a strong focus on you, then you can build the item. \n___________________________________\n\n# Crest\n\nCrest is ***\"Santification\"*** through the maximum life we have, we give our mates and ourselves up to 700 shields. We can also build ***\"Leafsong\"*** depending on the situation to have more mobility as a team\n___________________________________\n\n# other Crest Situational\n\nDepending on the situation, we can build ***\"Silentium\"*** for a little more CC or ***\"Rift Walkers\"*** to have a little engage or disengage or ***\"Reclamation\"*** to cleanse you and your Teammates. from CC.\n___________________________________\n\n\n
                    """.trimIndent(),
                    heroId = 16,
                    crest = 37,
                    buildItems = listOf(117, 172, 112, 154, 199),
                    skillOrder = listOf(1, 4, 3, 2, 1, 1, 1, 2, 3, 4, 3, 2, 3, 2, 2, 2, 2, 1),
                    upvotes = 0,
                    downvotes = 0,
                    modules = listOf(
                        ItemModule(
                            title = "\\\\\\ Core Items ///",
                            items = listOf(
                                172,
                                117
                            )
                        ),
                        ItemModule(
                            title = "First Core Item",
                            items = listOf(
                                117
                            )
                        )
                    ),
                    createdAt = "Test",
                    updatedAt = "Test",
                )
            ),
            onItemClicked = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SkillOrderScrollableRowPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            SkillOrderScrollableRow(
                skillOrder = listOf(4, 2, 1, 4, 3, 2, 2, 1, 4, 1, 1, 1, 1)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SkillLevelColumnPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            SkillLevelColumn(
                selectedSkill = 1
            )
        }
    }
}