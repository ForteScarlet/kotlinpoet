package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmExecutableElement
import com.squareup.kotlinpoet.jvm.alias.JvmVariableElement

internal actual fun doParametersOf(method: JvmExecutableElement): List<ParameterSpec> =
  method.parameters.map(ParameterSpec::get)

internal actual fun doGet(element: JvmVariableElement): ParameterSpec {
  val name = element.simpleName.toString()
  val type = element.asType().asTypeName()
  return ParameterSpec.builder(name, type)
    .build()
}
