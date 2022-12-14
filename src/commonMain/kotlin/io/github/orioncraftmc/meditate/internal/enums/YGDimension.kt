package io.github.orioncraftmc.meditate.internal.enums

enum class YGDimension {
    YGDimensionWidth, YGDimensionHeight;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGDimension {
            return values()[value]
        }
    }
}