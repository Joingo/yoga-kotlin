package com.joingo.yoga.internal.enums

enum class YGAlign //Type originates from: YGEnums.h
{
    YGAlignAuto, YGAlignFlexStart, YGAlignCenter, YGAlignFlexEnd, YGAlignStretch, YGAlignBaseline, YGAlignSpaceBetween, YGAlignSpaceAround;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        fun forValue(value: Int): YGAlign {
            return values()[value]
        }
    }
}