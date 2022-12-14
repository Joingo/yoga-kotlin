package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.YGSize
import com.joingo.yoga.internal.enums.YGMeasureMode

fun interface MeasureWithContextFn {
    operator fun invoke(
        UnnamedParameter: YGNode?,
        UnnamedParameter2: Float,
        UnnamedParameter3: YGMeasureMode?,
        UnnamedParameter4: Float,
        UnnamedParameter5: YGMeasureMode?,
        UnnamedParameter6: Any?
    ): YGSize
}