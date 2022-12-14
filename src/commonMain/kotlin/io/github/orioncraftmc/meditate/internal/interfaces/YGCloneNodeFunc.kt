package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGNode

fun interface YGCloneNodeFunc {
    operator fun invoke(oldNode: YGNode?, owner: YGNode?, childIndex: Int): YGNode
}