package com.aowen.monolith.core.ui.dropdown

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.MonolithTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredCompanionSortDropdown(
    modifier: Modifier = Modifier,
    selectedOption: String,
    sortOptions: List<String>,
    onClickOption: (String) -> Unit = { _ -> }
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        Row(
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sort by: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Default.Sort,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Sort by $selectedOption"
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
                sortOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onClickOption(option)
                            expanded = false
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
fun BuildSortDropdownPreview() {
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
fun BuildSortDropdownDarkPreview() {
    val sortOptions = listOf("Most Recent", "Highest Rated", "Most Popular")
    var selectedOption by remember { mutableStateOf(sortOptions.first()) }

    MonolithTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
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