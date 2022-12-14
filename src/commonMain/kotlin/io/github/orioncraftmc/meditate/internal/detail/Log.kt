package io.github.orioncraftmc.meditate.internal.detail

import io.github.orioncraftmc.meditate.internal.YGConfig
import io.github.orioncraftmc.meditate.internal.YGNode
import io.github.orioncraftmc.meditate.internal.enums.YGLogLevel

//struct YGNode;
//struct YGConfig;
object Log {
    fun log(node: YGNode?, level: YGLogLevel, context: Any?, format: String, vararg args: Any?) {
        GlobalMembers.Companion.vlog(node?.getConfig(), node, level, context, format, *args)
    }

    fun log(
        config: YGConfig?,
        level: YGLogLevel,
        context: Any?,
        format: String,
        vararg args: Any?
    ) {
        GlobalMembers.Companion.vlog(config, null, level, context, format, *args)
    }
}