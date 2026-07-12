package com.aowen.predcompanion.feature.profile.impl.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.ui.theme.dropDownDefaults
import com.aowen.predcompanion.ui.theme.inputFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleDropdownMenu(
    modifier: Modifier = Modifier,
    console: Console,
    handleSaveConsole: (Console) -> Unit
) {

    var consoleDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Games,
                contentDescription = "Console",
                tint = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "Console",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = consoleDropdownExpanded,
            onExpandedChange = {
                consoleDropdownExpanded = it
            }
        ) {
            TextField(
                value = console.name,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(
                        targetState = consoleDropdownExpanded,
                        label = ""
                    ) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                expanded = consoleDropdownExpanded,
                onDismissRequest = { consoleDropdownExpanded = false }
            ) {
                Console.entries.forEach { entry ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = entry == console,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary,
                                    ),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = entry.name)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            handleSaveConsole(entry)
                            consoleDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}