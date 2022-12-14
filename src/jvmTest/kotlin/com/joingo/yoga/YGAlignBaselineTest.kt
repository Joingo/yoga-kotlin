/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

import com.joingo.yoga.YogaNodeFactory.create
import com.joingo.yoga.enums.YogaAlign
import com.joingo.yoga.enums.YogaFlexDirection
import com.joingo.yoga.interfaces.YogaBaselineFunction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class YGAlignBaselineTest {
    private val baselineFunc: YogaBaselineFunction
        private get() = YogaBaselineFunction { node: com.joingo.yoga.YogaNode?, width: Float, height: Float -> height / 2 }

    @Test
    fun test_align_baseline_parent_using_child_in_column_as_reference() {
        val config = com.joingo.yoga.YogaConfigFactory.create()
        val root = createYGNode(config, YogaFlexDirection.ROW, 1000f, 1000f, true)
        val root_child0 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 600f, false)
        root.addChildAt(root_child0, 0)
        val root_child1 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 800f, false)
        root.addChildAt(root_child1, 1)
        val root_child1_child0 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 300f, false)
        root_child1.addChildAt(root_child1_child0, 0)
        val root_child1_child1 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 400f, false)
        root_child1_child1.setBaselineFunction(baselineFunc)
        root_child1_child1.setIsReferenceBaseline(true)
        root_child1.addChildAt(root_child1_child1, 1)
        root.calculateLayout(com.joingo.yoga.YogaConstants.UNDEFINED, com.joingo.yoga.YogaConstants.UNDEFINED)
        Assertions.assertEquals(0f, root_child0.getLayoutX(), 0.0f)
        Assertions.assertEquals(0f, root_child0.getLayoutY(), 0.0f)
        Assertions.assertEquals(500f, root_child1.getLayoutX(), 0.0f)
        Assertions.assertEquals(100f, root_child1.getLayoutY(), 0.0f)
        Assertions.assertEquals(0f, root_child1_child0.getLayoutX(), 0.0f)
        Assertions.assertEquals(0f, root_child1_child0.getLayoutY(), 0.0f)
        Assertions.assertEquals(0f, root_child1_child1.getLayoutX(), 0.0f)
        Assertions.assertEquals(300f, root_child1_child1.getLayoutY(), 0.0f)
    }

    @Test
    fun test_align_baseline_parent_using_child_in_row_as_reference() {
        val config = com.joingo.yoga.YogaConfigFactory.create()
        val root = createYGNode(config, YogaFlexDirection.ROW, 1000f, 1000f, true)
        val root_child0 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 600f, false)
        root.addChildAt(root_child0, 0)
        val root_child1 = createYGNode(config, YogaFlexDirection.ROW, 500f, 800f, true)
        root.addChildAt(root_child1, 1)
        val root_child1_child0 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 500f, false)
        root_child1.addChildAt(root_child1_child0, 0)
        val root_child1_child1 = createYGNode(config, YogaFlexDirection.COLUMN, 500f, 400f, false)
        root_child1_child1.setBaselineFunction(baselineFunc)
        root_child1_child1.setIsReferenceBaseline(true)
        root_child1.addChildAt(root_child1_child1, 1)
        root.calculateLayout(com.joingo.yoga.YogaConstants.UNDEFINED, com.joingo.yoga.YogaConstants.UNDEFINED)
        Assertions.assertEquals(0f, root_child0.getLayoutX(), 0.0f)
        Assertions.assertEquals(0f, root_child0.getLayoutY(), 0.0f)
        Assertions.assertEquals(500f, root_child1.getLayoutX(), 0.0f)
        Assertions.assertEquals(100f, root_child1.getLayoutY(), 0.0f)
        Assertions.assertEquals(0f, root_child1_child0.getLayoutX(), 0.0f)
        Assertions.assertEquals(0f, root_child1_child0.getLayoutY(), 0.0f)
        Assertions.assertEquals(500f, root_child1_child1.getLayoutX(), 0.0f)
        Assertions.assertEquals(300f, root_child1_child1.getLayoutY(), 0.0f)
    }

    private fun createYGNode(
        config: com.joingo.yoga.YogaConfig,
        flexDirection: YogaFlexDirection,
        width: Float,
        height: Float,
        alignBaseline: Boolean
    ): com.joingo.yoga.YogaNode {
        val node = create(config)
        node.setFlexDirection(flexDirection)
        node.setWidth(width)
        node.setHeight(height)
        if (alignBaseline) {
            node.setAlignItems(YogaAlign.BASELINE)
        }
        return node
    }
}