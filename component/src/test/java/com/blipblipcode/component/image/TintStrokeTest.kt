package com.blipblipcode.component.image

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TintStrokeTest {

    @Test
    fun `All recolors every stroke and is not undefined`() {
        val cap = TintStroke.All
        assertFalse(cap.isUndefined)
        for (i in -1..10) {
            assertTrue("layer $i should be stroked by All", cap.appliesTo(i))
        }
    }

    @Test
    fun `Undefined never recolors the stroke and reports itself as undefined`() {
        val cap = TintStroke.Undefined
        assertTrue(cap.isUndefined)
        for (i in -5..20) {
            assertFalse("layer $i should NOT be stroked by Undefined", cap.appliesTo(i))
        }
    }

    @Test
    fun `Index recolors only the matching layer`() {
        val cap = TintStroke.index(3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Index works with negative and out-of-range positions`() {
        val cap = TintStroke.index(0)
        assertFalse(cap.appliesTo(-1))
        assertTrue(cap.appliesTo(0))
        assertFalse(cap.appliesTo(1))
    }

    @Test
    fun `Range recolors every stroke inside the range, inclusive`() {
        val cap = TintStroke.range(1..3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertTrue(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Range with start and endInclusive helper works`() {
        val cap = TintStroke.range(0, 2)
        assertTrue(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertTrue(cap.appliesTo(2))
        assertFalse(cap.appliesTo(3))
    }

    @Test
    fun `Layers recolors only the specified positions`() {
        val cap = TintStroke.layers(1, 3)
        assertFalse(cap.isUndefined)
        assertFalse(cap.appliesTo(0))
        assertTrue(cap.appliesTo(1))
        assertFalse(cap.appliesTo(2))
        assertTrue(cap.appliesTo(3))
        assertFalse(cap.appliesTo(4))
    }

    @Test
    fun `Layers accepts a list factory`() {
        val cap = TintStroke.layers(listOf(0, 4, 7))
        assertTrue(cap.appliesTo(0))
        assertFalse(cap.appliesTo(1))
        assertTrue(cap.appliesTo(4))
        assertTrue(cap.appliesTo(7))
        assertFalse(cap.appliesTo(8))
    }

    @Test
    fun `Layer ordering is preserved`() {
        val cap = TintStroke.layers(5, 0, 2)
        assertEquals(listOf(5, 0, 2), (cap as TintStroke.Layers).layers)
    }
}