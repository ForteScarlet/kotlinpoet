package com.squareup.kotlinpoet

import kotlin.reflect.KClass

/** [KClass] for [kotlin.jvm.Synchronized] */
@Suppress("DEPRECATION", "unused")
internal actual val SynchronizedClass: KClass<out Annotation>?
  get() = kotlin.jvm.Synchronized::class
