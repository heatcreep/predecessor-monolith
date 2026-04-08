package com.aowen.predcompanion.core.ui.components

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable

@Composable
fun RefreshableContainer(
    isRefreshing: Boolean = false,
    pullRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
   PullToRefreshBox(
       isRefreshing = isRefreshing,
       onRefresh = onRefresh,
   ) { }
}