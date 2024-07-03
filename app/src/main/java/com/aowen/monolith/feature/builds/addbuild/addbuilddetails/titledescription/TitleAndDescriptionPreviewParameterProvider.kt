package com.aowen.monolith.feature.builds.addbuild.addbuilddetails.titledescription

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.monolith.feature.builds.addbuild.AddBuildState

data class AddBuildPreviewState(
    val isPreviewing: Boolean = false,
    val addBuildState: AddBuildState = AddBuildState()
)

class TitleAndDescriptionPreviewParameterProvider : PreviewParameterProvider<AddBuildPreviewState> {

    override val values: Sequence<AddBuildPreviewState>
        get() = sequenceOf(
            AddBuildPreviewState(),
            AddBuildPreviewState(
                isPreviewing = true,
                addBuildState = AddBuildState(
                    buildTitle = "Build Title",
                    buildDescription = """
                        ## Build Description
                        
                        This is a build description. It can contain markdown. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                    """.trimIndent()
                )
            ),
        )
}