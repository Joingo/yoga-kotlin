/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

import com.joingo.yoga.internal.YGSize

/**
 * Helpers for building measure output value.
 */
object YogaMeasureOutput {
    fun make(width: Float, height: Float): YGSize {
        return YGSize(width, height)
    }

    fun make(width: Int, height: Int): YGSize {
        return make(width.toFloat(), height.toFloat())
    }

    fun getWidth(measureOutput: YGSize): Float {
        return measureOutput.width
    }

    fun getHeight(measureOutput: YGSize): Float {
        return measureOutput.height
    }
}