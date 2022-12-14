package com.joingo.yoga.internal.enums

enum class YGWrap //Type originates from: YGEnums.h
{
    YGWrapNoWrap, YGWrapWrap, YGWrapWrapReverse;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGWrap {
            return values()[value]
        }
    }
}