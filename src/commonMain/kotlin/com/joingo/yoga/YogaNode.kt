/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

import com.joingo.yoga.enums.YogaEdge
import java.lang.IllegalArgumentException
import com.joingo.yoga.enums.YogaUnit
import com.joingo.yoga.enums.YogaWrap
import com.joingo.yoga.enums.YogaAlign
import com.joingo.yoga.enums.YogaDisplay
import com.joingo.yoga.enums.YogaJustify
import com.joingo.yoga.enums.YogaLogLevel
import com.joingo.yoga.enums.YogaNodeType
import com.joingo.yoga.enums.YogaOverflow
import com.joingo.yoga.enums.YogaDimension
import com.joingo.yoga.enums.YogaDirection
import com.joingo.yoga.enums.YogaLayoutType
import com.joingo.yoga.enums.YogaMeasureMode
import com.joingo.yoga.enums.YogaPositionType
import com.joingo.yoga.enums.YogaPrintOptions
import com.joingo.yoga.enums.YogaFlexDirection
import com.joingo.yoga.enums.YogaExperimentalFeature
import com.joingo.yoga.internal.enums.YGEdge
import com.joingo.yoga.internal.enums.YGUnit
import com.joingo.yoga.internal.enums.YGWrap
import com.joingo.yoga.internal.enums.YGAlign
import com.joingo.yoga.internal.enums.YGDisplay
import com.joingo.yoga.internal.enums.YGJustify
import com.joingo.yoga.internal.enums.YGLogLevel
import com.joingo.yoga.internal.enums.YGNodeType
import com.joingo.yoga.internal.enums.YGOverflow
import com.joingo.yoga.internal.enums.YGDimension
import com.joingo.yoga.internal.enums.YGDirection
import com.joingo.yoga.internal.enums.YGExperiment
import java.util.EnumSet
import com.joingo.yoga.internal.enums.YGMeasureMode
import com.joingo.yoga.internal.enums.YGPositionType
import com.joingo.yoga.internal.enums.YGPrintOptions
import com.joingo.yoga.internal.enums.YGFlexDirection
import com.joingo.yoga.internal.enums.YGExperimentalFeature
import java.util.HashMap
import com.joingo.yoga.internal.event.CallableEvent
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.jvm.JvmOverloads
import com.joingo.yoga.internal.YGNode
import com.joingo.yoga.internal.event.Event.EmptyEventData
import com.joingo.yoga.internal.event.LayoutType
import com.joingo.yoga.internal.event.LayoutData
import com.joingo.yoga.internal.YGConfig
import com.joingo.yoga.internal.detail.CompactValue
import com.joingo.yoga.internal.YGValue
import com.joingo.yoga.internal.detail.CompactValue.Payload
import java.util.Arrays
import com.joingo.yoga.internal.YGSize
import com.joingo.yoga.internal.YGNode.measure_Struct
import com.joingo.yoga.internal.YGNode.baseline_Struct
import com.joingo.yoga.internal.YGNode.print_Struct
import com.joingo.yoga.internal.interfaces.YGDirtiedFunc
import com.joingo.yoga.internal.YGStyle
import com.joingo.yoga.internal.YGLayout
import com.joingo.yoga.internal.interfaces.YGBaselineFunc
import com.joingo.yoga.internal.interfaces.BaselineWithContextFn
import com.joingo.yoga.internal.interfaces.YGPrintFunc
import com.joingo.yoga.internal.interfaces.PrintWithContextFn
import com.joingo.yoga.internal.YGFloatOptional
import com.joingo.yoga.internal.interfaces.YGMeasureFunc
import com.joingo.yoga.internal.interfaces.MeasureWithContextFn
import java.util.function.UnaryOperator
import java.util.function.BiConsumer
import com.joingo.yoga.internal.YGStyle.BitfieldRef
import com.joingo.yoga.internal.interfaces.YGLogger
import com.joingo.yoga.internal.YGConfig.logger_Struct
import com.joingo.yoga.internal.YGConfig.cloneNodeCallback_Struct
import java.lang.CloneNotSupportedException
import java.lang.RuntimeException
import com.joingo.yoga.internal.YGConfig.LogWithContextFn
import com.joingo.yoga.internal.interfaces.YGCloneNodeFunc
import com.joingo.yoga.internal.YGConfig.CloneWithContextFn
import com.joingo.yoga.internal.YGCachedMeasurement
import java.util.Objects
import java.util.concurrent.atomic.AtomicInteger
import com.joingo.yoga.internal.event.NodeAllocationEventData
import com.joingo.yoga.internal.event.NodeDeallocationEventData
import com.joingo.yoga.internal.interfaces.YGNodeCleanupFunc
import java.util.function.BiFunction
import java.util.function.Supplier
import java.lang.UnsupportedOperationException
import com.joingo.yoga.internal.event.NodeLayoutEventData
import com.joingo.yoga.internal.detail.RefObject
import com.joingo.yoga.internal.event.MeasureCallbackEndEventData
import com.joingo.yoga.internal.YGCollectFlexItemsRowValues
import com.joingo.yoga.internal.event.LayoutPassStartEventData
import com.joingo.yoga.internal.event.LayoutPassEndEventData
import com.joingo.yoga.internal.GlobalMembers.NodeTraverseDelegate
import com.joingo.yoga.interfaces.YogaMeasureFunction
import com.joingo.yoga.interfaces.YogaBaselineFunction
import com.joingo.yoga.YogaValue
import com.joingo.yoga.YogaNode
import com.joingo.yoga.interfaces.YogaProps
import java.lang.IllegalStateException
import com.joingo.yoga.YogaConstants
import com.joingo.yoga.interfaces.YogaLogger
import com.joingo.yoga.YogaNodeWrapper
import com.joingo.yoga.YogaConfig
import com.joingo.yoga.YogaConfigWrapper
import com.joingo.yoga.YogaNode.Inputs
import com.joingo.yoga.YogaMeasureOutput

abstract class YogaNode : YogaProps {
    /** The interface the [.getData] object can optionally implement.  */
    interface Inputs {
        /** Requests the data object to disable mutations of its inputs.  */
        fun freeze(node: YogaNode?, parent: YogaNode?)
    }

    abstract fun reset()
    abstract fun getChildCount(): Int
    abstract fun getChildAt(i: Int): YogaNode
    abstract fun addChildAt(child: YogaNode?, i: Int)
    abstract override fun setIsReferenceBaseline(isReferenceBaseline: Boolean)
    abstract fun isReferenceBaseline(): Boolean
    abstract fun removeChildAt(i: Int): YogaNode

    /**
     * @return the [YogaNode] that owns this [YogaNode]. The owner is used to identify
     * the YogaTree that a [YogaNode] belongs to. This method will return the parent of the
     * [YogaNode] when the [YogaNode] only belongs to one YogaTree or null when the
     * [YogaNode] is shared between two or more YogaTrees.
     */
    abstract fun getOwner(): YogaNode?

    @Deprecated("Use #getOwner() instead. This will be removed in the next version. ")
    abstract fun getParent(): YogaNode?
    abstract fun indexOf(child: YogaNode?): Int
    abstract fun calculateLayout(width: Float, height: Float)
    abstract fun hasNewLayout(): Boolean
    abstract fun dirty()
    abstract fun isDirty(): Boolean
    abstract fun copyStyle(srcNode: YogaNode?)
    abstract fun markLayoutSeen()
    abstract override fun getStyleDirection(): YogaDirection?
    abstract override fun setDirection(direction: YogaDirection)
    abstract override fun getFlexDirection(): YogaFlexDirection?
    abstract override fun setFlexDirection(flexDirection: YogaFlexDirection)
    abstract override fun getJustifyContent(): YogaJustify?
    abstract override fun setJustifyContent(justifyContent: YogaJustify)
    abstract override fun getAlignItems(): YogaAlign?
    abstract override fun setAlignItems(alignItems: YogaAlign)
    abstract override fun getAlignSelf(): YogaAlign?
    abstract override fun setAlignSelf(alignSelf: YogaAlign)
    abstract override fun getAlignContent(): YogaAlign?
    abstract override fun setAlignContent(alignContent: YogaAlign)
    abstract override fun getPositionType(): YogaPositionType?
    abstract override fun setPositionType(positionType: YogaPositionType)
    abstract fun getWrap(): YogaWrap
    abstract override fun setWrap(flexWrap: YogaWrap)
    abstract fun getOverflow(): YogaOverflow
    abstract fun setOverflow(overflow: YogaOverflow)
    abstract fun getDisplay(): YogaDisplay
    abstract fun setDisplay(display: YogaDisplay)
    abstract fun getFlex(): Float
    abstract override fun setFlex(flex: Float)
    abstract override fun getFlexGrow(): Float
    abstract override fun setFlexGrow(flexGrow: Float)
    abstract override fun getFlexShrink(): Float
    abstract override fun setFlexShrink(flexShrink: Float)
    abstract override fun getFlexBasis(): YogaValue?
    abstract override fun setFlexBasis(flexBasis: Float)
    abstract override fun setFlexBasisPercent(percent: Float)
    abstract override fun setFlexBasisAuto()
    abstract override fun getMargin(edge: YogaEdge): YogaValue?
    abstract override fun setMargin(edge: YogaEdge, margin: Float)
    abstract override fun setMarginPercent(edge: YogaEdge, percent: Float)
    abstract override fun setMarginAuto(edge: YogaEdge)
    abstract override fun getPadding(edge: YogaEdge): YogaValue?
    abstract override fun setPadding(edge: YogaEdge, padding: Float)
    abstract override fun setPaddingPercent(edge: YogaEdge, percent: Float)
    abstract override fun getBorder(edge: YogaEdge): Float
    abstract override fun setBorder(edge: YogaEdge, border: Float)
    abstract override fun getPosition(edge: YogaEdge): YogaValue?
    abstract override fun setPosition(edge: YogaEdge, position: Float)
    abstract override fun setPositionPercent(edge: YogaEdge, percent: Float)
    abstract override fun getWidth(): YogaValue?
    abstract override fun setWidth(width: Float)
    abstract override fun setWidthPercent(percent: Float)
    abstract override fun setWidthAuto()
    abstract override fun getHeight(): YogaValue?
    abstract override fun setHeight(height: Float)
    abstract override fun setHeightPercent(percent: Float)
    abstract override fun setHeightAuto()
    abstract override fun getMinWidth(): YogaValue?
    abstract override fun setMinWidth(minWidth: Float)
    abstract override fun setMinWidthPercent(percent: Float)
    abstract override fun getMinHeight(): YogaValue?
    abstract override fun setMinHeight(minHeight: Float)
    abstract override fun setMinHeightPercent(percent: Float)
    abstract override fun getMaxWidth(): YogaValue?
    abstract override fun setMaxWidth(maxWidth: Float)
    abstract override fun setMaxWidthPercent(percent: Float)
    abstract override fun getMaxHeight(): YogaValue?
    abstract override fun setMaxHeight(maxheight: Float)
    abstract override fun setMaxHeightPercent(percent: Float)
    abstract override fun getAspectRatio(): Float
    abstract override fun setAspectRatio(aspectRatio: Float)
    abstract fun getLayoutX(): Float
    abstract fun getLayoutY(): Float
    abstract fun getLayoutWidth(): Float
    abstract fun getLayoutHeight(): Float
    abstract fun getLayoutMargin(edge: YogaEdge): Float
    abstract fun getLayoutPadding(edge: YogaEdge): Float
    abstract fun getLayoutBorder(edge: YogaEdge): Float
    abstract fun getLayoutDirection(): YogaDirection
    abstract override fun setMeasureFunction(measureFunction: YogaMeasureFunction?)
    abstract override fun setBaselineFunction(baselineFunction: YogaBaselineFunction?)
    abstract fun isMeasureDefined(): Boolean
    abstract fun isBaselineDefined(): Boolean
    abstract fun setData(data: Any?)
    abstract fun getData(): Any?
    abstract fun print()
    abstract fun cloneWithoutChildren(): YogaNode
    abstract fun cloneWithChildren(): YogaNode
}