/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga.interfaces

import com.joingo.yoga.enums.YogaLogLevel
import com.joingo.yoga.internal.YGConfig
import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.enums.YGLogLevel
import com.joingo.yoga.internal.interfaces.YGLogger

/**
 * Interface for receiving logs from native layer. Use by setting YogaNode.setLogger(myLogger);
 * See YogaLogLevel for the different log levels.
 */
interface YogaLogger : YGLogger {
    fun log(level: YogaLogLevel?, message: String?)
    override fun invoke(
        config: YGConfig?,
        node: YGNode?,
        level: YGLogLevel,
        format: String,
        vararg args: Any?
    ): Int {
        // FIXME: Uncomment, port to common
        //log(YogaLogLevel.Companion.fromInt(level.getValue()), String.format(format!!, *args))
        return 0
    }
}