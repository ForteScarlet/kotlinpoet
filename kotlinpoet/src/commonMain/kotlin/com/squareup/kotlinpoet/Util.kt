/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.CodeBlock.Companion.isPlaceholder
import kotlin.reflect.KClass

internal object NullAppendable : Appendable {
  override fun append(charSequence: CharSequence?) = this
  override fun append(charSequence: CharSequence?, start: Int, end: Int) = this
  override fun append(c: Char) = this
}

internal expect fun <K, V> Map<K, V>.toImmutableMap(): Map<K, V>

internal expect fun <T> Collection<T>.toImmutableList(): List<T>

internal expect fun <T> Collection<T>.toImmutableSet(): Set<T>

internal inline fun <reified T : Enum<T>> Collection<T>.toEnumSet(): Set<T> =
  enumValues<T>().filterTo(mutableSetOf(), this::contains)

internal fun requireNoneOrOneOf(modifiers: Set<KModifier>, vararg mutuallyExclusive: KModifier) {
  val count = mutuallyExclusive.count(modifiers::contains)
  require(count <= 1) {
    "modifiers $modifiers must contain none or only one of ${mutuallyExclusive.contentToString()}"
  }
}

internal fun requireNoneOf(modifiers: Set<KModifier>, vararg forbidden: KModifier) {
  require(forbidden.none(modifiers::contains)) {
    "modifiers $modifiers must contain none of ${forbidden.contentToString()}"
  }
}

// 如果不使用 inline,
// 那么在 CodeBlock.builder().build() 中使用 Char.isSingleCharNoArgPlaceholder 的时候
// 会在**首次调用**时抛出
// PatternSyntaxException: No such character class
// 但出现此问题的原因未知，目前还没有尝试成功以最小单位复现，
// 但是此inline的方式似乎可以解决此问题。
internal inline fun <reified T> T.isOneOf(t1: T, t2: T, t3: T? = null, t4: T? = null, t5: T? = null, t6: T? = null) =
  this == t1 || this == t2 || this == t3 || this == t4 || this == t5 || this == t6

internal fun <T> Collection<T>.containsAnyOf(vararg t: T) = t.any(this::contains)

// see https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6
internal fun characterLiteralWithoutSingleQuotes(c: Char) = when {
  c == '\b' -> "\\b" // \u0008: backspace (BS)
  c == '\t' -> "\\t" // \u0009: horizontal tab (HT)
  c == '\n' -> "\\n" // \u000a: linefeed (LF)
  c == '\r' -> "\\r" // \u000d: carriage return (CR)
  c == '\"' -> "\"" // \u0022: double quote (")
  c == '\'' -> "\\'" // \u0027: single quote (')
  c == '\\' -> "\\\\" // \u005c: backslash (\)
  c.isIsoControl -> formatIsoControlCode(c.code)
  else -> c.toString()
}

internal expect fun formatIsoControlCode(code: Int): String

@ExperimentalStdlibApi
internal val HexFormatWithoutLeadingZeros = HexFormat {
  number {
    removeLeadingZeros = true
  }
}

internal expect fun Int.toHexStr(): String

internal fun escapeCharacterLiterals(s: String) = buildString {
  for (c in s) append(characterLiteralWithoutSingleQuotes(c))
}

private val Char.isIsoControl: Boolean
  get() {
    return this in '\u0000'..'\u001F' || this in '\u007F'..'\u009F'
  }

/** Returns the string literal representing `value`, including wrapping double quotes.  */
internal fun stringLiteralWithQuotes(
  value: String,
  isInsideRawString: Boolean = false,
  isConstantContext: Boolean = false,
): String {
  if (!isConstantContext && '\n' in value) {
    val result = StringBuilder(value.length + 32)
    result.append("\"\"\"\n|")
    var i = 0
    while (i < value.length) {
      val c = value[i]
      if (value.regionMatches(i, "\"\"\"", 0, 3)) {
        // Don't inadvertently end the raw string too early
        result.append("\"\"\${'\"'}")
        i += 2
      } else if (c == '\n') {
        // Add a '|' after newlines. This pipe will be removed by trimMargin().
        result.append("\n|")
      } else if (c == '$' && !isInsideRawString) {
        // Escape '$' symbols with ${'$'}.
        result.append("\${\'\$\'}")
      } else {
        result.append(c)
      }
      i++
    }
    // If the last-emitted character wasn't a margin '|', add a blank line. This will get removed
    // by trimMargin().
    if (!value.endsWith("\n")) result.append("\n")
    result.append("\"\"\".trimMargin()")
    return result.toString()
  } else {
    val result = StringBuilder(value.length + 32)
    // using pre-formatted strings allows us to get away with not escaping symbols that would
    // normally require escaping, e.g. "foo ${"bar"} baz"
    if (isInsideRawString) result.append("\"\"\"") else result.append('"')
    for (c in value) {
      // Trivial case: single quote must not be escaped.
      if (c == '\'') {
        result.append("'")
        continue
      }
      // Trivial case: double quotes must be escaped.
      if (c == '\"' && !isInsideRawString) {
        result.append("\\\"")
        continue
      }
      // Trivial case: $ signs must be escaped.
      if (c == '$' && !isInsideRawString) {
        result.append("\${\'\$\'}")
        continue
      }
      // Default case: just let character literal do its work.
      result.append(if (isInsideRawString) c else characterLiteralWithoutSingleQuotes(c))
      // Need to append indent after linefeed?
    }
    if (isInsideRawString) result.append("\"\"\"") else result.append('"')
    return result.toString()
  }
}

internal fun CodeBlock.ensureEndsWithNewLine() = trimTrailingNewLine('\n')

internal fun CodeBlock.trimTrailingNewLine(replaceWith: Char? = null) = if (isEmpty()) {
  this
} else {
  with(toBuilder()) {
    val lastFormatPart = trim().formatParts.last()
    if (lastFormatPart.isPlaceholder && args.isNotEmpty()) {
      val lastArg = args.last()
      if (lastArg is String) {
        val trimmedArg = lastArg.trimEnd('\n')
        args[args.size - 1] = if (replaceWith != null) {
          trimmedArg + replaceWith
        } else {
          trimmedArg
        }
      }
    } else {
      formatParts[formatParts.lastIndexOf(lastFormatPart)] = lastFormatPart.trimEnd('\n')
      if (replaceWith != null) {
        formatParts += "$replaceWith"
      }
    }
    return@with build()
  }
}

// TODO Will be crashed in wasm-js :(
//  -> PatternSyntaxException: No such character class
//  It works in JS and JVM
private val IDENTIFIER_REGEX =
  (
    "((\\p{gc=Lu}+|\\p{gc=Ll}+|\\p{gc=Lt}+|\\p{gc=Lm}+|\\p{gc=Lo}+|\\p{gc=Nl}+)+" +
      "\\d*" +
      "\\p{gc=Lu}*\\p{gc=Ll}*\\p{gc=Lt}*\\p{gc=Lm}*\\p{gc=Lo}*\\p{gc=Nl}*)" +
      "|" +
      "(`[^\n\r`]+`)"
    )
    .toRegex()

internal val String.isIdentifier get() = IDENTIFIER_REGEX.matches(this)

// https://kotlinlang.org/docs/reference/keyword-reference.html
internal val KEYWORDS = setOf(
  // Hard keywords
  "as",
  "break",
  "class",
  "continue",
  "do",
  "else",
  "false",
  "for",
  "fun",
  "if",
  "in",
  "interface",
  "is",
  "null",
  "object",
  "package",
  "return",
  "super",
  "this",
  "throw",
  "true",
  "try",
  "typealias",
  "typeof",
  "val",
  "var",
  "when",
  "while",

  // Soft keywords
  "by",
  "catch",
  "constructor",
  "delegate",
  "dynamic",
  "field",
  "file",
  "finally",
  "get",
  "import",
  "init",
  "param",
  "property",
  "receiver",
  "set",
  "setparam",
  "where",

  // Modifier keywords
  "actual",
  "abstract",
  "annotation",
  "companion",
  "const",
  "crossinline",
  "data",
  "enum",
  "expect",
  "external",
  "final",
  "infix",
  "inline",
  "inner",
  "internal",
  "lateinit",
  "noinline",
  "open",
  "operator",
  "out",
  "override",
  "private",
  "protected",
  "public",
  "reified",
  "sealed",
  "suspend",
  "tailrec",
  "value",
  "vararg",

  // These aren't keywords anymore but still break some code if unescaped. https://youtrack.jetbrains.com/issue/KT-52315
  "header",
  "impl",

  // Other reserved keywords
  "yield",
)

private const val ALLOWED_CHARACTER = '$'

private const val UNDERSCORE_CHARACTER = '_'

internal val String.isKeyword get() = this in KEYWORDS

internal val String.hasAllowedCharacters get() = this.any { it == ALLOWED_CHARACTER }

internal val String.allCharactersAreUnderscore get() = this.all { it == UNDERSCORE_CHARACTER }

// https://github.com/JetBrains/kotlin/blob/master/compiler/frontend.java/src/org/jetbrains/kotlin/resolve/jvm/checkers/JvmSimpleNameBacktickChecker.kt
private val ILLEGAL_CHARACTERS_TO_ESCAPE = setOf('.', ';', '[', ']', '/', '<', '>', ':', '\\')

private fun String.failIfEscapeInvalid() {
  require(!any { it in ILLEGAL_CHARACTERS_TO_ESCAPE }) {
    "Can't escape identifier $this because it contains illegal characters: " +
      ILLEGAL_CHARACTERS_TO_ESCAPE.intersect(this.toSet()).joinToString("")
  }
}

internal fun String.escapeIfNecessary(validate: Boolean = true): String = escapeIfNotJavaIdentifier()
  .escapeIfKeyword()
  .escapeIfHasAllowedCharacters()
  .escapeIfAllCharactersAreUnderscore()
  .apply { if (validate) failIfEscapeInvalid() }

/**
 * Because of [KT-18706](https://youtrack.jetbrains.com/issue/KT-18706)
 * bug all aliases escaped with backticks are not resolved.
 *
 * So this method is used instead, which uses custom escape rules:
 * - if all characters are underscores, add `'0'` to the end
 * - if it's a keyword, prepend it with double underscore `"__"`
 * - if first character cannot be used as identifier start (e.g. a number), underscore is prepended
 * - all `'$'` replaced with double underscore `"__"`
 * - all characters that cannot be used as identifier part (e.g. space or hyphen) are
 *   replaced with `"_U<code>"` where `code` is 4-digit Unicode character code in hexadecimal form
 */
internal fun String.escapeAsAlias(validate: Boolean = true): String {
  if (allCharactersAreUnderscore) {
    return "${this}0" // add '0' to make it a valid identifier
  }

  if (isKeyword) {
    return "__$this"
  }

  val newAlias = StringBuilder("")

  if (!first().isJavaIdentifierStart()) {
    newAlias.append('_')
  }

  for (ch in this) {
    if (ch == ALLOWED_CHARACTER) {
      newAlias.append("__") // all $ replaced with __
      continue
    }

    if (!ch.isJavaIdentifierPart()) {
      newAlias.append("_U").append(ch.code.toHexStr().padStart(4, '0'))
      continue
    }

    newAlias.append(ch)
  }

  return newAlias.toString().apply { if (validate) failIfEscapeInvalid() }
}

private fun String.alreadyEscaped() = startsWith("`") && endsWith("`")

private fun String.escapeIfKeyword() = if (isKeyword && !alreadyEscaped()) "`$this`" else this

private fun String.escapeIfHasAllowedCharacters() = if (hasAllowedCharacters && !alreadyEscaped()) "`$this`" else this

private fun String.escapeIfAllCharactersAreUnderscore() = if (allCharactersAreUnderscore && !alreadyEscaped()) "`$this`" else this

private fun String.escapeIfNotJavaIdentifier(): String {
  return if ((
      !first().isJavaIdentifierStart() ||
        drop(1).any { !it.isJavaIdentifierPart() }
      ) &&
    !alreadyEscaped()
  ) {
    "`$this`".replace(' ', '·')
  } else {
    this
  }
}

internal fun String.escapeSegmentsIfNecessary(delimiter: Char = '.') = split(delimiter)
  .filter { it.isNotEmpty() }
  .joinToString(delimiter.toString()) { it.escapeIfNecessary() }

internal expect fun <T : Comparable<T>> Sequence<T>.toSortedSet(): Set<T>
internal expect fun <T : Comparable<T>> List<T>.toSortedSet(): Set<T>
internal expect fun <T : Comparable<T>> sortedSetOf(): MutableSet<T>

internal expect inline fun <reified E : Enum<E>> enumSetOf(vararg values: E): MutableSet<E>

internal expect fun KClass<*>.enclosingClass(): KClass<*>?

internal expect fun Char.isJavaIdentifierStart(): Boolean

internal expect fun Char.isJavaIdentifierPart(): Boolean
