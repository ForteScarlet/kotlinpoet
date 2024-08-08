package com.squareup.kotlinpoet

internal actual fun String.codePointAt(index: Int): CodePoint {
  val str = this
  val code = jsCodePointAt(str, index)
  return CodePoint(code)
}

// TODO CodePoint.isLowerCase
internal actual fun CodePoint.isLowerCase(): Boolean {
  val code = this.code
  val str = jsFromCodePoint(code)

  if (str.length != 1) {
    return false
  }

  return str.first().isLowerCase()
}

// TODO CodePoint.isUpperCase
internal actual fun CodePoint.isUpperCase(): Boolean {
  val code = this.code
  val str = jsFromCodePoint(code)

  if (str.length != 1) {
    return false
  }

  return str.first().isUpperCase()
}

@Suppress("UNUSED_PARAMETER")
private fun jsCodePointAt(str: String, index: Int): Int =
  js("str.codePointAt(index)")

@Suppress("UNUSED_PARAMETER")
private fun jsFromCodePoint(code: Int): String =
  js("String.fromCodePoint(code)")

internal actual fun CodePoint.isJavaIdentifierStart(): Boolean =
  // TODO How check Java identifier start?
  false

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean =
  // TODO How check Java identifier part?
  false

internal actual fun CodePoint.charCount(): Int {
  return if (code >= 0x010000) 2 else 1
  // return jsFromCodePoint(code).length
}
