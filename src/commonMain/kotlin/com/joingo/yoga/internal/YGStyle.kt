package com.joingo.yoga.internal

import com.joingo.yoga.internal.detail.CompactValue
import com.joingo.yoga.internal.detail.GlobalMembers
import com.joingo.yoga.internal.detail.Values
import com.joingo.yoga.internal.enums.*
import kotlin.reflect.KClass

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
    private val flags: MutableMap<Any?, Any> = hashMapOf()
    private var aspectRatio_ = YGFloatOptional()
    private var flex_ = YGFloatOptional()
    private var flexGrow_ = YGFloatOptional()
    private var flexShrink_ = YGFloatOptional()
    private var flexBasis_: CompactValue = CompactValue.Companion.ofAuto()

    init {
        GlobalMembers.Companion.setEnumData<YGAlign>(
            YGAlign::class, flags, alignContentOffset, YGAlign.YGAlignFlexStart
        )
        GlobalMembers.Companion.setEnumData<YGAlign>(
            YGAlign::class, flags, alignItemsOffset, YGAlign.YGAlignStretch
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
            YGDirection::class, flags, directionOffset
        )
    }

    fun directionBitfieldRef(): BitfieldRef<YGDirection> {
        return BitfieldRef(this, directionOffset)
    }

    fun flexDirection(): YGFlexDirection {
        return GlobalMembers.Companion.getEnumData<YGFlexDirection>(
            YGFlexDirection::class, flags, flexdirectionOffset
        )
    }

    fun flexDirectionBitfieldRef(): BitfieldRef<YGFlexDirection> {
        return BitfieldRef(this, flexdirectionOffset)
    }

    fun justifyContent(): YGJustify {
        return GlobalMembers.Companion.getEnumData<YGJustify>(
            YGJustify::class, flags, justifyContentOffset
        )
    }

    fun justifyContentBitfieldRef(): BitfieldRef<YGJustify> {
        return BitfieldRef(this, justifyContentOffset)
    }

    fun alignContent(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class, flags, alignContentOffset
        )
    }

    fun alignContentBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignContentOffset)
    }

    fun alignItems(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class, flags, alignItemsOffset
        )
    }

    fun alignItemsBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignItemsOffset)
    }

    fun alignSelf(): YGAlign {
        return GlobalMembers.Companion.getEnumData<YGAlign>(
            YGAlign::class, flags, alignSelfOffset
        )
    }

    fun alignSelfBitfieldRef(): BitfieldRef<YGAlign> {
        return BitfieldRef(this, alignSelfOffset)
    }

    fun positionType(): YGPositionType {
        return GlobalMembers.Companion.getEnumData<YGPositionType>(
            YGPositionType::class, flags, positionTypeOffset
        )
    }

    fun positionTypeBitfieldRef(): BitfieldRef<YGPositionType> {
        return BitfieldRef(this, positionTypeOffset)
    }

    fun flexWrap(): YGWrap {
        return GlobalMembers.Companion.getEnumData<YGWrap>(
            YGWrap::class, flags, flexWrapOffset
        )
    }

    fun flexWrapBitfieldRef(): BitfieldRef<YGWrap> {
        return BitfieldRef(this, flexWrapOffset)
    }

    fun overflow(): YGOverflow {
        return GlobalMembers.Companion.getEnumData<YGOverflow>(
            YGOverflow::class, flags, overflowOffset
        )
    }

    fun overflowBitfieldRef(): BitfieldRef<YGOverflow> {
        return BitfieldRef(this, overflowOffset)
    }

    fun display(): YGDisplay {
        return GlobalMembers.Companion.getEnumData<YGDisplay>(
            YGDisplay::class, flags, displayOffset
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

    //Type originates from: YGStyle.h
    class BitfieldRef<T : Enum<T>>(
        val style: YGStyle,
        val offset: Int,
        private val enumValues: Array<T>
    ) {
        fun getValue(enumClazz: KClass<T>): T {
            return GlobalMembers.Companion.getEnumData(enumClazz, enumValues, style.flags, offset)
        }

        fun setValue(x: T): BitfieldRef<T> {
            GlobalMembers.Companion.setEnumData<T>(x::class, style.flags, offset, x)
            return this
        }
    }

    inline fun <reified T : Enum<T>> BitfieldRef(style: YGStyle, offset: Int): BitfieldRef<T> =
        BitfieldRef<T>(style, offset, enumValues())

    companion object {
        private const val directionOffset = 0
        private val flexdirectionOffset: Int =
            directionOffset + GlobalMembers.Companion.bitWidthFn<YGDirection>()
        private val justifyContentOffset: Int =
            flexdirectionOffset + GlobalMembers.Companion.bitWidthFn<YGFlexDirection>()
        private val alignContentOffset: Int =
            justifyContentOffset + GlobalMembers.Companion.bitWidthFn<YGJustify>()

        //  ~YGStyle() = default;
        private val alignItemsOffset: Int =
            alignContentOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>()
        private val alignSelfOffset: Int =
            alignItemsOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>()
        private val positionTypeOffset: Int =
            alignSelfOffset + GlobalMembers.Companion.bitWidthFn<YGAlign>()
        private val flexWrapOffset: Int =
            positionTypeOffset + GlobalMembers.Companion.bitWidthFn<YGPositionType>()
        private val overflowOffset: Int =
            flexWrapOffset + GlobalMembers.Companion.bitWidthFn<YGWrap>()
        private val displayOffset: Int =
            overflowOffset + GlobalMembers.Companion.bitWidthFn<YGOverflow>()
    }
}