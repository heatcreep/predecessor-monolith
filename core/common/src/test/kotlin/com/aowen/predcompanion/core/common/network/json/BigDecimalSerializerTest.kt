package com.aowen.predcompanion.core.common.network.json

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class BigDecimalSerializerTest {

    private val json = Json

    @Test
    fun `serialize BigDecimal`() {
        val result = json.encodeToString(BigDecimalSerializer, BigDecimal("123.456"))
        assertEquals("123.456", result)
    }

    @Test
    fun `deserialize to BigDecimal`() {
        val result = json.decodeFromString(BigDecimalSerializer, "123.456")
        assertEquals(BigDecimal("123.456"), result)
    }

    @Test
    fun `round trip preserves precision`() {
        val original = BigDecimal("99999999.123456789")
        val encoded = json.encodeToString(BigDecimalSerializer, original)
        val decoded = json.decodeFromString(BigDecimalSerializer, encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun `deserialize integer value`() {
        val result = json.decodeFromString(BigDecimalSerializer, "42")
        assertEquals(BigDecimal("42"), result)
    }

    @Test
    fun `serialize zero`() {
        val result = json.encodeToString(BigDecimalSerializer, BigDecimal.ZERO)
        assertEquals("0", result)
    }
}
