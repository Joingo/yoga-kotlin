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

class YGConfig(logger: YGLogger?) : Cloneable //Type originates from: YGConfig.h
{
    private val logger_struct = logger_Struct()
    var useWebDefaults = false
    var useLegacyStretchBehaviour = false
    var shouldDiffLayoutWithoutLegacyStretchBehaviour = false
    var printTree = false
    var pointScaleFactor = 1.0f
    val experimentalFeatures = ArrayList<Boolean>()
    var context: Any? = null
    private var cloneNodeCallback_struct: cloneNodeCallback_Struct? = cloneNodeCallback_Struct()
    private var cloneNodeUsesContext_ = false
    private var loggerUsesContext_: Boolean

    init  //Method definition originates from: YGConfig.cpp
    {
        cloneNodeCallback_struct = null
        logger_struct.noContext = logger
        loggerUsesContext_ = false
        for (i in YGExperiment.values().indices) {
            experimentalFeatures.add(false)
        }
    }

    public override fun clone(): YGConfig {
        return try {
            super.clone() as YGConfig
        } catch (e: CloneNotSupportedException) {
            throw RuntimeException(e)
        }
    }

    fun log(
        config: YGConfig?,
        node: YGNode?,
        logLevel: YGLogLevel,
        logContext: Any?,
        format: String,
        vararg args: Any?
    ) //Method definition originates from: YGConfig.cpp
    {
        if (loggerUsesContext_) {
            logger_struct.withContext!!.invoke(config, node, logLevel, logContext, format, *args)
        } else {
            logger_struct.noContext!!.invoke(config, node, logLevel, format, *args)
        }
    }

    fun setLogger(logger: YGLogger?) {
        logger_struct.noContext = logger
        loggerUsesContext_ = false
    }

    fun setLogger(logger: LogWithContextFn?) {
        logger_struct.withContext = logger
        loggerUsesContext_ = true
    }

    fun setLogger() {
        logger_struct.noContext = null
        loggerUsesContext_ = false
    }

    fun cloneNode(
        node: YGNode,
        owner: YGNode?,
        childIndex: Int,
        cloneContext: Any?
    ): YGNode //Method definition originates from: YGConfig.cpp
    {
        var clone: YGNode? = null
        if (cloneNodeCallback_struct!!.noContext != null) {
            clone = if (cloneNodeUsesContext_) cloneNodeCallback_struct!!.withContext!!.invoke(
                node, owner, childIndex,
                cloneContext
            ) else cloneNodeCallback_struct!!.noContext!!.invoke(node, owner, childIndex)
        }
        if (clone == null) {
            clone = GlobalMembers.YGNodeClone(node)
        }
        return clone
    }

    fun setCloneNodeCallback(cloneNode: YGCloneNodeFunc?) {
        cloneNodeCallback_struct!!.noContext = cloneNode
        cloneNodeUsesContext_ = false
    }

    fun setCloneNodeCallback(cloneNode: CloneWithContextFn?) {
        cloneNodeCallback_struct!!.withContext = cloneNode
        cloneNodeUsesContext_ = true
    }

    fun setCloneNodeCallback() {
        cloneNodeCallback_struct!!.noContext = null
        cloneNodeUsesContext_ = false
    }

    fun interface LogWithContextFn {
        operator fun invoke(
            config: YGConfig?,
            node: YGNode?,
            level: YGLogLevel?,
            context: Any?,
            format: String?,
            vararg args: Any?
        ): Int
    }

    fun interface CloneWithContextFn {
        operator fun invoke(
            node: YGNode?,
            owner: YGNode?,
            childIndex: Int,
            cloneContext: Any?
        ): YGNode
    }

    internal class cloneNodeCallback_Struct {
        var withContext: CloneWithContextFn? = null
        var noContext: YGCloneNodeFunc? = null
    }

    internal class logger_Struct {
        var withContext: LogWithContextFn? = null
        var noContext: YGLogger? = null
    }
}