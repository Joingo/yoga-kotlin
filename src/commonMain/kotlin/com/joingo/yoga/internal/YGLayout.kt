package com.joingo.yoga.internal

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
import java.util.ArrayList
import java.util.List

class YGLayout {
    private val flags: MutableMap<Any?, Any> = HashMap()
    val position = createEmptyFloatArray()
    val dimensions = ArrayList(Arrays.asList(GlobalMembers.YGUndefined, GlobalMembers.YGUndefined))
    val margin = createEmptyFloatArray()
    val border = createEmptyFloatArray()
    val padding = createEmptyFloatArray()
    var computedFlexBasisGeneration = 0
    var computedFlexBasis = YGFloatOptional()
    var generationCount = 0
    var lastOwnerDirection = YGDirection.YGDirectionInherit
    var nextCachedMeasurementsIndex = 0
    val cachedMeasurements = ArrayList<YGCachedMeasurement>(YG_MAX_CACHED_RESULT_COUNT)
    val measuredDimensions =
        ArrayList(Arrays.asList(GlobalMembers.YGUndefined, GlobalMembers.YGUndefined))
    val cachedLayout = YGCachedMeasurement()

    init {
        for (i in 0 until YG_MAX_CACHED_RESULT_COUNT) {
            cachedMeasurements.add(YGCachedMeasurement())
        }
    }

    private fun createEmptyFloatArray(): ArrayList<Float> {
        return ArrayList(List.of(0f, 0f, 0f, 0f))
    }

    fun direction(): YGDirection {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getEnumData<YGDirection>(
            YGDirection::class.java, flags, directionOffset
        )
    }

    fun setDirection(direction: YGDirection) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setEnumData<YGDirection>(
            YGDirection::class.java, flags, directionOffset, direction
        )
    }

    fun didUseLegacyFlag(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            didUseLegacyFlagOffset
        )
    }

    fun setDidUseLegacyFlag(`val`: Boolean) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            didUseLegacyFlagOffset,
            `val`
        )
    }

    fun doesLegacyStretchFlagAffectsLayout(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            doesLegacyStretchFlagAffectsLayoutOffset
        )
    }

    fun setDoesLegacyStretchFlagAffectsLayout(`val`: Boolean) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            doesLegacyStretchFlagAffectsLayoutOffset,
            `val`
        )
    }

    fun hadOverflow(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            hadOverflowOffset
        )
    }

    fun setHadOverflow(hadOverflow: Boolean) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            hadOverflowOffset,
            hadOverflow
        )
    }

    fun equalsTo(layout: YGLayout): Boolean //Method definition originates from: YGLayout.cpp
    {
        var isEqual = GlobalMembers.YGFloatArrayEqual(
            position,
            layout.position
        ) && GlobalMembers.YGFloatArrayEqual(
            dimensions,
            layout.dimensions
        ) && GlobalMembers.YGFloatArrayEqual(
            margin,
            layout.margin
        ) && GlobalMembers.YGFloatArrayEqual(
            border,
            layout.border
        ) && GlobalMembers.YGFloatArrayEqual(
            padding,
            layout.padding
        ) && direction() == layout.direction() && hadOverflow() == layout.hadOverflow() && lastOwnerDirection == layout.lastOwnerDirection && nextCachedMeasurementsIndex == layout.nextCachedMeasurementsIndex && cachedLayout.equalsTo(
            layout.cachedLayout
        ) && computedFlexBasis === layout.computedFlexBasis
        var i = 0
        while (i < YG_MAX_CACHED_RESULT_COUNT && isEqual) {
            //TODO: Verify if this is correct
            isEqual = cachedMeasurements[i].equalsTo(layout.cachedMeasurements[i])
            ++i
        }
        if (!GlobalMembers.isUndefined(measuredDimensions[0]) || !GlobalMembers.isUndefined(
                layout.measuredDimensions[0]
            )
        ) {
            isEqual = isEqual && measuredDimensions[0] == layout.measuredDimensions[0]
        }
        if (!GlobalMembers.isUndefined(measuredDimensions[1]) || !GlobalMembers.isUndefined(
                layout.measuredDimensions[1]
            )
        ) {
            isEqual = isEqual && measuredDimensions[1] == layout.measuredDimensions[1]
        }
        return isEqual
    }

    fun notEqualsTo(layout: YGLayout): Boolean {
        return !equalsTo(layout)
    }

    companion object {
        // This value was chosen based on empirical data:
        // 98% of analyzed layouts require less than 8 entries.
        const val YG_MAX_CACHED_RESULT_COUNT = 8
        private const val directionOffset = 0
        private val didUseLegacyFlagOffset: Int =
            directionOffset + com.joingo.yoga.internal.detail.GlobalMembers.Companion.bitWidthFn<YGDirection>(
                YGDirection::class.java
            )
        private val doesLegacyStretchFlagAffectsLayoutOffset = didUseLegacyFlagOffset + 1
        private val hadOverflowOffset = doesLegacyStretchFlagAffectsLayoutOffset + 1
    }
}