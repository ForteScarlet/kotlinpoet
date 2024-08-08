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
