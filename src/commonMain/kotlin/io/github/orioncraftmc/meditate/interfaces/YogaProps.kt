/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.github.orioncraftmc.meditate.interfaces

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

interface YogaProps {
    /* Width properties */
    fun setWidth(width: Float)
    fun setWidthPercent(percent: Float)
    fun setMinWidth(minWidth: Float)
    fun setMinWidthPercent(percent: Float)
    fun setMaxWidth(maxWidth: Float)
    fun setMaxWidthPercent(percent: Float)
    fun setWidthAuto()

    /* Height properties */
    fun setHeight(height: Float)
    fun setHeightPercent(percent: Float)
    fun setMinHeight(minHeight: Float)
    fun setMinHeightPercent(percent: Float)
    fun setMaxHeight(maxHeight: Float)
    fun setMaxHeightPercent(percent: Float)
    fun setHeightAuto()

    /* Margin properties */
    fun setMargin(edge: YogaEdge, margin: Float)
    fun setMarginPercent(edge: YogaEdge, percent: Float)
    fun setMarginAuto(edge: YogaEdge)

    /* Padding properties */
    fun setPadding(edge: YogaEdge, padding: Float)
    fun setPaddingPercent(edge: YogaEdge, percent: Float)

    /* Position properties */
    fun setPositionType(positionType: YogaPositionType)
    fun setPosition(edge: YogaEdge, position: Float)
    fun setPositionPercent(edge: YogaEdge, percent: Float)

    /* Alignment properties */
    fun setAlignContent(alignContent: YogaAlign)
    fun setAlignItems(alignItems: YogaAlign)
    fun setAlignSelf(alignSelf: YogaAlign)

    /* Flex properties */
    fun setFlex(flex: Float)
    fun setFlexBasisAuto()
    fun setFlexBasisPercent(percent: Float)
    fun setFlexBasis(flexBasis: Float)
    fun setFlexDirection(direction: YogaFlexDirection)
    fun setFlexGrow(flexGrow: Float)
    fun setFlexShrink(flexShrink: Float)

    /* Other properties */
    fun setJustifyContent(justifyContent: YogaJustify)
    fun setDirection(direction: YogaDirection)
    fun setBorder(edge: YogaEdge, value: Float)
    fun setWrap(wrap: YogaWrap)
    fun setAspectRatio(aspectRatio: Float)
    fun setIsReferenceBaseline(isReferenceBaseline: Boolean)
    fun setMeasureFunction(measureFunction: YogaMeasureFunction?)
    fun setBaselineFunction(yogaBaselineFunction: YogaBaselineFunction?)

    /* Getters */
    fun getWidth(): YogaValue?
    fun getMinWidth(): YogaValue?
    fun getMaxWidth(): YogaValue?
    fun getHeight(): YogaValue?
    fun getMinHeight(): YogaValue?
    fun getMaxHeight(): YogaValue?
    fun getStyleDirection(): YogaDirection?
    fun getFlexDirection(): YogaFlexDirection?
    fun getJustifyContent(): YogaJustify?
    fun getAlignItems(): YogaAlign?
    fun getAlignSelf(): YogaAlign?
    fun getAlignContent(): YogaAlign?
    fun getPositionType(): YogaPositionType?
    fun getFlexGrow(): Float
    fun getFlexShrink(): Float
    fun getFlexBasis(): YogaValue?
    fun getAspectRatio(): Float
    fun getMargin(edge: YogaEdge): YogaValue?
    fun getPadding(edge: YogaEdge): YogaValue?
    fun getPosition(edge: YogaEdge): YogaValue?
    fun getBorder(edge: YogaEdge): Float
}