package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.skillorder

import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.R
import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillBottomSheet(
    sheetState: SheetState,
    ability: AbilityDetails,
    closeBottomSheet: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = closeBottomSheet,
        sheetState = sheetState
    ) {
        SkillBottomSheetContent(
            ability = ability
        )
    }
}

@Composable
fun SkillBottomSheetContent(
    modifier: Modifier = Modifier,
    ability: AbilityDetails,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val model = ImageRequest.Builder(context)
            .data(ability.image)
            .crossfade(true)
            .placeholder(R.drawable.unknown_ability)
            .build()
        SubcomposeAsyncImage(
            model = model,
            contentDescription = null,
        ) {

            SubcomposeAsyncImageContent(
                modifier = modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.secondary,
                        CircleShape
                    ),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )

        }
        Spacer(modifier = Modifier.size(24.dp))
        Text(text = ability.displayName)
        Spacer(modifier = Modifier.size(24.dp))
        ability.menuDescription?.let {
            val imgRegex = "<img[^>]*>".toRegex()
            val resultString = it.replace(imgRegex, "")
            val textColor = MaterialTheme.colorScheme.secondary
            AndroidView(
                factory = { context ->
                    TextView(context).apply {
                        textSize = 14f
                    }
                },
                update = { tv ->
                    tv.text = HtmlCompat.fromHtml(resultString, 0)
                    tv.setTextColor(textColor.toArgb())
                }
            )
        }
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Preview
@LightDarkPreview
@Composable
fun SkillBottomSheetPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            SkillBottomSheetContent(
                ability = AbilityDetails(
                    displayName = "Ability Name",
                    image = "https://cdn.dota2.com/apps/dota2/images/abilities/antimage_mana_break_md.png",
                    gameDescription = "Ability Description",
                    menuDescription = "Ranged basic attack dealing 54 <AttackDamageText>(+60%</AttackDamageText><img id=\\\"ADIconOrange\\\"></img><AttackDamageText>)</AttackDamageText> physical damage.",
                    cooldown = listOf(1f, 2f, 3f),
                    cost = listOf(1f, 2f, 3f)
                )
            )
        }
    }
}