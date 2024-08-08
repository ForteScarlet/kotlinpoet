@file:JvmName("JvmClasses")
@file:JvmMultifileClass

package com.squareup.kotlinpoet.jvm.alias

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

public expect interface JvmType

public expect fun JvmType.typeName(): String

/**
 *
 * @author ForteScarlet
 */
public expect class JvmClass<T : Any> : JvmType

/**
 * Convert [JvmClass] to [KClass].
 */
public expect val <T : Any> JvmClass<T>.kotlin: KClass<T>

