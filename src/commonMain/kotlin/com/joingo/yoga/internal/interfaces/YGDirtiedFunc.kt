package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode

fun interface YGDirtiedFunc {
    operator fun invoke(node: YGNode?)
}