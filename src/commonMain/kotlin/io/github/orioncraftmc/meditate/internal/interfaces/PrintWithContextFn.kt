package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface PrintWithContextFn {
    operator fun invoke(UnnamedParameter: YGNode?, UnnamedParameter2: Any?)
}