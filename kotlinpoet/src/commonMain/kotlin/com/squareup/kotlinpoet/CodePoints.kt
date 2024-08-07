package com.squareup.kotlinpoet

import kotlin.jvm.JvmInline

@JvmInline
internal value class CodePoint(val code: Int)

internal expect fun String.codePointAt(index: Int): CodePoint

internal expect fun CodePoint.isLowerCase(): Boolean
internal expect fun CodePoint.isUpperCase(): Boolean
