/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.github.orioncraftmc.meditate.interfaces

import io.github.orioncraftmc.meditate.YogaNode
import io.github.orioncraftmc.meditate.enums.YogaMeasureMode
import io.github.orioncraftmc.meditate.internal.YGSize

interface YogaMeasureFunction {
    /**
     * Return a value created by YogaMeasureOutput.make(width, height);
     */
    fun measure(
        node: YogaNode?,
        width: Float,
        widthMode: YogaMeasureMode?,
        height: Float,
        heightMode: YogaMeasureMode?
    ): YGSize?
}