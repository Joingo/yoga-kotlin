package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode

fun interface YGCloneNodeFunc {
    operator fun invoke(oldNode: YGNode?, owner: YGNode?, childIndex: Int): YGNode
}