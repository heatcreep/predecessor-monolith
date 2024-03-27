package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.SlotType
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@Composable
fun ItemSelectList(
    modifier: Modifier = Modifier,
    itemsList: List<ItemDetails>,
    selectedCrest: ItemDetails? = null,
    selectedItems: List<ItemDetails>,
    onCrestAdded: (ItemDetails) -> Unit,
    onCrestRemoved: (ItemDetails) -> Unit,
    onItemAdded: (ItemDetails) -> Unit,
    onItemRemoved: (ItemDetails) -> Unit,
    onItemDetailsClicked: (ItemDetails) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(itemsList) { index, item ->
            val isItemEnabled = if (item.slotType == SlotType.CREST) {
                true
            } else {
                selectedItems.size < 6
            }
            val isSelected = if (item.slotType == SlotType.CREST) {
                selectedCrest == item
            } else {
                selectedItems.contains(item)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            enabled = isItemEnabled,
                        ) {
                            if (isSelected) {
                                if (item.slotType == SlotType.CREST) {
                                    onCrestRemoved(item)
                                } else {
                                    onItemRemoved(item)
                                }
                            } else {
                                if (item.slotType == SlotType.CREST) {
                                    onCrestAdded(item)
                                } else {
                                    onItemAdded(item)
                                }
                            }
                        },
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSelected) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "${item.displayName} selected",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Image(
                            painter = painterResource(
                                id = getItemImage(item.id)
                            ),
                            modifier = Modifier.size(48.dp),
                            contentDescription = item.displayName,
                            colorFilter = if (isItemEnabled) null else ColorFilter.tint(
                                Color.Gray
                            )
                        )
                    }
                    Text(
                        text = if (isSelected) "${item.displayName} Selected" else item.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            if (isItemEnabled) MaterialTheme.colorScheme.secondary else Color.Gray
                        }
                    )
                }
                TextButton(onClick = { onItemDetailsClicked(item) }) {
                    Text(
                        text = "Details",
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            if (isItemEnabled) MaterialTheme.colorScheme.secondary else Color.Gray
                        }
                    )
                }
            }
            if (index != itemsList.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@LightDarkPreview
@Composable
fun ItemSelectListPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ItemSelectList(
                itemsList = listOf(
                    ItemDetails(
                        id = 4,
                        displayName = "Stamina Tonic"
                    ),
                    ItemDetails(
                        id = 5,
                        displayName = "Strength Tonic"
                    )

                ),
                selectedItems = listOf(ItemDetails(id = 4, displayName = "Stamina Tonic")),
                onItemAdded = {},
                onCrestAdded = {},
                onCrestRemoved = {},
                onItemRemoved = {},
                onItemDetailsClicked = {}
            )
        }
    }
}