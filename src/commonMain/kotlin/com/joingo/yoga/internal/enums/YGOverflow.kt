package com.joingo.yoga.internal.enums

enum class YGOverflow {
    YGOverflowVisible, YGOverflowHidden, YGOverflowScroll;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGOverflow {
            return values()[value]
        }
    }
}