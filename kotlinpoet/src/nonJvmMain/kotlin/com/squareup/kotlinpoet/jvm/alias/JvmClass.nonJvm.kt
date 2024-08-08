package com.squareup.kotlinpoet.jvm.alias

import com.squareup.kotlinpoet.JvmTypeAliasKotlinPoetApi
import kotlin.reflect.KClass

@JvmTypeAliasKotlinPoetApi
public actual interface JvmType

@JvmTypeAliasKotlinPoetApi
public actual fun JvmType.typeName(): String =
  throw UnsupportedOperationException()

/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual class JvmClass<T : Any> private constructor() : JvmType {
  init {
    error("JvmClass can't be constructed in a non-JVM platform")
  }
}

@JvmTypeAliasKotlinPoetApi
public actual val <T : Any> JvmClass<T>.kotlin: KClass<T>
  get() = throw UnsupportedOperationException()
