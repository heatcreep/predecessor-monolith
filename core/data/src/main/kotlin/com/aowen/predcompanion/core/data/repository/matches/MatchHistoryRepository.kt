package com.aowen.predcompanion.core.data.repository.matches

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aowen.predcompanion.core.data.model.MatchHistoryPagingSource
import com.aowen.predcompanion.core.data.model.MatchHistoryTarget
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MatchHistoryRepository {
    fun getMatchHistoryFor(
        matchHistoryTarget: MatchHistoryTarget
    ): Flow<PagingData<MatchHistoryItem>>

    fun invalidate()
}

class MatchHistoryRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : MatchHistoryRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    private var currentSource: MatchHistoryPagingSource? = null

    override fun getMatchHistoryFor(
        matchHistoryTarget: MatchHistoryTarget
    ): Flow<PagingData<MatchHistoryItem>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                MatchHistoryPagingSource(
                    client = apolloClient,
                    target = matchHistoryTarget
                ).also { currentSource = it }
            }
        ).flow

    override fun invalidate() {
        currentSource?.invalidate()
    }
}
