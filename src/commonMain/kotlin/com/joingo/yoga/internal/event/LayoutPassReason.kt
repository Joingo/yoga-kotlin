package com.joingo.yoga.internal.event

enum class LayoutPassReason //Type originates from: event.h
    (private val intValue: Int) {
    kInitial(0), kAbsLayout(1), kStretch(2), kMultilineStretch(3), kFlexLayout(4), kMeasureChild(5), kAbsMeasureChild(
        6
    ),
    kFlexMeasure(7), COUNT(8);

    fun getValue(): Int {
        return intValue
    }

    companion object {
        const val SIZE = Integer.SIZE
        private var mappings: Map<Int, LayoutPassReason>? =
            values().associate { it.intValue to it }

        private fun getMappings(): Map<Int, LayoutPassReason>? {
            return mappings
        }

        fun forValue(value: Int): LayoutPassReason? {
            return getMappings()!![value]
        }
    }
}