package com.aowen.monolith.core.ui.dropdown

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aowen.monolith.feature.home.playerdetails.HeroUiModel
import com.aowen.monolith.ui.theme.dropDownDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HeroSelectDropdown(
    modifier: Modifier = Modifier,
    heroName: String,
    heroImageId: Int? = null,
    onSelect: (String) -> Unit,
    heroes: List<HeroUiModel>
) {
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                heroImageId?.let { image ->
                    Image(
                        painter = painterResource(id = image),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp),
                        contentDescription = heroName
                    )
                }
                Text(
                    text = heroName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            AnimatedContent(targetState = expanded, label = "") {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (it) 180f else 0f)
                )
            }

        }

        ExposedDropdownMenu(
            modifier = Modifier.wrapContentWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            heroes.forEach { hero ->
                val image = hero.imageId
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            image?.let {
                                Image(
                                    painter = painterResource(id = image),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(36.dp),
                                    contentDescription = heroName
                                )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = hero.name)
                        }
                    },
                    colors = dropDownDefaults(),
                    onClick = {
                        onSelect(hero.name)
                        expanded = false
                    },
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }

}