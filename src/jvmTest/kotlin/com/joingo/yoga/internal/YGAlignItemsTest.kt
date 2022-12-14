package com.joingo.yoga.internal

import com.joingo.yoga.internal.GlobalMembers.YGConfigFree
import com.joingo.yoga.internal.GlobalMembers.YGConfigNew
import com.joingo.yoga.internal.GlobalMembers.YGNodeCalculateLayout
import com.joingo.yoga.internal.GlobalMembers.YGNodeFreeRecursive
import com.joingo.yoga.internal.GlobalMembers.YGNodeInsertChild
import com.joingo.yoga.internal.GlobalMembers.YGNodeLayoutGetHeight
import com.joingo.yoga.internal.GlobalMembers.YGNodeLayoutGetLeft
import com.joingo.yoga.internal.GlobalMembers.YGNodeLayoutGetTop
import com.joingo.yoga.internal.GlobalMembers.YGNodeLayoutGetWidth
import com.joingo.yoga.internal.GlobalMembers.YGNodeNewWithConfig
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetAlignItems
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetAlignSelf
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetFlexDirection
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetFlexGrow
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetFlexShrink
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetFlexWrap
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetHeight
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetJustifyContent
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetMargin
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetPadding
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetPosition
import com.joingo.yoga.internal.GlobalMembers.YGNodeStyleSetWidth
import com.joingo.yoga.internal.enums.*
import org.junit.jupiter.api.Test

class YGAlignItemsTest : YogaTest() {
    @Test
    fun align_items_stretch() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
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
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
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
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_center() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
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
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
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
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_flex_start() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignFlexStart)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
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
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
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
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_flex_end() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignFlexEnd)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
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
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
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
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_multiline() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 60f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetFlexWrap(root_child1, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 25f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 25f)
        YGNodeStyleSetHeight(root_child1_child0, 20f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child1, 25f)
        YGNodeStyleSetHeight(root_child1_child1, 10f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        val root_child1_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child2, 25f)
        YGNodeStyleSetHeight(root_child1_child2, 20f)
        YGNodeInsertChild(root_child1, root_child1_child2, 2)
        val root_child1_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child3, 25f)
        YGNodeStyleSetHeight(root_child1_child3, 10f)
        YGNodeInsertChild(root_child1, root_child1_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_multiline_override() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 60f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetFlexWrap(root_child1, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 25f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 25f)
        YGNodeStyleSetHeight(root_child1_child0, 20f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child1_child1, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root_child1_child1, 25f)
        YGNodeStyleSetHeight(root_child1_child1, 10f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        val root_child1_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child2, 25f)
        YGNodeStyleSetHeight(root_child1_child2, 20f)
        YGNodeInsertChild(root_child1, root_child1_child2, 2)
        val root_child1_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child1_child3, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root_child1_child3, 25f)
        YGNodeStyleSetHeight(root_child1_child3, 10f)
        YGNodeInsertChild(root_child1, root_child1_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_multiline_no_override_on_secondline() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 60f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root_child1, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetFlexWrap(root_child1, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 25f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 25f)
        YGNodeStyleSetHeight(root_child1_child0, 20f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child1_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child1, 25f)
        YGNodeStyleSetHeight(root_child1_child1, 10f)
        YGNodeInsertChild(root_child1, root_child1_child1, 1)
        val root_child1_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child2, 25f)
        YGNodeStyleSetHeight(root_child1_child2, 20f)
        YGNodeInsertChild(root_child1, root_child1_child2, 2)
        val root_child1_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignSelf(root_child1_child3, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root_child1_child3, 25f)
        YGNodeStyleSetHeight(root_child1_child3, 10f)
        YGNodeInsertChild(root_child1, root_child1_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child1))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetLeft(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child2))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root_child1_child3))
        ASSERT_FLOAT_EQ(25f, YGNodeLayoutGetWidth(root_child1_child3))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_top() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPosition(root_child0, YGEdge.YGEdgeTop, 10f)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_top2() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPosition(root_child1, YGEdge.YGEdgeTop, 5f)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_double_nested_child() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0, 50f)
        YGNodeStyleSetHeight(root_child0_child0, 20f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 15f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(15f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(15f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_column() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_margin() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root_child0, YGEdge.YGEdgeLeft, 5f)
        YGNodeStyleSetMargin(root_child0, YGEdge.YGEdgeTop, 5f)
        YGNodeStyleSetMargin(root_child0, YGEdge.YGEdgeRight, 5f)
        YGNodeStyleSetMargin(root_child0, YGEdge.YGEdgeBottom, 5f)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root_child1_child0, YGEdge.YGEdgeLeft, 1f)
        YGNodeStyleSetMargin(root_child1_child0, YGEdge.YGEdgeTop, 1f)
        YGNodeStyleSetMargin(root_child1_child0, YGEdge.YGEdgeRight, 1f)
        YGNodeStyleSetMargin(root_child1_child0, YGEdge.YGEdgeBottom, 1f)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(44f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(1f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(1f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(44f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(-1f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(1f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_child_padding() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeLeft, 5f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeTop, 5f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeRight, 5f)
        YGNodeStyleSetPadding(root, YGEdge.YGEdgeBottom, 5f)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeLeft, 5f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeTop, 5f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeRight, 5f)
        YGNodeStyleSetPadding(root_child1, YGEdge.YGEdgeBottom, 5f)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
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
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(55f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
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
        ASSERT_FLOAT_EQ(45f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(-5f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(-5f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(5f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_multiline() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 20f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2, 50f)
        YGNodeStyleSetHeight(root_child2, 20f)
        YGNodeInsertChild(root, root_child2, 2)
        val root_child2_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2_child0, 50f)
        YGNodeStyleSetHeight(root_child2_child0, 10f)
        YGNodeInsertChild(root_child2, root_child2_child0, 0)
        val root_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child3, 50f)
        YGNodeStyleSetHeight(root_child3, 50f)
        YGNodeInsertChild(root, root_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(60f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_multiline_column() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 30f)
        YGNodeStyleSetHeight(root_child1, 50f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 20f)
        YGNodeStyleSetHeight(root_child1_child0, 20f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2, 40f)
        YGNodeStyleSetHeight(root_child2, 70f)
        YGNodeInsertChild(root, root_child2, 2)
        val root_child2_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2_child0, 10f)
        YGNodeStyleSetHeight(root_child2_child0, 10f)
        YGNodeInsertChild(root_child2, root_child2_child0, 0)
        val root_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child3, 50f)
        YGNodeStyleSetHeight(root_child3, 20f)
        YGNodeInsertChild(root, root_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_multiline_column2() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 30f)
        YGNodeStyleSetHeight(root_child1, 50f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 20f)
        YGNodeStyleSetHeight(root_child1_child0, 20f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2, 40f)
        YGNodeStyleSetHeight(root_child2, 70f)
        YGNodeInsertChild(root, root_child2, 2)
        val root_child2_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2_child0, 10f)
        YGNodeStyleSetHeight(root_child2_child0, 10f)
        YGNodeInsertChild(root_child2, root_child2_child0, 0)
        val root_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child3, 50f)
        YGNodeStyleSetHeight(root_child3, 20f)
        YGNodeInsertChild(root, root_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(30f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(70f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_baseline_multiline_row_and_column() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexDirection(root, YGFlexDirection.YGFlexDirectionRow)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignBaseline)
        YGNodeStyleSetFlexWrap(root, YGWrap.YGWrapWrap)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0, 50f)
        YGNodeStyleSetHeight(root_child0, 50f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child1 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1, 50f)
        YGNodeStyleSetHeight(root_child1, 50f)
        YGNodeInsertChild(root, root_child1, 1)
        val root_child1_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child1_child0, 50f)
        YGNodeStyleSetHeight(root_child1_child0, 10f)
        YGNodeInsertChild(root_child1, root_child1_child0, 0)
        val root_child2 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2, 50f)
        YGNodeStyleSetHeight(root_child2, 20f)
        YGNodeInsertChild(root, root_child2, 2)
        val root_child2_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child2_child0, 50f)
        YGNodeStyleSetHeight(root_child2_child0, 10f)
        YGNodeInsertChild(root_child2, root_child2_child0, 0)
        val root_child3 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child3, 50f)
        YGNodeStyleSetHeight(root_child3, 20f)
        YGNodeInsertChild(root, root_child3, 3)
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
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
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetTop(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetHeight(root_child1))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child1_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child1_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child1_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetLeft(root_child2))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetTop(root_child2))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child2))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child2_child0))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child2_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetHeight(root_child2_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child3))
        ASSERT_FLOAT_EQ(90f, YGNodeLayoutGetTop(root_child3))
        ASSERT_FLOAT_EQ(50f, YGNodeLayoutGetWidth(root_child3))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child3))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_center_child_with_margin_bigger_than_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetWidth(root, 52f)
        YGNodeStyleSetHeight(root, 52f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignCenter)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root_child0_child0, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetMargin(root_child0_child0, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetWidth(root_child0_child0, 52f)
        YGNodeStyleSetHeight(root_child0_child0, 52f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_flex_end_child_with_margin_bigger_than_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetWidth(root, 52f)
        YGNodeStyleSetHeight(root, 52f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignFlexEnd)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root_child0_child0, YGEdge.YGEdgeLeft, 10f)
        YGNodeStyleSetMargin(root_child0_child0, YGEdge.YGEdgeRight, 10f)
        YGNodeStyleSetWidth(root_child0_child0, 52f)
        YGNodeStyleSetHeight(root_child0_child0, 52f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(10f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_center_child_without_margin_bigger_than_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetWidth(root, 52f)
        YGNodeStyleSetHeight(root, 52f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignCenter)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0, 72f)
        YGNodeStyleSetHeight(root_child0_child0, 72f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_items_flex_end_child_without_margin_bigger_than_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root, YGJustify.YGJustifyCenter)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetWidth(root, 52f)
        YGNodeStyleSetHeight(root, 52f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignFlexEnd)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0, 72f)
        YGNodeStyleSetHeight(root_child0_child0, 72f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(52f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(-10f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(72f, YGNodeLayoutGetHeight(root_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_center_should_size_based_on_content() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root, YGAlign.YGAlignCenter)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeTop, 20f)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root_child0, YGJustify.YGJustifyCenter)
        YGNodeStyleSetFlexShrink(root_child0, 1f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0, 1f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child0_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0_child0, 20f)
        YGNodeStyleSetHeight(root_child0_child0_child0, 20f)
        YGNodeInsertChild(root_child0_child0, root_child0_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(40f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_strech_should_size_based_on_parent() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetMargin(root, YGEdge.YGEdgeTop, 20f)
        YGNodeStyleSetWidth(root, 100f)
        YGNodeStyleSetHeight(root, 100f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetJustifyContent(root_child0, YGJustify.YGJustifyCenter)
        YGNodeStyleSetFlexShrink(root_child0, 1f)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0, 1f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child0_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root_child0_child0_child0, 20f)
        YGNodeStyleSetHeight(root_child0_child0_child0, 20f)
        YGNodeInsertChild(root_child0_child0, root_child0_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(100f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(80f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(20f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_flex_start_with_shrinking_children() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 500f)
        YGNodeStyleSetHeight(root, 500f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignFlexStart)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0, 1f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child0_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0_child0, 1f)
        YGNodeInsertChild(root_child0_child0, root_child0_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_flex_start_with_stretching_children() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 500f)
        YGNodeStyleSetHeight(root, 500f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0, 1f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child0_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0_child0, 1f)
        YGNodeInsertChild(root_child0_child0, root_child0_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }

    @Test
    fun align_flex_start_with_shrinking_children_with_stretch() {
        val config = YGConfigNew()
        val root = YGNodeNewWithConfig(config)
        YGNodeStyleSetWidth(root, 500f)
        YGNodeStyleSetHeight(root, 500f)
        val root_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetAlignItems(root_child0, YGAlign.YGAlignFlexStart)
        YGNodeInsertChild(root, root_child0, 0)
        val root_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0, 1f)
        YGNodeInsertChild(root_child0, root_child0_child0, 0)
        val root_child0_child0_child0 = YGNodeNewWithConfig(config)
        YGNodeStyleSetFlexGrow(root_child0_child0_child0, 1f)
        YGNodeStyleSetFlexShrink(root_child0_child0_child0, 1f)
        YGNodeInsertChild(root_child0_child0, root_child0_child0_child0, 0)
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionLTR
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeCalculateLayout(
            root,
            GlobalMembers.YGUndefined,
            GlobalMembers.YGUndefined,
            YGDirection.YGDirectionRTL
        )
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetHeight(root))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetWidth(root_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0))
        ASSERT_FLOAT_EQ(500f, YGNodeLayoutGetLeft(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetLeft(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetTop(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetWidth(root_child0_child0_child0))
        ASSERT_FLOAT_EQ(0f, YGNodeLayoutGetHeight(root_child0_child0_child0))
        YGNodeFreeRecursive(root)
        YGConfigFree(config)
    }
}