package com.squareup.kotlinpoet

internal actual fun String.codePointAt(index: Int): CodePoint {
  @Suppress("UNUSED_VARIABLE") val str = this
  val code = js("str.codePointAt(index)").unsafeCast<Int>()
  return CodePoint(code)
}


// TODO CodePoint.isLowerCase
internal actual fun CodePoint.isLowerCase(): Boolean {
  @Suppress("UNUSED_VARIABLE")
  val code = this.code
  val str = js("String.fromCodePoint(code)").toString()

  if (str.length != 1) {
    return false
  }

  return str.first().isLowerCase()
}


// TODO CodePoint.isUpperCase
internal actual fun CodePoint.isUpperCase(): Boolean {
  @Suppress("UNUSED_VARIABLE")
  val code = this.code
  val str = js("String.fromCodePoint(code)").toString()

  if (str.length != 1) {
    return false
  }

  return str.first().isUpperCase()
}

internal actual fun CodePoint.isJavaIdentifierStart(): Boolean =
  // TODO How check Java identifier start?
  false

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean =
  // TODO How check Java identifier start?
  false

internal actual fun CodePoint.charCount(): Int {
  return if (code >= 0x010000) 2 else 1
}
