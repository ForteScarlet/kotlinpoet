@file:Suppress("unused")
package com.squareup.kotlinpoet

import kotlin.reflect.KClass

internal actual val JvmNameClass: KClass<out Annotation>?
  get() = JvmName::class
internal actual val JvmMultifileClassClass: KClass<out Annotation>?
  get() = JvmMultifileClass::class
internal actual val JvmSuppressWildcardsClass: KClass<out Annotation>?
  get() = JvmSuppressWildcards::class
internal actual val JvmInlineClass: KClass<out Annotation>?
  get() = JvmInline::class
internal actual val JvmRecordClass: KClass<out Annotation>?
  get() = JvmRecord::class
internal actual val JvmStaticClass: KClass<out Annotation>?
  get() = JvmStatic::class
internal actual val JvmOverloadsClass: KClass<out Annotation>?
  get() = JvmOverloads::class
internal actual val ThrowsClass: KClass<out Annotation>?
  get() = Throws::class
internal actual val SynchronizedClass: KClass<out Annotation>?
  get() = Synchronized::class
internal actual val StrictfpClass: KClass<out Annotation>?
  get() = Strictfp::class
internal actual val JvmFieldClass: KClass<out Annotation>?
  get() = JvmField::class
internal actual val TransientClass: KClass<out Annotation>?
  get() = Transient::class
internal actual val VolatileClass: KClass<out Annotation>?
  get() = kotlin.concurrent.Volatile::class
internal actual val JvmWildcardClass: KClass<out Annotation>?
  get() = JvmWildcard::class
