package com.joingo.yoga.internal.event

import com.joingo.yoga.internal.YGNode
import kotlin.reflect.KClass

object Event //Type originates from: event.h
{
    private val listeners = HashMap<KClass<*>, MutableList<(CallableEvent) -> Unit>>()
    fun reset() //Method definition originates from: event.cpp
    {
        listeners.clear()
    }

    fun <T : CallableEvent> subscribe(
        clazz: KClass<T>,
        listener: (T) -> Unit
    ) //Method definition originates from: event.cpp
    {
        listeners.getOrPut(clazz) { mutableListOf() }
            .add(
                listener as (CallableEvent) -> Unit
            )
    }

    fun publish(node: YGNode?, eventData: CallableEvent = EmptyEventData()) {
        val listeners = listeners[eventData::class]
        if (listeners != null) {
            for (listener in listeners) {
                listener(eventData)
            }
        }
    }

    private fun publish(
        node: YGNode,
        eventType: Type,
        eventData: CallableEvent
    ) //Method definition originates from: event.cpp
    {
        publish(node, eventData)
    }

    enum class Type //Type originates from: event.h
    {
        NodeAllocation, NodeDeallocation, NodeLayout, LayoutPassStart, LayoutPassEnd, MeasureCallbackStart, MeasureCallbackEnd, NodeBaselineStart, NodeBaselineEnd;

        fun getValue(): Int {
            return ordinal
        }

        companion object {

            fun forValue(value: Int): Type {
                return values()[value]
            }
        }
    }

    class EmptyEventData : CallableEvent() //Type originates from: event.h
}