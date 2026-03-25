package com.aowen.monolith.core.ui.filters

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.aowen.monolith.core.ui.filters.preview.ChipFilterPreviewProvider
import com.aowen.monolith.core.ui.filters.preview.ChipFilterPreviewState
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun PredCompanionChipFilter(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FilterChip(
            colors = FilterChipDefaults.filterChipColors().copy(
                labelColor = MaterialTheme.colorScheme.secondary,
                selectedLabelColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                leadingIconColor = MaterialTheme.colorScheme.secondary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),

                    )
            },
            label = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            selected = selected,
            onClick = onClick,
        )
    }


}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun RoleFilterLightPreview(
    @PreviewParameter(ChipFilterPreviewProvider ::class) uiState: List<ChipFilterPreviewState>
) {
    MonolithTheme {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(uiState) {
                PredCompanionChipFilter(
                    text = it.text,
                    selected = it.selected,
                    iconRes = it.iconRes,
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RoleFilterDarkPreview(
    @PreviewParameter(ChipFilterPreviewProvider ::class) uiState: List<ChipFilterPreviewState>
) {
    MonolithTheme {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(uiState) {
                    PredCompanionChipFilter(
                        text = it.text,
                        selected = it.selected,
                        iconRes = it.iconRes,
                    )
            }
        }
    }
}