package com.squareup.kotlinpoet

import kotlin.reflect.KClass

internal actual fun KClass<*>.qualifiedNameInternal(): String? {
  return qualifiedNameInternalNonJvm() ?: simpleName
}
