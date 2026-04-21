package com.aowen.predcompanion.core.ui.shared

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.ui.components.ItemEffectRow
import com.aowen.predcompanion.ui.components.ItemStatRow
import com.aowen.predcompanion.ui.components.TaperedItemTypeRow
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun ItemDetailsContent(
    modifier: Modifier = Modifier,
    itemDetails: ItemDetails
) {
    val localHeight = LocalWindowInfo.current.containerSize.height
    Column(
        modifier = modifier
            .height(
                localHeight.dp - 28.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape),
            model = itemDetails.imageSrc,
            contentDescription = null
        )
        Text(
            text = itemDetails.displayName,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        val price = itemDetails.price
        if (itemDetails.totalPrice > 0
            && price != null
            && price > 0
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
                        painter = painterResource(id = coreResources.drawable.gold_per_second),
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
                        painter = painterResource(id = coreResources.drawable.gold_per_second),
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