package com.squareup.kotlinpoet

internal actual fun formatNumericValue(o: Number): Any? {
  val isFloat = !js("Number").isInteger(o).unsafeCast<Boolean>()

  val options: NumberFormatOptions = js("{}").unsafeCast<NumberFormatOptions>().apply {
    style = "decimal"
    maximumFractionDigits = 100
    useGrouping = true
  }

  val format = js("Intl").NumberFormat("en-US", options).format(o)
    .unsafeCast<String>().replace(',', '_')

  if (isFloat && '.' !in format) {
    return "$format.0"
  }

  return format
}

internal external interface NumberFormatOptions {
  var style: String
  var maximumFractionDigits: Int
  var useGrouping: Boolean
}

