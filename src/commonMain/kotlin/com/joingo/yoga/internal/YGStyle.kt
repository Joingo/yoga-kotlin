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
import com.joingo.yoga.internal.detail.GlobalMembers
import com.joingo.yoga.internal.detail.Values
import java.util.Map

class YGStyle //Type originates from: YGStyle.h
{
    private val margin_ = Values<YGEdge>()
    private val position_ = Values<YGEdge>()
    private val padding_ = Values<YGEdge>()
    private val border_ = Values<YGEdge>()
    private val dimensions_ =
        Values<YGDimension>(CompactValue.Companion.ofAuto().convertToYgValue())
    private val minDimensions_ = Values<YGDimension>()
    private val maxDimensions_ = Values<YGDimension>()
    private val flags: MutableMap<Any?, Any> = HashMap(Map.of())
    private var aspectRatio_ = YGFloatOptional()
    private var flex_ = YGFloatOptional()
    private var flexGrow_ = YGFloatOptional()
    private var flexShrink_ = YGFloatOptional()
    private var flexBasis_: CompactValue = CompactValue.Companion.ofAuto()

    init {
        GlobalMembers.Companion.setEnumData<YGAlign>(
            YGAlign::class.java, flags, alignContentOffset, YGAlign.YGAlignFlexStart
        )
        GlobalMembers.Companion.setEnumData<YGAlign>(
            YGAlign::class.java, flags, alignItemsOffset, YGAlign.YGAlignStretch
        )
    }

    fun setAspectRatio(aspectRatio_: YGFloatOptional) {
        this.aspectRatio_ = aspectRatio_
    }

    fun setFlex(flex_: YGFloatOptional) {
        this.flex_ = flex_
    }

    fun setFlexGrow(flexGrow_: YGFloatOptional) {
        this.flexGrow_ = flexGrow_
    }

    fun setFlexShrink(flexShrink_: YGFloatOptional) {
        this.flexShrink_ = flexShrink_
    }

    fun setFlexBasis(flexBasis_: CompactValue) {
        this.flexBasis_ = flexBasis_
    }

    fun direction(): YGDirection {
        return GlobalMembers.Companion.getEnumData<YGDirection>(
            YGDirection::class.java, flags, directionOffset
        )
    }

    fun directionBitfieldRef(): BitfieldRef<YGDirection> {
        return BitfieldRef(this, directionOffset)
    }

    fun flexDirection(): YGFlexDirection {
        return GlobalMembers.Companion.getEnumData<YGFlexDirection>(
            YGFlexDirection::class.java, flags, flexdirectionOffset
        )
    }

    fun flexDirectionBitfieldRef(): BitfieldRef<YGFlexDirection> {
        return BitfieldRef(this, flexdirectionOffset)
    }

    fun justifyContent(): YGJustify {
        return GlobalMembers.Companion.getEnumData<YGJustify>(
            YGJustify::class.java, flags, justifyContentOffset
        )
    }

    fun justifyContentBitfieldRef(): BitfieldRef<YGJustify> {
        return BitfieldRef(this, justifyContentOffset)
    }

    fun alignContent(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class.java, flags, alignContentOffset
        )
    }

    fun alignContentBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignContentOffset)
    }

    fun alignItems(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class.java, flags, alignItemsOffset
        )
    }

    fun alignItemsBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignItemsOffset)
    }

    fun alignSelf(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class.java, flags, alignSelfOffset
        )
    }

    fun alignSelfBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignSelfOffset)
    }

    fun positionType(): YGPositionType {
        return GlobalMembers.Companion.getEnumData<YGPositionType>(
            YGPositionType::class.java, flags, positionTypeOffset
        )
    }

    fun positionTypeBitfieldRef(): BitfieldRef<YGPositionType> {
        return BitfieldRef(this, positionTypeOffset)
    }

    fun flexWrap(): YGWrap {
        return GlobalMembers.Companion.getEnumData<YGWrap>(
            YGWrap::class.java, flags, flexWrapOffset
        )
    }

    fun flexWrapBitfieldRef(): BitfieldRef<YGWrap> {
        return BitfieldRef(this, flexWrapOffset)
    }

    fun overflow(): YGOverflow {
        return GlobalMembers.Companion.getEnumData<YGOverflow>(
            YGOverflow::class.java, flags, overflowOffset
        )
    }

    fun overflowBitfieldRef(): BitfieldRef<YGOverflow> {
        return BitfieldRef(this, overflowOffset)
    }

    fun display(): YGDisplay {
        return GlobalMembers.Companion.getEnumData<YGDisplay>(
            YGDisplay::class.java, flags, displayOffset
        )
    }

    fun displayBitfieldRef(): BitfieldRef<YGDisplay> {
        return BitfieldRef(this, displayOffset)
    }

    fun flex(): YGFloatOptional {
        return flex_
    }

    fun flexGrow(): YGFloatOptional {
        return flexGrow_
    }

    fun flexShrink(): YGFloatOptional {
        return flexShrink_
    }

    fun flexBasis(): CompactValue {
        return flexBasis_
    }

    fun margin(): Values<YGEdge> {
        return margin_
    }

    fun position(): Values<YGEdge> {
        return position_
    }

    fun padding(): Values<YGEdge> {
        return padding_
    }

    fun border(): Values<YGEdge> {
        return border_
    }

    fun dimensions(): Values<YGDimension> {
        return dimensions_
    }

    fun minDimensions(): Values<YGDimension> {
        return minDimensions_
    }

    fun maxDimensions(): Values<YGDimension> {
        return maxDimensions_
    }

    fun aspectRatio(): YGFloatOptional {
        return aspectRatio_
    }

    class BitfieldRef<T : Enum<T>> //Type originates from: YGStyle.h
        (val style: YGStyle, val offset: Int) {
        fun getValue(enumClazz: Class<T>): T {
            return GlobalMembers.Companion.getEnumData<T>(enumClazz, style.flags, offset)
        }

        fun setValue(x: T): BitfieldRef<T> {
            GlobalMembers.Companion.setEnumData<T>(x.javaClass as Class<T>, style.flags, offset, x)
            return this
        }
    }

    companion object {
        private const val directionOffset = 0
        private val flexdirectionOffset: Int =
            directionOffset + GlobalMembers.Companion.bitWidthFn<YGDirection>(
                YGDirection::class.java
            )
        private val justifyContentOffset: Int =
            flexdirectionOffset + GlobalMembers.Companion.bitWidthFn<YGFlexDirection>(
                YGFlexDirection::class.java
            )
        private val alignContentOffset: Int =
            justifyContentOffset + GlobalMembers.Companion.bitWidthFn<YGJustify>(
                YGJustify::class.java
            )

        //  ~YGStyle() = default;
        private val alignItemsOffset: Int =
            alignContentOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>(
                YGAlign::class.java
            )
        private val alignSelfOffset: Int =
            alignItemsOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>(
                YGAlign::class.java
            )
        private val positionTypeOffset: Int =
            alignSelfOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>(
                YGAlign::class.java
            )
        private val flexWrapOffset: Int =
            positionTypeOffset + GlobalMembers.Companion.bitWidthFn<YGPositionType>(
                YGPositionType::class.java
            )
        private val overflowOffset: Int =
            flexWrapOffset + GlobalMembers.Companion.bitWidthFn<YGWrap>(
                YGWrap::class.java
            )
        private val displayOffset: Int =
            overflowOffset + GlobalMembers.Companion.bitWidthFn<YGOverflow>(
                YGOverflow::class.java
            )
    }
}