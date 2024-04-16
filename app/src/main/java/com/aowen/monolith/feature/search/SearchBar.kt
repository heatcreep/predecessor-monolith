package com.aowen.monolith.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.inputFieldDefaults

@Composable
fun SearchBar(
    searchValue: String,
    setSearchValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchLabel: String = "",
    handleSubmitSearch: (() -> Unit)? = null,
    handleClearSearch: (() -> Unit)? = null
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min),
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text(
                text = searchLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                handleClearSearch?.let {
                    if (searchValue.isNotEmpty()) {
                        IconButton(onClick = {
                            handleClearSearch()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                    }
                }
                handleSubmitSearch?.let {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        handleSubmitSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                handleSubmitSearch?.let {
                    handleSubmitSearch()
                }
            }
        ),
        value = searchValue,
        colors = inputFieldDefaults(),
        singleLine = true,
        maxLines = 1,
        onValueChange = setSearchValue
    )
}