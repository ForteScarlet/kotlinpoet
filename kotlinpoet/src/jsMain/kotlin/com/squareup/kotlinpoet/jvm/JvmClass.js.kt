package com.squareup.kotlinpoet.jvm

import kotlin.reflect.KClass

/**
 *
 * @author ForteScarlet
 */
public actual class JvmClass<T : Any> private constructor() {
  init {
      error("JvmClass can't be constructed in a non-JVM platform")
  }
}

public actual val <T : Any> JvmClass<T>.kotlin: KClass<T>
  get() = throw UnsupportedOperationException()
