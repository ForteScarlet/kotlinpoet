package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmClass
import com.squareup.kotlinpoet.jvm.alias.JvmTypeElement
import com.squareup.kotlinpoet.jvm.alias.kotlin
import kotlin.reflect.KClass

@JvmTypeAliasKotlinPoetApi
@DelicateKotlinPoetApi(
  message = "Java reflection APIs don't give complete information on Kotlin types. Consider using" +
    " the kotlinpoet-metadata APIs instead.",
)
public actual fun JvmClass<*>.asClassName(): ClassName =
  kotlin.asClassName()

@JvmTypeAliasKotlinPoetApi
@DelicateKotlinPoetApi(
  message = "Java reflection APIs don't give complete information on Kotlin types. Consider using" +
    " the kotlinpoet-metadata APIs instead.",
)
public actual fun JvmTypeElement.asClassName(): ClassName =
  throw UnsupportedOperationException()

internal actual fun Enum<*>.declaringClassName(): ClassName =
  this::class.asClassName()

internal fun KClass<*>.qualifiedNameInternalNonJvm(): String? {
  return when (this) {
    Boolean::class -> "kotlin.Boolean"
    Byte::class -> "kotlin.Byte"
    Char::class -> "kotlin.Char"
    Double::class -> "kotlin.Double"
    Enum::class -> "kotlin.Enum"
    Float::class -> "kotlin.Float"
    Int::class -> "kotlin.Int"
    Long::class -> "kotlin.Long"
    Short::class -> "kotlin.Short"
    String::class -> "kotlin.String"
    Boolean.Companion::class -> "kotlin.Boolean.Companion"
    Byte.Companion::class -> "kotlin.Byte.Companion"
    Char.Companion::class -> "kotlin.Char.Companion"
    Double.Companion::class -> "kotlin.Double.Companion"
    Enum.Companion::class -> "kotlin.Enum.Companion"
    Float.Companion::class -> "kotlin.Float.Companion"
    Int.Companion::class -> "kotlin.Int.Companion"
    Long.Companion::class -> "kotlin.Long.Companion"
    Short.Companion::class -> "kotlin.Short.Companion"
    String.Companion::class -> "kotlin.String.Companion"
    else -> null
  }
}
