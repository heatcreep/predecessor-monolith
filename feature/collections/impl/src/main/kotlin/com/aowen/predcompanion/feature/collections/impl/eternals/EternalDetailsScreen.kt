package com.aowen.predcompanion.feature.collections.impl.eternals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.shared.PredGGMarkupText
import com.aowen.predcompanion.core.ui.utils.LocalMarkupImageAltText
import com.aowen.predcompanion.core.ui.utils.LocalMarkupStyles
import com.aowen.predcompanion.core.ui.utils.rememberMarkupImageAltText
import com.aowen.predcompanion.core.ui.utils.rememberMarkupStyles
import com.aowen.predcompanion.feature.collections.impl.R
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalDetailsUiModel
import com.aowen.predcompanion.feature.collections.impl.ui.model.MinorBlessingUiModel
import com.aowen.predcompanion.ui.components.FullScreenErrorWithRetry
import com.aowen.predcompanion.ui.components.FullScreenLoadingIndicator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun EternalDetailsScreen(
    eternalDetailsUiState: EternalDetailsUiState,
    onRetry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MonolithTopAppBar(
                title = stringResource(R.string.feature_collection_eternal_details),
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
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (eternalDetailsUiState) {
                is EternalDetailsUiState.Loading -> {
                    FullScreenLoadingIndicator(
                        loadingText = stringResource(R.string.feature_collection_eternal_details_loading)
                    )
                }

                is EternalDetailsUiState.Error -> {
                    FullScreenErrorWithRetry(
                        errorMessage = stringResource(eternalDetailsUiState.messageResId),
                        onRetry = onRetry
                    )
                }

                is EternalDetailsUiState.Loaded -> {
                    val eternalDetails = eternalDetailsUiState.content
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Eternal Name with Category
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Icon
                                AsyncImage(
                                    model = eternalDetails.imageSrc,
                                    contentDescription = eternalDetails.displayName
                                )
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = eternalDetails.displayName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color(eternalDetails.categoryColorString.toColorInt()))
                                                .padding(4.dp),
                                            text = eternalDetails.eternalCategory,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                    Text(
                                        text = eternalDetails.eternalDescription,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 14.sp),
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                                // Eternal Description + Eternal Primary Effect
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                PredGGMarkupText(
                                    text = eternalDetails.eternalAbilityDescription,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        // Minor Blessing 1
                        Text(
                            text = stringResource(R.string.feature_collection_minor_blessing_1),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp),
                        ) {
                            eternalDetails.firstMinorBlessing.forEachIndexed { index, blessing ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.size(48.dp),
                                            model = blessing.imageSrc,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
                                            contentDescription = blessing.displayName
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = blessing.displayName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.size(8.dp))
                                            PredGGMarkupText(
                                                text = blessing.description,
                                                style = MaterialTheme.typography.bodySmall,
                                            )
                                        }
                                    }
                                    if (index != eternalDetails.firstMinorBlessing.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 16.dp),
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        // Minor Blessing 1
                        Text(
                            text = stringResource(R.string.feature_collection_minor_blessing_2),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp),
                        ) {
                            eternalDetails.secondMinorBlessing.forEachIndexed { index, blessing ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.size(48.dp),
                                            model = blessing.imageSrc,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
                                            contentDescription = blessing.displayName
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = blessing.displayName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.size(8.dp))
                                            PredGGMarkupText(
                                                text = blessing.description,
                                                style = MaterialTheme.typography.bodySmall,
                                            )
                                        }
                                    }
                                    if (index != eternalDetails.firstMinorBlessing.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 16.dp),
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@PreviewLightDark
@Composable
fun EternalDetailsScreenPreview() {

    val context = LocalContext.current
    val eternalImageUrl = "https://example.com/eternal.png"
    val blessingImageUrl = "https://example.com/blessing.png"

    val previewHandler = remember {

        AsyncImagePreviewHandler { request ->
            val drawableRes = when (request.data) {
                eternalImageUrl -> R.drawable.pilow
                blessingImageUrl -> coreResources.drawable.magical_power
                else -> coreResources.drawable.unknown
            }
            ContextCompat.getDrawable(context, drawableRes)!!.asImage()
        }
    }
    MonolithTheme {
        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides previewHandler,
            LocalMarkupStyles provides rememberMarkupStyles(),
            LocalMarkupImageAltText provides rememberMarkupImageAltText()
        ) {
            EternalDetailsScreen(
                eternalDetailsUiState = EternalDetailsUiState.Loaded(
                    content = EternalDetailsUiModel(
                        displayName = "Pilow",
                        imageSrc = eternalImageUrl,
                        eternalCategory = "Divine",
                        eternalDescription = "Radiant entities known for their unyielding preservation and transcendent aura, standing as bastions of absolute light against the encroachment of the void.",
                        eternalAbilityDescription = "Decrease your <EffectText>Size</> by <EffectText>10%</>. Upon casting an Ability, nearby Enemy Heroes within a <EffectText>600u</> radius are <CC_Text>Debuffed</>, causing them to deal <CC_Text>4%</> less <CC_Text>Damage</> for <CC_Text>3.5s</>, stacking up to <CC_Text>3</> times.",
                        firstMinorBlessing = listOf(
                            MinorBlessingUiModel(
                                displayName = "Bioluminescence",
                                imageSrc = blessingImageUrl,
                                description = "Enemies you damage take 3% bonus Damage and are Revealed for 4s."
                            ),
                            MinorBlessingUiModel(
                                displayName = "Harmonic Currents",
                                imageSrc = blessingImageUrl,
                                description = "Casting an Ability restores <EffectText>2%</> of your <HealthText>Missing Health</> and <ManaText>Missing Mana</>."
                            ),
                            MinorBlessingUiModel(
                                displayName = "Diffusion",
                                imageSrc = blessingImageUrl,
                                description = "Increases the Radius of Pilow's Debuff Pulse by 50% and the Duration by 1.5s."
                            ),
                        ),
                        secondMinorBlessing = listOf(
                            MinorBlessingUiModel(
                                displayName = "Bioluminescence",
                                imageSrc = blessingImageUrl,
                                description = "Enemies you damage take <HealthEffect>3% bonus Damage</> and are Revealed for 4s."
                            ),
                            MinorBlessingUiModel(
                                displayName = "Harmonic Currents",
                                imageSrc = blessingImageUrl,
                                description = "Casting an Ability restores <EffectText>2%</> of your Missing Health and Missing Mana."
                            ),
                            MinorBlessingUiModel(
                                displayName = "Diffusion",
                                imageSrc = blessingImageUrl,
                                description = "Increases the Radius of Pilow's Debuff Pulse by 50% and the Duration by 1.5s."
                            ),
                        ),
                        categoryColorString = "#907fd4"
                    )
                ),
                onRetry = {},
                navigateBack = {}
            )
        }
    }
}