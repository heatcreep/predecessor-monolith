package com.aowen.monolith.ui

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.resetPageCount
import com.aowen.monolith.feature.builds.BuildsPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BuildsPagingSourceTest {

    @After
    fun cleanup() {
        // Reset the page count after each test
        resetPageCount()
    }

    @Test
    fun `pager refresh should contain the list of builds`() = runTest {

        val mockBuilds = List(10) { fakeBuildDto.create() }

        val pagingSource = BuildsPagingSource(
            repository = FakeOmedaCityRepository()
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        val result = with(pager) {
            refresh()
        } as LoadResult.Page

        assertEquals(result.data, mockBuilds)
    }

    @Test
    fun `pager append should return empty list when there are no more builds`() = runTest {

        val pagingSource = BuildsPagingSource(
            repository = FakeOmedaCityRepository()
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        //  fake repo returns empty list on fourth page
        val result = with(pager) {
            refresh()
            append()
            append()
            append()
        } as LoadResult.Page

        assertEquals(result.data, emptyList<BuildListItem>())
    }

    @Test
    fun `pager returns error when repo returns error`() = runTest {

        val pagingSource = BuildsPagingSource(
            repository = FakeOmedaCityRepository(hasBuildsError = true)
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        val result = pager.refresh()

        assertTrue(result is LoadResult.Error)

        val page = pager.getLastLoadedPage()

        assertTrue(page == null)
    }
}