package com.joingo.yoga.internal.enums

enum class YGDirection {
    YGDirectionInherit, YGDirectionLTR, YGDirectionRTL;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        fun forValue(value: Int): YGDirection {
            return values()[value]
        }
    }
}