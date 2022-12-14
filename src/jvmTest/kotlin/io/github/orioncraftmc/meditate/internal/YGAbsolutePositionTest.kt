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
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetAlignItems
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetAlignSelf
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetBorder
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexDirection
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexGrow
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetFlexWrap
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetHeight
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetJustifyContent
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetMargin
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetOverflow
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetPadding
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetPosition
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetPositionPercent
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetPositionType
import io.github.orioncraftmc.meditate.internal.GlobalMembers.YGNodeStyleSetWidth
import io.github.orioncraftmc.meditate.internal.enums.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class YGAbsolutePositionTest : YogaTest() {
    @Test
    fun absolute_layout_width_height_start_top() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeStart, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetWidth(root_child0, 10f)
        YGNodeStyleSetHeight(root_child0, 10f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        Assertions.assertEquals(0f, YGNodeLayoutGetLeft(root))
        Assertions.assertEquals(0f, YGNodeLayoutGetTop(root))
        Assertions.assertEquals(100f, YGNodeLayoutGetWidth(root))
        Assertions.assertEquals(100f, YGNodeLayoutGetHeight(root))
        Assertions.assertEquals(10f, YGNodeLayoutGetLeft(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetTop(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetWidth(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        Assertions.assertEquals(0f, YGNodeLayoutGetLeft(root))
        Assertions.assertEquals(0f, YGNodeLayoutGetTop(root))
        Assertions.assertEquals(100f, YGNodeLayoutGetWidth(root))
        Assertions.assertEquals(100f, YGNodeLayoutGetHeight(root))
        Assertions.assertEquals(80f, YGNodeLayoutGetLeft(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetTop(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetWidth(root_child0))
        Assertions.assertEquals(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_width_height_end_bottom() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeEnd, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child0, 10f)
        YGNodeStyleSetHeight(root_child0, 10f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_start_top_end_bottom() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeStart, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeEnd, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeBottom, 10f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_width_height_start_top_end_bottom() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeStart, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeEnd, 10f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child0, 10f)
        YGNodeStyleSetHeight(root_child0, 10f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun do_not_clamp_height_of_absolute_node_to_height_of_its_overflow_hidden_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetOverflow(root, YGOverflow.YGOverflowHidden)
        YGNodeStyleSetWidth(root, 50f)
        YGNodeStyleSetHeight(root, 50f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeStart, 0f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 0f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0, 100f)
        YGNodeStyleSetHeight(root_child0_child0, 100f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_within_border() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetBorder(root, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetBorder(root, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetBorder(root, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetBorder(root, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeLeft, 0f)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 0f)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child1, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child1, YGEdge.YGEdgeRight, 0f)
        YGNodeStyleSetPosition(root_child1, YGEdge.YGEdgeBottom, 0f)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 50f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child2, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child2, YGEdge.YGEdgeLeft, 0f)
        YGNodeStyleSetPosition(root_child2, YGEdge.YGEdgeTop, 0f)
        YGNodeStyleSetMargin(root_child2, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetMargin(root_child2, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetMargin(root_child2, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetMargin(root_child2, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child2, 50f)
        YGNodeStyleSetHeight(root_child2, 50f)
        YGNodeInsertChild(root, root_child2, 2)
        val root_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child3, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child3, YGEdge.YGEdgeRight, 0f)
        YGNodeStyleSetPosition(root_child3, YGEdge.YGEdgeBottom, 0f)
        YGNodeStyleSetMargin(root_child3, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetMargin(root_child3, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetMargin(root_child3, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetMargin(root_child3, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child3, 50f)
        YGNodeStyleSetHeight(root_child3, 50f)
        YGNodeInsertChild(root, root_child3, 3)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child3))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_center() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_flex_end() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyFlexEnd)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignFlexEnd)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_justify_content_center() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_center() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_center_on_child_only() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child0, YGAlign.YGAlignCenter)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_center_and_top_position() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_center_and_bottom_position() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_center_and_left_position() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeLeft, 5f)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_align_items_and_justify_content_center_and_right_position() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetFlexGrow(root, 1f)
        YGNodeStyleSetWidth(root, 110f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeRight, 5f)
        YGNodeStyleSetWidth(root_child0, 60f)
        YGNodeStyleSetHeight(root_child0, 40f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(110f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun position_root_with_rtl_should_position_withoutdirection() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetPosition(root, YGEdge.YGEdgeLeft, 72f)
        YGNodeStyleSetWidth(root, 52f)
        YGNodeStyleSetHeight(root, 52f)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_percentage_bottom_based_on_parent_height() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 200f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPositionPercent(root_child0, YGEdge.YGEdgeTop, 50f)
        YGNodeStyleSetWidth(root_child0, 10f)
        YGNodeStyleSetHeight(root_child0, 10f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child1, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPositionPercent(root_child1, YGEdge.YGEdgeBottom, 50f)
        YGNodeStyleSetWidth(root_child1, 10f)
        YGNodeStyleSetHeight(root_child1, 10f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child2, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetPositionPercent(root_child2, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetPositionPercent(root_child2, YGEdge.YGEdgeBottom, 10f)
        YGNodeStyleSetWidth(root_child2, 10f)
        YGNodeInsertChild(root, root_child2, 2)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(200f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(160f, YGNodeLayoutGetHeight(root_child2))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(200f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(160f, YGNodeLayoutGetHeight(root_child2))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_in_wrap_reverse_column_container() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrapReverse)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 20f)
        YGNodeStyleSetHeight(root_child0, 20f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_in_wrap_reverse_row_container() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrapReverse)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 20f)
        YGNodeStyleSetHeight(root_child0, 20f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_in_wrap_reverse_column_container_flex_end() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrapReverse)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child0, YGAlign.YGAlignFlexEnd)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 20f)
        YGNodeStyleSetHeight(root_child0, 20f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun absolute_layout_in_wrap_reverse_row_container_flex_end() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrapReverse)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child0, YGAlign.YGAlignFlexEnd)
        YGNodeStyleSetPositionType(root_child0, YGPositionType.YGPositionTypeAbsolute)
        YGNodeStyleSetWidth(root_child0, 20f)
        YGNodeStyleSetHeight(root_child0, 20f)
        YGNodeInsertChild(root, root_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }
}