package com.aowen.monolith.feature.builds.addbuild.addbuilddetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsBottomSheet

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemSelectList(
    modifier: Modifier = Modifier,
    itemsList: List<ItemDetails>,
    selectedItems: List<Int>,
    onItemClicked: (Int) -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    var itemDetailsBottomSheetOpen by remember { mutableStateOf(false) }
    var currentlyOpenItem by remember { mutableStateOf<ItemDetails?>(null) }
    val closeBottomSheet = { itemDetailsBottomSheetOpen = false }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (itemDetailsBottomSheetOpen && currentlyOpenItem != null) {
        currentlyOpenItem?.let {
            ItemDetailsBottomSheet(
                itemDetails = it,
                sheetState = bottomSheetState,
                closeBottomSheet = closeBottomSheet
            )
        }
    }

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(itemsList) { index, item ->
            val isSelected = selectedItems.any { it == item.id }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                    .combinedClickable(
                        onClick = { onItemClicked(item.id) },
                        onLongClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentlyOpenItem = item
                            itemDetailsBottomSheetOpen = true
                        }
                    ),
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
                    )
                }
                Text(
                    text = if (isSelected) "${item.displayName} Selected" else item.displayName,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
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