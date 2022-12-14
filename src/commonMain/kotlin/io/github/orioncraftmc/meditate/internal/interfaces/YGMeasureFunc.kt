package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode
import io.github.orioncraftmc.meditate.internal.YGSize
import io.github.orioncraftmc.meditate.internal.enums.YGMeasureMode

fun interface YGMeasureFunc {
    operator fun invoke(
        node: YGNode?,
        width: Float,
        widthMode: YGMeasureMode?,
        height: Float,
        heightMode: YGMeasureMode?
    ): YGSize
}