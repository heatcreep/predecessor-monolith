package com.aowen.predcompanion.core.ui.cards.playerprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel

@Composable
fun PlayerProfileLayout(
    profileCard: PlayerProfileCardUiModel,
    tabLabels: List<Int>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (selectedTab: Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlayerProfileCard(
            playerProfileCardUiModel = profileCard,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(tabLabels) { index, labelRes ->
                PredCompanionChipFilter(
                    text = stringResource(labelRes),
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) }
                )
            }
        }
        content(selectedTab)
    }
}
