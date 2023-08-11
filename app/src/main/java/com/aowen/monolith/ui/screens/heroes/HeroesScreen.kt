package com.aowen.monolith.ui.screens.heroes

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroImage
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
fun HeroesScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HeroesScreenViewModel = hiltViewModel()
) {

    val heroesScreenUiState by viewModel.uiState.collectAsState()

    HeroesScreen(
        modifier = modifier,
        uiState = heroesScreenUiState
    )

}

@Composable
fun HeroesScreen(
    uiState: HeroesScreenUiState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(uiState.heroes) { hero ->
                HeroCard(hero = hero)
            }
        }
    }

}

@Composable
fun HeroCard(
    hero: HeroDetails,
    modifier: Modifier = Modifier
) {

    val image = hero.imageId ?: HeroImage.NARBASH.drawableId
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomCenter,
        propagateMinConstraints = true
    ) {
        Image(
            painter = painterResource(id = image),
            contentScale = ContentScale.FillWidth,
            contentDescription = hero.displayName
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(top = 20.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = hero.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmWhite
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeroCardPreview() {
    MonolithTheme() {
        Surface() {
            HeroCard(
                hero = HeroDetails(
                    imageId = HeroImage.NARBASH.drawableId,
                )
            )
        }
    }
}

@Preview(showBackground = true,
group = "HeroesScreen")
@Composable
fun HeroesScreenPreview() {
    MonolithTheme() {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    heroes = listOf(
                        HeroDetails(
                            imageId = HeroImage.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = HeroImage.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = HeroImage.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = HeroImage.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "HeroesScreen")
@Composable
fun HeroesScreenPreviewDark() {
    MonolithTheme() {
        Surface {
            HeroesScreen(
                uiState = HeroesScreenUiState(
                    heroes = listOf(
                        HeroDetails(
                            imageId = HeroImage.NARBASH.drawableId,
                            displayName = "Narbash"
                        ),
                        HeroDetails(
                            imageId = HeroImage.BELICA.drawableId,
                            displayName = " Lt. Belica"
                        ),
                        HeroDetails(
                            imageId = HeroImage.MORIGESH.drawableId,
                            displayName = "Morigesh"
                        ),
                        HeroDetails(
                            imageId = HeroImage.TWINBLAST.drawableId,
                            displayName = "TwinBlast"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GREYSTONE.drawableId,
                            displayName = "Greystone"
                        ),
                        HeroDetails(
                            imageId = HeroImage.GRUX.drawableId,
                            displayName = "Grux"
                        ),
                    )
                )
            )
        }
    }
}
