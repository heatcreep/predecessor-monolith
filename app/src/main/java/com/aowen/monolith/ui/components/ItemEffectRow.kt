package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.EffectDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.StyledText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemEffectRow(effect: EffectDetails) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            StyledText(
                content = "${effect.name}${effect.condition?.let { " - $it" }}",
            )
        }
        StyledText(content = effect.menuDescription ?: "")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemEffectRowPreview() {
    MonolithTheme {
        Surface {
            ItemEffectRow(
                effect = EffectDetails(
                    name = "Elation",
                    condition = "\u003CCondition\u003EAfter\u003C/Condition\u003E \u003CManaText\u003ELevel 6\u003C/ManaText\u003E\u003CCondition\u003E\u003C/Condition\u003E",
                    menuDescription = "Healing or Shielding Allied Heroes grants you both 40 magical power and 20 ability haste for 5s."
                )
            )
        }
    }
}