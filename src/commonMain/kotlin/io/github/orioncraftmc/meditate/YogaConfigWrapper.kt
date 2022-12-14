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
import io.github.orioncraftmc.meditate.internal.event.Event.EmptyEventData
import io.github.orioncraftmc.meditate.internal.event.LayoutType
import io.github.orioncraftmc.meditate.internal.event.LayoutData
import io.github.orioncraftmc.meditate.internal.detail.CompactValue
import io.github.orioncraftmc.meditate.internal.detail.CompactValue.Payload
import java.util.Arrays
import io.github.orioncraftmc.meditate.internal.YGNode.measure_Struct
import io.github.orioncraftmc.meditate.internal.YGNode.baseline_Struct
import io.github.orioncraftmc.meditate.internal.YGNode.print_Struct
import io.github.orioncraftmc.meditate.internal.interfaces.YGDirtiedFunc
import io.github.orioncraftmc.meditate.internal.interfaces.YGBaselineFunc
import io.github.orioncraftmc.meditate.internal.interfaces.BaselineWithContextFn
import io.github.orioncraftmc.meditate.internal.interfaces.YGPrintFunc
import io.github.orioncraftmc.meditate.internal.interfaces.PrintWithContextFn
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
import io.github.orioncraftmc.meditate.internal.*

class YogaConfigWrapper private constructor(var mNativePointer: YGConfig) : YogaConfig() {
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