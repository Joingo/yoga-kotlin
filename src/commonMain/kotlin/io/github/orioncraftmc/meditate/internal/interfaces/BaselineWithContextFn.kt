package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface BaselineWithContextFn {
    operator fun invoke(
        UnnamedParameter: YGNode?,
        UnnamedParameter2: Float,
        UnnamedParameter3: Float,
        UnnamedParameter4: Any?
    ): Float
}