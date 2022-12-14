package com.joingo.yoga.internal

import com.joingo.yoga.internal.YGStyle.BitfieldRef
import com.joingo.yoga.internal.detail.CompactValue
import com.joingo.yoga.internal.detail.Log
import com.joingo.yoga.internal.detail.RefObject
import com.joingo.yoga.internal.detail.Values
import com.joingo.yoga.internal.enums.*
import com.joingo.yoga.internal.event.*
import com.joingo.yoga.internal.event.Event.publish
import com.joingo.yoga.internal.interfaces.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass

// FIXME: Use real atomic
internal class AtomicInteger {
    fun incrementAndGet(): Int {
        value += 1
        return value
    }

    fun get(): Int {
        return value
    }

    private var value = 0
}

object GlobalMembers {
    const val YGUndefined = Float.NaN
    val YGValueAuto = YGValue(YGUndefined, YGUnit.YGUnitAuto)
    val YGValueUndefined = YGValue(YGUndefined, YGUnit.YGUnitUndefined)
    val YGValueZero = YGValue(0f, YGUnit.YGUnitPoint)
    val leading = ArrayList(
        listOf(YGEdge.YGEdgeTop, YGEdge.YGEdgeBottom, YGEdge.YGEdgeLeft, YGEdge.YGEdgeRight)
    )
    val trailing = ArrayList(
        listOf(YGEdge.YGEdgeBottom, YGEdge.YGEdgeTop, YGEdge.YGEdgeRight, YGEdge.YGEdgeLeft)
    )
    val pos = ArrayList(
        listOf(YGEdge.YGEdgeTop, YGEdge.YGEdgeBottom, YGEdge.YGEdgeLeft, YGEdge.YGEdgeRight)
    )
    val dim = ArrayList(
        listOf(
            YGDimension.YGDimensionHeight,
            YGDimension.YGDimensionHeight,
            YGDimension.YGDimensionWidth,
            YGDimension.YGDimensionWidth
        )
    )
    const val spacer = "                                                            "
    const val kDefaultFlexGrow = 0.0f
    const val kDefaultFlexShrink = 0.0f
    const val kWebDefaultFlexShrink = 1.0f
    val defaultConfig = YGConfigNew()
    internal val gCurrentGenerationCount = AtomicInteger()
    const val gPrintChanges = false
    const val gPrintSkips = false
    var gConfigInstanceCount = 0
    fun isUndefined(value: Float): Boolean {
        return value.isNaN()
    }

    fun isUndefined(value: Double): Boolean {
        return value.isNaN()
    }

    fun YGValueEqual(a: YGValue, b: YGValue): Boolean //Method definition originates from: Utils.cpp
    {
        if (a.unit != b.unit) {
            return false
        }
        return if (a.unit == YGUnit.YGUnitUndefined || isUndefined(
                a.value
            ) && isUndefined(b.value)
        ) {
            true
        } else abs(a.value - b.value) < 0.0001f
    }

    fun YGValueEqual(a: CompactValue, b: CompactValue): Boolean {
        return YGValueEqual(a.convertToYgValue(), b.convertToYgValue())
    }

    fun YGFloatsEqual(a: Float, b: Float): Boolean //Method definition originates from: Utils.cpp
    {
        return if (!isUndefined(a) && !isUndefined(
                b
            )
        ) {
            abs(a - b) < 0.0001f
        } else isUndefined(a) && isUndefined(
            b
        )
    }

    fun YGDoubleEqual(a: Double, b: Double): Boolean //Method definition originates from: Utils.cpp
    {
        return if (!isUndefined(a) && !isUndefined(
                b
            )
        ) {
            abs(a - b) < 0.0001
        } else isUndefined(a) && isUndefined(
            b
        )
    }

    fun YGFloatMax(a: Float, b: Float): Float //Method definition originates from: Utils.cpp
    {
        if (!isUndefined(a) && !isUndefined(b)) {
            return max(a, b)
        }
        return if (isUndefined(a)) b else a
    }

    fun YGFloatOptionalMax(
        op1: YGFloatOptional,
        op2: YGFloatOptional
    ): YGFloatOptional //Method definition originates from: Utils.cpp
    {
        if (greaterThanOrEqualTo(op1, op2)) {
            return op1
        }
        if (greaterThan(op2, op1)) {
            return op2
        }
        return if (op1.isUndefined()) op2 else op1
    }

    fun YGFloatMin(a: Float, b: Float): Float //Method definition originates from: Utils.cpp
    {
        if (!isUndefined(a) && !isUndefined(b)) {
            return min(a, b)
        }
        return if (isUndefined(a)) b else a
    }

    fun YGFloatArrayEqual(val1: ArrayList<Float>, val2: ArrayList<Float>): Boolean {
        var areEqual = true
        var i = 0
        while (i < val1.size && areEqual) {
            areEqual = YGFloatsEqual(val1[i], val2[i])
            ++i
        }
        return areEqual
    }

    fun YGFloatSanitize(`val`: Float): Float //Method definition originates from: Utils.cpp
    {
        return if (isUndefined(`val`)) 0f else `val`
    }

    fun YGFlexDirectionCross(
        flexDirection: YGFlexDirection?,
        direction: YGDirection?
    ): YGFlexDirection //Method definition originates from: Utils.cpp
    {
        return if (YGFlexDirectionIsColumn(flexDirection)) YGResolveFlexDirection(
            YGFlexDirection.YGFlexDirectionRow,
            direction
        ) else YGFlexDirection.YGFlexDirectionColumn
    }

    fun YGFlexDirectionIsRow(flexDirection: YGFlexDirection): Boolean {
        return flexDirection == YGFlexDirection.YGFlexDirectionRow || flexDirection == YGFlexDirection.YGFlexDirectionRowReverse
    }

    fun YGResolveValue(value: YGValue, ownerSize: Float): YGFloatOptional {
        return when (value.unit) {
            YGUnit.YGUnitPoint -> YGFloatOptional(value.value)
            YGUnit.YGUnitPercent -> YGFloatOptional(value.value * ownerSize * 0.01f)
            else -> YGFloatOptional()
        }
    }

    fun YGResolveValue(value: CompactValue, ownerSize: Float): YGFloatOptional {
        return YGResolveValue(value.convertToYgValue(), ownerSize)
    }

    fun YGFlexDirectionIsColumn(flexDirection: YGFlexDirection?): Boolean {
        return flexDirection == YGFlexDirection.YGFlexDirectionColumn || flexDirection == YGFlexDirection.YGFlexDirectionColumnReverse
    }

    fun YGResolveFlexDirection(
        flexDirection: YGFlexDirection,
        direction: YGDirection?
    ): YGFlexDirection {
        if (direction == YGDirection.YGDirectionRTL) {
            if (flexDirection == YGFlexDirection.YGFlexDirectionRow) {
                return YGFlexDirection.YGFlexDirectionRowReverse
            } else if (flexDirection == YGFlexDirection.YGFlexDirectionRowReverse) {
                return YGFlexDirection.YGFlexDirectionRow
            }
        }
        return flexDirection
    }

    fun YGResolveValueMargin(value: CompactValue, ownerSize: Float): YGFloatOptional {
        return if (value.isAuto()) YGFloatOptional(0f) else YGResolveValue(value, ownerSize)
    }

    fun YGAlignToString(value: YGAlign): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGAlign.YGAlignAuto -> "auto"
            YGAlign.YGAlignFlexStart -> "flex-start"
            YGAlign.YGAlignCenter -> "center"
            YGAlign.YGAlignFlexEnd -> "flex-end"
            YGAlign.YGAlignStretch -> "stretch"
            YGAlign.YGAlignBaseline -> "baseline"
            YGAlign.YGAlignSpaceBetween -> "space-between"
            YGAlign.YGAlignSpaceAround -> "space-around"
        }
    }

    fun LayoutPassReasonToString(value: LayoutPassReason): String {
        if (value == LayoutPassReason.kInitial) {
            return "initial"
        } else if (value == LayoutPassReason.kAbsLayout) {
            return "abs_layout"
        } else if (value == LayoutPassReason.kStretch) {
            return "stretch"
        } else if (value == LayoutPassReason.kMultilineStretch) {
            return "multiline_stretch"
        } else if (value == LayoutPassReason.kFlexLayout) {
            return "flex_layout"
        } else if (value == LayoutPassReason.kMeasureChild) {
            return "measure"
        } else if (value == LayoutPassReason.kAbsMeasureChild) {
            return "abs_measure"
        } else if (value == LayoutPassReason.kFlexMeasure) {
            return "flex_measure"
        }
        return "unknown"
    }

    //					   ;
    fun YGDimensionToString(value: YGDimension): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGDimension.YGDimensionWidth -> "width"
            YGDimension.YGDimensionHeight -> "height"
        }
    }

    fun YGDirectionToString(value: YGDirection): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGDirection.YGDirectionInherit -> "inherit"
            YGDirection.YGDirectionLTR -> "ltr"
            YGDirection.YGDirectionRTL -> "rtl"
        }
    }

    fun YGDisplayToString(value: YGDisplay): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGDisplay.YGDisplayFlex -> "flex"
            YGDisplay.YGDisplayNone -> "none"
        }
    }

    fun YGEdgeToString(value: YGEdge): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGEdge.YGEdgeLeft -> "left"
            YGEdge.YGEdgeTop -> "top"
            YGEdge.YGEdgeRight -> "right"
            YGEdge.YGEdgeBottom -> "bottom"
            YGEdge.YGEdgeStart -> "start"
            YGEdge.YGEdgeEnd -> "end"
            YGEdge.YGEdgeHorizontal -> "horizontal"
            YGEdge.YGEdgeVertical -> "vertical"
            YGEdge.YGEdgeAll -> "all"
        }
    }

    fun YGExperimentalFeatureToString(value: YGExperimentalFeature): String //Method definition originates from: YGEnums.cpp
    {
        return if (value == YGExperimentalFeature.YGExperimentalFeatureWebFlexBasis) {
            "web-flex-basis"
        } else "unknown"
    }

    fun YGFlexDirectionToString(value: YGFlexDirection): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGFlexDirection.YGFlexDirectionColumn -> "column"
            YGFlexDirection.YGFlexDirectionColumnReverse -> "column-reverse"
            YGFlexDirection.YGFlexDirectionRow -> "row"
            YGFlexDirection.YGFlexDirectionRowReverse -> "row-reverse"
        }
    }

    fun YGJustifyToString(value: YGJustify): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGJustify.YGJustifyFlexStart -> "flex-start"
            YGJustify.YGJustifyCenter -> "center"
            YGJustify.YGJustifyFlexEnd -> "flex-end"
            YGJustify.YGJustifySpaceBetween -> "space-between"
            YGJustify.YGJustifySpaceAround -> "space-around"
            YGJustify.YGJustifySpaceEvenly -> "space-evenly"
        }
    }

    fun YGLogLevelToString(value: YGLogLevel): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGLogLevel.YGLogLevelError -> "error"
            YGLogLevel.YGLogLevelWarn -> "warn"
            YGLogLevel.YGLogLevelInfo -> "info"
            YGLogLevel.YGLogLevelDebug -> "debug"
            YGLogLevel.YGLogLevelVerbose -> "verbose"
            YGLogLevel.YGLogLevelFatal -> "fatal"
        }
    }

    fun YGMeasureModeToString(value: YGMeasureMode): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGMeasureMode.YGMeasureModeUndefined -> "undefined"
            YGMeasureMode.YGMeasureModeExactly -> "exactly"
            YGMeasureMode.YGMeasureModeAtMost -> "at-most"
        }
    }

    fun YGNodeTypeToString(value: YGNodeType): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGNodeType.YGNodeTypeDefault -> "default"
            YGNodeType.YGNodeTypeText -> "text"
        }
    }

    fun YGOverflowToString(value: YGOverflow): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGOverflow.YGOverflowVisible -> "visible"
            YGOverflow.YGOverflowHidden -> "hidden"
            YGOverflow.YGOverflowScroll -> "scroll"
        }
    }

    fun YGPositionTypeToString(value: YGPositionType): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGPositionType.YGPositionTypeStatic -> "static"
            YGPositionType.YGPositionTypeRelative -> "relative"
            YGPositionType.YGPositionTypeAbsolute -> "absolute"
        }
    }

    fun YGPrintOptionsToString(value: YGPrintOptions): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGPrintOptions.YGPrintOptionsLayout -> "layout"
            YGPrintOptions.YGPrintOptionsStyle -> "style"
            YGPrintOptions.YGPrintOptionsChildren -> "children"
        }
    }

    fun YGUnitToString(value: YGUnit): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGUnit.YGUnitUndefined -> "undefined"
            YGUnit.YGUnitPoint -> "point"
            YGUnit.YGUnitPercent -> "percent"
            YGUnit.YGUnitAuto -> "auto"
        }
    }

    fun YGWrapToString(value: YGWrap): String //Method definition originates from: YGEnums.cpp
    {
        return when (value) {
            YGWrap.YGWrapNoWrap -> "no-wrap"
            YGWrap.YGWrapWrap -> "wrap"
            YGWrap.YGWrapWrapReverse -> "wrap-reverse"
        }
    }

    fun YGNodeNew(): YGNode //Method definition originates from: Yoga.cpp
    {
        return YGNodeNewWithConfig(YGConfigGetDefault())
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeNewWithConfig(config: YGConfig?): YGNode //Method definition originates from: Yoga.cpp
    {
        val node = YGNode(config!!)
        YGAssertWithConfig(config, node != null, "Could not allocate memory for node")
        Event.publish(node, NodeAllocationEventData(config))
        return node
    }

    fun YGNodeClone(oldNode: YGNode): YGNode //Method definition originates from: Yoga.cpp
    {
        val node = YGNode(oldNode)
        YGAssertWithConfig(oldNode.getConfig(), node != null, "Could not allocate memory for node")
        Event.publish(node, NodeAllocationEventData(node.getConfig()))
        node.setOwner(null)
        return node
    }

    fun YGNodeFree(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        val owner = node.getOwner()
        if (owner != null) {
            owner.removeChild(node)
            node.setOwner(null)
        }
        val childCount = YGNodeGetChildCount(node)
        for (i in 0 until childCount) {
            val child = YGNodeGetChild(node, i)
            if (child != null) {
                child.setOwner(null)
            }
        }
        node.clearChildren()
        Event.publish(node, NodeDeallocationEventData(node.getConfig()))
    }

    fun YGNodeFreeRecursiveWithCleanupFunc(
        root: YGNode,
        cleanup: YGNodeCleanupFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        var skipped = 0
        while (YGNodeGetChildCount(root) > skipped) {
            val child = YGNodeGetChild(root, skipped)
            if (child != null) {
                if (child.getOwner() !== root) {
                    skipped += 1
                } else {
                    YGNodeRemoveChild(root, child)
                    YGNodeFreeRecursive(child)
                }
            }
        }
        cleanup?.invoke(root)
        YGNodeFree(root)
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeFreeRecursive(root: YGNode) //Method definition originates from: Yoga.cpp
    {
        YGNodeFreeRecursiveWithCleanupFunc(root, null)
    }

    fun YGNodeReset(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        node.reset()
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeInsertChild(
        owner: YGNode,
        child: YGNode,
        index: Int?
    ) //Method definition originates from: Yoga.cpp
    {
        YGAssertWithNode(
            owner,
            child.getOwner() == null,
            "Child already has a owner, it must be removed first."
        )
        YGAssertWithNode(
            owner, !owner.hasMeasureFunc(),
            "Cannot add child: Nodes with measure functions cannot have children."
        )
        owner.insertChild(child, index)
        child.setOwner(owner)
        owner.markDirtyAndPropogate()
    }

    fun YGNodeSwapChild(
        owner: YGNode,
        child: YGNode,
        index: Int?
    ) //Method definition originates from: Yoga.cpp
    {
        owner.replaceChild(child, index)
        child.setOwner(owner)
    }

    fun YGNodeRemoveChild(
        owner: YGNode,
        excludedChild: YGNode
    ) //Method definition originates from: Yoga.cpp
    {
        if (YGNodeGetChildCount(owner) == 0) {
            return
        }
        val childOwner = excludedChild.getOwner()
        if (owner.removeChild(excludedChild)) {
            if (owner === childOwner) {
                excludedChild.setLayout(null)
                excludedChild.setOwner(null)
            }
            owner.markDirtyAndPropogate()
        }
    }

    fun YGNodeRemoveAllChildren(owner: YGNode) //Method definition originates from: Yoga.cpp
    {
        val childCount = YGNodeGetChildCount(owner)
        if (childCount == 0) {
            return
        }
        val firstChild = YGNodeGetChild(owner, 0)
        if (firstChild != null && firstChild.getOwner() === owner) {
            for (i in 0 until childCount) {
                val oldChild = YGNodeGetChild(owner, i)
                oldChild!!.setLayout(null)
                oldChild.setOwner(null)
            }
            owner.clearChildren()
            owner.markDirtyAndPropogate()
            return
        }
        owner.getChildren().clear()
        owner.markDirtyAndPropogate()
    }

    fun YGNodeGetChild(
        node: YGNode,
        index: Int
    ): YGNode? //Method definition originates from: Yoga.cpp
    {
        return if (index < node.getChildren().size) {
            node.getChild(index)
        } else null
    }

    fun YGNodeGetOwner(node: YGNode): YGNode? //Method definition originates from: Yoga.cpp
    {
        return node.getOwner()
    }

    fun YGNodeGetParent(node: YGNode): YGNode? //Method definition originates from: Yoga.cpp
    {
        return node.getOwner()
    }

    fun YGNodeGetChildCount(node: YGNode): Int //Method definition originates from: Yoga.cpp
    {
        return node.getChildren().size
    }

    fun YGNodeSetChildren(
        owner: YGNode?,
        c: Array<YGNode>,
        count: Int?
    ) //Method definition originates from: Yoga.cpp
    {
        val children = ArrayList(c.asList())
        YGNodeSetChildrenInternal(owner, children)
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeSetIsReferenceBaseline(
        node: YGNode,
        isReferenceBaseline: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        if (node.isReferenceBaseline() != isReferenceBaseline) {
            node.setIsReferenceBaseline(isReferenceBaseline)
            node.markDirtyAndPropogate()
        }
    }

    fun YGNodeIsReferenceBaseline(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.isReferenceBaseline()
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeCalculateLayout(
        node: YGNode,
        ownerWidth: Float,
        ownerHeight: Float,
        ownerDirection: YGDirection
    ) //Method definition originates from: Yoga.cpp
    {
        YGNodeCalculateLayoutWithContext(node, ownerWidth, ownerHeight, ownerDirection, null)
    }

    fun YGNodeMarkDirty(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        YGAssertWithNode(
            node, node.hasMeasureFunc(),
            "Only leaf nodes with custom measure functions" + "should manually mark themselves as dirty"
        )
        node.markDirtyAndPropogate()
    }

    fun YGNodeMarkDirtyAndPropogateToDescendants(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        node.markDirtyAndPropogateDownwards()
    }

    fun YGFloatIsUndefined(value: Float): Boolean //Method definition originates from: Yoga.cpp
    {
        return isUndefined(value)
    }

    fun YGNodeCanUseCachedMeasurement(
        widthMode: YGMeasureMode,
        width: Float,
        heightMode: YGMeasureMode,
        height: Float,
        lastWidthMode: YGMeasureMode?,
        lastWidth: Float,
        lastHeightMode: YGMeasureMode?,
        lastHeight: Float,
        lastComputedWidth: Float,
        lastComputedHeight: Float,
        marginRow: Float,
        marginColumn: Float,
        config: YGConfig?
    ): Boolean //Method definition originates from: Yoga.cpp
    {
        if (!YGFloatIsUndefined(lastComputedHeight) && lastComputedHeight < 0f || !YGFloatIsUndefined(
                lastComputedWidth
            ) && lastComputedWidth < 0f
        ) {
            return false
        }
        val useRoundedComparison = config != null && config.pointScaleFactor != 0f
        val effectiveWidth = if (useRoundedComparison) YGRoundValueToPixelGrid(
            width.toDouble(), config!!.pointScaleFactor.toDouble(),
            false, false
        ) else width
        val effectiveHeight = if (useRoundedComparison) YGRoundValueToPixelGrid(
            height.toDouble(), config!!.pointScaleFactor.toDouble(),
            false, false
        ) else height
        val effectiveLastWidth = if (useRoundedComparison) YGRoundValueToPixelGrid(
            lastWidth.toDouble(),
            config!!.pointScaleFactor.toDouble(), false, false
        ) else lastWidth
        val effectiveLastHeight = if (useRoundedComparison) YGRoundValueToPixelGrid(
            lastHeight.toDouble(),
            config!!.pointScaleFactor.toDouble(), false, false
        ) else lastHeight
        val hasSameWidthSpec = lastWidthMode == widthMode && YGFloatsEqual(
            effectiveLastWidth,
            effectiveWidth
        )
        val hasSameHeightSpec = lastHeightMode == heightMode && YGFloatsEqual(
            effectiveLastHeight,
            effectiveHeight
        )
        val widthIsCompatible =
            hasSameWidthSpec || YGMeasureModeSizeIsExactAndMatchesOldMeasuredSize(
                widthMode, width - marginRow, lastComputedWidth
            ) || YGMeasureModeOldSizeIsUnspecifiedAndStillFits(
                widthMode, width - marginRow, lastWidthMode,
                lastComputedWidth
            ) || YGMeasureModeNewMeasureSizeIsStricterAndStillValid(
                widthMode, width - marginRow,
                lastWidthMode, lastWidth, lastComputedWidth
            )
        val heightIsCompatible =
            hasSameHeightSpec || YGMeasureModeSizeIsExactAndMatchesOldMeasuredSize(
                heightMode, height - marginColumn, lastComputedHeight
            ) || YGMeasureModeOldSizeIsUnspecifiedAndStillFits(
                heightMode, height - marginColumn, lastHeightMode,
                lastComputedHeight
            ) || YGMeasureModeNewMeasureSizeIsStricterAndStillValid(
                heightMode,
                height - marginColumn, lastHeightMode, lastHeight, lastComputedHeight
            )
        return widthIsCompatible && heightIsCompatible
    }

    fun YGNodeCopyStyle(
        dstNode: YGNode,
        srcNode: YGNode
    ) //Method definition originates from: Yoga.cpp
    {
        if (!(dstNode.getStyle() === srcNode.getStyle())) {
            dstNode.setStyle(srcNode.getStyle())
            dstNode.markDirtyAndPropogate()
        }
    }

    fun YGNodeGetContext(node: YGNode): Any? //Method definition originates from: Yoga.cpp
    {
        return node.getContext()
    }

    fun YGNodeSetContext(node: YGNode, context: Any?) //Method definition originates from: Yoga.cpp
    {
        node.setContext(context)
    }

    fun YGConfigSetPrintTreeFlag(
        config: YGConfig,
        enabled: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        config.printTree = enabled
    }

    fun YGNodeHasMeasureFunc(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.hasMeasureFunc()
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeSetMeasureFunc(
        node: YGNode,
        measureFunc: YGMeasureFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        node.setMeasureFunc(measureFunc)
    }

    fun YGNodeHasBaselineFunc(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.hasBaselineFunc()
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeSetBaselineFunc(
        node: YGNode,
        baselineFunc: YGBaselineFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        node.setBaselineFunc(baselineFunc)
    }

    fun YGNodeGetDirtiedFunc(node: YGNode): YGDirtiedFunc? //Method definition originates from: Yoga.cpp
    {
        return node.getDirtied()
    }

    fun YGNodeSetDirtiedFunc(
        node: YGNode,
        dirtiedFunc: YGDirtiedFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        node.setDirtiedFunc(dirtiedFunc)
    }

    fun YGNodeSetPrintFunc(
        node: YGNode,
        printFunc: YGPrintFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        node.setPrintFunc(printFunc)
    }

    fun YGNodeGetHasNewLayout(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.getHasNewLayout()
    }

    fun YGNodeSetHasNewLayout(
        node: YGNode,
        hasNewLayout: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        node.setHasNewLayout(hasNewLayout)
    }

    fun YGNodeGetNodeType(node: YGNode): YGNodeType? //Method definition originates from: Yoga.cpp
    {
        return node.getNodeType()
    }

    fun YGNodeSetNodeType(
        node: YGNode,
        nodeType: YGNodeType
    ) //Method definition originates from: Yoga.cpp
    {
        node.setNodeType(nodeType)
    }

    fun YGNodeIsDirty(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.isDirty()
    }

    fun YGNodeLayoutGetDidUseLegacyFlag(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.didUseLegacyFlag()
    }

    fun YGNodeStyleSetDirection(
        node: YGNode,
        value: YGDirection
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGDirection>(
            node,
            YGDirection::class,
            value,
            YGStyle::directionBitfieldRef
        )
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetFlexDirection(
        node: YGNode,
        flexDirection: YGFlexDirection
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGFlexDirection>(
            node,
            YGFlexDirection::class,
            flexDirection,
            YGStyle::flexDirectionBitfieldRef
        )
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetJustifyContent(
        node: YGNode,
        justifyContent: YGJustify
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGJustify>(
            node,
            YGJustify::class,
            justifyContent,
            YGStyle::justifyContentBitfieldRef
        )
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetAlignContent(
        node: YGNode,
        alignContent: YGAlign
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGAlign>(
            node,
            YGAlign::class,
            alignContent,
            YGStyle::alignContentBitfieldRef
        )
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetAlignItems(
        node: YGNode,
        alignItems: YGAlign
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGAlign>(
            node,
            YGAlign::class,
            alignItems,
            { obj: YGStyle -> obj.alignItemsBitfieldRef() })
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetAlignSelf(
        node: YGNode,
        alignSelf: YGAlign
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGAlign>(
            node,
            YGAlign::class,
            alignSelf,
            { obj: YGStyle -> obj.alignSelfBitfieldRef() })
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetPositionType(
        node: YGNode,
        positionType: YGPositionType
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGPositionType>(
            node,
            YGPositionType::class,
            positionType,
            { obj: YGStyle -> obj.positionTypeBitfieldRef() })
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetFlexWrap(
        node: YGNode,
        flexWrap: YGWrap
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGWrap>(
            node,
            YGWrap::class,
            flexWrap,
            { obj: YGStyle -> obj.flexWrapBitfieldRef() })
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetOverflow(
        node: YGNode,
        overflow: YGOverflow
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGOverflow>(
            node,
            YGOverflow::class,
            overflow,
            { obj: YGStyle -> obj.overflowBitfieldRef() })
    }

    fun YGNodeStyleSetDisplay(
        node: YGNode,
        display: YGDisplay
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle<YGDisplay>(
            node,
            YGDisplay::class,
            display,
            { obj: YGStyle -> obj.displayBitfieldRef() })
    }

    fun YGNodeStyleSetFlex(node: YGNode, flex: Float) //Method definition originates from: Yoga.cpp
    {
        updateStyle(
            node,
            flex,
            { ygStyle: YGStyle?, `val`: Float ->
                `val` != ygStyle!!.flex().unwrap()
            }) { ygStyle: YGStyle?, `val`: Float -> ygStyle!!.setFlex(YGFloatOptional(`val`)) }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetFlexGrow(
        node: YGNode,
        flexGrow: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle(
            node,
            flexGrow,
            { ygStyle: YGStyle?, `val`: Float ->
                `val` != ygStyle!!.flexGrow().unwrap()
            }) { ygStyle: YGStyle?, `val`: Float -> ygStyle!!.setFlexGrow(YGFloatOptional(`val`)) }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetFlexShrink(
        node: YGNode,
        flexShrink: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle(
            node,
            flexShrink,
            { ygStyle: YGStyle?, `val`: Float ->
                `val` != ygStyle!!.flexShrink().unwrap()
            }) { ygStyle: YGStyle?, `val`: Float -> ygStyle!!.setFlexShrink(YGFloatOptional(`val`)) }
    }

    fun YGNodeStyleSetFlexBasis(
        node: YGNode,
        flexBasis: Float
    ) //Method definition originates from: Yoga.cpp
    {
        val value: CompactValue = CompactValue.Companion.ofMaybe(flexBasis, YGUnit.YGUnitPoint)
        updateStyle(node, value, { ygStyle: YGStyle?, `val`: CompactValue ->
            !CompactValue.Companion.equalsTo(
                ygStyle!!.flexBasis(), `val`
            )
        }) { ygStyle: YGStyle?, `val`: CompactValue? -> ygStyle!!.setFlexBasis(value) }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetFlexBasisPercent(
        node: YGNode,
        flexBasisPercent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        val value: CompactValue =
            CompactValue.Companion.ofMaybe(flexBasisPercent, YGUnit.YGUnitPercent)
        updateStyle(node, value, { ygStyle: YGStyle?, `val`: CompactValue ->
            !CompactValue.Companion.equalsTo(
                ygStyle!!.flexBasis(), `val`
            )
        }) { ygStyle: YGStyle?, `val`: CompactValue? -> ygStyle!!.setFlexBasis(value) }
    }

    fun YGNodeStyleSetFlexBasisAuto(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        val value: CompactValue = CompactValue.Companion.ofAuto()
        updateStyle(node, value, { ygStyle: YGStyle?, `val`: CompactValue ->
            !CompactValue.Companion.equalsTo(
                ygStyle!!.flexBasis(), `val`
            )
        }) { ygStyle: YGStyle?, `val`: CompactValue? -> ygStyle!!.setFlexBasis(value) }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetPosition(
        node: YGNode,
        edge: YGEdge,
        points: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            points,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle -> obj.position() }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetPositionPercent(
        node: YGNode,
        edge: YGEdge,
        percent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            percent,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.position() }
    }

    fun YGNodeStyleGetPosition(
        node: YGNode,
        edge: YGEdge
    ): YGValue? //Method definition originates from: Yoga.cpp
    {
        return node.getStyle().position()[edge.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetMargin(
        node: YGNode,
        edge: YGEdge,
        points: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            points,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle -> obj.margin() }
    }

    private fun <T : Enum<T>?> updateStyleIndexed(
        node: YGNode,
        edge: T,
        value: Float,
        unit: YGUnit,
        values: (YGStyle) -> Values<T>
    ) {
        updateStyleIndexed(node, edge, CompactValue.Companion.ofMaybe(value, unit), values)
    }

    private fun <T : Enum<T>?> updateStyleIndexed(
        node: YGNode,
        edge: T,
        value: CompactValue,
        values: (YGStyle) -> Values<T>
    ) {
        updateStyle(node, value,
            { ygStyle: YGStyle?, `val`: CompactValue ->
                !CompactValue.Companion.equalsTo(
                    values.invoke(node.getStyle()).getCompactValue(
                        edge!!.ordinal
                    ),
                    `val`
                )
            }) { ygStyle: YGStyle?, `val`: CompactValue? ->
            values.invoke(node.getStyle())[edge!!.ordinal] = value
        }
    }

    fun YGNodeStyleSetMarginPercent(
        node: YGNode,
        edge: YGEdge,
        percent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            percent,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.margin() }
    }

    fun YGNodeStyleSetMarginAuto(
        node: YGNode,
        edge: YGEdge
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            CompactValue.Companion.ofAuto()
        ) { obj: YGStyle? -> obj!!.margin() }
    }

    fun YGNodeStyleGetMargin(
        node: YGNode,
        edge: YGEdge
    ): YGValue? //Method definition originates from: Yoga.cpp
    {
        return node.getStyle()!!.margin()[edge.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetPadding(
        node: YGNode,
        edge: YGEdge,
        points: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            points,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.padding() }
    }

    fun YGNodeStyleSetPaddingPercent(
        node: YGNode,
        edge: YGEdge,
        percent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            percent,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.padding() }
    }

    fun YGNodeStyleGetPadding(
        node: YGNode,
        edge: YGEdge
    ): YGValue? //Method definition originates from: Yoga.cpp
    {
        return node.getStyle()!!.padding()[edge.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetBorder(
        node: YGNode,
        edge: YGEdge,
        border: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            edge,
            border,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.border() }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetWidth(
        node: YGNode,
        points: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            points,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleSetWidthPercent(
        node: YGNode,
        percent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            percent,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleSetWidthAuto(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            CompactValue.Companion.ofAuto()
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleGetWidth(node: YGNode): YGValue? //Method definition originates from: Yoga.cpp
    {
        return node.getStyle()!!.dimensions()[YGDimension.YGDimensionWidth.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetHeight(
        node: YGNode,
        points: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            points,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleSetHeightPercent(
        node: YGNode,
        percent: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            percent,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleSetHeightAuto(node: YGNode) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            CompactValue.Companion.ofAuto()
        ) { obj: YGStyle? -> obj!!.dimensions() }
    }

    fun YGNodeStyleGetHeight(node: YGNode): YGValue? //Method definition originates from: Yoga.cpp
    {
        return node.getStyle()!!.dimensions()[YGDimension.YGDimensionHeight.getValue()]
    }

    fun YGNodeStyleSetMinWidth(
        node: YGNode,
        minWidth: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            minWidth,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.minDimensions() }
    }

    fun YGNodeStyleSetMinWidthPercent(
        node: YGNode,
        minWidth: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            minWidth,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.minDimensions() }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetMinHeight(
        node: YGNode,
        minHeight: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            minHeight,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.minDimensions() }
    }

    fun YGNodeStyleSetMinHeightPercent(
        node: YGNode,
        minHeight: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            minHeight,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.minDimensions() }
    }

    fun YGNodeStyleSetMaxWidth(
        node: YGNode,
        maxWidth: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            maxWidth,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.maxDimensions() }
    }

    fun YGNodeStyleSetMaxWidthPercent(
        node: YGNode,
        maxWidth: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionWidth,
            maxWidth,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.maxDimensions() }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeStyleSetMaxHeight(
        node: YGNode,
        maxHeight: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            maxHeight,
            YGUnit.YGUnitPoint
        ) { obj: YGStyle? -> obj!!.maxDimensions() }
    }

    fun YGNodeStyleSetMaxHeightPercent(
        node: YGNode,
        maxHeight: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyleIndexed(
            node,
            YGDimension.YGDimensionHeight,
            maxHeight,
            YGUnit.YGUnitPercent
        ) { obj: YGStyle? -> obj!!.maxDimensions() }
    }

    // YGValue YGNodeStyleGetMaxHeight(YGNode node);
    fun YGNodeStyleSetAspectRatio(
        node: YGNode,
        aspectRatio: Float
    ) //Method definition originates from: Yoga.cpp
    {
        updateStyle(
            node,
            aspectRatio,
            { ygStyle: YGStyle?, `val`: Float ->
                `val` != ygStyle!!.aspectRatio().unwrap()
            }) { ygStyle: YGStyle?, `val`: Float -> ygStyle!!.setAspectRatio(YGFloatOptional(`val`)) }
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeLayoutGetLeft(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.position[YGEdge.YGEdgeLeft.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeLayoutGetTop(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.position[YGEdge.YGEdgeTop.getValue()]
    }

    fun YGNodeLayoutGetRight(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.position[YGEdge.YGEdgeRight.getValue()]
    }

    fun YGNodeLayoutGetBottom(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.position[YGEdge.YGEdgeBottom.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeLayoutGetWidth(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.dimensions[YGDimension.YGDimensionWidth.getValue()]
    }

    @kotlin.jvm.JvmStatic
    fun YGNodeLayoutGetHeight(node: YGNode): Float //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.dimensions[YGDimension.YGDimensionHeight.getValue()]
    }

    fun YGNodeLayoutGetDirection(node: YGNode): YGDirection? //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.direction()
    }

    fun YGNodeLayoutGetHadOverflow(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.hadOverflow()
    }

    fun YGNodeLayoutGetDidLegacyStretchFlagAffectLayout(node: YGNode): Boolean //Method definition originates from: Yoga.cpp
    {
        return node.getLayout()!!.doesLegacyStretchFlagAffectsLayout()
    }

    fun YGNodeLayoutGetMargin(
        node: YGNode,
        edge: YGEdge
    ): Float //Method definition originates from: Yoga.cpp
    {
        YGAssertWithNode(
            node, edge.getValue() <= YGEdge.YGEdgeEnd.getValue(),
            "Cannot get layout properties of multi-edge shorthands"
        )
        if (edge == YGEdge.YGEdgeStart) {
            return if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.margin[YGEdge.YGEdgeRight.getValue()]
            } else {
                node.getLayout()!!.margin[YGEdge.YGEdgeLeft.getValue()]
            }
        }
        return if (edge == YGEdge.YGEdgeEnd) {
            if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.margin[YGEdge.YGEdgeLeft.getValue()]
            } else {
                node.getLayout()!!.margin[YGEdge.YGEdgeRight.getValue()]
            }
        } else node.getLayout()!!.margin[edge.getValue()]
    }

    fun YGNodeLayoutGetBorder(
        node: YGNode,
        edge: YGEdge
    ): Float //Method definition originates from: Yoga.cpp
    {
        YGAssertWithNode(
            node, edge.getValue() <= YGEdge.YGEdgeEnd.getValue(),
            "Cannot get layout properties of multi-edge shorthands"
        )
        if (edge == YGEdge.YGEdgeStart) {
            return if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.border[YGEdge.YGEdgeRight.getValue()]
            } else {
                node.getLayout()!!.border[YGEdge.YGEdgeLeft.getValue()]
            }
        }
        return if (edge == YGEdge.YGEdgeEnd) {
            if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.border[YGEdge.YGEdgeLeft.getValue()]
            } else {
                node.getLayout()!!.border[YGEdge.YGEdgeRight.getValue()]
            }
        } else node.getLayout()!!.border[edge.getValue()]
    }

    fun YGNodeLayoutGetPadding(
        node: YGNode,
        edge: YGEdge
    ): Float //Method definition originates from: Yoga.cpp
    {
        YGAssertWithNode(
            node, edge.getValue() <= YGEdge.YGEdgeEnd.getValue(),
            "Cannot get layout properties of multi-edge shorthands"
        )
        if (edge == YGEdge.YGEdgeStart) {
            return if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.padding[YGEdge.YGEdgeRight.getValue()]
            } else {
                node.getLayout()!!.padding[YGEdge.YGEdgeLeft.getValue()]
            }
        }
        return if (edge == YGEdge.YGEdgeEnd) {
            if (node.getLayout()!!.direction() == YGDirection.YGDirectionRTL) {
                node.getLayout()!!.padding[YGEdge.YGEdgeLeft.getValue()]
            } else {
                node.getLayout()!!.padding[YGEdge.YGEdgeRight.getValue()]
            }
        } else node.getLayout()!!.padding[edge.getValue()]
    }

    fun YGConfigSetLogger(
        config: YGConfig,
        logger: YGLogger?
    ) //Method definition originates from: Yoga.cpp
    {
        config.setLogger(logger ?: YGLogger { ygConfig, ygNode, ygLogLevel, format, args ->
            YGDefaultLog(
                ygConfig,
                ygNode,
                ygLogLevel,
                format,
                *args
            )
        }
        )
    }

    @OptIn(ExperimentalContracts::class)
    fun YGAssert(condition: Boolean, message: String?) //Method definition originates from: Yoga.cpp
    {
        contract {
            returns() implies condition
        }
        if (!condition) {
            Log.log(null as YGNode?, YGLogLevel.YGLogLevelFatal, null, "%s\n", message)
            throw RuntimeException(message)
        }
    }

    @OptIn(ExperimentalContracts::class)
    fun YGAssertWithNode(
        node: YGNode?,
        condition: Boolean,
        message: String?
    ) //Method definition originates from: Yoga.cpp
    {
        contract {
            returns() implies condition
        }
        if (!condition) {
            Log.log(node, YGLogLevel.YGLogLevelFatal, null, "%s\n", message)
            throw RuntimeException(message)
        }
    }

    @OptIn(ExperimentalContracts::class)
    fun YGAssertWithConfig(
        config: YGConfig?,
        condition: Boolean,
        message: String?
    ) //Method definition originates from: Yoga.cpp
    {
        contract {
            returns() implies condition
        }
        if (!condition) {
            Log.log(config, YGLogLevel.YGLogLevelFatal, null, "%s\n", message)
            throw RuntimeException(message)
        }
    }

    fun YGConfigSetPointScaleFactor(
        config: YGConfig,
        pixelsInPoint: Float
    ) //Method definition originates from: Yoga.cpp
    {
        YGAssertWithConfig(
            config,
            pixelsInPoint >= 0.0f,
            "Scale factor should not be less than zero"
        )
        if (pixelsInPoint == 0.0f) {
            config.pointScaleFactor = 0.0f
        } else {
            config.pointScaleFactor = pixelsInPoint
        }
    }

    fun YGConfigSetShouldDiffLayoutWithoutLegacyStretchBehaviour(
        config: YGConfig,
        shouldDiffLayout: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        config.shouldDiffLayoutWithoutLegacyStretchBehaviour = shouldDiffLayout
    }

    fun YGConfigSetUseLegacyStretchBehaviour(
        config: YGConfig,
        useLegacyStretchBehaviour: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        config.useLegacyStretchBehaviour = useLegacyStretchBehaviour
    }

    @kotlin.jvm.JvmStatic
    fun YGConfigNew(): YGConfig //Method definition originates from: Yoga.cpp
    {
        val config =
            YGConfig { ygConfig, ygNode, ygLogLevel, format, args ->
                YGDefaultLog(
                    ygConfig,
                    ygNode,
                    ygLogLevel,
                    format,
                    *args
                )
            }
        gConfigInstanceCount++
        return config
    }

    @kotlin.jvm.JvmStatic
    fun YGConfigFree(config: YGConfig?) //Method definition originates from: Yoga.cpp
    {
        gConfigInstanceCount--
    }

    fun YGConfigCopy(dest: YGConfig?, src: YGConfig?) //Method definition originates from: Yoga.cpp
    {
        //memcpy(dest, src, sizeof(YGConfig));
        //TODO: Implement copy
        throw UnsupportedOperationException("Not implemented")
    }

    fun YGConfigGetInstanceCount(): Int //Method definition originates from: Yoga.cpp
    {
        return gConfigInstanceCount
    }

    fun YGConfigSetExperimentalFeatureEnabled(
        config: YGConfig,
        feature: YGExperimentalFeature,
        enabled: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        config.experimentalFeatures[feature.getValue()] = enabled
    }

    fun YGConfigIsExperimentalFeatureEnabled(
        config: YGConfig,
        feature: YGExperimentalFeature
    ): Boolean //Method definition originates from: Yoga.cpp
    {
        return config.experimentalFeatures[feature.getValue()]
    }

    fun YGConfigSetUseWebDefaults(
        config: YGConfig,
        enabled: Boolean
    ) //Method definition originates from: Yoga.cpp
    {
        config.useWebDefaults = enabled
    }

    fun YGConfigGetUseWebDefaults(config: YGConfig): Boolean //Method definition originates from: Yoga.cpp
    {
        return config.useWebDefaults
    }

    fun YGConfigSetCloneNodeFunc(
        config: YGConfig,
        callback: YGCloneNodeFunc?
    ) //Method definition originates from: Yoga.cpp
    {
        config.setCloneNodeCallback(callback)
    }

    fun YGConfigGetDefault(): YGConfig //Method definition originates from: Yoga.cpp
    {
        //   static struct YGConfig* defaultConfig = YGConfigNew();
        return defaultConfig
    }

    fun YGConfigSetContext(
        config: YGConfig,
        context: Any?
    ) //Method definition originates from: Yoga.cpp
    {
        config.context = context
    }

    fun YGConfigGetContext(config: YGConfig): Any? //Method definition originates from: Yoga.cpp
    {
        return config.context
    }

    fun YGRoundValueToPixelGrid(
        value: Double,
        pointScaleFactor: Double,
        forceCeil: Boolean,
        forceFloor: Boolean
    ): Float //Method definition originates from: Yoga.cpp
    {
        var scaledValue = value * pointScaleFactor
        var fractial = scaledValue % 1.0
        if (fractial < 0) {
            ++fractial
        }
        scaledValue =
            if (YGDoubleEqual(fractial, 0.0)) {
                scaledValue - fractial
            } else if (YGDoubleEqual(
                    fractial,
                    1.0
                )
            ) {
                scaledValue - fractial + 1.0
            } else if (forceCeil) {
                scaledValue - fractial + 1.0
            } else if (forceFloor) {
                scaledValue - fractial
            } else {
                scaledValue - fractial + if (!YGDoubleIsUndefined(
                        fractial
                    ) && (fractial > 0.5 || YGDoubleEqual(
                        fractial, 0.5
                    ))
                ) 1.0 else 0.0
            }
        return if (YGDoubleIsUndefined(scaledValue) || YGDoubleIsUndefined(
                pointScaleFactor
            )
        ) YGUndefined else (scaledValue / pointScaleFactor).toFloat()
    }

    fun YGDefaultLog(
        ygConfig: YGConfig?,
        ygNode: YGNode?,
        ygLogLevel: YGLogLevel,
        s: String,
        vararg objects: Any?
    ): Int //Method definition originates from: Yoga.cpp
    {
        // FIXME: Uncomment logging
        /*when (ygLogLevel) {
            YGLogLevel.YGLogLevelError, YGLogLevel.YGLogLevelFatal -> {
                System.err.printf("$s%n", *objects)
                System.out.printf("$s%n", *objects)
            }
            YGLogLevel.YGLogLevelWarn, YGLogLevel.YGLogLevelInfo, YGLogLevel.YGLogLevelDebug, YGLogLevel.YGLogLevelVerbose -> System.out.printf(
                "$s%n", *objects
            )
            else -> System.out.printf("$s%n", *objects)
        }*/
        return 0
    }

    fun YGDoubleIsUndefined(value: Double): Boolean {
        return isUndefined(value)
    }

    fun YGConfigClone(oldConfig: YGConfig): YGConfig {
        throw NotImplementedError()
        /*val config = oldConfig.clone()
        YGAssert(config != null, "Could not allocate memory for config")
        gConfigInstanceCount++
        return config*/
    }

    fun YGNodeDeepClone(oldNode: YGNode): YGNode {
        var config: YGConfig? = null
        if (oldNode.getConfig() != null) {
            config = YGConfigClone(oldNode.getConfig()!!)
        }
        val node = YGNode(oldNode, config!!)
        node.setOwner(null)
        Event.publish(node, NodeAllocationEventData(node.getConfig()))
        val vec = ArrayList<YGNode>()
        vec.ensureCapacity(oldNode.getChildren().size)
        var childNode: YGNode
        for (item in oldNode.getChildren()) {
            childNode = YGNodeDeepClone(item)
            childNode.setOwner(node)
            vec.add(childNode)
        }
        node.setChildren(vec)
        return YGNode(node)
    }

    fun YGConfigFreeRecursive(root: YGNode) {
        if (root.getConfig() != null) {
            gConfigInstanceCount--
            root.setConfig(null)
        }
        for (child in root.getChildren()) {
            YGConfigFreeRecursive(child)
        }
    }

    fun YGNodeSetChildrenInternal(owner: YGNode?, children: ArrayList<YGNode>) {
        if (owner == null) {
            return
        }
        if (children.size == 0) {
            if (YGNodeGetChildCount(owner) > 0) {
                for (child in owner.getChildren()) {
                    child.setLayout(null)
                    child.setOwner(null)
                }
                owner.getChildren().clear()
                owner.markDirtyAndPropogate()
            }
        } else {
            if (YGNodeGetChildCount(owner) > 0) {
                for (oldChild in owner.getChildren()) {

//TODO: What is this??
                    //if (std::find (children.iterator(), children.end(), oldChild) ==children.end())
                    //{
                    //oldChild.setLayout(YGLayout());
                    oldChild.setLayout(null)
                    oldChild.setOwner(null)
                    //}
                }
            }
            owner.setChildren(children)
            for (child in children) {
                child.setOwner(owner)
            }
            owner.markDirtyAndPropogate()
        }
    }

    fun YGNodeSetChildren(owner: YGNode?, children: ArrayList<YGNode>) {
        YGNodeSetChildrenInternal(owner, children)
    }

    fun YGNodeStyleGetFlexGrow(node: YGNode): Float {
        return if (node.getStyle()!!
                .flexGrow().isUndefined()
        ) kDefaultFlexGrow else node.getStyle()!!
            .flexGrow().unwrap()
    }

    fun YGNodeStyleGetFlexShrink(node: YGNode): Float {
        return if (node.getStyle()!!.flexShrink()
                .isUndefined()
        ) (if (node.getConfig() != null && node.getConfig()!!.useWebDefaults) kWebDefaultFlexShrink else kDefaultFlexShrink) else node.getStyle()
            .flexShrink().unwrap()
    }

    fun <T : Enum<T>> updateStyle(
        node: YGNode,
        enumClazz: KClass<T>,
        value: T,
        fieldRef: (YGStyle) -> BitfieldRef<T>
    ) {
        updateStyle(
            node,
            value,
            { ygStyle: YGStyle, newVal: T ->
                fieldRef.invoke(node.getStyle()).getValue(enumClazz) !== newVal
            }
        ) { ygStyle: YGStyle, newVal: T -> fieldRef.invoke(node.getStyle()).setValue(newVal) }
    }

    fun <T> updateStyle(
        node: YGNode,
        value: T,
        needsUpdate: (YGStyle, T) -> Boolean,
        update: (YGStyle, T) -> Unit
    ) {
        if (needsUpdate.invoke(node.getStyle(), value)) {
            update.invoke(node.getStyle(), value)
            node.markDirtyAndPropogate()
        }
    }

    fun YGNodeStyleGetDirection(node: YGNode): YGDirection? {
        return node.getStyle()!!.direction()
    }

    fun YGNodeStyleGetFlexDirection(node: YGNode): YGFlexDirection? {
        return node.getStyle()!!.flexDirection()
    }

    fun YGNodeStyleGetJustifyContent(node: YGNode): YGJustify? {
        return node.getStyle()!!.justifyContent()
    }

    fun YGNodeStyleGetAlignContent(node: YGNode): YGAlign? {
        return node.getStyle()!!.alignContent()
    }

    fun YGNodeStyleGetAlignItems(node: YGNode): YGAlign? {
        return node.getStyle()!!.alignItems()
    }

    fun YGNodeStyleGetAlignSelf(node: YGNode): YGAlign? {
        return node.getStyle()!!.alignSelf()
    }

    fun YGNodeStyleGetPositionType(node: YGNode): YGPositionType? {
        return node.getStyle()!!.positionType()
    }

    fun YGNodeStyleGetFlexWrap(node: YGNode): YGWrap? {
        return node.getStyle()!!.flexWrap()
    }

    fun YGNodeStyleGetOverflow(node: YGNode): YGOverflow? {
        return node.getStyle()!!.overflow()
    }

    fun YGNodeStyleGetDisplay(node: YGNode): YGDisplay? {
        return node.getStyle()!!.display()
    }

    fun YGNodeStyleGetFlex(node: YGNode): Float {
        return if (node.getStyle()!!
                .flex().isUndefined()
        ) YGUndefined else node.getStyle()!!
            .flex().unwrap()
    }

    fun YGNodeStyleGetFlexBasis(node: YGNode): YGValue? {
        val flexBasis = node.getStyle()!!.flexBasis().convertToYgValue()
        if (flexBasis!!.unit == YGUnit.YGUnitUndefined || flexBasis!!.unit == YGUnit.YGUnitAuto) {
            flexBasis!!.value = YGUndefined
        }
        return flexBasis
    }

    fun YGNodeStyleGetBorder(node: YGNode, edge: YGEdge): Float {
        val border = node.getStyle()!!.border().getCompactValue(edge.getValue())
        return if (border.isUndefined() || border.isAuto()) {
            YGUndefined
        } else border.convertToYgValue().value
    }

    fun YGNodeStyleGetAspectRatio(node: YGNode): Float {
        val op = node.getStyle()!!.aspectRatio()
        return if (op!!.isUndefined()) YGUndefined else op!!.unwrap()
    }

    fun YGNodeStyleGetMinWidth(node: YGNode): YGValue? {
        return node.getStyle()!!.minDimensions()[YGDimension.YGDimensionWidth.getValue()]
    }

    fun YGNodeStyleGetMinHeight(node: YGNode): YGValue? {
        return node.getStyle()!!.minDimensions()[YGDimension.YGDimensionHeight.getValue()]
    }

    fun YGNodeStyleGetMaxWidth(node: YGNode): YGValue? {
        return node.getStyle()!!.maxDimensions()[YGDimension.YGDimensionWidth.getValue()]
    }

    fun YGNodeStyleGetMaxHeight(node: YGNode): YGValue? {
        return node.getStyle()!!.maxDimensions()[YGDimension.YGDimensionHeight.getValue()]
    }

    fun YGLayoutNodeInternal(
        node: YGNode,
        availableWidth: Float,
        availableHeight: Float,
        ownerDirection: YGDirection,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        ownerWidth: Float,
        ownerHeight: Float,
        performLayout: Boolean,
        reason: LayoutPassReason,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ): Boolean //Method definition originates from: Yoga.cpp
    {
        var depth = depth
        val layout = node.getLayout()
        depth++
        val needToVisitNode =
            node.isDirty() && layout!!.generationCount != generationCount || layout!!.lastOwnerDirection != ownerDirection
        if (needToVisitNode) {
            layout!!.nextCachedMeasurementsIndex = 0
            layout!!.cachedLayout.availableWidth = -1f
            layout!!.cachedLayout.availableHeight = -1f
            layout!!.cachedLayout.widthMeasureMode = YGMeasureMode.YGMeasureModeUndefined
            layout!!.cachedLayout.heightMeasureMode = YGMeasureMode.YGMeasureModeUndefined
            layout!!.cachedLayout.computedWidth = -1f
            layout!!.cachedLayout.computedHeight = -1f
        }
        var cachedResults: YGCachedMeasurement? = null
        if (node.hasMeasureFunc()) {
            val marginAxisRow =
                node.getMarginForAxis(YGFlexDirection.YGFlexDirectionRow, ownerWidth).unwrap()
            val marginAxisColumn =
                node.getMarginForAxis(YGFlexDirection.YGFlexDirectionColumn, ownerWidth)
                    .unwrap()
            if (YGNodeCanUseCachedMeasurement(
                    widthMeasureMode,
                    availableWidth,
                    heightMeasureMode,
                    availableHeight,
                    layout!!.cachedLayout.widthMeasureMode,
                    layout!!.cachedLayout.availableWidth,
                    layout!!.cachedLayout.heightMeasureMode,
                    layout!!.cachedLayout.availableHeight,
                    layout!!.cachedLayout.computedWidth,
                    layout!!.cachedLayout.computedHeight,
                    marginAxisRow,
                    marginAxisColumn,
                    config
                )
            ) {
                cachedResults = layout!!.cachedLayout
            } else {
                for (i in 0 until layout!!.nextCachedMeasurementsIndex) {
                    if (YGNodeCanUseCachedMeasurement(
                            widthMeasureMode,
                            availableWidth,
                            heightMeasureMode,
                            availableHeight,
                            layout!!.cachedMeasurements[i].widthMeasureMode,
                            layout!!.cachedMeasurements[i].availableWidth,
                            layout!!.cachedMeasurements[i].heightMeasureMode,
                            layout!!.cachedMeasurements[i].availableHeight,
                            layout!!.cachedMeasurements[i].computedWidth,
                            layout!!.cachedMeasurements[i].computedHeight,
                            marginAxisRow,
                            marginAxisColumn,
                            config
                        )
                    ) {
                        cachedResults = layout!!.cachedMeasurements[i]
                        break
                    }
                }
            }
        } else if (performLayout) {
            if (YGFloatsEqual(
                    layout!!.cachedLayout.availableWidth,
                    availableWidth
                ) && YGFloatsEqual(
                    layout!!.cachedLayout.availableHeight,
                    availableHeight
                ) && layout!!.cachedLayout.widthMeasureMode == widthMeasureMode && layout!!.cachedLayout.heightMeasureMode == heightMeasureMode
            ) {
                cachedResults = layout!!.cachedLayout
            }
        } else {
            for (i in 0 until layout!!.nextCachedMeasurementsIndex) {
                if (YGFloatsEqual(
                        layout!!.cachedMeasurements[i].availableWidth,
                        availableWidth
                    ) && YGFloatsEqual(
                        layout!!.cachedMeasurements[i].availableHeight,
                        availableHeight
                    ) && layout!!.cachedMeasurements[i].widthMeasureMode == widthMeasureMode && layout!!.cachedMeasurements[i].heightMeasureMode == heightMeasureMode
                ) {
                    cachedResults = layout!!.cachedMeasurements[i]
                    break
                }
            }
        }
        if (!needToVisitNode && cachedResults != null) {
            layout!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()] =
                cachedResults.computedWidth
            layout!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] =
                cachedResults.computedHeight
            if (performLayout) layoutMarkerData.cachedLayouts += 1 else layoutMarkerData.cachedMeasures += 1
            if (gPrintChanges && gPrintSkips) {
                Log.log(
                    node,
                    YGLogLevel.YGLogLevelVerbose,
                    null,
                    "%s%d.{[skipped] ",
                    YGSpacer(depth),
                    depth
                )
                node.print(layoutContext)
                Log.log(
                    node,
                    YGLogLevel.YGLogLevelVerbose,
                    null,
                    "wm: %s, hm: %s, aw: %f ah: %f => d: (%f, %f) %s\n",
                    YGMeasureModeName(widthMeasureMode, performLayout),
                    YGMeasureModeName(heightMeasureMode, performLayout),
                    availableWidth,
                    availableHeight,
                    cachedResults.computedWidth,
                    cachedResults.computedHeight,
                    LayoutPassReasonToString(reason)
                )
            }
        } else {
            if (gPrintChanges) {
                Log.log(
                    node,
                    YGLogLevel.YGLogLevelVerbose,
                    null,
                    "%s%d.{%s",
                    YGSpacer(depth),
                    depth,
                    if (needToVisitNode) "*" else ""
                )
                node.print(layoutContext)
                Log.log(
                    node,
                    YGLogLevel.YGLogLevelVerbose,
                    null,
                    "wm: %s, hm: %s, aw: %f ah: %f %s\n",
                    YGMeasureModeName(widthMeasureMode, performLayout),
                    YGMeasureModeName(heightMeasureMode, performLayout),
                    availableWidth,
                    availableHeight,
                    LayoutPassReasonToString(reason)
                )
            }
            YGNodelayoutImpl(
                node,
                availableWidth,
                availableHeight,
                ownerDirection,
                widthMeasureMode,
                heightMeasureMode,
                ownerWidth,
                ownerHeight,
                performLayout,
                config,
                layoutMarkerData,
                layoutContext,
                depth,
                generationCount,
                reason
            )
            if (gPrintChanges) {
                Log.log(
                    node,
                    YGLogLevel.YGLogLevelVerbose,
                    null,
                    "%s%d.}%s",
                    YGSpacer(depth),
                    depth,
                    if (needToVisitNode) "*" else ""
                )
                node.print(layoutContext)
                Log.log(
                    node, YGLogLevel.YGLogLevelVerbose, null, "wm: %s, hm: %s, d: (%f, %f) %s\n",
                    YGMeasureModeName(widthMeasureMode, performLayout),
                    YGMeasureModeName(heightMeasureMode, performLayout),
                    layout!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()],
                    layout!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()],
                    LayoutPassReasonToString(reason)
                )
            }
            layout!!.lastOwnerDirection = ownerDirection
            if (cachedResults == null) {
                if (layout!!.nextCachedMeasurementsIndex + 1 > layoutMarkerData.maxMeasureCache) {
                    layoutMarkerData.maxMeasureCache = layout!!.nextCachedMeasurementsIndex + 1
                }
                if (layout!!.nextCachedMeasurementsIndex == YGLayout.Companion.YG_MAX_CACHED_RESULT_COUNT) {
                    if (gPrintChanges) {
                        Log.log(node, YGLogLevel.YGLogLevelVerbose, null, "Out of cache entries!\n")
                    }
                    layout!!.nextCachedMeasurementsIndex = 0
                }
                val newCacheEntry: YGCachedMeasurement
                if (performLayout) {
                    newCacheEntry = layout!!.cachedLayout
                } else {
                    newCacheEntry =
                        layout!!.cachedMeasurements[layout!!.nextCachedMeasurementsIndex]
                    layout!!.nextCachedMeasurementsIndex++
                }
                newCacheEntry.availableWidth = availableWidth
                newCacheEntry.availableHeight = availableHeight
                newCacheEntry.widthMeasureMode = widthMeasureMode
                newCacheEntry.heightMeasureMode = heightMeasureMode
                newCacheEntry.computedWidth =
                    layout!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()]
                newCacheEntry.computedHeight =
                    layout!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()]
            }
        }
        if (performLayout) {
            node.setLayoutDimension(
                node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()],
                YGDimension.YGDimensionWidth.getValue()
            )
            node.setLayoutDimension(
                node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()],
                YGDimension.YGDimensionHeight.getValue()
            )
            node.setHasNewLayout(true)
            node.setDirty(false)
        }
        layout!!.generationCount = generationCount
        val layoutType: LayoutType
        layoutType = if (performLayout) {
            if (!needToVisitNode && cachedResults === layout!!.cachedLayout) LayoutType.kCachedLayout else LayoutType.kLayout
        } else {
            if (cachedResults != null) LayoutType.kCachedMeasure else LayoutType.kMeasure
        }
        /* Event.NodeLayout */Event.publish(node, NodeLayoutEventData(layoutType, layoutContext))
        return needToVisitNode || cachedResults == null
    }

    fun YGNodePaddingAndBorderForAxis(
        node: YGNode,
        axis: YGFlexDirection,
        widthSize: Float
    ): Float {
        return plus(
            node.getLeadingPaddingAndBorder(axis, widthSize),
            node.getTrailingPaddingAndBorder(axis, widthSize)
        ).unwrap()
    }

    fun YGNodeAlignItem(node: YGNode, child: YGNode): YGAlign {
        val align = if (child.getStyle()!!.alignSelf() == YGAlign.YGAlignAuto) node.getStyle()
            .alignItems() else child.getStyle()!!.alignSelf()
        return if (align == YGAlign.YGAlignBaseline && YGFlexDirectionIsColumn(
                node.getStyle()!!.flexDirection()
            )
        ) {
            YGAlign.YGAlignFlexStart
        } else align
    }

    fun YGBaseline(node: YGNode, layoutContext: Any?): Float {
        if (node.hasBaselineFunc()) {

            /* Event.NodeBaselineStart */
            publish(node)
            val baseline = node.baseline(
                node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()],
                node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()],
                layoutContext
            )

            /* Event.NodeBaselineEnd */publish(node)
            YGAssertWithNode(
                node,
                !YGFloatIsUndefined(baseline),
                "Expect custom baseline function to not return NaN"
            )
            return baseline
        }
        var baselineChild: YGNode? = null
        val childCount = YGNodeGetChildCount(node)
        for (i in 0 until childCount) {
            val child = YGNodeGetChild(node, i)
            if (child != null && child.getLineIndex() > 0) {
                break
            }
            if (child!!.getStyle()!!.positionType() == YGPositionType.YGPositionTypeAbsolute) {
                continue
            }
            if (YGNodeAlignItem(
                    node,
                    child
                ) == YGAlign.YGAlignBaseline || child.isReferenceBaseline()
            ) {
                baselineChild = child
                break
            }
            if (baselineChild == null) {
                baselineChild = child
            }
        }
        if (baselineChild == null) {
            return node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()]
        }
        val baseline = YGBaseline(baselineChild, layoutContext)
        return baseline + baselineChild.getLayout()!!.position[YGEdge.YGEdgeTop.getValue()]
    }

    fun YGIsBaselineLayout(node: YGNode): Boolean {
        if (YGFlexDirectionIsColumn(
                node.getStyle()!!.flexDirection()
            )
        ) {
            return false
        }
        if (node.getStyle()!!.alignItems() == YGAlign.YGAlignBaseline) {
            return true
        }
        val childCount = YGNodeGetChildCount(node)
        for (i in 0 until childCount) {
            val child = YGNodeGetChild(node, i)
            if (child != null && child.getStyle()
                    .positionType() != YGPositionType.YGPositionTypeAbsolute && child.getStyle()
                    .alignSelf() == YGAlign.YGAlignBaseline
            ) {
                return true
            }
        }
        return false
    }

    fun YGNodeDimWithMargin(node: YGNode, axis: YGFlexDirection, widthSize: Float): Float {
        return plus(
            YGFloatOptional(node.getLayout()!!.measuredDimensions[dim[axis.getValue()].getValue()]),
            plus(node.getLeadingMargin(axis, widthSize), node.getTrailingMargin(axis, widthSize))
        ).unwrap()
    }

    fun YGNodeIsStyleDimDefined(node: YGNode, axis: YGFlexDirection, ownerSize: Float): Boolean {
        val resolvedDimension = node.getResolvedDimension(dim[axis.getValue()].getValue())
        val isUndefined = YGFloatIsUndefined(
            resolvedDimension!!.value
        )
        val isAutoUnit = resolvedDimension.unit == YGUnit.YGUnitAuto
        val isUndefinedUnit = resolvedDimension.unit == YGUnit.YGUnitUndefined
        val isPointUnit = resolvedDimension.unit == YGUnit.YGUnitPoint
        val isDefined = !isUndefined
        val isResolvedValueLessThanZero = resolvedDimension.value < 0.0f
        val isResolvedPointValueNegative = isPointUnit && isDefined && isResolvedValueLessThanZero
        val isPercentUnit = resolvedDimension.unit == YGUnit.YGUnitPercent
        val isOwnerSizeUndefined = YGFloatIsUndefined(ownerSize)
        return !(isAutoUnit || isUndefinedUnit || isResolvedPointValueNegative || isPercentUnit && isDefined && (isResolvedValueLessThanZero || isOwnerSizeUndefined))
    }

    fun YGNodeIsLayoutDimDefined(node: YGNode, axis: YGFlexDirection): Boolean {
        val value = node.getLayout()!!.measuredDimensions[dim[axis.getValue()].getValue()]
        return !YGFloatIsUndefined(value) && value >= 0.0f
    }

    fun YGNodeBoundAxisWithinMinAndMax(
        node: YGNode,
        axis: YGFlexDirection,
        value: YGFloatOptional,
        axisSize: Float
    ): YGFloatOptional {
        var min = YGFloatOptional()
        var max = YGFloatOptional()
        if (YGFlexDirectionIsColumn(axis)) {
            min = YGResolveValue(
                node.getStyle()!!.minDimensions()[YGDimension.YGDimensionHeight.getValue()],
                axisSize
            )
            max = YGResolveValue(
                node.getStyle()!!.maxDimensions()[YGDimension.YGDimensionHeight.getValue()],
                axisSize
            )
        } else if (YGFlexDirectionIsRow(axis)) {
            min = YGResolveValue(
                node.getStyle()!!.minDimensions()[YGDimension.YGDimensionWidth.getValue()],
                axisSize
            )
            max = YGResolveValue(
                node.getStyle()!!.maxDimensions()[YGDimension.YGDimensionWidth.getValue()],
                axisSize
            )
        }
        if (greaterThanOrEqualTo(
                max, YGFloatOptional(
                    0f
                )
            ) && greaterThan(value, max)
        ) {
            return max
        }
        return if (greaterThanOrEqualTo(
                min, YGFloatOptional(
                    0f
                )
            ) && lessThan(value, min)
        ) {
            min
        } else value
    }

    fun YGNodeBoundAxis(
        node: YGNode,
        axis: YGFlexDirection,
        value: Float,
        axisSize: Float,
        widthSize: Float
    ): Float {
        return YGFloatMax(
            YGNodeBoundAxisWithinMinAndMax(
                node, axis, YGFloatOptional(
                    value
                ), axisSize
            ).unwrap(),
            YGNodePaddingAndBorderForAxis(node, axis, widthSize)
        )
    }

    fun YGNodeSetChildTrailingPosition(node: YGNode, child: YGNode, axis: YGFlexDirection) {
        val size = child.getLayout()!!.measuredDimensions[dim[axis.getValue()].getValue()]
        child.setLayoutPosition(
            node.getLayout()!!.measuredDimensions[dim[axis.getValue()].getValue()] - size - child.getLayout()!!.position[pos[axis.getValue()].getValue()],
            trailing[axis.getValue()].getValue()
        )
    }

    fun YGConstrainMaxSizeForMode(
        node: YGNode,
        axis: YGFlexDirection,
        ownerAxisSize: Float,
        ownerWidth: Float,
        mode: YGMeasureMode,
        size: RefObject<Float>
    ) {
        val maxSize = plus(
            YGResolveValue(
                node.getStyle()!!.maxDimensions().getCompactValue(dim[axis.getValue()].getValue()),
                ownerAxisSize
            ), node.getMarginForAxis(axis, ownerWidth)
        )
        when (mode) {
            YGMeasureMode.YGMeasureModeExactly, YGMeasureMode.YGMeasureModeAtMost -> size.argValue =
                if (maxSize.isUndefined() || size.argValue < maxSize.unwrap()) size.argValue else maxSize.unwrap()
            YGMeasureMode.YGMeasureModeUndefined -> if (!maxSize.isUndefined()) {
                //TODO: What is this???
                size.argValue = maxSize.unwrap()
            }
        }
    }

    fun YGNodeComputeFlexBasisForChild(
        node: YGNode,
        child: YGNode,
        width: Float,
        widthMode: YGMeasureMode,
        height: Float,
        ownerWidth: Float,
        ownerHeight: Float,
        heightMode: YGMeasureMode,
        direction: YGDirection,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ) {
        val mainAxis = YGResolveFlexDirection(
            node.getStyle()!!.flexDirection(), direction
        )
        val isMainAxisRow = YGFlexDirectionIsRow(mainAxis)
        val mainAxisSize = if (isMainAxisRow) width else height
        val mainAxisownerSize = if (isMainAxisRow) ownerWidth else ownerHeight
        var childWidth: Float
        var childHeight: Float
        var childWidthMeasureMode: YGMeasureMode
        var childHeightMeasureMode: YGMeasureMode
        val resolvedFlexBasis = YGResolveValue(
            child.resolveFlexBasisPtr()!!,
            mainAxisownerSize
        )
        val isRowStyleDimDefined = YGNodeIsStyleDimDefined(
            child, YGFlexDirection.YGFlexDirectionRow,
            ownerWidth
        )
        val isColumnStyleDimDefined = YGNodeIsStyleDimDefined(
            child, YGFlexDirection.YGFlexDirectionColumn,
            ownerHeight
        )
        if (!resolvedFlexBasis.isUndefined() && !YGFloatIsUndefined(mainAxisSize)) {
            if (child.getConfig() != null && (child.getLayout()!!.computedFlexBasis.isUndefined() || YGConfigIsExperimentalFeatureEnabled(
                    child.getConfig()!!,
                    YGExperimentalFeature.YGExperimentalFeatureWebFlexBasis
                ) && child.getLayout()!!.computedFlexBasisGeneration != generationCount)
            ) {
                val paddingAndBorder = YGFloatOptional(
                    YGNodePaddingAndBorderForAxis(child, mainAxis, ownerWidth)
                )
                child.setLayoutComputedFlexBasis(
                    YGFloatOptionalMax(
                        resolvedFlexBasis,
                        paddingAndBorder
                    )
                )
            }
        } else if (isMainAxisRow && isRowStyleDimDefined) {
            val paddingAndBorder = YGFloatOptional(
                YGNodePaddingAndBorderForAxis(child, YGFlexDirection.YGFlexDirectionRow, ownerWidth)
            )
            child.setLayoutComputedFlexBasis(
                YGFloatOptionalMax(
                    YGResolveValue(
                        child.getResolvedDimensions()[YGDimension.YGDimensionWidth.getValue()],
                        ownerWidth
                    ), paddingAndBorder
                )
            )
        } else if (!isMainAxisRow && isColumnStyleDimDefined) {
            val paddingAndBorder = YGFloatOptional(
                YGNodePaddingAndBorderForAxis(
                    child,
                    YGFlexDirection.YGFlexDirectionColumn,
                    ownerWidth
                )
            )
            child.setLayoutComputedFlexBasis(
                YGFloatOptionalMax(
                    YGResolveValue(
                        child.getResolvedDimensions()[YGDimension.YGDimensionHeight.getValue()],
                        ownerHeight
                    ), paddingAndBorder
                )
            )
        } else {
            childWidth = YGUndefined
            childHeight = YGUndefined
            childWidthMeasureMode = YGMeasureMode.YGMeasureModeUndefined
            childHeightMeasureMode = YGMeasureMode.YGMeasureModeUndefined
            val marginRow =
                child.getMarginForAxis(YGFlexDirection.YGFlexDirectionRow, ownerWidth).unwrap()
            val marginColumn =
                child.getMarginForAxis(YGFlexDirection.YGFlexDirectionColumn, ownerWidth).unwrap()
            if (isRowStyleDimDefined) {
                childWidth = YGResolveValue(
                    child.getResolvedDimensions()[YGDimension.YGDimensionWidth.getValue()],
                    ownerWidth
                ).unwrap() + marginRow
                childWidthMeasureMode = YGMeasureMode.YGMeasureModeExactly
            }
            if (isColumnStyleDimDefined) {
                childHeight = YGResolveValue(
                    child.getResolvedDimensions()[YGDimension.YGDimensionHeight.getValue()],
                    ownerHeight
                ).unwrap() + marginColumn
                childHeightMeasureMode = YGMeasureMode.YGMeasureModeExactly
            }
            if (!isMainAxisRow && node.getStyle()!!
                    .overflow() == YGOverflow.YGOverflowScroll || node.getStyle()
                    .overflow() != YGOverflow.YGOverflowScroll
            ) {
                if (YGFloatIsUndefined(childWidth) && !YGFloatIsUndefined(width)) {
                    childWidth = width
                    childWidthMeasureMode = YGMeasureMode.YGMeasureModeAtMost
                }
            }
            if (isMainAxisRow && node.getStyle()!!
                    .overflow() == YGOverflow.YGOverflowScroll || node.getStyle()
                    .overflow() != YGOverflow.YGOverflowScroll
            ) {
                if (YGFloatIsUndefined(childHeight) && !YGFloatIsUndefined(height)) {
                    childHeight = height
                    childHeightMeasureMode = YGMeasureMode.YGMeasureModeAtMost
                }
            }
            val childStyle = child.getStyle()
            if (!childStyle!!.aspectRatio().isUndefined()) {
                if (!isMainAxisRow && childWidthMeasureMode == YGMeasureMode.YGMeasureModeExactly) {
                    childHeight =
                        marginColumn + (childWidth - marginRow) / childStyle!!.aspectRatio()
                            .unwrap()
                    childHeightMeasureMode = YGMeasureMode.YGMeasureModeExactly
                } else if (isMainAxisRow && childHeightMeasureMode == YGMeasureMode.YGMeasureModeExactly) {
                    childWidth =
                        marginRow + (childHeight - marginColumn) * childStyle!!.aspectRatio()
                            .unwrap()
                    childWidthMeasureMode = YGMeasureMode.YGMeasureModeExactly
                }
            }
            val hasExactWidth =
                !YGFloatIsUndefined(width) && widthMode == YGMeasureMode.YGMeasureModeExactly
            val childWidthStretch = YGNodeAlignItem(
                node,
                child
            ) == YGAlign.YGAlignStretch && childWidthMeasureMode != YGMeasureMode.YGMeasureModeExactly
            if (!isMainAxisRow && !isRowStyleDimDefined && hasExactWidth && childWidthStretch) {
                childWidth = width
                childWidthMeasureMode = YGMeasureMode.YGMeasureModeExactly
                if (!childStyle!!.aspectRatio().isUndefined()) {
                    childHeight = (childWidth - marginRow) / childStyle!!.aspectRatio().unwrap()
                    childHeightMeasureMode = YGMeasureMode.YGMeasureModeExactly
                }
            }
            val hasExactHeight = !YGFloatIsUndefined(
                height
            ) && heightMode == YGMeasureMode.YGMeasureModeExactly
            val childHeightStretch = YGNodeAlignItem(
                node,
                child
            ) == YGAlign.YGAlignStretch && childHeightMeasureMode != YGMeasureMode.YGMeasureModeExactly
            if (isMainAxisRow && !isColumnStyleDimDefined && hasExactHeight && childHeightStretch) {
                childHeight = height
                childHeightMeasureMode = YGMeasureMode.YGMeasureModeExactly
                if (!childStyle!!.aspectRatio().isUndefined()) {
                    childWidth = (childHeight - marginColumn) * childStyle!!.aspectRatio().unwrap()
                    childWidthMeasureMode = YGMeasureMode.YGMeasureModeExactly
                }
            }
            val childWidthRef = RefObject(childWidth)
            YGConstrainMaxSizeForMode(
                child, YGFlexDirection.YGFlexDirectionRow, ownerWidth, ownerWidth,
                childWidthMeasureMode, childWidthRef
            )
            YGConstrainMaxSizeForMode(
                child, YGFlexDirection.YGFlexDirectionColumn, ownerHeight, ownerWidth,
                childHeightMeasureMode, childWidthRef
            )
            childWidth = childWidthRef.argValue
            YGLayoutNodeInternal(
                child,
                childWidth,
                childHeight,
                direction,
                childWidthMeasureMode,
                childHeightMeasureMode,
                ownerWidth,
                ownerHeight,
                false,
                LayoutPassReason.kMeasureChild,
                config,
                layoutMarkerData,
                layoutContext,
                depth,
                generationCount
            )
            child.setLayoutComputedFlexBasis(
                YGFloatOptional(
                    YGFloatMax(
                        child.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()],
                        YGNodePaddingAndBorderForAxis(child, mainAxis, ownerWidth)
                    )
                )
            )
        }
        child.setLayoutComputedFlexBasisGeneration(generationCount)
    }

    fun YGNodeAbsoluteLayoutChild(
        node: YGNode,
        child: YGNode,
        width: Float,
        widthMode: YGMeasureMode,
        height: Float,
        direction: YGDirection,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ) {
        val mainAxis = YGResolveFlexDirection(
            node.getStyle()!!.flexDirection(), direction
        )
        val crossAxis = YGFlexDirectionCross(mainAxis, direction)
        val isMainAxisRow = YGFlexDirectionIsRow(mainAxis)
        var childWidth = YGUndefined
        var childHeight = YGUndefined
        var childWidthMeasureMode: YGMeasureMode
        val childHeightMeasureMode: YGMeasureMode
        val marginRow = child.getMarginForAxis(YGFlexDirection.YGFlexDirectionRow, width).unwrap()
        val marginColumn =
            child.getMarginForAxis(YGFlexDirection.YGFlexDirectionColumn, width).unwrap()
        if (YGNodeIsStyleDimDefined(child, YGFlexDirection.YGFlexDirectionRow, width)) {
            childWidth = YGResolveValue(
                child.getResolvedDimensions()[YGDimension.YGDimensionWidth.getValue()],
                width
            ).unwrap() + marginRow
        } else {
            if (child.isLeadingPositionDefined(YGFlexDirection.YGFlexDirectionRow) && child.isTrailingPosDefined(
                    YGFlexDirection.YGFlexDirectionRow
                )
            ) {
                childWidth =
                    node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()] - (node.getLeadingBorder(
                        YGFlexDirection.YGFlexDirectionRow
                    ) + node.getTrailingBorder(YGFlexDirection.YGFlexDirectionRow)) - (child.getLeadingPosition(
                        YGFlexDirection.YGFlexDirectionRow,
                        width
                    ).unwrap() + child.getTrailingPosition(
                        YGFlexDirection.YGFlexDirectionRow,
                        width
                    ).unwrap())
                childWidth = YGNodeBoundAxis(
                    child,
                    YGFlexDirection.YGFlexDirectionRow,
                    childWidth,
                    width,
                    width
                )
            }
        }
        if (YGNodeIsStyleDimDefined(child, YGFlexDirection.YGFlexDirectionColumn, height)) {
            childHeight = YGResolveValue(
                child.getResolvedDimensions()[YGDimension.YGDimensionHeight.getValue()],
                height
            ).unwrap() + marginColumn
        } else {
            if (child.isLeadingPositionDefined(YGFlexDirection.YGFlexDirectionColumn) && child.isTrailingPosDefined(
                    YGFlexDirection.YGFlexDirectionColumn
                )
            ) {
                childHeight =
                    node.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] - (node.getLeadingBorder(
                        YGFlexDirection.YGFlexDirectionColumn
                    ) + node.getTrailingBorder(YGFlexDirection.YGFlexDirectionColumn)) - (child.getLeadingPosition(
                        YGFlexDirection.YGFlexDirectionColumn,
                        height
                    ).unwrap() + child.getTrailingPosition(
                        YGFlexDirection.YGFlexDirectionColumn,
                        height
                    ).unwrap())
                childHeight = YGNodeBoundAxis(
                    child,
                    YGFlexDirection.YGFlexDirectionColumn,
                    childHeight,
                    height,
                    width
                )
            }
        }
        val childStyle = child.getStyle()
        if (YGFloatIsUndefined(childWidth) xor YGFloatIsUndefined(childHeight)) {
            if (!childStyle!!.aspectRatio().isUndefined()) {
                if (YGFloatIsUndefined(childWidth)) {
                    childWidth =
                        marginRow + (childHeight - marginColumn) * childStyle!!.aspectRatio()
                            .unwrap()
                } else if (YGFloatIsUndefined(childHeight)) {
                    childHeight =
                        marginColumn + (childWidth - marginRow) / childStyle!!.aspectRatio()
                            .unwrap()
                }
            }
        }
        if (YGFloatIsUndefined(childWidth) || YGFloatIsUndefined(childHeight)) {
            childWidthMeasureMode = if (YGFloatIsUndefined(
                    childWidth
                )
            ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
            childHeightMeasureMode = if (YGFloatIsUndefined(
                    childHeight
                )
            ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
            if (!isMainAxisRow && YGFloatIsUndefined(
                    childWidth
                ) && widthMode != YGMeasureMode.YGMeasureModeUndefined && !YGFloatIsUndefined(
                    width
                ) && width > 0f
            ) {
                childWidth = width
                childWidthMeasureMode = YGMeasureMode.YGMeasureModeAtMost
            }
            YGLayoutNodeInternal(
                child,
                childWidth,
                childHeight,
                direction,
                childWidthMeasureMode,
                childHeightMeasureMode,
                childWidth,
                childHeight,
                false,
                LayoutPassReason.kAbsMeasureChild,
                config,
                layoutMarkerData,
                layoutContext,
                depth,
                generationCount
            )
            childWidth =
                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()] + child.getMarginForAxis(
                    YGFlexDirection.YGFlexDirectionRow,
                    width
                ).unwrap()
            childHeight =
                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] + child.getMarginForAxis(
                    YGFlexDirection.YGFlexDirectionColumn,
                    width
                ).unwrap()
        }
        YGLayoutNodeInternal(
            child,
            childWidth,
            childHeight,
            direction,
            YGMeasureMode.YGMeasureModeExactly,
            YGMeasureMode.YGMeasureModeExactly,
            childWidth,
            childHeight,
            true,
            LayoutPassReason.kAbsLayout,
            config,
            layoutMarkerData,
            layoutContext,
            depth,
            generationCount
        )
        if (child.isTrailingPosDefined(mainAxis) && !child.isLeadingPositionDefined(mainAxis)) {
            child.setLayoutPosition(
                node.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()] - node.getTrailingBorder(
                    mainAxis
                ) - child.getTrailingMargin(mainAxis, width).unwrap() - child.getTrailingPosition(
                    mainAxis,
                    if (isMainAxisRow) width else height
                ).unwrap(), leading[mainAxis.getValue()].getValue()
            )
        } else if (!child.isLeadingPositionDefined(mainAxis) && node.getStyle()
                .justifyContent() == YGJustify.YGJustifyCenter
        ) {
            child.setLayoutPosition(
                (node.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()]) / 2.0f,
                leading[mainAxis.getValue()].getValue()
            )
        } else if (!child.isLeadingPositionDefined(mainAxis) && node.getStyle()
                .justifyContent() == YGJustify.YGJustifyFlexEnd
        ) {
            child.setLayoutPosition(
                node.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()],
                leading[mainAxis.getValue()].getValue()
            )
        }
        if (child.isTrailingPosDefined(crossAxis) && !child.isLeadingPositionDefined(crossAxis)) {
            child.setLayoutPosition(
                node.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] - node.getTrailingBorder(
                    crossAxis
                ) - child.getTrailingMargin(crossAxis, width).unwrap() - child.getTrailingPosition(
                    crossAxis, if (isMainAxisRow) height else width
                ).unwrap(), leading[crossAxis.getValue()].getValue()
            )
        } else if (!child.isLeadingPositionDefined(crossAxis) && YGNodeAlignItem(
                node,
                child
            ) == YGAlign.YGAlignCenter
        ) {
            child.setLayoutPosition(
                (node.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()]) / 2.0f,
                leading[crossAxis.getValue()].getValue()
            )
        } else if (!child.isLeadingPositionDefined(crossAxis) && (YGNodeAlignItem(
                node,
                child
            ) == YGAlign.YGAlignFlexEnd) xor (node.getStyle()!!
                .flexWrap() == YGWrap.YGWrapWrapReverse)
        ) {
            child.setLayoutPosition(
                node.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] - child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()],
                leading[crossAxis.getValue()].getValue()
            )
        }
    }

    fun YGNodeWithMeasureFuncSetMeasuredDimensions(
        node: YGNode,
        availableWidth: Float,
        availableHeight: Float,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        ownerWidth: Float,
        ownerHeight: Float,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        reason: LayoutPassReason
    ) {
        var availableWidth = availableWidth
        var availableHeight = availableHeight
        YGAssertWithNode(
            node,
            node.hasMeasureFunc(),
            "Expected node to have custom measure function"
        )
        if (widthMeasureMode == YGMeasureMode.YGMeasureModeUndefined) {
            availableWidth = YGUndefined
        }
        if (heightMeasureMode == YGMeasureMode.YGMeasureModeUndefined) {
            availableHeight = YGUndefined
        }
        val padding = node.getLayout()!!.padding
        val border = node.getLayout()!!.border
        val paddingAndBorderAxisRow =
            padding[YGEdge.YGEdgeLeft.getValue()]!! + padding[YGEdge.YGEdgeRight.getValue()]!! + border[YGEdge.YGEdgeLeft.getValue()]!! + border[YGEdge.YGEdgeRight.getValue()]!!
        val paddingAndBorderAxisColumn =
            padding[YGEdge.YGEdgeTop.getValue()]!! + padding[YGEdge.YGEdgeBottom.getValue()]!! + border[YGEdge.YGEdgeTop.getValue()]!! + border[YGEdge.YGEdgeBottom.getValue()]!!
        val innerWidth = if (YGFloatIsUndefined(availableWidth)) availableWidth else YGFloatMax(
            0f,
            availableWidth - paddingAndBorderAxisRow
        )
        val innerHeight = if (YGFloatIsUndefined(availableHeight)) availableHeight else YGFloatMax(
            0f,
            availableHeight - paddingAndBorderAxisColumn
        )
        if (widthMeasureMode == YGMeasureMode.YGMeasureModeExactly && heightMeasureMode == YGMeasureMode.YGMeasureModeExactly) {
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node,
                    YGFlexDirection.YGFlexDirectionRow,
                    availableWidth,
                    ownerWidth,
                    ownerWidth
                ),
                YGDimension.YGDimensionWidth.getValue()
            )
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node, YGFlexDirection.YGFlexDirectionColumn, availableHeight, ownerHeight,
                    ownerWidth
                ), YGDimension.YGDimensionHeight.getValue()
            )
        } else {
            /* Event.MeasureCallbackStart */
            publish(node)
            val measuredSize = node.measure(
                innerWidth, widthMeasureMode, innerHeight, heightMeasureMode,
                layoutContext
            )
            layoutMarkerData.measureCallbacks += 1
            layoutMarkerData.measureCallbackReasonsCount[reason.getValue()] = 1

            /* Event.MeasureCallbackEnd */Event.publish(
                node,
                MeasureCallbackEndEventData(
                    layoutContext, innerWidth, widthMeasureMode, innerHeight,
                    heightMeasureMode, measuredSize!!.width, measuredSize.height, reason
                )
            )
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node, YGFlexDirection.YGFlexDirectionRow,
                    if (widthMeasureMode == YGMeasureMode.YGMeasureModeUndefined || widthMeasureMode == YGMeasureMode.YGMeasureModeAtMost) measuredSize.width + paddingAndBorderAxisRow else availableWidth,
                    ownerWidth, ownerWidth
                ), YGDimension.YGDimensionWidth.getValue()
            )
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node, YGFlexDirection.YGFlexDirectionColumn,
                    if (heightMeasureMode == YGMeasureMode.YGMeasureModeUndefined || heightMeasureMode == YGMeasureMode.YGMeasureModeAtMost) measuredSize.height + paddingAndBorderAxisColumn else availableHeight,
                    ownerHeight, ownerWidth
                ), YGDimension.YGDimensionHeight.getValue()
            )
        }
    }

    fun YGNodeEmptyContainerSetMeasuredDimensions(
        node: YGNode,
        availableWidth: Float,
        availableHeight: Float,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        ownerWidth: Float,
        ownerHeight: Float
    ) {
        val padding = node.getLayout()!!.padding
        val border = node.getLayout()!!.border
        var width = availableWidth
        if (widthMeasureMode == YGMeasureMode.YGMeasureModeUndefined || widthMeasureMode == YGMeasureMode.YGMeasureModeAtMost) {
            width =
                padding[YGEdge.YGEdgeLeft.getValue()]!! + padding[YGEdge.YGEdgeRight.getValue()]!! + border[YGEdge.YGEdgeLeft.getValue()]!! + border[YGEdge.YGEdgeRight.getValue()]!!
        }
        node.setLayoutMeasuredDimension(
            YGNodeBoundAxis(
                node,
                YGFlexDirection.YGFlexDirectionRow,
                width,
                ownerWidth,
                ownerWidth
            ),
            YGDimension.YGDimensionWidth.getValue()
        )
        var height = availableHeight
        if (heightMeasureMode == YGMeasureMode.YGMeasureModeUndefined || heightMeasureMode == YGMeasureMode.YGMeasureModeAtMost) {
            height =
                padding[YGEdge.YGEdgeTop.getValue()]!! + padding[YGEdge.YGEdgeBottom.getValue()]!! + border[YGEdge.YGEdgeTop.getValue()]!! + border[YGEdge.YGEdgeBottom.getValue()]!!
        }
        node.setLayoutMeasuredDimension(
            YGNodeBoundAxis(
                node,
                YGFlexDirection.YGFlexDirectionColumn,
                height,
                ownerHeight,
                ownerWidth
            ),
            YGDimension.YGDimensionHeight.getValue()
        )
    }

    fun YGNodeFixedSizeSetMeasuredDimensions(
        node: YGNode,
        availableWidth: Float,
        availableHeight: Float,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        ownerWidth: Float,
        ownerHeight: Float
    ): Boolean {
        if (!YGFloatIsUndefined(
                availableWidth
            ) && widthMeasureMode == YGMeasureMode.YGMeasureModeAtMost && availableWidth <= 0.0f || !YGFloatIsUndefined(
                availableHeight
            ) && heightMeasureMode == YGMeasureMode.YGMeasureModeAtMost && availableHeight <= 0.0f || widthMeasureMode == YGMeasureMode.YGMeasureModeExactly && heightMeasureMode == YGMeasureMode.YGMeasureModeExactly
        ) {
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node, YGFlexDirection.YGFlexDirectionRow,
                    if (YGFloatIsUndefined(
                            availableWidth
                        ) || widthMeasureMode == YGMeasureMode.YGMeasureModeAtMost && availableWidth < 0.0f
                    ) 0.0f else availableWidth,
                    ownerWidth, ownerWidth
                ), YGDimension.YGDimensionWidth.getValue()
            )
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node, YGFlexDirection.YGFlexDirectionColumn,
                    if (YGFloatIsUndefined(
                            availableHeight
                        ) || heightMeasureMode == YGMeasureMode.YGMeasureModeAtMost && availableHeight < 0.0f
                    ) 0.0f else availableHeight,
                    ownerHeight, ownerWidth
                ), YGDimension.YGDimensionHeight.getValue()
            )
            return true
        }
        return false
    }

    fun YGZeroOutLayoutRecursivly(node: YGNode, layoutContext: Any?) {
        node.setLayout(null)
        node.setLayoutDimension(0f, 0)
        node.setLayoutDimension(0f, 1)
        node.setHasNewLayout(true)
        node.iterChildrenAfterCloningIfNeeded<Any>(::YGZeroOutLayoutRecursivly, layoutContext)
    }

    fun YGNodeCalculateAvailableInnerDim(
        node: YGNode,
        dimension: YGDimension,
        availableDim: Float,
        paddingAndBorder: Float,
        ownerDim: Float
    ): Float {
        var availableInnerDim = availableDim - paddingAndBorder
        if (!YGFloatIsUndefined(availableInnerDim)) {
            val minDimensionOptional = YGResolveValue(
                node.getStyle()!!.minDimensions()[dimension.getValue()], ownerDim
            )
            val minInnerDim =
                if (minDimensionOptional.isUndefined()) 0.0f else minDimensionOptional.unwrap() - paddingAndBorder
            val maxDimensionOptional = YGResolveValue(
                node.getStyle()!!.maxDimensions()[dimension.getValue()], ownerDim
            )
            val maxInnerDim =
                if (maxDimensionOptional.isUndefined()) Float.MAX_VALUE else maxDimensionOptional.unwrap() - paddingAndBorder
            availableInnerDim = YGFloatMax(YGFloatMin(availableInnerDim, maxInnerDim), minInnerDim)
        }
        return availableInnerDim
    }

    fun YGNodeComputeFlexBasisForChildren(
        node: YGNode,
        availableInnerWidth: Float,
        availableInnerHeight: Float,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        direction: YGDirection,
        mainAxis: YGFlexDirection,
        config: YGConfig?,
        performLayout: Boolean,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ): Float {
        var totalOuterFlexBasis = 0.0f
        var singleFlexChild: YGNode? = null
        val children = node.getChildren()
        val measureModeMainDim =
            if (YGFlexDirectionIsRow(mainAxis)) widthMeasureMode else heightMeasureMode
        if (measureModeMainDim == YGMeasureMode.YGMeasureModeExactly) {
            for (child in children!!) {
                if (child.isNodeFlexible()) {
                    if (singleFlexChild != null || YGFloatsEqual(
                            child.resolveFlexGrow(),
                            0.0f
                        ) || YGFloatsEqual(
                            child.resolveFlexShrink(), 0.0f
                        )
                    ) {
                        singleFlexChild = null
                        break
                    } else {
                        singleFlexChild = child
                    }
                }
            }
        }
        for (child in children!!) {
            child.resolveDimension()
            if (child.getStyle()!!.display() == YGDisplay.YGDisplayNone) {
                YGZeroOutLayoutRecursivly(child, layoutContext)
                child.setHasNewLayout(true)
                child.setDirty(false)
                continue
            }
            if (performLayout) {
                val childDirection = child.resolveDirection(direction)
                val mainDim =
                    if (YGFlexDirectionIsRow(mainAxis)) availableInnerWidth else availableInnerHeight
                val crossDim =
                    if (YGFlexDirectionIsRow(mainAxis)) availableInnerHeight else availableInnerWidth
                child.setPosition(childDirection!!, mainDim, crossDim, availableInnerWidth)
            }
            if (child.getStyle()!!.positionType() == YGPositionType.YGPositionTypeAbsolute) {
                continue
            }
            if (child === singleFlexChild) {
                child.setLayoutComputedFlexBasisGeneration(generationCount)
                child.setLayoutComputedFlexBasis(YGFloatOptional(0f))
            } else {
                YGNodeComputeFlexBasisForChild(
                    node, child, availableInnerWidth, widthMeasureMode, availableInnerHeight,
                    availableInnerWidth, availableInnerHeight, heightMeasureMode, direction, config,
                    layoutMarkerData, layoutContext, depth, generationCount
                )
            }
            totalOuterFlexBasis += child.getLayout()!!.computedFlexBasis.unwrap() + child.getMarginForAxis(
                mainAxis,
                availableInnerWidth
            ).unwrap()
        }
        return totalOuterFlexBasis
    }

    fun YGCalculateCollectFlexItemsRowValues(
        node: YGNode,
        ownerDirection: YGDirection,
        mainAxisownerSize: Float,
        availableInnerWidth: Float,
        availableInnerMainDim: Float,
        startOfLineIndex: Int,
        lineCount: Int
    ): YGCollectFlexItemsRowValues {
        val flexAlgoRowMeasurement = YGCollectFlexItemsRowValues()
        flexAlgoRowMeasurement.relativeChildren.ensureCapacity(node.getChildren().size)
        var sizeConsumedOnCurrentLineIncludingMinConstraint = 0f
        val mainAxis = YGResolveFlexDirection(
            node.getStyle()!!.flexDirection(),
            node.resolveDirection(ownerDirection)
        )
        val isNodeFlexWrap = node.getStyle()!!.flexWrap() != YGWrap.YGWrapNoWrap
        var endOfLineIndex = startOfLineIndex
        while (endOfLineIndex < node.getChildren().size) {
            val child = node.getChild(endOfLineIndex)
            if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone || child.getStyle()
                    .positionType() == YGPositionType.YGPositionTypeAbsolute
            ) {
                endOfLineIndex++
                continue
            }
            child.setLineIndex(lineCount)
            val childMarginMainAxis = child.getMarginForAxis(mainAxis, availableInnerWidth).unwrap()
            val flexBasisWithMinAndMaxConstraints = YGNodeBoundAxisWithinMinAndMax(
                child, mainAxis,
                child.getLayout()!!.computedFlexBasis, mainAxisownerSize
            ).unwrap()
            if (sizeConsumedOnCurrentLineIncludingMinConstraint + flexBasisWithMinAndMaxConstraints + childMarginMainAxis > availableInnerMainDim && isNodeFlexWrap && flexAlgoRowMeasurement.itemsOnLine > 0) {
                break
            }
            sizeConsumedOnCurrentLineIncludingMinConstraint += flexBasisWithMinAndMaxConstraints + childMarginMainAxis
            flexAlgoRowMeasurement.sizeConsumedOnCurrentLine += flexBasisWithMinAndMaxConstraints + childMarginMainAxis
            flexAlgoRowMeasurement.itemsOnLine++
            if (child.isNodeFlexible()) {
                flexAlgoRowMeasurement.totalFlexGrowFactors += child.resolveFlexGrow()
                flexAlgoRowMeasurement.totalFlexShrinkScaledFactors += -child.resolveFlexShrink() * child.getLayout()!!.computedFlexBasis.unwrap()
            }
            flexAlgoRowMeasurement.relativeChildren.add(child)
            endOfLineIndex++
        }
        if (flexAlgoRowMeasurement.totalFlexGrowFactors > 0f && flexAlgoRowMeasurement.totalFlexGrowFactors < 1f) {
            flexAlgoRowMeasurement.totalFlexGrowFactors = 1f
        }
        if (flexAlgoRowMeasurement.totalFlexShrinkScaledFactors > 0f && flexAlgoRowMeasurement.totalFlexShrinkScaledFactors < 1f) {
            flexAlgoRowMeasurement.totalFlexShrinkScaledFactors = 1f
        }
        flexAlgoRowMeasurement.endOfLineIndex = endOfLineIndex
        return flexAlgoRowMeasurement
    }

    fun YGDistributeFreeSpaceSecondPass(
        collectedFlexItemsValues: YGCollectFlexItemsRowValues,
        node: YGNode,
        mainAxis: YGFlexDirection,
        crossAxis: YGFlexDirection,
        mainAxisownerSize: Float,
        availableInnerMainDim: Float,
        availableInnerCrossDim: Float,
        availableInnerWidth: Float,
        availableInnerHeight: Float,
        flexBasisOverflows: Boolean,
        measureModeCrossDim: YGMeasureMode,
        performLayout: Boolean,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ): Float {
        var childFlexBasis: Float
        var flexShrinkScaledFactor: Float
        var flexGrowFactor: Float
        var deltaFreeSpace = 0f
        val isMainAxisRow = YGFlexDirectionIsRow(mainAxis)
        val isNodeFlexWrap = node.getStyle()!!.flexWrap() != YGWrap.YGWrapNoWrap
        for (currentRelativeChild in collectedFlexItemsValues.relativeChildren) {
            childFlexBasis = YGNodeBoundAxisWithinMinAndMax(
                currentRelativeChild, mainAxis,
                currentRelativeChild.getLayout()!!.computedFlexBasis, mainAxisownerSize
            ).unwrap()
            var updatedMainSize = childFlexBasis
            if (!YGFloatIsUndefined(
                    collectedFlexItemsValues.remainingFreeSpace
                ) && collectedFlexItemsValues.remainingFreeSpace < 0f
            ) {
                flexShrinkScaledFactor = -currentRelativeChild.resolveFlexShrink() * childFlexBasis
                if (flexShrinkScaledFactor != 0f) {
                    var childSize: Float
                    childSize = if (!YGFloatIsUndefined(
                            collectedFlexItemsValues.totalFlexShrinkScaledFactors
                        ) && collectedFlexItemsValues.totalFlexShrinkScaledFactors == 0f
                    ) {
                        childFlexBasis + flexShrinkScaledFactor
                    } else {
                        childFlexBasis + collectedFlexItemsValues.remainingFreeSpace / collectedFlexItemsValues.totalFlexShrinkScaledFactors * flexShrinkScaledFactor
                    }
                    updatedMainSize = YGNodeBoundAxis(
                        currentRelativeChild, mainAxis, childSize, availableInnerMainDim,
                        availableInnerWidth
                    )
                }
            } else if (!YGFloatIsUndefined(
                    collectedFlexItemsValues.remainingFreeSpace
                ) && collectedFlexItemsValues.remainingFreeSpace > 0f
            ) {
                flexGrowFactor = currentRelativeChild.resolveFlexGrow()
                if (!YGFloatIsUndefined(flexGrowFactor) && flexGrowFactor != 0f) {
                    updatedMainSize = YGNodeBoundAxis(
                        currentRelativeChild, mainAxis,
                        childFlexBasis + collectedFlexItemsValues.remainingFreeSpace / collectedFlexItemsValues.totalFlexGrowFactors * flexGrowFactor,
                        availableInnerMainDim, availableInnerWidth
                    )
                }
            }
            deltaFreeSpace += updatedMainSize - childFlexBasis
            val marginMain =
                currentRelativeChild.getMarginForAxis(mainAxis, availableInnerWidth).unwrap()
            val marginCross =
                currentRelativeChild.getMarginForAxis(crossAxis, availableInnerWidth).unwrap()
            val childCrossSize = RefObject(0f)
            val childMainSize = RefObject(updatedMainSize + marginMain)
            var childCrossMeasureMode: YGMeasureMode
            val childMainMeasureMode = YGMeasureMode.YGMeasureModeExactly
            val childStyle = currentRelativeChild.getStyle()
            if (!childStyle!!.aspectRatio().isUndefined()) {
                childCrossSize.argValue =
                    if (isMainAxisRow) (childMainSize.argValue - marginMain) / childStyle!!.aspectRatio()
                        .unwrap() else (childMainSize.argValue - marginMain) * childStyle!!.aspectRatio()
                        .unwrap()
                childCrossMeasureMode = YGMeasureMode.YGMeasureModeExactly
                childCrossSize.argValue += marginCross
            } else if (!YGFloatIsUndefined(availableInnerCrossDim) && !YGNodeIsStyleDimDefined(
                    currentRelativeChild,
                    crossAxis,
                    availableInnerCrossDim
                ) && measureModeCrossDim == YGMeasureMode.YGMeasureModeExactly && !(isNodeFlexWrap && flexBasisOverflows) && YGNodeAlignItem(
                    node, currentRelativeChild
                ) == YGAlign.YGAlignStretch && currentRelativeChild.marginLeadingValue(
                    crossAxis
                )!!.unit != YGUnit.YGUnitAuto && currentRelativeChild.marginTrailingValue(
                    crossAxis
                )!!.unit != YGUnit.YGUnitAuto
            ) {
                childCrossSize.argValue = availableInnerCrossDim
                childCrossMeasureMode = YGMeasureMode.YGMeasureModeExactly
            } else if (!YGNodeIsStyleDimDefined(
                    currentRelativeChild,
                    crossAxis,
                    availableInnerCrossDim
                )
            ) {
                childCrossSize.argValue = availableInnerCrossDim
                childCrossMeasureMode = if (YGFloatIsUndefined(
                        childCrossSize.argValue
                    )
                ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeAtMost
            } else {
                childCrossSize.argValue = YGResolveValue(
                    currentRelativeChild.getResolvedDimension(dim[crossAxis.getValue()].getValue())!!,
                    availableInnerCrossDim
                ).unwrap() + marginCross
                val isLoosePercentageMeasurement = currentRelativeChild.getResolvedDimension(
                    dim[crossAxis.getValue()]
                        .getValue()
                )!!.unit == YGUnit.YGUnitPercent && measureModeCrossDim != YGMeasureMode.YGMeasureModeExactly
                childCrossMeasureMode = if (YGFloatIsUndefined(
                        childCrossSize.argValue
                    ) || isLoosePercentageMeasurement
                ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
            }
            YGConstrainMaxSizeForMode(
                currentRelativeChild, mainAxis, availableInnerMainDim, availableInnerWidth,
                childMainMeasureMode, childMainSize
            )
            YGConstrainMaxSizeForMode(
                currentRelativeChild, crossAxis, availableInnerCrossDim, availableInnerWidth,
                childCrossMeasureMode, childCrossSize
            )
            val requiresStretchLayout = !YGNodeIsStyleDimDefined(
                currentRelativeChild, crossAxis,
                availableInnerCrossDim
            ) && YGNodeAlignItem(
                node,
                currentRelativeChild
            ) == YGAlign.YGAlignStretch && currentRelativeChild.marginLeadingValue(
                crossAxis
            )!!.unit != YGUnit.YGUnitAuto && currentRelativeChild.marginTrailingValue(
                crossAxis
            )!!.unit != YGUnit.YGUnitAuto
            val childWidth = if (isMainAxisRow) childMainSize else childCrossSize
            val childHeight = if (!isMainAxisRow) childMainSize else childCrossSize
            val childWidthMeasureMode =
                if (isMainAxisRow) childMainMeasureMode else childCrossMeasureMode
            val childHeightMeasureMode =
                if (!isMainAxisRow) childMainMeasureMode else childCrossMeasureMode
            val isLayoutPass = performLayout && !requiresStretchLayout
            YGLayoutNodeInternal(
                currentRelativeChild,
                childWidth.argValue,
                childHeight.argValue,
                node.getLayout()!!.direction(),
                childWidthMeasureMode,
                childHeightMeasureMode,
                availableInnerWidth,
                availableInnerHeight,
                isLayoutPass,
                if (isLayoutPass) LayoutPassReason.kFlexLayout else LayoutPassReason.kFlexMeasure,
                config,
                layoutMarkerData,
                layoutContext,
                depth,
                generationCount
            )
            node.setLayoutHadOverflow(
                node.getLayout()!!.hadOverflow() or currentRelativeChild.getLayout()!!.hadOverflow()
            )
        }
        return deltaFreeSpace
    }

    fun YGDistributeFreeSpaceFirstPass(
        collectedFlexItemsValues: YGCollectFlexItemsRowValues,
        mainAxis: YGFlexDirection,
        mainAxisownerSize: Float,
        availableInnerMainDim: Float,
        availableInnerWidth: Float
    ) {
        var flexShrinkScaledFactor: Float
        var flexGrowFactor: Float
        var baseMainSize: Float
        var boundMainSize: Float
        var deltaFreeSpace = 0f
        for (currentRelativeChild in collectedFlexItemsValues.relativeChildren) {
            val childFlexBasis = YGNodeBoundAxisWithinMinAndMax(
                currentRelativeChild, mainAxis,
                currentRelativeChild.getLayout()!!.computedFlexBasis, mainAxisownerSize
            ).unwrap()
            if (collectedFlexItemsValues.remainingFreeSpace < 0f) {
                flexShrinkScaledFactor = -currentRelativeChild.resolveFlexShrink() * childFlexBasis
                if (!YGFloatIsUndefined(flexShrinkScaledFactor) && flexShrinkScaledFactor != 0f) {
                    baseMainSize =
                        childFlexBasis + collectedFlexItemsValues.remainingFreeSpace / collectedFlexItemsValues.totalFlexShrinkScaledFactors * flexShrinkScaledFactor
                    boundMainSize = YGNodeBoundAxis(
                        currentRelativeChild, mainAxis, baseMainSize, availableInnerMainDim,
                        availableInnerWidth
                    )
                    if (!YGFloatIsUndefined(baseMainSize) && !YGFloatIsUndefined(
                            boundMainSize
                        ) && baseMainSize != boundMainSize
                    ) {
                        deltaFreeSpace += boundMainSize - childFlexBasis
                        collectedFlexItemsValues.totalFlexShrinkScaledFactors -= -currentRelativeChild.resolveFlexShrink() * currentRelativeChild.getLayout()!!.computedFlexBasis.unwrap()
                    }
                }
            } else if (!YGFloatIsUndefined(
                    collectedFlexItemsValues.remainingFreeSpace
                ) && collectedFlexItemsValues.remainingFreeSpace > 0f
            ) {
                flexGrowFactor = currentRelativeChild.resolveFlexGrow()
                if (!YGFloatIsUndefined(flexGrowFactor) && flexGrowFactor != 0f) {
                    baseMainSize =
                        childFlexBasis + collectedFlexItemsValues.remainingFreeSpace / collectedFlexItemsValues.totalFlexGrowFactors * flexGrowFactor
                    boundMainSize = YGNodeBoundAxis(
                        currentRelativeChild, mainAxis, baseMainSize, availableInnerMainDim,
                        availableInnerWidth
                    )
                    if (!YGFloatIsUndefined(baseMainSize) && !YGFloatIsUndefined(
                            boundMainSize
                        ) && baseMainSize != boundMainSize
                    ) {
                        deltaFreeSpace += boundMainSize - childFlexBasis
                        collectedFlexItemsValues.totalFlexGrowFactors -= flexGrowFactor
                    }
                }
            }
        }
        collectedFlexItemsValues.remainingFreeSpace -= deltaFreeSpace
    }

    fun YGResolveFlexibleLength(
        node: YGNode,
        collectedFlexItemsValues: YGCollectFlexItemsRowValues,
        mainAxis: YGFlexDirection,
        crossAxis: YGFlexDirection,
        mainAxisownerSize: Float,
        availableInnerMainDim: Float,
        availableInnerCrossDim: Float,
        availableInnerWidth: Float,
        availableInnerHeight: Float,
        flexBasisOverflows: Boolean,
        measureModeCrossDim: YGMeasureMode,
        performLayout: Boolean,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int
    ) {
        val originalFreeSpace = collectedFlexItemsValues.remainingFreeSpace
        YGDistributeFreeSpaceFirstPass(
            collectedFlexItemsValues, mainAxis, mainAxisownerSize, availableInnerMainDim,
            availableInnerWidth
        )
        val distributedFreeSpace = YGDistributeFreeSpaceSecondPass(
            collectedFlexItemsValues,
            node,
            mainAxis,
            crossAxis,
            mainAxisownerSize,
            availableInnerMainDim,
            availableInnerCrossDim,
            availableInnerWidth,
            availableInnerHeight,
            flexBasisOverflows,
            measureModeCrossDim,
            performLayout,
            config,
            layoutMarkerData,
            layoutContext,
            depth,
            generationCount
        )
        collectedFlexItemsValues.remainingFreeSpace = originalFreeSpace - distributedFreeSpace
    }

    fun YGJustifyMainAxis(
        node: YGNode,
        collectedFlexItemsValues: YGCollectFlexItemsRowValues,
        startOfLineIndex: Int,
        mainAxis: YGFlexDirection,
        crossAxis: YGFlexDirection,
        measureModeMainDim: YGMeasureMode,
        measureModeCrossDim: YGMeasureMode,
        mainAxisownerSize: Float,
        ownerWidth: Float,
        availableInnerMainDim: Float,
        availableInnerCrossDim: Float,
        availableInnerWidth: Float,
        performLayout: Boolean,
        layoutContext: Any?
    ) {
        val style = node.getStyle()
        val leadingPaddingAndBorderMain =
            node.getLeadingPaddingAndBorder(mainAxis, ownerWidth).unwrap()
        val trailingPaddingAndBorderMain =
            node.getTrailingPaddingAndBorder(mainAxis, ownerWidth).unwrap()
        if (measureModeMainDim == YGMeasureMode.YGMeasureModeAtMost && collectedFlexItemsValues.remainingFreeSpace > 0f) {
            if (!style!!.minDimensions().getCompactValue(dim[mainAxis.getValue()].getValue())
                    .isUndefined() && !YGResolveValue(
                    style!!.minDimensions().getCompactValue(dim[mainAxis.getValue()].getValue()),
                    mainAxisownerSize
                ).isUndefined()
            ) {
                val minAvailableMainDim = YGResolveValue(
                    style!!.minDimensions().getCompactValue(dim[mainAxis.getValue()].getValue()),
                    mainAxisownerSize
                ).unwrap() - leadingPaddingAndBorderMain - trailingPaddingAndBorderMain
                val occupiedSpaceByChildNodes =
                    availableInnerMainDim - collectedFlexItemsValues.remainingFreeSpace
                collectedFlexItemsValues.remainingFreeSpace = YGFloatMax(
                    0f,
                    minAvailableMainDim - occupiedSpaceByChildNodes
                )
            } else {
                collectedFlexItemsValues.remainingFreeSpace = 0f
            }
        }
        var numberOfAutoMarginsOnCurrentLine = 0
        for (i in startOfLineIndex until collectedFlexItemsValues.endOfLineIndex) {
            val child = node.getChild(i)
            if (child!!.getStyle()!!.positionType() != YGPositionType.YGPositionTypeAbsolute) {
                if (child.marginLeadingValue(mainAxis)!!.unit == YGUnit.YGUnitAuto) {
                    numberOfAutoMarginsOnCurrentLine++
                }
                if (child.marginTrailingValue(mainAxis)!!.unit == YGUnit.YGUnitAuto) {
                    numberOfAutoMarginsOnCurrentLine++
                }
            }
        }
        var leadingMainDim = 0f
        var betweenMainDim = 0f
        val justifyContent = node.getStyle()!!.justifyContent()
        if (numberOfAutoMarginsOnCurrentLine == 0) {
            when (justifyContent) {
                YGJustify.YGJustifyCenter -> leadingMainDim =
                    collectedFlexItemsValues.remainingFreeSpace / 2
                YGJustify.YGJustifyFlexEnd -> leadingMainDim =
                    collectedFlexItemsValues.remainingFreeSpace
                YGJustify.YGJustifySpaceBetween -> betweenMainDim =
                    if (collectedFlexItemsValues.itemsOnLine > 1) {
                        YGFloatMax(
                            collectedFlexItemsValues.remainingFreeSpace,
                            0f
                        ) / (collectedFlexItemsValues.itemsOnLine - 1)
                    } else {
                        0f
                    }
                YGJustify.YGJustifySpaceEvenly -> {
                    betweenMainDim =
                        collectedFlexItemsValues.remainingFreeSpace / (collectedFlexItemsValues.itemsOnLine + 1)
                    leadingMainDim = betweenMainDim
                }
                YGJustify.YGJustifySpaceAround -> {
                    betweenMainDim =
                        collectedFlexItemsValues.remainingFreeSpace / collectedFlexItemsValues.itemsOnLine
                    leadingMainDim = betweenMainDim / 2
                }
                YGJustify.YGJustifyFlexStart -> {}
            }
        }
        collectedFlexItemsValues.mainDim = leadingPaddingAndBorderMain + leadingMainDim
        collectedFlexItemsValues.crossDim = 0f
        var maxAscentForCurrentLine = 0f
        var maxDescentForCurrentLine = 0f
        val isNodeBaselineLayout = YGIsBaselineLayout(node)
        for (i in startOfLineIndex until collectedFlexItemsValues.endOfLineIndex) {
            val child = node.getChild(i)
            val childStyle = child!!.getStyle()
            val childLayout = child.getLayout()
            if (childStyle!!.display() == YGDisplay.YGDisplayNone) {
                continue
            }
            if (childStyle!!.positionType() == YGPositionType.YGPositionTypeAbsolute && child.isLeadingPositionDefined(
                    mainAxis
                )
            ) {
                if (performLayout) {
                    child.setLayoutPosition(
                        child.getLeadingPosition(mainAxis, availableInnerMainDim)
                            .unwrap() + node.getLeadingBorder(
                            mainAxis
                        ) + child.getLeadingMargin(mainAxis, availableInnerWidth).unwrap(),
                        pos[mainAxis.getValue()].getValue()
                    )
                }
            } else {
                if (childStyle!!.positionType() != YGPositionType.YGPositionTypeAbsolute) {
                    if (child.marginLeadingValue(mainAxis)!!.unit == YGUnit.YGUnitAuto) {
                        collectedFlexItemsValues.mainDim += collectedFlexItemsValues.remainingFreeSpace / numberOfAutoMarginsOnCurrentLine
                    }
                    if (performLayout) {
                        child.setLayoutPosition(
                            childLayout!!.position[pos[mainAxis.getValue()].getValue()] + collectedFlexItemsValues.mainDim,
                            pos[mainAxis.getValue()].getValue()
                        )
                    }
                    if (child.marginTrailingValue(mainAxis)!!.unit == YGUnit.YGUnitAuto) {
                        collectedFlexItemsValues.mainDim += collectedFlexItemsValues.remainingFreeSpace / numberOfAutoMarginsOnCurrentLine
                    }
                    val canSkipFlex =
                        !performLayout && measureModeCrossDim == YGMeasureMode.YGMeasureModeExactly
                    if (canSkipFlex) {
                        collectedFlexItemsValues.mainDim += betweenMainDim + child.getMarginForAxis(
                            mainAxis,
                            availableInnerWidth
                        ).unwrap() + childLayout!!.computedFlexBasis.unwrap()
                        collectedFlexItemsValues.crossDim = availableInnerCrossDim
                    } else {
                        collectedFlexItemsValues.mainDim += betweenMainDim + YGNodeDimWithMargin(
                            child, mainAxis,
                            availableInnerWidth
                        )
                        if (isNodeBaselineLayout) {
                            val ascent = YGBaseline(
                                child, layoutContext
                            ) + child.getLeadingMargin(
                                YGFlexDirection.YGFlexDirectionColumn,
                                availableInnerWidth
                            ).unwrap()
                            val descent =
                                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] + child.getMarginForAxis(
                                    YGFlexDirection.YGFlexDirectionColumn,
                                    availableInnerWidth
                                ).unwrap() - ascent
                            maxAscentForCurrentLine = YGFloatMax(maxAscentForCurrentLine, ascent)
                            maxDescentForCurrentLine = YGFloatMax(maxDescentForCurrentLine, descent)
                        } else {
                            collectedFlexItemsValues.crossDim = YGFloatMax(
                                collectedFlexItemsValues.crossDim,
                                YGNodeDimWithMargin(
                                    child, crossAxis, availableInnerWidth
                                )
                            )
                        }
                    }
                } else if (performLayout) {
                    child.setLayoutPosition(
                        childLayout!!.position[pos[mainAxis.getValue()].getValue()] + node.getLeadingBorder(
                            mainAxis
                        ) + leadingMainDim, pos[mainAxis.getValue()].getValue()
                    )
                }
            }
        }
        collectedFlexItemsValues.mainDim += trailingPaddingAndBorderMain
        if (isNodeBaselineLayout) {
            collectedFlexItemsValues.crossDim = maxAscentForCurrentLine + maxDescentForCurrentLine
        }
    }

    //
    // This is the main routine that implements a subset of the flexbox layout
    // algorithm described in the W3C CSS documentation:
    // https://www.w3.org/TR/CSS3-flexbox/.
    //
    // Limitations of this algorithm, compared to the full standard:
    //  * Display property is always assumed to be 'flex' except for Text nodes,
    //    which are assumed to be 'inline-flex'.
    //  * The 'zIndex' property (or any form of z ordering) is not supported. Nodes
    //    are stacked in document order.
    //  * The 'order' property is not supported. The order of flex items is always
    //    defined by document order.
    //  * The 'visibility' property is always assumed to be 'visible'. Values of
    //    'collapse' and 'hidden' are not supported.
    //  * There is no support for forced breaks.
    //  * It does not support vertical inline directions (top-to-bottom or
    //    bottom-to-top text).
    //
    // Deviations from standard:
    //  * Section 4.5 of the spec indicates that all flex items have a default
    //    minimum main size. For text blocks, for example, this is the width of the
    //    widest word. Calculating the minimum width is expensive, so we forego it
    //    and assume a default minimum main size of 0.
    //  * Min/Max sizes in the main axis are not honored when resolving flexible
    //    lengths.
    //  * The spec indicates that the default value for 'flexDirection' is 'row',
    //    but the algorithm below assumes a default of 'column'.
    //
    // Input parameters:
    //    - node: current node to be sized and layed out
    //    - availableWidth & availableHeight: available size to be used for sizing
    //      the node or YGUndefined if the size is not available; interpretation
    //      depends on layout flags
    //    - ownerDirection: the inline (text) direction within the owner
    //      (left-to-right or right-to-left)
    //    - widthMeasureMode: indicates the sizing rules for the width (see below
    //      for explanation)
    //    - heightMeasureMode: indicates the sizing rules for the height (see below
    //      for explanation)
    //    - performLayout: specifies whether the caller is interested in just the
    //      dimensions of the node or it requires the entire node and its subtree to
    //      be layed out (with final positions)
    //
    // Details:
    //    This routine is called recursively to lay out subtrees of flexbox
    //    elements. It uses the information in node.getStyle(), which is treated as a
    //    read-only input. It is responsible for setting the layout.direction and
    //    layout.measuredDimensions fields for the input node as well as the
    //    layout.position and layout.lineIndex fields for its child nodes. The
    //    layout.measuredDimensions field includes any border or padding for the
    //    node but does not include margins.
    //
    //    The spec describes four different layout modes: "fill available", "max
    //    content", "min content", and "fit content". Of these, we don't use "min
    //    content" because we don't support default minimum main sizes (see above
    //    for details). Each of our measure modes maps to a layout mode from the
    //    spec (https://www.w3.org/TR/CSS3-sizing/#terms):
    //      - YGMeasureModeUndefined: max content
    //      - YGMeasureModeExactly: fill available
    //      - YGMeasureModeAtMost: fit content
    //
    //    When calling YGNodelayoutImpl and YGLayoutNodeInternal, if the caller
    //    passes an available size of undefined then it must also pass a measure
    //    mode of YGMeasureModeUndefined in that dimension.
    //
    private fun YGNodelayoutImpl(
        node: YGNode,
        availableWidth: Float,
        availableHeight: Float,
        ownerDirection: YGDirection,
        widthMeasureMode: YGMeasureMode,
        heightMeasureMode: YGMeasureMode,
        ownerWidth: Float,
        ownerHeight: Float,
        performLayout: Boolean,
        config: YGConfig?,
        layoutMarkerData: LayoutData,
        layoutContext: Any?,
        depth: Int,
        generationCount: Int,
        reason: LayoutPassReason
    ) {
        YGAssertWithNode(
            node,
            !YGFloatIsUndefined(availableWidth) || widthMeasureMode == YGMeasureMode.YGMeasureModeUndefined,
            "availableWidth is indefinite so widthMeasureMode must be " + "YGMeasureModeUndefined"
        )
        YGAssertWithNode(
            node,
            !YGFloatIsUndefined(availableHeight) || heightMeasureMode == YGMeasureMode.YGMeasureModeUndefined,
            "availableHeight is indefinite so heightMeasureMode must be " + "YGMeasureModeUndefined"
        )
        if (performLayout) layoutMarkerData.layouts += 1 else layoutMarkerData.measures += 1

        // Set the resolved resolution in the node's layout.
        val direction = node.resolveDirection(ownerDirection)
        node.setLayoutDirection(direction)
        val flexRowDirection = YGResolveFlexDirection(YGFlexDirection.YGFlexDirectionRow, direction)
        val flexColumnDirection =
            YGResolveFlexDirection(YGFlexDirection.YGFlexDirectionColumn, direction)
        val startEdge =
            if (direction == YGDirection.YGDirectionLTR) YGEdge.YGEdgeLeft else YGEdge.YGEdgeRight
        val endEdge =
            if (direction == YGDirection.YGDirectionLTR) YGEdge.YGEdgeRight else YGEdge.YGEdgeLeft
        val marginRowLeading = node.getLeadingMargin(flexRowDirection, ownerWidth).unwrap()
        node.setLayoutMargin(marginRowLeading, startEdge.getValue())
        val marginRowTrailing = node.getTrailingMargin(flexRowDirection, ownerWidth).unwrap()
        node.setLayoutMargin(marginRowTrailing, endEdge.getValue())
        val marginColumnLeading = node.getLeadingMargin(flexColumnDirection, ownerWidth).unwrap()
        node.setLayoutMargin(marginColumnLeading, YGEdge.YGEdgeTop.getValue())
        val marginColumnTrailing = node.getTrailingMargin(flexColumnDirection, ownerWidth).unwrap()
        node.setLayoutMargin(marginColumnTrailing, YGEdge.YGEdgeBottom.getValue())
        val marginAxisRow = marginRowLeading + marginRowTrailing
        val marginAxisColumn = marginColumnLeading + marginColumnTrailing
        node.setLayoutBorder(node.getLeadingBorder(flexRowDirection), startEdge.getValue())
        node.setLayoutBorder(node.getTrailingBorder(flexRowDirection), endEdge.getValue())
        node.setLayoutBorder(
            node.getLeadingBorder(flexColumnDirection),
            YGEdge.YGEdgeTop.getValue()
        )
        node.setLayoutBorder(
            node.getTrailingBorder(flexColumnDirection),
            YGEdge.YGEdgeBottom.getValue()
        )
        node.setLayoutPadding(
            node.getLeadingPadding(flexRowDirection, ownerWidth).unwrap(),
            startEdge.getValue()
        )
        node.setLayoutPadding(
            node.getTrailingPadding(flexRowDirection, ownerWidth).unwrap(),
            endEdge.getValue()
        )
        node.setLayoutPadding(
            node.getLeadingPadding(flexColumnDirection, ownerWidth).unwrap(),
            YGEdge.YGEdgeTop.getValue()
        )
        node.setLayoutPadding(
            node.getTrailingPadding(flexColumnDirection, ownerWidth).unwrap(),
            YGEdge.YGEdgeBottom.getValue()
        )
        if (node.hasMeasureFunc()) {
            YGNodeWithMeasureFuncSetMeasuredDimensions(
                node,
                availableWidth - marginAxisRow,
                availableHeight - marginAxisColumn,
                widthMeasureMode,
                heightMeasureMode,
                ownerWidth,
                ownerHeight,
                layoutMarkerData,
                layoutContext,
                reason
            )
            return
        }
        val childCount = YGNodeGetChildCount(node)
        if (childCount == 0) {
            YGNodeEmptyContainerSetMeasuredDimensions(
                node,
                availableWidth - marginAxisRow,
                availableHeight - marginAxisColumn,
                widthMeasureMode,
                heightMeasureMode,
                ownerWidth,
                ownerHeight
            )
            return
        }

        // If we're not being asked to perform a full layout we can skip the algorithm
        // if we already know the size
        if (!performLayout && YGNodeFixedSizeSetMeasuredDimensions(
                node,
                availableWidth - marginAxisRow,
                availableHeight - marginAxisColumn,
                widthMeasureMode,
                heightMeasureMode,
                ownerWidth,
                ownerHeight
            )
        ) {
            return
        }

        // At this point we know we're going to perform work. Ensure that each child
        // has a mutable copy.
        node.cloneChildrenIfNeeded(layoutContext)
        // Reset layout flags, as they could have changed.
        node.setLayoutHadOverflow(false)

        // STEP 1: CALCULATE VALUES FOR REMAINDER OF ALGORITHM
        val mainAxis = YGResolveFlexDirection(
            node.getStyle()!!.flexDirection(), direction
        )
        val crossAxis = YGFlexDirectionCross(mainAxis, direction)
        val isMainAxisRow = YGFlexDirectionIsRow(mainAxis)
        val isNodeFlexWrap = node.getStyle()!!.flexWrap() != YGWrap.YGWrapNoWrap
        val mainAxisownerSize = if (isMainAxisRow) ownerWidth else ownerHeight
        val crossAxisownerSize = if (isMainAxisRow) ownerHeight else ownerWidth
        val paddingAndBorderAxisMain = YGNodePaddingAndBorderForAxis(node, mainAxis, ownerWidth)
        val leadingPaddingAndBorderCross =
            node.getLeadingPaddingAndBorder(crossAxis, ownerWidth).unwrap()
        val trailingPaddingAndBorderCross =
            node.getTrailingPaddingAndBorder(crossAxis, ownerWidth).unwrap()
        val paddingAndBorderAxisCross = leadingPaddingAndBorderCross + trailingPaddingAndBorderCross
        var measureModeMainDim = if (isMainAxisRow) widthMeasureMode else heightMeasureMode
        val measureModeCrossDim = if (isMainAxisRow) heightMeasureMode else widthMeasureMode
        val paddingAndBorderAxisRow =
            if (isMainAxisRow) paddingAndBorderAxisMain else paddingAndBorderAxisCross
        val paddingAndBorderAxisColumn =
            if (isMainAxisRow) paddingAndBorderAxisCross else paddingAndBorderAxisMain

        // STEP 2: DETERMINE AVAILABLE SIZE IN MAIN AND CROSS DIRECTIONS
        val availableInnerWidth = YGNodeCalculateAvailableInnerDim(
            node, YGDimension.YGDimensionWidth,
            availableWidth - marginAxisRow, paddingAndBorderAxisRow, ownerWidth
        )
        val availableInnerHeight = YGNodeCalculateAvailableInnerDim(
            node, YGDimension.YGDimensionHeight,
            availableHeight - marginAxisColumn, paddingAndBorderAxisColumn, ownerHeight
        )
        var availableInnerMainDim = if (isMainAxisRow) availableInnerWidth else availableInnerHeight
        val availableInnerCrossDim =
            if (isMainAxisRow) availableInnerHeight else availableInnerWidth

        // STEP 3: DETERMINE FLEX BASIS FOR EACH ITEM
        val totalOuterFlexBasis = YGNodeComputeFlexBasisForChildren(
            node,
            availableInnerWidth,
            availableInnerHeight,
            widthMeasureMode,
            heightMeasureMode,
            direction!!,
            mainAxis,
            config,
            performLayout,
            layoutMarkerData,
            layoutContext,
            depth,
            generationCount
        )
        val flexBasisOverflows =
            measureModeMainDim != YGMeasureMode.YGMeasureModeUndefined && totalOuterFlexBasis > availableInnerMainDim
        if (isNodeFlexWrap && flexBasisOverflows && measureModeMainDim == YGMeasureMode.YGMeasureModeAtMost) {
            measureModeMainDim = YGMeasureMode.YGMeasureModeExactly
        }
        // STEP 4: COLLECT FLEX ITEMS INTO FLEX LINES

        // Indexes of children that represent the first and last items in the line.
        var startOfLineIndex = 0
        var endOfLineIndex = 0

        // Number of lines.
        var lineCount = 0

        // Accumulated cross dimensions of all lines so far.
        var totalLineCrossDim = 0f

        // Max main dimension of all the lines.
        var maxLineMainDim = 0f
        var collectedFlexItemsValues = YGCollectFlexItemsRowValues()
        while (endOfLineIndex < childCount) {
            collectedFlexItemsValues = YGCalculateCollectFlexItemsRowValues(
                node, ownerDirection, mainAxisownerSize,
                availableInnerWidth, availableInnerMainDim, startOfLineIndex, lineCount
            )
            endOfLineIndex = collectedFlexItemsValues.endOfLineIndex

            // If we don't need to measure the cross axis, we can skip the entire flex
            // step.
            val canSkipFlex =
                !performLayout && measureModeCrossDim == YGMeasureMode.YGMeasureModeExactly

            // STEP 5: RESOLVING FLEXIBLE LENGTHS ON MAIN AXIS
            // Calculate the remaining available space that needs to be allocated. If
            // the main dimension size isn't known, it is computed based on the line
            // length, so there's no more space left to distribute.
            var sizeBasedOnContent = false
            // If we don't measure with exact main dimension we want to ensure we don't
            // violate min and max
            if (measureModeMainDim != YGMeasureMode.YGMeasureModeExactly) {
                val minDimensions = node.getStyle()!!
                    .minDimensions()
                val maxDimensions = node.getStyle()!!
                    .maxDimensions()
                val minInnerWidth = YGResolveValue(
                    minDimensions[YGDimension.YGDimensionWidth.getValue()],
                    ownerWidth
                ).unwrap() - paddingAndBorderAxisRow
                val maxInnerWidth = YGResolveValue(
                    maxDimensions[YGDimension.YGDimensionWidth.getValue()],
                    ownerWidth
                ).unwrap() - paddingAndBorderAxisRow
                val minInnerHeight = YGResolveValue(
                    minDimensions[YGDimension.YGDimensionHeight.getValue()],
                    ownerHeight
                ).unwrap() - paddingAndBorderAxisColumn
                val maxInnerHeight = YGResolveValue(
                    maxDimensions[YGDimension.YGDimensionHeight.getValue()],
                    ownerHeight
                ).unwrap() - paddingAndBorderAxisColumn
                val minInnerMainDim = if (isMainAxisRow) minInnerWidth else minInnerHeight
                val maxInnerMainDim = if (isMainAxisRow) maxInnerWidth else maxInnerHeight
                if (!YGFloatIsUndefined(
                        minInnerMainDim
                    ) && collectedFlexItemsValues.sizeConsumedOnCurrentLine < minInnerMainDim
                ) {
                    availableInnerMainDim = minInnerMainDim
                } else if (!YGFloatIsUndefined(
                        maxInnerMainDim
                    ) && collectedFlexItemsValues.sizeConsumedOnCurrentLine > maxInnerMainDim
                ) {
                    availableInnerMainDim = maxInnerMainDim
                } else {
                    if (!node.getConfig()!!.useLegacyStretchBehaviour && (YGFloatIsUndefined(
                            collectedFlexItemsValues.totalFlexGrowFactors
                        ) && collectedFlexItemsValues.totalFlexGrowFactors == 0f || YGFloatIsUndefined(
                            node.resolveFlexGrow()
                        ) && node.resolveFlexGrow() == 0f)
                    ) {
                        // If we don't have any children to flex or we can't flex the node
                        // itself, space we've used is all space we need. Root node also
                        // should be shrunk to minimum
                        availableInnerMainDim = collectedFlexItemsValues.sizeConsumedOnCurrentLine
                    }
                    if (node.getConfig()!!.useLegacyStretchBehaviour) {
                        node.setLayoutDidUseLegacyFlag(true)
                    }
                    sizeBasedOnContent = !node.getConfig()!!.useLegacyStretchBehaviour
                }
            }
            if (!sizeBasedOnContent && !YGFloatIsUndefined(availableInnerMainDim)) {
                collectedFlexItemsValues.remainingFreeSpace =
                    availableInnerMainDim - collectedFlexItemsValues.sizeConsumedOnCurrentLine
            } else if (collectedFlexItemsValues.sizeConsumedOnCurrentLine < 0) {
                // availableInnerMainDim is indefinite which means the node is being sized
                // based on its content. sizeConsumedOnCurrentLine is negative which means
                // the node will allocate 0 points for its content. Consequently,
                // remainingFreeSpace is 0 - sizeConsumedOnCurrentLine.
                collectedFlexItemsValues.remainingFreeSpace =
                    -collectedFlexItemsValues.sizeConsumedOnCurrentLine
            }
            if (!canSkipFlex) {
                YGResolveFlexibleLength(
                    node,
                    collectedFlexItemsValues,
                    mainAxis,
                    crossAxis,
                    mainAxisownerSize,
                    availableInnerMainDim,
                    availableInnerCrossDim,
                    availableInnerWidth,
                    availableInnerHeight,
                    flexBasisOverflows,
                    measureModeCrossDim,
                    performLayout,
                    config,
                    layoutMarkerData,
                    layoutContext,
                    depth,
                    generationCount
                )
            }
            node.setLayoutHadOverflow(
                node.getLayout()!!
                    .hadOverflow() or (collectedFlexItemsValues.remainingFreeSpace < 0)
            )

            // STEP 6: MAIN-AXIS JUSTIFICATION & CROSS-AXIS SIZE DETERMINATION

            // At this point, all the children have their dimensions set in the main
            // axis. Their dimensions are also set in the cross axis with the exception
            // of items that are aligned "stretch". We need to compute these stretch
            // values and set the final positions.
            YGJustifyMainAxis(
                node,
                collectedFlexItemsValues,
                startOfLineIndex,
                mainAxis,
                crossAxis,
                measureModeMainDim,
                measureModeCrossDim,
                mainAxisownerSize,
                ownerWidth,
                availableInnerMainDim,
                availableInnerCrossDim,
                availableInnerWidth,
                performLayout,
                layoutContext
            )
            var containerCrossAxis = availableInnerCrossDim
            if (measureModeCrossDim == YGMeasureMode.YGMeasureModeUndefined || measureModeCrossDim == YGMeasureMode.YGMeasureModeAtMost) {
                // Compute the cross axis from the max cross dimension of the children.
                containerCrossAxis = YGNodeBoundAxis(
                    node,
                    crossAxis,
                    collectedFlexItemsValues.crossDim + paddingAndBorderAxisCross,
                    crossAxisownerSize,
                    ownerWidth
                ) - paddingAndBorderAxisCross
            }

            // If there's no flex wrap, the cross dimension is defined by the container.
            if (!isNodeFlexWrap && measureModeCrossDim == YGMeasureMode.YGMeasureModeExactly) {
                collectedFlexItemsValues.crossDim = availableInnerCrossDim
            }

            // Clamp to the min/max size specified on the container.
            collectedFlexItemsValues.crossDim = YGNodeBoundAxis(
                node, crossAxis,
                collectedFlexItemsValues.crossDim + paddingAndBorderAxisCross, crossAxisownerSize,
                ownerWidth
            ) - paddingAndBorderAxisCross

            // STEP 7: CROSS-AXIS ALIGNMENT
            // We can skip child alignment if we're just measuring the container.
            if (performLayout) {
                for (i in startOfLineIndex until endOfLineIndex) {
                    val child = node.getChild(i)
                    if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone) {
                        continue
                    }
                    if (child.getStyle()!!
                            .positionType() == YGPositionType.YGPositionTypeAbsolute
                    ) {
                        // If the child is absolutely positioned and has a
                        // top/left/bottom/right set, override all the previously computed
                        // positions to set it correctly.
                        val isChildLeadingPosDefined = child.isLeadingPositionDefined(crossAxis)
                        if (isChildLeadingPosDefined) {
                            child.setLayoutPosition(
                                child.getLeadingPosition(crossAxis, availableInnerCrossDim)
                                    .unwrap() + node.getLeadingBorder(crossAxis) + child.getLeadingMargin(
                                    crossAxis,
                                    availableInnerWidth
                                ).unwrap(),
                                pos[crossAxis.getValue()].getValue()
                            )
                        }
                        // If leading position is not defined or calculations result in Nan,
                        // default to border + margin
                        if (!isChildLeadingPosDefined || YGFloatIsUndefined(
                                child.getLayout()!!.position[pos[crossAxis.getValue()].getValue()]
                            )
                        ) {
                            child.setLayoutPosition(
                                node.getLeadingBorder(crossAxis) + child.getLeadingMargin(
                                    crossAxis,
                                    availableInnerWidth
                                ).unwrap(), pos[crossAxis.getValue()].getValue()
                            )
                        }
                    } else {
                        var leadingCrossDim = leadingPaddingAndBorderCross

                        // For a relative children, we're either using alignItems (owner) or
                        // alignSelf (child) in order to determine the position in the cross
                        // axis
                        val alignItem = YGNodeAlignItem(node, child)

                        // If the child uses align stretch, we need to lay it out one more
                        // time, this time forcing the cross-axis size to be the computed
                        // cross size for the current line.
                        if (alignItem == YGAlign.YGAlignStretch && child.marginLeadingValue(
                                crossAxis
                            )!!.unit != YGUnit.YGUnitAuto && child.marginTrailingValue(
                                crossAxis
                            )!!.unit != YGUnit.YGUnitAuto
                        ) {
                            // If the child defines a definite size for its cross axis, there's
                            // no need to stretch.
                            if (!YGNodeIsStyleDimDefined(
                                    child, crossAxis, availableInnerCrossDim
                                )
                            ) {
                                val childMainSize = RefObject(
                                    child.getLayout()!!.measuredDimensions[dim[mainAxis.getValue()].getValue()]
                                )
                                val childStyle = child.getStyle()
                                val childCrossSize = RefObject(
                                    if (!childStyle!!.aspectRatio()
                                            .isUndefined()
                                    ) child.getMarginForAxis(
                                        crossAxis,
                                        availableInnerWidth
                                    )
                                        .unwrap() + (if (isMainAxisRow) childMainSize.argValue / childStyle!!.aspectRatio()
                                        .unwrap() else childMainSize.argValue * childStyle!!.aspectRatio()
                                        .unwrap()) else collectedFlexItemsValues.crossDim
                                )
                                childMainSize.argValue += child.getMarginForAxis(
                                    mainAxis,
                                    availableInnerWidth
                                )
                                    .unwrap()
                                YGConstrainMaxSizeForMode(
                                    child,
                                    mainAxis,
                                    availableInnerMainDim,
                                    availableInnerWidth,
                                    YGMeasureMode.YGMeasureModeExactly,
                                    childMainSize
                                )
                                YGConstrainMaxSizeForMode(
                                    child,
                                    crossAxis,
                                    availableInnerCrossDim,
                                    availableInnerWidth,
                                    YGMeasureMode.YGMeasureModeExactly,
                                    childCrossSize
                                )
                                val childWidth =
                                    if (isMainAxisRow) childMainSize.argValue else childCrossSize.argValue
                                val childHeight =
                                    if (!isMainAxisRow) childMainSize.argValue else childCrossSize.argValue
                                val alignContent = node.getStyle()!!.alignContent()
                                val crossAxisDoesNotGrow =
                                    alignContent != YGAlign.YGAlignStretch && isNodeFlexWrap
                                val childWidthMeasureMode = if (YGFloatIsUndefined(
                                        childWidth
                                    ) || !isMainAxisRow && crossAxisDoesNotGrow
                                ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
                                val childHeightMeasureMode = if (YGFloatIsUndefined(
                                        childHeight
                                    ) || isMainAxisRow && crossAxisDoesNotGrow
                                ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
                                YGLayoutNodeInternal(
                                    child,
                                    childWidth,
                                    childHeight,
                                    direction,
                                    childWidthMeasureMode,
                                    childHeightMeasureMode,
                                    availableInnerWidth,
                                    availableInnerHeight,
                                    true,
                                    LayoutPassReason.kStretch,
                                    config,
                                    layoutMarkerData,
                                    layoutContext,
                                    depth,
                                    generationCount
                                )
                            }
                        } else {
                            val remainingCrossDim = containerCrossAxis - YGNodeDimWithMargin(
                                child, crossAxis,
                                availableInnerWidth
                            )
                            if (child.marginLeadingValue(crossAxis)!!.unit == YGUnit.YGUnitAuto && child.marginTrailingValue(
                                    crossAxis
                                )!!.unit == YGUnit.YGUnitAuto
                            ) {
                                leadingCrossDim += YGFloatMax(0.0f, remainingCrossDim / 2)
                            } else if (child.marginTrailingValue(crossAxis)!!.unit == YGUnit.YGUnitAuto) {
                                // No-Op
                            } else if (child.marginLeadingValue(crossAxis)!!.unit == YGUnit.YGUnitAuto) {
                                leadingCrossDim += YGFloatMax(0.0f, remainingCrossDim)
                            } else if (alignItem == YGAlign.YGAlignFlexStart) {
                                // No-Op
                            } else if (alignItem == YGAlign.YGAlignCenter) {
                                leadingCrossDim += remainingCrossDim / 2
                            } else {
                                leadingCrossDim += remainingCrossDim
                            }
                        }
                        // And we apply the position
                        child.setLayoutPosition(
                            child.getLayout()!!.position[pos[crossAxis.getValue()].getValue()] + totalLineCrossDim + leadingCrossDim,
                            pos[crossAxis.getValue()].getValue()
                        )
                    }
                }
            }
            totalLineCrossDim += collectedFlexItemsValues.crossDim
            maxLineMainDim = YGFloatMax(maxLineMainDim, collectedFlexItemsValues.mainDim)
            lineCount++
            startOfLineIndex = endOfLineIndex
        }

        // STEP 8: MULTI-LINE CONTENT ALIGNMENT
        // currentLead stores the size of the cross dim
        if (performLayout && (isNodeFlexWrap || YGIsBaselineLayout(node))) {
            var crossDimLead = 0f
            var currentLead = leadingPaddingAndBorderCross
            if (!YGFloatIsUndefined(availableInnerCrossDim)) {
                val remainingAlignContentDim = availableInnerCrossDim - totalLineCrossDim
                when (node.getStyle()!!.alignContent()) {
                    YGAlign.YGAlignFlexEnd -> currentLead += remainingAlignContentDim
                    YGAlign.YGAlignCenter -> currentLead += remainingAlignContentDim / 2
                    YGAlign.YGAlignStretch -> if (availableInnerCrossDim > totalLineCrossDim) {
                        crossDimLead = remainingAlignContentDim / lineCount
                    }
                    YGAlign.YGAlignSpaceAround -> if (availableInnerCrossDim > totalLineCrossDim) {
                        currentLead += remainingAlignContentDim / (2 * lineCount)
                        if (lineCount > 1) {
                            crossDimLead = remainingAlignContentDim / lineCount
                        }
                    } else {
                        currentLead += remainingAlignContentDim / 2
                    }
                    YGAlign.YGAlignSpaceBetween -> if (availableInnerCrossDim > totalLineCrossDim && lineCount > 1) {
                        crossDimLead = remainingAlignContentDim / (lineCount - 1)
                    }
                    YGAlign.YGAlignAuto, YGAlign.YGAlignFlexStart, YGAlign.YGAlignBaseline -> {}
                }
            }
            var endIndex = 0
            for (i in 0 until lineCount) {
                val startIndex = endIndex
                var ii: Int

                // compute the line's height and find the endIndex
                var lineHeight = 0f
                var maxAscentForCurrentLine = 0f
                var maxDescentForCurrentLine = 0f
                ii = startIndex
                while (ii < childCount) {
                    val child = node.getChild(ii)
                    if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone) {
                        ii++
                        continue
                    }
                    if (child.getStyle()!!
                            .positionType() != YGPositionType.YGPositionTypeAbsolute
                    ) {
                        if (child.getLineIndex() != i) {
                            break
                        }
                        if (YGNodeIsLayoutDimDefined(
                                child, crossAxis
                            )
                        ) {
                            lineHeight = YGFloatMax(
                                lineHeight,
                                child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] + child.getMarginForAxis(
                                    crossAxis,
                                    availableInnerWidth
                                ).unwrap()
                            )
                        }
                        if (YGNodeAlignItem(node, child) == YGAlign.YGAlignBaseline) {
                            val ascent = YGBaseline(
                                child, layoutContext
                            ) + child.getLeadingMargin(
                                YGFlexDirection.YGFlexDirectionColumn,
                                availableInnerWidth
                            ).unwrap()
                            val descent =
                                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] + child.getMarginForAxis(
                                    YGFlexDirection.YGFlexDirectionColumn,
                                    availableInnerWidth
                                ).unwrap() - ascent
                            maxAscentForCurrentLine = YGFloatMax(maxAscentForCurrentLine, ascent)
                            maxDescentForCurrentLine = YGFloatMax(maxDescentForCurrentLine, descent)
                            lineHeight = YGFloatMax(
                                lineHeight,
                                maxAscentForCurrentLine + maxDescentForCurrentLine
                            )
                        }
                    }
                    ii++
                }
                endIndex = ii
                lineHeight += crossDimLead
                if (performLayout) {
                    ii = startIndex
                    while (ii < endIndex) {
                        val child = node.getChild(ii)
                        if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone) {
                            ii++
                            continue
                        }
                        if (child.getStyle()!!
                                .positionType() != YGPositionType.YGPositionTypeAbsolute
                        ) {
                            when (YGNodeAlignItem(node, child)) {
                                YGAlign.YGAlignFlexStart -> {
                                    child.setLayoutPosition(
                                        currentLead + child.getLeadingMargin(
                                            crossAxis,
                                            availableInnerWidth
                                        )
                                            .unwrap(), pos[crossAxis.getValue()].getValue()
                                    )
                                }
                                YGAlign.YGAlignFlexEnd -> {
                                    child.setLayoutPosition(
                                        currentLead + lineHeight - child.getTrailingMargin(
                                            crossAxis,
                                            availableInnerWidth
                                        )
                                            .unwrap() - child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()],
                                        pos[crossAxis.getValue()].getValue()
                                    )
                                }
                                YGAlign.YGAlignCenter -> {
                                    val childHeight =
                                        child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()]
                                    child.setLayoutPosition(
                                        currentLead + (lineHeight - childHeight) / 2,
                                        pos[crossAxis.getValue()].getValue()
                                    )
                                }
                                YGAlign.YGAlignStretch -> {
                                    child.setLayoutPosition(
                                        currentLead + child.getLeadingMargin(
                                            crossAxis,
                                            availableInnerWidth
                                        )
                                            .unwrap(), pos[crossAxis.getValue()].getValue()
                                    )

                                    // Remeasure child with the line height as it as been only
                                    // measured with the owners height yet.
                                    if (!YGNodeIsStyleDimDefined(
                                            child, crossAxis, availableInnerCrossDim
                                        )
                                    ) {
                                        val childWidth =
                                            if (isMainAxisRow) child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()] + child.getMarginForAxis(
                                                mainAxis,
                                                availableInnerWidth
                                            ).unwrap() else lineHeight
                                        val childHeight =
                                            if (!isMainAxisRow) child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()] + child.getMarginForAxis(
                                                crossAxis,
                                                availableInnerWidth
                                            ).unwrap() else lineHeight
                                        if (!(YGFloatsEqual(
                                                childWidth,
                                                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionWidth.getValue()]
                                            ) && YGFloatsEqual(
                                                childHeight,
                                                child.getLayout()!!.measuredDimensions[YGDimension.YGDimensionHeight.getValue()]
                                            ))
                                        ) {
                                            YGLayoutNodeInternal(
                                                child,
                                                childWidth,
                                                childHeight,
                                                direction,
                                                YGMeasureMode.YGMeasureModeExactly,
                                                YGMeasureMode.YGMeasureModeExactly,
                                                availableInnerWidth,
                                                availableInnerHeight,
                                                true,
                                                LayoutPassReason.kMultilineStretch,
                                                config,
                                                layoutMarkerData,
                                                layoutContext,
                                                depth,
                                                generationCount
                                            )
                                        }
                                    }
                                }
                                YGAlign.YGAlignBaseline -> {
                                    child.setLayoutPosition(
                                        currentLead + maxAscentForCurrentLine - YGBaseline(
                                            child,
                                            layoutContext
                                        ) + child.getLeadingPosition(
                                            YGFlexDirection.YGFlexDirectionColumn,
                                            availableInnerCrossDim
                                        ).unwrap(), YGEdge.YGEdgeTop.getValue()
                                    )
                                }
                                YGAlign.YGAlignAuto, YGAlign.YGAlignSpaceBetween, YGAlign.YGAlignSpaceAround -> {}
                            }
                        }
                        ii++
                    }
                }
                currentLead += lineHeight
            }
        }

        // STEP 9: COMPUTING FINAL DIMENSIONS
        node.setLayoutMeasuredDimension(
            YGNodeBoundAxis(
                node,
                YGFlexDirection.YGFlexDirectionRow,
                availableWidth - marginAxisRow,
                ownerWidth,
                ownerWidth
            ),
            YGDimension.YGDimensionWidth.getValue()
        )
        node.setLayoutMeasuredDimension(
            YGNodeBoundAxis(
                node,
                YGFlexDirection.YGFlexDirectionColumn,
                availableHeight - marginAxisColumn,
                ownerHeight,
                ownerWidth
            ), YGDimension.YGDimensionHeight.getValue()
        )

        // If the user didn't specify a width or height for the node, set the
        // dimensions based on the children.
        if (measureModeMainDim == YGMeasureMode.YGMeasureModeUndefined || node.getStyle()
                .overflow() != YGOverflow.YGOverflowScroll && measureModeMainDim == YGMeasureMode.YGMeasureModeAtMost
        ) {
            // Clamp the size to the min/max size, if specified, and make sure it
            // doesn't go below the padding and border amount.
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(node, mainAxis, maxLineMainDim, mainAxisownerSize, ownerWidth),
                dim[mainAxis.getValue()].getValue()
            )
        } else if (measureModeMainDim == YGMeasureMode.YGMeasureModeAtMost && node.getStyle()!!
                .overflow() == YGOverflow.YGOverflowScroll
        ) {
            node.setLayoutMeasuredDimension(
                YGFloatMax(
                    YGFloatMin(
                        availableInnerMainDim + paddingAndBorderAxisMain,
                        YGNodeBoundAxisWithinMinAndMax(
                            node, mainAxis, YGFloatOptional(
                                maxLineMainDim
                            ),
                            mainAxisownerSize
                        ).unwrap()
                    ), paddingAndBorderAxisMain
                ), dim[mainAxis.getValue()].getValue()
            )
        }
        if (measureModeCrossDim == YGMeasureMode.YGMeasureModeUndefined || node.getStyle()
                .overflow() != YGOverflow.YGOverflowScroll && measureModeCrossDim == YGMeasureMode.YGMeasureModeAtMost
        ) {
            // Clamp the size to the min/max size, if specified, and make sure it
            // doesn't go below the padding and border amount.
            node.setLayoutMeasuredDimension(
                YGNodeBoundAxis(
                    node,
                    crossAxis,
                    totalLineCrossDim + paddingAndBorderAxisCross,
                    crossAxisownerSize,
                    ownerWidth
                ), dim[crossAxis.getValue()].getValue()
            )
        } else if (measureModeCrossDim == YGMeasureMode.YGMeasureModeAtMost && node.getStyle()!!
                .overflow() == YGOverflow.YGOverflowScroll
        ) {
            node.setLayoutMeasuredDimension(
                YGFloatMax(
                    YGFloatMin(
                        availableInnerCrossDim + paddingAndBorderAxisCross,
                        YGNodeBoundAxisWithinMinAndMax(
                            node, crossAxis,
                            YGFloatOptional(totalLineCrossDim + paddingAndBorderAxisCross),
                            crossAxisownerSize
                        ).unwrap()
                    ), paddingAndBorderAxisCross
                ), dim[crossAxis.getValue()].getValue()
            )
        }

        // As we only wrapped in normal direction yet, we need to reverse the
        // positions on wrap-reverse.
        if (performLayout && node.getStyle()!!.flexWrap() == YGWrap.YGWrapWrapReverse) {
            for (i in 0 until childCount) {
                val child = YGNodeGetChild(node, i)
                if (child!!.getStyle()!!.positionType() != YGPositionType.YGPositionTypeAbsolute) {
                    child.setLayoutPosition(
                        node.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()] - child.getLayout()!!.position[pos[crossAxis.getValue()]
                            .getValue()] - child.getLayout()!!.measuredDimensions[dim[crossAxis.getValue()].getValue()],
                        pos[crossAxis.getValue()].getValue()
                    )
                }
            }
        }
        if (performLayout) {
            // STEP 10: SIZING AND POSITIONING ABSOLUTE CHILDREN
            for (child in node.getChildren()) {
                if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone || child!!.getStyle()
                        .positionType() != YGPositionType.YGPositionTypeAbsolute
                ) {
                    continue
                }
                YGNodeAbsoluteLayoutChild(
                    node,
                    child!!,
                    availableInnerWidth,
                    if (isMainAxisRow) measureModeMainDim else measureModeCrossDim,
                    availableInnerHeight,
                    direction,
                    config,
                    layoutMarkerData,
                    layoutContext,
                    depth,
                    generationCount
                )
            }

            // STEP 11: SETTING TRAILING POSITIONS FOR CHILDREN
            val needsMainTrailingPos =
                mainAxis == YGFlexDirection.YGFlexDirectionRowReverse || mainAxis == YGFlexDirection.YGFlexDirectionColumnReverse
            val needsCrossTrailingPos =
                crossAxis == YGFlexDirection.YGFlexDirectionRowReverse || crossAxis == YGFlexDirection.YGFlexDirectionColumnReverse

            // Set trailing position if necessary.
            if (needsMainTrailingPos || needsCrossTrailingPos) {
                for (i in 0 until childCount) {
                    val child = node.getChild(i)
                    if (child!!.getStyle()!!.display() == YGDisplay.YGDisplayNone) {
                        continue
                    }
                    if (needsMainTrailingPos) {
                        YGNodeSetChildTrailingPosition(node, child, mainAxis)
                    }
                    if (needsCrossTrailingPos) {
                        YGNodeSetChildTrailingPosition(node, child, crossAxis)
                    }
                }
            }
        }
    }

    fun YGSpacer(level: Int): String {
        val spacerLen = spacer.length
        return if (level > spacerLen) {
            spacer.substring(0, 0)
        } else {
            spacer.substring(spacerLen - level)
        }
    }

    fun YGMeasureModeName(mode: YGMeasureMode, performLayout: Boolean): String {
        val kMeasureModeNames = arrayOf("UNDEFINED", "EXACTLY", "AT_MOST")
        val kLayoutModeNames = arrayOf("LAY_UNDEFINED", "LAY_EXACTLY", "LAY_AT_MOST")
        return if (performLayout) kLayoutModeNames[mode.getValue()] else kMeasureModeNames[mode.getValue()]
    }

    fun YGMeasureModeSizeIsExactAndMatchesOldMeasuredSize(
        sizeMode: YGMeasureMode,
        size: Float,
        lastComputedSize: Float
    ): Boolean {
        return sizeMode == YGMeasureMode.YGMeasureModeExactly && YGFloatsEqual(
            size,
            lastComputedSize
        )
    }

    fun YGMeasureModeOldSizeIsUnspecifiedAndStillFits(
        sizeMode: YGMeasureMode,
        size: Float,
        lastSizeMode: YGMeasureMode?,
        lastComputedSize: Float
    ): Boolean {
        return sizeMode == YGMeasureMode.YGMeasureModeAtMost && lastSizeMode == YGMeasureMode.YGMeasureModeUndefined && (size >= lastComputedSize || YGFloatsEqual(
            size, lastComputedSize
        ))
    }

    fun YGMeasureModeNewMeasureSizeIsStricterAndStillValid(
        sizeMode: YGMeasureMode,
        size: Float,
        lastSizeMode: YGMeasureMode?,
        lastSize: Float,
        lastComputedSize: Float
    ): Boolean {
        return lastSizeMode == YGMeasureMode.YGMeasureModeAtMost && sizeMode == YGMeasureMode.YGMeasureModeAtMost && !YGFloatIsUndefined(
            lastSize
        ) && !YGFloatIsUndefined(size) && !YGFloatIsUndefined(
            lastComputedSize
        ) && lastSize > size && (lastComputedSize <= size || YGFloatsEqual(
            size,
            lastComputedSize
        ))
    }

    fun YGRoundToPixelGrid(
        node: YGNode,
        pointScaleFactor: Double,
        absoluteLeft: Double,
        absoluteTop: Double
    ) {
        if (pointScaleFactor == 0.0) {
            return
        }
        val nodeLeft = node.getLayout()!!.position[YGEdge.YGEdgeLeft.getValue()].toDouble()
        val nodeTop = node.getLayout()!!.position[YGEdge.YGEdgeTop.getValue()].toDouble()
        val nodeWidth =
            node.getLayout()!!.dimensions[YGDimension.YGDimensionWidth.getValue()].toDouble()
        val nodeHeight =
            node.getLayout()!!.dimensions[YGDimension.YGDimensionHeight.getValue()].toDouble()
        val absoluteNodeLeft = absoluteLeft + nodeLeft
        val absoluteNodeTop = absoluteTop + nodeTop
        val absoluteNodeRight = absoluteNodeLeft + nodeWidth
        val absoluteNodeBottom = absoluteNodeTop + nodeHeight
        val textRounding = node.getNodeType() == YGNodeType.YGNodeTypeText
        node.setLayoutPosition(
            YGRoundValueToPixelGrid(nodeLeft, pointScaleFactor, false, textRounding),
            YGEdge.YGEdgeLeft.getValue()
        )
        node.setLayoutPosition(
            YGRoundValueToPixelGrid(nodeTop, pointScaleFactor, false, textRounding),
            YGEdge.YGEdgeTop.getValue()
        )
        val hasFractionalWidth =
            !YGDoubleEqual(nodeWidth * pointScaleFactor % 1.0, 0.0) && !YGDoubleEqual(
                nodeWidth * pointScaleFactor % 1.0, 1.0
            )
        val hasFractionalHeight = !YGDoubleEqual(
            nodeHeight * pointScaleFactor % 1.0,
            0.0
        ) && !YGDoubleEqual(nodeHeight * pointScaleFactor % 1.0, 1.0)
        node.setLayoutDimension(
            YGRoundValueToPixelGrid(
                absoluteNodeRight, pointScaleFactor, textRounding && hasFractionalWidth,
                textRounding && !hasFractionalWidth
            ) - YGRoundValueToPixelGrid(
                absoluteNodeLeft,
                pointScaleFactor, false, textRounding
            ), YGDimension.YGDimensionWidth.getValue()
        )
        node.setLayoutDimension(
            YGRoundValueToPixelGrid(
                absoluteNodeBottom, pointScaleFactor, textRounding && hasFractionalHeight,
                textRounding && !hasFractionalHeight
            ) - YGRoundValueToPixelGrid(
                absoluteNodeTop,
                pointScaleFactor, false, textRounding
            ), YGDimension.YGDimensionHeight.getValue()
        )
        val childCount = YGNodeGetChildCount(node)
        for (i in 0 until childCount) {
            YGRoundToPixelGrid(
                YGNodeGetChild(node, i)!!,
                pointScaleFactor,
                absoluteNodeLeft,
                absoluteNodeTop
            )
        }
    }

    fun unsetUseLegacyFlagRecursively(node: YGNode) {
        if (node.getConfig() != null) {
            node.getConfig()!!.useLegacyStretchBehaviour = false
        }
        for (child in node.getChildren()) {
            unsetUseLegacyFlagRecursively(child)
        }
    }

    fun YGNodeCalculateLayoutWithContext(
        node: YGNode,
        ownerWidth: Float,
        ownerHeight: Float,
        ownerDirection: YGDirection,
        layoutContext: Any?
    ) {

        /* Event.LayoutPassStart */
        Event.publish(node, LayoutPassStartEventData(layoutContext))
        val markerData = LayoutData()
        gCurrentGenerationCount.incrementAndGet()
        node.resolveDimension()
        val width: Float
        val widthMeasureMode: YGMeasureMode
        val maxDimensions = node.getStyle()!!
            .maxDimensions()
        if (YGNodeIsStyleDimDefined(node, YGFlexDirection.YGFlexDirectionRow, ownerWidth)) {
            width = YGResolveValue(
                node.getResolvedDimension(dim[YGFlexDirection.YGFlexDirectionRow.getValue()].getValue())!!,
                ownerWidth
            ).unwrap() + node.getMarginForAxis(YGFlexDirection.YGFlexDirectionRow, ownerWidth)
                .unwrap()
            widthMeasureMode = YGMeasureMode.YGMeasureModeExactly
        } else if (!YGResolveValue(
                maxDimensions[YGDimension.YGDimensionWidth.getValue()],
                ownerWidth
            ).isUndefined()
        ) {
            width = YGResolveValue(
                maxDimensions[YGDimension.YGDimensionWidth.getValue()], ownerWidth
            ).unwrap()
            widthMeasureMode = YGMeasureMode.YGMeasureModeAtMost
        } else {
            width = ownerWidth
            widthMeasureMode = if (YGFloatIsUndefined(
                    width
                )
            ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
        }
        val height: Float
        val heightMeasureMode: YGMeasureMode
        if (YGNodeIsStyleDimDefined(node, YGFlexDirection.YGFlexDirectionColumn, ownerHeight)) {
            height = YGResolveValue(
                node.getResolvedDimension(dim[YGFlexDirection.YGFlexDirectionColumn.getValue()].getValue())!!,
                ownerHeight
            ).unwrap() + node.getMarginForAxis(YGFlexDirection.YGFlexDirectionColumn, ownerWidth)
                .unwrap()
            heightMeasureMode = YGMeasureMode.YGMeasureModeExactly
        } else if (!YGResolveValue(
                maxDimensions[YGDimension.YGDimensionHeight.getValue()],
                ownerHeight
            ).isUndefined()
        ) {
            height = YGResolveValue(
                maxDimensions[YGDimension.YGDimensionHeight.getValue()], ownerHeight
            ).unwrap()
            heightMeasureMode = YGMeasureMode.YGMeasureModeAtMost
        } else {
            height = ownerHeight
            heightMeasureMode = if (YGFloatIsUndefined(
                    height
                )
            ) YGMeasureMode.YGMeasureModeUndefined else YGMeasureMode.YGMeasureModeExactly
        }
        if (YGLayoutNodeInternal(
                node,
                width,
                height,
                ownerDirection,
                widthMeasureMode,
                heightMeasureMode,
                ownerWidth,
                ownerHeight,
                true,
                LayoutPassReason.kInitial,
                node.getConfig(),
                markerData,
                layoutContext,
                0,
                gCurrentGenerationCount.get()
            )
        ) {
            node.setPosition(node.getLayout()!!.direction(), ownerWidth, ownerHeight, ownerWidth)
            if (node.getConfig() != null) {
                YGRoundToPixelGrid(node, node.getConfig()!!.pointScaleFactor.toDouble(), 0.0, 0.0)
            }
        }

        /* Event.LayoutPassEnd */Event.publish(
            node,
            LayoutPassEndEventData(layoutContext, markerData)
        )
        if (node.getConfig() != null && node.getConfig()!!.shouldDiffLayoutWithoutLegacyStretchBehaviour && node.didUseLegacyFlag()) {
            val nodeWithoutLegacyFlag = YGNodeDeepClone(node)
            nodeWithoutLegacyFlag.resolveDimension()
            nodeWithoutLegacyFlag.markDirtyAndPropogateDownwards()
            gCurrentGenerationCount.incrementAndGet()
            unsetUseLegacyFlagRecursively(nodeWithoutLegacyFlag)
            val layoutMarkerData = LayoutData()
            if (YGLayoutNodeInternal(
                    nodeWithoutLegacyFlag, width, height, ownerDirection, widthMeasureMode,
                    heightMeasureMode, ownerWidth, ownerHeight, true, LayoutPassReason.kInitial,
                    nodeWithoutLegacyFlag.getConfig(), layoutMarkerData, layoutContext, 0,
                    gCurrentGenerationCount.get()
                )
            ) {
                nodeWithoutLegacyFlag.setPosition(
                    nodeWithoutLegacyFlag.getLayout()!!.direction(), ownerWidth,
                    ownerHeight, ownerWidth
                )
                YGRoundToPixelGrid(
                    nodeWithoutLegacyFlag,
                    nodeWithoutLegacyFlag.getConfig()!!.pointScaleFactor.toDouble(),
                    0.0,
                    0.0
                )
                val neededLegacyStretchBehaviour =
                    !nodeWithoutLegacyFlag.isLayoutTreeEqualToNode(node)
                node.setLayoutDoesLegacyFlagAffectsLayout(neededLegacyStretchBehaviour)
            }
            YGConfigFreeRecursive(nodeWithoutLegacyFlag)
            YGNodeFreeRecursive(nodeWithoutLegacyFlag)
        }
    }

    fun YGTraverseChildrenPreOrder(children: ArrayList<YGNode>, f: NodeTraverseDelegate) {
        for (node in children) {
            f.invoke(node)
            YGTraverseChildrenPreOrder(node.getChildren(), f)
        }
    }

    fun YGTraversePreOrder(node: YGNode?, f: NodeTraverseDelegate) {
        if (node == null) {
            return
        }
        f.invoke(node)
        YGTraverseChildrenPreOrder(node.getChildren(), f)
    }

    fun plus(lhs: YGFloatOptional, rhs: YGFloatOptional): YGFloatOptional {
        return YGFloatOptional(lhs.unwrap() + rhs.unwrap())
    }

    fun greaterThan(lhs: YGFloatOptional, rhs: YGFloatOptional): Boolean {
        return lhs.unwrap() > rhs.unwrap()
    }

    fun lessThan(lhs: YGFloatOptional, rhs: YGFloatOptional): Boolean {
        return lhs.unwrap() < rhs.unwrap()
    }

    fun greaterThanOrEqualTo(lhs: YGFloatOptional, rhs: YGFloatOptional): Boolean {
        return lhs.unwrap() > rhs.unwrap() || lhs.unwrap() == rhs.unwrap()
    }

    fun lessThanOrEqualTo(lhs: YGFloatOptional, rhs: YGFloatOptional): Boolean {
        return lhs.unwrap() < rhs.unwrap() || lhs.unwrap() == rhs.unwrap()
    }

    fun interface NodeTraverseDelegate {
        operator fun invoke(node: YGNode?)
    }
}