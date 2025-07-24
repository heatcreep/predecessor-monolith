package com.aowen.monolith.core.ui.filters

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.R
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun RoleFilter(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        modifier = modifier.width(IntrinsicSize.Max),
        colors = FilterChipDefaults.filterChipColors().copy(
            labelColor = MaterialTheme.colorScheme.primaryContainer,
            containerColor = MaterialTheme.colorScheme.secondary,
            selectedLabelColor = MaterialTheme.colorScheme.secondary,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.secondary,
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

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun RoleFilterPreview() {
    MonolithTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RoleFilter(text = "Support", selected = true, iconRes = R.drawable.simple_support)
            RoleFilter(text = "Jungle", iconRes = R.drawable.simple_jungle)
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RoleFilterDarkPreview() {
    MonolithTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RoleFilter(text = "Support", selected = true, iconRes = R.drawable.simple_support)
            RoleFilter(text = "Jungle", iconRes = R.drawable.simple_jungle)
        }
    }
}