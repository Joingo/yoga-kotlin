package com.joingo.yoga.internal.detail

import kotlin.reflect.KClass

data class StyleEnumFlagsKey(
    val enumClazz: KClass<*>,
    val index: Int
)
