package com.squareup.kotlinpoet

import java.util.Collections
import java.util.EnumSet
import kotlin.sequences.toSortedSet as toSortedSetKt
import kotlin.collections.toSortedSet as toSortedSetKt
import com.squareup.kotlinpoet.FileSpec.Builder
import java.util.TreeSet
import kotlin.reflect.KClass
import kotlin.sequences.toSortedSet

internal actual fun <K, V> Map<K, V>.toImmutableMap(): Map<K, V> =
  Collections.unmodifiableMap(LinkedHashMap(this))

internal actual fun <T> Collection<T>.toImmutableList(): List<T> =
  Collections.unmodifiableList(ArrayList(this))

internal actual fun <T> Collection<T>.toImmutableSet(): Set<T> =
  Collections.unmodifiableSet(LinkedHashSet(this))

internal actual fun <T : Comparable<T>> Sequence<T>.toSortedSet(): Set<T> =
  toSortedSetKt()

internal actual fun <T : Comparable<T>> List<T>.toSortedSet(): Set<T> =
  toSortedSetKt()

internal actual fun <T : Comparable<T>> sortedSetOf(): MutableSet<T> =
  TreeSet()

internal actual inline fun <reified E : Enum<E>> enumSetOf(vararg values: E): MutableSet<E> {
  return when {
    values.isEmpty() -> EnumSet.noneOf(E::class.java)
    values.size == 1 -> EnumSet.of(values.first())
    values.size == 2 -> EnumSet.of(values.first(), values[1])
    values.size == 3 -> EnumSet.of(values.first(), values[1], values[2])
    values.size == 4 -> EnumSet.of(values.first(), values[1], values[2], values[3])
    values.size == 5 -> EnumSet.of(values.first(), values[1], values[2], values[3], values[4])
    else -> EnumSet.copyOf(values.toSet())
  }
}

internal actual fun KClass<*>.enclosingClass(): KClass<*>? =
  java.enclosingClass?.kotlin
