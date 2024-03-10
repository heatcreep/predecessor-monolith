package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.data.EffectDetails
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.utils.StyledText

@Composable
fun ItemEffectRow(effect: EffectDetails) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "${effect.name}: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.ExtraBold,
            overflow = TextOverflow.Visible
        )
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
                    menuDescription = "Healing or Shielding Allied Heroes grants you both 40 magical power and 20 ability haste for 5s."
                )
            )
        }
    }
}