package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.JvmClass
import com.squareup.kotlinpoet.jvm.kotlin

@DelicateKotlinPoetApi(
  message = "Java reflection APIs don't give complete information on Kotlin types. Consider using" +
    " the kotlinpoet-metadata APIs instead.",
)
public actual fun JvmClass<*>.asClassName(): ClassName =
  kotlin.asClassName()
