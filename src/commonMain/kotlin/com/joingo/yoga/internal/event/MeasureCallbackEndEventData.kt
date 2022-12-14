package com.joingo.yoga.internal.event

import com.joingo.yoga.internal.enums.YGMeasureMode

class MeasureCallbackEndEventData(
    val layoutContext: Any?,
    val width: Float,
    val widthMeasureMode: YGMeasureMode,
    val height: Float,
    val heightMeasureMode: YGMeasureMode,
    val measuredWidth: Float,
    val measuredHeight: Float,
    val reason: LayoutPassReason
) : CallableEvent() //Type originates from: event.h
