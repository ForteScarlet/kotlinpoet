package com.squareup.kotlinpoet.jvm.alias

import com.squareup.kotlinpoet.JvmTypeAliasKotlinPoetApi


/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual class JvmFile private constructor() {
  init {
    error("JvmFile can't be constructed in a non-JVM platform")
  }
  public actual fun toPath(): JvmPath = throw UnsupportedOperationException()
}
