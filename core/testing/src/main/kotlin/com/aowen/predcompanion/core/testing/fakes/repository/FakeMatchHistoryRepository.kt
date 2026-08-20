package com.aowen.predcompanion.core.testing.fakes.repository

import androidx.paging.PagingData
import com.aowen.predcompanion.core.data.model.MatchHistoryTarget
import com.aowen.predcompanion.core.data.repository.matches.MatchHistoryRepository
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMatchHistoryRepository : MatchHistoryRepository {

    override fun getMatchHistoryFor(
        matchHistoryTarget: MatchHistoryTarget
    ): Flow<PagingData<MatchHistoryItem>> = flowOf(PagingData.empty())

    override fun invalidate() {}
}
