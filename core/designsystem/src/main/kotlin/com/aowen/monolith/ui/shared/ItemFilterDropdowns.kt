package com.aowen.monolith.ui.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.dropDownDefaults
import com.aowen.monolith.ui.theme.inputFieldDefaults

val defaultStats = listOf(
    "Max Health",
    "Max Mana",
    "Health Regen",
    "Mana Regen",
    "Physical Power",
    "Magical Power",
    "Attack Speed",
    "Physical Armor",
    "Magical Armor",
    "Heal and Shield Increase",
    "Ability Haste",
    "Lifesteal",
    "Magical Lifesteal",
    "Omnivamp",
    "Movement Speed",
    "Physical Penetration",
    "Magical Penetration",
    "Critical Chance",
    "Tenacity",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.TierFilterDropdown(
    selectedTierFilter: String?,
    onSelectTier: (String) -> Unit,
    onClearTierFilter: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val tierFilters = listOf("Tier I", "Tier II", "Tier III")
    Row(
        modifier = Modifier.weight(1f)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = if (!selectedTierFilter.isNullOrEmpty()) selectedTierFilter else "Tier",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(targetState = expanded, label = "") {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .selectableGroup(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tierFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedTierFilter == filter,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary,
                                    ),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = filter)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            expanded = false
                            onSelectTier(filter)
                        }
                    )
                }
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearTierFilter)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.StatFilterDropdown(
    selectedStatFilters: List<String>,
    allStats: List<String>,
    onSelectStat: (String) -> Unit,
    onClearStatsFilters: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier.weight(1f)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = "Stats",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(targetState = expanded, label = "") {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allStats.forEach { stat ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Checkbox(
                                    checked = selectedStatFilters.contains(stat),
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                    )
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = stat)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            onSelectStat(stat)
                        }
                    )
                }
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearStatsFilters)
            }
        }
    }
}
