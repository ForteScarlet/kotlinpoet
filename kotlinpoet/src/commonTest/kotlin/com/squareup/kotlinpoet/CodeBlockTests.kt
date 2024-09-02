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

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@JvmInline
private value class AssertThat(val value: Any?) {
  fun isEqualTo(other: Any) = assertEquals(other, value)
  fun isNull() = assertNull(value)
}

private fun assertThat(value: Any?): AssertThat = AssertThat(value)

class CodeBlockTests {
  @Test fun equalsAndHashCode() {
    var a = CodeBlock.builder().build()
    var b = CodeBlock.builder().build()
    assertEquals(a, b)
    assertEquals(b.hashCode(), a.hashCode())
    a = CodeBlock.builder().add("%L", "taco").build()
    b = CodeBlock.builder().add("%L", "taco").build()
    assertEquals(a, b)
    assertEquals(b.hashCode(), a.hashCode())
  }

  @Test fun initPackageEquals() {
    // TODO
    val np = initNoPackage()
    // assertNotSame("", initNoPackage())
    // assertSame(np, np)
    // assertEquals("", np)
    val className = ClassName("", "Hello")
    println("create className")
    println("create className: ${className.toString()}")
    // assertEquals(
    //   ClassName(initNoPackage(), "Hello"),
    //   ClassName("", "Hello"),
    // )
  }

  @Test fun of() {
    val a = CodeBlock.of("%L taco", "delicious")
    assertEquals("delicious taco", a.toString())
  }

  @Test fun doublePrecision() {
    val doubles = listOf(
      12345678900000.0 to "12_345_678_900_000.0",
      12345678900000.07 to "12_345_678_900_000.07",
      123456.0 to "123_456.0",
      1234.5678 to "1_234.5678",
      12.345678 to "12.345678",
      0.12345678 to "0.12345678",
      0.0001 to "0.0001",
      0.00001 to "0.00001",
      0.000001 to "0.000001",
      0.0000001 to "0.0000001",
    )
    for ((d, expected) in doubles) {
      val a = CodeBlock.of("number %L", d)
      assertEquals("number $expected", a.toString())
    }
  }

  @Test fun floatPrecision() {
    val floats = listOf(
      12345678.0f to "12_345_678.0",
      123456.0f to "123_456.0",
      1234.567f to "1_234.567",
      12.34567f to "12.34567",
      0.1234567f to "0.1234567",
      0.0001f to "0.0001",
      0.00001f to "0.00001",
      0.000001f to "0.000001",
      0.0000001f to "0.0000001",
    )
    for ((f, expected) in floats) {
      val a = CodeBlock.of("number %L", f)
      assertEquals("number $expected", a.toString())
    }
  }

  @Test fun percentEscapeCannotBeIndexed() {
    val err = assertFails {
      CodeBlock.builder().add("%1%", "taco").build()
    }
    assertIs<IllegalArgumentException>(err)
    assertEquals("%% may not have an index", err.message)
  }

  @Test fun nameFormatCanBeIndexed() {
    val block = CodeBlock.builder().add("%1N", "taco").build()
    assertEquals("taco", block.toString())
  }

  @Test fun literalFormatCanBeIndexed() {
    val block = CodeBlock.builder().add("%1L", "taco").build()
    assertEquals("taco", block.toString())
  }

  @Test fun stringFormatCanBeIndexed() {
    val block = CodeBlock.builder().add("%1S", "taco").build()
    assertEquals("\"taco\"", block.toString())
  }

  @Test fun typeFormatCanBeIndexed() {
    val block = CodeBlock.builder().add("%1T", String::class).build()
    assertEquals("kotlin.String", block.toString())
  }

  @Test fun simpleNamedArgument() {
    val map = LinkedHashMap<String, Any>()
    map["text"] = "taco"
    val block = CodeBlock.builder().addNamed("%text:S", map).build()
    assertEquals("\"taco\"", block.toString())
  }

  @Test fun repeatedNamedArgument() {
    val map = LinkedHashMap<String, Any>()
    map["text"] = "tacos"
    val block = CodeBlock.builder()
      .addNamed("\"I like \" + %text:S + \". Do you like \" + %text:S + \"?\"", map)
      .build()
    assertEquals("\"I like \" + \"tacos\" + \". Do you like \" + \"tacos\" + \"?\"", block.toString())
  }

  @Test fun namedAndNoArgFormat() {
    val map = LinkedHashMap<String, Any>()
    map["text"] = "tacos"
    val block = CodeBlock.builder()
      .addNamed("⇥\n%text:L for\n⇤%%3.50", map).build()
    assertEquals("\n  tacos for\n%3.50", block.toString())
  }

  // @Test fun missingNamedArgument() {
  //   assertThrows<IllegalArgumentException> {
  //     val map = LinkedHashMap<String, Any>()
  //     CodeBlock.builder().addNamed("%text:S", map).build()
  //   }.hasMessageThat().isEqualTo("Missing named argument for %text")
  // }
  //
  // @Test fun lowerCaseNamed() {
  //   assertThrows<IllegalArgumentException> {
  //     val map = LinkedHashMap<String, Any>()
  //     map["Text"] = "tacos"
  //     CodeBlock.builder().addNamed("%Text:S", map).build()
  //   }.hasMessageThat().isEqualTo("argument 'Text' must start with a lowercase character")
  // }

  // @Test fun multipleNamedArguments() {
  //   val map = LinkedHashMap<String, Any>()
  //   map["pipe"] = System::class
  //   map["text"] = "tacos"
  //
  //   val block = CodeBlock.builder()
  //     .addNamed("%pipe:T.out.println(\"Let's eat some %text:L\");", map)
  //     .build()
  //
  //   assertThat(block.toString()).isEqualTo(
  //     "java.lang.System.out.println(\"Let's eat some tacos\");",
  //   )
  // }

  @Test fun namedNewline() {
    val map = LinkedHashMap<String, Any>()
    map["clazz"] = Int::class
    val block = CodeBlock.builder().addNamed("%clazz:T\n", map).build()
    assertEquals("kotlin.Int\n", block.toString())
  }

  // @Test fun danglingNamed() {
  //   val map = LinkedHashMap<String, Any>()
  //   map["clazz"] = Int::class
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().addNamed("%clazz:T%", map).build()
  //   }.hasMessageThat().isEqualTo("dangling % at end")
  // }
  //
  // @Test fun indexTooHigh() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%2T", String::class).build()
  //   }.hasMessageThat().isEqualTo("index 2 for '%2T' not in range (received 1 arguments)")
  // }
  //
  // @Test fun indexIsZero() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%0T", String::class).build()
  //   }.hasMessageThat().isEqualTo("index 0 for '%0T' not in range (received 1 arguments)")
  // }
  //
  // @Test fun indexIsNegative() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%-1T", String::class).build()
  //   }.hasMessageThat().isEqualTo("invalid format string: '%-1T'")
  // }
  //
  // @Test fun indexWithoutFormatType() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%1", String::class).build()
  //   }.hasMessageThat().isEqualTo("dangling format characters in '%1'")
  // }
  //
  // @Test fun indexWithoutFormatTypeNotAtStringEnd() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%1 taco", String::class).build()
  //   }.hasMessageThat().isEqualTo("invalid format string: '%1 taco'")
  // }
  //
  // @Test fun indexButNoArguments() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%1T").build()
  //   }.hasMessageThat().isEqualTo("index 1 for '%1T' not in range (received 0 arguments)")
  // }
  //
  // @Test fun formatIndicatorAlone() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("%", String::class).build()
  //   }.hasMessageThat().isEqualTo("dangling format characters in '%'")
  // }
  //
  // @Test fun formatIndicatorWithoutIndexOrFormatType() {
  //   assertThrows<IllegalArgumentException> {
  //     CodeBlock.builder().add("% tacoString", String::class).build()
  //   }.hasMessageThat().isEqualTo("invalid format string: '% tacoString'")
  // }
  // @Test fun sameIndexCanBeUsedWithDifferentFormats() {
  //   val block = CodeBlock.builder()
  //     .add("%1T.out.println(%1S)", System::class.asClassName())
  //     .build()
  //   assertEquals("java.lang.System.out.println(\"java.lang.System\")", block.toString())
  // }

  // @Test fun tooManyStatementEnters() {
  //   val codeBlock = CodeBlock.builder()
  //     .addStatement("print(«%L»)", "1 + 1")
  //     .build()
  //   assertThrows<IllegalStateException> {
  //     // We can't report this error until rendering type because code blocks might be composed.
  //     codeBlock.toString()
  //   }.hasMessageThat().isEqualTo(
  //     """
  //     |Can't open a new statement until the current statement is closed (opening « followed
  //     |by another « without a closing »).
  //     |Current code block:
  //     |- Format parts: [«, print(, «, %L, », ), \n, »]
  //     |- Arguments: [1 + 1]
  //     |
  //     """.trimMargin(),
  //   )
  // }

  // @Test fun statementExitWithoutStatementEnter() {
  //   val codeBlock = CodeBlock.builder()
  //     .addStatement("print(%L»)", "1 + 1")
  //     .build()
  //   assertThrows<IllegalStateException> {
  //     // We can't report this error until rendering type because code blocks might be composed.
  //     codeBlock.toString()
  //   }.hasMessageThat().isEqualTo(
  //     """
  //     |Can't close a statement that hasn't been opened (closing » is not preceded by an
  //     |opening «).
  //     |Current code block:
  //     |- Format parts: [«, print(, %L, », ), \n, »]
  //     |- Arguments: [1 + 1]
  //     |
  //     """.trimMargin(),
  //   )
  // }

  @Test fun nullableType() {
    val type = String::class.asTypeName().copy(nullable = true)
    val typeBlock = CodeBlock.of("%T", type)
    assertEquals("kotlin.String?", typeBlock.toString())

    val list = (List::class.asClassName().copy(nullable = true) as ClassName)
      .parameterizedBy(Int::class.asTypeName().copy(nullable = true))
      .copy(nullable = true)
    val listBlock = CodeBlock.of("%T", list)
    assertEquals("kotlin.collections.List<kotlin.Int?>?", listBlock.toString())

    val map = (Map::class.asClassName().copy(nullable = true) as ClassName)
      .parameterizedBy(String::class.asTypeName().copy(nullable = true), list)
      .copy(nullable = true)
    val mapBlock = CodeBlock.of("%T", map)
    assertThat(mapBlock.toString())
      .isEqualTo("kotlin.collections.Map<kotlin.String?, kotlin.collections.List<kotlin.Int?>?>?")

    val rarr = WildcardTypeName.producerOf(String::class.asTypeName().copy(nullable = true))
    val rarrBlock = CodeBlock.of("%T", rarr)
    assertEquals("out kotlin.String?", rarrBlock.toString())
  }

  @Test fun withoutPrefixMatching() {
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("")),
    )
      .isEqualTo(CodeBlock.of("abcd %S efgh %S ijkl", "x", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("ab")),
    )
      .isEqualTo(CodeBlock.of("cd %S efgh %S ijkl", "x", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd ")),
    )
      .isEqualTo(CodeBlock.of("%S efgh %S ijkl", "x", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S", "x")),
    )
      .isEqualTo(CodeBlock.of(" efgh %S ijkl", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S ef", "x")),
    )
      .isEqualTo(CodeBlock.of("gh %S ijkl", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh ", "x")),
    )
      .isEqualTo(CodeBlock.of("%S ijkl", "y"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S", "x", "y")),
    )
      .isEqualTo(CodeBlock.of(" ijkl"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ij", "x", "y")),
    )
      .isEqualTo(CodeBlock.of("kl"))
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")),
    )
      .isEqualTo(CodeBlock.of(""))
  }

  @Test fun withoutPrefixNoArgs() {
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("")),
    )
      .isEqualTo(CodeBlock.of("abcd %% efgh %% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("ab")),
    )
      .isEqualTo(CodeBlock.of("cd %% efgh %% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd ")),
    )
      .isEqualTo(CodeBlock.of("%% efgh %% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %%")),
    )
      .isEqualTo(CodeBlock.of(" efgh %% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %% ef")),
    )
      .isEqualTo(CodeBlock.of("gh %% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %% efgh ")),
    )
      .isEqualTo(CodeBlock.of("%% ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %% efgh %%")),
    )
      .isEqualTo(CodeBlock.of(" ijkl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %% efgh %% ij")),
    )
      .isEqualTo(CodeBlock.of("kl"))
    assertThat(
      CodeBlock.of("abcd %% efgh %% ijkl")
        .withoutPrefix(CodeBlock.of("abcd %% efgh %% ijkl")),
    )
      .isEqualTo(CodeBlock.of(""))
  }

  @Test fun withoutPrefixArgMismatch() {
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ij", "x", "z")),
    )
      .isNull()
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ij", "z", "y")),
    )
      .isNull()
  }

  @Test fun withoutPrefixFormatPartMismatch() {
    assertThat(
      CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgx %S ij", "x", "y")),
    )
      .isNull()
    assertThat(
      CodeBlock.of("abcd %S efgh %% ijkl", "x")
        .withoutPrefix(CodeBlock.of("abcd %% efgh %S ij", "x")),
    )
      .isNull()
  }

  @Test fun withoutPrefixTooShort() {
    assertThat(
      CodeBlock.of("abcd %S efgh %S", "x", "y")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")),
    )
      .isNull()
    assertThat(
      CodeBlock.of("abcd %S efgh", "x")
        .withoutPrefix(CodeBlock.of("abcd %S efgh %S ijkl", "x", "y")),
    )
      .isNull()
  }

  @Test fun trimEmpty() {
    assertThat(CodeBlock.of("").trim())
      .isEqualTo(CodeBlock.of(""))
  }

  @Test fun trimNoPlaceholders() {
    assertThat(CodeBlock.of("return null").trim())
      .isEqualTo(CodeBlock.of("return null"))
  }

  @Test fun trimPlaceholdersWithArgs() {
    assertThat(CodeBlock.of("return %S", "taco").trim())
      .isEqualTo(CodeBlock.of("return %S", "taco"))
  }

  @Test fun trimNoArgPlaceholderMiddle() {
    assertThat(CodeBlock.of("this.taco = %S", "taco").trim())
      .isEqualTo(CodeBlock.of("this.taco = %S", "taco"))
  }

  @Test fun trimNoArgPlaceholderStart() {
    assertThat(CodeBlock.of("⇥return ").trim())
      .isEqualTo(CodeBlock.of("return "))
  }

  @Test fun trimNoArgPlaceholderEnd() {
    assertThat(CodeBlock.of("return ⇥").trim())
      .isEqualTo(CodeBlock.of("return "))
  }

  @Test fun trimNoArgPlaceholdersStartEnd() {
    assertThat(CodeBlock.of("«return this»").trim())
      .isEqualTo(CodeBlock.of("return this"))
  }

  @Test fun trimMultipleNoArgPlaceholders() {
    assertThat(
      CodeBlock.of("«return if (x > %L) %S else %S»", 1, "a", "b").trim(),
    )
      .isEqualTo(CodeBlock.of("return if (x > %L) %S else %S", 1, "a", "b"))
  }

  @Test fun trimOnlyNoArgPlaceholders() {
    assertThat(CodeBlock.of("«»⇥⇤").trim())
      .isEqualTo(CodeBlock.of(""))
  }

  @Test fun replaceSimple() {
    assertThat(CodeBlock.of("%%⇥%%").replaceAll("%%", ""))
      .isEqualTo(CodeBlock.of("⇥"))
  }

  @Test fun replaceNoMatches() {
    assertThat(CodeBlock.of("%%⇥%%").replaceAll("⇤", ""))
      .isEqualTo(CodeBlock.of("%%⇥%%"))
  }

  // @Test fun replaceRegex() {
  //   assertThat(CodeBlock.of("%%⇥%%⇤").replaceAll("[⇥|⇤]", ""))
  //     .isEqualTo(CodeBlock.of("%%%%"))
  // }

  @Test fun joinToCode() {
    val blocks = listOf(CodeBlock.of("%L", "taco1"), CodeBlock.of("%L", "taco2"), CodeBlock.of("%L", "taco3"))
    assertThat(blocks.joinToCode(prefix = "(", suffix = ")"))
      .isEqualTo(CodeBlock.of("(%L, %L, %L)", "taco1", "taco2", "taco3"))
  }

  @Test fun joinToCodeTransform() {
    val blocks = listOf("taco1", "taco2", "taco3")
    assertThat(blocks.joinToCode { CodeBlock.of("%S", it) })
      .isEqualTo(CodeBlock.of("%S, %S, %S", "taco1", "taco2", "taco3"))
  }

  @Test fun beginControlFlowWithParams() {
    val controlFlow = CodeBlock.builder()
      .beginControlFlow("list.forEach { element ->")
      .addStatement("println(element)")
      .endControlFlow()
      .build()
    assertThat(controlFlow.toString()).isEqualTo(
      """
      |list.forEach { element ->
      |  println(element)
      |}
      |
      """.trimMargin(),
    )
  }

  @Test fun beginControlFlowWithParamsAndTemplateString() {
    val controlFlow = CodeBlock.builder()
      .beginControlFlow("listOf(\"\${1.toString()}\").forEach { element ->")
      .addStatement("println(element)")
      .endControlFlow()
      .build()
    assertThat(controlFlow.toString()).isEqualTo(
      """
      |listOf("${'$'}{1.toString()}").forEach { element ->
      |  println(element)
      |}
      |
      """.trimMargin(),
    )
  }

  @Test fun buildCodeBlock() {
    val codeBlock = buildCodeBlock {
      beginControlFlow("if (2 == 2)")
      addStatement("println(%S)", "foo")
      nextControlFlow("else")
      addStatement("println(%S)", "bar")
      endControlFlow()
    }
    assertThat(codeBlock.toString()).isEqualTo(
      """
      |if (2 == 2) {
      |  println("foo")
      |} else {
      |  println("bar")
      |}
      |
      """.trimMargin(),
    )
  }

  @Test fun nonWrappingControlFlow() {
    val file = FileSpec.builder("com.squareup.tacos", "Test")
      .addFunction(
        FunSpec.builder("test")
          .beginControlFlow("if (%1S == %1S)", "Very long string that would wrap the line ")
          .nextControlFlow("else if (%1S == %1S)", "Long string that would wrap the line 2 ")
          .endControlFlow()
          .build(),
      )
      .build()
    assertThat(file.toString()).isEqualTo(
      """
      |package com.squareup.tacos
      |
      |public fun test() {
      |  if ("Very long string that would wrap the line " ==
      |      "Very long string that would wrap the line ") {
      |  } else if ("Long string that would wrap the line 2 " ==
      |      "Long string that would wrap the line 2 ") {
      |  }
      |}
      |
      """.trimMargin(),
    )
  }

  @Test fun ensureEndsWithNewLineWithNoArgs() {
    val codeBlock = CodeBlock.builder()
      .addStatement("Modeling a kdoc")
      .add("\n")
      .addStatement("Statement with no args")
      .build()

    assertThat(codeBlock.ensureEndsWithNewLine().toString()).isEqualTo(
      """
      |Modeling a kdoc
      |
      |Statement with no args
      |
      """.trimMargin(),
    )
  }

  @Test fun `N_escapes_keywords`() {
    val funSpec = FunSpec.builder("object").build()
    assertEquals("`object`", CodeBlock.of("%N", funSpec).toString())
  }

  @Test fun `N_escapes_spaces`() {
    val funSpec = FunSpec.builder("create taco").build()
    assertEquals("`create taco`", CodeBlock.of("%N", funSpec).toString())
  }

  @Test fun clear() {
    val blockBuilder = CodeBlock.builder().addStatement("%S is some existing code", "This")

    blockBuilder.clear()

    assertTrue(blockBuilder.build().toString().isEmpty())
  }

  @Test fun withIndent() {
    val codeBlock = CodeBlock.Builder()
      .apply {
        addStatement("User(")
        withIndent {
          addStatement("age = 42,")
          addStatement("cities = listOf(")
          withIndent {
            addStatement("%S,", "Berlin")
            addStatement("%S,", "London")
          }
          addStatement(")")
        }
        addStatement(")")
      }
      .build()

    assertThat(codeBlock.toString()).isEqualTo(
      """
      |User(
      |  age = 42,
      |  cities = listOf(
      |    "Berlin",
      |    "London",
      |  )
      |)
      |
      """.trimMargin(),
    )
  }

  // https://github.com/square/kotlinpoet/issues/1236
  @Test fun dontEscapeBackslashesInRawStrings() {
    // println("ESCAPE '\\'") -> ESCAPE '\'
    assertEquals("\"ESCAPE '\\\\'\"", CodeBlock.of("%S", "ESCAPE '\\'").toString())
    // println("""ESCAPE '\'""") -> ESCAPE '\'
    assertEquals("\"\"\"ESCAPE '\\'\"\"\"", CodeBlock.of("%P", """ESCAPE '\'""").toString())
  }

  // https://github.com/square/kotlinpoet/issues/1381
  @Test fun useUnderscoresOnLargeDecimalLiterals() {
    assertEquals("10_000", CodeBlock.of("%L", 10000).toString())
    assertEquals("100_000", CodeBlock.of("%L", 100000L).toString())
    assertEquals("-2_147_483_648", CodeBlock.of("%L", Int.MIN_VALUE).toString())
    assertEquals("2_147_483_647", CodeBlock.of("%L", Int.MAX_VALUE).toString())
    assertEquals("-9_223_372_036_854_775_808", CodeBlock.of("%L", Long.MIN_VALUE).toString())
    assertEquals("10_000.123", CodeBlock.of("%L", 10000.123).toString())
    assertEquals("3.0", CodeBlock.of("%L", 3.0).toString())
    assertEquals("10_000.123", CodeBlock.of("%L", 10000.123f).toString())
    assertEquals("10_000.123456789011", CodeBlock.of("%L", 10000.123456789012).toString())
    assertEquals("1_281", CodeBlock.of("%L", 1281.toShort()).toString())

    assertEquals("\"10000\"", CodeBlock.of("%S", 10000).toString())
    assertEquals("\"100000\"", CodeBlock.of("%S", 100000L).toString())
    assertEquals("\"-2147483648\"", CodeBlock.of("%S", Int.MIN_VALUE).toString())
    assertEquals("\"2147483647\"", CodeBlock.of("%S", Int.MAX_VALUE).toString())
    assertEquals("\"-9223372036854775808\"", CodeBlock.of("%S", Long.MIN_VALUE).toString())
    assertEquals("\"10000.123\"", CodeBlock.of("%S", 10000.123).toString())
    assertEquals("\"3.0\"", CodeBlock.of("%S", 3.0).toString())
    assertEquals("\"10000.123\"", CodeBlock.of("%S", 10000.123f).toString())
    assertEquals("\"10000.12345678901\"", CodeBlock.of("%S", 10000.12345678901).toString())
    assertEquals("\"1281\"", CodeBlock.of("%S", 1281.toShort()).toString())
  }

  // https://github.com/square/kotlinpoet/issues/1657
  // @Test fun minusSignInSwedishLocale() {
  //   val defaultLocale = Locale.getDefault()
  //   Locale.setDefault(Locale.forLanguageTag("sv"))
  //
  //   val i = -42
  //   val s = CodeBlock.of("val i = %L", i)
  //   assertEquals("val i = -42", s.toString())
  //
  //   Locale.setDefault(defaultLocale)
  // }
}
