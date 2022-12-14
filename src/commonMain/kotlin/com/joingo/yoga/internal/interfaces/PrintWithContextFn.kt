package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGNode

fun interface PrintWithContextFn {
    operator fun invoke(UnnamedParameter: YGNode?, UnnamedParameter2: Any?)
}