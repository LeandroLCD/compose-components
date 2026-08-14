package com.blipblipcode.component.image

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TintCapTest {

    @Test
    fun `All tints every layer and is not undefined`() {
        val cap = TintCap.All
        assertFalse(cap.isUndefined)
        for (i in -1..10) {
            assertTrue("layer $i should be tinted by All", cap.appliesTo(i))
        }
    }

    @Test
    fun `Undefined never tints and reports itself as undefined`() {
        val cap = TintCap.Undefined
        assertTrue(cap.isUndefined)
        for (i in -5..20) {
            assertFalse("layer $i should NOT be tinted by Undefined", cap.appliesTo(i))
        }
    }

    @Test
    fun `Index tints only the matching layer`() {
        val cap = TintCap.index(3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Index works with negative and out-of-range positions`() {
        val cap = TintCap.index(0)
        assertFalse(cap.appliesTo(-1))
        assertTrue(cap.appliesTo(0))
        assertFalse(cap.appliesTo(1))
    }

    @Test
    fun `Range tints every layer within the range, inclusive`() {
        val cap = TintCap.range(1..3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertTrue(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Range with start and endInclusive helper works`() {
        val cap = TintCap.range(0, 2)
        assertTrue(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertTrue(cap.appliesTo(2))
        assertFalse(cap.appliesTo(3))
    }

    @Test
    fun `Layers tints only the specified positions`() {
        val cap = TintCap.layers(1, 3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertFalse(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Layers accepts a list factory`() {
        val cap = TintCap.layers(listOf(0, 4, 7))
        assertTrue(cap.appliesTo(0))
        assertFalse(cap.appliesTo(1))
        assertTrue(cap.appliesTo(4))
        assertTrue(cap.appliesTo(7))
        assertFalse(cap.appliesTo(8))
    }

    @Test
    fun `Layer ordering is preserved`() {
        val cap = TintCap.layers(5, 0, 2)
        // Even unsorted, appliesTo is membership-based, but equality should preserve order
        assertEquals(listOf(5, 0, 2), (cap as TintCap.Layers).layers)
    }
}