@file:JvmName("JvmClasses")
@file:JvmMultifileClass

package com.squareup.kotlinpoet.jvm.alias

import kotlin.reflect.KClass
import kotlin.jvm.kotlin as kJvmKotlin
import java.lang.reflect.Type

public actual typealias JvmType = Type

public actual fun JvmType.typeName(): String = typeName

/**
 * Typealias [JvmClass] to [Class].
 *
 * @author ForteScarlet
 */
public actual typealias JvmClass<T> = Class<T>

public actual val <T : Any> JvmClass<T>.kotlin: KClass<T>
  get() = kJvmKotlin

