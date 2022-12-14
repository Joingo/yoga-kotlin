package com.joingo.yoga.internal.enums

enum class YGFlexDirection {
    YGFlexDirectionColumn, YGFlexDirectionColumnReverse, YGFlexDirectionRow, YGFlexDirectionRowReverse;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGFlexDirection {
            return values()[value]
        }
    }
}