package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmAnnotationMirror


internal actual fun resolveEnumValueCodeBlock(value: Enum<*>): CodeBlock =
  CodeBlock.of("%T.%L", value::class, value.name)

internal actual fun doGet(
  annotation: Annotation,
  includeDefaultValues: Boolean,
): AnnotationSpec {
  throw UnsupportedOperationException()
}

@JvmTypeAliasKotlinPoetApi
internal actual fun doGet(annotation: JvmAnnotationMirror): AnnotationSpec =
  throw UnsupportedOperationException()
