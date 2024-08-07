@file:JvmName("JvmClasses")
@file:JvmMultifileClass

package com.squareup.kotlinpoet.jvm

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.reflect.KClass


/**
 *
 * @author ForteScarlet
 */
public expect class JvmClass<T : Any>

/**
 * Convert [JvmClass] to [KClass].
 */
public expect val <T : Any> JvmClass<T>.kotlin: KClass<T>

// /**
//  * Get [JvmClass] from [T].
//  */
// internal expect val <T : Any> T.jvmClass: JvmClass<T>
