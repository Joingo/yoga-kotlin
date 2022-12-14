package com.joingo.yoga.internal.enums

enum class YGJustify {
    YGJustifyFlexStart, YGJustifyCenter, YGJustifyFlexEnd, YGJustifySpaceBetween, YGJustifySpaceAround, YGJustifySpaceEvenly;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        
        fun forValue(value: Int): YGJustify {
            return values()[value]
        }
    }
}