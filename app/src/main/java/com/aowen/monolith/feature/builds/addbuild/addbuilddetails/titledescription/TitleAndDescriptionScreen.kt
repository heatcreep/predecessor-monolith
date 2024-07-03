package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.titledescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aowen.monolith.feature.builds.addbuild.AddBuildState
import com.aowen.monolith.feature.builds.addbuild.AddBuildViewModel
import com.aowen.monolith.ui.components.MonolithTopAppBar
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.inputFieldDefaults
import com.aowen.monolith.ui.tooling.previews.LightDarkPreview
import com.meetup.twain.MarkdownText

@Composable
fun TitleAndDescriptionRoute(
    navController: NavController,
    viewModel: AddBuildViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    var isPreviewing by remember { mutableStateOf(false) }

    TitleAndDescriptionScreen(
        uiState = uiState,
        onBuildTitleChanged = viewModel::onBuildTitleChanged,
        onBuildDescriptionChanged = viewModel::onBuildDescriptionChanged,
        isPreviewing = isPreviewing,
        onPreviewClicked = { isPreviewing = !isPreviewing },
        navigateBack = navController::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAndDescriptionScreen(
    modifier: Modifier = Modifier,
    uiState: AddBuildState,
    isPreviewing: Boolean,
    onPreviewClicked: () -> Unit,
    onBuildTitleChanged: (String) -> Unit,
    onBuildDescriptionChanged: (String) -> Unit,
    navigateBack: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = "Add Title and Description",
                titleStyle = MaterialTheme.typography.bodyLarge,
                backAction = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate up"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "Title",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "Enter a title for your build",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    value = uiState.buildTitle,
                    colors = inputFieldDefaults(),
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        onBuildTitleChanged(it)
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Description",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(onClick = { onPreviewClicked() }) {
                        Text(
                            text = if (isPreviewing) "Edit" else "Preview",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (isPreviewing) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        MarkdownText(
                            markdown = uiState.buildDescription,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                } else {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        value = uiState.buildDescription,
                        colors = inputFieldDefaults(),
                        placeholder = {
                            Text(
                                text = "Enter a description for your build",
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        onValueChange = {
                            onBuildDescriptionChanged(it)
                        }
                    )
                }
            }

        }
    }
}

@LightDarkPreview
@Composable
fun TitleAndDescriptionScreenPreview(
    @PreviewParameter(TitleAndDescriptionPreviewParameterProvider::class) previewState: AddBuildPreviewState
) {
    MonolithTheme {
        TitleAndDescriptionScreen(
            uiState = previewState.addBuildState,
            isPreviewing = previewState.isPreviewing,
            onPreviewClicked = {},
            onBuildTitleChanged = {},
            onBuildDescriptionChanged = {},
            navigateBack = {}
        )
    }
}