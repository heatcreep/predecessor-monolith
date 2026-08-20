package com.aowen.predcompanion.core.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ListHelpersTest {

    @Test
    fun `filterOrOriginal returns filtered list when matches exist`() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.filterOrOriginal { it > 3 }
        assertEquals(listOf(4, 5), result)
    }

    @Test
    fun `filterOrOriginal returns original list when no matches`() {
        val list = listOf(1, 2, 3)
        val result = list.filterOrOriginal { it > 10 }
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun `filterOrOriginal with empty collection returns empty list`() {
        val list = emptyList<Int>()
        val result = list.filterOrOriginal { it > 0 }
        assertEquals(emptyList<Int>(), result)
    }

    @Test
    fun `filterOrOriginal works with sets`() {
        val set = setOf("a", "b", "c")
        val result = set.filterOrOriginal { it == "b" }
        assertEquals(listOf("b"), result)
    }
}
