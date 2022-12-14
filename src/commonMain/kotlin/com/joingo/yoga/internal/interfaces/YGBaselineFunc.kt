package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode

fun interface YGBaselineFunc {
    operator fun invoke(node: YGNode?, width: Float, height: Float): Float
}