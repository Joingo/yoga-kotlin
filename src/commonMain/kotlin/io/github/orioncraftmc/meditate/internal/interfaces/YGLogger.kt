package io.github.orioncraftmc.meditate.internal.interfaces

import io.github.orioncraftmc.meditate.internal.YGConfig
import io.github.orioncraftmc.meditate.internal.YGNode
import io.github.orioncraftmc.meditate.internal.enums.YGLogLevel

fun interface YGLogger {
    operator fun invoke(
        config: YGConfig?,
        node: YGNode?,
        level: YGLogLevel,
        format: String,
        vararg args: Any?
    ): Int
}