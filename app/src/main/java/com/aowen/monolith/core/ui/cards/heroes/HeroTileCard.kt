package com.aowen.monolith.core.ui.cards.heroes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.R
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
fun HeroTileCard(
    modifier: Modifier = Modifier,
    hero: HeroDetails,
    heroImageId: Int? = hero.posterImageId,
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val image = heroImageId ?: R.drawable.unknown
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.BottomCenter,
        propagateMinConstraints = true
    ) {
        Image(
            painter = painterResource(id = image),
            contentScale = ContentScale.FillWidth,
            contentDescription = hero.displayName
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Column(
                modifier = modifier
                    .aspectRatio(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hero.displayName,
                        style = labelTextStyle,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 10f
                        ),
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(top = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = hero.displayName,
                    style = labelTextStyle,
                    color = WarmWhite
                )
            }
        }
    }
}

@Preview(
    group = "Hero Card"
)
@Composable
fun HeroCardPreview() {
    MonolithTheme {
        LazyVerticalGrid(GridCells.Fixed(5)) {
            items(5) {
                HeroTileCard(
                    hero = HeroDetails(
                        imageId = Hero.NARBASH.drawableId,
                        displayName = "Narbash"
                    ),
                    isSelected = true
                )
            }
        }

    }
}