/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

object YogaNodeFactory {
    fun create(): com.joingo.yoga.YogaNode {
        return com.joingo.yoga.YogaNodeWrapper()
    }

    @kotlin.jvm.JvmStatic
    fun create(config: com.joingo.yoga.YogaConfig): com.joingo.yoga.YogaNode {
        return com.joingo.yoga.YogaNodeWrapper(config)
    }
}