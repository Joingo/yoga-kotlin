package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface YGNodeCleanupFunc {
    operator fun invoke(node: YGNode?)
}