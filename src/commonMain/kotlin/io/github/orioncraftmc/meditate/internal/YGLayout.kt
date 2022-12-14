package io.github.orioncraftmc.meditate.internal

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
        return io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.getEnumData<YGDirection>(
            YGDirection::class.java, flags, directionOffset
        )
    }

    fun setDirection(direction: YGDirection) {
        io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.setEnumData<YGDirection>(
            YGDirection::class.java, flags, directionOffset, direction
        )
    }

    fun didUseLegacyFlag(): Boolean {
        return io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            didUseLegacyFlagOffset
        )
    }

    fun setDidUseLegacyFlag(`val`: Boolean) {
        io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            didUseLegacyFlagOffset,
            `val`
        )
    }

    fun doesLegacyStretchFlagAffectsLayout(): Boolean {
        return io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            doesLegacyStretchFlagAffectsLayoutOffset
        )
    }

    fun setDoesLegacyStretchFlagAffectsLayout(`val`: Boolean) {
        io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            doesLegacyStretchFlagAffectsLayoutOffset,
            `val`
        )
    }

    fun hadOverflow(): Boolean {
        return io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            hadOverflowOffset
        )
    }

    fun setHadOverflow(hadOverflow: Boolean) {
        io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.setBooleanData(
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
            directionOffset + io.github.orioncraftmc.meditate.internal.detail.GlobalMembers.Companion.bitWidthFn<YGDirection>(
                YGDirection::class.java
            )
        private val doesLegacyStretchFlagAffectsLayoutOffset = didUseLegacyFlagOffset + 1
        private val hadOverflowOffset = doesLegacyStretchFlagAffectsLayoutOffset + 1
    }
}