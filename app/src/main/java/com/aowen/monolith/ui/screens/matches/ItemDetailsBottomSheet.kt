package com.aowen.monolith.ui.screens.matches

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.R
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.ui.components.ItemEffectRow
import com.aowen.monolith.ui.components.ItemStatRow
import com.aowen.monolith.ui.components.TaperedItemTypeRow
import com.aowen.monolith.ui.theme.MonolithTheme

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

@Composable
fun ItemDetailsContent(
    modifier: Modifier = Modifier,
    itemDetails: ItemDetails
) {
    val config = LocalConfiguration.current
    Column(
        modifier = modifier
            .height(
                config.screenHeightDp.dp - 28.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape),
            painter = painterResource(id = getItemImage(itemDetails.id)),
            contentDescription = null
        )
        Text(
            text = itemDetails.displayName,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        if (itemDetails.totalPrice > 0
            && itemDetails.price != null
            && itemDetails.price > 0
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                        painter = painterResource(id = R.drawable.gold_per_second),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "${itemDetails.totalPrice} Total Cost",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                        painter = painterResource(id = R.drawable.gold_per_second),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "${itemDetails.price} Upgrade Cost",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        TaperedItemTypeRow(effectType = if (itemDetails.effects.any { it?.active == true }) "Active" else "Passive")
        itemDetails.stats.forEach { stat ->
            ItemStatRow(stat = stat)
        }
        itemDetails.effects.forEach { effect ->
            if (effect != null) {
                ItemEffectRow(effect = effect)
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
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