package com.joingo.yoga.internal.detail

import com.joingo.yoga.internal.YGConfig
import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.enums.YGLogLevel

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