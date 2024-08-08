package com.squareup.kotlinpoet

import kotlin.reflect.KClass

/** [KClass] for [kotlin.jvm.Synchronized] */
public actual val SynchronizedClass: KClass<out Annotation>?
  get() = null
