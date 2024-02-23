package com.aowen.monolith.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aowen.monolith.R
import com.aowen.monolith.ui.theme.MonolithTheme

@Composable
fun PlayerIcon(
    modifier: Modifier = Modifier,
    heroImageId: Int? = null,
    heroIconSize: Dp = 52.dp,
    roleIcon: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        Image(
            modifier = Modifier
                .padding(bottom = if (roleIcon != null) 8.dp else 0.dp)
                .size(heroIconSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = heroImageId ?: R.drawable.unknown),
            contentDescription = null
        )
        if (roleIcon != null) {
            roleIcon()
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PlayerIconPreview() {
    MonolithTheme {
        Surface {
            PlayerIcon(
                heroImageId = R.drawable.narbash
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .align(Alignment.BottomEnd),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    painter = painterResource(id = R.drawable.support),
                    contentDescription = null
                )
            }
        }
    }
}