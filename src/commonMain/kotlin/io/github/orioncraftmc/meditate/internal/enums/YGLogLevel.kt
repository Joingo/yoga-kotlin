package io.github.orioncraftmc.meditate.internal.enums

enum class YGLogLevel {
    YGLogLevelError, YGLogLevelWarn, YGLogLevelInfo, YGLogLevelDebug, YGLogLevelVerbose, YGLogLevelFatal;

    fun getValue(): Int {
        return ordinal
    }

    companion object {
        const val SIZE = Integer.SIZE
        fun forValue(value: Int): YGLogLevel {
            return values()[value]
        }
    }
}