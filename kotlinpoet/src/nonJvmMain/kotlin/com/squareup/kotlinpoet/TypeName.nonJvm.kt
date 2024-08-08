package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmType
import com.squareup.kotlinpoet.jvm.alias.JvmTypeMirror

/** Returns a [TypeName] equivalent to this [JvmType].  */
@JvmTypeAliasKotlinPoetApi
public actual fun JvmType.asTypeName(): TypeName =
  throw UnsupportedOperationException()

/** Returns a [TypeName] equivalent to this [JvmTypeMirror]. */
@DelicateKotlinPoetApi(
  message = "Mirror APIs don't give complete information on Kotlin types. Consider using" +
    " the kotlinpoet-metadata APIs instead.",
)
@JvmTypeAliasKotlinPoetApi
public actual fun JvmTypeMirror.asTypeName(): TypeName =
  throw UnsupportedOperationException()
