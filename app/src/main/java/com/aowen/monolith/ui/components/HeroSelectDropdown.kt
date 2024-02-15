package com.aowen.monolith.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.ui.theme.dropDownDefaults
import com.aowen.monolith.ui.theme.inputFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HeroSelectDropdown(
    selectedHero: HeroDetails,
    onSelect: (HeroDetails) -> Unit,
    heroes: List<HeroDetails>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val heroImage = selectedHero.imageId ?: Hero.UNKNOWN.drawableId

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedHero.displayName,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = inputFieldDefaults(),
                leadingIcon = if (selectedHero.imageId != null) {
                    {
                        Image(
                            painter = painterResource(id = heroImage),
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp),
                            contentDescription = selectedHero.displayName
                        )
                    }
                } else null,
                trailingIcon = {
                    AnimatedContent(targetState = expanded, label = "") {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                heroes.forEach { hero ->
                    val image = hero.imageId ?: Hero.UNKNOWN.drawableId
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = image),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(36.dp),
                                    contentDescription = hero.displayName
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = hero.displayName)
                            }
                        },
                        colors = dropDownDefaults(),
                        onClick = {
                            onSelect(hero)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}