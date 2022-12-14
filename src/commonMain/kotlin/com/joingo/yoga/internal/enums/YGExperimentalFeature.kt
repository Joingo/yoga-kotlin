package com.joingo.yoga.internal.enums

enum class YGExperimentalFeature {
    YGExperimentalFeatureWebFlexBasis;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGExperimentalFeature {
            return values()[value]
        }
    }
}