/*
 * Copyright (C) 2024 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.kotlinpoet

import kotlin.reflect.KClass
import kotlin.sequences.toCollection

internal actual fun <K, V> Map<K, V>.toImmutableMap(): Map<K, V> =
  toMap()

internal actual fun <T> Collection<T>.toImmutableList(): List<T> =
  toList()

internal actual fun <T> Collection<T>.toImmutableSet(): Set<T> =
  toSet()

internal actual inline fun <reified E : Enum<E>> enumSetOf(vararg values: E): MutableSet<E> {
  return values.toMutableSet()
}

internal actual fun <T : Comparable<T>> Sequence<T>.toSortedSet(): Set<T> =
  toCollection(linkedSetOf())

internal actual fun <T : Comparable<T>> List<T>.toSortedSet(): Set<T> =
  toCollection(linkedSetOf())

internal actual fun <T : Comparable<T>> sortedSetOf(): MutableSet<T> = linkedSetOf()

internal actual fun KClass<*>.enclosingClass(): KClass<*>? =
  null
