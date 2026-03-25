package com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aowen.monolith.feature.builds.addbuild.AddBuildState

data class AddBuildPreviewState(
    val isPreviewing: Boolean = false,
    val addBuildState: AddBuildState = AddBuildState()
)

class TitleAndDescriptionPreviewParameterProvider : PreviewParameterProvider<com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription.AddBuildPreviewState> {

    override val values: Sequence<com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription.AddBuildPreviewState>
        get() = sequenceOf(
            _root_ide_package_.com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription.AddBuildPreviewState(),
            _root_ide_package_.com.aowen.predcompanion.feature.builds.addbuild.addbuilddetails.titledescription.AddBuildPreviewState(
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