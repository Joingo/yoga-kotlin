package com.joingo.yoga.internal.interfaces

import com.joingo.yoga.internal.YGConfig
import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.enums.YGLogLevel

fun interface YGLogger {
    operator fun invoke(
        config: YGConfig?,
        node: YGNode?,
        level: YGLogLevel,
        format: String,
        vararg args: Any?
    ): Int
}