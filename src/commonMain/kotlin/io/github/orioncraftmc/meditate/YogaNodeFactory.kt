/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.github.orioncraftmc.meditate

object YogaNodeFactory {
    fun create(): YogaNode {
        return YogaNodeWrapper()
    }

    @kotlin.jvm.JvmStatic
    fun create(config: YogaConfig): YogaNode {
        return YogaNodeWrapper(config)
    }
}