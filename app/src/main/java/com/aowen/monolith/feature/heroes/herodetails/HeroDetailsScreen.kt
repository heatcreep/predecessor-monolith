package com.aowen.monolith.feature.heroes.herodetails

import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.getAbilityKey
import com.aowen.monolith.data.getAbilityName
import com.aowen.monolith.data.getHeroRole
import com.aowen.monolith.feature.builds.BuildListItem
import com.aowen.monolith.feature.builds.builddetails.navigation.navigateToBuildDetails
import com.aowen.monolith.feature.heroes.herodetails.preview.heroBuildsLoadingState
import com.aowen.monolith.feature.heroes.herodetails.preview.heroBuildsState
import com.aowen.monolith.ui.common.PlayerIcon
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.components.SpiderChart
import com.aowen.monolith.ui.theme.BadgeBlueGreen
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.NeroBlack
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import kotlinx.coroutines.launch

class SampleHeroDetailsScreenUiStateProvider :
    CollectionPreviewParameterProvider<HeroDetailsUiState>(
        collection = listOf(
            heroBuildsLoadingState,
            heroBuildsState
        )
    )

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroDetailsRoute(
    navController: NavController,
    viewModel: HeroDetailsViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    val console by viewModel.console.collectAsState()

    // Tab State
    val tabs = listOf("Overview", "Stats", "Abilities")
    val pageCount = tabs.size

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = 0,
    )


    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Hero Details",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (uiState.isLoading) {
                FullScreenLoadingIndicator("Hero Details")
            } else {
                when (uiState.heroDetailsErrors) {
                    is HeroDetailsError.HeroErrorMessage,
                    is HeroDetailsError.StatisticsErrorMessage -> {
                        val errorMessage = uiState.heroDetailsErrors?.errorMessage
                        FullScreenErrorWithRetry(
                            errorMessage = errorMessage
                        ) {
                            viewModel.initViewModel()
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TabRow(
                                selectedTabIndex = pagerState.currentPage,
                                indicator = { tabPositions ->
                                    TabRowDefaults.SecondaryIndicator(
                                        Modifier.tabIndicatorOffset(
                                            tabPositions[pagerState.currentPage]
                                        ),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, tab ->
                                    Tab(
                                        text = { Text(text = tab) },
                                        unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                                        selectedContentColor = MaterialTheme.colorScheme.secondary,
                                        selected = pagerState.currentPage == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    )
                                }
                            }
                            HorizontalPager(
                                modifier = Modifier.fillMaxWidth(),
                                state = pagerState
                            ) { page ->
                                when (page) {
                                    0 -> HeroOverviewScreen(
                                        uiState = uiState,
                                        navigateToBuildDetails = navController::navigateToBuildDetails
                                    )

                                    1 -> HeroStatsScreen(
                                        uiState = uiState
                                    )

                                    2 -> HeroAbilitiesScreen(
                                        uiState = uiState,
                                        console = console
                                    )
                                }
                            }

                        }

                    }
                }
            }
        }
    }
}


@Composable
fun HeroOverviewScreen(
    uiState: HeroDetailsUiState,
    modifier: Modifier = Modifier,
    navigateToBuildDetails: (Int) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerIcon(
                    heroImageId = getHeroRole(uiState.hero.id).drawableId,
                    heroIconSize = 64.dp
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = uiState.hero.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

            }
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                HeroRoleChips(uiState.hero.roles)
                Spacer(modifier = Modifier.size(4.dp))
                HeroClassChips(uiState.hero.classes)
            }

            Spacer(modifier = Modifier.size(32.dp))
            SpiderChart(
                statPoints = uiState.hero.stats,
            )
            Spacer(modifier = Modifier.size(32.dp))
            HeroStatsRateBar(
                title = "Win Rate",
                rate = uiState.statistics.winRate
            )
            Spacer(modifier = Modifier.size(16.dp))
            HeroStatsRateBar(
                title = "Pick Rate",
                rate = uiState.statistics.pickRate
            )
            Spacer(modifier = Modifier.size(32.dp))
            Text(
                text = "Popular Builds",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.size(16.dp))
            AnimatedContent(targetState = uiState.isLoadingBuilds, label = "") { loadingBuilds ->
                if (loadingBuilds) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PlayerLoadingCard(
                            avatarSize = 64.dp,
                            titleHeight = 16.dp,
                            subtitleHeight = 12.dp,
                            titleWidth = 100.dp,
                            subtitleWidth = 200.dp,
                        )
                    }
                } else {
                    if (uiState.heroDetailsErrors is HeroDetailsError.HeroBuildsErrorMessage) {
                        Text(
                            text = uiState.heroDetailsErrors.errorMessage
                                ?: "Failed to fetch hero builds.",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else if (uiState.heroBuilds.isEmpty()) {
                        Text(
                            text = "No builds found.",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            uiState.heroBuilds.forEach { build ->
                                BuildListItem(
                                    build = build,
                                    navigateToBuildDetails = navigateToBuildDetails
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
fun HeroStatsRateBar(
    modifier: Modifier = Modifier,
    title: String,
    rate: Float
) {

    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animated $title progress"
    )
    Text(
        text = "$title ${rate.toInt()}%",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary
    )
    Spacer(modifier = modifier.size(4.dp))
    LinearProgressIndicator(
        modifier = Modifier
            .height(16.dp)
            .fillMaxWidth(),
        progress = { animatedProgress },
        trackColor = MaterialTheme.colorScheme.primaryContainer,
        color = MaterialTheme.colorScheme.secondary,
        strokeCap = StrokeCap.Butt,
    )
    LaunchedEffect(rate) {
        progress = rate / 100
    }
}

@Composable
fun HeroStatsScreen(
    uiState: HeroDetailsUiState
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var sliderValue by rememberSaveable { mutableFloatStateOf(0f) }

        Text(
            text = "Player Stats",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level ${sliderValue.toInt() + 1}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.size(16.dp))
            Slider(
                modifier = Modifier.fillMaxWidth(1f),
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..17f,
                steps = 18,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                    thumbColor = MaterialTheme.colorScheme.secondary,
                ),
            )
        }

        // Max Health
        StatRow(
            statName = "Max Health",
            painterId = R.drawable.max_health,
            statValue = uiState.hero.baseStats.maxHealth[sliderValue.toInt()].toString()
        )
        // Health Regen
        StatRow(
            statName = "Health Regeneration",
            painterId = R.drawable.health_regen,
            statValue = uiState.hero.baseStats.healthRegen[sliderValue.toInt()].toString()
        )
        // Max Mana
        StatRow(
            statName = "Max Mana",
            painterId = R.drawable.max_mana,
            statValue = uiState.hero.baseStats.maxMana[sliderValue.toInt()].toString()
        )
        // Mana Regen
        StatRow(
            statName = "Mana Regeneration",
            painterId = R.drawable.mana_regen,
            statValue = uiState.hero.baseStats.manaRegen[sliderValue.toInt()].toString()
        )
        // Attack Speed
        StatRow(
            statName = "Attack Speed",
            painterId = R.drawable.attack_speed,
            statValue = uiState.hero.baseStats.attackSpeed[sliderValue.toInt()].toString()
        )
        // Physical Armor
        StatRow(
            statName = "Physical Armor",
            painterId = R.drawable.physical_armor,
            statValue = uiState.hero.baseStats.physicalArmor[sliderValue.toInt()].toString()
        )
        // Magical Armor
        StatRow(
            statName = "Magical Armor",
            painterId = R.drawable.magical_armor,
            statValue = uiState.hero.baseStats.magicalArmor[sliderValue.toInt()].toString()
        )
        // Physical Power
        StatRow(
            statName = "Physical Power",
            painterId = R.drawable.physical_power,
            statValue = uiState.hero.baseStats.physicalPower[sliderValue.toInt()].toString()
        )
        // Movement Speed
        StatRow(
            statName = "Movement Speed",
            painterId = R.drawable.movement_speed,
            statValue = uiState.hero.baseStats.movementSpeed.toString()
        )
        // Cleave
        StatRow(
            statName = "Cleave",
            painterId = R.drawable.cleave,
            statValue = uiState.hero.baseStats.cleave.toString()
        )
        // Attack Range
        StatRow(
            statName = "Attack Range",
            painterId = R.drawable.attack_range,
            statValue = uiState.hero.baseStats.attackRange.toString(),
            isLastRow = true
        )

    }
}

@Composable
fun StatRow(
    statName: String,
    painterId: Int,
    statValue: String,
    isLastRow: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.secondary,
                painter = painterResource(id = painterId),
                contentDescription = null
            )
            Text(
                text = statName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = statValue,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.ExtraBold
        )
    }
    if (!isLastRow) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun HeroAbilitiesScreen(
    uiState: HeroDetailsUiState,
    console: Console
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.hero.abilities.forEachIndexed { index, ability ->
            val abilityKey = getAbilityKey(index, console)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val model = ImageRequest.Builder(context)
                        .data(ability.image)
                        .crossfade(true)
                        .build()
                    SubcomposeAsyncImage(
                        model = model,
                        contentDescription = null,
                    ) {
                        val state = painter.state
                        if (state is AsyncImagePainter.State.Success) {
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
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = ability.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "${getAbilityName(index)} ${if (abilityKey != null) "($abilityKey)" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        ability.menuDescription?.let {
                            val imgRegex = "<img[^>]*>".toRegex()
                            val resultString = it.replace(imgRegex, "")
                            val textColor = MaterialTheme.colorScheme.secondary
                            AndroidView(
                                factory = { context ->
                                    TextView(context).apply {
                                        textSize = 14f
                                    }
                                },
                                update = { tv ->
                                    tv.text = HtmlCompat.fromHtml(resultString, 0)
                                    tv.setTextColor(textColor.toArgb())
                                }
                            )
//                            Text(
//                                text = HtmlCompat.fromHtml(it, 0),
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.secondary
//                            )
                        }
                    }
                }
                if (index < uiState.hero.abilities.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        }
    }
}

@Composable
fun HeroRoleChips(
    roles: List<HeroRole?>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        roles.forEach { role ->
            if (role != null) {
                ElevatedAssistChip(
                    enabled = false,
                    onClick = {},
                    colors = AssistChipDefaults.elevatedAssistChipColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    label = {
                        Text(
                            text = role.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HeroClassChips(
    heroClasses: List<HeroClass?>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        heroClasses.forEach { heroClass ->
            if (heroClass != null) {
                ElevatedAssistChip(
                    onClick = {},
                    enabled = false,
                    colors = AssistChipDefaults.elevatedAssistChipColors().copy(
                        containerColor = BadgeBlueGreen,
                        disabledContainerColor = BadgeBlueGreen,
                        disabledLabelColor = NeroBlack,
                        labelColor = NeroBlack
                    ),
                    label = {
                        Text(
                            text = heroClass.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = NeroBlack
                        )
                    }
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
fun HeroOverviewScreenPreview(
    @PreviewParameter(SampleHeroDetailsScreenUiStateProvider::class) uiState: HeroDetailsUiState
) {
    MonolithTheme {
        Surface {
            HeroOverviewScreen(
                uiState = uiState
            )
        }
    }
}

@LightDarkPreview
@Composable
fun HeroStatsScreenPreview(
    @PreviewParameter(SampleHeroDetailsScreenUiStateProvider::class) uiState: HeroDetailsUiState
) {
    MonolithTheme {
        Surface {
            HeroStatsScreen(
                uiState = uiState
            )
        }
    }
}

@LightDarkPreview
@Composable
fun HeroAbilitiesScreenPreview(
    @PreviewParameter(SampleHeroDetailsScreenUiStateProvider::class) uiState: HeroDetailsUiState
) {
    MonolithTheme {
        Surface {
            HeroAbilitiesScreen(
                uiState = uiState,
                console = Console.PC
            )
        }
    }
}