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