package com.joingo.yoga.internal.enums

enum class YGPositionType {
    YGPositionTypeStatic, YGPositionTypeRelative, YGPositionTypeAbsolute;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        fun forValue(value: Int): YGPositionType {
            return values()[value]
        }
    }
}