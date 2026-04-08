package com.aowen.predcompanion.feature.matches.impl.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.feature.home.api.navigation.navigateToPlayerDetails
import com.aowen.predcompanion.feature.matches.api.navigation.MatchDetailsNavKey
import com.aowen.predcompanion.feature.matches.api.navigation.MoreMatchesNavKey
import com.aowen.predcompanion.feature.matches.api.navigation.navigateToMatchDetails
import com.aowen.predcompanion.feature.matches.impl.matchdetails.MatchDetailsRoute
import com.aowen.predcompanion.feature.matches.impl.matchdetails.MatchDetailsViewModel
import com.aowen.predcompanion.feature.matches.impl.morematches.MoreMatchesRoute
import com.aowen.predcompanion.feature.matches.impl.morematches.MoreMatchesViewModel
import com.aowen.predcompanion.navigation.Navigator
import com.aowen.predcompanion.ui.components.MonolithTopAppBar

fun EntryProviderScope<NavKey>.matchesEntry(navigator: Navigator) {
    entry<MatchDetailsNavKey> { navKey ->
        val viewModel = hiltViewModel<MatchDetailsViewModel, MatchDetailsViewModel.Factory> {
            it.create(navKey.matchId)
        }
        Scaffold(
            topBar = {
                MonolithTopAppBar(
                    title = "Match Details",
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    backAction = {
                        IconButton(onClick = { navigator.goBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "navigate up"
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            MatchDetailsRoute(
                modifier = Modifier.padding(innerPadding),
                navigateToPlayerDetails = navigator::navigateToPlayerDetails,
                viewModel = viewModel
            )
        }
    }

    entry<MoreMatchesNavKey> { navKey ->
        val viewModel = hiltViewModel<MoreMatchesViewModel, MoreMatchesViewModel.Factory> {
            it.create(navKey.playerId)
        }
        MoreMatchesRoute(
            playerId = navKey.playerId,
            viewModel = viewModel,
            navigateToMatchDetails = navigator::navigateToMatchDetails
        )
    }
}