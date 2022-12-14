package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.YGSize
import com.joingo.yoga.internal.enums.YGMeasureMode

fun interface YGMeasureFunc {
    operator fun invoke(
        node: YGNode?,
        width: Float,
        widthMode: YGMeasureMode?,
        height: Float,
        heightMode: YGMeasureMode?
    ): YGSize
}