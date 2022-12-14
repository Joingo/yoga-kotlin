/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.github.orioncraftmc.meditate

import io.github.orioncraftmc.meditate.enums.YogaEdge
import java.lang.IllegalArgumentException
import io.github.orioncraftmc.meditate.enums.YogaUnit
import io.github.orioncraftmc.meditate.enums.YogaWrap
import io.github.orioncraftmc.meditate.enums.YogaAlign
import io.github.orioncraftmc.meditate.enums.YogaDisplay
import io.github.orioncraftmc.meditate.enums.YogaJustify
import io.github.orioncraftmc.meditate.enums.YogaLogLevel
import io.github.orioncraftmc.meditate.enums.YogaNodeType
import io.github.orioncraftmc.meditate.enums.YogaOverflow
import io.github.orioncraftmc.meditate.enums.YogaDimension
import io.github.orioncraftmc.meditate.enums.YogaDirection
import io.github.orioncraftmc.meditate.enums.YogaLayoutType
import io.github.orioncraftmc.meditate.enums.YogaMeasureMode
import io.github.orioncraftmc.meditate.enums.YogaPositionType
import io.github.orioncraftmc.meditate.enums.YogaPrintOptions
import io.github.orioncraftmc.meditate.enums.YogaFlexDirection
import io.github.orioncraftmc.meditate.enums.YogaExperimentalFeature
import io.github.orioncraftmc.meditate.internal.enums.YGEdge
import io.github.orioncraftmc.meditate.internal.enums.YGUnit
import io.github.orioncraftmc.meditate.internal.enums.YGWrap
import io.github.orioncraftmc.meditate.internal.enums.YGAlign
import io.github.orioncraftmc.meditate.internal.enums.YGDisplay
import io.github.orioncraftmc.meditate.internal.enums.YGJustify
import io.github.orioncraftmc.meditate.internal.enums.YGLogLevel
import io.github.orioncraftmc.meditate.internal.enums.YGNodeType
import io.github.orioncraftmc.meditate.internal.enums.YGOverflow
import io.github.orioncraftmc.meditate.internal.enums.YGDimension
import io.github.orioncraftmc.meditate.internal.enums.YGDirection
import io.github.orioncraftmc.meditate.internal.enums.YGExperiment
import java.util.EnumSet
import io.github.orioncraftmc.meditate.internal.enums.YGMeasureMode
import io.github.orioncraftmc.meditate.internal.enums.YGPositionType
import io.github.orioncraftmc.meditate.internal.enums.YGPrintOptions
import io.github.orioncraftmc.meditate.internal.enums.YGFlexDirection
import io.github.orioncraftmc.meditate.internal.enums.YGExperimentalFeature
import java.util.HashMap
import io.github.orioncraftmc.meditate.internal.event.CallableEvent
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.jvm.JvmOverloads
import io.github.orioncraftmc.meditate.internal.YGNode
import io.github.orioncraftmc.meditate.internal.event.Event.EmptyEventData
import io.github.orioncraftmc.meditate.internal.event.LayoutType
import io.github.orioncraftmc.meditate.internal.event.LayoutData
import io.github.orioncraftmc.meditate.internal.YGConfig
import io.github.orioncraftmc.meditate.internal.detail.CompactValue
import io.github.orioncraftmc.meditate.internal.YGValue
import io.github.orioncraftmc.meditate.internal.detail.CompactValue.Payload
import java.util.Arrays
import io.github.orioncraftmc.meditate.internal.YGSize
import io.github.orioncraftmc.meditate.internal.YGNode.measure_Struct
import io.github.orioncraftmc.meditate.internal.YGNode.baseline_Struct
import io.github.orioncraftmc.meditate.internal.YGNode.print_Struct
import io.github.orioncraftmc.meditate.internal.interfaces.YGDirtiedFunc
import io.github.orioncraftmc.meditate.internal.YGStyle
import io.github.orioncraftmc.meditate.internal.YGLayout
import io.github.orioncraftmc.meditate.internal.interfaces.YGBaselineFunc
import io.github.orioncraftmc.meditate.internal.interfaces.BaselineWithContextFn
import io.github.orioncraftmc.meditate.internal.interfaces.YGPrintFunc
import io.github.orioncraftmc.meditate.internal.interfaces.PrintWithContextFn
import io.github.orioncraftmc.meditate.internal.YGFloatOptional
import io.github.orioncraftmc.meditate.internal.interfaces.YGMeasureFunc
import io.github.orioncraftmc.meditate.internal.interfaces.MeasureWithContextFn
import java.util.function.UnaryOperator
import java.util.function.BiConsumer
import io.github.orioncraftmc.meditate.internal.YGStyle.BitfieldRef
import io.github.orioncraftmc.meditate.internal.interfaces.YGLogger
import io.github.orioncraftmc.meditate.internal.YGConfig.logger_Struct
import io.github.orioncraftmc.meditate.internal.YGConfig.cloneNodeCallback_Struct
import java.lang.CloneNotSupportedException
import java.lang.RuntimeException
import io.github.orioncraftmc.meditate.internal.YGConfig.LogWithContextFn
import io.github.orioncraftmc.meditate.internal.interfaces.YGCloneNodeFunc
import io.github.orioncraftmc.meditate.internal.YGConfig.CloneWithContextFn
import io.github.orioncraftmc.meditate.internal.YGCachedMeasurement
import java.util.Objects
import java.util.concurrent.atomic.AtomicInteger
import io.github.orioncraftmc.meditate.internal.event.NodeAllocationEventData
import io.github.orioncraftmc.meditate.internal.event.NodeDeallocationEventData
import io.github.orioncraftmc.meditate.internal.interfaces.YGNodeCleanupFunc
import java.util.function.BiFunction
import java.util.function.Supplier
import java.lang.UnsupportedOperationException
import io.github.orioncraftmc.meditate.internal.event.NodeLayoutEventData
import io.github.orioncraftmc.meditate.internal.detail.RefObject
import io.github.orioncraftmc.meditate.internal.event.MeasureCallbackEndEventData
import io.github.orioncraftmc.meditate.internal.YGCollectFlexItemsRowValues
import io.github.orioncraftmc.meditate.internal.event.LayoutPassStartEventData
import io.github.orioncraftmc.meditate.internal.event.LayoutPassEndEventData
import io.github.orioncraftmc.meditate.internal.GlobalMembers.NodeTraverseDelegate
import io.github.orioncraftmc.meditate.interfaces.YogaMeasureFunction
import io.github.orioncraftmc.meditate.interfaces.YogaBaselineFunction
import io.github.orioncraftmc.meditate.YogaValue
import io.github.orioncraftmc.meditate.YogaNode
import io.github.orioncraftmc.meditate.interfaces.YogaProps
import java.lang.IllegalStateException
import io.github.orioncraftmc.meditate.YogaConstants
import io.github.orioncraftmc.meditate.interfaces.YogaLogger
import io.github.orioncraftmc.meditate.YogaNodeWrapper
import io.github.orioncraftmc.meditate.YogaConfig
import io.github.orioncraftmc.meditate.YogaConfigWrapper
import io.github.orioncraftmc.meditate.YogaNode.Inputs
import io.github.orioncraftmc.meditate.YogaMeasureOutput

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