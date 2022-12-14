package com.joingo.yoga.internal.enums

enum class YGUnit //Type originates from: YGEnums.h
{
    YGUnitUndefined, YGUnitPoint, YGUnitPercent, YGUnitAuto;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        fun forValue(value: Int): YGUnit {
            return values()[value]
        }
    }
}