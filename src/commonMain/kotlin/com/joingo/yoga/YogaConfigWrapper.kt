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
import com.joingo.yoga.internal.event.Event.EmptyEventData
import com.joingo.yoga.internal.event.LayoutType
import com.joingo.yoga.internal.event.LayoutData
import com.joingo.yoga.internal.detail.CompactValue
import com.joingo.yoga.internal.detail.CompactValue.Payload
import java.util.Arrays
import com.joingo.yoga.internal.YGNode.measure_Struct
import com.joingo.yoga.internal.YGNode.baseline_Struct
import com.joingo.yoga.internal.YGNode.print_Struct
import com.joingo.yoga.internal.interfaces.YGDirtiedFunc
import com.joingo.yoga.internal.interfaces.YGBaselineFunc
import com.joingo.yoga.internal.interfaces.BaselineWithContextFn
import com.joingo.yoga.internal.interfaces.YGPrintFunc
import com.joingo.yoga.internal.interfaces.PrintWithContextFn
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
import com.joingo.yoga.internal.*

class YogaConfigWrapper private constructor(var mNativePointer: YGConfig) : com.joingo.yoga.YogaConfig() {
    private var mLogger: YogaLogger? = null

    internal constructor() : this(GlobalMembers.YGConfigNew()) {}

    override fun setExperimentalFeatureEnabled(feature: YogaExperimentalFeature, enabled: Boolean) {
        GlobalMembers.YGConfigSetExperimentalFeatureEnabled(
            mNativePointer,
            YGExperimentalFeature.Companion.forValue(feature.intValue()), enabled
        )
    }

    override fun setUseWebDefaults(useWebDefaults: Boolean) {
        GlobalMembers.YGConfigSetUseWebDefaults(mNativePointer, useWebDefaults)
    }

    override fun setPrintTreeFlag(enable: Boolean) {
        GlobalMembers.YGConfigSetPrintTreeFlag(mNativePointer, enable)
    }

    override fun setPointScaleFactor(pixelsInPoint: Float) {
        GlobalMembers.YGConfigSetPointScaleFactor(mNativePointer, pixelsInPoint)
    }

    /**
     * Yoga previously had an error where containers would take the maximum space possible instead of the minimum
     * like they are supposed to. In practice this resulted in implicit behaviour similar to align-self: stretch;
     * Because this was such a long-standing bug we must allow legacy users to switch back to this behaviour.
     */
    override fun setUseLegacyStretchBehaviour(useLegacyStretchBehaviour: Boolean) {
        GlobalMembers.YGConfigSetUseLegacyStretchBehaviour(
            mNativePointer,
            useLegacyStretchBehaviour
        )
    }

    /**
     * If this flag is set then yoga would diff the layout without legacy flag and would set a bool in
     * YogaNode(mDoesLegacyStretchFlagAffectsLayout) with true if the layouts were different and false
     * if not
     */
    override fun setShouldDiffLayoutWithoutLegacyStretchBehaviour(
        shouldDiffLayoutWithoutLegacyStretchBehaviour: Boolean
    ) {
        GlobalMembers.YGConfigSetShouldDiffLayoutWithoutLegacyStretchBehaviour(
            mNativePointer, shouldDiffLayoutWithoutLegacyStretchBehaviour
        )
    }

    override fun setLogger(logger: YogaLogger?) {
        mLogger = logger
        GlobalMembers.YGConfigSetLogger(mNativePointer, logger)
    }

    override fun getLogger(): YogaLogger? {
        return mLogger
    }

    public override fun getNativePointer(): YGConfig {
        return mNativePointer
    }
}