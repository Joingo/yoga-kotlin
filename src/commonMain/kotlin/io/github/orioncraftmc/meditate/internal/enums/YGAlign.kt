package io.github.orioncraftmc.meditate.internal.enums

enum class YGAlign //Type originates from: YGEnums.h
{
    YGAlignAuto, YGAlignFlexStart, YGAlignCenter, YGAlignFlexEnd, YGAlignStretch, YGAlignBaseline, YGAlignSpaceBetween, YGAlignSpaceAround;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGAlign {
            return values()[value]
        }
    }
}