package com.squareup.kotlinpoet

internal actual fun String.codePointAt(index: Int): CodePoint {
  val str = this
  val code = js("str.codePointAt(index)")
  return CodePoint(code)
}


internal actual fun CodePoint.isLowerCase(): Boolean {
  val code = this.code
  val str = js("String.fromCodePoint(code)").toString()

  // TODO ?
  if (str.length != 1) {
    return false
  }

  return str.first().isLowerCase()
}


internal actual fun CodePoint.isUpperCase(): Boolean {
  val code = this.code
  val str = js("String.fromCodePoint(code)").toString()

  // TODO ?
  if (str.length != 1) {
    return false
  }

  return str.first().isUpperCase()
}
