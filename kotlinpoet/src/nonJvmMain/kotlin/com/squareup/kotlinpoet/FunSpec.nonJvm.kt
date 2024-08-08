package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmDeclaredType
import com.squareup.kotlinpoet.jvm.alias.JvmExecutableElement
import com.squareup.kotlinpoet.jvm.alias.JvmTypes

@JvmTypeAliasKotlinPoetApi
internal actual fun doOverriding(method: JvmExecutableElement): FunSpec.Builder =
  throw UnsupportedOperationException()

@JvmTypeAliasKotlinPoetApi
internal actual fun doOverriding(
  method: JvmExecutableElement,
  enclosing: JvmDeclaredType,
  types: JvmTypes,
): FunSpec.Builder =
  throw UnsupportedOperationException()

