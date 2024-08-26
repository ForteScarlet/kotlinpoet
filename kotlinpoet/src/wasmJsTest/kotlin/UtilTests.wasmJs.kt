internal actual fun initRegexMatcher(): (String) -> Boolean {
  // val regExp = createRegExp(REGEX_PATTERN)
  val regExp = RegExp(REGEX_PATTERN, "gu")
  return f@{ input ->
    regExp.reset()
    val match = regExp.exec(input)
    match != null && match.index == 0 && regExp.lastIndex == input.length
    // regExpReset(regExp)
    // val match = regExpExec(regExp, input) ?: return@f false
    // regExpMatchCheck(regExp, match, input)
  }
}

private fun createRegExp(pattern: String): JsAny =
  js("new RegExp(pattern, 'gu')")

private fun regExpReset(regExp: JsAny) {
  js("regExp.lastIndex = 0")
}

private fun regExpExec(regExp: JsAny, input: String): JsAny? =
  js("regExp.exec(input)")

private fun regExpMatchCheck(
  regExp: JsAny,
  regExpMatch: JsAny,
  input: String,
): Boolean =
  js("regExpMatch.index == 0 && regExp.lastIndex == input.length")


external class RegExp(pattern: String, flags: String? = definedExternally) {
  fun test(str: String): Boolean
  fun exec(str: String): RegExpMatch?
  var lastIndex: Int
}
external interface RegExpMatch {
  val index: Int
  val input: String
  val length: Int
}

fun RegExp.reset() {
  lastIndex = 0
}
