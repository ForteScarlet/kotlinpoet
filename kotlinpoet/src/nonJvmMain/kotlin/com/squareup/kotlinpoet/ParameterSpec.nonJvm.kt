package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmExecutableElement
import com.squareup.kotlinpoet.jvm.alias.JvmVariableElement

@JvmTypeAliasKotlinPoetApi
internal actual fun doParametersOf(method: JvmExecutableElement): List<ParameterSpec> =
  emptyList()

@JvmTypeAliasKotlinPoetApi
internal actual fun doGet(element: JvmVariableElement): ParameterSpec =
  throw UnsupportedOperationException()
