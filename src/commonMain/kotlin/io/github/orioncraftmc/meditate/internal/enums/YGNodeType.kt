package io.github.orioncraftmc.meditate.internal.enums

enum class YGNodeType {
    YGNodeTypeDefault, YGNodeTypeText;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGNodeType {
            return values()[value]
        }
    }
}