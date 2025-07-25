package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToAddModule
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToEditModuleOrder
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToItemSelect
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToSkillOrderSelect
import com.aowen.monolith.feature.builds.addbuild.addbuilddetails.navigation.navigateToTitleAndDescription
import com.aowen.monolith.feature.builds.builddetails.SkillOrderScrollableRow
import com.aowen.monolith.logDebug
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import com.meetup.twain.MarkdownText
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SkillOrderAndModuleSelectRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val console by viewModel.console.collectAsState()
    SkillOrderAndModuleSelectScreen(
        uiState = uiState,
        console = console,
        onSkillSelected = viewModel::onSkillSelected,
        onBuildTitleChanged = viewModel::onBuildTitleChanged,
        navigateBack = navController::navigateUp,
        navigateToItemSelect = navController::navigateToItemSelect,
        navigateToSkillOrderSelect = navController::navigateToSkillOrderSelect,
        navigateToAddModule = navController::navigateToAddModule,
        navigateToEditModuleOrder = navController::navigateToEditModuleOrder,
        navigateToEditTitleAndDescription = navController::navigateToTitleAndDescription,
        onSubmitToOmeda = viewModel::onSubmitNewBuild,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillOrderAndModuleSelectScreen(
    uiState: AddBuildState,
    console: Console,
    onSkillSelected: (Int, Int) -> Unit,
    onBuildTitleChanged: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToItemSelect: () -> Unit,
    navigateToSkillOrderSelect: () -> Unit,
    navigateToEditModuleOrder: () -> Unit = {},
    navigateToAddModule: () -> Unit = {},
    navigateToEditTitleAndDescription: () -> Unit = {},
    onSubmitToOmeda: () -> String,
) {
    val context = LocalContext.current
    var isSkillOrderEnabled by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Add New ${uiState.selectedHero?.displayName} ${
                    uiState.selectedRole.roleName.capitalize(
                        Locale.current
                    )
                } Build",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title and Description
                SectionWithAction(
                    title = "Title and Description",
                    action = {
                        IconButton(onClick = navigateToEditTitleAndDescription) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Edit Items"
                            )
                        }
                    }
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.size(16.dp))

                    if (uiState.buildTitle.isEmpty()) {
                        Text(
                            text = "Tap the '+' button to add a title and description.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                text = uiState.buildTitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            uiState.buildDescription.let {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    MarkdownText(
                                        markdown = it,
                                        color = MaterialTheme.colorScheme.secondary,
                                        maxLines = 4
                                    )
                                }
                            }
                        }
                    }


                }
                Spacer(modifier = Modifier.size(16.dp))
                // Items
                SectionWithAction(
                    title = "Items",
                    action = {
                        IconButton(onClick = navigateToItemSelect) {
                            Icon(
                                imageVector = if (uiState.selectedItems.isEmpty()) Icons.Default.Add else Icons.Default.Edit,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = if (uiState.selectedItems.isEmpty()) "Add Items" else "Edit Items"
                            )
                        }
                    }
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (uiState.selectedItems.isEmpty() && uiState.selectedCrest == null) {
                            Text(
                                text = "No items added. Tap the '+' button to add items.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    // TODO:
                                }) {
                                uiState.selectedCrest?.let { crest ->
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .aspectRatio(1f),
                                        contentScale = ContentScale.FillBounds,
                                        painter = painterResource(id = getItemImage(crest.id)),
                                        contentDescription = null,
                                    )
                                }
                            }
                            repeat(5) {
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        // TODO:
                                    }) {
                                    val item = uiState.selectedItems.getOrNull(it)
                                    if (item != null) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .aspectRatio(1f),
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(id = getItemImage(item.id)),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }
                // Skill Order
                SectionWithAction(
                    title = "Skill Order",
                    action = {
                        Switch(
                            checked = isSkillOrderEnabled,
                            onCheckedChange = { isSkillOrderEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                checkedBorderColor = MaterialTheme.colorScheme.secondary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedIconColor = MaterialTheme.colorScheme.tertiary
                            ),
                        )
                    },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(
                            visible = isSkillOrderEnabled,
                            enter = expandHorizontally(
                                expandFrom = Alignment.CenterHorizontally
                            ),
                            exit = shrinkHorizontally(
                                shrinkTowards = Alignment.CenterHorizontally
                            )
                        ) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.tertiary,
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                    AnimatedVisibility(
                        visible = isSkillOrderEnabled,
                        exit = shrinkVertically(
                            animationSpec = tween(delayMillis = 300),
                            shrinkTowards = Alignment.CenterVertically
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SkillOrderScrollableRow(
                                modifier = Modifier.weight(1f),
                                skillOrder = uiState.skillOrder,
                                console = console
                            )
                            Row {
                                IconButton(onClick = navigateToSkillOrderSelect) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        contentDescription = "Edit Skill Order"
                                    )
                                }
                            }
                        }
                    }
                }
                // Modules
                SectionWithAction(
                    title = "Modules",
                    action = {
                        Row {
                            if (uiState.modules.isNotEmpty()) {
                                IconButton(onClick = navigateToEditModuleOrder) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        contentDescription = "Edit Module Order"
                                    )
                                }
                            }
                            IconButton(onClick = navigateToAddModule) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = "Add Module"
                                )
                            }
                        }
                    },
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    if (uiState.modules.isEmpty()) {
                        Text(
                            text = "No modules added.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            uiState.modules.forEach { module ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.secondary)
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        text = module.title,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),

                                        ) {
                                        repeat(5) {
                                            Box(modifier = Modifier
                                                .weight(1f)
                                                .fillMaxSize()
                                                .aspectRatio(1f)
                                                .background(MaterialTheme.colorScheme.primary)
                                                .clickable {
                                                    // TODO:
                                                }) {
                                                val item = module.items.getOrNull(it)
                                                if (item != null) {
                                                    Image(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .aspectRatio(1f),
                                                        contentScale = ContentScale.FillBounds,
                                                        painter = painterResource(
                                                            id = getItemImage(
                                                                item
                                                            )
                                                        ),
                                                        contentDescription = null,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedHero != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = {
                        val string = onSubmitToOmeda()
                        val intent = Intent(Intent.ACTION_VIEW, string.toUri())
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            logDebug("Error opening URL: ${e.message}")
                        }
                    }
                ) {
                    Text(text = "Save Build")
                }
            }
        }
    }

}

@Composable
fun SectionWithAction(
    modifier: Modifier = Modifier,
    title: String,
    action: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            action?.let {
                it()
            }
        }
        content()
    }
}

@Preview(
    showBackground = true
)
@LightDarkPreview
@Composable
fun SkillOrderAndModuleSelectScreenPreview() {
    MonolithTheme {

        SkillOrderAndModuleSelectScreen(
            uiState = AddBuildState(
                buildTitle = "Super Tank Narbash",
                buildDescription = """
                    ## Core Items
                    
                    First up there's Wayfarer's Bauble. It's a great first item and I love it so much. This is just more text. akdjwakawjdkajdakdjwdkawjdakdjwakdkdajwdklawjdaklwjawlkdjawlkdajwdklajdklawdjawkldjadklajdalwkdjalkdjwakldjadlkawjdlakwdjawlkdjawlkdjawkldwad
                """.trimIndent(),
                selectedCrest = ItemDetails(id = 37, displayName = "Sanctification"),
                selectedItems = persistentListOf(
                    ItemDetails(id = 1),
                    ItemDetails(id = 2),
                    ItemDetails(id = 3),
                    ItemDetails(id = 4),
                    ItemDetails(id = 5)
                ),
                modules = listOf(
                    ItemModule(
                        title = "First Buy",
                        items = listOf(
                            1,
                            2,
                            3,
                            4,
                            5
                        )
                    ),
                    ItemModule(
                        title = "Core Items",
                        items = listOf(
                            1,
                            2,
                            3
                        )
                    )
                )
            ),
            console = Console.PC,
            onSkillSelected = { _, _ -> },
            navigateBack = {},
            navigateToItemSelect = { },
            onBuildTitleChanged = { },
            navigateToSkillOrderSelect = { },
            navigateToEditModuleOrder = { },
            navigateToAddModule = { },
            navigateToEditTitleAndDescription = { },
            onSubmitToOmeda = { "" }
        )

    }
}


@Preview(
    showBackground = true
)
@LightDarkPreview
@Composable
fun PreviewSectionWithToggle() {

    var checked by remember { mutableStateOf(false) }
    MonolithTheme {
        Surface {
            Row(modifier = Modifier.fillMaxWidth()) {

                SectionWithAction(
                    title = "Section Title",
                    action = {
                        Switch(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                checkedBorderColor = MaterialTheme.colorScheme.secondary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedIconColor = MaterialTheme.colorScheme.tertiary
                            ),
                        )
                    },
                    content = {
                        Text("Content")
                    }
                )
            }
        }
    }
}
