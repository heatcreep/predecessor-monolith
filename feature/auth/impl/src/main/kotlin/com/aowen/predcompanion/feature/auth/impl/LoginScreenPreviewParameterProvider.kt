package com.aowen.predcompanion.feature.auth.impl

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginScreenPreviewParameterProvider : PreviewParameterProvider<LoginUiState> {

    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            LoginUiState(),
            LoginUiState(isLoading = false),
            LoginUiState(
                isLoading = false,
                errorMessage = """
                    There was an issue signing you in.
                    Please sign in again.
                    """.trimIndent()
            )
        )
}
