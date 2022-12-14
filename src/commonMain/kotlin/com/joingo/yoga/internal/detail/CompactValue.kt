package com.joingo.yoga.internal.detail

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

//static_assert(std::numeric_limits<float>::is_iec559, "facebook::yoga::detail::CompactValue only works with IEEE754 floats");
class CompactValue //Type originates from: CompactValue.h
{
    private val payload_: Payload
    private var undefined = false

    constructor() {
        undefined = true
        payload_ = Payload(Float.NaN, YGUnit.YGUnitUndefined)
    }

    private constructor(data: Payload) {
        payload_ = data
    }

    fun convertToYgValue(): YGValue {
        return YGValue(payload_.value, payload_.unit)
    }

    fun isUndefined(): Boolean {
        return undefined || !isAuto() && !isPoint() && !isPercent() && java.lang.Float.isNaN(
            payload_.value
        )
    }

    private fun isPercent(): Boolean {
        return payload_.unit == YGUnit.YGUnitPercent
    }

    private fun isPoint(): Boolean {
        return payload_.unit == YGUnit.YGUnitPoint
    }

    fun isAuto(): Boolean {
        return payload_.unit == YGUnit.YGUnitAuto
    }

    private fun repr(): YGUnit {
        return payload_.unit
    }

    internal class Payload //Type originates from: CompactValue.h
        (val value: Float, val unit: YGUnit)

    companion object {
        const val LOWER_BOUND = 1.08420217e-19f
        const val UPPER_BOUND_POINT = 36893485948395847680.0f
        const val UPPER_BOUND_PERCENT = 18446742974197923840.0f
        fun of(value: Float, Unit: YGUnit): CompactValue {
            var value = value
            if (value < LOWER_BOUND && value > -LOWER_BOUND) {
                return CompactValue(Payload(0f, Unit))
            }
            val upperBound =
                if (Unit == YGUnit.YGUnitPercent) UPPER_BOUND_PERCENT else UPPER_BOUND_POINT
            if (value > upperBound || value < -upperBound) {
                value = Math.copySign(upperBound, value)
            }
            val data = Payload(value, Unit)
            return CompactValue(data)
        }

        fun ofMaybe(value: Float, Unit: YGUnit): CompactValue {
            return if (java.lang.Float.isNaN(value) || java.lang.Float.isInfinite(value)) ofUndefined() else of(
                value,
                Unit
            )
        }

        fun ofZero(): CompactValue {
            return CompactValue(Payload(0f, YGUnit.YGUnitPoint))
        }

        fun ofUndefined(): CompactValue {
            return CompactValue()
        }

        fun ofAuto(): CompactValue {
            return CompactValue(Payload(0f, YGUnit.YGUnitAuto))
        }

        fun createCompactValue(x: YGValue): CompactValue {
            val compactValue = when (x.unit) {
                YGUnit.YGUnitUndefined -> ofUndefined()
                YGUnit.YGUnitAuto -> ofAuto()
                YGUnit.YGUnitPoint -> of(x.value, YGUnit.YGUnitPoint)
                YGUnit.YGUnitPercent -> of(x.value, YGUnit.YGUnitPercent)
            }
            return compactValue
        }

        fun equalsTo(
            a: CompactValue,
            b: CompactValue
        ): Boolean //Method definition originates from: CompactValue.h
        {
            return a.payload_.unit == b.payload_.unit
        }
    }
}