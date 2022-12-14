package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface YGBaselineFunc {
    operator fun invoke(node: YGNode?, width: Float, height: Float): Float
}