package com.squareup.kotlinpoet

import kotlin.text.codePointAt as codePointAtKt

internal actual fun String.codePointAt(index: Int): CodePoint =
  CodePoint(codePointAtKt(index))

internal actual fun CodePoint.isLowerCase(): Boolean =
  Character.isLowerCase(code)

internal actual fun CodePoint.isUpperCase(): Boolean =
  Character.isUpperCase(code)
