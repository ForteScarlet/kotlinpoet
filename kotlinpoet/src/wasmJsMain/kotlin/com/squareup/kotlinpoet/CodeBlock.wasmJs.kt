package com.squareup.kotlinpoet

internal actual fun formatNumericValue(o: Number): Any? {
  val isFloat: Boolean
  val jsNumber: JsAny

  when (o) {
    is Long -> {
      isFloat = false
      jsNumber = o.toJsBigInt()
    }
    is Int, is Short, is Byte -> {
      isFloat = false
      jsNumber = o.toInt().toJsNumber()
    }

    else -> {
      jsNumber = o.toDouble().toJsNumber()
      isFloat = isInteger(jsNumber)
    }
  }

  val options = NumberFormatOptions(
    style = "decimal",
    maximumFractionDigits = 100,
    useGrouping = true,
  )

  val format = numberFormat(options, jsNumber).replace(',', '_')
  if (isFloat && '.' !in format) {
    return "$format.0"
  }

  return format
}

private fun isInteger(n: JsAny): Boolean =
  js("Number.isInteger(n)")

private fun numberFormat(options: NumberFormatOptions, n: JsAny): String =
  js("new Intl.NumberFormat('en-US', options).format(n)")

internal external interface NumberFormatOptions : JsAny {
  var style: String
  var maximumFractionDigits: Int
  var useGrouping: Boolean
}

internal fun NumberFormatOptions(
  style: String,
  maximumFractionDigits: Int,
  useGrouping: Boolean,
): NumberFormatOptions {
  js(
    "return { style: style,\n" +
      "maximumFractionDigits: maximumFractionDigits,\n" +
      "useGrouping: useGrouping }",
  )
}

