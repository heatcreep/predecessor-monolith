package com.aowen.monolith.feature.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.aowen.monolith.data.datastore.Theme
import com.aowen.monolith.ui.theme.dropDownDefaults
import com.aowen.monolith.ui.theme.inputFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDropdownMenu(
    modifier: Modifier = Modifier,
    theme: Theme,
    handleSaveTheme: (Theme) -> Unit
) {

    var themeDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Theme",
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.secondary
        )
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = themeDropdownExpanded,
            onExpandedChange = {
                themeDropdownExpanded = it
            }
        ) {
            TextField(
                value = theme.name,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                trailingIcon = {
                    AnimatedContent(
                        targetState = themeDropdownExpanded,
                        label = ""
                    ) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                expanded = themeDropdownExpanded,
                onDismissRequest = { themeDropdownExpanded = false }
            ) {
                Theme.entries.forEach { entry ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = entry == theme,
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
                            handleSaveTheme(entry)
                            themeDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}