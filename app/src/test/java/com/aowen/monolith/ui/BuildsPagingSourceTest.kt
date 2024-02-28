package com.aowen.monolith.ui

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.ui.screens.builds.BuildsPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BuildsPagingSourceTest {

    @Test
    fun `do a thing`() = runTest {

        val mockBuilds = listOf(
            fakeBuildDto.create(),
        )

        val pagingSource = BuildsPagingSource(
            repository = FakeOmedaCityRepository()
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        val result = pager.refresh() as LoadResult.Page

        assertEquals(result.data, mockBuilds)
    }
}