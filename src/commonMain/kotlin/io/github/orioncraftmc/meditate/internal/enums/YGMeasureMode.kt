package io.github.orioncraftmc.meditate.internal.enums

enum class YGMeasureMode {
    YGMeasureModeUndefined, YGMeasureModeExactly, YGMeasureModeAtMost;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGMeasureMode {
            return values()[value]
        }
    }
}