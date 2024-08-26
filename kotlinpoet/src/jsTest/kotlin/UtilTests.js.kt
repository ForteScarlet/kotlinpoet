internal actual fun initRegexMatcher(): (String) -> Boolean =
  REGEX_PATTERN.toRegex()::matches
