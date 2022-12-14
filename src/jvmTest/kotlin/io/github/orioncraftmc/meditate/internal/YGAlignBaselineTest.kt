package io.github.orioncraftmc.meditate.internal

import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGConfigFree
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGConfigNew
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeCalculateLayout
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeFreeRecursive
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeInsertChild
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeLayoutGetHeight
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeLayoutGetLeft
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeLayoutGetTop
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeLayoutGetWidth
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeNewWithConfig
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeSetBaselineFunc
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeSetIsReferenceBaseline
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeSetMeasureFunc
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetAlignContent
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetAlignItems
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexDirection
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexGrow
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexShrink
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetHeight
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetMargin
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetMaxHeight
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetMinHeight
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetPadding
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetWidth
import io.github.orioncraftmc.meditate.internal.enums.*
import org.junit.jupiter.api.Test

class YGAlignBaselineTest : YogaTest() {
    // Test case for bug in T32999822
    @Test
    fun align_baseline_parent_ht_not_specified() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignContent(root, YGAlign.YGAlignStretch)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 340f)
        YGNodeStyleSetMaxHeight(root, 170f)
        YGNodeStyleSetMinHeight(root, 0f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0, 0f)
        YGNodeStyleSetFlexShrink(root_child0, 1f)
        YGNodeSetMeasureFunc(root_child0) { node: YGNode?, width: Float, widthMode: YGMeasureMode?, height: Float, heightMode: YGMeasureMode? ->
            _measure1(
                node,
                width,
                widthMode,
                height,
                heightMode
            )
        }
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child1, 0f)
        YGNodeStyleSetFlexShrink(root_child1, 1f)
        YGNodeSetMeasureFunc(root_child1) { node: YGNode?, width: Float, widthMode: YGMeasureMode?, height: Float, heightMode: YGMeasureMode? ->
            _measure2(
                node,
                width,
                widthMode,
                height,
                heightMode
            )
        }
        YGNodeInsertChild(root, root_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(340f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(126f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(42f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(76f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(42f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(279f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(126f, YGNodeLayoutGetHeight(root_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_with_no_parent_ht() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 150f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 40f)
        YGNodeSetBaselineFunc(root_child1) { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeInsertChild(root, root_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(150f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_with_no_baseline_func_and_no_parent_ht() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 150f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 80f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 50f)
        YGNodeInsertChild(root, root_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(150f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_column_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_with_padding_in_column_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_with_padding_using_child_in_column_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(400f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_with_margin_using_child_in_column_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeStyleSetMargin(root_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetMargin(root_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetMargin(root_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetMargin(root_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(600f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_with_margin_in_column_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(400f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_row_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 500, 800, true)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_with_padding_in_row_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 500, 800, true)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetPadding(root_child1_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_with_margin_in_row_as_reference() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 500, 800, true)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeLeft, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeRight, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeTop, 100f)
        YGNodeStyleSetMargin(root_child1_child1, YGEdge.YGEdgeBottom, 100f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(600f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_column_as_reference_with_no_baseline_func() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 800, false)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_row_as_reference_with_no_baseline_func() {
        val config = YGConfigNew()
        val root = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 1000, 1000, true)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = createYGNode(config, YGFlexDirection.YGFlexDirectionRow, 500, 800, true)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_column_as_reference_with_height_not_specified() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 1000f)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionColumn)
        YGNodeStyleSetWidth(root_child1, 500f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(800f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(700f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_row_as_reference_with_height_not_specified() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 1000f)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetWidth(root_child1, 500f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        root_child1_child1.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            _baselineFunc(
                node,
                width,
                height
            )
        }
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(900f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(400f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_column_as_reference_with_no_baseline_func_and_height_not_specified() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 1000f)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionColumn)
        YGNodeStyleSetWidth(root_child1, 500f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 300, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(700f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(700f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(300f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_parent_using_child_in_row_as_reference_with_no_baseline_func_and_height_not_specified() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 1000f)
        val root_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 600, false)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetWidth(root_child1, 500f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 500, false)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 =
            createYGNode(config, YGFlexDirection.YGFlexDirectionColumn, 500, 400, false)
        YGNodeSetIsReferenceBaseline(root_child1_child1, true)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(700f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(200f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    companion object {
        private fun _baselineFunc(node: YGNode?, width: Float, height: Float): Float {
            return height / 2
        }

        private fun _measure1(
            node: YGNode?,
            width: Float,
            widthMode: YGMeasureMode?,
            height: Float,
            heightMode: YGMeasureMode?
        ): YGSize {
            return YGSize(42f, 50f)
        }

        private fun _measure2(
            node: YGNode?,
            width: Float,
            widthMode: YGMeasureMode?,
            height: Float,
            heightMode: YGMeasureMode?
        ): YGSize {
            return YGSize(279f, 126f)
        }

        private fun createYGNode(
            config: YGConfig,
            direction: YGFlexDirection,
            width: Int,
            height: Int,
            alignBaseline: Boolean
        ): YGNode {
            val node = YGNodeNewWithConfig(config)
            YGNodeStyleSetFlexDirection(node, direction)
            if (alignBaseline) {
                YGNodeStyleSetAlignItems(node, YGAlign.YGAlignBaseline)
            }
            YGNodeStyleSetWidth(node, width.toFloat())
            YGNodeStyleSetHeight(node, height.toFloat())
            return YGNode(node)
        }
    }
}