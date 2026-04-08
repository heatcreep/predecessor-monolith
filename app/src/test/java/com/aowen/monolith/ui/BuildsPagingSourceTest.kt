package com.aowen.predcompanion.ui

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.aowen.predcompanion.data.BuildListItem
import com.aowen.predcompanion.data.asBuildListItem
import com.aowen.predcompanion.fakes.data.fakeBuildDto
import com.aowen.predcompanion.fakes.repo.FakeOmedaCityBuildRepository
import com.aowen.predcompanion.fakes.repo.resetPageCount
import com.aowen.predcompanion.feature.builds.BuildsPagingSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.data.model.mapper.BuildListItemUiMapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BuildsPagingSourceTest {

    private var buildRepository = FakeOmedaCityBuildRepository()

    private val mockBuidListItemUiMapper = BuildListItemUiMapper()

    @After
    fun cleanup() {
        // Reset the page count after each test
        resetPageCount()
    }

    @Test
    fun `pager refresh should contain the list of builds`() = runTest {

        val mockBuilds = List(10) { mockBuidListItemUiMapper.buildFrom(fakeBuildDto.asBuildListItem()) }

        val pagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = mockBuidListItemUiMapper
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        val result = with(pager) {
            refresh()
        } as? LoadResult.Page

        assertEquals(mockBuilds, result?.data)
    }

    @Test
    fun `pager append should return null when there are no more builds`() = runTest {

        val pagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = mockBuidListItemUiMapper
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        //  fake repo returns empty list on fourth page
        val result = with(pager) {
            refresh()
            append()
            append()
        } as LoadResult.Page

        assertEquals(result.data, emptyList<BuildListItem>())
    }

    @Test
    fun `pager returns error when repo returns error`() = runTest {

        buildRepository = mockk()

        coEvery { buildRepository.fetchAllBuilds(
            name = any(),
            role = any(),
            order = any(),
            heroId = any(),
            skillOrder = any(),
            currentVersion = any(),
            modules = any(),
            page = any()
        ) } returns Resource.NetworkError(404, "Not Found")

        val pagingSource = BuildsPagingSource(
            omedaCityBuildRepository = buildRepository,
            buildListItemUiMapper = mockBuidListItemUiMapper
        )

        val pager = TestPager(PagingConfig(pageSize = 10), pagingSource)

        val result = pager.refresh()

        assertTrue(result is LoadResult.Error)

        val page = pager.getLastLoadedPage()

        assertTrue(page == null)
    }
}