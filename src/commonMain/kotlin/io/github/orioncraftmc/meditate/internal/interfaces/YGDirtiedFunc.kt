package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface YGDirtiedFunc {
    operator fun invoke(node: YGNode?)
}