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
import com.joingo.yoga.internal.GlobalMembers

class GlobalMembers {
    private fun notEqualsTo(a: CompactValue, b: CompactValue): Boolean {
        return !CompactValue.Companion.equalsTo(a, b)
    }

    companion object {
        fun vlog(
            config: YGConfig?,
            node: YGNode?,
            level: YGLogLevel,
            context: Any?,
            format: String,
            vararg args: Any?
        ) {
            val logConfig = config ?: GlobalMembers.YGConfigGetDefault()
            logConfig.log(logConfig, node, level, context, format, *args)
        }

        fun log2ceilFn(n: Int): Int {
            return if (n < 1) 0 else 1 + log2ceilFn(n / 2)
        }

        fun mask(bitWidth: Int, index: Int): Int {
            return (1 shl bitWidth) - 1 shl index
        }

        fun <E : Enum<E>?> bitWidthFn(e: Class<E>): Int {
            return log2ceilFn(e.enumConstants.size - 1)
        }

        fun <E : Enum<E>?> getEnumData(e: Class<E>, flags: Map<Any?, Any>, index: Int?): E {
            return (flags[StyleEnumFlagsKey(e, index!!)] ?: Arrays.stream(e.enumConstants).findFirst()
                .get()) as E
        }

        fun <E : Enum<E>> setEnumData(
            e: Class<E>,
            flags: MutableMap<Any?, Any>,
            index: Int,
            newValue: E
        ): Int {
            flags[StyleEnumFlagsKey(e, index)] = newValue
            return 0
        }

        fun getBooleanData(flags: Map<Any?, Any>, index: Int?): Boolean {
            return (flags[index] ?: false) as Boolean
        }

        fun setBooleanData(flags: MutableMap<Any?, Any>, index: Int, value: Boolean): Int {
            flags[index] = value
            return 0
        }
    }
}