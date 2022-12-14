package com.joingo.yoga.internal.enums

enum class YGNodeType {
    YGNodeTypeDefault, YGNodeTypeText;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        fun forValue(value: Int): YGNodeType {
            return values()[value]
        }
    }
}