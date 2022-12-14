package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode
import io.github.orioncraftmc.meditate.internal.YGSize
import io.github.orioncraftmc.meditate.internal.enums.YGMeasureMode

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