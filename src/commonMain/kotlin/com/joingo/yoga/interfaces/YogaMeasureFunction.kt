/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga.interfaces

import com.joingo.yoga.YogaNode
import com.joingo.yoga.enums.YogaMeasureMode
import com.joingo.yoga.internal.YGSize

interface YogaMeasureFunction {
    /**
     * Return a value created by YogaMeasureOutput.make(width, height);
     */
    fun measure(
        node: com.joingo.yoga.YogaNode?,
        width: Float,
        widthMode: YogaMeasureMode?,
        height: Float,
        heightMode: YogaMeasureMode?
    ): YGSize?
}