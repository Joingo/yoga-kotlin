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
import io.github.orioncraftmc.meditate.internal.detail.GlobalMembers
import io.github.orioncraftmc.meditate.internal.detail.Values
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