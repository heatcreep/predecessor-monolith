package com.aowen.predcompanion.core.ui.shared

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.data.ItemDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsBottomSheet(
    itemDetails: ItemDetails,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = closeBottomSheet,
        sheetState = sheetState
    ) {
        ItemDetailsContent(
            modifier = modifier,
            itemDetails = itemDetails
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemDetailsBottomSheetPreview() {
    MonolithTheme {
        Surface {
            ItemDetailsContent(
                itemDetails = ItemDetails(
                    image = "https://omeda.city/images/items/Refillable-Potion.webp",
                    name = "Refillable Potion",
                    displayName = "Malady",
                ),
            )
        }
    }
}