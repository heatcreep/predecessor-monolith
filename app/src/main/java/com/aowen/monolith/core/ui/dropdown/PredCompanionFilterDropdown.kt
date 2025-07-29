package com.aowen.monolith.core.ui.dropdown

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.MonolithTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredCompanionFilterDropdown(
    modifier: Modifier = Modifier,
    selectedOptions: List<String>,
    filterOptions: List<String>,
    onClickOption: (String) -> Unit = { _ -> }
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        Row(
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(targetState = expanded, label = "") {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (it) 180f else 0f)
                )
            }
            Text(
                text = "Filters: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = when{
                    selectedOptions.isEmpty() -> "None"
                    selectedOptions.size == 1 -> selectedOptions.first()
                    else -> "${selectedOptions.first()} + ${selectedOptions.size - 1} more"
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        ExposedDropdownMenu(
            containerColor = MaterialTheme.colorScheme.background,
            expanded = expanded,
            matchTextFieldWidth = false,
            onDismissRequest = { expanded = false }
        ) {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Checkbox(
                                    checked = selectedOptions.contains(option),
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                    )
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = option)
                            }
                        },
                        onClick = {
                            onClickOption(option)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

    }
}

@Preview(
    showBackground = true,
)
@Composable
fun PredCompanionFilterDropdownPreview() {
    val sortOptions = listOf("Most Recent", "Highest Rated", "Most Popular")
    var selectedOption by remember { mutableStateOf(sortOptions.first()) }

    MonolithTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            PredCompanionSortDropdown(
                selectedOption = selectedOption,
                sortOptions = sortOptions,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PredCompanionFilterDropdownDarkPreview() {
    val sortOptions = listOf("Physical Armor", "Magical Armor", "Physical Damage")
    var selectedOptions = remember { mutableStateListOf("Physical Armor") }

    MonolithTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)

        ) {
            PredCompanionFilterDropdown(
                selectedOptions = selectedOptions,
                filterOptions = sortOptions,
            )
        }
    }
}