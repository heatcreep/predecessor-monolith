package com.aowen.predcompanion.core.database.util

import org.junit.Assert.assertEquals
import org.junit.Test

class IntListConverterTest {

    private val converter = IntListConverter()

    @Test
    fun `intListToString joins with commas`() {
        assertEquals("1,2,3", converter.intListToString(listOf(1, 2, 3)))
    }

    @Test
    fun `intListToString single element`() {
        assertEquals("42", converter.intListToString(listOf(42)))
    }

    @Test
    fun `stringToIntList splits on commas`() {
        assertEquals(listOf(1, 2, 3), converter.stringToIntList("1,2,3"))
    }

    @Test
    fun `stringToIntList single element`() {
        assertEquals(listOf(42), converter.stringToIntList("42"))
    }

    @Test
    fun `round trip preserves list`() {
        val original = listOf(10, 20, 30, 40)
        val result = converter.stringToIntList(converter.intListToString(original))
        assertEquals(original, result)
    }
}
