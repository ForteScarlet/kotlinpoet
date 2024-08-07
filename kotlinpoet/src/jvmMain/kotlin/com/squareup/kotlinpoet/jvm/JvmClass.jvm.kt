@file:JvmName("JvmClasses")
@file:JvmMultifileClass

package com.squareup.kotlinpoet.jvm

import kotlin.reflect.KClass
import kotlin.jvm.kotlin as kJvmKotlin
import com.sun.tools.javac.resources.javac


/**
 * Typealias [JvmClass] to [Class].
 *
 * @author ForteScarlet
 */
public actual typealias JvmClass<T> = Class<T>

public actual val <T : Any> JvmClass<T>.kotlin: KClass<T>
  get() = kJvmKotlin

// /**
//  * Get [JvmClass] from [T].
//  */
// internal actual val <T : Any> T.jvmClass: JvmClass<T>
//   get() = javaClass
