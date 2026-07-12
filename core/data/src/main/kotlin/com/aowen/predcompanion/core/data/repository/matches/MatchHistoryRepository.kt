package com.aowen.predcompanion.core.data.repository.matches

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aowen.predcompanion.core.data.model.MatchHistoryPagingSource
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MatchHistoryRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    companion object {
        const val PAGE_SIZE = 10
    }

    private var currentSource: MatchHistoryPagingSource? = null

    fun getMatchHistory(): Flow<PagingData<MatchHistoryItem>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                MatchHistoryPagingSource(apolloClient).also { currentSource = it }
            }
        ).flow

    fun invalidate() {
        currentSource?.invalidate()
    }
}