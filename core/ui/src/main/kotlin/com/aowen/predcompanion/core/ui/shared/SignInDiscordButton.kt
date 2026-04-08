package com.aowen.predcompanion.core.ui.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.ui.theme.WarmWhite
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun SignInDiscordButton(
    modifier: Modifier = Modifier,
    submitLogin: () -> Unit = {}
) {

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = WarmWhite
    )

    ElevatedButton(
        onClick = submitLogin,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = PaddingValues(24.dp)
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = coreResources.drawable.discord_mark_white),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Sign in with Discord"
        )
    }
}
