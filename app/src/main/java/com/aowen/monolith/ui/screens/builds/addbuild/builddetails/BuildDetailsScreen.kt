package com.aowen.monolith.ui.screens.builds.addbuild.builddetails

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildState
import com.aowen.monolith.ui.screens.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun BuildDetailsRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    BuildDetailsScreen(
        uiState = uiState,
        onSkillSelected = viewModel::onSkillSelected
    )
}

@Composable
fun BuildDetailsScreen(
    uiState: AddBuildState,
    onSkillSelected: (Int, Int) -> Unit
) {
    BuildOrderPicker(
        onSkillSelected = onSkillSelected
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuildOrderPicker(
    onSkillSelected: (Int, Int) -> Unit

) {
    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly,
            maxItemsInEachRow = 4
        ) {
            BuilderHeaderRowItem(text = "Q")
            BuilderHeaderRowItem(text = "E")
            BuilderHeaderRowItem(text = "R")
            BuilderHeaderRowItem(text = "RMB")
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(18) { skillColumnIndex ->
                BuildOrderPickerRow(
                    onSkillSelected = onSkillSelected,
                    skillColumnIndex = skillColumnIndex
                )
            }
        }
    }
}

@Composable
fun RowScope.BuilderHeaderRowItem(
    text: String
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuildOrderPickerRow(
    skillColumnIndex: Int = 1,
    onSkillSelected: (Int, Int) -> Unit,
) {

    var selectedSkill by rememberSaveable {
        mutableIntStateOf(-1)
    }

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
                        selectedSkill = skillRowIndex
                        onSkillSelected(skillColumnIndex, selectedSkill + 1)
                    }
                    .background(
                        color = if (selectedSkill == skillRowIndex) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedSkill == skillRowIndex) {
                    Text(
                        text = "${skillColumnIndex + 1}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
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
fun BuildOrderPickerPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)

        ) {
            BuildOrderPicker(
                onSkillSelected = { _, _ -> }
            )
        }
    }
}