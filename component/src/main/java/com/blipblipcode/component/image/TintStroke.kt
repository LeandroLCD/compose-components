package com.blipblipcode.component.image

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Defines which layers of an [androidx.compose.ui.graphics.vector.ImageVector] receive the
 * stroke tint color when rendering an [Icon] or [Image].
 *
 * An ImageVector is composed of a tree of top-level nodes (groups and paths). Each top-level
 * node is considered one "layer" and is identified by its position (zero-based) in the
 * vector's root.
 *
 * - [All]      Recolors the stroke of every layer.
 * - [Index]    Recolors the stroke of only the layer at the given position.
 * - [Range]    Recolors the stroke of every layer whose position lies inside the given [IntRange].
 * - [Layers]   Recolors the stroke of only the layers at the specified positions.
 * - [Undefined] Does not apply any stroke transformation; the vector's strokes are rendered with
 *               their original colors.
 */
@Stable
sealed class TintStroke {

    /** Whether this stroke tint cap should skip stroke recoloring entirely and preserve the vector's original stroke colors. */
    abstract val isUndefined: Boolean

    /** Returns `true` when the top-level node at [layerIndex] should receive the stroke tint color. */
    abstract fun appliesTo(layerIndex: Int): Boolean

    @Immutable
    object All : TintStroke() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = true
        override fun toString(): String = "TintStroke.All"
    }

    @Immutable
    object Undefined : TintStroke() {
        override val isUndefined: Boolean = true
        override fun appliesTo(layerIndex: Int): Boolean = false
        override fun toString(): String = "TintStroke.Undefined"
    }

    @Immutable
    data class Index(val layer: Int) : TintStroke() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex == layer
    }

    @Immutable
    data class Range(val range: IntRange) : TintStroke() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex in range
    }

    @Immutable
    data class Layers(val layers: List<Int>) : TintStroke() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex in layers
    }

    companion object {
        /** Builds a [TintStroke] that recolors the stroke of the single layer at [layer]. */
        fun index(layer: Int): TintStroke = Index(layer)

        /** Builds a [TintStroke] that recolors the stroke of every layer whose index lies inside [range]. */
        fun range(range: IntRange): TintStroke = Range(range)

        /** Builds a [TintStroke] that recolors the stroke of every layer whose index lies in `start..endInclusive`. */
        fun range(start: Int, endInclusive: Int): TintStroke = Range(start..endInclusive)

        /** Builds a [TintStroke] that recolors the stroke of every layer whose index appears in [layers]. */
        fun layers(vararg layers: Int): TintStroke = Layers(layers.toList())

        /** Builds a [TintStroke] that recolors the stroke of every layer whose index appears in [layers]. */
        fun layers(layers: List<Int>): TintStroke = Layers(layers.toList())
    }
}