package io.github.orioncraftmc.meditate.internal

import org.junit.jupiter.api.Assertions

open class YogaTest {
    fun ASSERT_FLOAT_EQ(expected: Float, value: Float) {
        Assertions.assertEquals(expected, value)
    }
}