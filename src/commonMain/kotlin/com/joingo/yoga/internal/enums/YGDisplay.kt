package com.joingo.yoga.internal.enums

enum class YGDisplay {
    YGDisplayFlex, YGDisplayNone;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGDisplay {
            return values()[value]
        }
    }
}