package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode

fun interface BaselineWithContextFn {
    operator fun invoke(
        UnnamedParameter: YGNode?,
        UnnamedParameter2: Float,
        UnnamedParameter3: Float,
        UnnamedParameter4: Any?
    ): Float
}