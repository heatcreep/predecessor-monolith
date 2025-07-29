package com.aowen.monolith.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.getItemImage
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.theme.BadgeBlueGreen
import com.aowen.monolith.ui.theme.BadgePurple
import com.aowen.monolith.ui.theme.NeroBlack
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
fun ItemSearchSection(
    isLoading: Boolean,
    filteredItems: List<ItemDetails>,
    navigateToItemDetails: (String) -> Unit = {}
) {
    if (filteredItems.isNotEmpty()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Items",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            AnimatedContent(targetState = isLoading, label = "loadingItems") { loading ->
                if (loading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlayerLoadingCard(
                            titleWidth = 100.dp,
                            subtitleWidth = 75.dp
                        )
                    }
                } else {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredItems.forEach { item ->
                            ItemResultCard(
                                itemDetails = item,
                                navigateToItemDetails = {
                                    navigateToItemDetails(it)
                                }
                            )
                        }
                    }

                }
            }


        }
    }

}

@Composable
fun ItemResultCard(
    modifier: Modifier = Modifier,
    itemDetails: ItemDetails,
    navigateToItemDetails: (String) -> Unit = {}
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToItemDetails(itemDetails.name)
            }
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(4.dp)
            ),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,

            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item Image
                Image(
                    painter = painterResource(id = getItemImage(itemDetails.id)),
                    contentDescription = null
                )
                Column {
                    // Item Name
                    Text(
                        text = itemDetails.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Item Rarity (e.g. Tier III)
                        Text(
                            text = itemDetails.rarity.value,
                            modifier = Modifier
                                .background(
                                    BadgePurple,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmWhite
                        )
                        // Hero Class (e.g. Support, Mage, etc.)

                        Text(
                            text = itemDetails.heroClass.label,
                            modifier = Modifier
                                .background(
                                    BadgeBlueGreen,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = NeroBlack
                        )

                        // Aggression Type (e.g. Scaling, Mobility, etc.)
                        itemDetails.aggressionType?.let { aggressionType ->
                            Text(
                                text = aggressionType,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}