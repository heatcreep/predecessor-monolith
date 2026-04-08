package com.aowen.predcompanion.core.ui.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aowen.predcompanion.ui.theme.dropDownDefaults
import com.aowen.predcompanion.ui.theme.inputFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.FilterDropdown(
    dropdownTitle: String = "Filter",
    filterOptions: List<String>,
    selectedFilter: String?,
    onSelectOption: (String) -> Unit,
    onClearOption: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
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
                value = selectedFilter ?: dropdownTitle,
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
                DropdownMenuItem(text = { Text(text = "Clear") }, onClick = onClearOption)
                filterOptions.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(text = filter) },
                        colors = dropDownDefaults(),
                        onClick = {
                            expanded = false
                            onSelectOption(filter)
                        }
                    )
                }
            }
        }
    }
}
