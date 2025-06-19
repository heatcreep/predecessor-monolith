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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.getHeroImage
import com.aowen.monolith.ui.components.PlayerLoadingCard
import com.aowen.monolith.ui.theme.BadgeBlueGreen
import com.aowen.monolith.ui.theme.NeroBlack

@Composable
fun HeroSearchSection(
    isLoading: Boolean,
    filteredHeroes: List<HeroDetails>,
    navigateToHeroDetails: (Long, String) -> Unit = { _, _ -> }
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Heroes",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedContent(targetState = isLoading, label = "heroLoading") { loading ->
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
                if (filteredHeroes.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredHeroes.forEach { hero ->
                            HeroResultCard(
                                heroDetails = hero,
                                navigateToHeroDetails = navigateToHeroDetails
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No heroes match your search.",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }


    }
}

@Composable
fun HeroResultCard(
    modifier: Modifier = Modifier,
    heroDetails: HeroDetails,
    navigateToHeroDetails: (Long, String) -> Unit = { _, _ -> }
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToHeroDetails(
                    heroDetails.id,
                    heroDetails.name
                )
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
                // Hero Image
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = getHeroImage(heroDetails.id)),
                    contentDescription = null
                )
                Column {
                    // Hero Name
                    Text(
                        text = heroDetails.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Carry, Offlane, etc.
                        heroDetails.roles.forEach { role ->
                            Text(
                                text = role.name,
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
                        // Sharpshooter, Fighter, etc.
                        heroDetails.classes.forEach { heroClass ->
                            Text(
                                text = heroClass.name,
                                modifier = Modifier
                                    .background(
                                        BadgeBlueGreen,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = NeroBlack
                            )
                        }
                    }
                }
            }
        }
    }
}