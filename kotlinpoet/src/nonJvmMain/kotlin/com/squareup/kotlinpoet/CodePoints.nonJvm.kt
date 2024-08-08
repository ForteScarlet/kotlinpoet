package com.squareup.kotlinpoet

/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
internal actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder {
  // Copied from StringBuilder.kt,
  // TODO Is this correct?
  val code = codePoint.code
  if (code <= Char.MAX_VALUE.code) {
    append(code.toChar())
  } else {
    append(Char.MIN_HIGH_SURROGATE + ((code - 0x10000) shr 10))
    append(Char.MIN_LOW_SURROGATE + (code and 0x3ff))
  }
  return this
}
