package com.blipblipcode.component.image

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Defines which layers of an [androidx.compose.ui.graphics.vector.ImageVector] receive the
 * tint color when rendering an [Icon] or [Image].
 *
 * An ImageVector is composed of a tree of top-level nodes (groups and paths). Each top-level
 * node is considered one "layer" and is identified by its position (zero-based) in the
 * vector's root.
 *
 * - [All]      Tints every layer (default for [Icon], matches standard Compose tinting).
 * - [Index]    Tints only the layer at the given position.
 * - [Range]    Tints every layer whose position lies inside the given [IntRange].
 * - [Layers]   Tints only the layers at the specified positions.
 * - [Undefined] Does not apply any tint transformation; the vector is rendered with its
 *               original colors (default for [Image]).
 */
@Stable
sealed class TintCap {

    /** Whether this tint cap should skip tinting entirely and preserve the vector's original colors. */
    abstract val isUndefined: Boolean

    /** Returns `true` when the top-level node at [layerIndex] should receive the tint color. */
    abstract fun appliesTo(layerIndex: Int): Boolean

    @Immutable
    object All : TintCap() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = true
        override fun toString(): String = "TintCap.All"
    }

    @Immutable
    object Undefined : TintCap() {
        override val isUndefined: Boolean = true
        override fun appliesTo(layerIndex: Int): Boolean = false
        override fun toString(): String = "TintCap.Undefined"
    }

    @Immutable
    data class Index(val layer: Int) : TintCap() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex == layer
    }

    @Immutable
    data class Range(val range: IntRange) : TintCap() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex in range
    }

    @Immutable
    data class Layers(val layers: List<Int>) : TintCap() {
        override val isUndefined: Boolean = false
        override fun appliesTo(layerIndex: Int): Boolean = layerIndex in layers
    }

    companion object {
        /** Convenience alias for [All]. */
        val All: TintCap get() = All

        /** Convenience alias for [Undefined]. */
        val Undefined: TintCap get() = Undefined

        /** Builds a [TintCap] that tints the single layer at [layer]. */
        fun index(layer: Int): TintCap = Index(layer)

        /** Builds a [TintCap] that tints every layer whose index lies inside [range]. */
        fun range(range: IntRange): TintCap = Range(range)

        /** Builds a [TintCap] that tints every layer whose index lies in `start..endInclusive`. */
        fun range(start: Int, endInclusive: Int): TintCap = Range(start..endInclusive)

        /** Builds a [TintCap] that tints every layer whose index appears in [layers]. */
        fun layers(vararg layers: Int): TintCap = Layers(layers.toList())

        /** Builds a [TintCap] that tints every layer whose index appears in [layers]. */
        fun layers(layers: List<Int>): TintCap = Layers(layers)
    }
}